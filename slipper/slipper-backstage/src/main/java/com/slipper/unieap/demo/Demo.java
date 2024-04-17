package com.slipper.unieap.demo;

import org.apache.commons.lang3.StringUtils;

public class Demo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "02  守护方获胜，桑娜守护，川子破界.jpg";
		String b = "1.jpg";
		System.out.println(pad(2,Long.parseLong(generateCode(a))));
		System.out.println(pad(2,Long.parseLong(generateCode(b))));
	}

	public static String generateCode(String fileName) {
		if (StringUtils.contains(fileName, " ")) {
			return fileName.split(" ")[0];
		} else {
			return fileName.split("\\.")[0];
		}
	}

	public static String pad(int length, long num) {
		return String.format("%0".concat(String.valueOf(length)).concat("d"), num);
	}
}
