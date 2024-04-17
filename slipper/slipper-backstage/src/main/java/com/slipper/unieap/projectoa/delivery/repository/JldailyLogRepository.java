package com.slipper.unieap.projectoa.delivery.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.delivery.pojo.JldailyLog;
import com.slipper.unieap.projectoa.delivery.vo.JldailyLogVO;

@Repository
public interface JldailyLogRepository extends UnieapRepository<JldailyLog> {
	
	@Query(value = "SELECT log.id as id,log.projectId as projectId,log.projectName as projectName,log.logTime as logTime,log.status as status,"
			+ "log.tempMax as tempMax,log.tempMin as tempMin,log.weatherMorning as weatherMorning,log.weatherAfternoon as weatherAfternoon,log.sgdw as sgdw,log.xjjl as xjjl,"
			+ "log.pzjl as pzjl,log.jzjl as jzjl,log.ybgcys as ybgcys,log.fxgcjy as fxgcjy,log.gclh as gclh,log.aqls as aqls,"
			+ "log.jlzlzx as jlzlzx,log.ktzscl as ktzscl,log.hbsxcl as hbsxcl,log.qtsx as qtsx,log.zjlyj as zjlyj,log.zjlName as zjlName,log.zjlDate as zjlDate,"
			+ "log.createDate as createDate,log.createBy as createBy,log.modifyDate as modifyDate,log.modifyBy as modifyBy,log.tenantId as tenantId,log.remark as remark "
			+ " FROM JldailyLog log WHERE (log.projectName like CONCAT('%',?1,'%') or ?1 is null) and (log.logTime >= ?2 or ?2 is null) and (log.logTime <= ?3 or ?3 is null) and (log.status = ?4 or ?4 is null)and (log.id = ?5 or ?5 is null) and (log.createBy = ?6 or ?6 is null) ORDER BY log.logTime DESC,log.projectName ASC", countQuery = "SELECT count(1) FROM JldailyLog log WHERE (log.projectName like CONCAT('%',?1,'%') or ?1 is null) and (log.logTime >= ?2 or ?2 is null) and (log.logTime <= ?3 or ?3 is null) and (log.status = ?4 or ?4 is null)and (log.id = ?5 or ?5 is null) and (log.createBy = ?6 or ?6 is null) ")
	Page<JldailyLogVO> getJldailyLogPage(String projectName, Date startDate,Date endDate,String status,Long logId,String createBy, Pageable pageable);
}
