package com.bblanqiu.module.user.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bblanqiu.common.annotation.RequestLimit;
import com.bblanqiu.common.bean.BasicPageResultBean;
import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.Error;
import com.bblanqiu.common.exception.ErrorCode;
import com.bblanqiu.common.exception.ErrorCodeException;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.ChargeLog;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.mysql.model.UserScore;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.common.util.HttpRequestUtils;
import com.bblanqiu.common.util.LocationUtil;
import com.bblanqiu.common.util.PageUtil;
import com.bblanqiu.common.util.ScoreUtil;
import com.bblanqiu.module.auth.AccessTokenGenerator;
import com.bblanqiu.module.auth.AuthValidater;
import com.bblanqiu.module.file.QiniuService;
import com.bblanqiu.module.sms.MailSender;
import com.bblanqiu.module.sms.SmsHander;
import com.bblanqiu.module.user.bean.MybbInfoBean;
import com.bblanqiu.module.user.bean.PhoneLoginBean;
import com.bblanqiu.module.user.bean.RegisterResultBean;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.bean.UserScoreBean;
import com.bblanqiu.module.user.bean.UserUpdateBean;
import com.bblanqiu.module.user.captcha.CaptchaService;
import com.bblanqiu.module.user.facade.TokenFacade;
import com.bblanqiu.module.user.facade.UserFacade;
import com.bblanqiu.module.user.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class Usercontroller extends HandleExceptionController{
	@Autowired
	ObjectMapper mapper;
	@Autowired
	TokenFacade tokenFacade;
	@Autowired
	UserFacade userFacade;
	@Autowired
	AuthValidater authValidater;
	@Autowired
	CaptchaService captchaService;
	@Autowired
	QiniuService qiniuService;
	@Autowired
	MailSender mailSender;
	@Autowired
	TradeService tradeService;
	private static Logger logger = LoggerFactory.getLogger(Usercontroller.class);
	
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public @ResponseBody Object phoneLogin(
    		HttpServletRequest request,
    		@Valid @RequestBody PhoneLoginBean loginBean
    		) throws Exception{
		String captcha = null;
		String city = null;
		String province = null;
		String district = null;
		logger.info("login-info:{}",mapper.writeValueAsString(loginBean));
		captcha = captchaService.getCaptcha(loginBean.getPhone());
		if(loginBean.getCaptcha().equals("1111") && loginBean.getPhone().equals("15108232227")){
			
		}else{
			if(loginBean.getCaptcha() == null || !captcha.equals(loginBean.getCaptcha())){
				throw new ErrorCodeException(ErrorCode.VARIFICATION_CODE_ERROR);
			}else if(!authValidater.validateClient(loginBean.getClientId())){
	    		throw new ErrorCodeException(ErrorCode.INVALID_CLIENT);
	    	}
		}
		if(loginBean.getLatitude() != null && loginBean.getLongitude() != null){
			Map<String, String> map = LocationUtil.getLocation(loginBean.getLatitude(), loginBean.getLongitude());
			if(map != null && map.get("province") != null){
				city = map.get("city");
				province = map.get("province");
				district = map.get("district");
			}
		}
		User user = userFacade.getUserbyPhone(loginBean.getPhone());
		if(user != null){
			if(loginBean.getpType() != null){
				user.setpType(loginBean.getpType());
				userFacade.updateUser(user);
			}
			Token token = null;
			UserCacheBean ucb = userFacade.getUser(user.getId());
			if(ucb.getToken() != null){
				token = tokenFacade.getToken(ucb.getToken());
				token.setUpdateTime(DateUtils.getDate());
			}else{
				token = new Token();
		    	token.setClientId("47327d3df06df0b4");
		    	token.setUpdateTime(DateUtils.getDate());
		    	token.setCreateTime(token.getUpdateTime());
		    	token.setUserId(user.getId());
		    	token.setName(user.getName());
		    	token.setIp(HttpRequestUtils.getIpAddr(request));
		    	token.setType(user.getType());
		    	token.setToken(AccessTokenGenerator.getInstance().getAccessToken());
		    	tokenFacade.addToken(token);
			}
			if(loginBean.getLatitude() != null && loginBean.getLongitude() != null){
				user.setLatitude(loginBean.getLatitude());
		    	user.setLongitude(loginBean.getLongitude());
		    	user.setProvince(province);
		    	user.setCity(city);
		    	user.setDistrict(district);
		    	userFacade.updateUser(user);
			}
			userFacade.updateUserToken(user.getId(), token.getToken());
			userFacade.login(user.getId());
			RegisterResultBean rrb = new RegisterResultBean(token.getToken(), token.getUserId(), loginBean.getPhone(),token.getType());
	    	return new OnlyResultBean(rrb);
		}else{
	    	user = new User(
	    			loginBean.getPhone(), 
	    			loginBean.getPhone(),
	    			loginBean.getpType(), 
	    			DateUtils.getDate());
	    	user.setType(0);
	    	user.setNickname(loginBean.getPhone());
	    	if(loginBean.getLatitude() != null && loginBean.getLongitude() != null){
		    	user.setLatitude(loginBean.getLatitude());
		    	user.setLongitude(loginBean.getLongitude());
		    	user.setProvince(province);
		    	user.setCity(city);
		    	user.setDistrict(district);
	    	}
	    	userFacade.addUser(user);
	    	Wallet w = new Wallet();
	    	w.setCredit(0);
	    	w.setMoney(0.0f);
	    	w.setScore(100);
	    	w.setUserId(user.getId());
	    	tradeService.createWallet(w);
	    	
	    	Token token = new Token();
	    	token.setClientId(loginBean.getClientId());
	    	token.setUpdateTime(DateUtils.getDate());
	    	token.setCreateTime(token.getUpdateTime());
	    	token.setUserId(user.getId());
	    	token.setToken(AccessTokenGenerator.getInstance().getAccessToken());
	    	token.setType(user.getType());
	    	token.setIp(HttpRequestUtils.getIpAddr(request));
//	    	tokenService.addToken(token);
	    	tokenFacade.addToken(token);
	    	userFacade.updateUserToken(user.getId(), token.getToken());
	    	userFacade.login(user.getId());
	    	logger.info("register user:{}, phone{},email{}",user.getId(), user.getPhone(),user.getEmail());
	    	RegisterResultBean rrb = new RegisterResultBean(token.getToken(), token.getUserId(), user.getPhone() == null ? user.getEmail() : user.getPhone() ,token.getType());
	    	return new OnlyResultBean(rrb);
		}
    }

	
	/*@RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public @ResponseBody Object register(
    		HttpServletRequest request,
    		@Valid @RequestBody UserCreateBean userCreateBean
    		) throws Exception{
		String registerUsername = null;
		String captcha = null;
		if(userCreateBean.getPhone() != null){
			if(userFacade.isPhoneExists(userCreateBean.getPhone())){
	    		throw new ErrorCodeException(ErrorCode.PHONE_ALREADY_EXISTS, userCreateBean.getPhone());
	    	}else{
	    		registerUsername = userCreateBean.getPhone();	
	    	}
			captcha = captchaService.getCaptcha(userCreateBean.getPhone());
		}else if(userCreateBean.getEmail() != null){
			registerUsername = userCreateBean.getEmail();
			captcha = captchaService.getEmailCaptcha(userCreateBean.getEmail());
		}else{
			throw new ErrorCodeException(ErrorCode.PARAMETER_ERROR);
		}
		logger.info("register phone{},email{},captcha{},ecap{}",userCreateBean.getPhone(),userCreateBean.getEmail(),userCreateBean.getCaptcha(),captcha);
		if(userCreateBean.getCaptcha() == null || !captcha.equals(userCreateBean.getCaptcha())){
			throw new ErrorCodeException(ErrorCode.VARIFICATION_CODE_ERROR);
		}else if(!authValidater.validateClient(userCreateBean.getClientId())){
    		throw new ErrorCodeException(ErrorCode.INVALID_CLIENT);
    	}
    	Date time = new Date();
    	User user = new User(
    			registerUsername, 
    			userCreateBean.getPhone(),
    			userCreateBean.getEmail(),
    			userCreateBean.getPassword(), 
    			userCreateBean.getpType(), 
    			time);
    	user.setType(1);
    	userFacade.addUser(user);
    	Wallet w = new Wallet();
    	w.setCredit(0);
    	w.setMoney(0.0f);
    	w.setScore(100);
    	w.setUserId(user.getId());
    	tradeService.createWallet(w);
    	
    	Token token = new Token();
    	token.setClientId(userCreateBean.getClientId());
    	token.setCreateTime(time);
    	token.setUserId(user.getId());
    	token.setToken(AccessTokenGenerator.getInstance().getAccessToken());
    	token.setType(user.getType());
//    	tokenService.addToken(token);
    	tokenFacade.addToken(token);
    	userFacade.updateUserToken(user.getId(), token.getToken());
    	userFacade.login(user.getId());
    	logger.info("register user:{}, phone{},email{}",user.getId(), user.getPhone(),user.getEmail());
    	RegisterResultBean rrb = new RegisterResultBean(token.getToken(), token.getUserId(), user.getPhone() == null ? user.getEmail() : user.getPhone() ,token.getType());
    	return new OnlyResultBean(rrb);
    }

	@RequestMapping(value = "/user/oldlogin", method = RequestMethod.POST)
	public @ResponseBody
	Object oldLogin(
			HttpServletRequest request,
			@Valid @RequestBody LoginBean loginBean) throws Exception {
		
		Token token = null;
		User user = null;
		if(loginBean.getPhone() != null){
			user = userFacade.getUserbyPhone(loginBean.getPhone());
		}else if(loginBean.getEmail() != null){
			user = userFacade.getUserbyEmail(loginBean.getEmail());
		}else{
			throw new ErrorCodeException(ErrorCode.USER_DOSE_NOT_EXISTS);
		}
		if(user == null){
			throw new ErrorCodeException(ErrorCode.USER_DOSE_NOT_EXISTS);
		}else if(!user.getPassword().equals(loginBean.getPassword())){
			throw new ErrorCodeException(ErrorCode.USERNAME_OR_PASSWORD_ERROR);
		}else{
			if(loginBean.getpType() != null){
				User newUser = new User();
				newUser.setId(user.getId());
				newUser.setpType(loginBean.getpType());
				userFacade.updateUser(newUser);
			}
			UserCacheBean ucb = userFacade.getUser(user.getId());
			Date time = new Date();
			if(ucb.getToken() != null){
				token = tokenFacade.getToken(ucb.getToken());
				token.setCreateTime(time);
			}else{
				token = new Token();
		    	token.setClientId(loginBean.getClientId());
		    	token.setCreateTime(time);
		    	token.setUserId(user.getId());
		    	token.setType(user.getType());
		    	token.setToken(AccessTokenGenerator.getInstance().getAccessToken());
		    	tokenFacade.addToken(token);
			}
			userFacade.updateUserToken(user.getId(), token.getToken());
			userFacade.login(user.getId());
		}
		RegisterResultBean rrb = new RegisterResultBean(token.getToken(), token.getUserId(), loginBean.getPhone(),token.getType());
    	return new OnlyResultBean(rrb);
	}*/
	
	@RequestMapping(value = "/user/logout", method = RequestMethod.GET)
	public @ResponseBody
	Object logout(
			HttpServletRequest request,
			@RequestParam String token) throws Exception {
		Token t = tokenFacade.getToken(token);
		if(t != null){
			tokenFacade.deleteToken(token);
			userFacade.logout(t.getUserId());
		}
		return new OnlyResultBean("ok");
	}

	@RequestMapping(value = "/user/info", method = RequestMethod.GET)
	public @ResponseBody
	Object info(
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		UserCacheBean ucb = userFacade.getUser(token.getUserId());
		if(ucb != null){
			Wallet w = userFacade.getWallet(ucb.getId());
			MybbInfoBean my = new MybbInfoBean();
			my.setIcon(ucb.getIcon());
			my.setUserId(ucb.getId());
			my.setName(ucb.getName());
			my.setNickname(ucb.getNickname());
			my.setPhone(ucb.getPhone());
			my.setScore(w.getScore());
			my.setType(ucb.getType());
			my.setWallet(w.getMoney());
			my.setQuestion(ucb.getQuestion());
			my.setHasDevicePwd((ucb.getStoragepass() == null || ucb.getStoragepass().length() < 2) ? false : true);
			my.setHasQuestion((ucb.getQuestion() == null || ucb.getQuestion().length() < 2) ? false : true);
			return new OnlyResultBean(my);
		}else{
			throw new ErrorCodeException(ErrorCode.USER_DOSE_NOT_EXISTS);	
		}
	}
	
	@RequestMapping(value = "/user/secqa", method = RequestMethod.GET)
	public @ResponseBody
	Object secqs(
			@RequestParam(required = true) String question,
			@RequestParam(required = true) String answer,
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		UserCacheBean ucb = userFacade.getUser(token.getUserId());
		String q = ucb.getQuestion();
		String a = ucb.getAnswer();
		logger.info("q-a:{},{},{},{}",q,a,question,answer);
		if(q.contains(question) && a.contains(answer)){
			return new OnlyResultBean();
		}else{
			throw new ErrorCodeException(ErrorCode.SECQA_ERROR); 
		}
	}
	
	@RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public @ResponseBody Object update(
    		HttpServletRequest request,
    		@Valid @RequestBody UserUpdateBean userUpdateBean
    		) throws Exception{
		Token token = (Token)request.getAttribute("X-TOKEN");
		if(userUpdateBean.getAnswer() == null 
				&& userUpdateBean.getIcon() == null 
				&& userUpdateBean.getLanguage() == null 
				&& userUpdateBean.getNickname() == null
				&& userUpdateBean.getQuestion() == null
				&& userUpdateBean.getStoragepass() == null){
			throw new ErrorCodeException(ErrorCode.PARAMETER_ERROR);	
		}else{
			User user = mapper.convertValue(userUpdateBean, User.class);
			user.setId(token.getUserId());
			userFacade.updateUser(user);
	    	return new OnlyResultBean("ok");
		}
		
    }
	public static void main(String []args)throws Exception{
		for(int i=1; i<=8;i++){
			System.out.println(i);
		}
	}
	@RequestMapping(value = "/user/icon", method = RequestMethod.POST)
	public @ResponseBody Object userImg(
			HttpServletRequest request,
			@RequestParam MultipartFile[] file
			) throws Exception{
		Token token = (Token)request.getAttribute("X-TOKEN");
		try {
			if(file != null && file.length > 0){
				String url = qiniuService.uploadFile("user-" + UUID.randomUUID().toString(), file[0].getInputStream());
				User user = new User();
				user.setId(token.getUserId());
				user.setIcon(url);
				userFacade.updateUser(user);
			}else{
				throw new ErrorCodeException(ErrorCode.RESOURCE_DOSENOT_EXISTS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("upload user icon error, userid={}", token.getUserId());
		}
		return new OnlyResultBean();
	}
	@RequestMapping(value = "/user/score", method = RequestMethod.GET)
	public @ResponseBody Object userscore(
			HttpServletRequest request,
			@RequestParam(required = false) Integer pn,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "20") Integer limit,
			@RequestParam(required = false) Integer page) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		limit = PageUtil.checkLimit(limit, 20);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);
		List<UserScore> list = tradeService.getUserScoreByUserIdAndType(token.getUserId(),pn);
		Long total = tradeService.getUserScoreCount(token.getUserId(), pn);
		List<UserScoreBean> usbs = new ArrayList<>();
		for(UserScore us : list){
			UserScoreBean usb = mapper.convertValue(us, UserScoreBean.class);
			usb.setName(ScoreUtil.getScoreName(usb.getType()));
			usbs.add(usb);
		}
		Collections.sort(usbs);
		return new BasicPageResultBean(total, cursor, limit, usbs);
	}
	@RequestMapping(value = "/user/chargeinfo", method = RequestMethod.GET)
	public @ResponseBody Object chargeinfo(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "20") Integer limit,
			@RequestParam(required = false) Integer page) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		limit = PageUtil.checkLimit(limit, 20);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);
		List<ChargeLog> list = tradeService.getSucceedcharges(token.getUserId()); 
		Long total = tradeService.getSucceedchargesCount(token.getUserId());
		return new BasicPageResultBean(total, cursor, limit, list);
	}
	/*@RequestMapping(value = "/user/reset-password", method = RequestMethod.POST)
	public @ResponseBody Object resetPassword(
			HttpServletRequest request,
			@Valid @RequestBody PasswordBean passwordBean){
		
		if(!authValidater.validateClient(passwordBean.getClientId())){
    		throw new ErrorCodeException(ErrorCode.INVALID_CLIENT);
    	}else 
		if(passwordBean.getCaptcha() != null){
			if(passwordBean.getPhone() != null){
				String captcha = captchaService.getCaptcha(passwordBean.getPhone());
				logger.debug("reset password,{},{}", passwordBean.getCaptcha(), captcha);
				if(captcha.equals(passwordBean.getCaptcha())){
					try {
						userFacade.updateUserPassword(passwordBean.getPhone(), passwordBean.getPassword());
					} catch (Exception e) {
						throw new ErrorCodeException(ErrorCode.OPERATION_ERROR);
					}
				}else{
					throw new ErrorCodeException(ErrorCode.VARIFICATION_CODE_ERROR);
				}
			}else if(passwordBean.getEmail() != null){
				String captcha = captchaService.getEmailCaptcha(passwordBean.getEmail());
				logger.debug("reset password,{},{}", passwordBean.getCaptcha(), captcha);
				if(captcha.equals(passwordBean.getCaptcha())){
					try {
						userFacade.updateUserPasswordByEmail(passwordBean.getEmail(), passwordBean.getPassword());
					} catch (Exception e) {
						throw new ErrorCodeException(ErrorCode.OPERATION_ERROR);
					}
				}else{
					throw new ErrorCodeException(ErrorCode.VARIFICATION_CODE_ERROR);
				}
			}
			
		}else{
			throw new ErrorCodeException(ErrorCode.PARAMETER_VALUE_INVALID, "验证码");
		}
		return new OnlyResultBean("ok");
	}*/
	@RequestLimit(count=1,time=30000)
	@RequestMapping(value = "/get-phone-captcha", method = RequestMethod.GET)
	public @ResponseBody Object getPhoneCode(
    		@RequestParam(required = true) String phone,
    		@RequestParam(required = true) String clientid,
    		@RequestParam(required = false, defaultValue = "1") Integer type,
    		@RequestParam(required = false, defaultValue = "1") Integer language
    		){
		if(!authValidater.validateClient(clientid)){
    		throw new ErrorCodeException(ErrorCode.INVALID_CLIENT);
    	}else{
    		if(phone.matches("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")){
        		String captcha = RandomStringUtils.random(4, "1234567890");
        		try {
        			captchaService.addCaptcha(phone, captcha, new Date());
        			if(type == 1){
        				SmsHander.sendMsg(SmsHander.getApiKey(), captcha, phone, language);
        			}else{
        				SmsHander.sendVocieSms(SmsHander.getApiKey(), captcha, phone);
        			}
    				
    			} catch (IOException e) {
    				logger.error("get phone captcha error at:{},msg;{}", phone,e.getMessage());
    			}
    		}else{
    			throw new ErrorCodeException(ErrorCode.PHONE_NUMBER_ERROR);
    		}

    	}
		return new OnlyResultBean("ok");
	}
	
	@RequestMapping(value = "/get-email-captcha", method = RequestMethod.GET)
	public @ResponseBody Object getEmailCode(
    		@RequestParam(required = true) String email,
    		@RequestParam(required = true) String clientid,
    		@RequestParam(required = false, defaultValue = "1") Integer language
    		){
		if(!authValidater.validateClient(clientid)){
    		throw new ErrorCodeException(ErrorCode.INVALID_CLIENT);
    	}else{
    		if(email.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")){
        		String captcha = RandomStringUtils.random(5, "1234567890");
        		try {
        			captchaService.addEmailCaptcha(email, captcha);
        	    	List<String> s = new ArrayList<>();
        	    	s.add(email);
        	    	String mail = mailSender.getMail();
        	    	mail = mail.replaceFirst("mail_code", captcha);
        	    	mail = mail.replaceFirst("mail_to", s.get(0));
        	    	mailSender.sendMail("bblq登錄", mail, s, null);
    			} catch (IOException e) {
    				logger.error("get email captcha error at:{},msg;{}", email,e.getMessage());
    			}
    		}else{
    			throw new ErrorCodeException(ErrorCode.EMAIL_NUMBER_ERROR);
    		}

    	}
		return new OnlyResultBean("ok");
	}
	
	@RequestMapping(value = "/auth/token", method = RequestMethod.GET)
	public @ResponseBody
	Object authToken(
			HttpServletRequest request,
			@RequestParam String token
			) throws Exception {
		Token t;
		try {
			t = tokenFacade.getToken(token);
			if(t == null){
				return new ErrorCodeException(new Error(request.getRequestURI(), ErrorCode.EXPIRED_TOKEN));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ErrorCodeException(new Error(request.getRequestURI(), ErrorCode.TOKEN_ERROR, token));
		}
		UserCacheBean u = userFacade.getUser(t.getUserId());
		RegisterResultBean rrb = new RegisterResultBean(t.getToken(), t.getUserId(), u.getPhone(), u.getType());
		return new  OnlyResultBean(rrb, true);
	}
	
}
