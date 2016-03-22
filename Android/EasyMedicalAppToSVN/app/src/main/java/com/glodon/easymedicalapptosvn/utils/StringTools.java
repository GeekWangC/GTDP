package com.glodon.easymedicalapptosvn.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * StringTools
 * @author shirr
 *
 */
public class StringTools {

	private static final String TAG = "StringTools";

	/**
	 * UTF-8奇数中文乱码
	 * 
	 * @param gbk
	 * @return
	 */
	public static String correctEncode(String gbk) {
		String iso;
		String utf = "";
		try {
			iso = new String(gbk.getBytes("UTF-8"), "ISO-8859-1");
			utf = new String(iso.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// 输出Log
			Log.e(TAG, e.getMessage());
			utf = "";
		}
		// 模拟UTF-8编码的网站显示
		return utf;
	}
	

	/**
	 * 获取不重复的值
	 * @return
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//2014-07-25 14:39:55
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String timeStr = formatter.format(curDate);
        return timeStr;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

}
