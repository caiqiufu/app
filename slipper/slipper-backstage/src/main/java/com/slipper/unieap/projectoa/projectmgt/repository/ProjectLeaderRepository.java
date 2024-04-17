package com.slipper.unieap.projectoa.projectmgt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.projectoa.projectmgt.pojo.ProjectLeader;

@Repository
public interface ProjectLeaderRepository extends UnieapRepository<ProjectLeader> {
	
	
	@Query(value = "select e from ProjectLeader e where e.projectId = ?1")
	List<ProjectLeader> getProjectLeaderList(Long projectId);
}
