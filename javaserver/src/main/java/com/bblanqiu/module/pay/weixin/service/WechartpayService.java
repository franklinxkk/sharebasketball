package com.bblanqiu.module.pay.weixin.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom2.JDOMException;
import org.springframework.stereotype.Service;

import com.bblanqiu.module.pay.util.ConfigUtil;
import com.bblanqiu.module.pay.util.MD5Util;
import com.bblanqiu.module.pay.util.PayCommonUtil;
import com.bblanqiu.module.pay.weixin.bean.WxInitBean;


@Service
public class WechartpayService  {
	public WxInitBean getWxPayInfo(String tradeId, int price100,String ip)throws Exception{
		String randomString = PayCommonUtil.getRandomString(32);
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();  
		parameterMap.put("appid", PayCommonUtil.APPID);  
		parameterMap.put("mch_id", PayCommonUtil.MCH_ID);  
		parameterMap.put("nonce_str", randomString);  
		parameterMap.put("body", "bblqwxcz");
		parameterMap.put("out_trade_no", tradeId);
		parameterMap.put("fee_type", "CNY");  
		parameterMap.put("total_fee", String.valueOf(price100));  
		parameterMap.put("spbill_create_ip", ip);  
		parameterMap.put("notify_url", "https://www.bblanqiu.com/bblq/wxpay/charge/callback");
		parameterMap.put("trade_type", "APP"); 
		String sign = createSign("utf-8",parameterMap);
		parameterMap.put("sign", sign);  
		String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
		System.out.println("rand:"+randomString);
		System.out.println("sign:"+sign);
		System.out.println("xml:"+requestXML);  
		String result = PayCommonUtil.httpRequest(  
				"https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
				requestXML);  
		System.out.println(result);  
		Map<String, String> map = null;  
		try {  
			map = PayCommonUtil.doXMLParse(result); 
			String noncestr = PayCommonUtil.getRandomString(32);
			Long timestampwx = Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
			SortedMap<String, Object> resultMap = new TreeMap<String, Object>();  
			resultMap.put("appid", PayCommonUtil.APPID);  
			resultMap.put("partnerid", PayCommonUtil.MCH_ID);  
			resultMap.put("prepayid", map.get("prepay_id"));  
			resultMap.put("package", "Sign=WXPay");  
			resultMap.put("noncestr", noncestr);  
			//本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
			resultMap.put("timestamp", timestampwx);  
			String sign2 = createSign("utf-8",resultMap);
			resultMap.put("sign", sign2);
			WxInitBean wib = new WxInitBean(PayCommonUtil.APPID, PayCommonUtil.MCH_ID, 
					map.get("prepay_id"), "Sign=WXPay", noncestr, String.valueOf(timestampwx), sign2);
			System.out.println(wib);
			return wib;
		} catch (JDOMException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		} catch (IOException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
		return null;
	}
    public static String createSign(String characterEncoding,SortedMap<String,Object> parameters){  
        StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + ConfigUtil.API_KEY);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }  
	public static void main(String []args) throws Exception{
		String randomString = PayCommonUtil.getRandomString(32);
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();  
		parameterMap.put("appid", PayCommonUtil.APPID);  
		parameterMap.put("mch_id", PayCommonUtil.MCH_ID);  
		parameterMap.put("nonce_str", randomString);  
		parameterMap.put("body", "bblqwxcz");
		parameterMap.put("out_trade_no", "bblq123456789");
		parameterMap.put("fee_type", "CNY");  
		int total = 100;
		parameterMap.put("total_fee", String.valueOf(total));  
		parameterMap.put("spbill_create_ip", "61.157.126.30");  
		parameterMap.put("notify_url", "http://www.bblanqiu.com/bblq/wxpay/charge/callback");
		parameterMap.put("trade_type", "APP"); 
		String sign = createSign("utf-8",parameterMap);
		parameterMap.put("sign", sign);  
		String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
		System.out.println("rand:"+randomString);
		System.out.println("sign:"+sign);
		System.out.println("xml:"+requestXML);  
		String result = PayCommonUtil.httpRequest(  
				"https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
				requestXML);  
		System.out.println(result);  
		Map<String, String> map = null;  
		try {  
			map = PayCommonUtil.doXMLParse(result); 
			System.out.println(map);
			SortedMap<String, Object> resultMap = new TreeMap<String, Object>();  
			resultMap.put("appid", ConfigUtil.APPID);  
			resultMap.put("partnerid", ConfigUtil.MCH_ID);  
			resultMap.put("prepayid", map.get("prepay_id"));  
			resultMap.put("package", "Sign=WXPay");  
			resultMap.put("noncestr", PayCommonUtil.getRandomString(32));  
			//本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
			resultMap.put("timestamp", Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0,10)));  
			String sign2 = createSign("utf-8",resultMap);
			resultMap.put("sign", sign2); 
			System.out.println(resultMap);
		} catch (JDOMException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		} catch (IOException e) {  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
		}  
	}
}
