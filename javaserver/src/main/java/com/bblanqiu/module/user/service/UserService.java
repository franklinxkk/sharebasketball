package com.bblanqiu.module.user.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.MysqlHqlUtil;
import com.bblanqiu.module.admin.bean.UserAllBean;

@Service
public class UserService extends MysqlBasicService{
	public void addUser(User user) throws Exception {
		add(user);
	}
	@SuppressWarnings("unchecked")
	public List<User> getUsers(int cursor, int limit)throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		List<User> list = null;
		try {
			session.beginTransaction();
			Criteria c = session.createCriteria(User.class);
			c.setFirstResult(cursor);
			c.setMaxResults(limit);
			c.addOrder(Order.desc("id"));
			list = c.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return list;
	}
	public List<UserAllBean> getAllUsers(int cursor, int limit){
		String senseDataSql = "select u.id,u.p_type as pType,u.phone,u.city,u.name,u.nickname,u.icon,u.longitude,u.latitude,u.question,u.answer,"
				+ "w.money,w.credit,w.score,(select count(*) from user_cost where user_id=u.id) as tCount "
				+ "from user u INNER JOIN wallet w on u.id=w.user_id order by u.id desc";
		List<UserAllBean> list = null;
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			SQLQuery query = session.createSQLQuery(senseDataSql);
			query.addScalar("id", IntegerType.INSTANCE)
            .addScalar("phone", StringType.INSTANCE)
            .addScalar("name", StringType.INSTANCE)
            .addScalar("nickname", StringType.INSTANCE)
            .addScalar("icon", StringType.INSTANCE)
            .addScalar("longitude", DoubleType.INSTANCE)
            .addScalar("latitude", DoubleType.INSTANCE)
            .addScalar("question", StringType.INSTANCE)
            .addScalar("answer", StringType.INSTANCE)
            .addScalar("city", StringType.INSTANCE)
            .addScalar("money", DoubleType.INSTANCE)
            .addScalar("credit", IntegerType.INSTANCE)
            .addScalar("score", IntegerType.INSTANCE)
            .addScalar("pType", IntegerType.INSTANCE)
            .addScalar("tCount", IntegerType.INSTANCE);
			query.setResultTransformer(Transformers.aliasToBean(UserAllBean.class));
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
	public long getUsersCount()throws Exception{
		String hql="select count(*) from " + User.class.getName();
		return getTotalCountByHql(hql);
	}
	public void updateUser(User user) {
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = MysqlHqlUtil.getSingleUpdateHql(user, "id");
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public User getUser(Integer id) {
		Session session = sessionFacotry.getCurrentSession();
		User mu=null;
		try {
			session.beginTransaction();
			Criteria c = session.createCriteria(User.class);
			c.add(Restrictions.eq("id", id));
			Object o = c.uniqueResult();
			if(o!=null){
				mu=(User)o;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return mu;
	}
	
	public User getUserbyPhone(String phone) {
		return getUserbyField("phone", phone);
	}
	public User getUserbyEmail(String email) {
		return getUserbyField("email", email);
	}
	public User getUserbyField(String key, String value) {
		Session session = sessionFacotry.getCurrentSession();
		User mu=null;
		try {
			session.beginTransaction();
			Criteria c = session.createCriteria(User.class);
			c.add(Restrictions.eq(key, value));
			Object o = c.uniqueResult();
			if(o!=null){
				mu=(User)o;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return mu;
	}
	public boolean isPhoneExists(String phone) {
		return isValueExists(User.class.getName(), "phone", phone);
	}
	public void updateUserPassword(String phone, String password){
		String hql = "update " + User.class.getName() + " m set m.password='"
				+ password +"' where " + "m.phone='" + phone +"'";
		update(hql);
	}
	public void updateUserType(Integer userId, int type){
		String hql = "update " + User.class.getName() + " m set m.type="
				+ type +" where " + "m.id=" + userId;
		update(hql);
	}
	public void updateUserTypeAndName(Integer userId, int type,String name){
		String hql = "update " + User.class.getName() + " m set m.type="
				+ type +",m.name='"+name+"' where " + "m.id=" + userId;
		update(hql);
	}
	public void updateUserPasswordByEmail(String email, String password){
		String hql = "update " + User.class.getName() + " m set m.password='"
				+ password +"' where " + "m.email='" + email +"'";
		update(hql);
	}
	public boolean validatePhoneAndPassword(String phone, String password){
		Session session = sessionFacotry.getCurrentSession();
		Long count=0l;
		try {
			session.beginTransaction();
			String hql = "select count(*) from " + User.class.getName() 
					+ " c where c.phone='" + phone + "'"
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
	
}
