package com.slipper.unieap.bakerstreet.scriptmgt.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.scriptmgt.pojo.ClueDetail;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface ClueDetailRepository extends UnieapRepository<ClueDetail> {

	@Query("select COALESCE((max(convert(cd.code,signed))+1),1) from ClueDetail cd,ClueInfo ci where cd.clueId = ci.id and ci.scriptId = ?1")
	Long getSeq(Long scriptId);
	
}
