package com.slipper.unieap.ea.me.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.slipper.service.unieap.base.email.MailBO;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.bo.OrderBO;
import com.slipper.unieap.ea.me.vo.MeInfoVO;
import com.slipper.unieap.ea.myaccount.vo.AccountInfoVO;
import com.slipper.unieap.ea.myaccount.vo.TradeAutoVO;
import com.slipper.unieap.ea.pojo.TradeAuto;
import com.slipper.unieap.ea.pojo.TradeSubscribeRecord;
import com.slipper.unieap.ea.pojo.TradeUser;
import com.slipper.unieap.ea.pojo.TradeUserAccount;
import com.slipper.unieap.ea.pojo.TradeUserFee;
import com.slipper.unieap.ea.repository.TradeAutoRepository;
import com.slipper.unieap.ea.repository.TradeSubscribeRecordRepository;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.ea.repository.TradeUserFeeRepository;
import com.slipper.unieap.ea.repository.TradeUserRepository;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.vo.OrderVO;

@Service
public class MeBO extends BaseBO {

	@Autowired
	public TradeUserRepository tradeUserRepository;

	@Autowired
	public TradeUserFeeRepository tradeUserFeeRepository;

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeAutoRepository tradeAutoRepository;

	@Autowired
	public TradeSubscribeRecordRepository tradeSubscribeRecordRepository;

	@Autowired
	public MailBO mailBO;

	@Autowired
	public OrderBO orderBO;

	/**
	 * 获取用户信息,包括用户下的账户列表
	 * 
	 * @param userCode
	 * @return
	 */
	public MeInfoVO getMeInfo(String userCode) {
		MeInfoVO meInfoVO = tradeUserRepository.getUserInfo(userCode);
		List<AccountInfoVO> accountList = tradeUserAccountRepository.getAccountList(userCode);
		meInfoVO.setAccountList(accountList);
		return meInfoVO;
	}

	/**
	 * 修改账户状态
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int updateActivateFlag(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		if (StringUtils.equals(vo.getActivateFlag(), pojo.getActivateFlag())) {
			return 1;
		} else {
			pojo.setActivateFlag(vo.getActivateFlag());
			tradeUserAccountRepository.save(pojo);
			// 启用后,增加订阅信息
			if (StringUtils.equals(vo.getActivateFlag(), UnieapConstants.YES)) {
				addTradeSubscribeRecord(pojo.getAccountCode(), pojo.getSubscribeFlag());
			} else {
				//停用
				List<TradeSubscribeRecord> tradeSubscribeRecordList = tradeSubscribeRecordRepository
						.getSubscribeRecordList(pojo.getAccountCode());
				//修改最新的订阅结束时间为当前时间-1.结束订阅
				if (tradeSubscribeRecordList != null && tradeSubscribeRecordList.size() > 0) {
					for (TradeSubscribeRecord tradeSubscribeRecord : tradeSubscribeRecordList) {
						tradeSubscribeRecord.setSubscribeEndDate(com.slipper.unieap.utils.DateUtils
								.getDayEnd(DateUtils.addDays(UnieapConstants.getDateTime(), -1)));
					}
					tradeSubscribeRecordRepository.saveAll(tradeSubscribeRecordList);
				}				
			}
			updateTradeAuto(pojo);
			return 0;
		}
	}

	/**
	 * 修改策略
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateEaType(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		pojo.setEaType(vo.getEaType());
		tradeUserAccountRepository.save(pojo);
		updateTradeAuto(pojo);
	}

	/**
	 * 修改订阅
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateSubscribeFlag(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		String oldFeeType = pojo.getSubscribeFlag();
		pojo.setSubscribeFlag(vo.getSubscribeFlag());
		pojo.setSubscribeStartDate(com.slipper.unieap.utils.DateUtils.getDayStart(UnieapConstants.getDateTime()));
		if (!StringUtils.equals(vo.getSubscribeFlag(), "S0") && !StringUtils.equals(vo.getSubscribeFlag(), "S1")) {
			pojo.setAutoFlag(UnieapConstants.YES);
		} else {
			pojo.setAutoFlag(UnieapConstants.NO);
		}
		// 如果试用时间为空,增加试用时间
		if (pojo.getTrialStartDate() == null) {
			pojo.setTrialStartDate(com.slipper.unieap.utils.DateUtils.getDayStart(UnieapConstants.getDateTime()));
			pojo.setTrialEndDate(
					com.slipper.unieap.utils.DateUtils.getDayEnd(DateUtils.addDays(UnieapConstants.getDateTime(), 30)));
		}
		tradeUserAccountRepository.save(pojo);

		List<TradeUserFee> feeTypes = tradeUserFeeRepository.findByUserIdAndAccountCodeAndFeeType(pojo.getUserId(),
				pojo.getAccountCode(), oldFeeType);
		if (feeTypes == null || feeTypes.size() == 0) {
			TradeUserFee fee = new TradeUserFee();
			fee.setUserId(pojo.getUserId());
			fee.setAccountCode(pojo.getAccountCode());
			fee.setActivateFlag(UnieapConstants.YES);
			fee.setFeeType(pojo.getSubscribeFlag());
			fee.setTenantId(UnieapConstants.getTenantId());
			tradeUserFeeRepository.save(fee);
		} else {
			for (TradeUserFee fee : feeTypes) {
				fee.setFeeType(pojo.getSubscribeFlag());
			}
			tradeUserFeeRepository.saveAll(feeTypes);
		}
		updateTradeAuto(pojo);
		//增加或者修改订阅记录
		updateTradeSubscribeRecord(pojo.getAccountCode(), vo.getSubscribeFlag());

	}

	@Value("${app_lzy.eaHIP}")
	String eaHIP;
	@Value("${app_lzy.eaDIP}")
	String eaDIP;

	/**
	 * 更新托管信息
	 * 
	 * @param vo
	 */
	public void updateTradeAuto(TradeUserAccount vo) {
		TradeAutoVO tradeAuto = tradeAutoRepository.getAutoInfo(vo.getAccountCode());
		if(StringUtils.equals(vo.getActivateFlag(),UnieapConstants.NO))
		{
			//停用后删除托管信息
			if (tradeAuto != null) {
				tradeAutoRepository.deleteById(tradeAuto.getId());
			}
		}else
		{
			if (!StringUtils.equals(vo.getSubscribeFlag(), "S0") && !StringUtils.equals(vo.getSubscribeFlag(), "S1")) {
				// 订阅自动托管,如果不存在托管记录则增加托管记录
				if (tradeAuto == null) {
					//可托管的服务器地址,在每台服务器上遍历是否有可用的托管服务
					String[] climentServerList = UnieapConstants.SYS_DATA.get("ClientServerList").split(",");
					boolean isAdded = false;
					int clientTraderMaxNumber = Integer.parseInt(UnieapConstants.SYS_DATA.get("ClientTraderMaxNumber"));
					for (String clientServer : climentServerList) {
						if(isAdded) {
							break;
						}
						List<TradeAuto> tradeAutoList = tradeAutoRepository.findByExecuteIp(clientServer);
						if (tradeAutoList != null && tradeAutoList.size() > 0) {
							String formNos = "";
							for (TradeAuto auto : tradeAutoList) {
								formNos = formNos + "," + auto.getFormNo().substring("Trade".length());
							}
							for (int i = 1; i <= clientTraderMaxNumber; i++) {
								if (!formNos.contains("" + i)) {
									TradeAuto nTradeAuto = new TradeAuto();
									nTradeAuto.setAccountCode(vo.getAccountCode());
									nTradeAuto.setAutoStatus(UnieapConstants.NO);
									nTradeAuto.setExecuteIp(clientServer);
									if (StringUtils.equals(vo.getEaType(), "H")) {
										nTradeAuto.setEaIp(eaHIP.split(",")[0]);
									}
									if (StringUtils.equals(vo.getEaType(), "D")) {
										nTradeAuto.setEaIp(eaDIP.split(",")[0]);
									}
									nTradeAuto.setFormNo("Trade" + i);
									nTradeAuto.setLoginStatus(UnieapConstants.NO);
									nTradeAuto.setAutoTrade(UnieapConstants.YES);
									nTradeAuto.setActivateFlag(UnieapConstants.YES);
									nTradeAuto.setTenantId(UnieapConstants.getTenantId());
									tradeAutoRepository.save(nTradeAuto);
									isAdded = true;
									break;
								}
							}
						} else {
							TradeAuto nTradeAuto = new TradeAuto();
							nTradeAuto.setAccountCode(vo.getAccountCode());
							nTradeAuto.setAutoStatus(UnieapConstants.NO);
							nTradeAuto.setExecuteIp(clientServer);
							if (StringUtils.equals(vo.getEaType(), "H")) {
								nTradeAuto.setEaIp(eaHIP.split(",")[0]);
							}
							if (StringUtils.equals(vo.getEaType(), "D")) {
								nTradeAuto.setEaIp(eaDIP.split(",")[0]);
							}
							nTradeAuto.setFormNo("Trade1");
							nTradeAuto.setLoginStatus(UnieapConstants.NO);
							nTradeAuto.setAutoTrade(UnieapConstants.YES);
							nTradeAuto.setActivateFlag(UnieapConstants.YES);
							nTradeAuto.setTenantId(UnieapConstants.getTenantId());
							tradeAutoRepository.save(nTradeAuto);
							isAdded = true;
							break;
						}
					}
				} else {
					//修改托管策略地址
					TradeAuto eTradeAuto = tradeAutoRepository.getById(tradeAuto.getId());
					if (StringUtils.equals(vo.getEaType(), "H")) {
						eTradeAuto.setEaIp(eaHIP.split(",")[0]);
					}
					if (StringUtils.equals(vo.getEaType(), "D")) {
						eTradeAuto.setEaIp(eaDIP.split(",")[0]);
					}
					tradeAutoRepository.save(eTradeAuto);
				}
			} else {
				// 如果是试用账户或者取消托管账户,则删除托管信息
				if (tradeAuto != null) {
					tradeAutoRepository.deleteById(tradeAuto.getId());
				}
			}
		}		
	}

	/**
	 * 修改托管交易手数
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateValumn(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		pojo.setValumn(Double.parseDouble(vo.getValumn()));
		tradeUserAccountRepository.save(pojo);
	}

	/**
	 * 修改托管交易密码
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePassword(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		pojo.setPassword(vo.getPassword());
		tradeUserAccountRepository.save(pojo);
	}

	/**
	 * 修改email
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateEmail(TradeUser vo) throws Exception {
		TradeUser pojo = tradeUserRepository.findById(vo.getId()).get();
		pojo.setEmail(vo.getEmail());
		tradeUserRepository.save(pojo);
	}

	/**
	 * 修改电话号码
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updatePhoneNumber(TradeUser vo) throws Exception {
		TradeUser pojo = tradeUserRepository.findById(vo.getId()).get();
		pojo.setPhoneNumber(vo.getPhoneNumber());
		tradeUserRepository.save(pojo);
	}

	/**
	 * 修改托管交易
	 * 
	 * @param vo
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateAutoFlag(AccountInfoVO vo) throws Exception {
		TradeUserAccount pojo = tradeUserAccountRepository.findById(vo.getId()).get();
		pojo.setAutoFlag(vo.getAutoFlag());
		tradeUserAccountRepository.save(pojo);
		OrderVO changePasswordOrder = new OrderVO();
		JSONObject inputParameters = new JSONObject();
		inputParameters.put("accountCode", vo.getAccountCode());
		changePasswordOrder.setId(UnieapConstants.getSeq(null));
		// 修改MT4交易密码
		changePasswordOrder.setOrderType(UnieapConstants.ORDER_TYPE.MODIFY_MT4_PASSWORD.name());
		changePasswordOrder.setOrderStatus("N");
		changePasswordOrder.setInputParameters(inputParameters.toJSONString());

		OrderVO passwordNotifyOrder = new OrderVO();
		passwordNotifyOrder.setPendingOrderId(changePasswordOrder.getId());
		passwordNotifyOrder.setOrderType(UnieapConstants.ORDER_TYPE.SEND_PASSWORD_NOTIFY.name());
		passwordNotifyOrder.setOrderStatus("N");
		passwordNotifyOrder.setInputParameters(inputParameters.toJSONString());

		orderBO.addOrder(changePasswordOrder);
		orderBO.addOrder(passwordNotifyOrder);
	}

	/**
	 * 增加账户
	 * 
	 * @param vo
	 * @return 0：成功,3:推荐码不存在,4:账户已存在
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public int addAccount(AccountInfoVO vo) throws Exception {
		int result = checkAgentCodeExist(vo.getUserCode(), vo.getAgentCode());
		if (result != 0) {
			return result;
		}
		result = checkAccountCodeExist(vo.getAccountCode());
		if (result != 0) {
			return result;
		}
		TradeUserAccount pojo = new TradeUserAccount();
		pojo.setAccountCode(vo.getAccountCode());
		pojo.setAccountType(vo.getAccountType());
		pojo.setActivateFlag(UnieapConstants.YES);
		// pojo.setSubscribeFlag(vo.getSubscribeFlag());
		pojo.setSubscribeFlag("S0");// 新增加账户默认订阅S0
		// pojo.setEaType(vo.getEaType());
		pojo.setEaType("D");// 新增加账户默认采用D策略
		pojo.setSubscribeStartDate(UnieapConstants.getDateTime());
		pojo.setAutoFlag(UnieapConstants.YES);
		pojo.setAgentCode(vo.getAgentCode());
		pojo.setAutoFlag(UnieapConstants.NO);
		pojo.setValumn(0.1);// 新增加账户默认手数为0.1
		pojo.setBrokerCode(vo.getBrokerCode());
		pojo.setBrokerName(vo.getBrokerName());
		pojo.setPassword(vo.getPassword());
		pojo.setUserId(vo.getUserId());
		pojo.setUserCode(vo.getUserCode());
		pojo.setTrialStartDate(com.slipper.unieap.utils.DateUtils.getDayStart(UnieapConstants.getDateTime()));
		pojo.setTrialEndDate(
				com.slipper.unieap.utils.DateUtils.getDayEnd(DateUtils.addDays(UnieapConstants.getDateTime(), 30)));
		pojo.setAccountBalance(0);
		pojo.setAccountEquity(0);
		pojo.setAccountFreemargin(0);
		pojo.setAccountMargin(0);
		pojo.setEmailNotify(UnieapConstants.YES);
		pojo.setSmsNotify(UnieapConstants.YES);
		pojo.setTenantId(UnieapConstants.getTenantId());
		tradeUserAccountRepository.save(pojo);

		addTradeSubscribeRecord(vo.getAccountCode(), vo.getSubscribeFlag());

		updateTradeAuto(pojo);

		List<TradeUserFee> feeList = new ArrayList<TradeUserFee>();

		// 增加账户级费用项

		// 如果是代理商挂载在公司下,就增加额外返佣金
		if (StringUtils.equals(vo.getAgentCode(), "000000")) {
			List<TradeUserFee> userFeeList = tradeUserFeeRepository.findByUserIdAndFeeType(vo.getUserId(), "AC1");
			if (userFeeList == null || userFeeList.size() == 0) {
				TradeUserFee fee = new TradeUserFee();
				feeList.add(fee);
				fee.setUserId(pojo.getUserId());
				fee.setActivateFlag(UnieapConstants.YES);
				fee.setFeeType("AC1");
				fee.setTenantId(UnieapConstants.getTenantId());
			}
		}
		tradeUserFeeRepository.saveAll(feeList);
		return 0;
	}

	/**
	 * 检查推荐码是否存在
	 * 
	 * @param agentCode
	 * @return
	 */
	public int checkAgentCodeExist(String userCode, String agentCode) {
		// 公司推荐默认推荐码
		if (StringUtils.equals(agentCode, "000000")) {
			return 0;
		}
		MeInfoVO meInfoVO = tradeUserRepository.getUserInfoByAgentCode(agentCode);
		if (meInfoVO == null) {
			// 推荐码不存在
			return 3;
		} else {
			if (StringUtils.equals(userCode, meInfoVO.getUserCode())) {
				// 不能输入用户自己的推荐码
				return 5;
			}
		}
		return 0;
	}

	/**
	 * 检查账户是否已存在 0: 账户不存在,4:账户已存在
	 * 
	 * @param accountCode
	 * @return
	 */
	public int checkAccountCodeExist(String accountCode) {
		double num = tradeUserAccountRepository.checkAccountCodeExist(accountCode).doubleValue();
		if (num != 0) {
			// 账户已存在
			return 4;
		} else {
			return 0;
		}
	}

	/**
	 * 获取订阅套餐列表
	 * 
	 * @return
	 */
	public List<DicDataVO> getSubscribeDicDataList(String groupCode, String language) {
		List<DicDataVO> datas = tradeUserAccountRepository.getSubscribeDicDataList(groupCode, language);
		if (datas != null && datas.size() > 0) {
			return datas;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	/**
	 * 获取交易平台列表
	 * 
	 * @param groupCode
	 * @param language
	 * @return
	 */
	public List<DicDataVO> getBrokerDicDataList(String groupCode, String language) {
		List<DicDataVO> datas = tradeUserAccountRepository.getBrokerDicDataList(groupCode, language);
		if (datas != null && datas.size() > 0) {
			return datas;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	/**
	 * 增加账户订阅记录
	 * 
	 * @param accountCode
	 */
	public void addTradeSubscribeRecord(String accountCode, String subscribeFlag) {
		TradeSubscribeRecord tradeSubscribeRecord = new TradeSubscribeRecord();
		tradeSubscribeRecord.setAccountCode(accountCode);
		tradeSubscribeRecord.setActivateFlag(UnieapConstants.YES);
		tradeSubscribeRecord.setSubscribeFlag(subscribeFlag);
		tradeSubscribeRecord
				.setSubscribeStartDate(com.slipper.unieap.utils.DateUtils.getDayStart(UnieapConstants.getDateTime()));
		tradeSubscribeRecord.setTenantId(UnieapConstants.getTenantId());
		tradeSubscribeRecordRepository.save(tradeSubscribeRecord);
	}

	/**
	 * 更新订阅记录,原有记录设置订阅结束时间,并新增加订阅记录
	 * 
	 * @param accountCode
	 * @param subscribeFlag
	 */
	public void updateTradeSubscribeRecord(String accountCode, String subscribeFlag) {
		List<TradeSubscribeRecord> tradeSubscribeRecordList = tradeSubscribeRecordRepository
				.getSubscribeRecordList(accountCode);
		if (tradeSubscribeRecordList != null && tradeSubscribeRecordList.size() > 0) {
			for (TradeSubscribeRecord tradeSubscribeRecord : tradeSubscribeRecordList) {
				tradeSubscribeRecord.setSubscribeEndDate(com.slipper.unieap.utils.DateUtils
						.getDayEnd(DateUtils.addDays(UnieapConstants.getDateTime(), -1)));
			}
			tradeSubscribeRecordRepository.saveAll(tradeSubscribeRecordList);
		}
		addTradeSubscribeRecord(accountCode, subscribeFlag);
	}
}