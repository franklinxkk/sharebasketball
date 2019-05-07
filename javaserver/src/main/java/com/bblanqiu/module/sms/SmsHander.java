package com.bblanqiu.module.sms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.bblanqiu.common.util.DateUtils;

public class SmsHander {
	// 查账户信息的http地址
	private static String URI_GET_USER_INFO = "http://yunpian.com/v1/user/get.json";

	//通用发送接口的http地址
	private static String URI_SEND_SMS = "http://yunpian.com/v1/sms/send.json";
	private static String URI_SEND_V_SMS = "https://voice.yunpian.com/v2/voice/send.json";

	// 模板发送接口的http地址
	private static String URI_TPL_SEND_SMS = "http://yunpian.com/v1/sms/tpl_send.json";
	public static String ZH_SMS = "【八拜科技】您的验证码是%s。如非本人操作，请忽略本短信";
	public static String US_SMS = "【brother-tech】your captcha is:%s";
	public static String REGISGER = "【八拜科技】感谢您注册八拜科技，您的验证码是%s";
	public static String VOICE_CONTENT = "您的验证码是%s";
	
	public static String ALRAM_CODE = "1725764";//【bblq】用户#username#的设备#device#产生告警#alarm#请及时处理！
	public static String CAPTCHA = "1823112";//【八拜科技】您的验证码是#code#。如非本人操作，请忽略本短信
	public static String ALARM = "1823108";//【八拜科技】设备#name#有异常#content#，请及时查看处理！
	public static String CHARGE = "1823110";//【八拜科技】尊敬的用户，您的帐号于#time#成功充值#content#元，如有疑问请联系客服。
	
	public static String CHARGE_CONTENT = "【八拜科技】尊敬的用户，您的帐号于%s成功充值%s元，如有疑问请联系客服。";
	public static String ALARM_CONTENT = "【八拜科技】设备%s有异常（%s），请及时查看处理！";
	
	//编码格式。发送编码格式统一用UTF-8
	private static String ENCODING = "UTF-8";
	public static String getApiKey(){
		return "d799ca88142bfd8872535981ea622e57";
	}
	public static void main(String[] args) throws IOException, URISyntaxException {
		/*//修改为您的apikey.apikey可在官网（http://www.yuanpian.com)登录后用户中心首页看到
		String apikey = "d799ca88142bfd8872535981ea622e57";
		//修改为您要发送的手机号
		String mobile = "13693463991";

		*//**************** 查账户信息调用示例 *****************//*
		System.out.println(SmsHander.getUserInfo(apikey));
		String device = "AD00000000";
		String alarm = "湿度值过低";
		String content = "【bblq】用户"+mobile+"的设备"+device+"产生告警（"+alarm+"）请及时处理！";
		System.out.println(SmsHander.sendSms(apikey,content, mobile));*/
		System.out.println(sendVocieSms(getApiKey(), "1234", "18518099161"));
	}
	public static String sendSms(String apikey, String text, String mobile) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		params.put("text", text);
		params.put("mobile", mobile);
		return post(URI_SEND_SMS, params);
	}
	public static String sendVocieSms(String apikey,String code, String mobile){
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		params.put("code", code);
		params.put("mobile", mobile);
		return post(URI_SEND_V_SMS, params);
	}
	public static String sendMsg(String apiKey, String captcha,String mobile, int language)throws IOException{
		if(language == 1){
			return sendSms(apiKey, String.format(SmsHander.ZH_SMS, captcha), "+86" + mobile);
		}else{
			return sendSms(apiKey, String.format(SmsHander.US_SMS, captcha), "+1" + mobile);
		}
	}
	public static String sendChargeMsg(String mobile,String charge) throws IOException{
		return sendSms(getApiKey(), String.format(SmsHander.CHARGE_CONTENT, DateUtils.formatTimestamp(DateUtils.getDate()), charge	), mobile);
	}
	public static String sendAlarnMsg(String mobile,String deviceName, String alarmContent) throws IOException{
		return sendSms(getApiKey(), String.format(SmsHander.ALARM_CONTENT, deviceName,alarmContent), mobile);
	}

	public static String post(String url, Map<String, String> paramsMap) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, ENCODING));
			}
			response = client.execute(method);
			client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}
	public static String getUserInfo(String apikey) throws IOException, URISyntaxException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("apikey", apikey);
		return post(URI_GET_USER_INFO, params);
	}	
}
