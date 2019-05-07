package com.bblanqiu.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

public class DataCodeUtil {
	
	public static String encodeBase64(String s){
		return Base64.encodeBase64String(s.getBytes());
	}
	
	public static String decodeBase64(String s){
		return new String(Base64.decodeBase64(s));
	}
	public static boolean isChineseChar(String str){
	       boolean temp = false;
	       Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
	       Matcher m=p.matcher(str); 
	       if(m.find()){ 
	           temp =  true;
	       }
	       return temp;
	   }
}
