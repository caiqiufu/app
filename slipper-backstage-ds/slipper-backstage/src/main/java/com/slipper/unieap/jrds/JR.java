package com.slipper.unieap.jrds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Random;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class JR {
	public final Log logger = LogFactory.getLog(JR.class);
	public String test(String name) {
		return name+"test";
	}
	public String generateTimestampAndKey(String infAddress) {
		String timestamp = G();
		String key = w(infAddress, timestamp, "v1");
		//System.out.println("timestamp:"+timestamp+",key:"+key);
		logger.info("timestamp:"+timestamp+",key:"+key);
		return timestamp + "," + key;
	}

	public String G() {
		// long D = System.currentTimeMillis() -
		// com.jrjr.jrchina.comm.manager.g.m().l();
		// APP启动时请求的服务器时间接口：http://120.77.237.33:23001/?r=app/timestamp&v=v1&key=fed10f4cd5a78668&timestamp=168050019030468208971387&access_token=&device=android&cv=79&lan=chinese&userid=
		// com.jrjr.jrchina.comm.manager.g.m().l() = 当前时间戳毫秒 - 上面接口result返回值*1000
		// 我们这里就写个1000就行
		long D = System.currentTimeMillis() - 1000;
		long nanoTime = System.nanoTime();
		return String.valueOf(D / 1000) + String.valueOf(nanoTime + (Math.abs(new Random().nextLong()) % nanoTime));
	}

	// 这个方法在计算key的时候会调用，还是当前时间戳见那个时间差，暂时写1000
	public long D() {
		// return System.currentTimeMillis() - com.jrjr.jrchina.comm.manager.g.m().l();
		return System.currentTimeMillis() - 1000;
	}

	public String w(String str, String str2, String str3) {
		char[] charArray = str.toCharArray();
		int i2 = 0;
		while (i2 < charArray.length - 1) {
			int i3 = i2 + 1;
			for (int i4 = i3; i4 < charArray.length; i4++) {
				if (charArray[i2] > charArray[i4]) {
					char c2 = charArray[i2];
					charArray[i2] = charArray[i4];
					charArray[i4] = c2;
				}
			}
			i2 = i3;
		}
		String str4 = new String(charArray);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		String format = simpleDateFormat.format(Long.valueOf(D()));
		String valueOf = String.valueOf(str4 + format + str3 + str2);
		byte[] bArr = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			messageDigest.update(valueOf.getBytes());
			bArr = messageDigest.digest();
		} catch (NoSuchAlgorithmException e2) {
			e2.printStackTrace();
		}
		String d2 = d(bArr);
		return d2.length() >= 16 ? d2.substring(0, 16) : d2;
	}

	private String d(byte[] bArr) {
		Formatter formatter = new Formatter();
		int length = bArr.length;
		for (int i2 = 0; i2 < length; i2++) {
			formatter.format("%02x", Byte.valueOf(bArr[i2]));
		}
		String formatter2 = formatter.toString();
		formatter.close();
		return formatter2;
	}
}
