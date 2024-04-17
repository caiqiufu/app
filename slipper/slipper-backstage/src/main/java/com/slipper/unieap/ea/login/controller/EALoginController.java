package com.slipper.unieap.ea.login.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.ea.login.bo.LoginBO;
import com.slipper.unieap.verifycode.pojo.UnieapVerifycode;

@RestController
@RequestMapping("/backstage/ea/login")
public class EALoginController extends CommonController {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	LoginBO loginBO;

	@Log("生成验证码")
	@RequestMapping("/createVerifyCode")
	public R createVerifyCode(String userCode, String verifyType) throws Exception {
		UnieapVerifycode vo = new UnieapVerifycode();
		vo.setAppCode(UnieapConstants.APP_CODE_EA);
		vo.setUserCode(userCode);
		vo.setVerifyType(verifyType);
		vo.setSessionId(request.getSession().getId());
		loginBO.createVerifyCode(vo);
		return R.success();
	}

	@Log("登陆")
	@RequestMapping("/login")
	public R login(String userCode, String verifyCode, String password, String verifyType) throws Exception {
		UnieapVerifycode vo = new UnieapVerifycode();
		vo.setAppCode(UnieapConstants.APP_CODE_EA);
		vo.setUserCode(userCode);
		vo.setVerifyType(verifyType);
		vo.setSessionId(request.getSession().getId());
		String result = loginBO.login(vo, verifyCode, password);
		return R.success(result);
	}
}
