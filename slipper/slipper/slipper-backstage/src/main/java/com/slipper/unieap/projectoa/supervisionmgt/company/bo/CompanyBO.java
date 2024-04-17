package com.slipper.unieap.projectoa.supervisionmgt.company.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.projectoa.supervisionmgt.pojo.CompanyInfo;
import com.slipper.unieap.projectoa.supervisionmgt.repository.CompanyInfoRepository;

@Service
public class CompanyBO extends BaseBO {

	@Autowired
	public CompanyInfoRepository companyInfoRepository;
	public List<DicDataVO> getCompanyDicDataList() {
		List<CompanyInfo> datas = companyInfoRepository.findAll(Sort.by(Direction.ASC, "name"));
		if (datas != null && datas.size() > 0) {
			List<DicDataVO> dataList = new ArrayList<DicDataVO>();
			for (CompanyInfo data : datas) {
				DicDataVO datavo = new DicDataVO();
				datavo.setActivateFlag(data.getActivateFlag());
				datavo.setDicCode(data.getId().toString());
				datavo.setDicName(data.getName());
				dataList.add(datavo);
			}
			return dataList;
		} else {
			return new ArrayList<DicDataVO>();
		}
	}
}
