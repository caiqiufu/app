package com.slipper.unieap.ea.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.mybill.vo.AccountFeeItemVO;
import com.slipper.unieap.ea.pojo.TradeUserFee;

@Repository
public interface TradeUserFeeRepository extends UnieapRepository<TradeUserFee> {

	/**
	 * 查询账户级指定费用类型是否存在
	 * 
	 * @param userId
	 * @param accountCode
	 * @param feeType
	 * @return
	 */
	List<TradeUserFee> findByUserIdAndAccountCodeAndFeeType(Long userId, String accountCode, String feeType);

	/**
	 * 查询用户级指定费用类型是否存在
	 * 
	 * @param userId
	 * @param feeType
	 * @return
	 */
	List<TradeUserFee> findByUserIdAndFeeType(Long userId,String feeType);

	/**
	 * 获取用户的费用列表
	 * 
	 * @param userId
	 * @return
	 */
	List<TradeUserFee> findByUserId(Long userId);

	/**
	 * 获取账户列表
	 * 
	 * @param userId
	 * @return
	 */
	@Query(value = "SELECT account_code as accountCode FROM trade_user_fee uf WHERE user_id = ?1", nativeQuery = true)
	List<Map<String, String>> getAccountByUserIdList(Integer userId);

	/**
	 * 获取账户下的所有费用项,账户有订阅并且已过试用期才查询费用项
	 * 
	 * @param accountCode
	 * @return
	 */
	@Query(value = "SELECT uf.id,u.id as userId, u.user_code AS userCode,u.user_name AS userName,uf.account_code AS accountCode,ua.broker_name AS brokerName,uf.fee_type AS feeType,fi.fee_level AS feeLevel,fi.fee_desc AS feeDesc,fi.fee_amount AS feeAmount,fi.fee_amount_type AS feeAmountType, '' AS feeDate,DATE_FORMAT(ua.subscribe_start_date,'%Y%m%d') AS subscribeStartDate, '' as startDate,'' as endDate,'' AS latestPaymentDate FROM trade_user_fee uf, trade_user u, trade_fee_item fi,trade_user_account ua WHERE uf.user_id = u.id AND uf.fee_type = fi.fee_type AND uf.account_code = ua.account_code AND ua.subscribe_flag IS NOT NULL AND ua.trial_end_date <= sysdate() AND uf.account_code = ?1 AND uf.fee_type IN (?2)", nativeQuery = true)
	List<AccountFeeItemVO> getAccountFeeList(String accountCode, List<String> feeTypes);

	/**
	 * 获取账户下的订阅费用项,账户有订阅并且已过试用期才查询费用项
	 * 
	 * @param accountCode
	 * @return
	 */
	@Query(value = "SELECT sr.id,u.id as userId, u.user_code AS userCode,u.user_name AS userName,ua.account_code AS accountCode,ua.broker_name AS brokerName,fi.fee_type AS feeType,fi.fee_level AS feeLevel,fi.fee_desc AS feeDesc,fi.fee_amount AS feeAmount,fi.fee_amount_type AS feeAmountType, '' AS feeDate,DATE_FORMAT(ua.subscribe_start_date,'%Y%m%d') AS subscribeStartDate, '' as startDate,'' as endDate, '' AS latestPaymentDate  FROM trade_subscribe_record sr, trade_user u, trade_fee_item fi,trade_user_account ua WHERE sr.account_code = ua.account_code AND sr.subscribe_start_date <= LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH))  AND ua.user_id = u.id AND sr.subscribe_flag = fi.fee_type AND ua.subscribe_flag IS NOT NULL AND ua.trial_end_date <= sysdate() AND sr.account_code = ?1 AND sr.subscribe_flag IN (?2)", nativeQuery = true)
	List<AccountFeeItemVO> getAccountSubscribeFeeList(String accountCode, List<String> feeTypes);

	/**
	 * 获取账户下的费用项,该费用项是通过后台单独配置
	 * 
	 * @param feeTypes
	 * @return
	 */
	@Query(value = "SELECT uf.id,u.id as userId, u.user_code AS userCode,u.user_name AS userName,ua.account_code AS accountCode,ua.broker_name AS brokerName,fi.fee_type AS feeType,fi.fee_level AS feeLevel,fi.fee_desc AS feeDesc,fi.fee_amount AS feeAmount,fi.fee_amount_type AS feeAmountType, '' AS feeDate,DATE_FORMAT(ua.subscribe_start_date,'%Y%m%d') AS subscribeStartDate, '' as startDate,'' as endDate, '' AS latestPaymentDate FROM trade_user_fee uf, trade_user u, trade_fee_item fi,trade_user_account ua WHERE uf.account_code = ua.account_code  AND uf.user_id = u.id AND uf.fee_type = fi.fee_type AND ua.subscribe_flag IS NOT NULL AND ua.trial_end_date <= sysdate() AND uf.account_code = ?1", nativeQuery = true)
	List<AccountFeeItemVO> getUserAccountFeeList(String accountCode);

	/**
	 * 获取用户下的费用项,该费用项是通过后台单独配置
	 * 
	 * @param feeTypes
	 * @return
	 */
	@Query(value = "SELECT uf.id,u.id as userId, u.user_code AS userCode,u.user_name AS userName,uf.account_code AS accountCode,'' AS brokerName,fi.fee_type AS feeType,fi.fee_level AS feeLevel,fi.fee_desc AS feeDesc,fi.fee_amount AS feeAmount,fi.fee_amount_type AS feeAmountType, '' AS feeDate,'' AS subscribeStartDate,  '' as startDate,'' as endDate, '' AS latestPaymentDate  FROM trade_user_fee uf, trade_user u, trade_fee_item fi  WHERE uf.user_id = u.id AND uf.fee_type = fi.fee_type AND uf.user_id = ?1", nativeQuery = true)
	List<AccountFeeItemVO> getUserFeeList(Long userId);

}
