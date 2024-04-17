package com.slipper.unieap.school.jiaowuguanli.nianji.bo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.db.Criteria;
import com.slipper.unieap.db.Restrictions;
import com.slipper.unieap.mdm.dic.vo.DicDataVO;
import com.slipper.unieap.school.jiaowuguanli.pojo.GradeInfo;
import com.slipper.unieap.school.jiaowuguanli.repository.ClassInfoRepository;
import com.slipper.unieap.school.jiaowuguanli.repository.GradeInfoRepository;
import com.slipper.unieap.school.jiaowuguanli.vo.GradeInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class GradeBO extends BaseBO {

	@Autowired
	public GradeInfoRepository gradeInfoRepository;

	@Autowired
	public ClassInfoRepository classInfoRepository;

	public List<DicDataVO> getGradeDicDataList() {
		List<GradeInfo> datas = gradeInfoRepository.findByActivateFlag(UnieapConstants.YES);
		List<DicDataVO> dataList = new ArrayList<DicDataVO>();
		for (GradeInfo data : datas) {
			DicDataVO datavo = new DicDataVO();
			datavo.setActivateFlag(data.getActivateFlag());
			datavo.setDicCode(data.getId().toString());
			datavo.setDicName(data.getName());
			dataList.add(datavo);
		}
		return dataList;
	}

	public PaginationSupport getGridList(PaginationSupport page, GradeInfoVO vo) throws Exception {
		Criteria<GradeInfo> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("id", vo.getId(), true));
		gradeInfoRepository.getPaginationDataByMysql(page, criteria);
		if (page.items != null && page.items.size() > 0) {
			List<GradeInfoVO> nItems = new ArrayList<GradeInfoVO>();
			for (Object obj : page.items) {
				GradeInfo gradeInfo = (GradeInfo) obj;
				GradeInfoVO gradeInfoVO = new GradeInfoVO();
				nItems.add(gradeInfoVO);
				gradeInfoVO.setId(gradeInfo.getId());
				gradeInfoVO.setName(gradeInfo.getName());
				gradeInfoVO.setCreateDate(gradeInfo.getCreateDate());
				Long classCount = classInfoRepository.countByGradeIdAndGraduationFlag(gradeInfo.getId(),
						UnieapConstants.NO);
				gradeInfoVO.setClassCount(classCount);
			}
			page.items = nItems;
		}
		return page;
	}

	public GradeInfo getInfo(Long id) {
		return gradeInfoRepository.findById(id).get();
	}

	public GradeInfo create(GradeInfo vo) throws Exception {
		vo.setId(gradeInfoRepository.getSeq());
		vo.setActivateFlag(UnieapConstants.YES);
		vo.setTenantId(UnieapConstants.getTenantId());
		gradeInfoRepository.save(vo);
		return vo;
	}

	public GradeInfo update(GradeInfo vo) throws Exception {
		GradeInfo gradeInfo = gradeInfoRepository.getById(vo.getId());
		gradeInfo.setName(vo.getName());
		gradeInfoRepository.save(gradeInfo);
		return vo;
	}

	public void delete(Long id) throws Exception {
		gradeInfoRepository.deleteById(id);
	}

	public void deleteBatch(List<Long> ids) throws Exception {
		gradeInfoRepository.deleteAllByIdInBatch(ids);
	}
}
