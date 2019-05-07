package com.bblanqiu.module.pay.ali.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.ErrorCode;
import com.bblanqiu.common.exception.ErrorCodeException;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.ChargeLog;
import com.bblanqiu.common.mysql.model.ThirdInfo;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.pay.ali.bean.AliAuthBean;
import com.bblanqiu.module.pay.ali.bean.AliChargeBean;
import com.bblanqiu.module.pay.ali.bean.AliZhimaScoreBean;
import com.bblanqiu.module.pay.ali.bean.AuthResultBean;
import com.bblanqiu.module.pay.ali.service.AlipayService;
import com.bblanqiu.module.sms.SmsHander;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.facade.TokenFacade;
import com.bblanqiu.module.user.facade.UserFacade;
import com.bblanqiu.module.user.service.ThirdService;
import com.bblanqiu.module.user.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Controller
public class AlipayController extends HandleExceptionController{
	private static Logger logger = LoggerFactory.getLogger(AlipayController.class);
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	UserFacade userFacade;
	@Autowired
	AlipayService alipayService;
	@Autowired
	ThirdService thirdService;
	@Autowired
	TradeService tradeService;
	@Autowired
	TokenFacade tokenFacade;
	
	@RequestMapping(value = "/alipay/addinfo", method = RequestMethod.GET)
	public @ResponseBody Object addinfo(
			HttpServletRequest request
    		){
		try {
//			alipayService.certQuery(bizNo)
			logger.info("alipay-callback:{}",mapper.writeValueAsString(request.getParameterMap()));
//			logger.info("zhima-auth-result:{}",);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new OnlyResultBean("ok");
	}

	@RequestMapping(value = "/user/authentication/callback", method = RequestMethod.GET)
	public @ResponseBody Object authCallback(
			HttpServletRequest request
    		)throws Exception{
		AuthResultBean arb = null;
		try {
			Token token = (Token)request.getAttribute("X-TOKEN");
			ThirdInfo ti = thirdService.getThirdInfo(token.getUserId());
			if(ti != null){
				arb = alipayService.certQuery(ti.getZhimaAuthBizNo());
				logger.info("zhima-auth-result:{},{}",String.valueOf(arb.isPassed()),arb.getReason());
				if(arb.isPassed()){
					ti.setZhimaAuthPassed(1);
					AliZhimaScoreBean sb = null;
					UserCacheBean user = userFacade.getUser(token.getUserId());
					String openid = alipayService.zhimaAuthInfoReq(ti.getName(), ti.getNo());
					if(openid != null){
						ti.setZmopenid(openid);
						String transactionId = "bblanqusc"+DateUtils.getTimeTag() + "_"+user.getId();
						sb = alipayService.getZhimaCreditScore(transactionId,openid);
						if(sb != null){
							ti.setZmscore(sb.getScore());
							ti.setZhimaScoreBizNo(sb.getBizNo());
							ti.setZhimaScoreTransactionId(transactionId);
							thirdService.updateThirdInfo(ti);
							userFacade.updateUserTypeAndName(token.getUserId(), 3, ti.getName());
							tokenFacade.updateTokenType(token.getToken(), 3);
							Wallet w = tradeService.getWallet(token.getUserId());
							if(w != null){
								w.setCredit(Integer.valueOf(sb.getScore()));
								tradeService.updateWallet(w);
							}else{
								w = new Wallet();
								w.setUserId(token.getUserId());
								w.setCredit(Integer.valueOf(sb.getScore()));
								w.setMoney(0f);
								w.setScore(100);
								tradeService.createWallet(w);
							}
							return new OnlyResultBean("ok");
						}else{
							logger.error("get zhima score error:{},{}",openid);
						}
					}
					
				}
			}else{
				throw new ErrorCodeException(ErrorCode.ALI_UN_VERIFIED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ErrorCodeException(ErrorCode.AUTH_ERROR,arb == null ? "":arb.getReason());
	}
	@RequestMapping(value = "/user/authentication/init", method = RequestMethod.GET)
	public @ResponseBody Object authinit(
			HttpServletRequest request,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String no,
			@RequestParam(required = true, defaultValue="1") Integer type
    		)throws Exception{
		try {
			Token token = (Token)request.getAttribute("X-TOKEN");
			UserCacheBean user = userFacade.getUser(token.getUserId());
			String transactionId = "bblanquln"+DateUtils.getTimeTag() + "_"+user.getId();
			String bizNo = alipayService.authInit(transactionId, name, no);
			String url = alipayService.getAuthUrl(bizNo,type);
			ThirdInfo ti = thirdService.getThirdInfo(user.getId());
			if(ti == null){
				ti = new ThirdInfo();
				ti.setUserId(user.getId());
				ti.setZhimaAuthTime(DateUtils.getDate());
				ti.setZhimaAuthTransactionId(transactionId);
				ti.setZhimaAuthUrl(url);
				ti.setZhimaAuthBizNo(bizNo);
				ti.setZhimaAuthPassed(0);
				ti.setName(name);
				ti.setNo(no);
				thirdService.createThirdInfo(ti);
			}else{
				ti.setName(name);
				ti.setNo(no);
				ti.setUserId(user.getId());
				ti.setZhimaAuthTime(DateUtils.getDate());
				ti.setZhimaAuthTransactionId(transactionId);
				ti.setZhimaAuthUrl(url);
				ti.setZhimaAuthBizNo(bizNo);
				thirdService.updateThirdInfo(ti);
			}
			AliAuthBean aab = new AliAuthBean();
			aab.setZhimaAuthUrl(ti.getZhimaAuthUrl());
			return new OnlyResultBean(aab);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorCodeException(ErrorCode.AUTH_ERROR,e.getMessage());
		}
	}
	@RequestMapping(value = "/alipay/charge/init", method = RequestMethod.GET)
	public @ResponseBody Object chargeinit(
			HttpServletRequest request,
			@RequestParam(required = true) int charge
    		){
		try {
			Token token = (Token)request.getAttribute("X-TOKEN");
			UserCacheBean user = userFacade.getUser(token.getUserId());
			String tradeId = "bblqali"+DateUtils.getTimeTag() + "_"+user.getId();
			String order = alipayService.getOrder(charge, token.getUserId(), tradeId);
			AliChargeBean acb = new AliChargeBean();
			acb.setOuttradeno(order);
			ChargeLog cl = new ChargeLog();
			cl.setChargeQuota(charge);
			cl.setOuttradeno(tradeId);
			cl.setCreateTime(DateUtils.getDate());
			cl.setState(0);
			cl.setType(2);
			cl.setUserId(token.getUserId());
			tradeService.createCharge(cl);
			return new OnlyResultBean(acb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ErrorCodeException(ErrorCode.PAY_ORDER_ERROR);
	}
	@RequestMapping(value = "/alipay/charge/callback", method = RequestMethod.GET)
	public @ResponseBody Object chargecallback(
			HttpServletRequest request
    		){
		try {
			boolean isPayed = alipayService.isPayed(request.getParameterMap());
			logger.info("alipay-charge-g-callback:{},{}",mapper.writeValueAsString(request.getParameterMap()),isPayed);
			if(isPayed){
				String outtradeno = request.getParameter("out_trade_no");
				Float f = alipayService.aliTradeQuery(outtradeno);
				List<ChargeLog> cls = tradeService.getCurrentUncharges(outtradeno);
				if(cls != null && cls.size() > 0){
					ChargeLog cl = cls.get(0);
					cl.setState(1);
					tradeService.updatecharges(cl);
					
					Wallet w = tradeService.getWallet(cl.getUserId());
					if(w != null){
						w.setMoney(w.getMoney() + f);
						tradeService.updateWallet(w);
					}else{
						w = new Wallet();
						w.setUserId(cl.getUserId());
						w.setCredit(0);
						w.setMoney(f);
						w.setScore(100);
						tradeService.createWallet(w);
					}
					UserCacheBean user = userFacade.getUser(cl.getUserId());
					SmsHander.sendChargeMsg(user.getPhone(), String.valueOf(f));
				}
			}
			return new OnlyResultBean("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ErrorCodeException(ErrorCode.PAY_ORDER_ERROR);
	}
	@RequestMapping(value = "/alipay/charge/callback", method = RequestMethod.POST)
	public @ResponseBody Object chargecallbackpost(
			HttpServletRequest request
    		){
		try {
			boolean isPayed = alipayService.isPayed(request.getParameterMap());
			logger.info("alipay-charge-p-callback:{},{}",mapper.writeValueAsString(request.getParameterMap()),isPayed);
			if(isPayed){
				String outtradeno = request.getParameter("out_trade_no");
				Float f = alipayService.aliTradeQuery(outtradeno);
				List<ChargeLog> cls = tradeService.getCurrentUncharges(outtradeno);
				if(cls != null && cls.size() > 0){
					ChargeLog cl = cls.get(0);
					cl.setState(1);
					tradeService.updatecharges(cl);
					Wallet w = tradeService.getWallet(cl.getUserId());
					if(w != null){
						w.setMoney(w.getMoney() + f);
						tradeService.updateWallet(w);
					}else{
						w = new Wallet();
						w.setUserId(cl.getUserId());
						w.setCredit(0);
						w.setMoney(f);
						w.setScore(100);
						tradeService.createWallet(w);
					}
					UserCacheBean user = userFacade.getUser(cl.getUserId());
					SmsHander.sendChargeMsg(user.getPhone(), String.valueOf(f));
				}
			}
			return new OnlyResultBean("ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ErrorCodeException(ErrorCode.PAY_ORDER_ERROR);
	}
	/*@RequestMapping(value = "/user/authentication/zhimascore", method = RequestMethod.GET)
	public @ResponseBody Object zhimascore(
			HttpServletRequest request
    		){
		AliZhimaScoreBean sb = null;
		try {
			Token token = (Token)request.getAttribute("X-TOKEN");
			UserCacheBean user = userFacade.getUser(token.getUserId());
			if(user.getType() >= 2){
				ThirdInfo ti = thirdService.getThirdInfo(token.getUserId());
				if(ti != null){
					String openid = alipayService.zhimaAuthInfoReq(ti.getName(), ti.getNo());
					if(openid != null){
						ti.setZmopenid(openid);
						String transactionId = "bblanqusc"+DateUtils.getTimeTag() + user.getPhone();
						sb = alipayService.getZhimaCreditScore(transactionId,openid);
						if(sb != null){
							ti.setZmscore(sb.getScore());
							ti.setZhimaScoreBizNo(sb.getBizNo());
							ti.setZhimaScoreTransactionId(transactionId);
							thirdService.updateThirdInfo(ti);
							userFacade.updateUserType(token.getUserId(), 3);
							tokenFacade.updateTokenType(token.getToken(), 3);
							Wallet w = tradeService.getWallet(token.getUserId());
							if(w != null){
								w.setCredit(Integer.valueOf(sb.getScore()));
								tradeService.updateWallet(w);
							}else{
								w = new Wallet();
								w.setUserId(token.getUserId());
								w.setCredit(Integer.valueOf(sb.getScore()));
								w.setMoney(0f);
								w.setScore(100);
								tradeService.createWallet(w);
							}
							
						}else{
							logger.error("get zhima score error:{},{}",openid);
						}
					}else{
						logger.error("get zhima openid error:{},{}",ti.getName(), ti.getNo());
					}
				}else{
					throw new ErrorCodeException(ErrorCode.ALI_UN_VERIFIED);
				}
			}else{
				throw new ErrorCodeException(ErrorCode.ALI_UN_VERIFIED);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return new OnlyResultBean(sb);
	}*/
}
