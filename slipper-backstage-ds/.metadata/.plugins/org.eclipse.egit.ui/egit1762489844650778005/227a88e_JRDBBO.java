package com.slipper.unieap.jrds;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.db.DBManager;
import com.slipper.unieap.email.MailVO;
import com.slipper.unieap.utils.HttpUtils;

@Service
public class JRDBBO {
	public final Log logger = LogFactory.getLog(JRDBBO.class);
	@Autowired
	JR jr;
	public String DS_NAME = "JR";

	// 上次执行时间
	// public long lastSumExecuteTime;
	// public long lastDailyExecuteTime;
	// public long lastHourlyExecuteTime;
	// public long lastPriceExecuteTime;

	/**
	 * 近24小时建仓的持仓比例，数据更新频率约1小时
	 */
	public void dailySum() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("dailySum execute:" + sdf.format(now));
		// {"errcode":"0","errmsg":"success","data":{"pt":"2023-04-10","time":1681134046,"jbp":1819,"jsp":1784,"ybp":68,"ysp":153}}
		String result = apiRequest("day", "gold");
		// String result =
		// "{\"errcode\":\"0\",\"errmsg\":\"success\",\"data\":{\"pt\":\"2023-04-10\",\"time\":1681134046,\"jbp\":1819,\"jsp\":1784,\"ybp\":68,\"ysp\":153}}";
		if (StringUtils.isEmpty(result)) {
			logger.info("response is null");
			return;
		}
		if (!StringUtils.contains(result, "success")) {
			logger.info("response not success");
			MailVO mailvo = new MailVO();
			mailvo.setSubject("JR Daily Sum Data Failed, 请紧急处理");
			String content = "JR Daily Sum Data Failed, 请紧急处理,异常时间:" + sdf.format(now);
			mailvo.setContent(content);
			EmailHelper.sendEmail(mailvo);
			return;
		}
		try {
			JSONObject json = JSONObject.parseObject(result);
			JSONObject data = json.getJSONObject("data");
			if (data != null) {
				JSONObject jsonData = (JSONObject) data;
				String sqlCheckExistGold = "select count(1) as num from trade_daily_sum where symbol = 'gold' and buy_valume = '"
						+ jsonData.getString("jbp") + "' and sell_valume ='" + jsonData.getString("jsp")
						+ "' and ds_name = '" + DS_NAME + "'";
				int existCountGold = DBManager.getJT().queryForObject(sqlCheckExistGold, Long.class).intValue();
				if (existCountGold == 0) {
					String sqlGold = "insert into trade_daily_sum(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
					String sqlGoldHistory = "insert into trade_daily_sum_history(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
					List<Object[]> paraListGold = new ArrayList<Object[]>();
					paraListGold.add(new Object[] { "gold", jsonData.getString("pt"), jsonData.getString("jbp"),
							jsonData.getString("jsp"), UnieapConstants.getDateTime(), DS_NAME });
					DBManager.getJT().batchUpdate(sqlGold, paraListGold);
					DBManager.getJT().batchUpdate(sqlGoldHistory, paraListGold);
					// lastSumExecuteTime = new Date().getTime();
					Cache.executeTime.put("lastSumExecuteTime", Long.valueOf(new Date().getTime()));
				} else {
					logger.info("Gold Daily Sum data exist");
				}
				String sqlCheckExistSilver = "select count(1) as num from trade_daily_sum where symbol = 'silver' and buy_valume = '"
						+ jsonData.getString("ybp") + "' and sell_valume ='" + jsonData.getString("ysp")
						+ "' and ds_name = '" + DS_NAME + "'";
				int existCountSilver = DBManager.getJT().queryForObject(sqlCheckExistSilver, Long.class).intValue();
				if (existCountSilver == 0) {
					String sqlSilver = "insert into trade_daily_sum(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
					String sqlSilverHistory = "insert into trade_daily_sum_history(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
					List<Object[]> paraListSilver = new ArrayList<Object[]>();
					paraListSilver.add(new Object[] { "silver", jsonData.getString("pt"), jsonData.getString("ybp"),
							jsonData.getString("ysp"), UnieapConstants.getDateTime(), DS_NAME });
					DBManager.getJT().batchUpdate(sqlSilver, paraListSilver);
					DBManager.getJT().batchUpdate(sqlSilverHistory, paraListSilver);
				} else {
					logger.info("Silver Daily Sum exist");
				}
			}
			_DtaskExecuteDetails = getTaskExecuteStatus("D");
		} catch (RuntimeException e) {
			logger.info("dailySum Exception," + e.getMessage());
		}
	}

	/**
	 * 近24小时建仓的持仓比例，数据更新频率约1小时
	 */
	public void hourlyGold() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("hourlyGold execute:" + sdf.format(now));
		// {"errcode":"0","errmsg":"success","data":[{"symbol":"gold","type":"time","pt":"22:00","time":1681135767,"bp":488,"sp":162},{"symbol":"gold","type":"time","pt":"21:00","time":1681135767,"bp":1216,"sp":520},{"symbol":"gold","type":"time","pt":"20:00","time":1681135767,"bp":558,"sp":187},{"symbol":"gold","type":"time","pt":"19:00","time":1681135767,"bp":59,"sp":48},{"symbol":"gold","type":"time","pt":"18:00","time":1681135767,"bp":25,"sp":40},{"symbol":"gold","type":"time","pt":"17:00","time":1681135767,"bp":21,"sp":68},{"symbol":"gold","type":"time","pt":"16:00","time":1681135767,"bp":14,"sp":51},{"symbol":"gold","type":"time","pt":"15:00","time":1681135767,"bp":9,"sp":39},{"symbol":"gold","type":"time","pt":"14:00","time":1681135767,"bp":7,"sp":27},{"symbol":"gold","type":"time","pt":"13:00","time":1681135767,"bp":11,"sp":17},{"symbol":"gold","type":"time","pt":"12:00","time":1681135767,"bp":15,"sp":15},{"symbol":"gold","type":"time","pt":"11:00","time":1681135767,"bp":22,"sp":21},{"symbol":"gold","type":"time","pt":"10:00","time":1681135767,"bp":55,"sp":28},{"symbol":"gold","type":"time","pt":"09:00","time":1681135767,"bp":33,"sp":13},{"symbol":"gold","type":"time","pt":"08:00","time":1681135767,"bp":103,"sp":20},{"symbol":"gold","type":"time","pt":"07:00","time":1681135767,"bp":13,"sp":26},{"symbol":"gold","type":"time","pt":"06:00","time":1681135767,"bp":60,"sp":72},{"symbol":"gold","type":"time","pt":"05:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"04:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"03:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"02:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"01:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"00:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"23:00","time":1681135767,"bp":0,"sp":0}]}
		String result = apiRequest("hour", "gold");
		// String result =
		// "{\"errcode\":\"0\",\"errmsg\":\"success\",\"data\":[{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"22:00\",\"time\":1681135767,\"bp\":488,\"sp\":162},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"21:00\",\"time\":1681135767,\"bp\":1216,\"sp\":520},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"20:00\",\"time\":1681135767,\"bp\":558,\"sp\":187},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"19:00\",\"time\":1681135767,\"bp\":59,\"sp\":48},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"18:00\",\"time\":1681135767,\"bp\":25,\"sp\":40},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"17:00\",\"time\":1681135767,\"bp\":21,\"sp\":68},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"16:00\",\"time\":1681135767,\"bp\":14,\"sp\":51},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"15:00\",\"time\":1681135767,\"bp\":9,\"sp\":39},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"14:00\",\"time\":1681135767,\"bp\":7,\"sp\":27},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"13:00\",\"time\":1681135767,\"bp\":11,\"sp\":17},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"12:00\",\"time\":1681135767,\"bp\":15,\"sp\":15},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"11:00\",\"time\":1681135767,\"bp\":22,\"sp\":21},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"10:00\",\"time\":1681135767,\"bp\":55,\"sp\":28},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"09:00\",\"time\":1681135767,\"bp\":33,\"sp\":13},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"08:00\",\"time\":1681135767,\"bp\":103,\"sp\":20},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"07:00\",\"time\":1681135767,\"bp\":13,\"sp\":26},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"06:00\",\"time\":1681135767,\"bp\":60,\"sp\":72},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"05:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"04:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"03:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"02:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"01:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"00:00\",\"time\":1681135767,\"bp\":0,\"sp\":0},{\"symbol\":\"gold\",\"type\":\"time\",\"pt\":\"23:00\",\"time\":1681135767,\"bp\":0,\"sp\":0}]}";
		if (StringUtils.isEmpty(result)) {
			logger.info("response is null");
			return;
		}
		if (!StringUtils.contains(result, "success")) {
			logger.info("response not success");
			MailVO mailvo = new MailVO();
			mailvo.setSubject("JR Hourly Data Failed, 请紧急处理");
			String content = "JR Hourly Sum Data Failed, 请紧急处理,异常时间:" + sdf.format(now);
			mailvo.setContent(content);
			EmailHelper.sendEmail(mailvo);
			return;
		}
		try {
			/**
			 * 数据处理逻辑:每次只检查最新的一条数据, 如果symbol,pt,buy_valume,sell_valume,ds_name
			 * 完全一样,则说明是同一条记录
			 */
			JSONObject json = JSONObject.parseObject(result);
			JSONArray datas = json.getJSONArray("data");
			if (datas.size() > 0) {
				// 判断最新记录是否已存在,如果存在就不做数据插入处理
				boolean isExistFlag = false;
				logger.info("hourlyGold collect result：" + datas.size());
				// 获取最新的一条数据,需要判断该数据是否已经存在
				JSONObject newJsonData = datas.getJSONObject(0);
				String sqlCheckExistGold = "select count(1) as num from trade_daily_detail where symbol = 'gold' and pt= '"
						+ newJsonData.getString("pt") + "' and buy_valume = '" + newJsonData.getString("bp")
						+ "' and sell_valume ='" + newJsonData.getString("sp") + "' and ds_name = '" + DS_NAME + "' AND create_time > NOW() - INTERVAL 23 HOUR";
				int existCountGold = DBManager.getJT().queryForObject(sqlCheckExistGold, Long.class).intValue();
				if (existCountGold > 0) {
					isExistFlag = true;
				}
				if (!isExistFlag) {
					// 根据返回的数据顺序,最先获取的是最新的数据,在插入数据库前反转,最新数据最后插入
					List<Object[]> paraList = new ArrayList<Object[]>();
					for (Object data : datas) {
						JSONObject jsonData = (JSONObject) data;
						paraList.add(new Object[] { jsonData.getString("symbol"), jsonData.getString("time"),
								jsonData.getString("pt"), jsonData.getString("bp"), jsonData.getString("sp"),
								UnieapConstants.getDateTime(), DS_NAME });
					}

					String deletedata = "delete from trade_daily_detail where symbol = 'gold'" + " and ds_name = '"
							+ DS_NAME + "' and id >0";
					// 删除数据
					// DBManager.getJT().execute("SET SQL_SAFE_UPDATES = 0");
					DBManager.getJT().execute(deletedata);
					// 插入数据
					String insertSql = "insert into trade_daily_detail(symbol,time,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?,?)";
					//反转数据，最新的数据最后插入
					Collections.reverse(paraList);
					DBManager.getJT().batchUpdate(insertSql, paraList);
					// 把新数据插入历史表
					List<Object[]> newParaList = new ArrayList<Object[]>();
					newParaList.add(new Object[] { newJsonData.getString("symbol"), newJsonData.getString("time"),
							newJsonData.getString("pt"), newJsonData.getString("bp"), newJsonData.getString("sp"),
							UnieapConstants.getDateTime(), DS_NAME });
					String insertHistorySql = "insert into trade_daily_detail_history(symbol,time,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?,?)";
					DBManager.getJT().batchUpdate(insertHistorySql, newParaList);
					// 插入分时数据
					sumGold(result);
					// 插入最新买卖数据
					latestGold(result);
				}
			}
		} catch (RuntimeException e) {
			logger.info("hourlyGold Exception," + e.getMessage());
		}
	}

	/**
	 * 分价数据，当前系统在此均价范围内的多空单比例,更新频率约30分钟
	 */
	public void priceGold() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("priceGold execute:" + sdf.format(now));
		// {"errcode":"0","errmsg":"success","data":[{"symbol":"gold","type":"price","totalsb":11322,"pt":"1998.32","time":1681136468,"bp":227,"sp":198},{"symbol":"gold","type":"price","totalsb":11322,"pt":"1995.32","time":1681136468,"bp":229,"sp":104},{"symbol":"gold","type":"price","totalsb":11322,"pt":"1992.32","time":1681136468,"bp":633,"sp":244},{"symbol":"gold","type":"price","totalsb":11322,"pt":"1989.32","time":1681136468,"bp":886,"sp":526},{"symbol":"gold","type":"price","totalsb":11322,"pt":"1986.32","time":1681136468,"bp":348,"sp":249},{"symbol":"gold","type":"price","totalsb":11322,"pt":"1983.32","time":1681136468,"bp":48,"sp":65}]}
		String result = apiRequest("price", "gold");
		// String result =
		// "{\"errcode\":\"0\",\"errmsg\":\"success\",\"data\":[{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1998.32\",\"time\":1681136468,\"bp\":227,\"sp\":198},{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1995.32\",\"time\":1681136468,\"bp\":229,\"sp\":104},{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1992.32\",\"time\":1681136468,\"bp\":633,\"sp\":244},{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1989.32\",\"time\":1681136468,\"bp\":886,\"sp\":526},{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1986.32\",\"time\":1681136468,\"bp\":348,\"sp\":249},{\"symbol\":\"gold\",\"type\":\"price\",\"totalsb\":11322,\"pt\":\"1983.32\",\"time\":1681136468,\"bp\":48,\"sp\":65}]}";
		if (StringUtils.isEmpty(result)) {
			logger.info("response is null");
			return;
		}
		if (!StringUtils.contains(result, "success")) {
			logger.info("response not success");
			MailVO mailvo = new MailVO();
			mailvo.setSubject("JR Price Data Failed, 请紧急处理");
			String content = "JR Price Sum Data Failed, 请紧急处理,异常时间:" + sdf.format(now);
			mailvo.setContent(content);
			EmailHelper.sendEmail(mailvo);
			return;
		}
		try {
			JSONObject json = JSONObject.parseObject(result);
			JSONArray datas = json.getJSONArray("data");
			if (datas.size() > 0) {
				logger.info("priceGold collect：" + datas.size());
				// 按照倒序插入数据，最新的数据最先插入
				String insertSql = "insert into trade_price(symbol,time,price,totalsb_valume,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?,?,?)";
				List<Object[]> paraList = new ArrayList<Object[]>();
				for (Object data : datas) {
					JSONObject jsonData = (JSONObject) data;
					paraList.add(new Object[] { jsonData.getString("symbol"), jsonData.getString("time"),
							jsonData.getString("pt"), jsonData.getString("totalsb"), jsonData.getString("bp"),
							jsonData.getString("sp"), UnieapConstants.getDateTime(), DS_NAME });
				}
				String copydata = "insert into trade_price_history select * from trade_price where symbol = 'gold'"
						+ " and ds_name = '" + DS_NAME + "'";
				String deletedata = "delete from trade_price where symbol = 'gold'" + " and ds_name = '" + DS_NAME
						+ "'";
				// 备份数据
				DBManager.getJT().execute(copydata);
				// 删除数据
				DBManager.getJT().execute("SET SQL_SAFE_UPDATES = 0");
				DBManager.getJT().execute(deletedata);
				// 插入数据
				DBManager.getJT().batchUpdate(insertSql, paraList);
				Cache.executeTime.put("lastPriceExecuteTime", Long.valueOf(new Date().getTime()));
			}
		} catch (RuntimeException e) {
			logger.info("priceGold Exception," + e.getMessage());
		}
	}

	/**
	 * 通过分时数据获取总览数据
	 * 
	 * @param result
	 */
	public void sumGold(String result) {
		try {
			JSONObject json = JSONObject.parseObject(result);
			JSONArray datas = json.getJSONArray("data");
			if (datas.size() > 0) {
				logger.info("sumGold collect：" + datas.size());
				// 按照倒序插入数据，最新的数据最先插入
				String insertSql = "insert into trade_sum(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
				String insertSqlHistory = "insert into trade_sum_history(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
				List<Object[]> paraList = new ArrayList<Object[]>();
				double buyValume = 0;
				double sellValume = 0;
				for (Object data : datas) {
					JSONObject jsonData = (JSONObject) data;
					buyValume = buyValume + Double.parseDouble(jsonData.getString("bp"));
					sellValume = sellValume + Double.parseDouble(jsonData.getString("sp"));

				}
				String sqlCheckExistGold = "SELECT count(1) as num FROM trade_sum where symbol = 'gold' and buy_valume = '"
						+ buyValume + "' and sell_valume = '" + sellValume + "' and ds_name = '" + DS_NAME + "'";
				int existCountGold = DBManager.getJT().queryForObject(sqlCheckExistGold, Long.class).intValue();
				if (existCountGold == 0) {
					// Long seq = UnieapConstants.getSeq(null);
					paraList.add(
							new Object[] { "gold", "", buyValume, sellValume, UnieapConstants.getDateTime(), DS_NAME });
					// 插入数据
					DBManager.getJT().batchUpdate(insertSql, paraList);
					DBManager.getJT().batchUpdate(insertSqlHistory, paraList);
				} else {
					logger.info("Gold Sum exist");
				}
			}
		} catch (RuntimeException e) {
			logger.info("sumGold Exception," + e.getMessage());
		}
	}

	/**
	 * 通过hourly 数据，获取最新买卖数据变化
	 * 
	 * @param result
	 */
	public void latestGold(String result) {
		// {"errcode":"0","errmsg":"success","data":[{"symbol":"gold","type":"time","pt":"22:00","time":1681135767,"bp":488,"sp":162},{"symbol":"gold","type":"time","pt":"21:00","time":1681135767,"bp":1216,"sp":520},{"symbol":"gold","type":"time","pt":"20:00","time":1681135767,"bp":558,"sp":187},{"symbol":"gold","type":"time","pt":"19:00","time":1681135767,"bp":59,"sp":48},{"symbol":"gold","type":"time","pt":"18:00","time":1681135767,"bp":25,"sp":40},{"symbol":"gold","type":"time","pt":"17:00","time":1681135767,"bp":21,"sp":68},{"symbol":"gold","type":"time","pt":"16:00","time":1681135767,"bp":14,"sp":51},{"symbol":"gold","type":"time","pt":"15:00","time":1681135767,"bp":9,"sp":39},{"symbol":"gold","type":"time","pt":"14:00","time":1681135767,"bp":7,"sp":27},{"symbol":"gold","type":"time","pt":"13:00","time":1681135767,"bp":11,"sp":17},{"symbol":"gold","type":"time","pt":"12:00","time":1681135767,"bp":15,"sp":15},{"symbol":"gold","type":"time","pt":"11:00","time":1681135767,"bp":22,"sp":21},{"symbol":"gold","type":"time","pt":"10:00","time":1681135767,"bp":55,"sp":28},{"symbol":"gold","type":"time","pt":"09:00","time":1681135767,"bp":33,"sp":13},{"symbol":"gold","type":"time","pt":"08:00","time":1681135767,"bp":103,"sp":20},{"symbol":"gold","type":"time","pt":"07:00","time":1681135767,"bp":13,"sp":26},{"symbol":"gold","type":"time","pt":"06:00","time":1681135767,"bp":60,"sp":72},{"symbol":"gold","type":"time","pt":"05:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"04:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"03:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"02:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"01:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"00:00","time":1681135767,"bp":0,"sp":0},{"symbol":"gold","type":"time","pt":"23:00","time":1681135767,"bp":0,"sp":0}]}
		try {
			JSONObject json = JSONObject.parseObject(result);
			JSONArray datas = json.getJSONArray("data");
			if (datas.size() > 0) {
				logger.info("latestGold collect：" + datas.size());
				// 按照倒序插入数据，最新的数据最先插入
				String insertSql = "insert into trade_latest(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values  (?,?,?,?,?,?)";
				String insertSqlHistory = "insert into trade_latest_history(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values  (?,?,?,?,?,?)";
				List<Object[]> paraList = new ArrayList<Object[]>();
				JSONObject jsonData = datas.getJSONObject(0);

				String sqlCheckExistGold = "select count(1) as num from trade_latest where symbol = 'gold' and buy_valume = '"
						+ jsonData.getString("bp") + "' and sell_valume ='" + jsonData.getString("sp") + "' and pt = '"
						+ jsonData.getString("pt") + "' and ds_name = '" + DS_NAME + "'";
				int existCountGold = DBManager.getJT().queryForObject(sqlCheckExistGold, Long.class).intValue();
				if (existCountGold == 0) {
					paraList.add(new Object[] { jsonData.getString("symbol"), jsonData.getString("pt"),
							jsonData.getString("bp"), jsonData.getString("sp"), UnieapConstants.getDateTime(),
							DS_NAME });
					// 插入数据
					DBManager.getJT().batchUpdate(insertSql, paraList);
					DBManager.getJT().batchUpdate(insertSqlHistory, paraList);
				} else {
					logger.info("Gold Latest exist");
				}
			}
			_LtaskExecuteDetails = getTaskExecuteStatus("L");
		} catch (RuntimeException e) {
			logger.info("latestGold Exception," + e.getMessage());
		}
	}

	public String getTaskExecuteStatus(String type) {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
		return MessageFormat.format("{0}:{1}", type, dateFormat.format(date));
	}

	public String apiRequest(String category, String sort) {
		String[] res = jr.generateTimestampAndKey("index/jrdkcc").split(",");
		String url = MessageFormat.format(
				"http://120.77.237.33:23001/?r=index/jrdkcc&v=v1&key={0}&timestamp={1}&access_token=&device=android&cv=79&lan=chinese&userid=&category={2}&sort={3}",
				res[1], res[0], category, sort);
		return HttpUtils.doPost(url, null);
	}

	// Task执行状态 L:03:45,H:03:45,D:03:45
	public String _LtaskExecuteDetails = "";
	public String _HtaskExecuteDetails = "";
	public String _DtaskExecuteDetails = "";

	public String getTaskExecuteDetails() {
		return _LtaskExecuteDetails + "," + _HtaskExecuteDetails + "," + _DtaskExecuteDetails;
	}

	@Autowired
	private DelayQueueManager delayQueueManager;

	public void loadJRSourceDataDailyGoldJR() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber1 = random.nextInt(50);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("dailyGoldJR"), 50 * randomNumber1));
	}

	public void loadJRSourceDataHourlyGoldJR() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber1 = random.nextInt(50);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("hourlyGoldJR"), 50 * randomNumber1));
	}

	public void loadJRSourceDataPriceGoldJR() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber1 = random.nextInt(50);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("priceGoldJR"), 50 * randomNumber1));
	}

	public void loadJRSourceData() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber1 = random.nextInt(300);
		int randomNumber2 = random.nextInt(200);
		int randomNumber3 = random.nextInt(100);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("dailyGoldJR"), 30 * randomNumber1));
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("hourlyGoldJR"), 60 * randomNumber2));
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("priceGoldJR"), 90 * randomNumber3));
		// 测试任务需要下边代码执行，线上不用
		/*
		 * try { Thread.sleep(10 * 1000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

	public void clearJRSourceData() {
		logger.info("Start clear data");
		// 删除数据
		DBManager.getJT().execute("SET SQL_SAFE_UPDATES = 0");
		//String copypricedata = "insert into trade_price_history select * from trade_price";
		//已逐条插入最新记录,所以不再进行备份
		//String copydailydetaildata = "insert into trade_daily_detail_history select * from trade_daily_detail";
		//String copydailysumdata = "insert into trade_daily_sum_history select * from trade_daily_sum";
		//String copytradesumdata = "insert into trade_sum_history select * from trade_sum";
		//String copytradelatestdata = "insert into trade_latest_history select * from trade_latest";
		//DBManager.getJT().execute(copypricedata);
		//DBManager.getJT().execute(copydailydetaildata);
		//DBManager.getJT().execute(copydailysumdata);
		//DBManager.getJT().execute(copytradesumdata);
		//DBManager.getJT().execute(copytradelatestdata);
		//DBManager.getJT().execute("delete from trade_daily_detail");
		DBManager.getJT().execute("delete from trade_daily_sum");
		DBManager.getJT().execute("delete from trade_latest");
		DBManager.getJT().execute("delete from trade_price");
		DBManager.getJT().execute("delete from trade_sum");
		DBManager.getJT().execute("delete from trade_single_sum");
		DBManager.getJT().execute("delete from trade_single_transaction");
		logger.info("Clear data done");
	}

}
