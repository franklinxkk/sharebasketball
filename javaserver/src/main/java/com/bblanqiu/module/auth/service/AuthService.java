package com.bblanqiu.module.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.UserAuthority;
import com.bblanqiu.common.service.MysqlBasicService;

@Service
public class AuthService extends MysqlBasicService{
	public void createAuth(UserAuthority ua) throws Exception{
		add(ua);
	}
	public List<UserAuthority> getAuths(Integer type) throws Exception{
		String hql = "from " +UserAuthority.class.getName();
		if(type != null){
			hql = hql+ " where type="+type;
		}
		return gets(hql);
	}
}
