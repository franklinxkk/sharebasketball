package com.bblanqiu.module.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.module.user.service.TokenService;

@Component
public class AuthValidater {
	@Autowired
	TokenService tokenService;
	private List<String> clientTokenList;
	private List<String> freeApis;
	
	public AuthValidater(){
		clientTokenList = new ArrayList<>();
		clientTokenList.add("0b1f1b1055c3144d");//ios client id
		clientTokenList.add("0264a94959afb8d8");//android client id
		clientTokenList.add("47327d3df06df0b4");//web client id
		clientTokenList.add("72a3792fecfc3463");//weixin
		clientTokenList.add("10e7a4297540e8de");//支付宝
	}
	
	/**
	 *
	 * @param clientId
	 * @return
	 */
	public boolean validateClient(String clientId){
		//:TODO 待后期加上clientsecret
		boolean isClientId = false;
		if(clientId != null){
			isClientId = clientTokenList.contains(clientId);
		}
		return isClientId;
	}
	
	public boolean validateUserToken(String token){
		//:TODO 待写入缓存，以及待加入权限控制
		boolean validated = false;
		if(tokenService.getToken(token) != null){
			validated = true;
		}
		return validated;
	}
}
