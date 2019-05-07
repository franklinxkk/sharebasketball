package com.bblanqiu.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.bblanqiu.common.util.HttpConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserTest {
	private static HttpClient httpclient;
	static {
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager();
		// 设置最大并发连接数
		manager.setMaxTotal(100);
		httpclient = new DefaultHttpClient(manager, params);
	}
	public static void main(String []args) throws Exception{
		feedback();
	}
	public static void feedback() throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("content", "反馈测试，你们的appbug太多啦，赶紧升级啊！我擦 ");
		String url = "http://localhost:8080/v2/user/feedback?token=d7fa09261c911b83a1fc577383a24b9c";
		ObjectMapper mapper = new ObjectMapper();
		String json =  mapper.writeValueAsString(map);
		System.out.println(json);
		System.out.println(stream2String(postMethod(url, null,json)));
	}
	public static void upload() throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("cpValue", "500");
		map.put("nValue", "12");
		map.put("type", 0);
		map.put("longitude", 116.518484d);
		map.put("latitude", 39.709266);
		map.put("temp", 30.5);
		map.put("envid", 1);
		map.put("envname", "室内");
		map.put("continueTime", 300);
		map.put("remark", "辐射测试"+System.currentTimeMillis());
		map.put("deviceVersion", "v1.2");
		map.put("mac", "34:23:87:F4:A4:38");
		map.put("userId", 1);
		String url = "http://localhost:8080/v2/data/cpupload?token=d7fa09261c911b83a1fc577383a24b9c";
		ObjectMapper mapper = new ObjectMapper();
		String json =  mapper.writeValueAsString(map);
		System.out.println(json);
		System.out.println(stream2String(postMethod(url, null,json)));
	}
	public static void login()throws JsonProcessingException, IOException{
		Map<String, String> map = new HashMap<>();
		map.put("clientId", "47327d3df06df0b4");map.put("phone", "13693463991");map.put("captcha", "0945");
		String url = "http://localhost:8080/v2/user/login";
		ObjectMapper mapper = new ObjectMapper();
		String json =  mapper.writeValueAsString(map);
		System.out.println(json);
		System.out.println(stream2String(postMethod(url, null,json)));
	}
    public static String stream2String(InputStream in) {
        if (in != null) {
            try {
                byte[] b = new byte[1024];
                int size;
                StringBuffer sb = new StringBuffer();
                while ((size = in.read(b)) > 0) {
                    sb.append(new String(b, 0, size, "UTF-8"));
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
	public static InputStream postMethod(String url, Map<String, String> headers, String json) throws IOException {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            //headers
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestMethod("POST");
            if (headers != null) {
                Set<String> set = headers.keySet();
                Iterator<String> iter = set.iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    con.setRequestProperty(key, headers.get(key));
                }
            }
            //body
            con.setDoOutput(true);
            con.setDoInput(true);
            if (json != null) {
                OutputStream os = con.getOutputStream();
                os.write(json.getBytes("utf-8"));
                os.flush();
                os.close();
            }

            if (con.getResponseCode() == 200) {
                return con.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	public static String post(String url, Map<String, String> map) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
			NameValuePair nvp = new BasicNameValuePair(key, map.get(key));
			nvps.add(nvp);
		}
		String html = null;
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(nvps, "utf8"));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK
					&& httpEntity != null) {
				html = EntityUtils.toString(httpEntity, "utf8");
				EntityUtils.consume(httpEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
}
