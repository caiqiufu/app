package com.slipper.unieap.ea.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.me.vo.MeInfoVO;
import com.slipper.unieap.ea.pojo.TradeUser;

@Repository
public interface TradeUserRepository extends UnieapRepository<TradeUser> {

	/**
	 * 查询用户信息
	 * 
	 * @param userCode
	 * @return
	 */
	@Query(value = "SELECT u.id, u.user_code as userCode, u.user_name as userName,u.password,u.agent_code as agentCode,u.phone_number as phoneNumber,u.email,u.weixin,u.whatsapp,u.address,DATE_FORMAT(u.create_date,'%Y-%m-%d %T') as createDate,u.activate_flag as activateFlag FROM trade_user u WHERE LOWER(u.user_code) =?1 OR LOWER(u.email) = ?1 OR u.phone_number = ?1", nativeQuery = true)
	MeInfoVO getUserInfo(String userCode);

	/**
	 * 根据推荐码查询用户
	 * 
	 * @param agentCode
	 * @return
	 */
	@Query(value = "SELECT u.id, u.user_code as userCode, u.user_name as userName,u.password,u.agent_code as agentCode,u.phone_number as phoneNumber,u.email,u.weixin,u.whatsapp,u.address,u.create_date as createDate,u.activate_flag as activateFlag FROM trade_user u WHERE u.agent_code =?1", nativeQuery = true)
	MeInfoVO getUserInfoByAgentCode(String agentCode);

	/**
	 * 获取已订阅和试用期结束的用户列表
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT u FROM TradeUser u WHERE u.id IN (SELECT ua.userId FROM TradeUserAccount ua WHERE ua.subscribeFlag IS NOT NULL AND ua.trialEndDate <sysdate() AND ua.activateFlag = 'Y') ")
	List<TradeUser> getAllSubscribeUserList();

	/**
	 * 获取最新的推荐码
	 * 
	 * @return
	 */
	@Query(value = "SELECT LPAD(max(CONVERT(agent_code, UNSIGNED)+1), 6, '0') AS agentCode FROM trade_user", nativeQuery = true)
	String getAgentCode();
}
