package com.slipper.unieap.ea.job;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.common.utils.DateUtils;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.ea.myaccount.vo.AccountInfoVO;
import com.slipper.unieap.ea.mybill.vo.AccountFeeItemVO;
import com.slipper.unieap.ea.mybill.vo.BillDateInfoVO;
import com.slipper.unieap.ea.mybill.vo.BillDetailInfoVO;
import com.slipper.unieap.ea.pojo.TradeFeeItem;
import com.slipper.unieap.ea.pojo.TradeUser;
import com.slipper.unieap.ea.pojo.TradeUserBill;
import com.slipper.unieap.ea.repository.TradeFeeItemRepository;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.ea.repository.TradeUserBillRepository;
import com.slipper.unieap.ea.repository.TradeUserFeeRepository;
import com.slipper.unieap.ea.repository.TradeUserRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 生成账单任务
 */
@Service
public class EAGenerateBill extends DynamicJob {

	@Autowired
	public TradeUserRepository tradeUserRepository;

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeUserFeeRepository tradeUserFeeRepository;

	@Autowired
	public TradeFeeItemRepository tradeFeeItemRepository;

	@Autowired
	public TradeUserBillRepository tradeUserBillRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			// billRunForPostpaid();
			billRunForPrepaid();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String PREPAID = "PREPAID";

	private String POSTPAID = "POSTPAID";

	/**
	 * 预付费用户出账
	 */
	public void billRunForPrepaid() throws ParseException {
		List<TradeUser> userList = tradeUserRepository.getAllSubscribeUserList();
		if (userList != null && userList.size() > 0) {
			List<TradeUserBill> tradeUserBillList = new ArrayList<TradeUserBill>();
			for (TradeUser user : userList) {
				// 已出账的用户，不再出账，避免账单信息被覆盖
				List<String> subscribeFeeTypes = new ArrayList<String>();
				subscribeFeeTypes.add("S1");
				subscribeFeeTypes.add("S2");
				subscribeFeeTypes.add("S3");
				subscribeFeeTypes.add("S4");
				subscribeFeeTypes.add("S5");
				subscribeFeeTypes.add("S6");
				if (!checkPrepaidBillRunCompleted(user, subscribeFeeTypes)) {
					List<BillDetailInfoVO> userSubscribeFeeList = billRunPrepaidSubscribeFee(user);
					if (userSubscribeFeeList != null && userSubscribeFeeList.size() > 0) {
						for (BillDetailInfoVO bill : userSubscribeFeeList) {
							TradeUserBill tradeUserBill = new TradeUserBill();
							tradeUserBillList.add(tradeUserBill);
							tradeUserBill.setAccountCode(bill.getAccountCode());
							tradeUserBill.setActivateFlag(UnieapConstants.YES);
							tradeUserBill.setBillDate(bill.getBillDate());
							tradeUserBill.setStartDate(bill.getBillStartDate());
							tradeUserBill.setEndDate(bill.getBillEndDate());
							tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
							tradeUserBill.setFeeDesc(bill.getFeeDesc());
							tradeUserBill.setFeeType(bill.getFeeType());
							tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
							tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
							tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
							tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
							tradeUserBill.setTenantId(UnieapConstants.getTenantId());
							tradeUserBill.setUserCode(user.getUserCode());
							tradeUserBill.setUserName(user.getUserName());
						}
					}
				}
				List<String> agentCommissionFeeTypes = new ArrayList<String>();
				agentCommissionFeeTypes.add("AC");
				agentCommissionFeeTypes.add("AC1");
				agentCommissionFeeTypes.add("AC2");
				agentCommissionFeeTypes.add("AC3");
				if (!checkPrepaidBillRunCompleted(user, agentCommissionFeeTypes)) {
					List<BillDetailInfoVO> userBillList = billRunAgentPrepaidCommission(user);
					if (userBillList != null && userBillList.size() > 0) {
						for (BillDetailInfoVO bill : userBillList) {
							TradeUserBill tradeUserBill = new TradeUserBill();
							tradeUserBillList.add(tradeUserBill);
							tradeUserBill.setAccountCode(bill.getAccountCode());
							tradeUserBill.setActivateFlag(UnieapConstants.YES);
							tradeUserBill.setBillDate(bill.getBillDate());
							tradeUserBill.setEndDate(bill.getBillEndDate());
							tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
							tradeUserBill.setFeeDesc(bill.getFeeDesc());
							tradeUserBill.setFeeType(bill.getFeeType());
							tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
							tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
							tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
							tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
							tradeUserBill.setStartDate(bill.getBillStartDate());
							tradeUserBill.setTenantId(UnieapConstants.getTenantId());
							tradeUserBill.setUserCode(bill.getUserCode());
							tradeUserBill.setUserName(bill.getUserName());
						}
					}
				}
			}
			tradeUserBillRepository.saveAll(tradeUserBillList);
		}

		List<BillDetailInfoVO> revenueCommissionList = billRunRevenueCommission(PREPAID);
		if (revenueCommissionList != null && revenueCommissionList.size() > 0) {
			List<TradeUserBill> tradeUserBillList = new ArrayList<TradeUserBill>();
			for (BillDetailInfoVO bill : revenueCommissionList) {
				TradeUser user = new TradeUser();
				user.setUserCode(bill.getUserCode());
				List<String> revenueCommissionFeeTypes = new ArrayList<String>();
				revenueCommissionFeeTypes.add("TC");
				if (!checkPrepaidBillRunCompleted(user, revenueCommissionFeeTypes)) {
					TradeUserBill tradeUserBill = new TradeUserBill();
					tradeUserBillList.add(tradeUserBill);
					tradeUserBill.setAccountCode(bill.getAccountCode());
					tradeUserBill.setActivateFlag(UnieapConstants.YES);
					tradeUserBill.setBillDate(bill.getBillDate());
					tradeUserBill.setEndDate(bill.getBillEndDate());
					tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
					tradeUserBill.setFeeDesc(bill.getFeeDesc());
					tradeUserBill.setFeeType(bill.getFeeType());
					tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
					tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
					tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
					tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
					tradeUserBill.setStartDate(bill.getBillStartDate());
					tradeUserBill.setTenantId(UnieapConstants.getTenantId());
					tradeUserBill.setUserCode(bill.getUserCode());
					tradeUserBill.setUserName(bill.getUserName());
				}
			}
			tradeUserBillRepository.saveAll(tradeUserBillList);
		}
	}

	/**
	 * 后付费用户出账
	 * 
	 * @throws ParseException
	 */
	public void billRunForPostpaid() throws ParseException {
		List<TradeUser> userList = tradeUserRepository.getAllSubscribeUserList();
		if (userList != null && userList.size() > 0) {
			List<TradeUserBill> tradeUserBillList = new ArrayList<TradeUserBill>();
			for (TradeUser user : userList) {
				// 已出账的用户，不再出账，避免账单信息被覆盖
				List<String> subscribeFeeTypes = new ArrayList<String>();
				subscribeFeeTypes.add("S1");
				subscribeFeeTypes.add("S2");
				subscribeFeeTypes.add("S3");
				subscribeFeeTypes.add("S4");
				subscribeFeeTypes.add("S5");
				subscribeFeeTypes.add("S6");
				if (!checkPostpaidBillRunCompleted(user, subscribeFeeTypes)) {
					List<BillDetailInfoVO> userSubscribeFeeList = billRunPostpaidSubscribeFee(user);
					if (userSubscribeFeeList != null && userSubscribeFeeList.size() > 0) {
						for (BillDetailInfoVO bill : userSubscribeFeeList) {
							TradeUserBill tradeUserBill = new TradeUserBill();
							tradeUserBillList.add(tradeUserBill);
							tradeUserBill.setAccountCode(bill.getAccountCode());
							tradeUserBill.setActivateFlag(UnieapConstants.YES);
							tradeUserBill.setBillDate(bill.getBillDate());
							tradeUserBill.setEndDate(bill.getBillEndDate());
							tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
							tradeUserBill.setFeeDesc(bill.getFeeDesc());
							tradeUserBill.setFeeType(bill.getFeeType());
							tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
							tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
							tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
							tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
							tradeUserBill.setStartDate(bill.getBillStartDate());
							tradeUserBill.setTenantId(UnieapConstants.getTenantId());
							tradeUserBill.setUserCode(user.getUserCode());
							tradeUserBill.setUserName(user.getUserName());
						}
					}
				}

				List<String> agentCommissionFeeTypes = new ArrayList<String>();
				agentCommissionFeeTypes.add("AC");
				agentCommissionFeeTypes.add("AC1");
				agentCommissionFeeTypes.add("AC2");
				agentCommissionFeeTypes.add("AC3");
				if (!checkPostpaidBillRunCompleted(user, agentCommissionFeeTypes)) {
					List<BillDetailInfoVO> userBillList = billRunAgentPostpaidCommission(user);
					if (userBillList != null && userBillList.size() > 0) {
						for (BillDetailInfoVO bill : userBillList) {
							TradeUserBill tradeUserBill = new TradeUserBill();
							tradeUserBillList.add(tradeUserBill);
							tradeUserBill.setAccountCode(bill.getAccountCode());
							tradeUserBill.setActivateFlag(UnieapConstants.YES);
							tradeUserBill.setBillDate(bill.getBillDate());
							tradeUserBill.setEndDate(bill.getBillEndDate());
							tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
							tradeUserBill.setFeeDesc(bill.getFeeDesc());
							tradeUserBill.setFeeType(bill.getFeeType());
							tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
							tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
							tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
							tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
							tradeUserBill.setStartDate(bill.getBillStartDate());
							tradeUserBill.setTenantId(UnieapConstants.getTenantId());
							tradeUserBill.setUserCode(bill.getUserCode());
							tradeUserBill.setUserName(bill.getUserName());
						}
					}
				}
			}
			tradeUserBillRepository.saveAll(tradeUserBillList);
		}
		List<BillDetailInfoVO> revenueCommissionList = billRunRevenueCommission(POSTPAID);
		if (revenueCommissionList != null && revenueCommissionList.size() > 0) {
			List<TradeUserBill> tradeUserBillList = new ArrayList<TradeUserBill>();
			for (BillDetailInfoVO bill : revenueCommissionList) {
				TradeUser user = new TradeUser();
				user.setUserCode(bill.getUserCode());
				List<String> revenueCommissionFeeTypes = new ArrayList<String>();
				revenueCommissionFeeTypes.add("TC");
				if (!checkPostpaidBillRunCompleted(user, revenueCommissionFeeTypes)) {
					TradeUserBill tradeUserBill = new TradeUserBill();
					tradeUserBillList.add(tradeUserBill);
					tradeUserBill.setAccountCode(bill.getAccountCode());
					tradeUserBill.setActivateFlag(UnieapConstants.YES);
					tradeUserBill.setBillDate(bill.getBillDate());
					tradeUserBill.setEndDate(bill.getBillEndDate());
					tradeUserBill.setLatestPaymentDate(DateUtils.strToDate(bill.getLatestPaymentDate(), null));
					tradeUserBill.setFeeDesc(bill.getFeeDesc());
					tradeUserBill.setFeeType(bill.getFeeType());
					tradeUserBill.setOriginalAmount(Math.round(bill.getOriginalAmount()));
					tradeUserBill.setBillAmount(Math.round(bill.getBillAmount()));
					tradeUserBill.setOutstandingAmount(Math.round(bill.getOutstandingAmount()));
					tradeUserBill.setPaidAmount(Math.round(bill.getPaidAmount()));
					tradeUserBill.setStartDate(bill.getBillStartDate());
					tradeUserBill.setTenantId(UnieapConstants.getTenantId());
					tradeUserBill.setUserCode(bill.getUserCode());
					tradeUserBill.setUserName(bill.getUserName());
				}
			}
			tradeUserBillRepository.saveAll(tradeUserBillList);
		}
	}

	/**
	 * 出账前检查该预付费账户的费用项是否已经出账
	 * 
	 * @param user
	 * @param feeTypes
	 * @return
	 */
	public boolean checkPrepaidBillRunCompleted(TradeUser user, List<String> feeTypes) {
		BillDateInfoVO billDateInfo = tradeUserBillRepository
				.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
		Double isExist = tradeUserBillRepository.checkBillRunCompleted(user.getUserCode(), billDateInfo.getBillDate(),
				feeTypes);
		if (isExist.intValue() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 出账前检查该后付费账户的费用项是否已经出账
	 * 
	 * @param user
	 * @param feeTypes
	 * @return
	 */
	public boolean checkPostpaidBillRunCompleted(TradeUser user, List<String> feeTypes) {
		BillDateInfoVO billDateInfo = tradeUserBillRepository
				.getPostpaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
		Double isExist = tradeUserBillRepository.checkBillRunCompleted(user.getUserCode(), billDateInfo.getBillDate(),
				feeTypes);
		if (isExist.intValue() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取后付费账户订阅费用项
	 * 
	 * @param user
	 * @return
	 */
	public List<BillDetailInfoVO> billRunPostpaidSubscribeFee(TradeUser user) {
		List<BillDetailInfoVO> billDetailList = null;
		List<String> subscribeFeeTypes = new ArrayList<String>();
		subscribeFeeTypes.add("S1");
		subscribeFeeTypes.add("S2");
		subscribeFeeTypes.add("S3");
		subscribeFeeTypes.add("S4");
		subscribeFeeTypes.add("S5");
		subscribeFeeTypes.add("S6");
		List<AccountInfoVO> accountList = tradeUserAccountRepository.getBillAccountList(user.getUserCode(),
				subscribeFeeTypes);
		if (accountList != null && accountList.size() > 0) {
			billDetailList = new ArrayList<BillDetailInfoVO>();
			for (AccountInfoVO account : accountList) {
				List<AccountFeeItemVO> accountFeeList = tradeUserFeeRepository
						.getAccountSubscribeFeeList(account.getAccountCode(), subscribeFeeTypes);
				List<BillDetailInfoVO> subscribeFeeBillDetailList = calculateSubscribeFee(POSTPAID, accountFeeList);
				if (subscribeFeeBillDetailList != null) {
					billDetailList.addAll(subscribeFeeBillDetailList);
				}
			}
		}
		return billDetailList;
	}

	/**
	 * 获取预付费账户订阅费用项
	 * 
	 * @param user
	 * @return
	 */
	public List<BillDetailInfoVO> billRunPrepaidSubscribeFee(TradeUser user) {
		List<BillDetailInfoVO> billDetailList = null;
		List<String> subscribeFeeTypes = new ArrayList<String>();
		subscribeFeeTypes.add("S1");
		subscribeFeeTypes.add("S2");
		subscribeFeeTypes.add("S3");
		subscribeFeeTypes.add("S4");
		subscribeFeeTypes.add("S5");
		subscribeFeeTypes.add("S6");
		List<AccountInfoVO> accountList = tradeUserAccountRepository.getBillAccountList(user.getUserCode(),
				subscribeFeeTypes);
		if (accountList != null && accountList.size() > 0) {
			billDetailList = new ArrayList<BillDetailInfoVO>();
			for (AccountInfoVO account : accountList) {
				List<AccountFeeItemVO> accountFeeList = tradeUserFeeRepository
						.getAccountSubscribeFeeList(account.getAccountCode(), subscribeFeeTypes);
				List<BillDetailInfoVO> subscribeFeeBillDetailList = calculateSubscribeFee(PREPAID, accountFeeList);
				if (subscribeFeeBillDetailList != null) {
					billDetailList.addAll(subscribeFeeBillDetailList);
				}
			}
		}
		return billDetailList;
	}

	/**
	 * 计算后付费用户代理佣金
	 * 
	 * @param user
	 * @return
	 */
	public List<BillDetailInfoVO> billRunAgentPostpaidCommission(TradeUser user) {
		List<BillDetailInfoVO> billDetailList = null;
		billDetailList = new ArrayList<BillDetailInfoVO>();
		List<BillDetailInfoVO> agentBillDetailInfoList = tradeUserBillRepository
				.getAgentPostpaidBillInfoList(user.getAgentCode());
		// 有代理账户,根据代理账户订阅计算佣金并生成Bill Info
		if (agentBillDetailInfoList != null && agentBillDetailInfoList.size() > 0) {
			List<BillDetailInfoVO> agentCommissionFeeBillDetailList = calculateAgentCommissionFee(user.getId(),
					agentBillDetailInfoList);
			if (agentCommissionFeeBillDetailList != null && agentCommissionFeeBillDetailList.size() > 0) {
				for (BillDetailInfoVO agentBillDetailInfoVO : agentCommissionFeeBillDetailList) {
					billDetailList.add(agentBillDetailInfoVO);
					agentBillDetailInfoVO.setAccountCode(null);
					agentBillDetailInfoVO.setUserCode(user.getUserCode());
					agentBillDetailInfoVO.setUserName(user.getUserName());
					BillDateInfoVO billDateInfo = tradeUserBillRepository
							.getPostpaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
					agentBillDetailInfoVO.setBillDate(billDateInfo.getBillDate());
					agentBillDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
					agentBillDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
					agentBillDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
				}

			} else {
				// 没有代理账户,直接生成一条Bill Info,费用为0
				BillDateInfoVO billDateInfo = tradeUserBillRepository
						.getPostpaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
				BillDetailInfoVO billDetailInfoVO = new BillDetailInfoVO();
				billDetailList.add(billDetailInfoVO);
				billDetailInfoVO.setUserCode(user.getUserCode());
				billDetailInfoVO.setUserName(user.getUserName());
				billDetailInfoVO.setBillDate(billDateInfo.getBillDate());
				billDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
				billDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
				billDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
				TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType("AC").get(0);
				billDetailInfoVO.setFeeDesc(tradeFeeItem.getFeeDesc());
				billDetailInfoVO.setFeeType(tradeFeeItem.getFeeType());
				billDetailInfoVO.setId(tradeFeeItem.getId());
				billDetailInfoVO.setOriginalAmount(Double.valueOf(0));
				billDetailInfoVO.setBillAmount(Double.valueOf(0));
				billDetailInfoVO.setOutstandingAmount(Math.round(billDetailInfoVO.getBillAmount()));
				billDetailInfoVO.setPaidAmount(Double.valueOf(0));

			}
		} else {
			// 没有代理账户,直接生成一条Bill Info,费用为0
			BillDateInfoVO billDateInfo = tradeUserBillRepository
					.getPostpaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
			BillDetailInfoVO billDetailInfoVO = new BillDetailInfoVO();
			billDetailList.add(billDetailInfoVO);
			billDetailInfoVO.setUserCode(user.getUserCode());
			billDetailInfoVO.setUserName(user.getUserName());
			billDetailInfoVO.setBillDate(billDateInfo.getBillDate());
			billDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
			billDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
			billDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
			TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType("AC").get(0);
			billDetailInfoVO.setFeeDesc(tradeFeeItem.getFeeDesc());
			billDetailInfoVO.setFeeType(tradeFeeItem.getFeeType());
			billDetailInfoVO.setId(tradeFeeItem.getId());
			billDetailInfoVO.setOriginalAmount(Double.valueOf(0));
			billDetailInfoVO.setBillAmount(Double.valueOf(0));
			billDetailInfoVO.setOutstandingAmount(Math.round(billDetailInfoVO.getBillAmount()));
			billDetailInfoVO.setPaidAmount(Double.valueOf(0));
		}
		return billDetailList;
	}

	/**
	 * 计算预付费用户代理佣金
	 * 
	 * @param user
	 * @return
	 */
	public List<BillDetailInfoVO> billRunAgentPrepaidCommission(TradeUser user) {
		List<BillDetailInfoVO> billDetailList = null;
		billDetailList = new ArrayList<BillDetailInfoVO>();
		List<BillDetailInfoVO> agentBillDetailInfoList = tradeUserBillRepository
				.getAgentPrepaidBillInfoList(user.getAgentCode());
		// 有代理账户,根据代理账户订阅计算佣金并生成Bill Info
		if (agentBillDetailInfoList != null && agentBillDetailInfoList.size() > 0) {
			List<BillDetailInfoVO> agentCommissionFeeBillDetailList = calculateAgentCommissionFee(user.getId(),
					agentBillDetailInfoList);
			if (agentCommissionFeeBillDetailList != null && agentCommissionFeeBillDetailList.size() > 0) {
				for (BillDetailInfoVO agentBillDetailInfoVO : agentCommissionFeeBillDetailList) {
					billDetailList.add(agentBillDetailInfoVO);
					agentBillDetailInfoVO.setAccountCode(null);
					agentBillDetailInfoVO.setUserCode(user.getUserCode());
					agentBillDetailInfoVO.setUserName(user.getUserName());
					BillDateInfoVO billDateInfo = tradeUserBillRepository
							.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
					agentBillDetailInfoVO.setBillDate(billDateInfo.getBillDate());
					agentBillDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
					agentBillDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
					agentBillDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
				}

			} else {
				// 没有代理账户,直接生成一条Bill Info,费用为0
				BillDateInfoVO billDateInfo = tradeUserBillRepository
						.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
				BillDetailInfoVO billDetailInfoVO = new BillDetailInfoVO();
				billDetailList.add(billDetailInfoVO);
				billDetailInfoVO.setUserCode(user.getUserCode());
				billDetailInfoVO.setUserName(user.getUserName());
				billDetailInfoVO.setBillDate(billDateInfo.getBillDate());
				billDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
				billDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
				billDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
				TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType("AC").get(0);
				billDetailInfoVO.setFeeDesc(tradeFeeItem.getFeeDesc());
				billDetailInfoVO.setFeeType(tradeFeeItem.getFeeType());
				billDetailInfoVO.setId(tradeFeeItem.getId());
				billDetailInfoVO.setOriginalAmount(Double.valueOf(0));
				billDetailInfoVO.setBillAmount(Double.valueOf(0));
				billDetailInfoVO.setOutstandingAmount(Math.round(billDetailInfoVO.getBillAmount()));
				billDetailInfoVO.setPaidAmount(Double.valueOf(0));

			}
		} else {
			// 没有代理账户,直接生成一条Bill Info,费用为0
			BillDateInfoVO billDateInfo = tradeUserBillRepository
					.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
			BillDetailInfoVO billDetailInfoVO = new BillDetailInfoVO();
			billDetailList.add(billDetailInfoVO);
			billDetailInfoVO.setUserCode(user.getUserCode());
			billDetailInfoVO.setUserName(user.getUserName());
			billDetailInfoVO.setBillDate(billDateInfo.getBillDate());
			billDetailInfoVO.setBillStartDate(billDateInfo.getBillStartDate());
			billDetailInfoVO.setBillEndDate(billDateInfo.getBillEndDate());
			billDetailInfoVO.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
			TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType("AC").get(0);
			billDetailInfoVO.setFeeDesc(tradeFeeItem.getFeeDesc());
			billDetailInfoVO.setFeeType(tradeFeeItem.getFeeType());
			billDetailInfoVO.setId(tradeFeeItem.getId());
			billDetailInfoVO.setOriginalAmount(Double.valueOf(0));
			billDetailInfoVO.setBillAmount(Double.valueOf(0));
			billDetailInfoVO.setOutstandingAmount(Math.round(billDetailInfoVO.getBillAmount()));
			billDetailInfoVO.setPaidAmount(Double.valueOf(0));
		}
		return billDetailList;
	}

	/**
	 * 总收入佣金
	 * 
	 * @return
	 */
	public List<BillDetailInfoVO> billRunRevenueCommission(String paymentType) {
		List<BillDetailInfoVO> billList = null;
		List<TradeUser> userList = tradeUserRepository.getAllSubscribeUserList();
		if (userList != null && userList.size() > 0) {
			billList = new ArrayList<BillDetailInfoVO>();
			TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType("TC").get(0);
			// 该用户的总收入代理佣金
			for (TradeUser user : userList) {
				BillDetailInfoVO billInfo = null;
				if (StringUtils.equals(paymentType, PREPAID)) {
					billInfo = tradeUserBillRepository.getPrepaidRevenueCommissionBillInfo(user.getAgentCode());
				} else {
					billInfo = tradeUserBillRepository.getPostpaidRevenueCommissionBillInfo(user.getAgentCode());
				}
				if (billInfo != null) {
					BillDetailInfoVO billDetailVo = new BillDetailInfoVO();
					billList.add(billDetailVo);
					billDetailVo.setUserCode(user.getUserCode());
					billDetailVo.setUserName(user.getUserName());
					billDetailVo.setOriginalAmount(billInfo.getOriginalAmount() * tradeFeeItem.getFeeAmount());
					billDetailVo.setFeeType(billInfo.getFeeType());
					billDetailVo.setFeeDesc(billInfo.getFeeDesc());
					billDetailVo.setBillAmount(billDetailVo.getOriginalAmount());
					billDetailVo.setOutstandingAmount(billDetailVo.getBillAmount());
					billDetailVo.setPaidAmount(Double.valueOf(0));

					BillDateInfoVO billDateInfo = null;
					if (StringUtils.equals(paymentType, PREPAID)) {
						billDateInfo = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
					} else {
						billDateInfo = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
					}

					billDetailVo.setBillDate(billDateInfo.getBillDate());
					billDetailVo.setBillStartDate(billDateInfo.getBillStartDate());
					billDetailVo.setBillEndDate(billDateInfo.getBillEndDate());
					billDetailVo.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
				} else {
					BillDateInfoVO billDateInfo = null;
					if (StringUtils.equals(paymentType, PREPAID)) {
						billDateInfo = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
					} else {
						billDateInfo = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
					}
					BillDetailInfoVO billDetailVo = new BillDetailInfoVO();
					billList.add(billDetailVo);
					billDetailVo.setUserCode(user.getUserCode());
					billDetailVo.setUserName(user.getUserName());
					billDetailVo.setBillDate(billDateInfo.getBillDate());
					billDetailVo.setBillStartDate(billDateInfo.getBillStartDate());
					billDetailVo.setBillEndDate(billDateInfo.getBillEndDate());
					billDetailVo.setLatestPaymentDate(billDateInfo.getLatestPaymentDate());
					billDetailVo.setFeeDesc(tradeFeeItem.getFeeDesc());
					billDetailVo.setFeeType(tradeFeeItem.getFeeType());
					billDetailVo.setId(tradeFeeItem.getId());
					billDetailVo.setOriginalAmount(Double.valueOf(0));
					billDetailVo.setBillAmount(Double.valueOf(0));
					billDetailVo.setOutstandingAmount(billDetailVo.getBillAmount());
					billDetailVo.setPaidAmount(Double.valueOf(0));
				}
			}
		}
		return billList;
	}

	/**
	 * 计算订阅费用
	 * 
	 * @param paymentType
	 * @param feeList
	 * @return
	 */
	public List<BillDetailInfoVO> calculateSubscribeFee(String paymentType, List<AccountFeeItemVO> feeList) {
		List<BillDetailInfoVO> billDetailList = null;
		if (feeList != null && feeList.size() > 0) {
			billDetailList = new ArrayList<BillDetailInfoVO>();
			for (AccountFeeItemVO fee : feeList) {
				BillDetailInfoVO billDetailVo = new BillDetailInfoVO();
				if (StringUtils.equals(fee.getFeeType(), "S1") || StringUtils.equals(fee.getFeeType(), "S2")
						|| StringUtils.equals(fee.getFeeType(), "S3") || StringUtils.equals(fee.getFeeType(), "S4")
						|| StringUtils.equals(fee.getFeeType(), "S5") || StringUtils.equals(fee.getFeeType(), "S6")) {
					billDetailVo.setAccountCode(fee.getAccountCode());
					billDetailVo.setFeeDesc(fee.getFeeDesc());
					billDetailVo.setUserCode(fee.getUserCode());
					billDetailVo.setUserName(fee.getUserName());
					billDetailVo.setFeeType(fee.getFeeType());
					billDetailVo.setId(fee.getId());
					billDetailVo.setOriginalAmount(fee.getFeeAmount());
					double discountAmount = billDetailVo.getOriginalAmount();
					// 计算订阅天数
					List<Map<String, String>> subDatas = tradeUserBillRepository.getSubscribeDays(fee.getAccountCode(),
							fee.getFeeType());
					int subscribeDays = Integer.parseInt(subDatas.get(0).get("subscribeDays"));
					int totalDays = Integer.parseInt(subDatas.get(0).get("totalDays"));
					discountAmount = discountAmount * subscribeDays / totalDays;
					DecimalFormat decimalFormat = new DecimalFormat("#0.00");
					discountAmount = Double.parseDouble(decimalFormat.format(discountAmount));
					billDetailVo.setFeeDesc(billDetailVo.getFeeDesc() + "/" + "订阅天数[" + subscribeDays + "]天");
					List<AccountFeeItemVO> accountFeeList = tradeUserFeeRepository
							.getUserAccountFeeList(fee.getAccountCode());
					for (AccountFeeItemVO fee2 : accountFeeList) {
						if (StringUtils.equals(fee2.getFeeType(), "SD1") || StringUtils.equals(fee2.getFeeType(), "SD2")
								|| StringUtils.equals(fee2.getFeeType(), "SD3")
								|| StringUtils.equals(fee2.getFeeType(), "SD4")
								|| StringUtils.equals(fee2.getFeeType(), "SD5")
								|| StringUtils.equals(fee2.getFeeType(), "SD6")) {
							discountAmount = discountAmount * (1 - fee2.getFeeAmount());
							billDetailVo.setFeeDesc(billDetailVo.getFeeDesc() + "/" + fee2.getFeeDesc());
						}
					}
					billDetailVo.setBillAmount(discountAmount);
					billDetailVo.setOutstandingAmount(billDetailVo.getBillAmount());
					billDetailVo.setPaidAmount(Double.valueOf(0));
					BillDateInfoVO billDateInfoVO = null;
					if (StringUtils.equals(paymentType, PREPAID)) {
						billDateInfoVO = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Prepaid_Payment_Date"));
					} else {
						billDateInfoVO = tradeUserBillRepository
								.getPrepaidBillDateInfo(UnieapConstants.SYS_DATA.get("Postpaid_Payment_Date"));
					}

					billDetailVo.setBillDate(billDateInfoVO.getBillDate());
					billDetailVo.setBillStartDate(billDateInfoVO.getBillStartDate());
					billDetailVo.setBillEndDate(billDateInfoVO.getBillEndDate());
					billDetailVo.setLatestPaymentDate(billDateInfoVO.getLatestPaymentDate());
					billDetailList.add(billDetailVo);
				}
			}
		}
		return billDetailList;
	}

	/**
	 * 计算代理佣金费用
	 * 
	 * @param feeList
	 * @return
	 */
	public List<BillDetailInfoVO> calculateAgentCommissionFee(Long userId, List<BillDetailInfoVO> billDetailList) {
		List<BillDetailInfoVO> agentBillDetailList = null;
		if (billDetailList != null && billDetailList.size() > 0) {
			agentBillDetailList = new ArrayList<BillDetailInfoVO>();
			double totalSubscribeFee = 0;
			// 计算总订阅费用
			for (BillDetailInfoVO vo : billDetailList) {
				totalSubscribeFee = totalSubscribeFee + vo.getBillAmount();
			}
			// billDetailInfoVO.setUserCode(vo.getUserCode());
			// billDetailInfoVO.setUserName(vo.getUserName());
			// billDetailInfoVO.setBillDate(vo.getBillDate());
			// billDetailInfoVO.setBillStartDate(vo.getBillStartDate());
			// billDetailInfoVO.setBillEndDate(vo.getBillEndDate());
			// List<String> agentCommissionFeeItems = new ArrayList<String>();
			// agentCommissionFeeItems.add("AC");
			// agentCommissionFeeItems.add("AC1");
			// agentCommissionFeeItems.add("AC2");
			// agentCommissionFeeItems.add("AC3");
			// 计算总返佣金，有多个佣金项
			List<AccountFeeItemVO> agentCommissionFeeItems = tradeUserFeeRepository.getUserFeeList(userId);
			for (AccountFeeItemVO tradeFeeItemType : agentCommissionFeeItems) {
				// accountCode is null 说明是用户级的费用
				if (StringUtils.equals(tradeFeeItemType.getFeeType(), "AC")
						|| StringUtils.equals(tradeFeeItemType.getFeeType(), "AC1")
						|| StringUtils.equals(tradeFeeItemType.getFeeType(), "AC2")
						|| StringUtils.equals(tradeFeeItemType.getFeeType(), "AC3")) {
					BillDetailInfoVO billDetailInfoVO = new BillDetailInfoVO();
					agentBillDetailList.add(billDetailInfoVO);
					TradeFeeItem tradeFeeItem = tradeFeeItemRepository.findByFeeType(tradeFeeItemType.getFeeType())
							.get(0);
					billDetailInfoVO.setFeeDesc(tradeFeeItem.getFeeDesc());
					billDetailInfoVO.setFeeType(tradeFeeItem.getFeeType());
					// billDetailInfoVO.setId(tradeFeeItem.getId());
					billDetailInfoVO.setOriginalAmount(-totalSubscribeFee * tradeFeeItem.getFeeAmount());
					billDetailInfoVO.setBillAmount(billDetailInfoVO.getOriginalAmount());
					billDetailInfoVO.setOutstandingAmount(billDetailInfoVO.getBillAmount());
					billDetailInfoVO.setPaidAmount(Double.valueOf(0));
				}
			}
		}
		return agentBillDetailList;
	}
}
