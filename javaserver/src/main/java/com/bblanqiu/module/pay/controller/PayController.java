package com.bblanqiu.module.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.HandleExceptionController;

@Controller
public class PayController extends HandleExceptionController{
	
	/*@RequestMapping(value = "/pay/order", method = RequestMethod.GET)
	public @ResponseBody Object court(
    		@RequestParam(required = true) String sn
    		){
		return new OnlyResultBean("ok");
	}
	@RequestMapping(value = "/pay/order", method = RequestMethod.GET)
	public @ResponseBody Object court(
    		@RequestParam(required = true) String sn
    		){
		return new OnlyResultBean("ok");
	}*/
}
