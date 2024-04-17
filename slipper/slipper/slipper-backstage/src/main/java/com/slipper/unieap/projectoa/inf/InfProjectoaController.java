package com.slipper.unieap.projectoa.inf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.slipper.common.utils.R;
import com.slipper.service.annotation.Log;
import com.slipper.unieap.ApplicationContextProvider;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.controller.CommonController;
import com.slipper.unieap.extapi.whether.WhetherBO;
import com.slipper.unieap.projectoa.delivery.bo.JldailyLogBO;
import com.slipper.unieap.projectoa.delivery.pojo.ApproveLog;
import com.slipper.unieap.projectoa.delivery.vo.JldailyLogVO;
import com.slipper.unieap.projectoa.projectmgt.bo.ProjectInfoBO;
import com.slipper.unieap.vo.PaginationSupport;

@RestController
@RequestMapping("/backstage/projectoa/inf")
public class InfProjectoaController extends CommonController {

	@Autowired
	InfProjectoaBO infProjectoaBO;

	@Autowired
	JldailyLogBO jldailyLogBO;

	@Autowired
	WhetherBO whetherBO;

	@Autowired
	BaseBO baseBO;
	
	@Autowired
	ProjectInfoBO projectInfoBO;

	/**
	 * 字典值查询 PROJECT_LIST 项目、标段 {parentId:''} WEATHER_TYPE 天气情况 BUILDING_PART 施工部位
	 * 
	 * @api {GET} /backstage/projectoa/inf/getDicData RequestExample 请求参数示例
	 *      {'groupCode':''}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    [{ dicCode: '', // ID dicName:'', //Name groupCode: '',
	 *                    //分类编码 groupName:''//分类名称 }] }
	 */
	@RequestMapping("/getDicData")
	public R getDicData(String groupCode, String params) {
		return R.success(infProjectoaBO.getDataList(groupCode, params));
	}

	/**
	 * 创建监理日志
	 * 
	 * @api {POST} /backstage/projectoa/inf/create RequestExample 请求参数示例
	 *      {JldailyLogVO 的属性:''}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    }
	 */
	@RequestMapping("/create")
	public R create(@RequestBody JldailyLogVO vo) throws Exception {
		jldailyLogBO.create(vo);
		return R.success();
	}

	/**
	 * 修改监理日志
	 * 
	 * @api {POST} /backstage/projectoa/inf/update RequestExample 请求参数示例
	 *      {JldailyLogVO 的属性:''}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    }
	 */

	@Log("修改监理日志")
	@RequestMapping("/update")
	public R update(@RequestBody JldailyLogVO vo) throws Exception {
		jldailyLogBO.update(vo);
		return R.success();
	}
	

	/**
	 * 审核监理日志
	 * 
	 * @api {POST} /backstage/projectoa/inf/approve RequestExample 请求参数示例
	 *      {ApproveLog 的属性:''}
	 * @apiSuccessExample 响应结果示例 { code: 0, status: 'success', message: '成功!', data:
	 *                    }
	 */
	@Log("审核日志")
	@RequestMapping("/approve")
	public R approve(@RequestBody ApproveLog vo) throws Exception {
		jldailyLogBO.approve(vo);
		return R.success();
	}

	@Log("监理日志明细")
	@RequestMapping("/info")
	public R info(JldailyLogVO vo) throws Exception {
		return R.success(jldailyLogBO.getInfo(vo));
	}
	
	@RequestMapping("/projectInfo")
	public R projectInfo(Long projectId) throws Exception {
		return R.success(projectInfoBO.getInfo(projectId));
	}

	@Log("监理日志列表")
	@RequestMapping("/page")
	public R page(PaginationSupport page, JldailyLogVO vo) throws Exception {
		return R.success(jldailyLogBO.getGridList(page, vo).getMap());
	}

	@Log("天气情况")
	@RequestMapping("/whetherInfo")
	public R whetherInfo(String lat, String lon, int type) throws Exception {
		return R.success(whetherBO.getWhetherInfo(lat, lon, type));
	}
	
	@RequestMapping("/delete")
	public R delete(Long logId) throws Exception {
		Object repository = ApplicationContextProvider.getBean("jldailyLogRepository");
		jldailyLogBO.delete(repository, logId);
		return R.success();
	}
	
	@RequestMapping("/loginWithWX")
	public R loginWithWX(String code) {
		return R.success(infProjectoaBO.loginWithWX(code));
	}
	@RequestMapping("/updateEmplyeeName")
	public R updateEmployeeName(String wxauId,String name) {
		return R.success(infProjectoaBO.updateEmployeeName(wxauId,name));
	}
}
