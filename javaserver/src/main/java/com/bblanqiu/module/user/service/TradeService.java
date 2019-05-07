package com.bblanqiu.module.user.service;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.BasketBall;
import com.bblanqiu.common.mysql.model.ChargeLog;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.mysql.model.UserScore;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.MysqlHqlUtil;
import com.bblanqiu.module.admin.bean.ChargeLogBean;
import com.bblanqiu.module.admin.bean.UserAllBean;
import com.bblanqiu.module.admin.bean.UserCostInfoBean;

@Service
public class TradeService extends MysqlBasicService{
	public void createWallet(Wallet w) throws Exception{
		add(w);
	}
	public Wallet getWallet(Integer userId){
		return getByField(Wallet.class.getName(), "userId", userId);
	}
	public void updateWallet(Wallet w){
		update(MysqlHqlUtil.getSingleUpdateHql(w, "userId"));
	}
	
	public void createUserCost(UserCost uc) throws Exception{
		add(uc);
	}
	public void useBasketball(UserCost uc, BasketBall basketBall){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(uc);
			String hql = MysqlHqlUtil.getSingleUpdateHql(basketBall, "id");
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void useStorageBox(UserCost uc, StorageBox sb){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(uc);
			String hql = MysqlHqlUtil.getSingleUpdateHql(sb, "id");
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void closeStorageBox(UserCost uc, StorageBox sb, Wallet w){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.createQuery(MysqlHqlUtil.getSingleUpdateHql(uc, "id")).executeUpdate();
			session.createQuery(MysqlHqlUtil.getSingleUpdateHql(w, "userId")).executeUpdate();
			String hql = MysqlHqlUtil.getSingleUpdateHql(sb, "id");
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void closeBasketball(UserCost uc, BasketBall bb, Wallet w){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.createQuery(MysqlHqlUtil.getSingleUpdateHql(uc, "id")).executeUpdate();
			session.createQuery(MysqlHqlUtil.getSingleUpdateHql(w, "userId")).executeUpdate();
			String hql = MysqlHqlUtil.getSingleUpdateHql(bb, "sn");
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public boolean isUsed(Integer userId, Integer type){
		String hql = "select count(*) from " + 
				UserCost.class.getName()
				+ "  where userId=" + userId+" and type="+type+" and state=0";
		return isValueExists(hql);
	}
	public void updateUserCost(UserCost uc){
		update(MysqlHqlUtil.getSingleUpdateHql(uc, "id"));
	}
	public List<UserCost> getUserCostByUserIdAndType(Integer userId,Integer type,int cursor, int limit) throws Exception{
		String queryHql = " from " + UserCost.class.getName() 
				+ " where state=1 and userId=" + userId;
		if(type != null){
			queryHql +=" and type="+type;
		}
		queryHql+=" order by startTime desc";
		return gets(queryHql, cursor, limit);
	}
	public long getUserCostCountByUserIdAndType(int userId,Integer type){
		String hql = "select count(*) from " + UserCost.class.getName()
				+ " where state=1 and userId=" + userId;
		if(type != null){
			hql +=" and type="+type;
		}
		return getTotalCountByHql(hql);
	}
	public long getUserCostCount(){
		return getTotalCount(UserCost.class.getName());
	}
	public List<UserCost> getUserCosts(int cursor, int limit) throws Exception{
		return gets(" from " + UserCost.class.getName() +" order by startTime desc ", cursor, limit);
	}
	public List<UserCostInfoBean> getUserCostInfos(int cursor, int limit) throws Exception{
		String senseDataSql = "select u.phone,uc.id,uc.user_id as userId,uc.consume,uc.device_sn as deviceSn, "
				+ "uc.ball_sn as ballSn,uc.num,"
				+ "uc.start_time as startTime,uc.end_time as endTime, uc.address,uc.type,uc.state "
				+ "from user_cost uc INNER JOIN user u on u.id=uc.user_id order by uc.id desc";
		List<UserCostInfoBean> list = null;
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(senseDataSql);
			query.addScalar("id", IntegerType.INSTANCE)
            .addScalar("phone", StringType.INSTANCE)
            .addScalar("userId", IntegerType.INSTANCE)
            .addScalar("consume", FloatType.INSTANCE)
            .addScalar("ballSn", StringType.INSTANCE)
            .addScalar("deviceSn", StringType.INSTANCE)
            .addScalar("num", IntegerType.INSTANCE)
            .addScalar("startTime", TimestampType.INSTANCE)
            .addScalar("endTime", TimestampType.INSTANCE)
            .addScalar("address", StringType.INSTANCE)
            .addScalar("type", IntegerType.INSTANCE)
            .addScalar("state", IntegerType.INSTANCE);
			query.setResultTransformer(Transformers.aliasToBean(UserCostInfoBean.class));
			query.setFirstResult(cursor);
			query.setMaxResults(limit);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return list;
	}
	public UserCost getCurrentUserBallCost(String deviceSn) throws Exception{
		String queryHql = " from " + UserCost.class.getName() 
				+ " where deviceSn=" + deviceSn+" and state=0 and type=1";
		return get(queryHql);
	}
	public UserCost getCurrentUserBoxCost(Integer userId) throws Exception{
		String queryHql = " from " + UserCost.class.getName() 
				+ " where userId=" + userId+" and state=0 and type=2";
		return get(queryHql);
	}
	public UserCost getCurrentUserBallCost(Integer userId) throws Exception{
		String queryHql = " from " + UserCost.class.getName() 
				+ " where userId=" + userId+" and state=0 and type=1";
		return get(queryHql);
	}
	
	public UserCost getLatestUserBallCost(Integer userId){
		String queryHql = " from " + UserCost.class.getName() 
				+ " where userId="+userId+" and type=1 order by startTime desc";
		Session session = sessionFacotry.getCurrentSession();
		List<UserCost> list = null;
		try {
			session.beginTransaction();
			Query q = session.createQuery(queryHql);
			q.setMaxResults(1);
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;	
		}
	}
	
	public void createCharge(ChargeLog cl) throws Exception{
		add(cl);
	}
	public List<ChargeLog> getcharges(Integer userId) throws Exception{
		String queryHql = " from " + ChargeLog.class.getName() 
				+ " where userId=" + userId+" order by createTime desc";
		return gets(queryHql);
	}
	public List<ChargeLog> getSucceedcharges(Integer userId) throws Exception{
		String queryHql = " from " + ChargeLog.class.getName() 
				+ " where userId=" + userId+" and state = 1 order by createTime desc";
		return gets(queryHql);
	}
	public long getSucceedchargesCount(int userId){
		String hql = "select count(*) from " + ChargeLog.class.getName()
				+ " where userId=" + userId;
		return getTotalCountByHql(hql);
	}
	public List<ChargeLog> getCurrentUncharges(Integer userId) throws Exception{
		String queryHql = " from " + ChargeLog.class.getName() 
				+ " where userId=" + userId+" and state= 0 order by createTime desc limit 1";
		return gets(queryHql);
	}
	public List<ChargeLog> getCurrentUncharges(String outtradeno) throws Exception{
		String queryHql = " from " + ChargeLog.class.getName() 
				+ " where outtradeno='" + outtradeno+"' and state=0 order by createTime desc";
		Session session = sessionFacotry.getCurrentSession();
		List<ChargeLog> list = null;
		try {
			session.beginTransaction();
			Query q = session.createQuery(queryHql);
			q.setMaxResults(1);
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}  
		return list;
	}
	public void updatecharges(ChargeLog uc){
		update(MysqlHqlUtil.getSingleUpdateHql(uc, "id"));
	}
	public long getChargeLogCount(){
		return getTotalCount(ChargeLog.class.getName());
	}
	public List<ChargeLog> getChargeLogs(int cursor, int limit) throws Exception{
		return gets(" from " + ChargeLog.class.getName() +" order by createTime desc ", cursor, limit);
	}
	
	public List<ChargeLogBean> getChargeLogInfos(int cursor, int limit) throws Exception{
		String senseDataSql = "select u.phone,cl.id,cl.user_id as userId,"
				+ " cl.charge_quota as chargeQuota,cl.type,cl.state,"
				+ " cl.outtradeno,cl.create_time as createTime "
				+ " from charge_log cl INNER JOIN user u on u.id=cl.user_id order by cl.create_time desc";
		List<ChargeLogBean> list = null;
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(senseDataSql);
			query.setResultTransformer(Transformers.aliasToBean(ChargeLogBean.class));
			query.setFirstResult(cursor);
			query.setMaxResults(limit);
			list = query.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return list;
	}
	
	public void createUserScore(UserScore uc) throws Exception{
		add(uc);
	}
	public List<UserScore> getUserScoreByUserIdAndType(Integer userId,Integer pn) throws Exception{
		String queryHql = " from " + UserScore.class.getName() 
				+ " where userId=" + userId;
		if(pn != null){
			if(pn > 0){
				queryHql +=" and type < 200";	
			}else{
				queryHql +=" and type > 200";	
			}
			
		}
		queryHql+=" order by createTime desc";
		return gets(queryHql);
	}
	public long getUserScoreCount(int userId,Integer pn){
		String hql = "select count(*) from " + UserScore.class.getName()
				+ " where userId=" + userId;
		if(pn != null){
			if(pn > 0){
				hql +=" and type < 200";	
			}else{
				hql +=" and type > 200";	
			}
			
		}
		return getTotalCountByHql(hql);
	}
}
