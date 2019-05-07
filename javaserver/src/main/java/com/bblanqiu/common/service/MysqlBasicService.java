package com.bblanqiu.common.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author xkk
 *
 */
public abstract class MysqlBasicService{
    @Autowired
    protected ObjectMapper mapper;
	@Autowired
	protected  SessionFactory sessionFacotry;
	private static Logger logger = LoggerFactory.getLogger(MysqlBasicService.class);
	public boolean isValueExists(String className, String field, Object value){
		Session session = sessionFacotry.getCurrentSession();
		Long count=0l;
		try {
			session.beginTransaction();
			String hql = "select count(*) from " + className + " c where c." + field + "='" + value + "'";
			Query query = session.createQuery(hql);  
			count = (Long)query.uniqueResult();  
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return count > 0 ? true:false;
	}
	
	public boolean isValueExists(String hql){
		Session session = sessionFacotry.getCurrentSession();
		Long count=0l;
		try {
			session.beginTransaction();
			Query query = session.createQuery(hql);  
			count = (Long)query.uniqueResult();  
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return count > 0 ? true:false;
	}
	
	public void add(Object object)throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(object);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	
	public void deleteById(String clazz, Object id){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from " + clazz + "  where id=" + id;
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			logger.error("delete to mysql error:"+id.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String hql)throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		T t=null;
		try {
			session.beginTransaction();
			t = (T)session.createQuery(hql).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			logger.error("get in mysql error:"+hql);
		}  
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> gets(String hql)throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		List<T> list = null;
		try {
			session.beginTransaction();
			list = session.createQuery(hql).list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			logger.error("gets in mysql error:"+hql);
		}  
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> gets(String hql, int cursor, int limit)throws Exception{
		Session session = sessionFacotry.getCurrentSession();
		List<T> list = null;
		try {
			session.beginTransaction();
			Query q = session.createQuery(hql);
			q.setFirstResult(cursor);
			q.setMaxResults(limit);
			list = q.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			logger.error("gets in mysql error:"+hql);
		}  
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getById(String className, Integer id){
		Session session = sessionFacotry.getCurrentSession();
		T t=null;
		try {
			session.beginTransaction();
			String hql = "from " + className + " where id="+id;
			t = (T)session.createQuery(hql).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			logger.error("get in mysql error:className={},id={}",className, id);
		}  
		return t;
	}
	public<T> T getByField(String className, String name, Object field){
		Session session = sessionFacotry.getCurrentSession();
		T t=null;
		try {
			
			session.beginTransaction();
			Criteria c = session.createCriteria(Class.forName(className));
			c.add(Restrictions.eq(name, field));
			Object o = c.uniqueResult();
			if(o!=null){
				t=(T)o;
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return t;
	}
	
	public Long getTotalCount(String className){
		String hql="select count(*) from "+className;
		return getTotalCountByHql(hql);
	}
	public Long getTotalCountByHql(String hql){
		Session session =sessionFacotry.getCurrentSession();
		Long count=0L;
		try {
			session.beginTransaction();
			Query query=session.createQuery(hql);
			count = (Long) query.uniqueResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public List getByIds(List ids, Class paramClass){
		List list = null;
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			Criteria c = session.createCriteria(paramClass);
			c.add(Restrictions.in("id", ids));
			list = c.list();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
		return list;
	}
	
	public void delete(Object object){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(object);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}  
	}
	
	protected void update(String hql){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	public void updateBean(Object o){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.update(o);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	protected void executeUpdateSql(String sql){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.createSQLQuery(sql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	public void executeUpdateHql(String hql){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
}
