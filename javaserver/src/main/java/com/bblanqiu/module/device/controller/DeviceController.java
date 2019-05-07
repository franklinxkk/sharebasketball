package com.bblanqiu.module.device.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.amqp.AmqpMessageSender;
import com.bblanqiu.common.bean.BasicPageResultBean;
import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.ErrorCode;
import com.bblanqiu.common.exception.ErrorCodeException;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.BasketBall;
import com.bblanqiu.common.mysql.model.Court;
import com.bblanqiu.common.mysql.model.Device;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.CostUtil;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.common.util.PageUtil;
import com.bblanqiu.module.admin.facade.AdminFacade;
import com.bblanqiu.module.device.bean.CourtResultBean;
import com.bblanqiu.module.device.bean.DeviceCurrentBean;
import com.bblanqiu.module.device.bean.DeviceStateBean;
import com.bblanqiu.module.device.bean.DeviceTypeBean;
import com.bblanqiu.module.device.facade.DeviceFacade;
import com.bblanqiu.module.device.service.DeviceService;
import com.bblanqiu.module.mqtt.almq.MQTTSendMsg;
import com.bblanqiu.module.mqtt.converter.DataConverter;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.facade.UserFacade;
import com.bblanqiu.module.user.service.TradeService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DeviceController extends HandleExceptionController{
	private static Logger logger = LoggerFactory.getLogger(DeviceController.class);
	@Autowired
	DeviceService deviceService;
	@Autowired
	UserFacade userFacade;
	@Autowired
	TradeService tradeService;
	@Autowired
	MQTTSendMsg mqttSendMsg;
	@Autowired
	AmqpMessageSender amqpMessageSender;
	@Autowired
	ObjectMapper mapper;
	@Autowired
	DeviceFacade deviceFacade;
	@RequestMapping(value = "/device/useball", method = RequestMethod.GET)
	public @ResponseBody Object useball(
			@RequestParam(required = true) String sn,
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");

		if(token.getType() < 3){
			throw new ErrorCodeException(ErrorCode.ALI_UN_VERIFIED);
		}
		UserCacheBean user = userFacade.getUser(token.getUserId());
		if(user.getStoragepass() == null || user.getStoragepass().length() < 2){
			throw new ErrorCodeException(ErrorCode.DEVICE_OPENING_PASS);
		}
		
		if(tradeService.isUsed(token.getUserId(), DeviceTypeBean.USE_BALL)){
			throw new ErrorCodeException(ErrorCode.OLREADY_USED);
		}
		
		Wallet w = tradeService.getWallet(token.getUserId());
		if(w.getCredit() < 600){
			throw new ErrorCodeException(ErrorCode.AUTH_ZM_SCORE_LIMIT);
		}else if(w.getScore() <= 0){
			throw new ErrorCodeException(ErrorCode.AUTH_BB_SCORE_LIMIT);
		}else if(w == null || w.getMoney() <= 0){
			throw new ErrorCodeException(ErrorCode.ARREARAGE);
		}
		Device device = deviceService.getDeviceBySN(sn);
		if(device != null){
			if(device.getState() == 1){
				BasketBall bb = deviceService.getBasketballBySn(device.getBallSn());
				if(bb != null){
					if(bb.getState() == 0){
						deviceService.deleteBeforeOpenDevice(sn, device.getBallSn());
						deviceService.createBeforeOpenDevice(token.getUserId(), sn, device.getBallSn(), null, device.getCourtName());
						mqttSendMsg.sendMsg(sn, DataConverter.warpOpenBallBox(sn));
						return new OnlyResultBean(null,true);
					}else{
						throw new ErrorCodeException(ErrorCode.DEVICE_OLREADY_USED);
					}
				}else{
					throw new ErrorCodeException(ErrorCode.DEVICE_DOSE_NOT_PROVIDE,sn);
				}
				
			}else{
				throw new ErrorCodeException(ErrorCode.DEVICE_NOT_ONLINE);
			}
		}else{
			throw new ErrorCodeException(ErrorCode.DEVICE_DOSE_NOT_EXISTS,sn);
		}

		
	}
	@RequestMapping(value = "/device/usestoragebox", method = RequestMethod.GET)
	public @ResponseBody Object usestoragebox(
			@RequestParam(required = true) String sn,
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		if(token.getType() < 3){
			throw new ErrorCodeException(ErrorCode.ALI_UN_VERIFIED);
		}
		UserCacheBean user = userFacade.getUser(token.getUserId());
		if(user.getStoragepass() == null || user.getStoragepass().length() < 2){
			throw new ErrorCodeException(ErrorCode.DEVICE_OPENING_PASS);
		}
		if(deviceFacade.getSnUsedObject(sn) != null){
			Thread.sleep(3000);
		}else{
			deviceFacade.addSnUsedObject(sn);
		}
		if(tradeService.isUsed(token.getUserId(), DeviceTypeBean.USE_BOX)){
			throw new ErrorCodeException(ErrorCode.OLREADY_USED);
		}
		
		Wallet w = tradeService.getWallet(token.getUserId());
		logger.info(mapper.writeValueAsString(w));
		if(w.getCredit() < 600){
			throw new ErrorCodeException(ErrorCode.AUTH_ZM_SCORE_LIMIT);
		}else if(w.getScore() <= 0){
			throw new ErrorCodeException(ErrorCode.AUTH_BB_SCORE_LIMIT);
		}else if(w == null || w.getMoney() <= 0){
			throw new ErrorCodeException(ErrorCode.ARREARAGE);
		}
		Device device = deviceService.getDeviceBySN(sn);
		if(device != null){
			if(device.getState() == 1){
				
				StorageBox sb = deviceService.getFreeStoragebox(sn);
				if(sb != null){
					deviceService.deleteBeforeOpenDevice(sn, sb.getNum());
					deviceService.createBeforeOpenDevice(token.getUserId(), sn, null, sb.getNum(), device.getCourtName());
					mqttSendMsg.sendMsg(sn, DataConverter.warpOpenStorageBox(sn, sb.getNum()));
					return new OnlyResultBean(null,true);
				}else{
					throw new ErrorCodeException(ErrorCode.ALLBOX_USED);
				}
			}else{
				throw new ErrorCodeException(ErrorCode.DEVICE_NOT_ONLINE);
			}
		}else{
			throw new ErrorCodeException(ErrorCode.DEVICE_DOSE_NOT_EXISTS,sn);
		}
		
	}
	@RequestMapping(value = "/device/state", method = RequestMethod.GET)
	public @ResponseBody Object devicestate(
			@RequestParam(required = true) String sn,
			HttpServletRequest request) throws Exception {
		Device device = deviceService.getDeviceBySN(sn);
		if(device != null){
			if(device.getState() == 1){
				BasketBall bb = deviceService.getBasketballBySn(device.getBallSn());
				if(bb != null){
					boolean used = true;
					if(bb != null && bb.getState() == 0){
						used = false;
					}
					long free = deviceService.getFreeStorageBoxCount(sn);
					DeviceStateBean sb = new DeviceStateBean(used,(int)free);
					sb.setState(device.getState());
					return new OnlyResultBean(sb);
				}else{
					throw new ErrorCodeException(ErrorCode.BALL_DOSE_NOT_PROVIDE,device.getBallSn());
				}
			}else{
				throw new ErrorCodeException(ErrorCode.DEVICE_NOT_ONLINE);
			}
		}else{
			throw new ErrorCodeException(ErrorCode.DEVICE_DOSE_NOT_EXISTS,sn);
		}

	}
	@Autowired
	AdminFacade adminFacade;
	@RequestMapping(value = "/device/closestoragebox", method = RequestMethod.GET)
	public @ResponseBody Object closestoragebox(
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		UserCost uc = tradeService.getCurrentUserBoxCost(token.getUserId());
		if(uc != null){
			Device device = deviceService.getDeviceBySN(uc.getDeviceSn());
			if(device.getLockState() != null){
				if(device.getLockState().charAt(uc.getNum()) == Character.valueOf('1')){
					throw new ErrorCodeException(ErrorCode.DEVICE_OPENING);
				}
			}
			uc.setEndTime(DateUtils.getDate());
			uc.setState(1);
			Wallet w = tradeService.getWallet(token.getUserId());//0.5元 30分钟
			int minute = DateUtils.getTimeGapByMinute(uc.getStartTime(), uc.getEndTime());
			uc.setConsume(CostUtil.getCost(minute));
			w.setMoney(w.getMoney() - uc.getConsume());
			w.setScore(w.getScore() + 1);
			StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(uc.getDeviceSn(), uc.getNum());
			sb.setState(0);
			tradeService.closeStorageBox(uc, sb, w);
			//mqttSendMsg.sendMsg(uc.getDeviceSn(), DataConverter.warpOpenStorageBox(sn, sb.getNum()));
			amqpMessageSender.asyncPushDataMessage(uc, "statistics");
			return new OnlyResultBean(uc);
		}else{
			throw new ErrorCodeException(ErrorCode.OLREADY_END);
		}
		
	}

	public static void main(String []args) throws JsonParseException, JsonMappingException, IOException{
		String s= "101111110";
		System.out.println(s.length());
		Integer i = 0;
		System.out.println(i<=0);

	}
	
	@RequestMapping(value = "/court/query", method = RequestMethod.GET)
	public @ResponseBody Object court(
			@RequestParam(required = false) String province,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String district,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "20") Integer limit,
			@RequestParam(required = false) Integer page
			) throws Exception{
		System.out.println(province +" "+city+" "+" "+district);
		limit = PageUtil.checkLimit(limit, 20);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);
		List<Court> list = deviceService.getCourts(province, city, district,type, cursor, limit);
		List<CourtResultBean> cs =  new ArrayList<>();
		for(Court c : list){
			CourtResultBean crb = mapper.convertValue(c, CourtResultBean.class);
			crb.setStart(DateUtils.formatHMS(c.getStartTime()));
			crb.setEnd(DateUtils.formatHMS(c.getEndTime()));
			cs.add(crb);
		}
		Long total = deviceService.getCourtCount(province, city, district,type);
		return new BasicPageResultBean(total, cursor, limit, cs);
	}
	
	@RequestMapping(value = "/device/usehistory", method = RequestMethod.GET)
	public @ResponseBody Object usehistory(
			HttpServletRequest request,
			@RequestParam(required = false) Integer type,
			@RequestParam(required = false, defaultValue = "0") Integer cursor,
			@RequestParam(required = false, defaultValue = "20") Integer limit,
			@RequestParam(required = false) Integer page) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		limit = PageUtil.checkLimit(limit, 20);
		cursor = PageUtil.checkCursorByPage(cursor, limit, page);
		List<UserCost> list = tradeService.getUserCostByUserIdAndType(token.getUserId(),type,cursor, limit);
		Long total = tradeService.getUserCostCountByUserIdAndType(token.getUserId(), type);
		return new BasicPageResultBean(total, cursor, limit, list);
	}

	@RequestMapping(value = "/device/currentservice", method = RequestMethod.GET)
	public @ResponseBody Object currentservice(
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		UserCost box = tradeService.getCurrentUserBoxCost(token.getUserId());
		if(box != null){
			int minuteBox = DateUtils.getTimeGapByMinute(box.getStartTime(), DateUtils.getDate());
			box.setConsume(CostUtil.getCost(minuteBox));
		}
		UserCost ball = tradeService.getCurrentUserBallCost(token.getUserId());
		if(ball != null){
			int minuteBall = DateUtils.getTimeGapByMinute(ball.getStartTime(), DateUtils.getDate());
			ball.setConsume(CostUtil.getCost(minuteBall));	
		}
		
		DeviceCurrentBean dcb = new DeviceCurrentBean();
		dcb.setUserId(token.getUserId());
		dcb.setBox(box);
		dcb.setBall(ball);
		return new OnlyResultBean(dcb);
	}
	@RequestMapping(value = "/user/useballinfo", method = RequestMethod.GET)
	public @ResponseBody Object basketballinfo(
			@RequestParam(required = true) String sn,
			HttpServletRequest request) throws Exception {
		Token token = (Token)request.getAttribute("X-TOKEN");
		UserCost ball = tradeService.getLatestUserBallCost(token.getUserId());
		if(ball != null){
			if(ball.getConsume() == null){
				int minuteBall = DateUtils.getTimeGapByMinute(ball.getStartTime(), DateUtils.getDate());
				ball.setConsume(CostUtil.getCost(minuteBall));
			}
		}
		return new OnlyResultBean(ball);
	}
}
