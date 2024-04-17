package com.slipper.unieap.extapi.whether;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.extapi.aliyun.HttpUtils;

/**
 * 获取天气信息
 * 
 * @author caiqi
 *
 */
@Service
public class WhetherBO {
	/**
	 * 获取24小时天气预测
	 */
	public Object getWhetherInfo(String lat, String lon, int type) {
		switch (type) {
		case 1: {
			return getWhetherInfo24h(lat, lon);
		}
		}
		return "";
	}

	public Object getWhetherInfo24h(String lat, String lon) {
		JSONObject data = whetherApi(lat, lon);
		if (data != null) {
			WhetherInfoDailySummaryVO whetherInfoDailySummaryVO = new WhetherInfoDailySummaryVO();
			JSONArray hourlyDatas = data.getJSONObject("data").getJSONArray("hourly");
			for (int i = 0; i < hourlyDatas.size(); i++) {
				JSONObject hourlyData = hourlyDatas.getJSONObject(i);
				String hour = hourlyData.getString("hour");

				if (StringUtils.equals(hour, "9")) {
					whetherInfoDailySummaryVO.setWeatherMorning(UnieapConstants
							.getDicData("WEATHER_TYPE", null, hourlyData.getString("condition")).getDicCode());
					whetherInfoDailySummaryVO.setWeatherMorningDesc(hourlyData.getString("condition"));
				}

				if (StringUtils.equals(hour, "15")) {
					whetherInfoDailySummaryVO.setWeatherAfternoon(UnieapConstants
							.getDicData("WEATHER_TYPE", null, hourlyData.getString("condition")).getDicCode());
					whetherInfoDailySummaryVO.setWeatherAfternoonDesc(hourlyData.getString("condition"));
				}
				if (StringUtils.equals(hour, "14")) {
					whetherInfoDailySummaryVO.setTempMax(hourlyData.getString("temp"));
				}
				if (StringUtils.equals(hour, "2")) {
					whetherInfoDailySummaryVO.setTempMin(hourlyData.getString("temp"));
				}
			}
			return whetherInfoDailySummaryVO;
		}
		return new WhetherInfoDailySummaryVO();
	}

	@Value("${aliyun.appcode}")
	String appcode;
	@Value("${aliyun.moji.host}")
	String host;
	@Value("${aliyun.moji.path}")
	String path;

	public JSONObject whetherApi(String lat, String lon) {
		String method = "POST";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		// bodys.put("lat", "39.91488908");
		// bodys.put("lon", "116.40387397");
		bodys.put("lat", lat);
		bodys.put("lon", lon);
		// bodys.put("token", "1b89050d9f64191d494c806f78e8ea36");
		try {
			/**
			 * 重要提示如下: HttpUtils请从
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
			 * 下载
			 *
			 * 相应的依赖请参照
			 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
			 */
			HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
			// 获取response的body
			String result = EntityUtils.toString(response.getEntity());
			if (StringUtils.isNotEmpty(result)) {
				return JSONObject.parseObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
