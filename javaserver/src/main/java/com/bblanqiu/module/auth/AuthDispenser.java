//package com.bblanqiu.module.auth;
//
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.management.relation.RoleInfo;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.collections.MapUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import com.bblanqiu.common.exception.ErrorCode;
//import com.bblanqiu.common.exception.ErrorCodeException;
//import com.bblanqiu.common.mysql.model.Token;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//@Component
//public class AuthDispenser {
//	private String PARAM_ACCESS_TOKEN = "access_token";
//	@SuppressWarnings("rawtypes")
//	@Autowired
//	RedisTemplate redisTemplate;
//	@Autowired
//	AuthValidater authValidater;
//	@Autowired
//	ObjectMapper mapper;
//
//	public String getMethod(HttpServletRequest request){
//		return request.getMethod();
//	}
//	public String getApiName(HttpServletRequest request){
//		return request.getRequestURI();
//	}
//	public String getToken(HttpServletRequest request){
//		return request.getParameter(PARAM_ACCESS_TOKEN);
//	}
//	@SuppressWarnings("rawtypes")
//	public Map getParameter(HttpServletRequest request){
//		return request.getParameterMap();
//	}
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public Map getHeader(HttpServletRequest request){
//		Map map = new HashMap<>();
//		Enumeration<String> e = request.getHeaderNames();
//		while(e.hasMoreElements()){
//			String name = e.nextElement();
//			map.put(e.nextElement(), request.getHeader(name));
//		}
//		return map;
//	}
//	public String getAuthURI(String apiName){
//		return apiName;
//	}
//	public void authDispenser(HttpServletRequest request){
//		String authUri = getAuthURI(getApiName(request));
//		String httpMethod = getMethod(request);
//		String access_token = getToken(request);
//		Integer permissionId = null;//apiService.getApiPermissionIdByName(authUri+"&"+httpMethod.toLowerCase());
//		if(permissionId != 0){
//			Token token = authValidater.getToken(access_token);
//			request.getSession().setAttribute("token", token);
//			authPermission(token, permissionId);
//		}
//	}
//
//	/**
//	 * has the access permit to the API
//	 * @param token
//	 * @param permissionId
//	 */
//	@SuppressWarnings("rawtypes")
//	public void authPermission(Token token, Integer permissionId){
//		Map<String, Object> permissionMap = null;
//		Integer roleType = token.getType();
//		if(roleType < RoleInfo.NORMAL){
//			permissionMap = apiService.getUnloginPermissionMap();
//		}else if(roleType < RoleInfo.COMPANY){
//			permissionMap = apiService.getNormalPermissionMap();
//		}else if(roleType < RoleInfo.SYSTEM){
//			permissionMap = apiService.getCompanyPermissionMap();
//		}else{
//			permissionMap = apiService.getAdminPermissionMap();
//		}
//
//		boolean freezed = reviewFacade.isUserFreeze(token.getUser());
//		if(freezed && (permissionId % 2 == 0)){
//			throw new ErrorCodeException(new Error(ErrorCode.USER_WRITE_PERMISSION_FREEZED));
//		}
//
//		List accept = (List)permissionMap.get("accept");
//		List deny = (List)permissionMap.get("deny");
//		String defaultPrivilege = MapUtils.getString(permissionMap, "default", "readOnly");
//		boolean hasprivilege = false;
//		if(accept.contains(permissionId)){
//			hasprivilege = true;
//		}else if(defaultPrivilege.equalsIgnoreCase("all")){
//			if(!deny.contains(permissionMap)){
//				hasprivilege = true;
//			}
//		}else if(defaultPrivilege.equalsIgnoreCase("readOnly") && (permissionId % 2 == 1)){
//			hasprivilege = true;
//		}
//		if(!hasprivilege){
//			throw new ErrorCodeException(ErrorCode.PERMISSION_DENIED);
//		}
//	}
//}