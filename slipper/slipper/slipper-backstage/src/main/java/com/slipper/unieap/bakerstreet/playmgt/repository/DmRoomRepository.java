package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.playmgt.pojo.DmRoom;
import com.slipper.unieap.bakerstreet.playmgt.vo.RoomInfoVO;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface DmRoomRepository extends UnieapRepository<DmRoom> {

	@Query(value = "select dr.id as id,dr.player_id as playerId,dr.script_id as scriptId,dr.room_name as roomName,pi.name as playerName,si.name as scriptName,dr.start_date as startDate,dr.end_Date as endDate from bakerstreet_dm_room dr,bakerstreet_player_info pi,bakerstreet_script_info si where dr.script_id=si.id and dr.player_id = pi.id and to_days(dr.start_date)=to_days(now()) order by dr.start_date", countQuery = "select count(*) from bakerstreet_dm_room dr,bakerstreet_player_info pi,bakerstreet_script_info si where dr.script_id=si.id and dr.player_id = pi.id and to_days(dr.start_date)=to_days(now())", nativeQuery = true)
	Page<RoomInfoVO> getRoomlist(Pageable pageable);
}
