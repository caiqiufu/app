package com.slipper.unieap.projectoa.supervisionmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.TaskDefine;

@Repository
public interface TaskDefineRepository extends UnieapRepository<TaskDefine> {
	@Query("SELECT td FROM TaskDefine td WHERE (td.name like CONCAT('%',?1,'%') or ?1 is null)")
	List<TaskDefine> getAll(String taskName);
}
