package com.slipper.unieap.projectoa.supervisionmgt.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.EmployeeInfo;
import com.slipper.unieap.projectoa.supervisionmgt.vo.EmployeeInfoVO;

@Repository
public interface EmployeeInfoRepository extends UnieapRepository<EmployeeInfo> {
	@Query(value = "SELECT ei.id as id,ei.companyId as companyId,ci.name as companyName,"
			+ "ei.name as name,ei.gender as gender,ei.avatarUrl as avatarUrl,ei.position as position,"
			+ "ei.idNumber as idNumber,ei.phone as phone,ei.wx as wx,ei.certificateType as certificateType,"
			+ "ei.certificateLicense as certificateLicense"
			+ " FROM EmployeeInfo ei,CompanyInfo ci where ei.companyId = ci.id and "
			+ " (ei.name like CONCAT('%',?1,'%') or ?1 is null) and  (ci.name like CONCAT('%',?2,'%') or ?2 is null)", countQuery = "SELECT count(1) "
					+ "FROM EmployeeInfo ei,CompanyInfo ci where ei.companyId = ci.id and "
					+ " (ei.name like CONCAT('%',?1,'%') or ?1 is null) and  (ci.name like CONCAT('%',?2,'%') or ?2 is null)")
	Page<EmployeeInfoVO> getEmployeeInfoPage(String employeeName, String projectName, Pageable pageable);
	
	@Query("SELECT ei FROM EmployeeInfo ei,CompanyInfo ci WHERE ei.companyId = ci.id AND ci.type=?1 ")
	List<EmployeeInfo> getAllEmployeeInfoByCompanyTypeList(String type);
	
	EmployeeInfo findByWxauId(String wxauId);
	
}
