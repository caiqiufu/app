package com.slipper.service.unieap.base.sms;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;

import darabonba.core.client.ClientOverrideConfiguration;

@Service
public class SMSBO {
	/**
	 * 发送短信
	 * 
	 * @param phoneNumber
	 * @param content
	 * @param tempCode
	 * @throws Exception
	 */
	public void sendSMS(String[] noumbers, String content, String tempCode) throws Exception {
		//sendSMSByAliyun(noumbers, content, tempCode);
		sendSMSByAliyun2(noumbers, content, tempCode);
	}

	@Value("${aliyun.appcode}")
	String appcode;
	@Value("${aliyun.accessKeyId}")
	String accessKeyId;
	@Value("${aliyun.accessKeySecret}")
	String accessKeySecret;

	public void sendSMSByAliyun(String[] noumbers, String content, String tempCode) throws Exception {
		String phoneNumberStr = "";
		if (noumbers.length > 1) {
			phoneNumberStr = StringUtils.join(noumbers, ",");
		} else if (noumbers.length == 1) {
			phoneNumberStr = noumbers[0];
		} else {
			return;
		}
		String host = "https://dfsns.market.alicloudapi.com";
		String path = "/data/send_sms";
		String method = "POST";
		Map<String, String> headers = new HashMap<String, String>();
		// 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		headers.put("Authorization", "APPCODE " + appcode);
		// 根据API的要求，定义相对应的Content-Type
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		// bodys.put("content", "code:"+content);
		bodys.put("content", content);
		// 该模板为调试接口专用，短信下发有受限制，调试成功后请联系客服报备专属模板
		bodys.put("template_id", tempCode);

		bodys.put("phone_number", phoneNumberStr);

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
			System.out.println(response.toString());
			// 获取response的body
			// System.out.println(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendSMSByAliyun2(String[] noumbers, String content, String tempCode) throws Exception {
		String phoneNumberStr = "";
		if (noumbers.length > 1) {
			phoneNumberStr = StringUtils.join(noumbers, ",");
		} else if (noumbers.length == 1) {
			phoneNumberStr = noumbers[0];
		} else {
			return;
		}
		// HttpClient Configuration
		/*
		 * HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
		 * .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout
		 * time, the default is 10 seconds .responseTimeout(Duration.ofSeconds(10)) //
		 * Set the response timeout time, the default is 20 seconds .maxConnections(128)
		 * // Set the connection pool size .maxIdleTimeOut(Duration.ofSeconds(50)) //
		 * Set the connection pool timeout, the default is 30 seconds // Configure the
		 * proxy .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new
		 * InetSocketAddress("<your-proxy-hostname>", 9001))
		 * .setCredentials("<your-proxy-username>", "<your-proxy-password>")) // If it
		 * is an https connection, you need to configure the certificate, or ignore the
		 * certificate(.ignoreSSL(true)) .x509TrustManagers(new X509TrustManager[]{})
		 * .keyManagers(new KeyManager[]{}) .ignoreSSL(false) .build();
		 */

		// Configure Credentials authentication information, including ak, secret, token
		StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
				// Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and
				// ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
				//.accessKeyId(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"))
				//.accessKeySecret(System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"))
				.accessKeyId(accessKeyId)
				.accessKeySecret(accessKeySecret)
				// .securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS
				// token
				.build());

		// Configure the Client
		AsyncClient client = AsyncClient.builder().region("cn-shanghai") // Region ID
				// .httpClient(httpClient) // Use the configured HttpClient, otherwise use the
				// default HttpClient (Apache HttpClient)
				.credentialsProvider(provider)
				// .serviceConfiguration(Configuration.create()) // Service-level configuration
				// Client-level configuration rewrite, can set Endpoint, Http request
				// parameters, etc.
				.overrideConfiguration(ClientOverrideConfiguration.create()
						// Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
						.setEndpointOverride("dysmsapi.aliyuncs.com")
				// .setConnectTimeout(Duration.ofSeconds(30))
				).build();

		// Parameter settings for API request
		SendSmsRequest sendSmsRequest = SendSmsRequest.builder().signName("深圳市龙知易科技有限公司").templateCode(tempCode)
				.phoneNumbers(phoneNumberStr).templateParam(content)
				// Request-level configuration rewrite, can set Http request parameters, etc.
				// .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new
				// HttpHeaders()))
				.build();

		// Asynchronously get the return value of the API request
		CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
		// Synchronously get the return value of the API request
		SendSmsResponse resp = response.get();
		System.out.println(new Gson().toJson(resp));
		// Asynchronous processing of return values
		/*
		 * response.thenAccept(resp -> { System.out.println(new Gson().toJson(resp));
		 * }).exceptionally(throwable -> { // Handling exceptions
		 * System.out.println(throwable.getMessage()); return null; });
		 */

		// Finally, close the client
		client.close();
	}
}
