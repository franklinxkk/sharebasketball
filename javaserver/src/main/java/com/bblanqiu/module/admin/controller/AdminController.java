package com.bblanqiu.module.admin.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bblanqiu.amqp.AmqpMessageSender;
import com.bblanqiu.common.bean.BasicPageResultBean;
import com.bblanqiu.common.mysql.model.AlarmOpM;
import com.bblanqiu.common.mysql.model.BasketBall;
import com.bblanqiu.common.mysql.model.Court;
import com.bblanqiu.common.mysql.model.Device;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.mysql.model.UserScore;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.CostUtil;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.common.util.DigestUtils;
import com.bblanqiu.common.util.HttpRequestUtils;
import com.bblanqiu.common.util.PageUtil;
import com.bblanqiu.common.util.ScoreUtil;
import com.bblanqiu.common.util.ValueFormatUtils;
import com.bblanqiu.module.admin.bean.ChargeLogBean;
import com.bblanqiu.module.admin.bean.DeviceBean;
import com.bblanqiu.module.admin.bean.UserAllBean;
import com.bblanqiu.module.admin.bean.UserCostInfoBean;
import com.bblanqiu.module.admin.facade.AdminFacade;
import com.bblanqiu.module.alarm.service.AlarmService;
import com.bblanqiu.module.auth.AccessTokenGenerator;
import com.bblanqiu.module.device.service.DeviceService;
import com.bblanqiu.module.file.QiniuService;
import com.bblanqiu.module.mqtt.almq.MQTTSendMsg;
import com.bblanqiu.module.mqtt.converter.DataConverter;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.facade.TokenFacade;
import com.bblanqiu.module.user.facade.UserFacade;
import com.bblanqiu.module.user.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AdminController {
	@Autowired
	UserFacade userFacade;
	@Autowired
	TokenFacade tokenFacade;
	@Autowired
	AdminFacade adminFacade;
	@Autowired
	DeviceService deviceService;
	@Autowired
	TradeService tradeService;
	@Autowired
	MQTTSendMsg mqttSendMsg;
	@Autowired
	AlarmService alarmService;
	@Autowired
	QiniuService qiniuService;
	@Autowired
	AmqpMessageSender amqpMessageSender;
	@Autowired
	ObjectMapper mapper;
	@RequestMapping(value = "/admin")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("welcome");
		return mav;
	}
	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public ModelAndView loginerror(
			HttpServletRequest request
			) throws Exception{
		ModelAndView mav = new ModelAndView("welcome");
		return mav;
	}
	@RequestMapping(value = "/admin/login", method = RequestMethod.POST)
	public ModelAndView login(
			HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String password
			) throws Exception{
		ModelAndView errormav = new ModelAndView("welcome");
		User user = userFacade.getUserbyPhone(name);
		if(user == null){
			errormav.addObject("errorinfo", "<script language=\"javascript\">alert('用户不存在！');</script>");
		}else if(user.getType() == 1){
			errormav.addObject("errorinfo", "<script language=\"javascript\">alert('权限不够！');</script>");
		}else if(user.getPassword().equals(password)){
			if(user.getType() == 100){
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
			    	token.setName(name);
			    	token.setIp(HttpRequestUtils.getIpAddr(request));
			    	token.setType(user.getType());
			    	token.setToken(AccessTokenGenerator.getInstance().getAccessToken());
			    	tokenFacade.addToken(token);
				}
				userFacade.updateUserToken(user.getId(), token.getToken());
				userFacade.login(user.getId());
				
				HttpSession hs = request.getSession(true);
				hs.setMaxInactiveInterval(7200);
				hs.setAttribute("token", token);
				
				ModelAndView mav = new ModelAndView("index");
				mav.addObject("navNum", 1);
				long totalDevice = adminFacade.getAllDevice();
				long totalUser = adminFacade.getAlluser();
				Double totalBall = adminFacade.getSumInball();
				Double totalBox = adminFacade.getSumInbox();
				
				mav.addObject("totalDevice", totalDevice);
				mav.addObject("totalUser", totalUser);
				mav.addObject("totalBall", totalBall);
				mav.addObject("navNum", 1);
				mav.addObject("totalBox", totalBox);
				return mav;
			}else{
				errormav.addObject("errorinfo", "<script language=\"javascript\">alert('权限不够！');</script>");
			}
		}else{
			errormav.addObject("errorinfo", "<script language=\"javascript\">alert('用户名密码错误！');</script>");
		}
		return errormav;
	}
	@RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
	public ModelAndView loginout(HttpSession session) {
		session.removeAttribute("token");
		session.invalidate();
		ModelAndView mav = new ModelAndView("welcome");
		mav.addObject("info", "退出登录");
		return mav;
	}
	@RequestMapping(value = "/admin/getstatistic", method = RequestMethod.GET)
	public ModelAndView getstatistic(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		long totalDevice = adminFacade.getAllDevice();
		long totalUser = adminFacade.getAlluser();
		Double totalBall = adminFacade.getSumInball();
		Double totalBox = adminFacade.getSumInbox();
		
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("totalDevice", totalDevice);
		mav.addObject("totalUser", totalUser);
		mav.addObject("totalBall", totalBall);
		mav.addObject("navNum", 1);
		mav.addObject("totalBox", totalBox);
		return mav;
	}
	@RequestMapping(value = "/admin/getusers", method = RequestMethod.GET)
	public ModelAndView getusers(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<UserAllBean> list = userFacade.getAllUserList(cursor, limit);
		Long total = userFacade.getUserCount();
		ModelAndView mav = new ModelAndView("listusers");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 2);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/getdevices", method = RequestMethod.GET)
	public ModelAndView getdevices(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<Device> list = deviceService.getDevices(cursor, limit);
		List<DeviceBean> dbs = new ArrayList<>();
		for(Device d : list){
			DeviceBean bb = mapper.convertValue(d, DeviceBean.class);
			bb.setSumTrade(adminFacade.getSumInSn(bb.getSn()));
			if(d.getLockState() != null){
				ValueFormatUtils.parseLocks(d.getLockState().toCharArray(), bb);
			}
			dbs.add(bb);
		}
		Long total = deviceService.getDeviceCount();
		ModelAndView mav = new ModelAndView("listdevices");
		mav.addObject("list", dbs);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 3);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/gisdevices", method = RequestMethod.GET)
	public ModelAndView gisdevices(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "500") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		ModelAndView mav = new ModelAndView("gisdevices");
		mav.addObject("navNum", 3);
		Token t = (Token)request.getSession().getAttribute("token");		
		mav.addObject("accesstoken", t.getToken());
		return mav;
	}
	@RequestMapping(value = "/admin/getgisdevices", method = RequestMethod.GET)
	public @ResponseBody Object getgisdevices(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "500") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 1000);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<Device> list = deviceService.getDevices(cursor, limit);
		List<Object> gispoints = new ArrayList<>();
		for(Device d : list){
			if(d.getLatitude() != null && d.getLongitude() != null){
				List<Object> point = new ArrayList<>();
				point.add(d.getLatitude());point.add(d.getLongitude());point.add(d.getCourtName());
				gispoints.add(point);
			}
		}
		Long total = deviceService.getDeviceCount();
		
		return new BasicPageResultBean(total, cursor, limit, gispoints);
	}
	@RequestMapping(value = "/admin/device/init", method = RequestMethod.GET)
	public ModelAndView deviceInit(
			HttpServletRequest request,
			@RequestParam String sn
			) throws Exception{
		mqttSendMsg.sendMsg(sn, DataConverter.warpDeviceInitCmd(sn));
		return getdevices(request, 0, 20, 1);
	}
	@RequestMapping(value = "/admin/user/charge", method = RequestMethod.GET)
	public ModelAndView usercharge(
			HttpServletRequest request,
			@RequestParam Integer userid
			) throws Exception{
		Wallet w = userFacade.getWallet(userid);
		w.setMoney(w.getMoney()+10);
		userFacade.updateWallet(w);
		return getusers(request, 0, 20, 1);
	}
	@RequestMapping(value = "/admin/device/openballbox", method = RequestMethod.GET)
	public ModelAndView openballbox(
			HttpServletRequest request,
			@RequestParam String sn
			) throws Exception{
//		Device device = deviceService.getDeviceBySN(sn);
//		BasketBall bb = new BasketBall();
//		bb.setSn(device.getBallSn());
//		bb.setState(1);
//		deviceService.updateBasketballBySN(bb);
		mqttSendMsg.sendMsg(sn, DataConverter.warpOpenBallBox(sn));
		return getdevices(request, 0, 20, 1);
	}
	@RequestMapping(value = "/admin/device/closeball", method = RequestMethod.GET)
	public ModelAndView closeball(
			HttpServletRequest request,
			@RequestParam String sn
			) throws Exception{
		Device device = deviceService.getDeviceBySN(sn);
		UserCost uc = tradeService.getCurrentUserBallCost(sn);
		if(uc != null){
			uc.setEndTime(DateUtils.getDate());
			uc.setState(1);

			Wallet w = tradeService.getWallet(uc.getUserId());//0.5元 30分钟
			int minute = DateUtils.getTimeGapByMinute(uc.getStartTime(), uc.getEndTime());
			uc.setConsume(CostUtil.getCost(minute));
			w.setMoney(w.getMoney() - uc.getConsume());
			w.setScore(w.getScore() + 1);
			BasketBall bb = new BasketBall();
			bb.setSn(device.getBallSn());
			bb.setState(0);

			tradeService.closeBasketball(uc, bb, w);
		}

		return getdevices(request, 0, 20, 1);
	}
	@RequestMapping(value = "/admin/device/openstorage", method = RequestMethod.GET)
	public ModelAndView openstorage(
			HttpServletRequest request,
			@RequestParam String sn,
			@RequestParam Integer num
			) throws Exception{
//		StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(sn, num);
//		sb.setState(1);
//		deviceService.updateStorageboxById(sb);
		mqttSendMsg.sendMsg(sn, DataConverter.warpOpenStorageBox(sn, num));
		return getdevices(request, 0, 20, 1);
	}
	@RequestMapping(value = "/admin/device/ack", method = RequestMethod.GET)
	public ModelAndView ack(
			HttpServletRequest request,
			@RequestParam String sn,
			@RequestParam Integer num
			) throws Exception{
		if(num == 0){
			mqttSendMsg.sendMsgToDataChannle(sn, DataConverter.warpUseBallAck(sn, sn));
		}else if(num <= 1 && num <=8){
			mqttSendMsg.sendMsgToDataChannle(sn, DataConverter.warpUseBoxAck(sn, num));
		}
		
		return getdevices(request, 0, 20, 1);
	}
/*	@RequestMapping(value = "/admin/device/closestorage", method = RequestMethod.GET)
	public ModelAndView closestorage(
			HttpServletRequest request,
			@RequestParam String sn,
			@RequestParam Integer num
			) throws Exception{
		StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(sn, num);
		sb.setState(0);
		deviceService.updateStorageboxById(sb);
		mqttSendMsg.sendMsg(sn, DataConverter.warpOpenStorageBox(sn, num));
		return getdevices(request, 0, 20, 1);
	}*/
	@RequestMapping(value = "/admin/gettrades", method = RequestMethod.GET)
	public ModelAndView gettrades(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<UserCostInfoBean> list = tradeService.getUserCostInfos(cursor, limit);
		Long total = tradeService.getUserCostCount();
		ModelAndView mav = new ModelAndView("listtrades");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 4);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/getcharges", method = RequestMethod.GET)
	public ModelAndView getcharges(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<ChargeLogBean> list = tradeService.getChargeLogInfos(cursor, limit);
		Long total = tradeService.getChargeLogCount();
		ModelAndView mav = new ModelAndView("listchargelogs");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 5);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/getcourts", method = RequestMethod.GET)
	public ModelAndView getcourts(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<Court> list = deviceService.getCourts(null, null, null,null, cursor, limit);
		Long total = deviceService.getCourtCount(null, null, null,null);
		ModelAndView mav = new ModelAndView("listcourts");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 6);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/toaddcourt", method = RequestMethod.GET)
	public ModelAndView toaddcourt(
			HttpServletRequest request
			) throws Exception{
		ModelAndView mav = new ModelAndView("addcourt");
		return mav;
	}
	@RequestMapping(value = "/admin/addcourt", method = RequestMethod.POST)
	public ModelAndView addcourt(
			HttpServletRequest request,
			@RequestParam String name,
			@RequestParam(required = false ) Integer operate,
			@RequestParam(required = false ) String startTime,
			@RequestParam(required = false ) String endTime,
			@RequestParam Integer type,
			@RequestParam Double longitude,
			@RequestParam Double latitude,
			@RequestParam(required = false ) String province,
			@RequestParam(required = false ) String city,
			@RequestParam(required = false ) String district,
			@RequestParam(required = false ) String detail,
			@RequestParam(required = false ) MultipartFile[] file
			) throws Exception{
		ModelAndView mav = new ModelAndView("listcourts");
		try{
			Date sDate = DateUtils.formatHMS(startTime);
			Date eDate = DateUtils.formatHMS(endTime);
			Court c = new Court(name, operate, sDate, eDate, type, longitude, latitude, province, city, district, detail);
			if(file != null && file.length > 0){
				String url = qiniuService.uploadFile("user-" + UUID.randomUUID().toString(), file[0].getInputStream());
				c.setPicture(url);
			}
			deviceService.createCourt(c);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("errorinfo", "<script language=\"javascript\">alert('场馆添加失败！');</script>");
		}finally{
			List<Court> list = deviceService.getCourts(null, null, null,null, 0, 20);
			Long total = deviceService.getCourtCount(null, null, null,null);
			mav.addObject("list", list);
			mav.addObject("page", 1);
			mav.addObject("count", total);
			mav.addObject("navNum", 6);
			mav.addObject("total", PageUtil.getTotalPage(total, 20));
		}
		return mav;
	}
	@RequestMapping(value = "/admin/toadddevice", method = RequestMethod.GET)
	public ModelAndView toadddevice(
			HttpServletRequest request
			) throws Exception{
		ModelAndView mav = new ModelAndView("adddevice");
		return mav;
	}
	@RequestMapping(value = "/admin/adddevice", method = RequestMethod.POST)
	public ModelAndView adddevice(
			HttpServletRequest request,
			@RequestParam Integer courtid,
			@RequestParam String courtname,
			@RequestParam (required = true,defaultValue="1" )Integer  type1,
			@RequestParam MultipartFile[] file
			) throws Exception{
		ModelAndView mav = new ModelAndView("adddevice");
		mav.addObject("navNum", 3);
		try{
			if(file != null && file.length > 0){
				List<Device> devices = new ArrayList<>();
				InputStream is = file[0].getInputStream();
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				HSSFSheet devicebook = workbook.getSheetAt(0);
				List<String> sns = new ArrayList<>();
				if(devicebook != null){
					int total = devicebook.getLastRowNum();
					if(total > 0 ){
						for(int num = 1; num <= total; num ++){
							HSSFRow row = devicebook.getRow(num);
							sns.add(row.getCell(0).getStringCellValue());	
						}
					}
				}
				workbook.close();
				int i = 0;
				long total1 = deviceService.getDeviceCount();
				for(String sn : sns){
					Device device = new Device();
					device.setSn(sn);
					device.setCourtId(courtid);
					device.setCourtName(courtname);
					device.setSn(sns.get(i++));
					device.setType(type1);
					device.setState(0);
					device.setCreateTime(DateUtils.getDate());
					device.setUpdateTime(device.getCreateTime());
					deviceService.createDevice(device);
				}
				long total2 = deviceService.getDeviceCount();
				long total = total2 - total1;
				if(total > 0){
					mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加成功（"+total+"）！');</script>");
				}else{
					mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加失败（"+total+"）,确认正确填写excel文件！');</script>");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加失败,确认正确填写excel文件！');</script>");
			return mav;
		}finally{
		}
		return mav;
	}
	@RequestMapping(value = "/admin/addbasketball", method = RequestMethod.POST)
	public ModelAndView addbasketball(
			HttpServletRequest request,
			@RequestParam (required = true,defaultValue="1" )Integer  type2,
			@RequestParam MultipartFile[] file
			) throws Exception{
		ModelAndView mav = new ModelAndView("adddevice");
		mav.addObject("navNum", 3);
		try{
			if(file != null && file.length > 0){
				List<BasketBall> balls = new ArrayList<>();
				InputStream is = file[0].getInputStream();
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				HSSFSheet devicebook = workbook.getSheetAt(0);
				if(devicebook != null){
					int total = devicebook.getLastRowNum();
					if(total > 0 ){
						for(int num = 1; num <= total; num ++){
							HSSFRow row = devicebook.getRow(num);
							String ballSn = row.getCell(0).getStringCellValue();
							if(ballSn != null && ballSn.length() == 8){
								BasketBall ball = new BasketBall();
								ball.setSn(ballSn);
								ball.setType(type2);
								ball.setState(0);
								balls.add(ball);
							}
						}
					}
				}
				workbook.close();
				if(balls.size() > 0){
					long total1 = deviceService.getBasketballCount();
					deviceService.createBasketballs(balls);
					long total2 = deviceService.getBasketballCount();
					long total = total2 - total1;
					if(total > 0){
						mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加成功（"+total+"）！');</script>");
					}else{
						mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加失败（"+total+"）,确认正确填写excel文件！');</script>");
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("addinfo", "<script language=\"javascript\">alert('批量添加失败,确认正确填写excel文件！');</script>");
		}finally{
		}
		return mav;
	}
	@RequestMapping(value = "/admin/getalarms", method = RequestMethod.GET)
	public ModelAndView getalarms(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<DeviceAlarm> list = alarmService.getAlarms(null, cursor, limit);
		Long total = alarmService.getAlarmCount(null);
		ModelAndView mav = new ModelAndView("listalarms");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 7);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/handlealarm", method = RequestMethod.GET)
	public ModelAndView handlealarm(
			HttpServletRequest request,
			@RequestParam Integer id
			) throws Exception{
		DeviceAlarm da = alarmService.getAlarm(id);
		da.setState(1);
		alarmService.updateAlarm(da);
		return getalarms(request, 0, 50, 1);
	}
	@RequestMapping(value = "/admin/getalramusers", method = RequestMethod.GET)
	public ModelAndView getalramusers(
			HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "1") Integer page
			) throws Exception{
		limit = PageUtil.checkLimit(limit, 500);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);

		List<AlarmOpM> list = alarmService.getAlarmUsers(null, cursor, limit);
		Long total = alarmService.getAlarmUserCount(null);
		ModelAndView mav = new ModelAndView("listalarmuser");
		mav.addObject("list", list);
		mav.addObject("page", page);
		mav.addObject("count", total);
		mav.addObject("navNum", 8);
		mav.addObject("total", PageUtil.getTotalPage(total, limit));
		return mav;
	}
	@RequestMapping(value = "/admin/toaddalarmuser", method = RequestMethod.GET)
	public ModelAndView toaddalarmuser(
			HttpServletRequest request
			) throws Exception{
		ModelAndView mav = new ModelAndView("addalarmuser");
		return mav;
	}
	@RequestMapping(value = "/admin/addalarmuser", method = RequestMethod.POST)
	public ModelAndView addalarmuser(
			HttpServletRequest request,
			@RequestParam String name,
			@RequestParam String phone,
			@RequestParam Integer type
			) throws Exception{
		ModelAndView mav = new ModelAndView("listalarmuser");
		try{
			AlarmOpM ao = new AlarmOpM();
			ao.setName(name);
			ao.setPhone(phone);
			ao.setType(type);
			ao.setPassword(DigestUtils.md5("bblq1234", null));
			alarmService.createAlarmUser(ao);
			
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("errorinfo", "<script language=\"javascript\">alert('添加失败！');</script>");
		}finally{
			List<AlarmOpM> list = alarmService.getAlarmUsers(null, 0, 20);
			Long total = alarmService.getAlarmUserCount(null);
			mav.addObject("list", list);
			mav.addObject("page", 1);
			mav.addObject("count", total);
			mav.addObject("navNum", 8);
			mav.addObject("total", PageUtil.getTotalPage(total, 20));
		}
		return mav;
	}
	
	@RequestMapping(value = "/admin/toeditscore", method = RequestMethod.GET)
	public ModelAndView toeditscore(
			HttpServletRequest request
			) throws Exception{
		ModelAndView mav = new ModelAndView("editscore");
		return mav;
	}
	@RequestMapping(value = "/admin/editscore", method = RequestMethod.POST)
	public ModelAndView editscore(
			HttpServletRequest request,
			@RequestParam String phone,
			@RequestParam Integer type
			) throws Exception{
		ModelAndView mav = new ModelAndView("editscore");
		mav.addObject("phone", phone);
		mav.addObject("type", type);
		try{
			User user = userFacade.getUserbyPhone(phone);
			Wallet w = tradeService.getWallet(user.getId());
			UserScore us = new UserScore();
			us.setCreateTime(DateUtils.getDate());
			
			int score = ScoreUtil.getScore(type);
			if(score == Integer.MIN_VALUE){
				if(w.getScore() > 0){
					us.setScore(-w.getScore());
					w.setScore(0);
				}
			}else{
				w.setScore(w.getScore() + score);
				us.setScore(score);
			}us.setType(type);us.setUserId(user.getId());
			tradeService.createUserScore(us);
			tradeService.updateWallet(w);
			mav.addObject("info","已将用户信用分数更新至（"+w.getScore()+"）");
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("info", "<script language=\"javascript\">alert('处理失败！');</script>");
		}finally{
			mav.addObject("navNum", 9);
		}
		return mav;
	}
}
