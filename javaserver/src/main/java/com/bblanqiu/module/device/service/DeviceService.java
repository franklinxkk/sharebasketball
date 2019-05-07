package com.bblanqiu.module.device.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.BasketBall;
import com.bblanqiu.common.mysql.model.BeforeOpenDevice;
import com.bblanqiu.common.mysql.model.Court;
import com.bblanqiu.common.mysql.model.Device;
import com.bblanqiu.common.mysql.model.DeviceData;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.common.util.MysqlHqlUtil;

@Service
public class DeviceService extends MysqlBasicService{
	public void createDevice(Device device){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(device);
			for(int i=1; i<=8;i++){
				StorageBox box = new StorageBox();
				box.setDeviceSn(device.getSn());
				box.setNum(i);
				box.setState(0);
				session.save(box);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void createDeviceData(DeviceData dd) throws Exception{
		add(dd);
	}
	public void updateDeviceBySN(Device device){
		device.setUpdateTime(DateUtils.getDate());
		updateDevice(device, "sn");
	}
	public void updateDeviceById(Device device){
		device.setUpdateTime(DateUtils.getDate());
		updateDevice(device, "id");
	}
	public void updateDevice(Device device, String set){
		String hql = MysqlHqlUtil.getSingleUpdateHql(device, set);
		update(hql);
	}
	public void deleteDevice(Integer id){
		deleteById(Device.class.getName(), id);
	}

	public Device getDeviceById(Integer id){
		return getById(Device.class.getName(), id);
	}
	public Device getDeviceBySN(String sn)throws Exception{
		String deviceQueryHql = " from " + Device.class.getName() + " where sn='" + sn + "'";
		return get(deviceQueryHql);
	}
	public boolean isDeviceExist(String sn){
		String hql = "select count(*) from "+Device.class.getName()+" c where c.sn='" + sn + "'";
		return isValueExists(hql);
	}
	public long getDeviceCount(){
		return getTotalCount(Device.class.getName());
	}
	public List<Device> getDevices(int cursor, int limit) throws Exception{
		return gets(" from " + Device.class.getName() +" order by id desc", cursor, limit);
	}
	public long getBasketballCount(){
		return getTotalCount(BasketBall.class.getName());
	}
	public void createBasketball(BasketBall basketBall) throws Exception{
		add(basketBall);
	}
	public void createBasketballs(List<BasketBall> list){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			for(BasketBall ball:list){
				session.save(ball);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void updateBasketballBySN(BasketBall basketBall){
		updateBasketball(basketBall, "sn");
	}
	public void updateBasketballById(BasketBall basketBall){
		updateBasketball(basketBall, "id");
	}
	public void updateBasketball(BasketBall basketBall, String set){
		String hql = MysqlHqlUtil.getSingleUpdateHql(basketBall, set);
		update(hql);
	}
	public void deleteBasketball(Integer id){
		deleteById(BasketBall.class.getName(), id);
	}

	public BasketBall getBasketballById(Integer id){
		return getById(BasketBall.class.getName(), id);
	}
	public BasketBall getBasketballBySn(String sn) throws Exception{
		String deviceQueryHql = " from " + BasketBall.class.getName() 
				+ " where sn='" + sn+"'";
		return get(deviceQueryHql);
	}
	public void borrowBasketball(String sn){
		BasketBall bb = new BasketBall();
		bb.setSn(sn);bb.setState(1);
		updateBasketball(bb, "sn");
	}
	public void returnBasketball(String sn){
		BasketBall bb = new BasketBall();
		bb.setSn(sn);bb.setState(0);
		updateBasketball(bb, "sn");
	}
	public boolean isBasketballExist(String sn){
		String hql = "select count(*) from "+BasketBall.class.getName()+" c where c.sn='" + sn + "'";
		return isValueExists(hql);
	}

	public void createStoragebox(StorageBox storageBox) throws Exception{
		add(storageBox);
	}

	public void createStoragebox(String sn){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			for(int i=1; i<=8;i++){
				StorageBox box = new StorageBox();
				box.setDeviceSn(sn);
				box.setNum(i);
				box.setState(0);
				session.save(box);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} 
	}
	public void updateStorageboxById(StorageBox storageBox){
		updateStoragebox(storageBox, "id");
	}
	public void updateStoragebox(StorageBox storageBox, String set){
		String hql = MysqlHqlUtil.getSingleUpdateHql(storageBox, set);
		update(hql);
	}
	public void deleteStoragebox(Integer id){
		deleteById(StorageBox.class.getName(), id);
	}

	public StorageBox getStorageboxById(Integer id){
		return getById(StorageBox.class.getName(), id);
	}
	public StorageBox getStorageboxByDeviceSnAndNum(String sn,Integer num) throws Exception{
		String deviceQueryHql = " from " + StorageBox.class.getName() 
				+ " where deviceSn='" + sn+"' and num="+num;
		return get(deviceQueryHql);
	}
	public boolean isStorageboxExist(String sn){
		String hql = "select count(*) from "+StorageBox.class.getName()+" c where c.sn='" + sn + "'";
		return isValueExists(hql);
	}
	public StorageBox getFreeStoragebox(String sn) throws Exception{
		String hql = "from "+StorageBox.class.getName()+" where deviceSn='"+sn+"' and state=0";
		List<StorageBox> list = gets(hql);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public List<StorageBox> getFreeStorageboxs(String sn) throws Exception{
		String hql = "from "+StorageBox.class.getName()+" where deviceSn='"+sn+"' and state=0";
		return gets(hql);
	}
	public long getFreeStorageBoxCount(String sn)throws Exception{
		String hql="select count(*) from " + StorageBox.class.getName() + " where deviceSn='"+sn+"' and state=0 ";
		return getTotalCountByHql(hql);
	}
	public void createCourt(Court c) throws Exception{
		add(c);
	}
	public Court getCourtById(Integer id){
		return getById(Court.class.getName(), id);
	}
	public List<Court> getCourts(String province, String city, String district,Integer type,Integer cursor,Integer limit) throws Exception{
		String hql = "from "+Court.class.getName()+" where 1=1 ";
		if(province != null){
			hql+=" and province='"+province+"' ";
		}
		if(city != null){
			hql+=" and city='"+city+"' ";
		}
		if(district != null){
			hql+=" and district='"+district+"' ";
		}
		if(type != null){
			hql+=" and type="+type;
		}
		hql += " order by createTime desc ";
		return gets(hql, cursor, limit);
	}

	public long getCourtCount(String province, String city, String district,Integer type)throws Exception{
		String hql="select count(*) from " + Court.class.getName() + " where 1=1 ";
		if(province != null){
			hql+=" and province='"+province+"' ";
		}
		if(city != null){
			hql+=" and city='"+city+"' ";
		}
		if(district != null){
			hql+=" and district='"+district+"' ";
		}
		if(type != null){
			hql+=" and type="+type;
		}
		return getTotalCountByHql(hql);
	}
	public void createBeforeOpenDevice(Integer userId, String sn, String ballSn, Integer num,String address)throws Exception{
		BeforeOpenDevice bod = new BeforeOpenDevice();
		bod.setUserId(userId);
		bod.setBallSn(ballSn);
		bod.setCreateTime(DateUtils.getDate());
		bod.setNum(num);bod.setSn(sn);
		bod.setAddress(address);
		add(bod);
	}
	public BeforeOpenDevice getBeforeOpenDevice(String sn)throws Exception{
		String deviceQueryHql = " from " + BeforeOpenDevice.class.getName() + " where sn='" + sn + "'";
		return get(deviceQueryHql);
	}
	public BeforeOpenDevice getBeforeOpenDevices(String sn)throws Exception{
		String deviceQueryHql = " from " + BeforeOpenDevice.class.getName() + " where sn='" + sn + "'";
		List<BeforeOpenDevice> list = gets(deviceQueryHql);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public BeforeOpenDevice getBeforeOpenDevice(String sn,String ballSn)throws Exception{
		String deviceQueryHql = " from " + BeforeOpenDevice.class.getName() + " where sn='" + sn+"' and ballSn='"+ballSn+"'";
		return get(deviceQueryHql);
	}
	public BeforeOpenDevice getBeforeOpenDevice(String sn,Integer num)throws Exception{
		String deviceQueryHql = " from " + BeforeOpenDevice.class.getName() + " where sn='" + sn+"' and num="+num;
		return get(deviceQueryHql);
	}
	public void deleteBeforeOpenDevice(String sn,String ballSn){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from " + BeforeOpenDevice.class.getName() 
					+ "  where sn='" + sn+"' and ballSn='"+ballSn+"'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	public void deleteBeforeOpenDevice(String sn,Integer num){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from " + BeforeOpenDevice.class.getName() 
					+ "  where sn='" + sn+"' and num="+num;
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	public void deleteBeforeOpenDevice(String sn){
		Session session = sessionFacotry.getCurrentSession();
		try {
			session.beginTransaction();
			String hql = "delete from " + BeforeOpenDevice.class.getName() 
					+ "  where sn='" + sn+"'";
			session.createQuery(hql).executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		}
	}
}
