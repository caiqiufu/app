package com.slipper.unieap.school.renyuanguanli.zhigong.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.school.renyuanguanli.pojo.StaffInfo;
import com.slipper.unieap.school.renyuanguanli.repository.StaffInfoRepository;

@Service
public class StaffInfoBO extends BaseBO {

	@Autowired
	public StaffInfoRepository staffInfoRepository;

	public List<DicDataVO> getStaffInfoDicDataList() {
		List<StaffInfo> datas = staffInfoRepository.findByActivateFlag(UnieapConstants.YES);
		List<DicDataVO> dataList = new ArrayList<DicDataVO>();
		for (StaffInfo data : datas) {
			DicDataVO datavo = new DicDataVO();
			datavo.setActivateFlag(data.getActivateFlag());
			datavo.setDicCode(data.getId().toString());
			datavo.setDicName(data.getName());
			dataList.add(datavo);
		}
		return dataList;
	}
}
