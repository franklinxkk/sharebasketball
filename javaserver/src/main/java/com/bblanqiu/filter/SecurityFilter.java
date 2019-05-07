package com.bblanqiu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bblanqiu.common.exception.Error;
import com.bblanqiu.common.exception.ErrorCode;
import com.bblanqiu.common.mysql.model.Token;
import com.bblanqiu.module.user.facade.TokenFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
public class SecurityFilter implements Filter{
	@Autowired
	private TokenFacade tokenFacade;
	/*@Autowired
	private AdminFacade adminFacade;*/
	@Autowired
	private ObjectMapper mapper;
	
	private static Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		synchronized (request) {
			HttpServletRequest hsr = (HttpServletRequest)request;
			String apiName = hsr.getRequestURI();
			boolean isSecurity = false;
			if(apiName.contains("admin")){
				HttpSession hs= hsr.getSession(true);      
				Token t = (Token)hs.getAttribute("token"); 
				if(t != null){
					isSecurity =true;
				}else{
					if(apiName.contains("login") 
							|| apiName.contains("logout")){
						isSecurity =true; 
					}else{
						request.getRequestDispatcher("/admin/login").forward(request, response);                
						return;  
					}
				}
			}else{
				String token = request.getParameter("token");
				if(apiName.contains("login") 
						|| apiName.contains("logout") 
						|| apiName.contains("register")
						|| apiName.contains("version")
						|| apiName.contains("reset-password")
						|| apiName.contains("get-phone-captcha")
						|| apiName.contains("/auth/")
						|| apiName.contains("mqtt")
						|| apiName.contains("push")
						|| apiName.contains("device/init")
						|| apiName.contains("alipay/charge/callback")
						|| apiName.contains("wxpay/charge/callback")
						|| apiName.contains("/alipay/addinfo")){
					isSecurity = true;
				}else if(token != null){
					Token t;
					try {
						t = tokenFacade.getToken(token);
					} catch (Exception e) {
						e.printStackTrace();
						response.getWriter().write(mapper.writeValueAsString(new Error(apiName, ErrorCode.TOKEN_ERROR, token)));
						return;
					}
					if(t != null){
						request.setAttribute("X-TOKEN", t);
						isSecurity = true;
					}
				}
			}
			if(!isSecurity){
				String result = mapper.writeValueAsString(new Error(apiName, ErrorCode.USER_LOGIN_REQUIRED));
				response.getWriter().write(result);
				return;
			}
			chain.doFilter(request, response); 
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
