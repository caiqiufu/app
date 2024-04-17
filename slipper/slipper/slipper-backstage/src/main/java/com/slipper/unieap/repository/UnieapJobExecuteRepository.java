package com.slipper.unieap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.pojo.UnieapJobExecute;

@Repository
public interface UnieapJobExecuteRepository extends JpaRepository<UnieapJobExecute, Long> {
}
