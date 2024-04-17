package com.slipper.unieap.jrds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ds")
public class DsController {

	@Autowired
	JR jr;
	
	@Autowired
	JRDBBO jRDBBO;

	@RequestMapping("/jr/key")
	public @ResponseBody String jrKey(String infName) throws Exception {
		return jr.generateTimestampAndKey(infName);
	}
	@RequestMapping("/collectionStatus")
	public @ResponseBody String dsCollectionStatus() throws Exception {
		return jRDBBO.getTaskExecuteDetails();
	}
}
