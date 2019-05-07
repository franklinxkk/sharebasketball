package com.bblanqiu.common.util;

import org.apache.commons.codec.binary.Base64;

public class GenerateBase64 {
	public static String encodeByBase64(String s){
		return new String(Base64.encodeBase64(s.getBytes()));
	}
	public static String decodeBase64(String s){
		return new String(Base64.decodeBase64(s));
	}
	public static void main(String []args){
		System.out.println(encodeByBase64("720a34d31d141444-7ce60b13720a34d31d141444b1680393"));
	}
}
