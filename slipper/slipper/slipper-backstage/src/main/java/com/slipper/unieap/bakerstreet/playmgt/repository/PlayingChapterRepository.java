package com.slipper.unieap.bakerstreet.playmgt.repository;

import org.springframework.stereotype.Repository;

import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayingChapter;
import com.slipper.unieap.db.UnieapRepository;

@Repository
public interface PlayingChapterRepository extends UnieapRepository<PlayingChapter> {

	void deleteByPlayingIdAndRoleIdAndChapterId(Long playingId, Long roleId, Long chapterId);

}
