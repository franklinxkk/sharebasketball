package com.bblanqiu.module.push.controller;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.push.Demo;
@Controller
public class PushController {
	@RequestMapping(value = "/push/send", method = RequestMethod.GET)
	public @ResponseBody Object getEmailCode(
			@RequestParam(required = true) String phone,
			@RequestParam(required = false) int num
			){
		UserCost uc = new UserCost();
		uc.setStartTime(DateUtils.getDate());
		uc.setConsume(1.024f);
		JSONObject json = new JSONObject(uc);
		Demo.sendBallBackMsg4a(phone, json);
		return new OnlyResultBean("ok");
	}
}
