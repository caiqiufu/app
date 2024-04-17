package com.slipper.unieap.ea.myaccount.bo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.ea.myaccount.vo.AccountInfoVO;
import com.slipper.unieap.ea.myaccount.vo.TradeAutoVO;
import com.slipper.unieap.ea.myaccount.vo.TradeRecordVO;
import com.slipper.unieap.ea.pojo.TradeAuto;
import com.slipper.unieap.ea.repository.TradeAutoRepository;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class MyaccountBO extends BaseBO {

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeAutoRepository tradeAutoRepository;

	public List<DicDataVO> getDataList(String groupCode, String params) {
		JSONObject json = null;
		if (StringUtils.isNotEmpty(params)) {
			json = JSONObject.parseObject(params);
		}
		if (StringUtils.equals(groupCode, "ACCOUNT_LIST")) {
			String userCode = json.getString("userCode");
			return getAccountDicDataList(userCode);
		}
		return null;
	}

	/**
	 * 获取用户下的所有账户列表,包括账户余额信息
	 * 
	 * @param userCode
	 * @return
	 */
	public List<DicDataVO> getAccountDicDataList(String userCode) {
		List<AccountInfoVO> datas = tradeUserAccountRepository.getAccountDicList(userCode);
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (AccountInfoVO data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setDicCode(data.getAccountCode());
				datavo.setDicName(data.getAccountCode() + "|" + data.getBrokerName());
				datavo.setAttr1(data.getBrokerCode());
				datavo.setAttr2(String.valueOf(data.getAccountBalance()));
				datavo.setAttr3(String.valueOf(data.getAccountEquity()));
				datavo.setAttr4(String.valueOf(data.getAccountMargin()));
				datavo.setAttr5(String.valueOf(data.getAccountFreemargin()));

				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}

	public AccountInfoVO getAccountInfo(String accountCode) {
		AccountInfoVO accountInfo = tradeUserAccountRepository.getAccountBalanceInfo(accountCode);
		AccountInfoVO dailyProfitInfo = tradeUserAccountRepository.getDailyProfitInfo(accountCode);
		DecimalFormat df = new DecimalFormat("#.00");
		if (dailyProfitInfo != null) {
			accountInfo.setDailyProfitAmount(dailyProfitInfo.getDailyProfitAmount());
			accountInfo.setDailyProfitPoint(dailyProfitInfo.getDailyProfitPoint());
			if (dailyProfitInfo.getDailyBalanceStart() > 0) {
				double profitpercentage = dailyProfitInfo.getDailyProfitAmount()
						/ dailyProfitInfo.getDailyBalanceStart();
				profitpercentage = Double.parseDouble(df.format(profitpercentage));
				accountInfo.setDailyProfitpercentage(String.format("%.2f%%", profitpercentage * 100));
			} else {
				accountInfo.setDailyProfitpercentage("0");
			}
		}else {
			dailyProfitInfo = accountInfo;
			dailyProfitInfo.setDailyProfitpercentage("0.00%");
			dailyProfitInfo.setDailyProfitAmount(0.00);
			dailyProfitInfo.setDailyProfitPoint(0.00);
		}
		AccountInfoVO weeklyProfitInfo = tradeUserAccountRepository.getWeeklyProfitInfo(accountCode);
		if (weeklyProfitInfo != null) {
			accountInfo.setWeeklyProfitAmount(weeklyProfitInfo.getWeeklyProfitAmount());
			accountInfo.setWeeklyProfitPoint(weeklyProfitInfo.getWeeklyProfitPoint());
			if (weeklyProfitInfo.getWeeklyBalanceStart() > 0) {
				double profitpercentage = weeklyProfitInfo.getWeeklyProfitAmount()
						/ weeklyProfitInfo.getWeeklyBalanceStart();
				profitpercentage = Double.parseDouble(df.format(profitpercentage));
				accountInfo.setWeeklyProfitpercentage(String.format("%.2f%%", profitpercentage * 100));
			} else {
				accountInfo.setWeeklyProfitpercentage("0");
			}
		}else {
			weeklyProfitInfo = accountInfo;
			weeklyProfitInfo.setWeeklyProfitpercentage("0.00%");
			weeklyProfitInfo.setWeeklyProfitAmount(0.00);
			weeklyProfitInfo.setWeeklyProfitPoint(0.00);
		}
		AccountInfoVO monthlyProfitInfo = tradeUserAccountRepository.getMonthlyProfitInfo(accountCode);
		if (monthlyProfitInfo != null) {
			accountInfo.setMonthlyProfitAmount(monthlyProfitInfo.getMonthlyProfitAmount());
			accountInfo.setMonthlyProfitPoint(monthlyProfitInfo.getMonthlyProfitPoint());
			if (monthlyProfitInfo.getMonthlyBalanceStart() > 0) {
				double profitpercentage = monthlyProfitInfo.getMonthlyProfitAmount()
						/ monthlyProfitInfo.getMonthlyBalanceStart();
				profitpercentage = Double.parseDouble(df.format(profitpercentage));
				accountInfo.setMonthlyProfitpercentage(String.format("%.2f%%", profitpercentage * 100));
			} else {
				accountInfo.setMonthlyProfitpercentage("0");
			}
		}else {
			monthlyProfitInfo = accountInfo;
			monthlyProfitInfo.setMonthlyProfitpercentage("0.00%");
			monthlyProfitInfo.setMonthlyProfitAmount(0.00);
			monthlyProfitInfo.setMonthlyProfitPoint(0.00);
		}
		return accountInfo;
	}

	/**
	 * 获取自动交易状态
	 * 
	 * @param accountCode
	 * @return
	 */
	public Map<String, String> getTradeStatus(String accountCode) {
		Map<String, String> data = new HashMap<String, String>();
		TradeAutoVO tradeAutoVO = tradeAutoRepository.getAutoInfo(accountCode);
		if (tradeAutoVO == null) {
			data.put("isAccountLogin", UnieapConstants.NO);
			data.put("isAccountAuto", UnieapConstants.NO);
			data.put("isAutoTrade", UnieapConstants.NO);
		} else {
			if (StringUtils.equals(tradeAutoVO.getLoginStatus(), UnieapConstants.YES)) {
				data.put("isAccountLogin", tradeAutoVO.getLoginStatus());
				data.put("loginDate", tradeAutoVO.getLoginDate());
				data.put("isAccountAuto", tradeAutoVO.getAutoStatus());
				data.put("autoDate", tradeAutoVO.getAutoDate());
			} else {
				data.put("isAccountLogin", UnieapConstants.NO);
				data.put("isAccountAuto", UnieapConstants.NO);
			}
			data.put("isAutoTrade", tradeAutoVO.getAutoTrade());

		}
		return data;
	}

	public PaginationSupport getTradeRecordGridList(PaginationSupport page, String accountCode, Date startTime,
			Date endTime) {
		Page<TradeRecordVO> datas = tradeUserAccountRepository.queryTradeRecordPage(accountCode,
				this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}
	
	/**
	 * 修改自动交易标识
	 * 
	 * @param accountCode
	 * @param autoTrade
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateAutoTrade(String accountCode, String autoTrade) throws Exception {
		TradeAutoVO tradeAutoVO = tradeAutoRepository.getAutoInfo(accountCode);
		TradeAuto tradeAuto = tradeAutoRepository.getById(tradeAutoVO.getId());
		tradeAuto.setAutoTrade(autoTrade);
		tradeAutoRepository.save(tradeAuto);
	}
}
