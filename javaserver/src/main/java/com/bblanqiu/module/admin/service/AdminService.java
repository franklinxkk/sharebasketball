package com.bblanqiu.module.admin.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.BBUser;
import com.bblanqiu.common.mysql.model.Device;
import com.bblanqiu.common.mysql.model.SysStatistics;
import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.MysqlHqlUtil;

@Service
public class AdminService extends MysqlBasicService{
	public boolean validatePhoneAndPassword(String name, String password){
		Session session = sessionFacotry.getCurrentSession();
		Long count=0l;
		try {
			session.beginTransaction();
			String hql = "select count(*) from " + BBUser.class.getName() 
					+ " c where c.name='" + name + "'"
					+ " and c.password='" + password + "'";
			Query query = session.createQuery(hql);  
			count = (Long)query.uniqueResult();  
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return count > 0 ? true:false;
	}
	
	public long getAllUser(){
		return getTotalCount(User.class.getName());
	}
	public long getAllDevice(){
		return getTotalCount(Device.class.getName());
	}
	public Float getAllBallTrade() throws Exception{
		SysStatistics ss = get("from " + SysStatistics.class.getName());
		return ss.getBallTradeAmount();
	}
	public Float getAllBoxTrade() throws Exception{
		SysStatistics ss = get("from " + SysStatistics.class.getName());
		return ss.getBoxTradeAmount();
	}
	public SysStatistics getAllTrade() throws Exception{
		return get("from " + SysStatistics.class.getName());
	}
	public Float get1000Trade(int type) throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		List<UserCost> list = null;
		try {
			session.beginTransaction();
			Query q = session.createQuery(" from " + UserCost.class.getName() + " where type="+type+" order by startTime desc");
			q.setMaxResults(1000);
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}  
		float f =0f;
		for(UserCost uc : list){
			f+=uc.getConsume();
		}
		return f;
	}
	public void addAllBallTrade(Float f){
		SysStatistics ss = new SysStatistics();
		ss.setId(1);
		ss.setBallTradeAmount(f);
		update(MysqlHqlUtil.getSingleUpdateHql(ss, "id"));
	}
	public void addAllBoxTrade(Float f){
		SysStatistics ss = new SysStatistics();
		ss.setId(1);
		ss.setBoxTradeAmount(f);
		update(MysqlHqlUtil.getSingleUpdateHql(ss, "id"));
	}
	public Object getSumInbox() throws Exception{
		return get("select sum(consume) from " +UserCost.class.getName() +" where type=2");
	}
	public Object getSumInball() throws Exception{
		return get("select sum(consume) from " +UserCost.class.getName() +" where type=1");
	}
	public Object getSumInSn(String sn)throws Exception{
		return get("select sum(consume) from " +UserCost.class.getName() +" where deviceSn='"+sn+"'");
	}
	
}
