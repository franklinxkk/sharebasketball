package com.bblanqiu.module.admin.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.module.admin.service.AdminService;

@Component
public class AdminFacade extends RedisService{
	@Autowired
	AdminService adminService;
	public Double getSumInbox() throws Exception{
		Double f = 0d;
		try {
			Object o = adminService.getSumInbox();
			if(o != null){
				f = (double)o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public Double getSumInball() throws Exception{
		Double f = 0d;
		try {
			Object o = adminService.getSumInball();
			if(o != null){
				f = (double)o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
	public Double getSumInSn(String sn)throws Exception{
		Double f = 0d;
		try {
			Object o = adminService.getSumInSn(sn);
			if(o != null){
				f = (double)o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
/*	public Float getAllBallTrade() throws Exception{
		Float f = null;
		Object o = getValue(CacheTableNames.PREFIX_STATISTIC + ":all:balltrade");
		if(o == null){
			f = adminService.getAllBallTrade();
			addValue(CacheTableNames.PREFIX_STATISTIC + ":all:balltrade", String.valueOf(f), null);
		}else{
			f = Float.valueOf(o.toString());
		}
		return f;
	}
	public void addBallTrade(Float f){
		try {
			Object o = getValue(CacheTableNames.PREFIX_STATISTIC + ":all:balltrade");
			if(o == null){
				Float in = adminService.getAllBallTrade();
				addValue(CacheTableNames.PREFIX_STATISTIC + ":all:balltrade", String.valueOf(in + f), null);
				adminService.addAllBallTrade(Float.valueOf(o.toString()) + f);
			}else{
				addValue(CacheTableNames.PREFIX_STATISTIC + ":all:balltrade", String.valueOf(Float.valueOf(o.toString()) + f), null);
				adminService.addAllBallTrade(Float.valueOf(o.toString()) + f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Float getAllBoxTrade() throws Exception{
		Float f = null;
		Object o = getValue(CacheTableNames.PREFIX_STATISTIC + ":all:boxtrade");
		if(o == null){
			f = adminService.getAllBoxTrade();
			addValue(CacheTableNames.PREFIX_STATISTIC + ":all:boxtrade", String.valueOf(f), null);
		}else{
			f = Float.valueOf(o.toString());
		}
		return f;
	}
	public void addBoxTrade(Float f){
		try {
			Object o = getValue(CacheTableNames.PREFIX_STATISTIC + ":all:boxtrade");
			if(o == null){
				Float in = adminService.getAllBoxTrade();
				addValue(CacheTableNames.PREFIX_STATISTIC + ":all:boxtrade", String.valueOf(in + f), null);
				adminService.addAllBoxTrade(in + f);
			}else{
				addValue(CacheTableNames.PREFIX_STATISTIC + ":all:boxtrade", String.valueOf(Float.valueOf(o.toString()) + f), null);
				adminService.addAllBoxTrade(Float.valueOf(o.toString()) + f);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	public long getAlluser(){
		return adminService.getAllUser();
	}
	public long getAllDevice(){
		return adminService.getAllDevice();
	}
}
