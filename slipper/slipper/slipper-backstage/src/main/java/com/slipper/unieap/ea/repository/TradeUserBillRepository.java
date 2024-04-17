package com.slipper.unieap.ea.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.myagent.vo.AgentSummaryInfoVO;
import com.slipper.unieap.ea.mybill.vo.BillDateInfoVO;
import com.slipper.unieap.ea.mybill.vo.BillDetailInfoVO;
import com.slipper.unieap.ea.mybill.vo.BillSummaryInfoVO;
import com.slipper.unieap.ea.pojo.TradeUserBill;

@Repository
public interface TradeUserBillRepository extends UnieapRepository<TradeUserBill> {

	/**
	 * 获取用户所有账户有欠费费用汇总
	 * 
	 * @param userCode
	 * @return
	 */
	@Query(value = "SELECT ub.user_code AS userCode, ub.user_name AS userName, sum(ub.bill_amount) AS totalBillAmount,sum(ub.outstanding_amount) AS totalOutstandingAmount,sum(ub.paid_amount) AS totalPaidAmount  FROM trade_user_bill ub WHERE ub.outstanding_amount <> 0 AND ub.user_code =?1 GROUP BY ub.user_code, ub.user_name", nativeQuery = true)
	BillSummaryInfoVO getBillSummaryInfo(String userCode);

	/**
	 * 查询用户账户费用明细
	 * 
	 * @param userCode
	 * @param startDate
	 * @param endDate
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT ub.id,ub.user_code AS userCode,ub.user_name AS userName,ub.account_code AS accountCode,ub.fee_type AS feeType,ub.fee_desc AS feeDesc,ub.bill_date AS billDate,ub.start_date AS billStartDate,ub.end_date AS billEndDate,CONCAT(SUBSTRING_INDEX(ub.start_date, ' ', 1),'/',SUBSTRING_INDEX(ub.end_date, ' ', 1)) AS billDuration,ub.create_date AS createDate,ub.original_amount AS originalAmount,ub.bill_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub WHERE ub.user_code = ?1 AND ub.bill_date IN (?2)", countQuery = "SELECT count(*) FROM trade_user_bill ub WHERE ub.user_code = ?1 AND ub.bill_date IN (?2)", nativeQuery = true)
	Page<BillDetailInfoVO> queryBillDetailInfoPage(String userCode, List<String> duration, Pageable pageable);

	/**
	 * 获取指定账单明细信息
	 * @param id
	 * @return
	 */
	@Query(value = "SELECT ub.id,ub.user_code AS userCode,ub.user_name AS userName,ub.account_code AS accountCode,ub.fee_type AS feeType,ub.fee_desc AS feeDesc,ub.bill_date AS billDate,ub.start_date AS billStartDate,ub.end_date AS billEndDate,CONCAT(SUBSTRING_INDEX(ub.start_date, ' ', 1),'/',SUBSTRING_INDEX(ub.end_date, ' ', 1)) AS billDuration,ub.create_date AS createDate,ub.original_amount AS originalAmount,ub.bill_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub WHERE ub.id = ?1", countQuery = "SELECT count(*) FROM trade_user_bill ub WHERE ub.id = ?1", nativeQuery = true)
	BillDetailInfoVO queryBillDetailInfo(Long id);

	/**
	 * 查询所有账单
	 * 
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT ub.id,ub.user_code AS userCode,ub.user_name AS userName,ub.account_code AS accountCode,ub.fee_type AS feeType,ub.fee_desc AS feeDesc,ub.bill_date AS billDate,ub.start_date AS billStartDate,ub.end_date AS billEndDate,CONCAT(SUBSTRING_INDEX(ub.start_date, ' ', 1),'/',SUBSTRING_INDEX(ub.end_date, ' ', 1)) AS billDuration,ub.create_date AS createDate,ub.original_amount AS originalAmount,ub.bill_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub WHERE (ub.user_code = ?1 OR ?1 IS NULL) AND (ub.account_code = ?2 OR ?2 IS NULL)", countQuery = "SELECT count(*) FROM trade_user_bill ub WHERE (ub.user_code = ?1 OR ?1 IS NULL) AND (ub.account_code = ?2 OR ?2 IS NULL)", nativeQuery = true)
	Page<BillDetailInfoVO> queryBillDetailInfoAllPage(String userCode, String accountCode, List<String> duration,
			Pageable pageable);

	/**
	 * 查询欠费账单
	 * 
	 * @param pageable
	 * @return
	 */
	@Query(value = "SELECT ub.id,ub.user_code AS userCode,ub.user_name AS userName,ub.account_code AS accountCode,ub.fee_type AS feeType,ub.fee_desc AS feeDesc,ub.bill_date AS billDate,ub.start_date AS billStartDate,ub.end_date AS billEndDate,CONCAT(SUBSTRING_INDEX(ub.start_date, ' ', 1),'/',SUBSTRING_INDEX(ub.end_date, ' ', 1)) AS billDuration,ub.create_date AS createDate,ub.original_amount AS originalAmount,ub.bill_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub WHERE ub.outstanding_amount >0", countQuery = "SELECT count(*) FROM trade_user_bill ub WHERE ub.outstanding_amount >0", nativeQuery = true)
	Page<BillDetailInfoVO> queryBillDetailInfoOutstandingPage(Pageable pageable);

	/**
	 * 查询用户总的账单金额，欠费金额，缴费金额
	 * 
	 * @param userCode
	 * @return
	 */
	@Query(value = "SELECT ub.user_code AS userCode, ub.user_name AS userName, SUM(ub.bill_amount) AS totalBillAmount,sum(ub.outstanding_amount) AS totalOutstandingAmount,SUM(ub.paid_amount) AS totalPaidAmount  FROM trade_user_bill ub WHERE ub.user_code = ?1 GROUP BY ub.user_code, ub.user_name", nativeQuery = true)
	AgentSummaryInfoVO getAgentSummaryInfo(String userCode);

	/**
	 * 后付费账户代理总收入佣金
	 * 
	 * @param agentCode
	 * @return
	 */
	@Query(value = "SELECT  0 AS id, '' AS userCode,'' AS userName,'' AS accountCode,fi.fee_type AS feeType,fi.fee_desc AS feeDesc, '' AS billDate, '' AS billStartDate,'' AS billEndDate,sysdate() AS createDate,'' AS latestPaymentDate,CAST(IF(ra.original_amount IS NULL,0.0, ra.original_amount) AS DOUBLE) AS originalAmount,CAST(0.0 AS DOUBLE) AS billAmount,CAST(0.0 AS DOUBLE) AS outstandingAmount, CAST(0.0 AS DOUBLE) AS paidAmount FROM (SELECT SUM(ub.bill_amount) AS original_amount FROM trade_user_bill ub,trade_user_account ua WHERE  ub.bill_date = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y%m') AND ub.bill_amount <0 AND ub.user_code = ua.user_code AND ua.agent_code = ?1) ra ,trade_fee_item fi WHERE fi.fee_type = 'TC'", nativeQuery = true)
	BillDetailInfoVO getPostpaidRevenueCommissionBillInfo(String agentCode);
	
	/**
	 * 预付费账户代理总收入佣金
	 * 
	 * @param agentCode
	 * @return
	 */
	@Query(value = "SELECT  0 AS id, '' AS userCode,'' AS userName,'' AS accountCode,fi.fee_type AS feeType,fi.fee_desc AS feeDesc, '' AS billDate, '' AS billStartDate,'' AS billEndDate,sysdate() AS createDate,'' AS latestPaymentDate,CAST(IF(ra.original_amount IS NULL,0.0, ra.original_amount) AS DOUBLE) AS originalAmount,CAST(0.0 AS DOUBLE) AS billAmount,CAST(0.0 AS DOUBLE) AS outstandingAmount, CAST(0.0 AS DOUBLE) AS paidAmount FROM (SELECT SUM(ub.bill_amount) AS original_amount FROM trade_user_bill ub,trade_user_account ua WHERE  ub.bill_date = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y%m') AND ub.bill_amount < 0 AND ub.user_code = ua.user_code AND ua.agent_code = ?1) ra ,trade_fee_item fi WHERE fi.fee_type = 'TC'", nativeQuery = true)
	BillDetailInfoVO getPrepaidRevenueCommissionBillInfo(String agentCode);

	/**
	 * 获取后付费出账日期信息
	 * @param latestPaymentDate
	 * @return
	 */
	@Query(value = "SELECT DATE_FORMAT(DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH), '%Y%m') AS billDate, DATE_FORMAT(LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH)), '%Y-%m-01') AS billStartDate,LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH)) AS billEndDate,sysdate() AS createDate,DATE_FORMAT(LAST_DAY(DATE_ADD(CURRENT_DATE(),INTERVAL 1 MONTH)), CONCAT('%Y-%m-', ?1)) AS latestPaymentDate", nativeQuery = true)
	BillDateInfoVO getPostpaidBillDateInfo(String latestPaymentDate);
	
	
	/**
	 * 获取预付费出账日期
	 * @param latestPaymentDate
	 * @return
	 */
	@Query(value = "SELECT DATE_FORMAT(DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH), '%Y%m') AS billDate, DATE_FORMAT(LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH)), '%Y-%m-01') AS billStartDate,LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL 1 MONTH)) AS billEndDate,sysdate() AS createDate,CONCAT(DATE_FORMAT(LAST_DAY(DATE_ADD(CURRENT_DATE(),INTERVAL 1 MONTH)), CONCAT('%Y-%m-', ?1)),' ','23:59:59') AS latestPaymentDate", nativeQuery = true)
	BillDateInfoVO getPrepaidBillDateInfo(String latestPaymentDate);

	/**
	 * 出账前,清除脏数据
	 * 
	 * @param userCode
	 * @param billDate
	 */
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM trade_user_bill ub WHERE ub.user_code = ?1 AND ub.bill_date = ?2", nativeQuery = true)
	void deleteUserBillInfo(String userCode, String billDate);

	/**
	 * 用户是否已出账
	 * 
	 * @param userCode
	 * @param billDate
	 * @param feeTypes
	 * @return
	 */
	@Query(value = "SELECT count(*) AS isExist FROM trade_user_bill ub WHERE ub.user_code = ?1 AND ub.bill_date = ?2 AND ub.fee_type IN (?3)", nativeQuery = true)
	Double checkBillRunCompleted(String userCode, String billDate, List<String> feeTypes);

	/**
	 * 账户是否欠费
	 * 
	 * @param userCode
	 * @param accountCode
	 * @return
	 */
	@Query(value = "SELECT count(*) AS isExist FROM trade_user_bill ub WHERE ub.user_code = ?1 AND ub.account_code = ?2 AND ub.outstanding_amount >0", nativeQuery = true)
	Double checkOutstandingAmount(String userCode, String accountCode);

	/**
	 * 获取账单最迟缴费日期
	 * 
	 * @return
	 */
	@Query(value = "SELECT CONCAT(DATE_ADD(DATE_FORMAT(?1,'%Y-%m-%d'), INTERVAL 1 MONTH),' ','23:59:59') AS latestPaymentDate", nativeQuery = true)
	String getcLatestPaymentDate(String billDate);

	/**
	 * 获取代理后付费账户下的所有费用,获取的是上上个账期数据,例如当前是4月15号，出3月份账单,3月份账单中包含代理用户在2月份的缴费情况
	 * 
	 * @param agentCode
	 * @return
	 */
	@Query(value = "SELECT  0 AS id, '' AS userCode,'' AS userName,'' AS accountCode,fi.fee_type AS feeType,fi.fee_desc AS feeDesc, '' AS billDate, '' AS billStartDate,'' AS billEndDate,sysdate() AS createDate,'' AS latestPaymentDate,ub.original_amount AS originalAmount,ub.original_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub,trade_fee_item fi WHERE ub.fee_type = fi.fee_type AND ub.fee_type IN('S1','S2','S3','S4','S5','S6') AND ub.user_code IN (SELECT user_code FROM trade_user_account ua WHERE ua.agent_code = ?1) AND ub.bill_date = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH), '%Y%m') AND ub.bill_amount >0 AND ub.outstanding_amount = 0", nativeQuery = true)
	List<BillDetailInfoVO> getAgentPostpaidBillInfoList(String agentCode);

	/**
	 * 获取代理预付费账户下的所有费用,获取的是上个账期数据,例如当前是4月25号，出5月份账单,5月份账单中包含代理用户在4月份的缴费情况
	 * @param agentCode
	 * @return
	 */
	@Query(value = "SELECT  0 AS id, '' AS userCode,'' AS userName,'' AS accountCode,fi.fee_type AS feeType,fi.fee_desc AS feeDesc, '' AS billDate, '' AS billStartDate,'' AS billEndDate,sysdate() AS createDate,'' AS latestPaymentDate,ub.original_amount AS originalAmount,ub.original_amount AS billAmount,ub.outstanding_amount AS outstandingAmount,ub.paid_amount AS paidAmount FROM trade_user_bill ub,trade_fee_item fi WHERE ub.fee_type = fi.fee_type AND ub.fee_type IN('S1','S2','S3','S4','S5','S6') AND ub.user_code IN (SELECT user_code FROM trade_user_account ua WHERE ua.agent_code = ?1) AND ub.bill_date = DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH), '%Y%m') AND ub.bill_amount >0 AND ub.outstanding_amount = 0", nativeQuery = true)
	List<BillDetailInfoVO> getAgentPrepaidBillInfoList(String agentCode);
	
	/**
	 * 获取欠费账单信息
	 * @param accountCode
	 * @param billDate
	 * @return
	 */
	@Query(value = "SELECT ub.id FROM trade_user_bill ub WHERE ub.outstanding_amount > 0 AND ub.account_code = ?1 AND ub.bill_date =?2 ", nativeQuery = true)
	List<BillDetailInfoVO> getOutstandingBill(String accountCode, String billDate);
	
	
	/**
	 * 获取上个账期订阅天数
	 * @param accountCode
	 * @return
	 */
	@Query(value = "SELECT sr1.account_code as accountCode, CAST(SUM(DATEDIFF(sr1.subscribe_end_date,sr1.subscribe_start_date)+1) AS CHAR) AS subscribeDays, CAST(DAYOFMONTH(LAST_DAY(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH))) AS CHAR) as totalDays FROM (select sr.id,sr.account_code,sr.subscribe_flag, IF((sr.subscribe_end_date is null and sr.subscribe_start_date < date_sub(date_sub(date_format(now(), '%y-%m-%d'), Interval extract(Day From now()) - 1 Day), Interval 1 Month)),date_sub(date_sub(date_format(now(), '%y-%m-%d'), Interval extract(Day From now()) - 1 Day), Interval 1 Month),sr.subscribe_start_date ) as subscribe_start_date,IF(sr.subscribe_end_date is null,date_sub(date_sub(date_format(now(), '%y-%m-%d'), Interval extract(Day From now()) Day), Interval 0 Month),sr.subscribe_end_date) as subscribe_end_date from trade_subscribe_record sr WHERE sr.subscribe_start_date <= LAST_DAY(DATE_ADD(CURRENT_DATE(), INTERVAL -1 MONTH)) ) sr1 WHERE sr1.account_code = ?1 AND sr1.subscribe_flag = ?2 GROUP BY sr1.account_code" , nativeQuery = true)
	List<Map<String, String>> getSubscribeDays(String accountCode,String subscribeFlag);
	
}
