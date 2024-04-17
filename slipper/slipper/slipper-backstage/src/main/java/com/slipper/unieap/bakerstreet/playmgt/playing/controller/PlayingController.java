package com.slipper.unieap.bakerstreet.playmgt.playing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.unieap.bakerstreet.playmgt.playing.bo.PlayingBO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/bakerstreet/playmgt/playing")
public class PlayingController extends CommonController {

	@Autowired
	PlayingBO playingBO;

	@RequestMapping("/roomlistPage")
	public R roomlistPage(PaginationSupport page) throws Exception {
		return R.success(playingBO.getRoomGridList(page).getMap());
	}

	@RequestMapping("/scriptlikelistPage")
	public R scriptlikelistPage(PaginationSupport page) throws Exception {
		return R.success(playingBO.getScriptLikeGridList(page).getMap());
	}
}
