package com.slipper.unieap.school.renyuanguanli.baobei.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.file.pojo.UnieapFileArchive;
import com.slipper.unieap.file.repository.FileArchiveRepository;
import com.slipper.unieap.school.jiaowuguanli.vo.ClassInfoVO;
import com.slipper.unieap.school.renyuanguanli.pojo.AddressInfo;
import com.slipper.unieap.school.renyuanguanli.pojo.BindTimecard;
import com.slipper.unieap.school.renyuanguanli.pojo.ContactInfo;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsClass;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsContact;
import com.slipper.unieap.school.renyuanguanli.pojo.KidsInfo;
import com.slipper.unieap.school.renyuanguanli.pojo.RegisterInfo;
import com.slipper.unieap.school.renyuanguanli.repository.AddressInfoRepository;
import com.slipper.unieap.school.renyuanguanli.repository.ContactInfoRepository;
import com.slipper.unieap.school.renyuanguanli.repository.KidsClassRepository;
import com.slipper.unieap.school.renyuanguanli.repository.KidsContactRepository;
import com.slipper.unieap.school.renyuanguanli.repository.KidsInfoRepository;
import com.slipper.unieap.school.renyuanguanli.repository.RegisterInfoRepository;
import com.slipper.unieap.school.renyuanguanli.repository.TimecardInfoRepository;
import com.slipper.unieap.school.renyuanguanli.vo.KidsContactVO;
import com.slipper.unieap.school.renyuanguanli.vo.KidsInfoVO;
import com.slipper.unieap.vo.PaginationSupport;

@Service
public class KidsBO extends BaseBO {

	@Autowired
	public KidsInfoRepository kidsInfoRepository;

	@Autowired
	public KidsClassRepository kidsClassRepository;

	@Autowired
	public ContactInfoRepository contactInfoRepository;

	@Autowired
	public KidsContactRepository kidsContactRepository;

	@Autowired
	public TimecardInfoRepository timecardInfoRepository;

	@Autowired
	public FileArchiveRepository fileArchiveRepository;

	@Autowired
	public AddressInfoRepository addressInfoRepository;

	@Autowired
	public RegisterInfoRepository registerInfoRepository;

	public PaginationSupport getGridList(PaginationSupport page, KidsInfoVO vo) throws Exception {
		String KidsName = null;
		if (StringUtils.isNotEmpty(vo.getKidsName())) {
			KidsName = vo.getKidsName();
		}
		String ContactName = null;
		if (StringUtils.isNotEmpty(vo.getContactName())) {
			ContactName = vo.getContactName();
		}
		String ContactNumber = null;
		if (StringUtils.isNotEmpty(vo.getContactNumber())) {
			ContactNumber = vo.getContactNumber();
		}
		Date RegisterDateStart = vo.getRegisterDateStart();
		Date RegisterDateEnd = vo.getRegisterDateEnd();
		Long gradeId = vo.getGradeId();
		Long ClassId = vo.getClassId();
		String TimecardNumber = null;
		if (StringUtils.isNotEmpty(vo.getTimecardNumber())) {
			TimecardNumber = vo.getTimecardNumber();
		}
		Page<KidsInfoVO> datas = kidsInfoRepository.findKidsInfo(KidsName, ContactName, ContactNumber,
				RegisterDateStart, RegisterDateEnd, gradeId, ClassId, TimecardNumber, this.getPageable(page));
		page.items = datas.getContent();
		page.setTotalCount((int) datas.getTotalElements());
		return page;
	}

	public KidsInfoVO getKidsInfo(Long id) throws Exception {
		KidsInfoVO vo = new KidsInfoVO();
		KidsInfo kidsInfo = kidsInfoRepository.getById(id);
		vo.setId(id);
		vo.setKidsCode(kidsInfo.getCode());
		vo.setKidsName(kidsInfo.getName());
		vo.setNickname(kidsInfo.getNickname());
		vo.setGender(kidsInfo.getGender());
		vo.setBirthday(kidsInfo.getBirthday());
		vo.setHeight(kidsInfo.getHeight());
		vo.setWeight(kidsInfo.getWeight());
		vo.setProfileId(kidsInfo.getProfileId());
		vo.setRegisterId(kidsInfo.getRegisterId());
		if (kidsInfo.getProfileId() != null) {
			UnieapFileArchive UnieapFileArchive = fileArchiveRepository.getById(kidsInfo.getProfileId());
			vo.setProfileUrl(UnieapFileArchive.getUrl());
		}
		vo.setRegisterDate(kidsInfo.getRegisterDate());
		ClassInfoVO classInfoVO = kidsClassRepository.findClassInfoByKidsId(vo.getId());
		vo.setGradeId(classInfoVO.getGradeId());
		vo.setGradeName(classInfoVO.getGradeName());
		vo.setClassId(classInfoVO.getId());
		vo.setClassName(classInfoVO.getClassName());
		AddressInfo addressInfo = addressInfoRepository.findByRefId(id);
		if (addressInfo != null) {
			vo.setAddressId(addressInfo.getId());
			vo.setProvinceId(addressInfo.getProvinceId());
			vo.setCityId(addressInfo.getCityId());
			vo.setStreetId(addressInfo.getStreetId());
			vo.setAddressDetail(addressInfo.getAddressDetail());
		}
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsInfoVO create(KidsInfoVO vo) throws Exception {
		vo.setId(UnieapConstants.getSeq(null));
		vo.setKidsCode(kidsInfoRepository.getCode(vo.getClassId()));
		saveKidsInfo(vo);
		saveKidsClass(vo);
		saveContactInfo(vo);
		saveTimecardInfo(vo);
		return vo;
	}

	public void saveKidsInfo(KidsInfoVO vo) {
		KidsInfo kidsInfo = new KidsInfo();
		kidsInfo.setId(vo.getId());
		kidsInfo.setCode(vo.getKidsCode());
		kidsInfo.setName(vo.getKidsName());
		kidsInfo.setGender(vo.getGender());
		kidsInfo.setSourceType(vo.getSourceType());
		kidsInfo.setBirthday(vo.getBirthday());
		kidsInfo.setRegisterDate(vo.getRegisterDate());
		kidsInfo.setActivateFlag(UnieapConstants.YES);
		kidsInfo.setTenantId(UnieapConstants.getTenantId());
		kidsInfoRepository.save(kidsInfo);
	}

	public void saveKidsClass(KidsInfoVO vo) {
		KidsClass kidsClass = new KidsClass();
		kidsClass.setId(UnieapConstants.getSeq(null));
		kidsClass.setKidsId(vo.getId());
		kidsClass.setClassId(vo.getClassId());
		kidsClass.setActivateFlag(UnieapConstants.YES);
		kidsClass.setTenantId(UnieapConstants.getTenantId());
		kidsClassRepository.save(kidsClass);
	}

	public void saveContactInfo(KidsInfoVO vo) {
		List<KidsContactVO> contactsInfoList = vo.getContactsInfoList();
		if (contactsInfoList != null && contactsInfoList.size() > 0) {
			List<ContactInfo> contactList = new ArrayList<ContactInfo>();
			List<KidsContact> kidsContactList = new ArrayList<KidsContact>();
			for (KidsContactVO contactVo : contactsInfoList) {
				if (StringUtils.isNotEmpty(contactVo.getContactName())) {
					ContactInfo contactsInfo = new ContactInfo();
					contactList.add(contactsInfo);
					KidsContact kidsContact = new KidsContact();
					kidsContactList.add(kidsContact);
					contactsInfo.setId(UnieapConstants.getSeq(null));
					contactsInfo.setName(contactVo.getContactName());
					contactsInfo.setPhoneNumber(contactVo.getContactNumber());
					contactsInfo.setSourceType("SYS");
					contactsInfo.setActivateFlag(UnieapConstants.YES);
					contactsInfo.setTenantId(UnieapConstants.getTenantId());

					kidsContact.setId(UnieapConstants.getSeq(null));
					kidsContact.setKidsId(vo.getId());
					kidsContact.setContactId(contactsInfo.getId());
					kidsContact.setRelationship(contactVo.getRelationship());
					kidsContact.setPriority(contactVo.isChecked() ? UnieapConstants.YES : UnieapConstants.NO);
					kidsContact.setActivateFlag(UnieapConstants.YES);
					kidsContact.setTenantId(UnieapConstants.getTenantId());
				}
			}
			contactInfoRepository.saveAll(contactList);
			kidsContactRepository.saveAll(kidsContactList);
		}
	}

	public void saveTimecardInfo(KidsInfoVO vo) {
		List<BindTimecard> timecardInfoList = vo.getTimecardInfoList();
		if (timecardInfoList != null && timecardInfoList.size() > 0) {
			List<BindTimecard> timecardList = new ArrayList<BindTimecard>();
			for (BindTimecard timecardVo : timecardInfoList) {
				if (StringUtils.isNotEmpty(timecardVo.getTimecardNumber())) {
					timecardList.add(timecardVo);
					timecardVo.setId(UnieapConstants.getSeq(null));
					timecardVo.setBindId(vo.getId());
					timecardVo.setActivateFlag(UnieapConstants.YES);
					timecardVo.setTenantId(UnieapConstants.getTenantId());
				}
			}
			timecardInfoRepository.saveAll(timecardList);
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsInfoVO update(KidsInfoVO vo) throws Exception {
		updateKidsInfo(vo);
		if (vo.getAddressId() == null) {
			saveAddressInfo(vo);
		} else {
			updateAddressInfo(vo);
		}
		return vo;
	}

	public void updateKidsInfo(KidsInfoVO vo) {
		KidsInfo kidsInfo = kidsInfoRepository.getById(vo.getId());
		kidsInfo.setName(vo.getKidsName());
		kidsInfo.setNickname(vo.getNickname());
		kidsInfo.setProfileId(vo.getProfileId());
		kidsInfo.setGender(vo.getGender());
		kidsInfo.setBirthday(vo.getBirthday());
		kidsInfo.setRegisterDate(vo.getRegisterDate());
		kidsInfo.setHeight(vo.getHeight());
		kidsInfo.setWeight(vo.getWeight());
		kidsInfoRepository.save(kidsInfo);
	}

	public void saveAddressInfo(KidsInfoVO vo) {
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setId(UnieapConstants.getSeq(null));
		addressInfo.setRefId(vo.getId());
		addressInfo.setProvinceId(vo.getProvinceId());
		addressInfo.setCityId(vo.getCityId());
		addressInfo.setStreetId(vo.getStreetId());
		addressInfo.setAddressDetail(vo.getAddressDetail());
		addressInfo.setActivateFlag(UnieapConstants.YES);
		addressInfo.setTenantId(UnieapConstants.getTenantId());
		addressInfoRepository.save(addressInfo);
	}

	public void updateAddressInfo(KidsInfoVO vo) {
		AddressInfo addressInfo = addressInfoRepository.getById(vo.getAddressId());
		addressInfo.setProvinceId(vo.getProvinceId());
		addressInfo.setCityId(vo.getCityId());
		addressInfo.setStreetId(vo.getStreetId());
		addressInfo.setAddressDetail(vo.getAddressDetail());
		addressInfoRepository.save(addressInfo);
	}

	public RegisterInfo getRegisterInfo(Long id) throws Exception {
		KidsInfo kidsInfo = kidsInfoRepository.getById(id);
		if (kidsInfo.getRegisterId() != null) {
			RegisterInfo regInfo = registerInfoRepository.getById(kidsInfo.getRegisterId());
			return regInfo;
		} else {
			return new RegisterInfo();
		}
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RegisterInfo createRegisterInfo(RegisterInfo vo) throws Exception {
		vo.setId(UnieapConstants.getSeq(null));
		vo.setActivateFlag(UnieapConstants.YES);
		vo.setTenantId(UnieapConstants.getTenantId());
		registerInfoRepository.save(vo);
		KidsInfo kidsInfo = kidsInfoRepository.getById(vo.getKidsId());
		kidsInfo.setRegisterId(vo.getId());
		kidsInfoRepository.save(kidsInfo);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public RegisterInfo updateRegisterInfo(RegisterInfo vo) throws Exception {
		RegisterInfo registerInfo = registerInfoRepository.getById(vo.getId());
		registerInfo.setIdType(vo.getIdType());
		registerInfo.setIdNumber(vo.getIdNumber());
		registerInfo.setBloodType(vo.getBloodType());
		registerInfo.setNationality(vo.getNationality());
		registerInfo.setBirthplace(vo.getBirthplace());
		registerInfo.setNativeplace(vo.getNativeplace());
		registerInfo.setHukouplace(vo.getHukouplace());
		registerInfo.setCurrentplace(vo.getCurrentplace());
		registerInfo.setStayType(vo.getStayType());
		registerInfo.setHealthStatus(vo.getHealthStatus());
		registerInfo.setOrphan(vo.getOrphan());
		registerInfo.setDeformity(vo.getDeformity());
		registerInfo.setGuardian(vo.getGuardian());
		registerInfo.setGuardianIdtype(vo.getGuardianIdtype());
		registerInfo.setGuardianIdnumber(vo.getGuardianIdnumber());
		return vo;
	}

	public List<KidsContactVO> getContactInfoList(Long id) throws Exception {
		List<KidsContactVO> contactInfoList = contactInfoRepository.findContactInfoList(UnieapConstants.YES, id);
		return contactInfoList;
	}

	public KidsContactVO getContactInfo(Long id) throws Exception {
		KidsContactVO contactInfo = contactInfoRepository.findContactInfo(id);
		return contactInfo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsContactVO createContactInfo(KidsContactVO vo) throws Exception {
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setId(UnieapConstants.getSeq(null));
		contactInfo.setEducation(vo.getEducation());
		contactInfo.setEmail(vo.getEmail());
		contactInfo.setIdNumber(vo.getIdNumber());
		contactInfo.setIdType(vo.getIdType());
		String password = new Sha256Hash(vo.getContactNumber(), vo.getContactNumber().substring(8)).toHex();
		contactInfo.setPassword(password);
		contactInfo.setName(vo.getContactName());
		contactInfo.setNation(vo.getNation());
		contactInfo.setOccupation(vo.getOccupation());
		contactInfo.setPhoneNumber(vo.getContactNumber());
		contactInfo.setSourceType(vo.getSourceType());
		contactInfo.setActivateFlag(UnieapConstants.YES);
		contactInfo.setTenantId(UnieapConstants.getTenantId());
		contactInfoRepository.save(contactInfo);
		KidsContact kidsContact = new KidsContact();
		kidsContact.setId(UnieapConstants.getSeq(null));
		kidsContact.setKidsId(vo.getKidsId());
		kidsContact.setContactId(contactInfo.getId());
		kidsContact.setRelationship(vo.getRelationship());
		kidsContact.setPriority(vo.getPriority());
		kidsContact.setActivateFlag(UnieapConstants.YES);
		kidsContact.setTenantId(UnieapConstants.getTenantId());
		kidsContactRepository.save(kidsContact);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsContactVO updateContactInfo(KidsContactVO vo) throws Exception {
		ContactInfo contactInfo = contactInfoRepository.getById(vo.getContactId());
		contactInfo.setEducation(vo.getEducation());
		contactInfo.setEmail(vo.getEmail());
		contactInfo.setIdNumber(vo.getIdNumber());
		contactInfo.setIdType(vo.getIdType());
		contactInfo.setName(vo.getContactName());
		contactInfo.setNation(vo.getNation());
		contactInfo.setOccupation(vo.getOccupation());
		contactInfo.setPhoneNumber(vo.getContactNumber());
		contactInfo.setSourceType(vo.getSourceType());
		contactInfoRepository.save(contactInfo);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsContactVO deleteContactInfo(KidsContactVO vo) throws Exception {
		ContactInfo contactInfo = contactInfoRepository.getById(vo.getContactId());
		contactInfo.setActivateFlag(UnieapConstants.NO);
		contactInfoRepository.save(contactInfo);
		vo.setActivateFlag(UnieapConstants.NO);
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsContactVO setFirstContactInfo(KidsContactVO vo) throws Exception {
		List<KidsContact> kidsContactList = kidsContactRepository.findByKidsId(vo.getKidsId());
		if (kidsContactList != null && kidsContactList.size() > 0) {
			for (KidsContact kc : kidsContactList) {
				if (kc.getContactId().intValue() == vo.getContactId().intValue()) {
					kc.setPriority("1");
				} else {
					kc.setPriority("-1");
				}
			}
			kidsContactRepository.saveAll(kidsContactList);
		}
		return vo;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsContactVO resetPasswordContactInfo(KidsContactVO vo) throws Exception {
		ContactInfo contactInfo = contactInfoRepository.getById(vo.getContactId());
		String password = new Sha256Hash(contactInfo.getPhoneNumber(), contactInfo.getPhoneNumber().substring(8))
				.toHex();
		contactInfo.setPassword(password);
		contactInfoRepository.save(contactInfo);
		return vo;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public KidsInfoVO bindTimecard(KidsInfoVO vo) throws Exception {
		saveTimecardInfo(vo);
		return vo;
	}
}
