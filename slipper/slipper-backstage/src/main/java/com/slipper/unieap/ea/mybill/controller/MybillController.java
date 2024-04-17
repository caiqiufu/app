package com.slipper.unieap.ea.mybill.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.ea.mybill.bo.MybillBO;
import com.slipper.unieap.ea.mybill.vo.PaymentInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/ea/mybill")
public class MybillController extends CommonController {

	@Autowired
	MybillBO mMybillBO;

	@Log("获取用户账单汇总信息")
	@RequestMapping("/getBillSummaryInfo")
	public R getBillSummaryInfo(String userCode) {
		return R.success(mMybillBO.getBillSummaryInfo(userCode));
	}

	@Log("获取指定账单明细信息")
	@RequestMapping("/getBillDetailInfo")
	public R getBillDetailInfo(Long id) {
		return R.success(mMybillBO.getBillDetailInfo(id));
	}

	@Log("账单明细")
	@RequestMapping("/userBillDetailPage")
	public R userBillDetailPage(PaginationSupport page, String userCode, Date startTime, Date endTime)
			throws Exception {
		return R.success(mMybillBO.getBillDetailGridList(page, userCode, startTime, endTime).getMap());
	}

	@Log("账单明细")
	@RequestMapping("/billDetailPage")
	public R billDetailPage(PaginationSupport page, String userCode, String accountCode, String billType,
			Date startTime, Date endTime) throws Exception {
		return R.success(
				mMybillBO.getBillDetailGridList(page, userCode, accountCode, billType, startTime, endTime).getMap());
	}

	@Log("账单缴费")
	@RequestMapping("/billPayment")
	public R billPayment(PaymentInfoVO vo) throws Exception {
		mMybillBO.billPayment(vo);
		return R.success();
	}
}
