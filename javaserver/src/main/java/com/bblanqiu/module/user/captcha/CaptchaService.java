package com.bblanqiu.module.user.captcha;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.module.user.captcha.bean.PhoneCaptcha;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CaptchaService extends RedisService{
	@Autowired
	ObjectMapper mapper;
	private static final String PREFIX = "captcha:phone:";
	private static final String PREFIX2 = "captcha:email:";
	public String getCaptcha(String phone){
		Object o = getValue(PREFIX + phone);
		del(PREFIX + phone);
		if(o == null){
			return "-1";
		}else{
			try {
				PhoneCaptcha c = mapper.readValue(o.toString(), PhoneCaptcha.class);
				return c.getCaptcha();
			} catch (Exception e) {
				return "-1";
			}
		}
	}
	
	public void addCaptcha(String phone, String captcha, Date date) throws JsonProcessingException{
		PhoneCaptcha c = new PhoneCaptcha();
		c.setCaptcha(captcha);
		c.setPhone(phone);
		c.setTimestamp(date);
		addValue(PREFIX + phone, mapper.writeValueAsString(c), 300L);
	}
	public String getEmailCaptcha(String email){
		Object o = getValue(PREFIX2 + email);
		del(PREFIX2 + email);
		if(o == null){
			return "-1";
		}else{
			try {
				return o.toString();
			} catch (Exception e) {
				return "-1";
			}
		}
	}
	
	public void addEmailCaptcha(String email, String captcha) throws JsonProcessingException{
		PhoneCaptcha c = new PhoneCaptcha();
		c.setCaptcha(captcha);
		c.setPhone(email);
		addValue(PREFIX2 + email, captcha, 300L);
	}
}
