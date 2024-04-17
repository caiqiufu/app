package com.slipper.unieap.ea.myaccount.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.ea.myaccount.bo.MyaccountBO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/ea/myaccount")
public class MyaccountController extends CommonController {

	@Autowired
	MyaccountBO mMyaccountBO;

	@Log("获取字典值")
	@RequestMapping("/getDicData")
	public R getDicData(String groupCode, String params) {
		return R.success(mMyaccountBO.getDataList(groupCode, params));
	}

	@Log("获取账户信息")
	@RequestMapping("/getAccountInfo")
	public R getAccountInfo(String accountCode) {
		return R.success(mMyaccountBO.getAccountInfo(accountCode));
	}

	@Log("交易日志")
	@RequestMapping("/tradePage")
	public R tradePage(PaginationSupport page, String accountCode, Date startTime, Date endTime) throws Exception {
		return R.success(mMyaccountBO.getTradeRecordGridList(page, accountCode, startTime, endTime).getMap());
	}

	@Log("获取自动交易状态")
	@RequestMapping("/getTradeStatus")
	public R getTradeStatus(String accountCode) throws Exception {
		return R.success(mMyaccountBO.getTradeStatus(accountCode));
	}

	@Log("更新自动交易标识")
	@RequestMapping("/updateAutoTrade")
	public R updateAutoTrade(String accountCode, String autoTrade) throws Exception {
		mMyaccountBO.updateAutoTrade(accountCode, autoTrade);
		return R.success();
	}
}
