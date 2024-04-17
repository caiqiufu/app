package com.slipper.unieap.school.renyuanguanli.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.school.renyuanguanli.pojo.ContactInfo;
import com.slipper.unieap.school.renyuanguanli.vo.KidsContactVO;

@Repository
public interface ContactInfoRepository extends UnieapRepository<ContactInfo> {

	List<ContactInfo> findByActivateFlag(String activateFlag);
	
	@Query(value = "select con.*,fa.url as profile_url from(" + 
			"SELECT ci.id as id,kc.kids_id as kids_id,ki.name as kids_name,ci.id as contact_id," + 
			"ci.name as contact_name,ci.phone_number as contact_number,kc.relationship as relationship," + 
			"kc.priority as priority,ci.id_type as id_type,ci.id_number as id_number," + 
			"ci.nation as nation,ci.education as education,ci.occupation as occupation," + 
			"ci.email as email,ci.source_type as source_type,ci.profile_id,ci.activate_flag as activate_flag " + 
			" FROM school_kids_contact kc,school_contact_info ci,school_kids_info ki " + 
			" where kc.contact_id = ci.id and kc.kids_id = ki.id) con left join unieap_file_archive fa " + 
			" on con.profile_id = fa.id  where con.activate_flag =?1 and con.kids_id = ?2", nativeQuery = true)
	List<KidsContactVO> findContactInfoList(String activateFlag,Long kidsId);
	
	@Query(value = "select con.*,fa.url as profile_url from(" + 
			"SELECT ci.id as id,kc.kids_id as kids_id,ki.name as kids_name,ci.id as contact_id," + 
			"ci.name as contact_name,ci.phone_number as contact_number,kc.relationship as relationship," + 
			"kc.priority as priority,ci.id_type as id_type,ci.id_number as id_number," + 
			"ci.nation as nation,ci.education as education,ci.occupation as occupation," + 
			"ci.email as email,ci.source_type as source_type,ci.profile_id " + 
			" FROM school_kids_contact kc,school_contact_info ci,school_kids_info ki " + 
			" where kc.contact_id = ci.id and kc.kids_id = ki.id) con left join unieap_file_archive fa " + 
			" on con.profile_id = fa.id  where con.id = ?1", nativeQuery = true)
	KidsContactVO findContactInfo(Long id);
}
