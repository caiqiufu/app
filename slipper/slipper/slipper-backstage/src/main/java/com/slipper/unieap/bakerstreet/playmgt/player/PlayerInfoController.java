package com.slipper.unieap.bakerstreet.playmgt.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.vo.PlayerInfoVO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/bakerstreet/playmgt/player")
public class PlayerInfoController extends CommonController {

	@Autowired
	PlayerInfoBO playerInfoBO;

	@RequestMapping("/getPlayerDicData")
	public R getPlayerDicData(PlayerInfo vo) {
		return R.success(playerInfoBO.getPlayerDicDataList(vo));
	}

	@Log("更新玩家黑名单状态")
	@RequestMapping("/updateBlacklistFlag")
	public R updateBlacklistFlag(PlayerInfoVO vo) throws Exception {
		playerInfoBO.updateBlacklistFlag(vo);
		return R.success();
	}
	
	@Log("更新玩家类型")
	@RequestMapping("/updatePlayerType")
	public R updatePlayerType(PlayerInfoVO vo) throws Exception {
		playerInfoBO.updatePlayerType(vo);
		return R.success();
	}

	@Log("更新DM开本权限状态")
	@RequestMapping("/updateScriptFlag")
	public R updateScriptFlag(PlayerInfoVO vo) throws Exception {
		playerInfoBO.updateScriptFlag(vo);
		return R.success();
	}

	@Log("更新回到玩家状态")
	@RequestMapping("/updateChangeFlag")
	public R updateChangeFlag(PlayerInfoVO vo) throws Exception {
		playerInfoBO.updateChangeFlag(vo);
		return R.success();
	}

	@RequestMapping("/playerPage")
	public R playerPage(PaginationSupport page, PlayerInfoVO vo) throws Exception {
		return R.success(playerInfoBO.getGridList(page, vo).getMap());
	}
	
	@RequestMapping("/blacklistPage")
	public R blacklistPage(PaginationSupport page, PlayerInfoVO vo) throws Exception {
		return R.success(playerInfoBO.getBlacklistGridList(page, vo).getMap());
	}
}
