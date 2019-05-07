package com.bblanqiu.common.util;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bblanqiu.common.jackson.ObjectMapperFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MysqlHqlUtil {
	
	@SuppressWarnings("unchecked")
	public static String getSingleUpdateHql(Object o,String whereStr){
		StringBuffer sb = new StringBuffer();
		String hqlObjectName = "o";
		sb.append("update ");
		sb.append(o.getClass().getName()).append(" ");
		sb.append(hqlObjectName).append(" set ");
		ObjectMapper mapper = new ObjectMapperFactory().getMapper();
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		Map<String, Object> m = mapper.convertValue(o, Map.class);
		if(m != null && m.size() > 0){
			Iterator<String> i = m.keySet().iterator();
			Object id = null;
			while(i.hasNext()){
				String str = i.next();
				if(!str.equalsIgnoreCase(whereStr) && m.get(str) != null){
					sb.append(" ").append(hqlObjectName).append(".").append(str).append("='").append(m.get(str)).append("',");
				}else{
					id = m.get(str);
				}	
			}
			if(id == null){
				return null;
			}else{
				sb.deleteCharAt(sb.length() - 1);
				sb.append(" where ").append(hqlObjectName).append("."+whereStr).append("='").append(id).append("'");
				return sb.toString();
			}
		}else{
			return null;
		}
	}
	
	/**
	 * int array
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String list2String(List list){
		String s = "";
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				s += list.get(i);
				if( i < list.size() - 1){
					s += ",";
				}
			}
		}
		return s;
	}
	/**
	 * string array
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String list2String2(List list){
		String s = "";
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				s = "'" + list.get(i)+"'";
				if( i < list.size() - 1){
					s += ",";
				}
			}
		}
		return s;
	}
}
