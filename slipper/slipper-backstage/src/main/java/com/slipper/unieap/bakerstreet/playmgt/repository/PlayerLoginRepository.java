package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerLogin;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayerLoginRepository extends UnieapRepository<PlayerLogin> {

}
