package com.slipper.unieap.ea.mybill.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.ea.mybill.vo.BillDetailInfoVO;
import com.slipper.unieap.ea.mybill.vo.BillSummaryInfoVO;
import com.slipper.unieap.ea.mybill.vo.PaymentInfoVO;
import com.slipper.unieap.ea.pojo.TradeUserAccount;
import com.slipper.unieap.ea.pojo.TradeUserBill;
import com.slipper.unieap.ea.pojo.TradeUserPayment;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.ea.repository.TradeUserBillRepository;
import com.slipper.unieap.ea.repository.TradeUserPaymentRepository;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class MybillBO extends BaseBO {

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeUserBillRepository tradeUserBillRepository;

	@Autowired
	public TradeUserPaymentRepository tradeUserPaymentRepository;

	/**
	 * 获取用户下的所有账户费用汇总信息
	 * 
	 * @param userCode
	 * @return
	 */
	public BillSummaryInfoVO getBillSummaryInfo(String userCode) {
		return tradeUserBillRepository.getBillSummaryInfo(userCode);
	}

	/**
	 * 获取指定账单信息
	 * 
	 * @param id
	 * @return
	 */
	public BillDetailInfoVO getBillDetailInfo(Long id) {
		return tradeUserBillRepository.queryBillDetailInfo(id);
	}

	/**
	 * 获取账户账单明细列表
	 * 
	 * @param page
	 * @param userCode
	 * @param accountCode
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public PaginationSupport getBillDetailGridList(PaginationSupport page, String userCode, Date startTime,
			Date endTime) {

		Calendar startTimeCalendar = Calendar.getInstance();
		startTimeCalendar.setTimeInMillis(startTime.getTime());
		Calendar endTimeCalendar = Calendar.getInstance();
		endTimeCalendar.setTimeInMillis(endTime.getTime());
		List<String> months = getMonthsBetweenDates(startTimeCalendar, endTimeCalendar);

		Page<BillDetailInfoVO> datas = tradeUserBillRepository.queryBillDetailInfoPage(userCode, months,
				this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	/**
	 * 获取所有账单信息
	 * 
	 * @param page
	 * @param userCode
	 * @param billType 0：全部账单,2:未缴费
	 * @return
	 */
	public PaginationSupport getBillDetailGridList(PaginationSupport page, String userCode, String accountCode,
			String billType, Date startTime, Date endTime) {
		if (startTime == null || endTime == null) {
			startTime = UnieapConstants.getDateTime();
			endTime = UnieapConstants.getDateTime();
		}
		Calendar startTimeCalendar = Calendar.getInstance();
		startTimeCalendar.setTimeInMillis(startTime.getTime());
		Calendar endTimeCalendar = Calendar.getInstance();
		endTimeCalendar.setTimeInMillis(endTime.getTime());
		List<String> months = getMonthsBetweenDates(startTimeCalendar, endTimeCalendar);
		Page<BillDetailInfoVO> datas = null;
		if (StringUtils.equals(billType, "0")) {
			if (StringUtils.isEmpty(userCode)) {
				userCode = null;
			}
			if (StringUtils.isEmpty(accountCode)) {
				accountCode = null;
			}
			datas = tradeUserBillRepository.queryBillDetailInfoAllPage(userCode, accountCode, months,
					this.getPageable(page));
		}
		if (StringUtils.equals(billType, "2")) {
			datas = tradeUserBillRepository.queryBillDetailInfoOutstandingPage(this.getPageable(page));
		}
		if (datas != null) {
			page.items = datas.getContent();
			page.setTotalCount((int) datas.getTotalElements());
		} else {
			page.items = new ArrayList<Object>();
			page.setTotalCount(0);
		}
		return page;
	}

	/**
	 * 订阅账单付费
	 * 
	 * @param accountCode
	 */
	public void billPaymentForSubscribe(String accountCode, String billDate) {
		List<BillDetailInfoVO> billVOs = tradeUserBillRepository.getOutstandingBill(accountCode, billDate);
		if (billVOs != null && billVOs.size() > 0) {
			for (BillDetailInfoVO vo : billVOs) {
				TradeUserBill bill = tradeUserBillRepository.getById(vo.getId());
				bill.setPaidAmount(bill.getOutstandingAmount());
				bill.setOutstandingAmount(0);
				tradeUserBillRepository.save(bill);
			}
			List<TradeUserAccount> accounts = tradeUserAccountRepository.findByAccountCode(accountCode);
			if (accounts != null && accounts.size() > 0) {
				TradeUserAccount tradeUserAccount = tradeUserAccountRepository.getById(accounts.get(0).getId());
				if (StringUtils.equals(tradeUserAccount.getActivateFlag(), UnieapConstants.NO)) {
					tradeUserAccount.setActivateFlag(UnieapConstants.YES);
					tradeUserAccountRepository.save(tradeUserAccount);
				}
			}
		}
	}

	/**
	 * 账单缴费
	 * 
	 * @param vo
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void billPayment(PaymentInfoVO vo) {
		// 更新账单信息
		TradeUserBill tradeUserBill = tradeUserBillRepository.getById(vo.getId());
		tradeUserBill.setOutstandingAmount(0);
		tradeUserBill.setPaidAmount(vo.getPaidAmount());
		tradeUserBillRepository.save(tradeUserBill);

		// 保存缴费信息
		TradeUserPayment tradeUserPayment = new TradeUserPayment();
		tradeUserPayment.setUserCode(tradeUserBill.getUserCode());
		tradeUserPayment.setAccountCode(tradeUserBill.getAccountCode());
		tradeUserPayment.setUserName(vo.getUserName());
		tradeUserPayment.setAccountNo(vo.getAccountNo());
		tradeUserPayment.setActivateFlag(UnieapConstants.YES);
		tradeUserPayment.setBillId(vo.getId());
		tradeUserPayment.setPayAmount(vo.getPaidAmount());
		tradeUserPayment.setPayDate(UnieapConstants.getDateTime());
		tradeUserPayment.setPayMethod(vo.getPayMethod());
		tradeUserPayment.setScreenshotUrl(vo.getScreenshotUrl());
		tradeUserPayment.setTenantId(UnieapConstants.getTenantId());
		tradeUserPaymentRepository.save(tradeUserPayment);

		// 如果是因欠费被停用,缴费后启用
		List<TradeUserAccount> accounts = tradeUserAccountRepository.findByAccountCode(tradeUserBill.getAccountCode());
		if (accounts != null && accounts.size() > 0) {
			for (TradeUserAccount tradeUserAccount : accounts) {
				if (StringUtils.equals(tradeUserAccount.getActivateFlag(), UnieapConstants.NO)) {
					tradeUserAccount.setActivateFlag(UnieapConstants.YES);
				}
			}
			tradeUserAccountRepository.saveAll(accounts);
		}
	}

	/**
	 * 佣金结算付费
	 * 
	 * @param accountCode
	 */
	public void billPaymentForCommission(String userCode) {

	}

	/**
	 * 获取指定之间段的统计数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private static List<String> getMonthsBetweenDates(Calendar startDate, Calendar endDate) {
		// List<Integer> months = new ArrayList<>();
		// String duration = "";
		int yearStart = startDate.get(Calendar.YEAR);
		int monthStart = startDate.get(Calendar.MONTH);
		int month = monthStart + 1;
		// duration = duration + "'" + yearStart + String.format("%02d", monthStart)+"'"
		// +",";
		List<String> months = new ArrayList<String>();

		while (!endDate.before(startDate)) {
			// months.add(monthStart + 1);
			// duration = duration + "'" + yearStart + String.format("%02d", month) + "'" +
			// ",";
			months.add(yearStart + String.format("%02d", month));
			if (monthStart == Calendar.DECEMBER) {
				yearStart++;
				monthStart = Calendar.JANUARY; // 将月份重新设置为上一年的最后一个月
				month = monthStart + 1;
				startDate.set(yearStart, monthStart, 1);
			} else {
				startDate.add(Calendar.MONTH, 1);
				monthStart = startDate.get(Calendar.MONTH);
				month = monthStart + 1;
			}
		}
		return months;
	}
}
