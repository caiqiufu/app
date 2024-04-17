package com.slipper.unieap;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import com.slipper.unieap.bo.BaseBO;
import com.slipper.unieap.db.DBManager;
import com.slipper.unieap.vo.DicDataVO;
import com.slipper.unieap.vo.DicGroupVO;

/**
 * Dec 14, 2010 全局变量,包含WEB层与Action层的数据交互定义
 */
public class UnieapConstants {

	public final static String YES = "Y";
	public final static String NO = "N";
	public final static String QUERY = "query";
	public final static String CREATE = "create";
	public final static String UPDATE = "update";
	public final static String DELETE = "delete";
	public final static String CHECKEXIST = "c_e";
	public final static String MODIFYACTIVATEFLAG = "m_af";
	public final static String DATEFORMAT = "yyyy-MM-dd";
	public final static String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static String LANGUAGE = Locale.SIMPLIFIED_CHINESE.toLanguageTag();

	/**
	 * 应用发布URL
	 */
	public static String APP_BASE_URL = "";
	/**
	 * 应用路径
	 */
	public static String APP_PATH = "";
	/**
	 * 共享文件物理目录
	 */
	public static String SHAREFOLDER_PATH = "";
	/**
	 * 共享文件访问路径
	 */
	public static String SHAREFOLDER_URL = "";

	public final static String getCurrentTime() {
		Date data = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(data);
		return dt;
	}

	public final static Date getDateTime() {
		Timestamp obj = DBManager.getBizJT(null).queryForObject("SELECT CURRENT_TIMESTAMP() CURRENTTIME",
				Timestamp.class);
		return obj;
	}
	
	public static DicGroupVO getDicGroup(String groupCode) {
		BaseBO baseBO = (BaseBO) ApplicationContextProvider.getBean("baseBO");
		return baseBO.getDicGroup(groupCode);
	}

	public static DicDataVO getDicData(String groupCode, String dicCode) {
		BaseBO baseBO = (BaseBO) ApplicationContextProvider.getBean("baseBO");
		return baseBO.getDicData(groupCode, dicCode);
	}

	public final static String getMessage(String code, String language, String[] args) {
		return getMessageResource().getMessage(code, args, Locale.forLanguageTag(language));
	}

	public final static String getMessage(String code, String[] args) {
		return getMessageResource().getMessage(code, args, Locale.forLanguageTag(LANGUAGE));
	}

	/**
	 * MessageSource
	 * 
	 * @return
	 */
	private static MessageSource getMessageResource() {
		return (MessageSource) ApplicationContextProvider.getBean("messageSource");
	}

	public synchronized final static Long getSeq(String serialName) {
		int batchNo = 1;
		return Long.valueOf(getBatchSeq(serialName, batchNo));
	}

	public synchronized final static String getMaxCode(int length, String tableName) {
		if (length < 0) {
			length = 0;
		}
		return DBManager.getBizJT(null).queryForObject("select lpad(COALESCE((max(convert(e.code,signed))+1),1),"
				+ length + ",0) as seq from " + tableName + " e", String.class);
	}

	public synchronized final static int getBatchSeq(String serialName, int batchLength) {
		if (StringUtils.isEmpty(serialName)) {
			serialName = "unieap";
		}
		return DBManager.getBizJT(null)
				.queryForObject("SELECT NEXTVAL('" + serialName + "'," + batchLength + ") SEQ", Long.class).intValue();
	}
}
