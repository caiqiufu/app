package com.slipper.unieap.school.renyuanguanli.baobei.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.school.renyuanguanli.pojo.DistrictInfo;
import com.slipper.unieap.school.renyuanguanli.repository.DistrictInfoRepository;

@Service
public class DistrictInfoBO extends BaseBO {

	@Autowired
	public DistrictInfoRepository districtInfoRepository;

	public List<DicDataVO> getDistrictDicDataList(Long parentId) {
		List<DistrictInfo> datas;
		if (parentId == null) {
			return new ArrayList<DicDataVO>();

		} else {
			datas = districtInfoRepository.findByParentId(parentId);
		}
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (DistrictInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getDistrict());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}
}
