package com.slipper.unieap.ea.myagent.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.ea.myagent.vo.AgentAccountInfoVO;
import com.slipper.unieap.ea.myagent.vo.AgentSummaryInfoVO;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.ea.repository.TradeUserBillRepository;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class MyAgentBO extends BaseBO {

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeUserBillRepository tradeUserBillRepository;

	/**
	 * 获取代理汇总信息
	 * 
	 * @param userCode
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public AgentSummaryInfoVO getAgentSummaryInfo(String userCode) {
		AgentSummaryInfoVO agentSummaryInfoVO = new AgentSummaryInfoVO();
		//总代理数
		Double totalNumber = tradeUserAccountRepository.getTotalAgentAmount(userCode);
		agentSummaryInfoVO.setTotalNumber(totalNumber == null ? "0" : totalNumber.toString());
		//总订阅数
		Double subscribedNumber = tradeUserAccountRepository.getTotalSubscribeAmount(userCode);
		agentSummaryInfoVO.setSubscribedNumber(subscribedNumber == null ? "0" : subscribedNumber.toString());
		//总欠费数
		Double outstandingNumber = tradeUserAccountRepository.getOutstandingNumber(userCode);
		agentSummaryInfoVO.setOutstandingNumber(outstandingNumber == null ? "0" : outstandingNumber.toString());
		Double totalRevenueAmount = tradeUserAccountRepository.getTotalRevenueAmount(userCode);
		agentSummaryInfoVO.setTotalRevenueAmount(totalRevenueAmount == null ? 0 : totalRevenueAmount.doubleValue());
		Double revenueAmount = tradeUserAccountRepository.getRevenueAmount(userCode);
		agentSummaryInfoVO.setRevenueAmount(revenueAmount == null ? 0 : revenueAmount.doubleValue());
		String revenueMonth = tradeUserAccountRepository.getRevenueMonth();
		agentSummaryInfoVO.setRevenueMonth(revenueMonth);
		return agentSummaryInfoVO;
	}

	/**
	 * 查询Agent账户信息
	 * 
	 * @param page
	 * @param userCode
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public PaginationSupport getAgentGridList(PaginationSupport page, String userCode) {
		Page<AgentAccountInfoVO> datas = tradeUserAccountRepository.queryAgentAccountInfoPage(userCode,
				this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}
}
