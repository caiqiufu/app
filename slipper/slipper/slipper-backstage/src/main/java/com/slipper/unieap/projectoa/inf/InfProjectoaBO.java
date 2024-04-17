package com.slipper.unieap.projectoa.inf;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.projectoa.projectmgt.bo.ProjectInfoBO;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.EmployeeInfo;
import com.slipper.unieap.projectoa.supervisionmgt.repository.EmployeeInfoRepository;

@Service
public class InfProjectoaBO extends BaseBO {

	@Autowired
	BaseBO baseBO;

	@Autowired
	ProjectInfoBO projectInfoBO;

	@Autowired
	EmployeeInfoRepository employeeInfoRepository;

	/**
	 * PROJECT_LIST
	 */
	public List<DicDataVO> getDataList(String groupCode, String params) {
		JSONObject json = null;
		if (StringUtils.isNotEmpty(params)) {
			json = JSONObject.parseObject(params);
		}
		if (StringUtils.equals(groupCode, "PROJECT_LIST")) {
			if (json == null || StringUtils.isEmpty(json.getString("parentId"))) {
				return projectInfoBO.getProjectDicDataList(null);
			} else {
				return projectInfoBO.getProjectDicDataList(Long.decode(json.getString("parentId")));
			}
		} else if (StringUtils.equals(groupCode, "BUILDING_PART")) {
			String ps = json.getString("projectId");
			return projectInfoBO.getBuildingPartDicDataList(Long.decode(ps));
		} else {
			return baseBO.getDicGroup(groupCode).getDataList();
		}
	}

	@Value("${wxapi.appid}")
	String wxapi_appid;
	@Value("${wxapi.secret}")
	String wxapi_secret;
	@Value("${wxapi.url}")
	String wxapi_url;

	public EmployeeInfo loginWithWX(String code) {
		String url = MessageFormat.format(wxapi_url, code);
		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(url, String.class);
		JSONObject exRsp = JSONObject.parseObject(result);
		// logger.debug(exRsp.toString());
		String openId = exRsp.getString("openid");
		// String sessionKey = exRsp.getString("session_key");
		if (StringUtils.isNotEmpty(openId)) {
			EmployeeInfo employeeInfo = employeeInfoRepository.findByWxauId(openId);
			if (employeeInfo == null) {
				employeeInfo = new EmployeeInfo();
				employeeInfo.setId(UnieapConstants.getSeq(null));
				employeeInfo.setCompanyId(Long.decode("999"));
				employeeInfo.setWxauId(openId);
				employeeInfo.setName(openId);
				employeeInfo.setActivateFlag(UnieapConstants.YES);
				employeeInfo.setTenantId(UnieapConstants.getTenantId());
				employeeInfoRepository.save(employeeInfo);
			}
			return employeeInfo;
		} else {
			return null;
		}
	}

	public EmployeeInfo updateEmployeeName(String wxauId, String name) {
		EmployeeInfo employeeInfo = employeeInfoRepository.findByWxauId(wxauId);
		employeeInfo.setName(name);
		employeeInfoRepository.save(employeeInfo);
		return employeeInfo;
	}
}
