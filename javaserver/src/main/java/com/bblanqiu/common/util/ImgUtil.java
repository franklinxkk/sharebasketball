package com.bblanqiu.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImgUtil {
	public static String getImageByNumber(Integer number, String image){
		String url = "";
		if(image != null){
			image = formatUrls(image);
			if(image.contains(" ")){
				List<String> list = Arrays.asList(image.split(" "));
				if(number == null || number < 0 || number > list.size() - 1){
					url = list.get(0);
				}else{
					url = list.get(number);
				}
			}else{
				url = image;
			}
		}
		return url;
	}
	public static String formatUrls(String urls){
		if(urls != null){
			urls = urls.trim();
		}else{
			urls = "";
		}
		return urls;
	}
	public static List<String> getImages(String image){
		List<String> images = new ArrayList<String>();
		if(image != null){
			image = image.trim();
			if(image.contains(" ")){
				images.addAll(Arrays.asList(image.split(" ")));
			}else{
				images.add(image);
			}
		}
		return images;
	}
}
