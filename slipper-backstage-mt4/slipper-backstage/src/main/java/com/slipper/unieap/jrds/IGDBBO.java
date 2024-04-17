package com.slipper.unieap.jrds;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.slipper.unieap.UnieapConstants;
import com.slipper.unieap.db.DBManager;
import com.slipper.unieap.utils.HttpsUtils;

@Service
public class IGDBBO {
	public final Log logger = LogFactory.getLog(IGDBBO.class);
	public String DS_NAME = "IG";
	
	public void collectOtherData() {
		// String result =
				// {"clientsPositions":{"longPercent":73.0,"shortPercent":27.0,"total":5933.0},"tradingActivity":[{"hasTrades":true,"longPercent":48.0,"shortPercent":52.0,"total":407.0,"timePeriod":"L1H"},{"hasTrades":true,"longPercent":49.0,"shortPercent":51.0,"total":28419.0,"timePeriod":"L24H"},{"hasTrades":true,"longPercent":50.0,"shortPercent":50.0,"total":92666.0,"timePeriod":"L7D"},{"hasTrades":true,"longPercent":50.0,"shortPercent":50.0,"total":503929.0,"timePeriod":"L1M"}]}";
		String result = apiRequest("", "");		
		dailyGold(result);
		hourlyGold(result);
		sumGold(result);
	}
	
	public void collectLatestGold() {
		// String result =
				// {"clientsPositions":{"longPercent":73.0,"shortPercent":27.0,"total":5933.0},"tradingActivity":[{"hasTrades":true,"longPercent":48.0,"shortPercent":52.0,"total":407.0,"timePeriod":"L1H"},{"hasTrades":true,"longPercent":49.0,"shortPercent":51.0,"total":28419.0,"timePeriod":"L24H"},{"hasTrades":true,"longPercent":50.0,"shortPercent":50.0,"total":92666.0,"timePeriod":"L7D"},{"hasTrades":true,"longPercent":50.0,"shortPercent":50.0,"total":503929.0,"timePeriod":"L1M"}]}";
		String result = apiRequest("", "");		
		latestGold(result);
	}
	
	/**
	 * L24H 数据
	 * @param result
	 */
	public void dailyGold(String result) {	
		try {
			JSONObject json = JSONObject.parseObject(result);
			//总览
			JSONArray tradingActivity = json.getJSONArray("tradingActivity");
			if (tradingActivity != null) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String pt = sdf.format(now);
				JSONArray jsonData = (JSONArray) tradingActivity;
				JSONObject dailySum = jsonData.getJSONObject(1);
				String sqlGold = "insert into trade_daily_sum(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
				List<Object[]> paraListGold = new ArrayList<Object[]>();
				paraListGold.add(new Object[] { "gold", pt,
						dailySum.getString("longPercent"), dailySum.getString("shortPercent"), UnieapConstants.getDateTime(),DS_NAME});
				DBManager.getJT().batchUpdate(sqlGold, paraListGold);
			}
			_DtaskExecuteDetails = getTaskExecuteStatus("D");
		}catch(RuntimeException e) {
			logger.info("dailySum Exception,"+e.getMessage());
		}	
	}

	/**
	 * L1H 数据
	 * @param result
	 */
	public void hourlyGold(String result) {
		try {
			JSONObject json = JSONObject.parseObject(result);
			//总览
			JSONArray tradingActivity = json.getJSONArray("tradingActivity");
			if (tradingActivity != null) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String pt = sdf.format(now);
				JSONArray jsonData = (JSONArray) tradingActivity;
				JSONObject dailySum = jsonData.getJSONObject(0);
				String sqlGold =  "insert into trade_daily_detail(symbol,time,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?,?)";
				List<Object[]> paraListGold = new ArrayList<Object[]>();
				paraListGold.add(new Object[] { "gold", now.getTime(),pt,
						dailySum.getString("longPercent"), dailySum.getString("shortPercent"), UnieapConstants.getDateTime(),DS_NAME});
				DBManager.getJT().batchUpdate(sqlGold, paraListGold);
			}
			_DtaskExecuteDetails = getTaskExecuteStatus("H");
		}catch(RuntimeException e) {
			logger.info("dailySum Exception,"+e.getMessage());
		}	
	}

	/**
	 * 总览
	 * @param result
	 */
	public void sumGold(String result) {
		try {
			JSONObject json = JSONObject.parseObject(result);			
			JSONObject clientsPositions = json.getJSONObject("clientsPositions");
			if (clientsPositions != null) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String pt = sdf.format(now);
				JSONObject jsonData = (JSONObject) clientsPositions;
				String sqlGold =  "insert into trade_sum(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values (?,?,?,?,?,?)";
				List<Object[]> paraListGold = new ArrayList<Object[]>();
				paraListGold.add(new Object[] { "gold", pt,
						jsonData.getString("longPercent"), jsonData.getString("shortPercent"), UnieapConstants.getDateTime(),DS_NAME});
				DBManager.getJT().batchUpdate(sqlGold, paraListGold);
			}
			_DtaskExecuteDetails = getTaskExecuteStatus("S");
		}catch(RuntimeException e) {
			logger.info("dailySum Exception,"+e.getMessage());
		}	
	}
	
	/**
	 * 通过hourly 数据，获取最新买卖数据变化
	 * 
	 * @param result
	 */
	public void latestGold(String result) {
		try {
			JSONObject json = JSONObject.parseObject(result);
			//总览
			JSONArray tradingActivity = json.getJSONArray("tradingActivity");
			if (tradingActivity != null) {
				Date now = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String pt = sdf.format(now);
				JSONArray jsonData = (JSONArray) tradingActivity;
				JSONObject dailySum = jsonData.getJSONObject(0);
				String sqlGold =  "insert into trade_latest(symbol,pt,buy_valume,sell_valume,create_time,ds_name) values  (?,?,?,?,?,?)";
				List<Object[]> paraListGold = new ArrayList<Object[]>();
				paraListGold.add(new Object[] {"gold", pt,
						dailySum.getString("longPercent"), dailySum.getString("shortPercent"), UnieapConstants.getDateTime(),DS_NAME});
				DBManager.getJT().batchUpdate(sqlGold, paraListGold);
			}
			_DtaskExecuteDetails = getTaskExecuteStatus("H");
		}catch(RuntimeException e) {
			logger.info("dailySum Exception,"+e.getMessage());
		}	
	}
	
    public String getTaskExecuteStatus(String type) 
    {
    	Date date = new Date();
    	SimpleDateFormat dateFormat= new SimpleDateFormat("HH:mm");
    	return MessageFormat.format("{0}:{1}", type, dateFormat.format(date));       
    }
	public String apiRequest(String category, String sort) {
		String url = "https://sentiment.api.caaprd.ig.com/sentiment/client-sentiment/GC";
		HttpsUtils h = new HttpsUtils(null);
		return h.doGet(url,null);
	}
	//Task执行状态 L:03:45,H:03:45,D:03:45
    public String _LtaskExecuteDetails = "";
    public String _HtaskExecuteDetails = "";
    public String _DtaskExecuteDetails = "";
    
    public String getTaskExecuteDetails() {
    	return _LtaskExecuteDetails+","+_HtaskExecuteDetails+","+_DtaskExecuteDetails;
    }
    
	@Autowired
	private DelayQueueManager delayQueueManager;

	public void collectOtherDataTask() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber1 = random.nextInt(10);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("IGCollectOtherData"), 1 * randomNumber1));
	}
	public void collectLatestGoldTask() {

		logger.info("Start load data");
		Random random = new Random();
		int randomNumber2 = random.nextInt(15);
		// 新增任务
		delayQueueManager.put(new DelayTask(new TaskBase("IGCollectLatestGold"), 3 * randomNumber2));
	}
	public void clearSourceData() {
		logger.info("Start clear data");
		// 删除数据
		DBManager.getJT().execute("SET SQL_SAFE_UPDATES = 0");
		String copypricedata = "insert into trade_price_history select * from trade_price";
		String copydailydetaildata = "insert into trade_daily_detail_history select * from trade_daily_detail";
		String copydailysumdata = "insert into trade_daily_sum_history select * from trade_daily_sum";
		String copytradesumdata = "insert into trade_sum_history select * from trade_sum";
		String copytradelatestdata = "insert into trade_latest_history select * from trade_latest";
		DBManager.getJT().execute(copypricedata);
		DBManager.getJT().execute(copydailydetaildata);
		DBManager.getJT().execute(copydailysumdata);
		DBManager.getJT().execute(copytradesumdata);
		DBManager.getJT().execute(copytradelatestdata);
		DBManager.getJT().execute("delete from trade_daily_detail");
		DBManager.getJT().execute("delete from trade_daily_sum");
		DBManager.getJT().execute("delete from trade_latest");
		DBManager.getJT().execute("delete from trade_price");
		DBManager.getJT().execute("delete from trade_sum");
		DBManager.getJT().execute("delete from trade_single_sum");
		DBManager.getJT().execute("delete from trade_single_transaction");
		logger.info("Clear data done");
	}

}
