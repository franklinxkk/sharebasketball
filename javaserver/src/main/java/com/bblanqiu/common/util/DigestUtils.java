package com.bblanqiu.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class DigestUtils {
	public static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
	public static String base64decode(String s) throws UnsupportedEncodingException{
		return new String(Base64.decodeBase64(s),"utf-8");
	}
	public static String md5(String key,String salt){
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		if(salt != null){
	        //将盐数据传入消息摘要对象   
	        md.update(salt.getBytes());
		}
        //将口令的数据传给消息摘要对象   
        md.update(key.getBytes());   
        //生成输入口令的消息摘要   
        byte[] mds = md.digest();
     // 把密文转换成十六进制的字符串形式
        int j = mds.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = mds[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
	}
	public static void main(String []args) throws UnsupportedEncodingException{
//		System.out.println(base64decode("cHbbaMuVXJYejJB6RYZxKPClnxSr+ypiYYeAecJE0AH1HBz6hubrzgnbHzawAb9Dlwuj4tJkIz9ScScRnv5c8MNmNFvF2KMQjuxbuVZ7k+GwZ2FlnA09tdXWOzKm2TO+bUyvcHT91DSNqtxAS6l8FTPaPsKK4DM4AOi4jg+TQMw="));
		System.out.println(md5("hejing123#", null));
	}
}
