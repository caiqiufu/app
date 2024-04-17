package com.slipper.unieap.bakerstreet.inf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.slipper.common.utils.R;
import com.slipper.unieap.bakerstreet.inf.vo.ScriptDetailInfoVO;
import com.slipper.unieap.bakerstreet.playmgt.playing.bo.PlayingBO;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.file.bo.FileBO;
import com.slipper.unieap.file.pojo.UnieapFileArchive;

///backstage/infmgt/** 为免鉴权目录
@RestController
@RequestMapping("/backstage/bakerstreet/inf")
public class InfController extends CommonController {

	@Autowired
	InfBO infBO;

	@Autowired
	FileBO fileBO;

	@Autowired
	PlayingBO playingBO;

	@Autowired
	BaseBO baseBO;

	/**
	 * Interface description 新本展示列表
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/getDicData RequestExample 请求参数示例
	 *      {'groupCode':''}//SCRIPT_TYPE:剧本类型 SCRIPT_CATEGORY:剧本类别
	 *      SCRIPT_DURATION:时间 SCRIPT_PLAYERCOUNT:人数 SCRIPT_CHAPTER:剧本幕数
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{ dicCode: '', // ID dicName:'', //Name groupCode: '',
	 *                    //分类编码 groupName:''//分类名称 }] }
	 */
	@RequestMapping("/getDicData")
	public R getDicData(String groupCode, String params) {
		return R.success(baseBO.getDicGroup(groupCode).getDataList());
	}

	/**
	 * Interface description 新本展示列表
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/newScriptList RequestExample 请求参数示例 {}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{ scriptId: '', // 剧本ID scriptName: '', //剧本名称 posterUrl:
	 *                    '', // 剧本海报图片连接 }] }
	 */
	@RequestMapping("/newScriptList")
	public R newScriptList() {
		return R.success(infBO.getNewScriptList());
	}

	/**
	 * Interface description 1.全部剧本列表 2.剧本名称模糊查询，类别、类型查询 3.DM可开本标记
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/scriptList RequestExample 请求参数示例 {
	 *      ID:123, scriptName:'123', typeList:'1,2',//参考字典值SCRIPT_TYPE
	 *      category:'2'//参考字典值SCRIPT_CATEGORY }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{ scriptId: '', // 剧本ID scriptName: '', //剧本名称 posterUrl:
	 *                    '', // 剧本海报图片连接 }] }
	 */
	@RequestMapping("/scriptList")
	public R scriptList(String ID, ScriptDetailInfoVO vo) {
		return R.success(infBO.getScriptList(ID, vo));
	}

	/**
	 * Interface description 获取剧本明细
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/scriptInfo RequestExample 请求参数示例 {
	 *      scriptId:123 }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    { scriptId:123, scriptName:'1122', posterUrl:'http://',
	 *                    typeList:'',//多个以逗号分割 typeListDesc:'',多个值逗号分割
	 *                    category:'',//多个以逗号分割 categoryDesc:'',多个值逗号分割
	 *                    duration:'',//多个以逗号分割 durationDesc:'',多个值逗号分割
	 *                    playerCount:5, roleList:[
	 *                    {id:'',name:'',sex:'',sexDesc:'',brief:'',avatarUrl:''} ],
	 *                    clueList:[ {id:'',name:'',clueName:'',sexDesc:'',url:''}
	 *                    ], guidebookList:[ {id:'',name:'':'',url:''} ] } }
	 */
	@RequestMapping("/scriptInfo")
	public R scriptInfo(Long scriptId) {
		return R.success(infBO.getScriptInfo(scriptId));
	}

	/**
	 * Interface description 获取玩家列表信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerList RequestExample 请求参数示例 {
	 *      playerType:''//玩家类别:1:普通玩家,2:DM,3:优秀DM列表 }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{id:'',name:'',avatarUrl:''}] }
	 */
	@RequestMapping("/playerList")
	public R playerList(String playerType) {
		return R.success(infBO.getPlayerList(playerType));
	}

	/**
	 * Interface description 文件上传
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/upload RequestExample 请求参数示例 {
	 *      extKey:''//业务主键 category:''//文件分类}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{id:'',url:''}] }
	 */

	@RequestMapping("/upload")
	public R upload(UnieapFileArchive vo, MultipartHttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, MultipartFile> fileMap = request.getFileMap();
		List<UnieapFileArchive> files = fileBO.upload(vo, fileMap);
		return R.success(files);
	}

	/**
	 * Interface description 玩家登录或者注册
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/loginOrRegister RequestExample 请求参数示例 {
	 *      ID:'',//玩家ID wxauId:'',//微信唯一标识 name:'',weixin:'',fileId:'',avatarUrl:''
	 *      }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false } }
	 */
	@RequestMapping("/loginOrRegister")
	public R loginOrRegister(com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO vo) {
		return R.success(infBO.loginOrRegister(vo));
	}

	/**
	 * Interface description 玩家微信登录
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/loginWithWX RequestExample 请求参数示例 {
	 *      code:''}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false } }
	 */
	@RequestMapping("/loginWithWX")
	public R loginWithWX(String code) {
		return R.success(infBO.loginWithWX(code));
	}

	/**
	 * Interface description 修改玩家信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/updatePlayerInfo RequestExample 请求参数示例
	 *      { ID:'',//玩家ID wxauId:'',//微信唯一标识 name:'',sex:'',weixin:'',avatarurl:'',
	 *      }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false } }
	 */
	@RequestMapping("/updatePlayerInfo")
	public R updatePlayerInfo(com.slipper.unieap.bakerstreet.inf.vo.PlayerInfoVO vo) {
		return R.success(infBO.updatePlayerInfo(vo));
	}

	/**
	 * Interface description 玩家信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerInfo RequestExample 请求参数示例 {
	 *      ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {id:'',code:'',name:'',avatarUrl:'',weixin:'',registerDate:'',playCount:'',
	 *                    playerScriptList:[{scriptId:'',scriptName:'',posterUrl:'',playTime:''}],
	 *                    dmScriptCount:'',
	 *                    dmScriptList:[{scriptId:'',scriptName:'',posterUrl:'',playTime:''}],
	 *                    } }
	 */
	@RequestMapping("/playerInfo")
	public R playerInfo(String ID) {
		return R.success(infBO.getPlayerInfo(ID));
	}

	/**
	 * Interface description DM获取可以开本的剧本列表
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/createRoom RequestExample 请求参数示例 {
	 *      ID:'',scriptId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{roomId:'',roomName:'',password:''}] }
	 */
	@RequestMapping("/createRoom")
	public R createRoom(String ID, Long scriptId) {
		return R.success(infBO.createDmRoom(ID, scriptId));
	}

	/**
	 * Interface description 玩家加入房间验证
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/roomInfo RequestExample 请求参数示例 {
	 *      roomId:123}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false|error info } }
	 */
	@RequestMapping("/roomInfo")
	public R roomInfo(Long roomId) {
		return R.success(infBO.roomInfo(roomId));
	}

	/**
	 * Interface description 玩家加入房间验证
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/joinRoom RequestExample 请求参数示例 {
	 *      ID:123,roomId:123,verifyCode:'4456' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false|error info } }
	 */
	@RequestMapping("/joinRoom")
	public R joinRoom(String ID, Long roomId, String verifyCode) {
		return R.success(infBO.joinRoom(ID, roomId, verifyCode));
	}

	/**
	 * Interface description 把玩家踢出房间
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/kickOutPlayer RequestExample 请求参数示例 {
	 *      roomId:123,ID:123 }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false } }
	 */
	@RequestMapping("/kickOutPlayer")
	public R kickOutPlayer(Long roomId, String ID) {
		return R.success(infBO.kickOutRoom(ID, roomId));
	}

	/**
	 * Interface description 房间玩家列表
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/roomPlayerList RequestExample 请求参数示例 {
	 *      roomId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{playerId:'',playerName:'',avatarUrl:'',playerType:'',playerTypeDesc:''}]
	 *                    }
	 */
	@RequestMapping("/roomPlayerList")
	public R roomPlayerList(Long roomId) {
		return R.success(infBO.getPlayingPlayerList(roomId));
	}

	/**
	 * Interface description 分配角色
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/assignRole RequestExample 请求参数示例 {
	 *      roomId:'',ID:'',roleId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{result:true|false}] }
	 */
	@RequestMapping("/assignRole")
	public R assignRole(Long roomId, String ID, Long roleId) {
		return R.success(infBO.assignPlayerRole(roomId, ID, roleId));
	}

	/**
	 * Interface description 开始游戏
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playStart RequestExample 请求参数示例 {
	 *      roomId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/playStart")
	public R playStart(Long roomId) {
		playingBO.playingStart(roomId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 结束游戏
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playEnd RequestExample 请求参数示例 {
	 *      roomId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/playEnd")
	public R playEnd(Long roomId) {
		playingBO.playingEnd(roomId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 获取玩家在房间中的角色信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerRole RequestExample 请求参数示例 {
	 *      roomId:'',ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {name:'',sex:'',avatarUrl:''} }
	 */
	@RequestMapping("/playerRole")
	public R playerRole(Long roomId, String ID) {
		return R.success(infBO.getPlayerRoleDetail(roomId, ID));
	}

	/**
	 * Interface description DM发放剧本幕给玩家
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/assignChapter RequestExample 请求参数示例 {
	 *      roomId:'',chapterNumber:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/assignChapter")
	public R assignChapter(Long roomId, String chapterNumber) {
		playingBO.assignPlayerChapter(roomId, chapterNumber);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description DM回收剧本
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/recoverChapter RequestExample 请求参数示例 {
	 *      roomId:'',chapterNumber:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/recoverChapter")
	public R recoverChapter(Long roomId, String chapterNumber) {
		playingBO.recoverChapter(roomId, chapterNumber);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description DM发放线索给玩家
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/assignClueDetail RequestExample 请求参数示例
	 *      { roomId:'',roleId:'',clueDetailIdList:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/assignClueDetail")
	public R assignClueDetail(Long roomId, Long roleId, String clueDetailIdList) {
		List<Long> lkeys = convertKeys(clueDetailIdList);
		playingBO.assignPlayerClue(roomId, roleId, lkeys);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description DM回收线索
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/assignClueDetail RequestExample 请求参数示例
	 *      { roomId:'',roleId:'',clueDetailIdList:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/recoverClueDetail")
	public R recoverClueDetail(Long roomId, Long roleId, String clueDetailIdList) {
		List<Long> lkeys = convertKeys(clueDetailIdList);
		playingBO.recoverClueDetail(roomId, roleId, lkeys);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 玩家在房间中的信息，包括角色信息，剧本幕信息，线索幕信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerPlayingInfo RequestExample 请求参数示例
	 *      { roomId:'',ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {roleId:'',roleName:'',avatarUrl:'',chapterList:[{chapterNumber:'',url:''}],clueList:[{clueName:'',detailName:'',url:''}]}
	 *                    }
	 */
	@RequestMapping("/playerPlayingInfo")
	public R playerPlayingInfo(Long roomId, String ID) {
		return R.success(infBO.getPlayingInfo(roomId, ID));
	}

	/**
	 * Interface description 交换线索
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerSwapClue RequestExample 请求参数示例 {
	 *      roomId:'',ID:'',clueDetailIdList:''//clueDetailId
	 *      多个逗号分割,receiverList:''//玩家ID多个逗号分割 }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/playerSwapClue")
	public R playerSwapClue(Long roomId, String ID, String clueDetailIdList, String receiverList) {
		List<Long> lkeys = convertKeys(clueDetailIdList);
		infBO.playerSwapClue(roomId, ID, lkeys, receiverList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 交换线索
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/playerSwapClue RequestExample 请求参数示例 {
	 *      roomId:'',ID:'',clueIdList:'',receiverList:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/playeRecoverClue")
	public R playeRecoverClue(Long roomId, String ID, String clueDetailIdList, String receiverList) {
		List<Long> lkeys = convertKeys(clueDetailIdList);
		infBO.playeRecoverClue(roomId, ID, lkeys, receiverList);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 剧本点赞
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/scriptLike RequestExample 请求参数示例 {
	 *      ID:'',scriptId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/scriptLike")
	public R scriptLike(String ID, Long scriptId) {
		infBO.scriptLike(ID, scriptId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 剧本取消点赞
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/scriptUnLike RequestExample 请求参数示例 {
	 *      ID:'',scriptId:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/scriptUnLike")
	public R scriptUnLike(String ID, Long scriptId) {
		infBO.scriptUnLike(ID, scriptId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", true);
		return R.success(result);
	}

	/**
	 * Interface description 玩家打过的剧本
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/myPlayedScriptList RequestExample
	 *      请求参数示例 { ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{url:'',scriptName:''}] }
	 */
	@RequestMapping("/myPlayedScriptList")
	public R myPlayedScriptList(String ID) {
		return R.success(infBO.getMyPlayedScriptList(ID));
	}

	/**
	 * Interface description 玩家点赞的剧本
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/myLikedScriptList RequestExample 请求参数示例
	 *      { ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{url:'',scriptName:''}] }
	 */
	@RequestMapping("/myLikedScriptList")
	public R myLikedScriptList(String ID) {
		return R.success(infBO.getMyLikeScriptList(ID));
	}

	/**
	 * Interface description 剧本点赞榜
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/scriptLikeList RequestExample 请求参数示例 {}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{url:'',scriptName:'',likeNum:''}] }
	 */
	@RequestMapping("/scriptLikeList")
	public R scriptLikeList() {
		return R.success(infBO.getScriptLikeList());
	}

	/**
	 * Interface description 获取升级为DM的激活码
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/getDmActivateCode RequestExample 请求参数示例
	 *      { ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {activateCode:''} }
	 */
	@RequestMapping("/getDmActivateCode")
	public R getDmActivateCode(String ID) {
		return R.success(infBO.generateDMActivateCode(ID));
	}

	/**
	 * Interface description 玩家打过的剧本
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/upgradeToDm RequestExample 请求参数示例 {
	 *      ID:'',activateCode:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    {result:true|false} }
	 */
	@RequestMapping("/upgradeToDm")
	public R upgradeToDm(String ID, String activateCode) {
		return R.success(infBO.upgradeToDm(ID, activateCode));
	}

	/**
	 * Interface description DM开本记录
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/myRoomList RequestExample 请求参数示例 {
	 *      ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{scriptName:'',url,playTime:''}] }
	 */
	@RequestMapping("/myRoomList")
	public R myRoomList(String ID) {
		return R.success(infBO.getMyRoomList(ID));
	}

	/**
	 * Interface description 玩家当前加入的房间信息
	 * 
	 * @api {GET} /backstage/bakerstreet/inf/myPlayingRoomInfo RequestExample 请求参数示例
	 *      { ID:'' }
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{url:'',scriptName:''}] }
	 */
	@RequestMapping("/myPlayingRoomInfo")
	public R myPlayingRoomInfo(String ID) {
		return R.success(infBO.getPlayerPlayingRoomInfo(ID));
	}
}
