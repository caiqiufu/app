package com.slipper.unieap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.pojo.UnieapOrder;

@Repository
public interface UnieapOrderRepository extends JpaRepository<UnieapOrder, Long> {


}
