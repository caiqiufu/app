package com.slipper.unieap.bakerstreet.playmgt.dmlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.bakerstreet.playmgt.dmlog.bo.DMLogBO;
import com.slipper.unieap.bakerstreet.playmgt.player.PlayerInfoBO;
import com.slipper.unieap.bakerstreet.playmgt.pojo.PlayerInfo;
import com.slipper.unieap.bakerstreet.playmgt.vo.DmShowVO;
import com.slipper.unieap.bakerstreet.playmgt.vo.ScriptShowVO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/bakerstreet/playmgt/dmlog")
public class DMLogController extends CommonController {

	@Autowired
	DMLogBO dMLogBO;

	@Autowired
	PlayerInfoBO playerInfoBO;

	@Log("查看DM浏览信息")
	@RequestMapping("/browsePage")
	public R browsePage(PaginationSupport page, PlayerInfo vo) throws Exception {
		return R.success(dMLogBO.getDMBrowseGridList(page, vo).getMap());
	}

	@Log("查看DM玩信息")
	@RequestMapping("/playPage")
	public R playPage(PaginationSupport page, PlayerInfo vo) throws Exception {
		return R.success(dMLogBO.getDMPlayGridList(page, vo).getMap());
	}

	@RequestMapping("/info")
	public R info(PlayerInfo vo) throws Exception {
		return R.success(dMLogBO.getInfo(vo.getId()));
	}

	@RequestMapping("/getDMDicData")
	public R getDMDicData(PlayerInfo vo) {
		return R.success(dMLogBO.getDMDicDataList(vo));
	}

	@RequestMapping("/scriptShowPage")
	public R scriptShowPage() {
		return R.success(dMLogBO.getScriptShowList());
	}

	@Log("更新新剧本列表")
	@RequestMapping("/updateScriptShow")
	public R updateScriptShow(ScriptShowVO vo) throws Exception {
		dMLogBO.updateScriptShow(vo);
		return R.success();
	}

	@RequestMapping("/dmShowPage")
	public R dmShowPage() {
		return R.success(dMLogBO.getDmShowList());
	}

	@Log("更新新优秀DM列表")
	@RequestMapping("/updateDmShow")
	public R updateDmShow(DmShowVO vo) throws Exception {
		dMLogBO.updateDmShow(vo);
		return R.success();
	}

	@RequestMapping("/getDMShowDicData")
	public R getDMShowDicData(PlayerInfo vo) {
		return R.success(playerInfoBO.getPlayerDicDataList(vo));
	}
}
