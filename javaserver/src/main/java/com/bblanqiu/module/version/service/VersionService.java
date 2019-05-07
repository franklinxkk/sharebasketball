package com.bblanqiu.module.version.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.Version;
import com.bblanqiu.common.service.MysqlBasicService;

@Service
public class VersionService extends MysqlBasicService{
	public void add(Version v) throws Exception{
		add(v);
	}

	public List<Version> gets() throws Exception{
		String deviceQueryHql = " from " + Version.class.getName();
		return gets(deviceQueryHql);
	}
}
