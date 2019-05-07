package com.bblanqiu.module.version.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.Version;
import com.bblanqiu.module.version.service.VersionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class VersionController extends HandleExceptionController{
	@Autowired
	ObjectMapper mapper;
	@Autowired
	VersionService versionService;
	//private static Logger logger = LoggerFactory.getLogger(VersionController.class);
	@RequestMapping(value = "/app/version", method = RequestMethod.GET)
	public @ResponseBody
	Object get(
			HttpServletRequest request) throws Exception {
		List<Version> list = versionService.gets();
		OnlyResultBean orb = new OnlyResultBean(list);
		return orb;
	}
}

