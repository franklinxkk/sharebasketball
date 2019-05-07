package com.bblanqiu.common.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpConnect {
	public static CloseableHttpClient getClient(){
		return HttpClientBuilder.create().build();
	}
	public static String getRequest(String url, String defaultCharset) throws Exception{ 
		CloseableHttpClient client = getClient();
		try{  
			HttpGet get = new HttpGet(url);
			RequestConfig rc = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
			get.setConfig(rc);
			setHeaders(get);
			HttpResponse httpResponse = client.execute(get);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				String result = EntityUtils.toString(httpResponse.getEntity(), defaultCharset);  
				return result;  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			client.close();  
		}
		return null;  
	} 
	
	public static HttpGet getBrowserHttpGetRequest(String url, String referer){
		HttpGet get = new HttpGet(url);
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8;"); 
		get.setHeader("Accept-Encoding", "gzip,deflate,sdch"); 
		get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4"); 
		get.setHeader("Cache-Control", "max-age=0"); 
		get.setHeader("Connection", "keep-alive"); 
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36"); 
		get.setHeader("Referer", referer);
		return get;
	}
	public static String getBrowserRequest(String url, String referer)throws Exception{
		CloseableHttpClient client = HttpClientBuilder.create().build();
		String result = null;
		try{  
			HttpGet get = new HttpGet(url);
			get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8;"); 
			get.setHeader("Accept-Encoding", "gzip,deflate,sdch"); 
			get.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4"); 
			get.setHeader("Cache-Control", "max-age=0"); 
			get.setHeader("Connection", "keep-alive"); 
			get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36"); 
			get.setHeader("Referer", referer);
			RequestConfig rc = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();
			get.setConfig(rc);
			HttpResponse httpResponse = client.execute(get);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				result = EntityUtils.toString(httpResponse.getEntity()); 
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			client.close();  
		}
		return result;
	}
	
	public static String getRequest(String url, HttpGet get) throws Exception{ 
		CloseableHttpClient client = getClient();
		try{  
			HttpResponse httpResponse = client.execute(get);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				String result = EntityUtils.toString(httpResponse.getEntity());  
				return result;  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			client.close();  
		}
		return null;  
	}
	
	public static InputStream getRequestStream(String url) throws Exception{ 
		InputStream result = null;
		CloseableHttpClient client = getClient();
		try{  
			
			HttpGet get = new HttpGet(url);
			setHeaders(get);
			HttpResponse httpResponse = client.execute(get);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				result = httpResponse.getEntity().getContent();  
				return result;  
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			client.close();  
		} 
		return result;  
	}
	
	public static byte[] getRequestBytes(String url) throws Exception{ 
		byte[] bytes = null;
		CloseableHttpClient client = getClient();
		try{  
			HttpGet get = new HttpGet(url);
			setHeaders(get);
			HttpResponse httpResponse = client.execute(get);  
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
				InputStream result = httpResponse.getEntity().getContent();  
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
		        byte[] data = new byte[1024];  
		        int count = -1;  
		        while((count = result.read(data,0,1024)) != -1)  
		            outStream.write(data, 0, count);  
		          
		        data = null;  
		        bytes = outStream.toByteArray();   
			}  
		}catch(Exception e){  
			e.printStackTrace();  
		}finally{  
			client.close();  
		} 
		return bytes;  
	} 
	
	public static String get(String url){
		String result = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            //headers
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestMethod("GET");
            if (con.getResponseCode() == HttpStatus.SC_OK) {
            	InputStream in =  con.getInputStream();
            	byte[] b = new byte[1024];
                int size;
                StringBuffer sb = new StringBuffer();
                while ((size = in.read(b)) > 0) {
                    sb.append(new String(b, 0, size, "UTF-8"));
                }
                result =  sb.toString();
            }else{
            	System.out.println(con.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	
	public static void writeStream(InputStream is, OutputStream os)
			throws Exception {
		try {
			int size;
			byte[] b = new byte[2048];
			while ((size = is.read(b)) > 0) {
				os.write(b, 0, size);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static void setHeaders(HttpGet method) {
		method.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"); 
		method.setHeader("Accept-Encoding", "gzip,deflate,sdch"); 
		method.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4"); 
//		method.setHeader("Cache-Control", "max-age=0"); 
//		method.setHeader("Connection", "keep-alive"); 
//		method.setHeader("Content-Type","GB2312");
		method.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.103 Safari/537.36"); 
	} 
	public static void main(String []args){
		for(int i=0;i<100;i++){
			get("https://www.bblanqiu.com/indexx.html");
		}
	}
}
