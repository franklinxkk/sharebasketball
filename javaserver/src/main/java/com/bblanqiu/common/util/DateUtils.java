package com.bblanqiu.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期格式化类
 *
 * @author franklin.li
 */
public class DateUtils {

	private static  final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat longSdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final Integer dayGap = 60*60*24*1000;

	public static Date getTargetDate(int t){//-1 昨天
		Calendar   cal   =   Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		cal.add(Calendar.DATE,   t);
		return cal.getTime();
	}
	/**
	 * 15个字符
	 * @return
	 */
	public static String getTimeTag(){
		DateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
		TimeZone zone = TimeZone.getDefault();
		format.setTimeZone(zone);
		return format.format(System.currentTimeMillis());
	}
    /**
     * Adds or subtracts the specified amount of time to the given calendar field,
     * based on the calendar's rules. For example, to subtract 5 days from
     * the current time of the calendar, you can achieve it by calling:
     * <p><code>add(Calendar.DAY_OF_MONTH, -5)</code>.
     *
     * @param field the calendar field.
     * @param amount the amount of date or time to be added to the field.
     */
	public static Date getTargetTime(int field, int t){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(field,   t);
		return cal.getTime();
	}
	
	/**
	 * 获得当天的开始时间
	 * 
	 */
	public static Date getDateStartTime(Date now) {
		try {
			now = shortSdf.parse(shortSdf.format(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得当天的结束时间
	 * 
	 */
	public static Date getDateEndTime(Date now) {
		try {
			now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	
	/**
	 * 获得本天的开始时间
	 * 
	 */
	public  Date getCurrentDayStartTime() {
		Date now = new Date();
		try {
			now = shortSdf.parse(shortSdf.format(now));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得本天的结束时间
	 * 
	 */
	public Date getCurrentDayEndTime() {
		Date now = new Date();
		try {
			now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 获得本月的开始时间，
	 * 
	 * @return
	 */
	public Date getCurrentMonthStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前月的结束时间，
	 * 
	 * @return
	 */
	public Date getCurrentMonthEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前年的开始时间，
	 * 
	 * @return
	 */
	public Date getCurrentYearStartTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DATE, 1);
			now = shortSdf.parse(shortSdf.format(c.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}

	/**
	 * 当前年的结束时间，
	 * 
	 * @return
	 */
	public Date getCurrentYearEndTime() {
		Calendar c = Calendar.getInstance();
		Date now = null;
		try {
			c.set(Calendar.MONTH, 11);
			c.set(Calendar.DATE, 31);
			now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return now;
	}
	public static long getUTC() {
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT");
		calendar.setTimeZone(tz);
		return calendar.getTimeInMillis();
	}

	public static String dateFormat(long time, String ID) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone zone = TimeZone.getTimeZone(ID);
		format.setTimeZone(zone);
		return format.format(time);
	}

	public static String dateFormat(long time) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TimeZone zone = TimeZone.getDefault();
		format.setTimeZone(zone);
		return format.format(time);
	}

	public static String dateFormat(long time, String ID, String format) {
		DateFormat df = new SimpleDateFormat(format);
		TimeZone zone = TimeZone.getTimeZone(ID);
		df.setTimeZone(zone);
		return df.format(time);
	}

	public static int getDateByUTC(long utc) {
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT");
		calendar.setTimeZone(tz);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 
	 * @param date
	 * @param num 负数减，正数加
	 * @return
	 */
	public static Date changeYear(Date date, int num){
		Calendar cal = Calendar.getInstance();
		if(date == null){
			cal.setTime(new Date());
		}else{
			cal.setTime(date);
		}
		cal.add(Calendar.YEAR,-num);
		return cal.getTime();
	}
	
	/**
	 * 
	 * @param date
	 * @param num 负数减，正数加
	 * @return
	 */
	public static Date changeSecond(Date date, int num){
		Calendar cal = Calendar.getInstance();
		if(date == null){
			cal.setTime(new Date());
		}else{
			cal.setTime(date);
		}
		cal.add(Calendar.SECOND,num);
		return cal.getTime();
	}
	public static int getYear(Date date){
		Calendar c = Calendar.getInstance();
		if(date == null){
			c.setTime(new Date());
		}else{
			c.setTime(date);
		}
		return c.get(Calendar.YEAR);
	}
	
	public static int getDay(Date date){
		Calendar c = Calendar.getInstance();
		if(date == null){
			c.setTime(new Date());
		}else{
			c.setTime(date);
		}
		return c.get(Calendar.DAY_OF_YEAR);
	}
	
	public static int getYearGap(Date startDate, Date endDate){
		return getYear(endDate) - getYear(startDate);
	}
	
	public static int getDayGap(Date startDate, Date endDate){
		return getDay(endDate) - getDay(startDate);
	}
	
	public static int getTimeGapByDay(Date startDate, Date endDate){
		int gap = 365;
		if(startDate != null){
			if(endDate == null){
				endDate = new Date();
			}
			long i = endDate.getTime() - startDate.getTime();
			i = i / (86400000);
			return (int)i;
		}
		return gap;
	}
	public static int getTimeGapByMinute(Date startDate, Date endDate){
		int gap = 1;
		if(startDate != null){
			if(endDate == null){
				endDate = new Date();
			}
			long i = endDate.getTime() - startDate.getTime();
			double d = i / (60000);
			if(d < 1){
				return 1;
			}else{
				return (int)d;
			}
		}
		return gap;
	}
	
	public static int getHourGap(Date startDate, Date endDate){
		return getGap(startDate, endDate, 3600000);
	}
	
	public static int getSecondGap(Date startDate, Date endDate){
		return getGap(startDate, endDate, 1000);
	}
	
	public static int getGap(Date startDate, Date endDate, int gapType){
		int gap = 0;
		if(endDate == null){
			endDate = new Date();
		}
		if(startDate == null){
			gap = 0;
		}else{
			Long h = endDate.getTime() - startDate.getTime();
			gap = (int)(h / (gapType));
		}
		return gap;
	}
	
	public static boolean isMoreThanAday(Date startDate, Date endDate){
		Long gap = getUTC(endDate) - getUTC(startDate);
		System.out.println(gap);
		if(gap > dayGap){
			return true;
		}else{
			return false;
		}
	}
	
	public static String formatDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	public static String formatHMS(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}
	public static Date formatDate(String dateString) throws ParseException{
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	    return sdf.parse(dateString);
	}
	public static Date formatHMS(String dateString) throws ParseException{
	    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
	    return sdf.parse(dateString);
	}
	
	public static String formatTimestamp(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}
	public static String formatTimestampUpper(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH24:MI:SS");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}
	public static Date formatTimestamp(String dateString) throws ParseException{
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    TimeZone.getTimeZone("GMT+8");
	    return sdf.parse(dateString);
	}
	public static Long getUTC(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}
	public static Date getDate(){
		TimeZone time = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(time);// 设置时区

		Calendar calendar = Calendar.getInstance();// 获取实例
		Date date = calendar.getTime();
		return date;
	}
	public static String getConstellation(Integer month,Integer day) 
	{    
		String s="魔羯座水瓶座双鱼座牡羊座金牛座双子座巨蟹座狮子座处女座天秤座天蝎座射手座魔羯座"; 
		Integer[] arr={20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};   
		Integer num = month * 3 - ( day < arr[ month - 1 ] ? 3 : 0); 
		return s.substring(num, num + 3);    
	}
	public static String getConstellation(String key) 
    { 
        Integer month=Integer.valueOf(key.substring(5,7)); 
        Integer day = Integer.valueOf(key.substring(8,10));	
        return getConstellation(month,day);  
    } 
	public static String getDate2Now(Date date){
		long l=getUTC()-date.getTime();
		long week = 1/(7*24*60*60*1000);
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		long s=(l/1000-day*24*60*60-hour*60*60-min*60);
		StringBuffer sb = new StringBuffer();
		if(week > 0){
			sb.append(formatTimestamp(date));
		}else if(day > 0){
			sb.append(day+"天前");
		}else if(hour > 0 ){
			sb.append(hour+"小时前");
		}else if(min > 0 ){
			sb.append(min+"分前");
		}else{
			sb.append(s+"秒前");
		}
		return sb.toString();
	}
	public static Date getDate(long time){
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
		calendar.setTimeInMillis(time);
		return calendar.getTime();
	}
	public static Date get1970Date()throws ParseException{
		return formatTimestamp("1970-01-01 00:00:00");
	}
	
	public static boolean isNight(int region){
		boolean isNight = false;
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = null;
		if(region == 1){
			tz = TimeZone.getTimeZone("Asia/Shanghai");
		}else{
			tz = TimeZone.getTimeZone("America/Los_Angeles");
		}
		calendar.setTimeZone(tz);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour >= 18 || hour <= 7){
			isNight = true;
		}
		return isNight;
	}
	
	public static int getHourOfDay(Date date){
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getHourOfDay(int region, Date date){
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = null;
		if(region == 1){
			tz = TimeZone.getDefault();
		}else{
			tz = TimeZone.getTimeZone("America/Los_Angeles");
		}
		calendar.setTimeZone(tz);
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * 午夜凌晨 休眠期 23-4
	 * @return
	 */
	public static boolean isMidnight(Integer region){
		boolean isMidnight = false;
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = null;
		if(region == null){
			region = 1;
		}
		if(region == 1){
			tz = TimeZone.getDefault();
		}else{
			tz = TimeZone.getTimeZone("America/Los_Angeles");
		}
		calendar.setTimeZone(tz);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour >= 24 || hour <= 3){
			isMidnight = true;
		}
		return isMidnight;
	}
	
	public static boolean canWatering(){
		boolean can = false;
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getDefault();
		calendar.setTimeZone(tz);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour >= 17 && hour <= 20){
			can = true;
		}else if(hour == 12){
			can = true;
		}else if(hour >= 8 && hour <= 10){
			can =true;
		}
		return can;
	}
	public static boolean canWatering(int region){
		boolean can = false;
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = null;
		if(region == 1){
			tz = TimeZone.getDefault();
		}else{
			return true;
			//tz = TimeZone.getTimeZone("America/Los_Angeles");
		}
		
		calendar.setTimeZone(tz);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if(hour >= 17 && hour <= 21){
			can = true;
		}else if(hour == 12){
			can = true;
		}else if(hour >= 7 && hour <= 10){
			can =true;
		}
		return can;
	}

	public static void main(String []args)throws Exception{
		Date d = new Date();
		//System.out.println(isNight());;
		
//		System.out.println(formatDate(d));
//		System.out.println(getDayGap(formatDate("2015-10-10"), d));
		Date s = get1970Date();
//		System.out.println(getTimeGapByMinute(null,formatTimestamp("2016-4-20 23:40:40")));
/*		TimeZone time = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(time);// 设置时区
		Date yesterday = DateUtils.getTargetDate(-1);
		Date beforeYesterday = DateUtils.getTargetDate(-2);
		Date start = DateUtils.getDateStartTime(beforeYesterday);
		Date end = DateUtils.getDateEndTime(yesterday);
		System.out.println(start);
		System.out.println(end);
		System.out.println(DateUtils.getDateStartTime(yesterday));*/
		int minute = DateUtils.getTimeGapByMinute(
				formatTimestamp("2017-05-14 12:51:01"), 
				formatTimestamp("2017-05-14 12:52:33"));
		System.out.println(CostUtil.getCost(minute)+"|"+minute);
		//System.out.println(getTimeTag());
	}
    public static String getIntervalDimension(int language, String dimension) {
        if (language == 1) {
            if (dimension.toLowerCase().equals("d")) {
                return "天";
            } else if (dimension.toLowerCase().equals("h")) {
                return "小时";
            } else if (dimension.toLowerCase().equals("m")) {
                return "分钟";
            } else if (dimension.toLowerCase().equals("s")) {
                return "秒";
            }
        } else {
            if (dimension.toLowerCase().equals("d")) {
                return "d";
            } else if (dimension.toLowerCase().equals("h")) {
                return "h";
            } else if (dimension.toLowerCase().equals("m")) {
                return "m";
            } else if (dimension.toLowerCase().equals("s")) {
                return "s";
            }
        }
        return null;
    }

    public static String formatInterval(int language, long seconds) {
        String resultStr = "";
        if (seconds < 60) {
            resultStr = seconds + getIntervalDimension(language, "s");
        } else if (seconds >= 60 && seconds < 3600) {
            long remainM = seconds % (60 * 60) / 60;
            resultStr += remainM + getIntervalDimension(language, "m");
            resultStr += seconds % 60 + getIntervalDimension(language, "s");
        } else if (seconds >= 3600 && seconds < 3600 * 24) {
            long remainH = seconds / (60 * 60);
            resultStr += remainH + getIntervalDimension(language, "h");
            long modH = seconds % (60 * 60);
            long remainM = 0;
            if (modH >= 60) {
                remainM = modH / 60;
            }
            resultStr += remainM + getIntervalDimension(language, "m");
            long modM = modH % 60;
            resultStr += modM + getIntervalDimension(language, "s");
        } else {
            long remainD = seconds / (60 * 60 * 24);
            resultStr += remainD + getIntervalDimension(language, "d");
            long modD = seconds % (60 * 60 * 24);
            long remainH = 0;
            if (modD >= 60 * 60) {
                remainH = modD / (60 * 60);
            }
            resultStr += remainH + getIntervalDimension(language, "h");
            long modM = modD % (60 * 60);
            long remainM = 0;
            if (modM >= 60) {
                remainM = modM / 60;
            }
            resultStr += remainM + getIntervalDimension(language, "m");
            long modS = modM % 60;
            resultStr += modS + getIntervalDimension(language, "s");
        }
        return resultStr;
    }
}
