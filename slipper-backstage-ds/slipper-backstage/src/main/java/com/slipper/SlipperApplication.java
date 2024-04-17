package com.slipper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories("com.slipper.unieap.*")
//支持在字段或方法上进行注解 @CreateDate、@CreatedBy、@LastModifiedDate、@LastModifiedBy
@EnableJpaAuditing
//开启缓存
@EnableCaching
@EnableScheduling   //开启定时任务
public class SlipperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlipperApplication.class, args);
		//MailVO mailvo = new MailVO();
		//mailvo.setSubject("JR Daily Sum Data Failed, 请紧急处理");
		//mailvo.setContent("JR Daily Sum Data Failed, 请紧急处理");
		//EmailHelper.sendEmail(mailvo);
		//JRDBBO jRDBBO = (JRDBBO) ApplicationContextProvider.getBean("JRDBBO");
		//jRDBBO.dailySum();
		//jRDBBO.hourlyGold();
		//jRDBBO.priceGold();
		//jRDBBO.loadJRSourceDate();
		//jRDBBO.clearJRSourceData();
		//String url = "https://sentiment.api.caaprd.ig.com/sentiment/client-sentiment/GC";
		//HttpsUtils h = new HttpsUtils(null);
		//h.doGet(url);
		//IGDBBO iGDBBO = (IGDBBO) ApplicationContextProvider.getBean("IGDBBO");
		//iGDBBO.collectData();
		//Date now = new Date();
		//SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		//String pt = sdf.format(now);
	}

}
