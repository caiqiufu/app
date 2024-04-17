package com.slipper.unieap.mdm.dic.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.mdm.dic.pojo.UnieapDicData;
import com.slipper.unieap.mdm.dic.pojo.UnieapDicGroup;
import com.slipper.unieap.mdm.dic.repository.UnieapDicDataRepository;
import com.slipper.unieap.mdm.dic.repository.UnieapDicGroupRepository;
import com.slipper.unieap.vo.TreeVO;

@Service
public class DicBO extends BaseBO {

	@Autowired
	public UnieapDicGroupRepository unieapDicGroupRepository;

	@Autowired
	public UnieapDicDataRepository unieapDicDataRepository;

	public List<TreeVO> getGridList() {
		List<UnieapDicGroup> datas = unieapDicGroupRepository.findAll(Sort.by(Direction.ASC, "groupName"));
		if (datas != null && datas.size() > 0) {
			List<TreeVO> result = new ArrayList<>();
			for (UnieapDicGroup data : datas) {
				TreeVO vo = new TreeVO();
				result.add(vo);
				vo.setId(data.getId().toString());
				vo.setText(data.getGroupName());
				vo.setAttr1(data.getGroupCode());
				vo.setAttr2("0");
				vo.setChildren(findChildren(data.getId()));
			}
			return result;
		}
		return null;
	}

	private List<TreeVO> findChildren(Long groupId) {
		List<UnieapDicData> datas = unieapDicDataRepository.findByGroupIdOrderByDicName(groupId);
		if (datas != null && datas.size() > 0) {
			List<TreeVO> result = new ArrayList<>();
			for (UnieapDicData data : datas) {
				TreeVO vo = new TreeVO();
				result.add(vo);
				vo.setId(data.getId().toString());
				vo.setText(data.getDicName());
				vo.setAttr1(data.getDicCode());
				vo.setParentId(groupId.toString());
				vo.setAttr2("1");
				vo.setAttr3(data.getSort().toString());
			}
			return result;
		}
		return null;
	}

	public TreeVO getInfo(Long id, String type) {
		TreeVO vo = new TreeVO();
		if (StringUtils.equals(type, "0")) {
			UnieapDicGroup group = unieapDicGroupRepository.getById(id);
			if (group != null) {
				vo.setId(group.getId().toString());
				vo.setText(group.getGroupName());
				vo.setAttr1(group.getGroupCode());
				vo.setAttr2("0");
			}
		}
		if (StringUtils.equals(type, "1")) {
			UnieapDicData data = unieapDicDataRepository.getById(id);
			if (data != null) {
				vo.setId(data.getId().toString());
				vo.setParentId(data.getGroupId().toString());
				vo.setText(data.getDicName());
				vo.setAttr1(data.getDicCode());
				vo.setAttr2("1");
				vo.setAttr3(data.getSort().toString());
			}
		}
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TreeVO create(TreeVO createOrUpdateForm) {
		if (StringUtils.equals(createOrUpdateForm.getAttr2().toString(), "0")) {
			UnieapDicGroup group = new UnieapDicGroup();
			group.setId(UnieapConstants.getSeq(null));
			createOrUpdateForm.setId(group.getId().toString());
			group.setGroupCode(createOrUpdateForm.getAttr1());
			group.setGroupName(createOrUpdateForm.getText());
			group.setLanguage(UnieapConstants.LANGUAGE);
			group.setActivateFlag(UnieapConstants.YES);
			group.setTenantId(UnieapConstants.getTenantId());
			unieapDicGroupRepository.save(group);
		}
		if (StringUtils.equals(createOrUpdateForm.getAttr2().toString(), "1")) {
			UnieapDicData data = new UnieapDicData();
			data.setId(UnieapConstants.getSeq(null));
			data.setGroupId(Long.decode(createOrUpdateForm.getParentId()));
			createOrUpdateForm.setId(data.getId().toString());
			data.setDicCode(createOrUpdateForm.getAttr1());
			data.setDicName(createOrUpdateForm.getText());
			data.setSort(Integer.decode(createOrUpdateForm.getAttr3()));
			data.setLanguage(UnieapConstants.LANGUAGE);
			data.setActivateFlag(UnieapConstants.YES);
			data.setTenantId(UnieapConstants.getTenantId());
			unieapDicDataRepository.save(data);
		}
		return createOrUpdateForm;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TreeVO update(TreeVO createOrUpdateForm) {
		if (StringUtils.equals(createOrUpdateForm.getAttr2().toString(), "0")) {
			UnieapDicGroup group = unieapDicGroupRepository.getById(Long.decode(createOrUpdateForm.getId()));
			group.setGroupCode(createOrUpdateForm.getAttr1());
			group.setGroupName(createOrUpdateForm.getText());
			unieapDicGroupRepository.save(group);
		}
		if (StringUtils.equals(createOrUpdateForm.getAttr2().toString(), "1")) {
			UnieapDicData data = unieapDicDataRepository.getById(Long.decode(createOrUpdateForm.getId()));
			data.setDicCode(createOrUpdateForm.getAttr1());
			data.setDicName(createOrUpdateForm.getText());
			data.setSort(Integer.decode(createOrUpdateForm.getAttr3()));
			unieapDicDataRepository.save(data);
		}
		return createOrUpdateForm;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Long id) {
		unieapDicDataRepository.deleteById(id);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateParent(TreeVO updateParentAndSortForm) {
		UnieapDicData data = unieapDicDataRepository.getById(Long.decode(updateParentAndSortForm.getId()));
		data.setGroupId(Long.decode(updateParentAndSortForm.getParentId()));
		unieapDicDataRepository.save(data);
	}
}
