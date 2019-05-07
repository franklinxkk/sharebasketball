package com.bblanqiu.module.user.service;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.service.MysqlBasicService;

@Service
public class TokenService extends MysqlBasicService{
	public Token getToken(String token) {
		Session session = sessionFacotry.getCurrentSession();
		Object o=null;
		try {
			session.beginTransaction();
			Criteria c = session.createCriteria(Token.class);
			c.add(Restrictions.eq("token", token));
			o = c.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		Token tokenBean = null;
		if(o != null){
			tokenBean = (Token)o;
		}
		return tokenBean;
	}
	public Token getToken(Integer user) throws Exception {
		return get("from " +Token.class.getName() + " where userId=" + user);
	}
	public void deleteToken(String token) {
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from Token t where t.token='" + token + "'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteTokenByUser(Integer user) {
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from Token t where t.userId='" + user + "'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public void addToken(Token token) throws Exception {
		Token oldToken=getToken(token.getUserId());
		if(oldToken!=null){
			deleteTokenByUser(token.getUserId());
		}
		add(token);
	}
	
	public void updateToken(Token token){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.update(token);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
}
