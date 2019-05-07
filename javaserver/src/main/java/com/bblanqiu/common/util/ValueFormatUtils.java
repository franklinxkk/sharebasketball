package com.bblanqiu.common.util;

import com.bblanqiu.module.admin.bean.DeviceBean;

public class ValueFormatUtils {
	public static void parseLocks(char[] locks,DeviceBean bb){
		try {
			if(bb != null){
				if(bb.getType() == null || bb.getType() == 1){
					bb.setB(Integer.parseInt(String.valueOf(locks[0])));
					bb.setS1(Integer.parseInt(String.valueOf(locks[1])));
					bb.setS2(Integer.parseInt(String.valueOf(locks[2])));
					bb.setS3(Integer.parseInt(String.valueOf(locks[3])));
					bb.setS4(Integer.parseInt(String.valueOf(locks[4])));
					bb.setS5(Integer.parseInt(String.valueOf(locks[5])));
					bb.setS6(Integer.parseInt(String.valueOf(locks[6])));
					bb.setS7(Integer.parseInt(String.valueOf(locks[7])));
					bb.setS8(Integer.parseInt(String.valueOf(locks[8])));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String []args){
		DeviceBean bb = new DeviceBean();
		char[] locks = "222222221".toCharArray();
		parseLocks(locks, bb);
		System.out.println(bb);
	}
    public static String coordFormat(Float f) {
        return coordFormat((double) f);
    }

    public static String coordFormat(Double d) {
        if (d != null) {
            d = Math.abs(d);
            StringBuffer sb = new StringBuffer();
            int degrees = d.intValue();
            if (degrees > 0) {
                sb.append(degrees).append("°");
            }
            double temp = (d - degrees) * 60;
            int minutes = new Double(temp).intValue();
            if (minutes > 0) {
                sb.append(minutes).append("'");
            }
            temp = (temp - minutes) * 60;
            int secondes = new Float(temp).intValue();
            if (secondes > 0) {
                sb.append(secondes).append("\"");
            }
            return sb.toString();
        }
        return null;
    }

    public static String getLatitude(Double d) {
        String result = "";
        if (d != null) {
            result = coordFormat(d);
            if (d >= 0) {
                result += "N";
            } else {
                result += "S";
            }
        }
        return result;
    }

    public static String getLongitude(Double d) {
        String result = "";
        if (d != null) {
            result = coordFormat(d);
            if (d >= 0) {
                result += "E";
            } else {
                result += "W";
            }

        }
        return result;
    }

    public static String getCoord(String coord) {
        String result = "";
        if (coord.contains(",")) {
            String[] strs = coord.split(",");
            result += getLongitude(Double.valueOf(strs[0]));
            result += getLatitude(Double.valueOf(strs[1]));
        }
        return result;
    }
}