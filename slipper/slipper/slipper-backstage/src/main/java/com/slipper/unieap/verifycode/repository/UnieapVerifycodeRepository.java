package com.slipper.unieap.verifycode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.verifycode.pojo.UnieapVerifycode;

@Repository
public interface UnieapVerifycodeRepository extends UnieapRepository<UnieapVerifycode> {

	List<UnieapVerifycode> findByActivateFlag(String activateFlag);

	UnieapVerifycode findByAppCodeAndUserCodeAndVerifyType(String appCode, String userCode, String verifyType);

	/**
	 * 获取验证码
	 * 
	 * @param appCode
	 * @param userCode
	 * @param verifyType
	 * @return
	 */
	@Query("SELECT e FROM UnieapVerifycode e WHERE e.appCode=?1 AND e.userCode=?2 AND e.verifyType=?3")
	List<UnieapVerifycode> getVerifyCode(String appCode, String userCode, String verifyType);
	/**
	 * 获取验证码
	 * @param appCode
	 * @param userCode
	 * @param sessionId
	 * @param verifyType
	 * @return
	 */
	@Query("SELECT e FROM UnieapVerifycode e WHERE e.appCode=?1 AND e.userCode=?2 AND e.sessionId = ?3 AND e.verifyType=?4")
	List<UnieapVerifycode> getVerifyCode(String appCode, String userCode,String sessionId, String verifyType);

	/**
	 * 删除验证码记录
	 * 
	 * @param appCode
	 * @param userCode
	 * @param verifyType
	 */
	@Modifying
	@Query("DELETE FROM UnieapVerifycode e WHERE e.appCode=?1 AND e.userCode=?2 AND e.verifyType=?3")
	void deleteVerifycode(String appCode, String userCode, String verifyType);
	
	/**
	 * 删除验证码记录
	 * @param appCode
	 * @param userCode
	 * @param sessionId
	 * @param verifyType
	 */
	@Modifying
	@Query("DELETE FROM UnieapVerifycode e WHERE e.appCode=?1 AND e.userCode=?2 AND e.sessionId=?3 AND e.verifyType=?4")
	void deleteVerifycode(String appCode, String userCode,String sessionId, String verifyType);
}
