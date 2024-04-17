package com.slipper.unieap.ea.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.slipper.unieap.ea.repository.TradeNotifyRepository;
import com.slipper.unieap.task.job.DynamicJob;

/**
 * 生成提醒任务
 */
@Service
public class EAGenerateNotify extends DynamicJob {

	@Autowired
	public TradeNotifyRepository tradeNotifyRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		generateNotify();
	}

	public void generateNotify() {
		generateEANotify();
		generateTradeNotify();
	}

	@Value("${app_lzy.eaHIP}")
	String eaHIP;

	public void generateEANotify() {
		List<String> notifyTypes = new ArrayList<String>();
		notifyTypes.add("H");
		notifyTypes.add("D");
		Integer maxId = tradeNotifyRepository.getMaxId(notifyTypes);
		if (maxId == null) {
			maxId = Integer.valueOf("0");
		}
		String eaIP = eaHIP.split(",")[1];
		tradeNotifyRepository.generateEANotify(maxId, eaIP);
	}

	public void generateTradeNotify() {
		List<String> notifyTypes = new ArrayList<String>();
		notifyTypes.add("T");
		Integer maxId = tradeNotifyRepository.getMaxId(notifyTypes);
		if (maxId == null) {
			maxId = Integer.valueOf("0");
		}
		tradeNotifyRepository.generateTradeNotify(maxId);
	}
}
