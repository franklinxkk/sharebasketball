package com.bblanqiu.module.user.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.CacheTableNames;
import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.admin.bean.UserAllBean;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.service.TokenService;
import com.bblanqiu.module.user.service.TradeService;
import com.bblanqiu.module.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserFacade extends RedisService{
	@Autowired
	TokenService tokenService;
	@Autowired
	UserService userService;
	@Autowired
	TradeService tradeService;
	@Autowired
	ObjectMapper mapper;

	/**
	 * 添加用户以及用户缓存
	 * @param user
	 * @throws Exception
	 */
	public void addUser(User user) throws Exception{
		userService.addUser(user);
		UserCacheBean ucb = mapper.convertValue(user, UserCacheBean.class);
		addValue(CacheTableNames.PREFIX_USER + user.getId(), mapper.writeValueAsString(ucb), null);
	}

	/**
	 * 更新用户信息 以及缓存
	 * @param user
	 * @throws Exception
	 */
	public void updateUser(User user) throws Exception{
		userService.updateUser(user);
		User userinfo = userService.getUser(user.getId());
		updateUserCache(userinfo);
	}

	/**
	 * 获取用户信息
	 * @param userId
	 * @return 用户信息
	 * @throws Exception
	 */
	public UserCacheBean getUser(Integer userId) throws Exception{
		UserCacheBean ucb = null;
		Object o = getValue(CacheTableNames.PREFIX_USER + userId);
		if(o != null){
			ucb = mapper.readValue(o.toString(), UserCacheBean.class);
		}else{
			User user = userService.getUser(userId);
			if(user != null){
				ucb = mapper.convertValue(user, UserCacheBean.class);
				addValue(CacheTableNames.PREFIX_USER + user.getId(), mapper.writeValueAsString(ucb), null);
			}
		}
		return ucb;
	}

	/**
	 * 更新用户表缓存
	 * @param user
	 * @throws Exception
	 */
	private void updateUserCache(User user) throws Exception{
		UserCacheBean ucb = mapper.convertValue(user, UserCacheBean.class);
		Token t = tokenService.getToken(user.getId());
		if(t != null){
			ucb.setToken(t.getToken());
		}
		addValue(CacheTableNames.PREFIX_USER + user.getId(), mapper.writeValueAsString(ucb), null);
	}

	/**
	 * 更新用户表缓存中的令牌
	 * @param userId
	 * @param token
	 * @throws Exception
	 */
	public void updateUserToken(Integer userId, String token)throws Exception{
		UserCacheBean ucb = getUser(userId);
		ucb.setToken(token);
		addValue(CacheTableNames.PREFIX_USER + userId, mapper.writeValueAsString(ucb), null);
	}

	public boolean isPhoneExists(String phone){
		return userService.isPhoneExists(phone);
	}

	public boolean validatePhoneAndPassword(String phone, String password){
		User user = getUserbyPhone(phone);
		return user.getPassword().equals(password);
	}

	public User getUserbyPhone(String phone){
		return userService.getUserbyPhone(phone);
	}
	public User getUserbyEmail(String email){
		return userService.getUserbyEmail(email);
	}

	/**
	 * 登出 
	 * @param userId
	 * @throws Exception
	 */
	public void logout(Integer userId) throws Exception{
		UserCacheBean ucb = getUser(userId);
		ucb.setIsLogin(false);
		ucb.setToken(null);
		addValue(CacheTableNames.PREFIX_USER + userId, mapper.writeValueAsString(ucb), null);
	}
	public void login(Integer userId) throws Exception{
		UserCacheBean ucb = getUser(userId);
		ucb.setIsLogin(true);
		ucb.setLoginTime(DateUtils.getDate());
		addValue(CacheTableNames.PREFIX_USER + userId, mapper.writeValueAsString(ucb), null);
	}

	public void updateUserPassword(String phone, String password) throws Exception{
		userService.updateUserPassword(phone, password);
		User user = userService.getUserbyPhone(phone);
		updateUserCache(user);
	}
	public void updateUserType(Integer userId, Integer type)throws Exception{
		userService.updateUserType(userId, type);
		User user = userService.getUser(userId);
		updateUserCache(user);
	}
	public void updateUserTypeAndName(Integer userId, Integer type,String name)throws Exception{
		userService.updateUserTypeAndName(userId, type, name);
		User user = userService.getUser(userId);
		updateUserCache(user);
	}
	public void updateUserPasswordByEmail(String email, String password) throws Exception{
		userService.updateUserPasswordByEmail(email, password);
		User user = userService.getUserbyEmail(email);
		updateUserCache(user);
	}

	public List<User> getUserList(Integer cursor, Integer limit) throws Exception{
		return userService.getUsers(cursor, limit);
	}
	public List<UserAllBean> getAllUserList(Integer cursor, Integer limit) throws Exception{
		return userService.getAllUsers(cursor, limit);
	}
	public long getUserCount() throws Exception{
		return userService.getUsersCount();
	}
	public Wallet getWallet(Integer userId){
		return tradeService.getWallet(userId);
	}
	public void addWallet(Wallet w) throws Exception{
		tradeService.createWallet(w);
	}
	public void updateWallet(Wallet w){
		tradeService.updateWallet(w);
	}
}
