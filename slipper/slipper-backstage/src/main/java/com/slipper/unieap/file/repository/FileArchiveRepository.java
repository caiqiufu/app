package com.slipper.unieap.file.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.db.UnieapRepository;
import com.slipper.unieap.file.pojo.UnieapFileArchive;

@Repository
public interface FileArchiveRepository extends UnieapRepository<UnieapFileArchive> {

	List<UnieapFileArchive> findByExtKeyAndCategory(String extKey, String category);

}
