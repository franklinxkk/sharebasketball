package com.bblanqiu.module.pay.weixin.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.ErrorCode;
import com.bblanqiu.common.exception.ErrorCodeException;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.ChargeLog;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.common.util.HttpRequestUtils;
import com.bblanqiu.module.pay.util.PayCommonUtil;
import com.bblanqiu.module.pay.util.XMLUtil;
import com.bblanqiu.module.pay.weixin.bean.WxInitBean;
import com.bblanqiu.module.pay.weixin.service.Signature;
import com.bblanqiu.module.pay.weixin.service.WechartpayService;
import com.bblanqiu.module.sms.SmsHander;
import com.bblanqiu.module.user.bean.UserCacheBean;
import com.bblanqiu.module.user.facade.UserFacade;
import com.bblanqiu.module.user.service.ThirdService;
import com.bblanqiu.module.user.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@Validated
public class WeixinpayController extends HandleExceptionController{
	private static Logger logger = LoggerFactory.getLogger(WeixinpayController.class);
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	UserFacade userFacade;
	@Autowired
	WechartpayService wechartpayService;
	@Autowired
	ThirdService thirdService;
	@Autowired
	TradeService tradeService;
	/**
	 * 微信统一下单接口
	 * @param request
	 * @param response
	 */
	@RequestMapping("/wxpay/charge/init")
	public @ResponseBody Object wxPrePay(HttpServletRequest request,
			HttpServletResponse response,
			@Max(value=100) @Min(value=1) @RequestParam(required = true) int charge){
		try {
			Token token = (Token)request.getAttribute("X-TOKEN");
			//List<ChargeLog> cls  = tradeService.getCurrentUncharges(token.getUserId());
			UserCacheBean user = userFacade.getUser(token.getUserId());
			String tradeId = "bblqwex"+DateUtils.getTimeTag() + "_"+user.getId();
			int price100 = charge * 100;
 
			ChargeLog cl = new ChargeLog();
			cl.setChargeQuota(charge);
			cl.setOuttradeno(tradeId);
			cl.setCreateTime(DateUtils.getDate());
			cl.setState(0);
			cl.setType(1);
			cl.setUserId(token.getUserId());
			tradeService.createCharge(cl);
			String ip = HttpRequestUtils.getIpAddr(request);
			WxInitBean wib = wechartpayService.getWxPayInfo(tradeId, price100, ip);
			return new OnlyResultBean(wib);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ErrorCodeException(ErrorCode.PAY_ORDER_ERROR);
	}
	/**
	 * 微信异步通知

	 */
	@RequestMapping("/wxpay/charge/callback")
	public void wxNotify(HttpServletRequest 
			request,HttpServletResponse response) throws Exception{
		InputStream inputStream ;  
		StringBuffer sb = new StringBuffer();  
		inputStream = request.getInputStream();  
		String s ;  
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
		while ((s = in.readLine()) != null){  
			sb.append(s);  
		}  
		in.close();  
		inputStream.close();  
		String resXml = "";  
		if(Signature.checkIsSignValidFromResponseString(sb.toString())){
			//解析xml成map  
			Map<String, String> m = new HashMap<String, String>();  
			m = XMLUtil.doXMLParse(sb.toString());
			logger.info(m.toString());
			if("SUCCESS".equals((String)m.get("result_code"))){
				String out_trade_no = (String)m.get("out_trade_no"); //商户订单号
				String total_fee = (String)m.get("total_fee");  
				List<ChargeLog> cls;
				try {
					cls = tradeService.getCurrentUncharges(out_trade_no);
					if(cls != null && cls.size() > 0){
						ChargeLog cl = cls.get(0);
						logger.info("host:{} pre-free:{} callback-free:{}",request.getRequestURL().toString(),cl.getChargeQuota(),total_fee);
						cl.setState(1);
						tradeService.updatecharges(cl);
						Wallet w = tradeService.getWallet(cl.getUserId());
						if(w != null){
							w.setMoney(w.getMoney() + cl.getChargeQuota());
							tradeService.updateWallet(w);
						}else{
							w = new Wallet();
							w.setUserId(cl.getUserId());
							w.setCredit(0);
							w.setMoney((float)(cl.getChargeQuota()));
							w.setScore(100);
							tradeService.createWallet(w);
						}
						
						UserCacheBean user = userFacade.getUser(cl.getUserId());
						SmsHander.sendChargeMsg(user.getPhone(), String.valueOf(cl.getChargeQuota()));
					}else{
						resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
						logger.info("订单已处理");  
					}
				} catch (Exception e) {
					e.printStackTrace();
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"  
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";  
					logger.info("订单查询失败");  
				}
			}else {  
				logger.info("支付失败,错误信息：" + m.get("err_code"));  
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";  
			} 		 
		}else{
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  
					+ "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> "; 
			logger.info("签名错误" + sb.toString());  
		}
		
		BufferedOutputStream out = new BufferedOutputStream( response.getOutputStream());  
		out.write(resXml.getBytes());  
		out.flush();  
		out.close();  
	}
}
