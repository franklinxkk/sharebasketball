package com.bblanqiu.module.auth;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

public class AccessTokenGenerator {
	
	private static AccessTokenGenerator tokenGenerator = new AccessTokenGenerator();
	
	public static AccessTokenGenerator getInstance(){
		return tokenGenerator;
	}
	
	private static OAuthIssuer oauthIssuer = new OAuthIssuerImpl(new MD5Generator());
	
	public String getAccessToken() throws OAuthSystemException{
		return oauthIssuer.accessToken();
	}
	
	public String getRefreshToken() throws OAuthSystemException{
		return oauthIssuer.refreshToken();
	}
}
