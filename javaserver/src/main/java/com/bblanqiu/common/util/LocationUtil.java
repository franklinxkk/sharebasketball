package com.bblanqiu.common.util;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.helper.StringUtil;
public class LocationUtil {
	// 以一个基点，来展开的半径，默认2米
		public static final int RADIUS = 1;
		// 地球半径
		public static final int EARTH_RADIUS = 6378137;
		
		//百度地图key
		public static final String BAIDUMAPKEY ="rM8HbXDhaPyKypOpDjTjdozQ";
		
	public static Map<String, String> getLocation(Double latitude, Double longitude) {
		String url = "http://api.map.baidu.com/geocoder?callback=renderReverse&location="+latitude+","+longitude
				+"&output=json&ak="+BAIDUMAPKEY;
		String str = HttpConnect.get(url);
		Map<String, String> map = null;
		if (!StringUtil.isBlank(str)) {
			map = new HashMap<String, String>();
			JSONObject rootObject = null;
			JSONObject jsonObject = null;
			try {
				rootObject = new JSONObject(str);
				jsonObject = rootObject.getJSONObject("result").getJSONObject("addressComponent");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			String province = null;
			String city = null;
			String district = null;
			String business = null;
			try {
				province = jsonObject.getString("province");
				map.put("province", province);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("province", "");
			}
			try {
				city = jsonObject.getString("city");
				map.put("city", city);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("city", "");
			}
			try {
				district = jsonObject.getString("district");
				map.put("district", district);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("district", "");
			}
		}
		return map;
	}
	public static void main(String []args){
		System.out.println(getLocation(39.709266,117.518484));
	}
}
