package com.slipper.unieap.ea.login.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson2.JSONObject;
import com.slipper.service.unieap.base.email.MailBO;
import com.slipper.service.unieap.base.email.MailVO;
import com.slipper.service.unieap.base.sms.SMSBO;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.ea.me.vo.MeInfoVO;
import com.slipper.unieap.ea.pojo.TradeUser;
import com.slipper.unieap.ea.pojo.TradeUserFee;
import com.slipper.unieap.ea.repository.TradeUserAccountRepository;
import com.slipper.unieap.ea.repository.TradeUserFeeRepository;
import com.slipper.unieap.ea.repository.TradeUserRepository;
import com.slipper.unieap.verifycode.bo.VerifyCodeBO;
import com.slipper.unieap.verifycode.pojo.UnieapVerifycode;

@Service
public class LoginBO extends BaseBO {

	@Autowired
	VerifyCodeBO verifyCodeBO;

	@Autowired
	public SMSBO sMSBO;

	@Autowired
	public MailBO mailBO;

	@Autowired
	public TradeUserRepository tradeUserRepository;

	@Autowired
	public TradeUserAccountRepository tradeUserAccountRepository;

	@Autowired
	public TradeUserFeeRepository tradeUserFeeRepository;

	public String createVerifyCode(UnieapVerifycode vo) throws Exception {
		verifyCodeBO.createVerifyCode(vo);
		UnieapVerifycode vvo = verifyCodeBO.getVerifyCode(vo);
		if (vvo == null) {
			return "Error";
		} else {
			MeInfoVO meInfoVO = tradeUserRepository.getUserInfo(vo.getUserCode().toLowerCase());
			if (meInfoVO != null) {
				if (StringUtils.isNotEmpty(meInfoVO.getPhoneNumber())) {
					verifyCodeBO.sendVerifyCode(null, meInfoVO.getPhoneNumber(), vvo.getVerifyCode());
				}
				if (StringUtils.isNotEmpty(meInfoVO.getEmail())) {
					verifyCodeBO.sendVerifyCode(meInfoVO.getEmail(), null, vvo.getVerifyCode());
				}
			} else {
				if (isPhoneNumber(vo.getUserCode())) {
					verifyCodeBO.sendVerifyCode(null, vo.getUserCode(), vvo.getVerifyCode());
				}
				if (isEmailAddress(vo.getUserCode())) {
					verifyCodeBO.sendVerifyCode(vo.getUserCode(), null, vvo.getVerifyCode());
				}
			}
			return vvo.getVerifyCode();
		}
	}

	private boolean isPhoneNumber(String input) {
		boolean isPhoneNumber = Pattern.matches("\\d{11}", input); // 使用正则表达式进行匹配

		if (isPhoneNumber) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isEmailAddress(String input) {
		boolean isEmailAddress = Pattern.matches(".+@.+", input); // 使用正则表达式进行匹配

		if (isEmailAddress) {
			return true;
		} else {
			return false;
		}
	}

	@Value("${spring.mail.username}")
	String username;
	@Value("${aliyun.tempCodeDefaultPasswordNotify}")
	String tempCode;

	/**
	 * 登陆校验 校验验证码,0：校验通过,1：用户名错误,2:验证码错误,3:验证码失效,4:校验码已使用,5:密码错误,6:密码和验证码不能同时为空
	 * 
	 * @param vo
	 * @param verifyCode
	 * @return
	 * @throws Exception
	 */
	public String login(UnieapVerifycode vo, String verifyCode, String password) throws Exception {
		if (StringUtils.isNotEmpty(password)) {
			MeInfoVO meInfoVO = tradeUserRepository.getUserInfo(vo.getUserCode().toLowerCase());
			if (meInfoVO == null) {
				// 用户名不存在
				return "1";
			} else {
				if (StringUtils.equals(meInfoVO.getPassword(), DigestUtils.md5DigestAsHex(password.getBytes()))) {
					// 登陆成功
					return meInfoVO.getUserCode();
				} else {
					// 密码不正确
					return "5";
				}
			}
		}
		if (StringUtils.isNotEmpty(verifyCode)) {
			int verifyResult = verifyCodeBO.checkVerifyCode(vo, verifyCode);
			String userCode = "";
			// 校验通过,如果用户不存在,自动注册用户,如果用户没有设置密码,自动生成默认密码
			if (verifyResult == 0) {
				MeInfoVO meInfoVO = tradeUserRepository.getUserInfo(vo.getUserCode());
				String defaultPassword = generatePassword();
				defaultPassword = "123456";// 用户默认密码
				if (meInfoVO == null) {
					userCode = vo.getUserCode();
					TradeUser tradeUser = new TradeUser();
					tradeUser.setUserCode(vo.getUserCode());
					tradeUser.setUserName(vo.getUserCode());
					
					if (isPhoneNumber(vo.getUserCode())) {
						tradeUser.setPhoneNumber(vo.getUserCode());
					}
					if (isEmailAddress(vo.getUserCode())) {
						tradeUser.setEmail(vo.getUserCode());
					}
					tradeUser.setPassword(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
					tradeUser.setAgentCode(getNewAgentCode());
					tradeUser.setActivateFlag(UnieapConstants.YES);
					tradeUser.setTenantId(UnieapConstants.getTenantId());
					tradeUser = tradeUserRepository.save(tradeUser);					
					
					// 增加用户级费用项
					List<TradeUserFee> feeList = new ArrayList<TradeUserFee>();
					TradeUserFee tc = new TradeUserFee();
					feeList.add(tc);
					tc.setActivateFlag(UnieapConstants.YES);
					tc.setFeeType("TC");
					tc.setTenantId(UnieapConstants.getTenantId());
					tc.setUserId(tradeUser.getId());

					TradeUserFee ac = new TradeUserFee();
					feeList.add(ac);
					ac.setActivateFlag(UnieapConstants.YES);
					ac.setFeeType("AC");
					ac.setTenantId(UnieapConstants.getTenantId());
					ac.setUserId(tradeUser.getId());
					tradeUserFeeRepository.saveAll(feeList);

					if (isPhoneNumber(vo.getUserCode())) {
						JSONObject obj = new JSONObject();
						obj.put("name", vo.getUserCode());
						obj.put("dp", defaultPassword);
						sMSBO.sendSMS(new String[] { vo.getUserCode() }, obj.toJSONString(), tempCode);
					}
					if (isEmailAddress(vo.getUserCode())) {
						MailVO mailvo = new MailVO();
						mailvo.setSubject("创建用户成功/Generate User Success");
						mailvo.setToAddressList(new String[] { vo.getUserCode() });
						mailvo.setContent("[龙知易科技]创建用户成功,用户名[" + vo.getUserCode() + "]默认密码[" + defaultPassword
								+ "]。[LZY]Generate User Success,User Code[" + vo.getUserCode() + "]Default Password["
								+ defaultPassword + "]");
						mailBO.sendMail(username, mailvo);
					}
				} else {
					// 如果用户没有设置密码,则生成默认密码
					userCode = meInfoVO.getUserCode();
					if (StringUtils.isEmpty(meInfoVO.getPassword())) {
						TradeUser tradeUser = tradeUserRepository.getById(meInfoVO.getId());					
						tradeUser.setPassword(DigestUtils.md5DigestAsHex(defaultPassword.getBytes()));
						tradeUser.setRemark(defaultPassword);
						tradeUserRepository.save(tradeUser);
						if (isPhoneNumber(vo.getUserCode())) {
							JSONObject obj = new JSONObject();
							obj.put("name", vo.getUserCode());
							obj.put("dp", defaultPassword);
							sMSBO.sendSMS(new String[] { vo.getUserCode() }, obj.toJSONString(), tempCode);
						}
						if (isEmailAddress(vo.getUserCode())) {
							MailVO mailvo = new MailVO();
							mailvo.setSubject("【深圳市龙知易科技有限公司】]创建用户成功/Generate User Success");
							mailvo.setToAddressList(new String[] { vo.getUserCode() });
							mailvo.setContent("龙知易EA用户创建成功,用户名[" + vo.getUserCode() + "]默认密码[" + defaultPassword
									+ "]，请登陆APP完成订阅和托管交易设置。[LZY]Generate User Success,User Code[" + vo.getUserCode()
									+ "]Default Password[" + defaultPassword + "]");
							mailBO.sendMail(username, mailvo);
						}
					}
				}
			}else {
				return ""+verifyResult;
			}
			return userCode;
		}
		return "System Error";
	}

	private String generatePassword() {
		int STRING_LENGTH = 8;
		char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray(); // 定义所有可能的字符集合
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < STRING_LENGTH; i++) {
			char c = CHARACTERS[rand.nextInt(CHARACTERS.length)]; // 从字符集合中随机选取一个字符
			sb.append(c);
		}
		return sb.toString();
	}

	private String getNewAgentCode() {
		return tradeUserRepository.getAgentCode();
	}
}
