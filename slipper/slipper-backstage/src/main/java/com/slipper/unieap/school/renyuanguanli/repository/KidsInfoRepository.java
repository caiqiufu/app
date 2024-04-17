package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsInfo;
import com.slipper.unieap.school.renyuanguanli.vo.KidsInfoVO;

@Repository
public interface KidsInfoRepository extends UnieapRepository<KidsInfo> {

	@Query("select (count(*)+1) as code from KidsInfo ki,KidsClass kc where ki.id = kc.kidsId and kc.classId = ?1")
	String getCode(Long classId);

	List<KidsInfo> findByActivateFlag(String activateFlag);

	@Query(value = " select rr.* from "
			+ " ( select ki.id as id,ki.code as kids_code,ki.name as kids_name,ki.gender as gender,ki.birthday as birthday, "
			+ " ki.register_date as register_date,ki.source_type as source_type, kidsclass.class_Id as class_Id, "
			+ " kidsclass.class_name as class_name,kidsclass.grade_id as grade_id, kidscont.relationship, "
			+ " kidscont.contact_id as contact_id,kidscont.contact_name as contact_name,kidscont.phone_number as contact_number, "
			+ " bt.timecard_number as timecard_number   " + " from school_kids_info ki   "
			+ " left join (select kclass.kids_id,cinfo.id as class_id,  "
			+ " cinfo.name as class_name,cinfo.grade_id from  school_kids_class kclass,school_class_info cinfo "
			+ " where kclass.class_id = cinfo.id) kidsclass on ki.id = kidsclass.kids_id  " + " left join  "
			+ " (select kcontact.kids_id,kcontact.relationship,kcontact.priority, continfo.id as contact_id, continfo.name as contact_name, continfo.phone_number "
			+ " from school_kids_contact kcontact, school_contact_info continfo where kcontact.contact_id = continfo.id) kidscont  "
			+ " on ki.id = kidscont.kids_id "
			+ " left join ( select sbt.bind_id,group_concat(DISTINCT(sbt.timecard_number) Separator ',') as timecard_number "
			+ " from school_bind_timecard sbt group by sbt.bind_id) bt " + " on bt.bind_id = ki.id) rr "
			+ " where (rr.kids_name like CONCAT('%',?1,'%') or ?1 is null) and (rr.contact_name like CONCAT('%',?2,'%') or ?2 is null) and "
			+ "(rr.contact_number like CONCAT('%',?3,'%') or ?3 is null) and (rr.register_date >= ?4 or ?4 is null) and "
			+ "(rr.register_date <= ?5 or ?5 is null) and (grade_id = ?6 or ?6 is null) and (class_id = ?7 or ?7 is null) and "
			+ "(rr.timecard_number = ?8 or ?8 is null)",
			countQuery = "select count(*) from "
					+ " ( select ki.id as id,ki.code as kids_code,ki.name as kids_name,ki.gender as gender,ki.birthday as birthday, "
					+ " ki.register_date as register_date,ki.source_type as source_type, kidsclass.class_Id as class_Id, "
					+ " kidsclass.class_name as class_name,kidsclass.grade_id as grade_id, kidscont.relationship, "
					+ " kidscont.contact_id as contact_id,kidscont.contact_name as contact_name,kidscont.phone_number as contact_number, "
					+ " bt.timecard_number as timecard_number   " + " from school_kids_info ki   "
					+ " left join (select kclass.kids_id,cinfo.id as class_id,  "
					+ " cinfo.name as class_name,cinfo.grade_id from  school_kids_class kclass,school_class_info cinfo "
					+ " where kclass.class_id = cinfo.id) kidsclass on ki.id = kidsclass.kids_id  " + " left join  "
					+ " (select kcontact.kids_id,kcontact.relationship,kcontact.priority, continfo.id as contact_id, continfo.name as contact_name, continfo.phone_number "
					+ " from school_kids_contact kcontact, school_contact_info continfo where kcontact.contact_id = continfo.id) kidscont  "
					+ " on ki.id = kidscont.kids_id "
					+ " left join ( select sbt.bind_id,group_concat(DISTINCT(sbt.timecard_number) Separator ',') as timecard_number "
					+ " from school_bind_timecard sbt group by sbt.bind_id) bt " + " on bt.bind_id = ki.id) rr "
					+ " where (rr.kids_name like CONCAT('%',?1,'%') or ?1 is null) and (rr.contact_name like CONCAT('%',?2,'%') or ?2 is null) and "
					+ "(rr.contact_number like CONCAT('%',?3,'%') or ?3 is null) and (rr.register_date >= ?4 or ?4 is null) and "
					+ "(rr.register_date <= ?5 or ?5 is null) and (grade_id = ?6 or ?6 is null) and (class_id = ?7 or ?7 is null) and "
					+ "(rr.timecard_number = ?8 or ?8 is null)", nativeQuery = true)
	Page<KidsInfoVO> findKidsInfo(String kidsName, String contactName, String phoneNumber, Date registerDateStart,
			Date registerDateEnd, Long gradeId, Long classId, String timecardNumber, Pageable pageable);
}
