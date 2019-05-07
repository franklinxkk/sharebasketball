package com.bblanqiu.module;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiTest {
	/**
     * @param url
     * @param headers http头
     * @param json    body体 json格式
     * @return
     * @throws IOException
     */
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
	public static void main(String []args) throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("clientId", "47327d3df06df0b4");map.put("phone", "18583243132");map.put("captcha", "72398");
		String json = new ObjectMapper().writeValueAsString(map);
		System.out.println(stream2String(postMethod("http://www.bblanqiu.com/bblq/user/login", null,json)));
	}
}
