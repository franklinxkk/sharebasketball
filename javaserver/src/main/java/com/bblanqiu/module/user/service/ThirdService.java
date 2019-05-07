package com.bblanqiu.module.user.service;

import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.ThirdInfo;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.MysqlHqlUtil;

@Service
public class ThirdService extends MysqlBasicService{
	public void createThirdInfo(ThirdInfo ti) throws Exception{
		add(ti);
	}
	public ThirdInfo getThirdInfo(Integer userId){
		return getByField(ThirdInfo.class.getName(), "userId", userId);
	}
	public void updateThirdInfo(ThirdInfo w){
		update(MysqlHqlUtil.getSingleUpdateHql(w, "userId"));
	}
	
}
