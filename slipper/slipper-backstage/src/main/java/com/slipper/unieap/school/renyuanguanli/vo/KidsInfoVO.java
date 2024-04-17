package com.slipper.unieap.school.renyuanguanli.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.exttools.JpaConvert;
import com.slipper.unieap.school.renyuanguanli.pojo.BindTimecard;
import com.slipper.unieap.utils.DateUtils;

import lombok.Data;

@Data
@JpaConvert
public class KidsInfoVO implements Serializable {

	/**
	 * 说明：
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String kidsCode;

	private String kidsName;

	private String nickname;

	private String gender;

	private String genderDesc;

	private Date birthday;

	private String birthdayDesc;

	private String height;

	private String weight;

	private Long profileId;

	private String profileUrl;

	private Date registerDate;

	private String registerDateDesc;

	private Date registerDateStart;

	private Date registerDateEnd;

	private Long gradeId;

	private String gradeName;

	private Long classId;

	private String className;

	private Long registerId;

	/**
	 * 宝贝来源：后台录入,APP 录入
	 */
	private String sourceType;

	private String sourceTypeDesc;

	private Long contactId;

	private String contactName;

	private String contactNumber;

	private String contactNameDesc;

	private String relationship;

	private List<KidsContactVO> contactsInfoList = new ArrayList<KidsContactVO>();

	private List<BindTimecard> timecardInfoList = new ArrayList<BindTimecard>();

	private String kidsContact;
	private String bindTimecard;

	private Long timecardId;

	private String timecardNumber;

	private Long addressId;

	private Long provinceId;

	private String provinceDesc;

	private Long cityId;

	private String cityDesc;

	private Long streetId;

	private String streetDesc;

	private String addressDetail;

	// 通用属性
	private String activateFlag;

	private String activateFlagDesc;

	public String getActivateFlagDesc() {
		this.activateFlagDesc = UnieapConstants.getDicData("ACTIVATE_FLAG", activateFlag).getDicName();
		return activateFlagDesc;
	}

	public String getGenderDesc() {
		this.genderDesc = UnieapConstants.getDicData("GENDER", gender).getDicName();
		return genderDesc;
	}

	public String getBirthdayDesc() {
		if (this.birthday != null) {
			this.birthdayDesc = DateUtils.formatToStr(this.birthday, DateUtils.DATE_FORMAT);
		}
		return birthdayDesc;
	}

	public String getSourceTypeDesc() {
		this.sourceTypeDesc = UnieapConstants.getDicData("KIDS_SOURCE_TYPE", sourceType).getDicName();
		return sourceTypeDesc;
	}

	public String getRegisterDateDesc() {
		if (this.registerDate != null) {
			this.registerDateDesc = DateUtils.formatToStr(this.registerDate, DateUtils.DATE_FORMAT);
		}
		return this.registerDateDesc;
	}

	public String getContactNameDesc() {
		if (StringUtils.isNoneEmpty(this.relationship)) {
			this.contactNameDesc = this.kidsName
					+ UnieapConstants.getDicData("CONTACT_RELATIONSHIP", this.relationship).getDicName();
		}
		return this.contactNameDesc;
	}

	private Date createDate;

	private String createBy;

	private Date modifyDate;

	private String modifyBy;

	private Long tenantId;

	private String remark;
}
