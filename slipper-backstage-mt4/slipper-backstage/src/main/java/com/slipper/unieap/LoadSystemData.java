package com.slipper.unieap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoadSystemData {
	private final Log logger = LogFactory.getLog(LoadSystemData.class);
	@Value("${unieap.app-base-url}")
	String app_base_url;
	@Value("${unieap.sharefolder-path}")
	String sharefolder_path;
	@Value("${service.context-path}")
	String context_path;

	public void loadSystemConfigure() {
		logger.info("====================================================================");
		logger.info("***加载系统初始化参数  ***");
		logger.info("====================================================================");
		UnieapConstants.APP_BASE_URL = app_base_url;
		logger.info("====================================================================");
		logger.info("***应用发布IP和端口***");
		logger.info(UnieapConstants.APP_BASE_URL);
		logger.info("====================================================================");
		UnieapConstants.SHAREFOLDER_PATH = sharefolder_path;
		logger.info("====================================================================");
		logger.info("***共享文件目录 ***");
		logger.info(UnieapConstants.SHAREFOLDER_PATH);
		logger.info("====================================================================");
		UnieapConstants.APP_PATH = context_path;
		logger.info("====================================================================");
		logger.info("***应用访问地址***");
		logger.info(UnieapConstants.APP_BASE_URL + UnieapConstants.APP_PATH);
		/*try {
			webSocketServer.run();
			logger.info("====================================================================");	
			logger.info("***websocket访问地址***");
			logger.info(webSocketServer.nettyConfig.getUrl());
			logger.info("====================================================================");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/	
	}

}
