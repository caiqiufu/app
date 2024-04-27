package com.slipper.unieap.ea.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.ea.eainfo.vo.EAInfoVO;
import com.slipper.unieap.ea.pojo.TradeEa;

@Repository
public interface TradeEARepository extends UnieapRepository<TradeEa> {

	/**
	 * 获取指定时间段内的策略数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Query(value = "SELECT c2.create_time as eaTime,ROUND(c2.profitPoint,2) AS profitPoint, CONCAT('交易类型: ',IF(c2.tradeType='CLOSE_SELL','卖','买'),', 开仓价格: ', c2.openPrice,', 平仓价格: ',c2.closePrice ,', 盈亏点数： ',c2.profitPoint) as eaCommand from (select c1.id,c1.create_time,c1.server_id, substring_index(c1.command,':',1) as tradeType,substring_index(substring_index(c1.command,':',2),':',-1) as openPrice, substring_index(substring_index(c1.command,':',3),':',-1) as closePrice,substring_index(c1.command,':',-1) as profitPoint from (select ea.id,ea.create_time,ea.server_id, substring_index(ea.ea_command,' ',-1) as command from trade_ea ea where (ea.ea_command LIKE '%CLOSE_SELL%' OR ea.ea_command LIKE '%CLOSE_BUY%') ) c1) c2 where  c2.server_id = ?1 AND c2.create_time>= ?2 AND c2.create_time<= ?3 order by c2.create_time ASC", nativeQuery = true)
	List<EAInfoVO> getEAInfo(String eaType,Date startDate, Date endDate);
	
	//@Query(value = "SELECT ss.eaTime as eaTime, CONCAT(ROUND(SUM(ss.profitPoint),2) ,',',COUNT(eaTime),',',SUM(profitOrder),',',SUM(lostOrder)) AS profitPoint FROM (select c2.create_time as eaTime,ROUND(c2.profitPoint,2) AS profitPoint,IF(c2.profitPoint>=0,1,0) AS profitOrder,IF(c2.profitPoint<0,1,0) AS lostOrder  FROM (select c1.id,c1.create_time,c1.server_id, substring_index(c1.command,':',1) as tradeType,substring_index(substring_index(c1.command,':',2),':',-1) as openPrice, substring_index(substring_index(c1.command,':',3),':',-1) as closePrice,substring_index(c1.command,':',-1) as profitPoint from (select ea.id,DATE_FORMAT(DATE_ADD(ea.create_time, INTERVAL -12 HOUR),'%Y%m%d')  as create_time,ea.server_id, substring_index(ea.ea_command,' ',-1) as command from trade_ea ea where (ea.ea_command LIKE '%CLOSE_SELL%' OR ea.ea_command LIKE '%CLOSE_BUY%') AND ea.create_time > DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) c1) c2  WHERE  c2.server_id = ?1 ORDER BY c2.create_time ASC) ss GROUP BY ss.eaTime", nativeQuery = true)
	@Query(value = "SELECT ss.eaTime as eaTime, CONCAT(ROUND(SUM(ss.profitPoint),2) ,',',COUNT(eaTime),',',SUM(profitOrder),',',SUM(lostOrder)) AS profitPoint FROM (select c2.create_time as eaTime,ROUND(c2.profitPoint,2) AS profitPoint,IF(c2.profitPoint>=0,1,0) AS profitOrder,IF(c2.profitPoint<0,1,0) AS lostOrder  FROM (select c1.id,c1.create_time,c1.server_id, substring_index(c1.command,':',1) as tradeType,substring_index(substring_index(c1.command,':',2),':',-1) as openPrice, substring_index(substring_index(c1.command,':',3),':',-1) as closePrice,substring_index(c1.command,':',-1) as profitPoint from (select ea.id,DATE_FORMAT(ea.create_time,'%Y%m%d')  as create_time,ea.server_id, substring_index(ea.ea_command,' ',-1) as command from trade_ea ea where (ea.ea_command LIKE '%CLOSE_SELL%' OR ea.ea_command LIKE '%CLOSE_BUY%') AND ea.create_time > DATE_SUB(CURDATE(), INTERVAL 1 MONTH)) c1) c2  WHERE  c2.server_id = ?1 ORDER BY c2.create_time ASC) ss GROUP BY ss.eaTime", nativeQuery = true)
	List<EAInfoVO> getMonthEAInfo(String eaType);
	
	@Query(value = "SELECT WEEK(ss.eaTime) as eaTime, CONCAT(ROUND(SUM(ss.profitPoint),2) ,',',COUNT(eaTime),',',SUM(profitOrder),',',SUM(lostOrder)) AS profitPoint FROM (select c2.create_time as eaTime,ROUND(c2.profitPoint,2) AS profitPoint,IF(c2.profitPoint>=0,1,0) AS profitOrder,IF(c2.profitPoint<0,1,0) AS lostOrder FROM (select c1.id,c1.create_time,c1.server_id, substring_index(c1.command,':',1) as tradeType,substring_index(substring_index(c1.command,':',2),':',-1) as openPrice, substring_index(substring_index(c1.command,':',3),':',-1) as closePrice,substring_index(c1.command,':',-1) as profitPoint from (select ea.id,ea.create_time,ea.server_id, substring_index(ea.ea_command,' ',-1) as command from trade_ea ea where (ea.ea_command LIKE '%CLOSE_SELL%' OR ea.ea_command LIKE '%CLOSE_BUY%') ) c1) c2  where  c2.server_id = ?1 ORDER BY c2.create_time ASC) ss GROUP BY WEEK(ss.eaTime) LIMIT 26", nativeQuery = true)
	List<EAInfoVO> get24WeeksEAInfo(String eaType);
}
