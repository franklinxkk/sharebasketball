package com.bblanqiu.module.pay.ali.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.antgroup.zmxy.openplatform.api.DefaultZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaAuthInfoAuthqueryRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditScoreGetRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationCertifyRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationInitializeRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCustomerCertificationQueryRequest;
import com.antgroup.zmxy.openplatform.api.response.ZhimaAuthInfoAuthqueryResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditScoreGetResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCustomerCertificationInitializeResponse;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCustomerCertificationQueryResponse;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.pay.ali.bean.AliZhimaScoreBean;
import com.bblanqiu.module.pay.ali.bean.AuthResultBean;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AlipayService {
	private String appId = "1002845";
	private String gatewayUrl = "https://zmopenapi.zmxy.com.cn/openapi.do";
	String pri="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMqBb3pLezbayW7S"
			+ "9kBT8y0JFeKY7XX5Lu2FPEG1tR1X7kH/Tk+4Ka9bCaJzJamVuUDz4W0W+ZqNH6BI"
			+ "Xx3Vgm/dtr5dwAs6Xhg9PPPP8WDMgCmWjfsGhEKhVpFgiUMC9A242BMSkcWqjSlC"
			+ "iBH7g2PB6nWN02uBjuAkGb0hZ29/AgMBAAECgYBcD9AARwRzAbAFo/6za/bniHPc"
			+ "mi2KLoh5DtNABD2cY3c7pbeSYmUBBWAx7Cs5F4oqzzSyhXlgG7w+/+fOBlzPc3Iu"
			+ "3StobgL7MGQjtiSyH1+uHRQth/0tuhKpUa26p2mTvIMrZEhi68XbhEgH8fBHPEB3"
			+ "lL3PXQVUBdRTiZ9rMQJBAOXSNy2wzI+JhrgUfAKKxkQ2cks9h6eVmIOCzPBxm4Xr"
			+ "fzBoKwhIdXq7Iol3MPVm9whKeWrw/7XCKuxAp/63k50CQQDhkqzCTHoqZa7BtJdD"
			+ "2ekVOJq+cvPF8sHE4EQMXFsO8krx5EO+BdGWAs1tk9vMzFnrpu3PoUXp+FBIXASa"
			+ "skrLAkBRuJZ2YM/cmoKVInOWU9J8nv+1UYPTS5aX7QMC3OL4k+z+QYxLvbhIlBKL"
			+ "3x5BpQNip1jVdiz3bLvmlncmVBDhAkEAncDfMw8bDNWJ7wwmEZyvBllM1047L7+D"
			+ "RfV1gZOBVyWZxitWOOmG1rtlAXI7cLGlXUmj3OYzp9oyAzjfO3TfGwJAPFXsPiZH"
			+ "bH9tDZMzlGcYIy+zHDTj2VcLEu3sMnW1FdPxRAJR/3YwsxSEabrbYU8Scaji2yK+"
			+ "eBBfana5GkVfqw==";
	private String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDECuXFThOK1xy1X0y4iudvFjIe72orn8f33wJRHJ6pG+Cvt8UKqGiR/+Q2AHAq8O9P5zSJrt1A+J5CGKJ+TsQ5NgCW/KBpATAris28oT5xmWZzmTBkwbM5IXeAmaWmtvZcSY0EsThLLOInLMWuHifbkSU+K20iXXRMWVoN8c3dBQIDAQAB";

	public String authInit(String transactionId,String name, String no){
		ZhimaCustomerCertificationInitializeRequest request = new ZhimaCustomerCertificationInitializeRequest();
		request.setPlatform("zmop");
		request.setMerchantConfig("{\"need_user_authorization\":\"true\"}");
		request.setTransactionId(transactionId);// 必要参数
		request.setProductCode("w1010100000000002978");// 必要参数
		request.setBizCode("FACE");// 必要参数
		request.setIdentityParam("{\"identity_type\":\"CERT_INFO\",\"cert_type\":\"IDENTITY_CARD\","
				+ "\"cert_name\":\""+name+"\",\"cert_no\":\""+no+"\"}");// 必要参数
		request.setExtBizParam("{}");// 必要参数
		System.out.println(pub);
		DefaultZhimaClient client = new DefaultZhimaClient(
		    "https://zmopenapi.zmxy.com.cn/openapi.do",
		    appId,
		    pri,
		    pub);
		try {
		    ZhimaCustomerCertificationInitializeResponse response = (ZhimaCustomerCertificationInitializeResponse) client
		        .execute(request);
//		    System.out.println(response.isSuccess());
//		    System.out.println(response.getBizNo());
//		    System.out.println(response.getBody());
//		    System.out.println(response.getErrorCode());
//		    System.out.println(response.getErrorMessage());
		    return response.getBizNo();
		} catch (ZhimaApiException e) {
		    e.printStackTrace();
		}
		return  null;
	}
	public String getAuthUrl(String bizno,int ptype){
		ZhimaCustomerCertificationCertifyRequest request = new ZhimaCustomerCertificationCertifyRequest();
		request.setPlatform("zmop");
		request.setBizNo(bizno);// 必要参数
		// 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议
		// alipay://www.taobao.com 或者 alipays://www.taobao.com,分别对应http和https请求
		if(ptype == 2){
			request.setReturnUrl("bblq://bblanqiu.com/start.html?android=true");// 必要参数
		}else{
			request.setReturnUrl("bblaappbybb://bblanqiu.com");// 必要参数
		}
		
		DefaultZhimaClient client = new DefaultZhimaClient(
		    "https://zmopenapi.zmxy.com.cn/openapi.do", appId, pri,
		    pub);
		try {
		    String url = client.generatePageRedirectInvokeUrl(request);
		    System.out.println("generateCertifyUrl url:" + url);
		    return url;
		} catch (ZhimaApiException e) {
		    e.printStackTrace();
		}
		return null;
	}
	public AuthResultBean certQuery(String bizNo){
		AuthResultBean  arb = new AuthResultBean();
		ZhimaCustomerCertificationQueryRequest req = new ZhimaCustomerCertificationQueryRequest();
		req.setPlatform("zmop");
		req.setBizNo(bizNo);// 必要参数
		DefaultZhimaClient client = new DefaultZhimaClient(
		        "https://zmopenapi.zmxy.com.cn/openapi.do", "1002845", pri,
		        pub);
		try {
		    ZhimaCustomerCertificationQueryResponse response =(ZhimaCustomerCertificationQueryResponse)client.execute(req);
//		    System.out.println(response.isSuccess());
//		    System.out.println(response.getIdentityInfo());
//		    System.out.println(response.getErrorCode());
//		    System.out.println(response.getErrorMessage());
//		    System.out.println(response.getBody());
//		    System.out.println(response.getParams());
		    arb.setPassed(Boolean.valueOf(response.getPassed()));
		    arb.setReason(response.getFailedReason());
		} catch (ZhimaApiException e) {
		    e.printStackTrace();
		}
		return arb;
	}
	public String zhimaAuthInfoReq(String name, String no) throws JsonProcessingException {
		ZhimaAuthInfoAuthqueryRequest req = new ZhimaAuthInfoAuthqueryRequest();
		req.setIdentityType("2");// 必要参数      
		req.setIdentityParam("{\"certNo\":\""+no+"\",\"certType\":\"IDENTITY_CARD\",\"name\":\""+name+"\"}");// 必要参数        
		DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, pri, pub);
		String openid = null;
		try {
			ZhimaAuthInfoAuthqueryResponse response = client.execute(req);
//			System.out.println(response.getBody());
//			System.out.println(response.getParams());
//			System.out.println(response.getOpenId());
//			System.out.println(response.getErrorCode());
//			System.out.println(response.getErrorMessage());
			openid = response.getOpenId();
		} catch (ZhimaApiException e) {
			e.printStackTrace();
		}
		return openid;
	}
	public AliZhimaScoreBean getZhimaCreditScore(String transid, String openid){
		ZhimaCreditScoreGetRequest req = new ZhimaCreditScoreGetRequest();
        req.setChannel("apppc");
        req.setPlatform("zmop");
        req.setTransactionId(transid);// 必要参数 
        req.setProductCode("w1010100100000000001");// 必要参数 
        req.setOpenId(openid);// 必要参数 
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, pri, pub);
        AliZhimaScoreBean azsb = null;
        try {
            ZhimaCreditScoreGetResponse response =(ZhimaCreditScoreGetResponse)client.execute(req);
//            System.out.println(response.isSuccess());
//            System.out.println(response.getErrorCode());
//            System.out.println(response.getErrorMessage());
            azsb = new AliZhimaScoreBean();
            azsb.setBizNo(response.getBizNo());
            azsb.setScore(response.getZmScore());
        } catch (ZhimaApiException e) {
            e.printStackTrace();
        }
        return azsb;
	}
	
	private String payAppId = "2017051407234908";
	private String sindbox = "2016080700189838";
	private String payPub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnGEnHVg6Ddt/oC"
			+ "gmbVnAiPOE9DVDmV3H2/Z3dz6xwmgpmPBuYgCLfZi2QcJTgpUu5E4BrjNLBZJRjO1KlePAiy"
			+ "qbKBxbBH7PKqQFXMPzF29Rkbt0YjTX35bdhzNH+dU5dpKSh+6omRz6iM+UInukRg9J//z6nH"
			+ "mlElDR699BUnR8oO8zkaq3aBQwnyi9DiyAOFQ3zoYoPORvK1Jg2JVFpSr1EcrVaiBrdqrb+W"
			+ "2k+XHe6KHWVd0GFWiZ8K7m1OXPkA8pZrCBj595sfFwtZxYBNe/oD3a+f20yGlZGl4+gOF3Gk"
			+ "bN4JLxQRna/CBn0eoLx4JL4ArA0Dj33mpXT4oVyQIDAQAB";
	private String sindPub = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7H8Jb2ExQtTKGhmpJ3cIH3+lx3vrVcQYZhyGRjcA2toB5H33hwU333u9kaL4HgsUD4zUYVDB5qybEfd43En6U7y/1wtVlNfN5GeZdwkxBkcXuFbAwKAznW6F7+znxcAPXoFQWDTL8/BuBpOtXqh0Wx/rhjTChO1Sq3KcHNWknFxEf90/nmNGTSNmK3a3ydBkd/gBnaQQaOAnoujAPSl9OL3L/TPnZt2eUyDSD7+fqNkL/Ls8ep/w0TroDF7wjqPalrPongBkA9NA2+jYvp3ErloS11F2jo3J0VRg9QP6kLe9t9Uj3ibSqnImyDLIaHAWmgbYeCeRbr55VCFgCIvmSwIDAQAB";
	private String payPri = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWdvqmE1"
			+ "rIqbdrgorqeq/k+GpUEI8ybGz92YNyu0YhZ0Jote8HYOB4hnPwof4xCXpjV+c7xJ9uEq2D9j"
			+ "w9xcwdRjk/l40fxX/1B8f6u2KFDsiiIJ3uKugxYgXG4Dn+7z4nMIl+i9QcBmJtsBj5vbcwH5"
			+ "LEwl76NcNXHSAyg6fxd6Uf5vONhfQ0WM/XwPtGr/as2F5v6LcyHPWUitB3SrhZdsVqefXOfH"
			+ "R37OpLz/k1k7dC1ORLJMciBm2UxSvqhKJa/ng/exXw03SFcfDAyQhHgzcKpcDZdrbcMRkUBU"
			+ "1chdghDW8B1796E4mFY/QeyHVXPvrqZEMQ0jkEBtA5BVupAgMBAAECggEACzqn7BsHrHAWEz"
			+ "zz1xn+5IyxisKNnUrAO5WvHLHjQvzs1Nm3FArj1brbDjtYuBon+yAFTs2WxGU6c929kzKe0B"
			+ "R8k0VhL9zSdtv0zb6MVLfMu2TJxIuJNp5FJhl32pHKRqx1pm8TDmBfmiYJRyIkeL/yeSK0ae"
			+ "H3SRoQXBS9ROT2rqn+FxBuHEx/KDR+sHWbD40QOJOQVDrYQ0R+T/sry9v+nqmOK+72pGTa+T"
			+ "TAQi9veqSNkMDTp92XgAZaCN2f7W6aKJBRxD03VCuDzGOO3U9uL1R0ExPcQm9WIVlWjyJF7I"
			+ "QyVbkAdHo8hLzT2AkUTrJ+7Pll6huPUdfWC7uEXQKBgQDHHVLPN3ZrbCsGFPpjr9w2h5ja9H"
			+ "CqOp5UTW1wuPOdiygbK79A46s1dfM4/Yu3RHcqDvhSS+M4NPNBGyn1RdddBeiWD2nxL76lla"
			+ "oSFXr2xlt/hriG3scX37EepltNG8WiqqubbRv256KqSvsrtxVW2q2yNua4XfIZ3tdh+3xVjw"
			+ "KBgQDBc4wy77TsPn16yziLSppo7+f1f/tGSn+ZQ4QWzsAB/qEGVLQRbj0XQx0pHHPPr0hThM"
			+ "d3SYHql2Ex8EVsYtmChBzC+9uiPSFkjzg/ZoKbAuSkXv4p72I+TAbvqZ9UBragnZRKgQkiXT"
			+ "nqAwOujkdvha9mWpkvBep2eznMynXPRwKBgQCDqp9cyVc0V9Xncq3Uh7D+GhJ5D1UFLDgQlR"
			+ "a+iH8H+cIunXId7a8EbhfE4tyl9s8576ZDfU0mmNUWg2jueR2dtM+Fk3HB/Il8I4jP5oZmcu"
			+ "qEXMje3qgW4GPWWblcNvYsx+EsqSzpy2ikZWrMEIg64+YiR+2vkugKNXKFp3AjfwKBgQCOiT"
			+ "Sj0zrbVq/d6h6fgziCWUHwAXyDLiEWLEkHBQ80aQj8QSzsKu2YVjsGL5ClVeFUqGDP47l1wF"
			+ "Q9F2wPkEXzhKQshqYtxBz2mxA2prFUxazx74wWGeupOxthOvnKT6gAcBrzSmAQkeA6LD5qPy"
			+ "sjYLz8DYUQwKsg4WqVGhfzwQKBgFfbs1i/pkQ5tD80YIQKob3u0lGOQNqmzONR8GXWgWDi+3"
			+ "m+gb+ZXECmKt2TQtaVo/7M6L+2dWFBWbnhqInedcfg+LqpVx719RM1Ba6jI6HMzTJWpdAkh/"
			+ "eBhjZtAJNoaAWTxLqgGihS6M86ylpY6zxYtMSTKMh8mOW//DdV82pZ";
	public String getOrder(int charge,int userId, String outtradeno){		
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", payAppId, payPri, "json", "utf-8", payPub, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("八拜篮球充值测试");
		model.setSubject("八拜篮球/充值/"+userId+"/"+charge);
		model.setOutTradeNo(outtradeno);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(String.valueOf(charge));
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl("https://www.bblanqiu.com/bblq/alipay/charge/callback");
		try {
		        //这里和普通的接口调用不同，使用的是sdkExecute
		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
		        //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
		        return response.getBody();
		    } catch (AlipayApiException e) {
		        e.printStackTrace();
		}
		return null;
	}
	public boolean isPayed(Map requestParams) throws AlipayApiException{
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  }
		  //乱码解决，这段代码在出现乱码时使用。
		  //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		  params.put(name, valueStr);
		 }
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean flag = AlipaySignature.rsaCheckV1(params, payPub, "utf-8", "RSA2");
		return flag;
	}
	public Float aliTradeQuery(String tradeId){
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", payAppId, payPri, "json", "utf-8", payPub, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(tradeId);
		request.setBizModel(model);
		try {
			//这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeQueryResponse response = alipayClient.execute(request);
			if (response.isSuccess()) {
				// 调用成功，则处理业务逻辑
				if ("10000".equals(response.getCode())) {
					return Float.valueOf(response.getTotalAmount());
				}
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String []args) throws JsonProcessingException{
		AlipayService as = new AlipayService();
		/*String t = "bblanqu"+DateUtils.getTimeTag()+"13693463991";
		//as.getAuthUrl(as.authInit("bblanqu"+DateUtils.getTimeTag()+"13693463991","李康","511681198912061817"));
	System.out.println(as.certQuery("ZM201705143000000222200140056981"));*/
		
//		String tradeId = "bblqcharge"+DateUtils.getTimeTag() + "13693463991";
//		as.getOrder(1, 1, tradeId);
//		System.out.println(as.aliTradeQuery("bblqcharge2017052015052013693463991"));
		String t = "bblqxyf"+DateUtils.getTimeTag()+"13693463991";
		//as.getAuthUrl(as.authInit("bblanqu"+DateUtils.getTimeTag()+"13693463991","李康","511681198912061817"));
//		System.out.println(as.zhimaAuthInfoReq("胡益铭", "51052419910921001X"));;
		System.out.println(as.getZhimaCreditScore("bblanqusc20170602114818583243132","268817368405886856454181147"));
		///as.getZhimaCreditScore("268816231939676969685782895");
		//as.certQuery("ZM201706023000000555500217157644");
	}
}
