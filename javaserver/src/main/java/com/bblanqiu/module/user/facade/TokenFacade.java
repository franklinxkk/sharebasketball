package com.bblanqiu.module.user.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.CacheTableNames;
import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.module.user.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TokenFacade extends RedisService{
	@Autowired
	TokenService tokenService;
	@Autowired
	ObjectMapper mapper;
	
	public Token getToken(String token) throws Exception{
		Token t = null;
		Object o = getValue(CacheTableNames.PREFIX_TOKEN + token);
		if(o != null){
			t = mapper.readValue(o.toString(), Token.class);
		}else{
			t = tokenService.getToken(token);
			if(t != null){
				addValue(CacheTableNames.PREFIX_TOKEN + token, mapper.writeValueAsString(t), null);
			}
		}
		return t;
	}
	public void addToken(Token token) throws Exception {
		addValue(CacheTableNames.PREFIX_TOKEN + token.getToken(), mapper.writeValueAsString(token), null);
		tokenService.addToken(token);
		//addValue(PREFIX_TOKEN + token.getToken(), mapper.writeValueAsString(token), null);
	}
	public void addToken(Token token, long timeout) throws Exception {
		addValue(CacheTableNames.PREFIX_TOKEN + token.getToken(), mapper.writeValueAsString(token), timeout);
		tokenService.addToken(token);
		//addValue(PREFIX_TOKEN + token.getToken(), mapper.writeValueAsString(token), null);
	}
	public void deleteToken(String token){
		del(CacheTableNames.PREFIX_TOKEN + token);
		tokenService.deleteToken(token);
	}
	public void updateTokenType(String token, int type){
		Token t = tokenService.getToken(token);
		t.setType(type);
		try {
			addValue(CacheTableNames.PREFIX_TOKEN + token, mapper.writeValueAsString(t), null);
			tokenService.updateToken(t);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
