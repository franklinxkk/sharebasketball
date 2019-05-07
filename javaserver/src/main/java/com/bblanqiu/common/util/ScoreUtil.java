package com.bblanqiu.common.util;

public class ScoreUtil {
	public static String getScoreName(Integer type){
		if(type == 1){
			return "用球完成";
		}else if(type == 2){
			return "储物完成";
		}else if(type == 101){
			return "上报设备故障";
		}else if(type == 102){
			return "举报成功";
		}else if(type == 201){
			return "违规行为";
		}else if(type == 202){
			return "忘关篮球箱，篮球找回";
		}else if(type == 203){
			return "忘关储物柜";
		}else if(type == 204){
			return "忘关篮球箱，篮球丢失";
		}else if(type == 205){
			return "违法行为";
		}else{
			return "";
		}
	}
	public static Integer getScore(Integer type){
		if(type == 1){
			return 1;
		}else if(type == 2){
			return 1;
		}else if(type == 101){
			return 2;
		}else if(type == 102){
			return 2;
		}else if(type == 201){
			return -20;
		}else if(type == 202){
			return -100;
		}else if(type == 203){
			return -100;
		}else if(type == 204){
			return Integer.MIN_VALUE;
		}else if(type == 205){
			return Integer.MIN_VALUE;
		}else{
			return 0;
		}
	}
}
