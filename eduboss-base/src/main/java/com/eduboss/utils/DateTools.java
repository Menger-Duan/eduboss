package com.eduboss.utils;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * -------------History------------------
 * DATE        AUTHOR        VERSION        DESCRIPTION
 * 2013-03-14  chenguiban    V01            �½��ĵ�
 * </pre>
 * 
 * @author <a href="chenguiban@revenco.com">�¹��</a>
 */
public class DateTools {

	private static  Logger logger = Logger.getLogger(DateTools.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * ��õ�ǰʱ�� ���ڸ�ʽΪyyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String now = dateFormat.format(new Date());
		return now;
	}

	public static String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String now = dateFormat.format(new Date());
		return now;
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = dateFormat.format(new Date());
		return now;
	}

	public static String getCurrentExactDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsssss");
		String now = dateFormat.format(new Date());
		return now;
	}

	/**
	 * ��õ�ǰʱ���ַ�
	 * 
	 * @param format
	 *            ���ڸ�ʽ
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDate(String format) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(format);
		String now = dateFormat.format(new Date());
		return now;
	}

	/**
	 * ���ʱ���ʽ������ַ��ʾ
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String dateConversString(Date date, String format)
			throws Exception {
		DateFormat dateFormat = new SimpleDateFormat(format);
		String newDate = dateFormat.format(date);
		return newDate;
	}

	/**
	 * ����date��ss���Ľṹ
	 * 
	 * @param date
	 * @param ss
	 * @return
	 */
	public static Date plusSecond(Date date, int ss) {
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			now.add(Calendar.SECOND, +(ss));
			return now.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getDateWithPlusDay(Date date, int datePlus) {
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(date);
			now.add(Calendar.DATE, datePlus);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(now.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期加减
	 *
	 * @param d
	 * @param field
	 *            类型
	 * @param amount
	 *            数量
	 * @return
	 */
	public static Date add(Date d, int field, int amount) {
		if (d == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 取得date的时间
	 * 
	 * @return
	 */
	public static Date getDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将日期转换成字符窜
	 * 
	 * @return
	 */
	public static String getDateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	/**
	 * 添加天
	 *
	 * @param d
	 * @param amount
	 * @return
	 */
	public static Date addDay(Date d, int amount) {
		return add(d, Calendar.DATE, amount);
	}

	/**
	 * 指定的日期加天数
	 * 
	 * @param date
	 * @param amount
	 * @return
	 */
	public static String addDateToString(String date, int amount) {
		if (date == null || "".equals(date.trim())) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate(date));
		calendar.add(Calendar.DATE, amount);
		return getDateToString(calendar.getTime());
	}

	/**
	 * 取得date的时间
	 * 
	 * @return
	 */
	public static Date getDateTime(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * * 获取指定日期是星期几 参数为null时表示获取当前日期是星期几
	 * 
	 * @param date
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDays[w];
	}

	/**
	 * 得到两个日期之间的日期包括两个日期.
	 * 
	 * @param p_start
	 *            Start Date
	 * @param p_end
	 *            End Date
	 * @return Dates List
	 */
	public static List<String> getDates(String p_start, String p_end) {
		Date date = DateTools.getDate(p_start);
		Calendar startcalendar = Calendar.getInstance();
		startcalendar.setTime(date);
		Date date2 = DateTools.getDate(p_end);
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.setTime(date2);
		List<String> result = new ArrayList<String>();
		while (startcalendar.before(endcalendar)) {
			result.add(DateTools.getDateToString(startcalendar.getTime()));
			startcalendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(p_end);
		return result;
	}
	
	
	/**
	 * 返回两个日期之间的月份List,YYYY-MM 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<String> getMonths(String startDate,String endDate){
		List<String> result= new ArrayList<String>();
		
		String preMonth="";
		Date date = DateTools.getDate(startDate);
		Calendar startcalendar = Calendar.getInstance();
		startcalendar.setTime(date);
		Date date2 = DateTools.getDate(endDate);
		Calendar endcalendar = Calendar.getInstance();
		endcalendar.setTime(date2);
		do {
			String nowMonth=DateTools.getDateToString(startcalendar.getTime()).substring(0, 7);
			if(!preMonth.equals(nowMonth)){
				result.add(nowMonth);
			}
			preMonth=nowMonth;
			startcalendar.add(Calendar.DAY_OF_YEAR, 1);
		}while (startcalendar.before(endcalendar));
		
		return result;
		
	}

	public static String getFistDayofMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前月第一天
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = format.format(c.getTime());
		return first;
	}

	public static String getLastDayofMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		return last;
	}

	// 获取某年某月的最后一天
	public static String getLastDayofMonth(int year, int month) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(year, month - 1, 1);
		ca.set(Calendar.DAY_OF_MONTH,
				ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = format.format(ca.getTime());
		return last;
	}

	/**
	 * 返回指定日期的月的第一天
	 *
	 * @return
	 */
	public static Date getFirstDayOfMonth(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate(date));
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		return calendar.getTime();
	}

	/**
	 * 返回指定日期的月的最后一天
	 *
	 * @return
	 */
	public static Date getLastDayOfMonth(String date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDate(date));
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				1);
		calendar.roll(Calendar.DATE, -1);
		return calendar.getTime();
	}

	/**
	 * 两个日期只差
	 * 
	 * @param fDate
	 * @param oDate
	 * @return
	 */
	public static int daysOfTwo(String fDate, String oDate) {
		Date fistDate = DateTools.getDate(fDate);
		Date endDate = DateTools.getDate(oDate);
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.setTime(fistDate);
		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
		aCalendar.setTime(endDate);
		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
	}

	//得到两个日期相差的天数
	public static int getDateSpace(String date1, String date2)
			throws ParseException {
		Calendar calst = Calendar.getInstance();;
		Calendar caled = Calendar.getInstance();
		calst.setTime(getDate(date1));
		caled.setTime(getDate(date2));
		//设置时间为0时
		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);
		//得到两个日期相差的天数
		int days = ((int)(caled.getTime().getTime()/1000)-(int)(calst.getTime().getTime()/1000))/3600/24;
		return days;
	}

	/**
	 * 字符串转换为日期
	 * 
	 * @param strDate
	 * @param formatStr
	 * @return
	 */
	public static Date stringConversDate(String strDate, String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	//根据指定日期获取上周开始日期
	public static Date getLastWeekStart(String date) throws Exception{
	      Calendar cal = Calendar.getInstance();
	      Date d = sdf.parse(date);
	      cal.setTime(d);
	      cal.add(Calendar.WEEK_OF_MONTH, -1);
        cal.add(Calendar.DATE, -1 * cal.get(Calendar.DAY_OF_WEEK) + 1);
        return cal.getTime();
	}
    
    /**
     * 两个日期之间差的分钟数
     */
    public static int minuteOfTwo(String fTime, String oTime) {
    	Date fistDate = DateTools.getDateTime(fTime);
    	Date endDate = DateTools.getDateTime(oTime);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fistDate);
        int hour1=aCalendar.get(Calendar.HOUR_OF_DAY);
        int time1 = aCalendar.get(Calendar.MINUTE);
        aCalendar.setTime(endDate);
        int hour2=aCalendar.get(Calendar.HOUR_OF_DAY);
        int time2 = aCalendar.get(Calendar.MINUTE);
        int hour=hour2-hour1;
        if(hour==0){
        	return time2 - time1;       	
        }else{
        	return 60;
        }
        
    }
    /**
     * 两个日期之间差的小时数
     */
    public static int hourOfTwo(String fTime, String oTime) {
    	Date fistDate = DateTools.getDateTime(fTime);
    	Date endDate = DateTools.getDateTime(oTime);
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fistDate);
        int day1=aCalendar.get(Calendar.DAY_OF_YEAR);
        int time1 = aCalendar.get(Calendar.HOUR_OF_DAY);
        aCalendar.setTime(endDate);
        int day2=aCalendar.get(Calendar.DAY_OF_YEAR);
        int time2 = aCalendar.get(Calendar.HOUR_OF_DAY);
        int day=day2-day1;
        if(day==0){
        	return time2 - time1;       	
        }else{
        	return 20;
        }
        
    }
	
	//根据指定日期获取上周结束日期
	public static Date getLastWeekEnd(String date) throws Exception{
	      Calendar cal = Calendar.getInstance();
	      Date d = sdf.parse(date);
	      cal.setTime(d);
	      cal.add(Calendar.WEEK_OF_MONTH, -1);
        cal.add(Calendar.DATE, -1 * cal.get(Calendar.DAY_OF_WEEK) + 7);
        return cal.getTime();
	}
	
	//根据指定日期获取上月开始日期
	public static Date getMonthStart(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.setTime(d);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, (1 - index));
		return calendar.getTime();
	}

	//根据指定日期获取上月开始日期
	public static Date getLastMonthStart(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, -1);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, (1 - index));
		return calendar.getTime();
	}

	//根据指定日期获取本月开始日期
	public static Date getMonthEnd(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, 1);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, (-index));
		return calendar.getTime();
	}

	//根据指定日期获取上月结束日期
	public static Date getLastMonthEnd(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.add(Calendar.MONTH, -1);
		calendar.setTime(d);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, (-index));
		return calendar.getTime();
	}
	
	//根据指定日期获取上周开始日期
	public static String getLastMonth(String date) throws Exception{
	    Calendar cal = Calendar.getInstance();
	    Date d = sdf.parse(date);
	    cal.setTime(d);
	    cal.add(Calendar.MONTH, -1);
        return dateConversString(cal.getTime(),"yyyy-MM");
	}
	
	//根据指定日期获取上周开始日期
	public static String getNextMonth(String date) throws Exception{
	    Calendar cal = Calendar.getInstance();
	    Date d = sdf.parse(date);
	    cal.setTime(d);
	    cal.add(Calendar.MONTH, 1);
        return dateConversString(cal.getTime(),"yyyy-MM");
	}

	//根据指定日期获取明天日期
	public static Date getNext(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.setTime(d);
		calendar.add(Calendar.DATE, 1);
		return calendar.getTime();
	}

	//根据指定日期获取昨天日期
	public static Date getPre(String date) throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(date);
		calendar.setTime(d);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	
	/**
	 * 计算两个日期相差天数可跨年
	 */
	public static int daysBetween(String date1,String date2)  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(DateTools.getDate(date1));  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(DateTools.getDate(date2));  
        long time2 = cal.getTimeInMillis();       
        long between_days=(time2-time1)/(1000*3600*24);  
          
       return Integer.parseInt(String.valueOf(between_days));         
    }

    public static long timeBetween(String date1,String date2){
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTools.getDateTime(date1));
		long time1 = cal.getTimeInMillis();
		cal.setTime(DateTools.getDateTime(date2));
		long time2 = cal.getTimeInMillis();
		long between =time2-time1;
		return between;
	}


	/**
	 * 计算两个日期相差秒
	 */
	public static long getSecondsBetweenTwoDays(String date1,String date2)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateTools.getDate(date1));
		long time1 = cal.getTimeInMillis();
		cal.setTime(DateTools.getDate(date2));
		long time2 = cal.getTimeInMillis();
		return (time2-time1)/1000;
	}

	
	//下个月的第一天
	public static String get2NextMonthStart() throws Exception {
		Calendar calendar = Calendar.getInstance();
		Date d = sdf.parse(getCurrentDate());
		calendar.setTime(d);
		calendar.add(Calendar.MONTH, 1);
		int index = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DATE, 1-index);
		return DateTools.dateConversString(calendar.getTime(),"yyyy-MM-dd");
	}
	
	
	/**
	 * 计算两个日期相差天数可跨年
	 */
	public static Map<String, String> getDayBetweenTowDay(String date1,String date2)  
    {  
		String beginDate=date1;
		Map<String, String> map =new HashMap<String, String>();
		map.put(beginDate, beginDate);
		if (DateTools.daysBetween(date1, date2) > 0) {
			do{
				beginDate=addDateToString(beginDate, 1);
				map.put(beginDate, beginDate);
			}while(!beginDate.equals(date2));
		}
		
          
       return map;         
    } 
	
	//获取日期在当年的周数
	public static int getWeeksOfDateMonth(String date){
		Calendar calendar = Calendar.getInstance();
		Date d = DateTools.getDate(date);
		calendar.setTime(d);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	//获取日期的月份
	public static int getMonthOfDate(String date) {
		Calendar calendar = Calendar.getInstance();
		Date d = DateTools.getDate(date);
		calendar.setTime(d);
		return calendar.get(Calendar.MONTH)+1;
	}
	
	//获取日期的年份
	public static int getYearOfDate(String date){
		Calendar calendar = Calendar.getInstance();
		Date d = DateTools.getDate(date);
		calendar.setTime(d);
		return calendar.get(Calendar.YEAR);
	}
	
	//根据指定日期获取本周结束日期
	public static int getWeekLastDay(String date) throws Exception{
		  Calendar cal = Calendar.getInstance();
		  Date d = sdf.parse(date);
		  cal.setTime(d);
		//获取周几
		int weekDay = getDayOfWeek(cal);
		cal.add(Calendar.DATE, -1 * weekDay + 7);
        return cal.get(Calendar.YEAR);
	}


	//按星期一为一周开始获取DAY_OF_WEEK
	public static int getDayOfWeek(Calendar cal){
		//获取周几
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		//若一周第一天为星期天，则-1
		weekDay = weekDay - 1;
		if(weekDay == 0){
			weekDay = 7;
		}
		return weekDay;
	}

	//根据指定日期获取上周结束日期
	public static int getLastWeekEndDay(String date) throws Exception{
	      Calendar cal = Calendar.getInstance();
	      Date d = sdf.parse(date);
	      cal.setTime(d);
	      cal.setFirstDayOfWeek(Calendar.MONDAY);
	      cal.add(Calendar.WEEK_OF_YEAR, -1);
		  int dayOfWeek=getDayOfWeek(cal);
          cal.add(Calendar.DATE, -1 * dayOfWeek + 1);
        return cal.get(Calendar.WEEK_OF_YEAR);
	}
	
	//根据指定日期获取上周结束年份
	public static int getLastWeekEndYear(String date) throws Exception{
	      Calendar cal = Calendar.getInstance();
	      Date d = sdf.parse(date);
	      cal.setTime(d);
	      cal.add(Calendar.WEEK_OF_YEAR, -1);
		  int dayOfWeek=getDayOfWeek(cal);
		  logger.info(date+"_"+dayOfWeek);
		  cal.add(Calendar.DATE, -1 * dayOfWeek + 1);
		  return cal.get(Calendar.YEAR);
	}
	
	//根据指定日期获取周的星期几
	public static int getDayOfWeekByDate(String date) throws Exception{
	    Calendar cal = Calendar.getInstance();
	    Date d = sdf.parse(date);
	    cal.setTime(d);
        return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	//获取当年的九月一号
	public static String getNineOneDate(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR)+"-09-01";
	}

	//java1.8
//	public static int getWeekOrYear(String dateStr){
//		LocalDate localDate = LocalDate.parse(dateStr);
//		int week = localDate.get ( IsoFields.WEEK_OF_WEEK_BASED_YEAR );
//		int weekYear = localDate.get ( IsoFields.WEEK_BASED_YEAR );
//		return week;
//	}
	
	public static void main(String[] args) throws Exception {

	}

	public static Date getDate(String date,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
	public static String addMinute(String startTime,int minute){
		Calendar cal = Calendar.getInstance();
		try {
			Date d = DateTools.getDate(DateTools.getCurrentDate() + " " + startTime,"yyyy-MM-dd HH:mm");
			cal.setTime(d);
			cal.add(Calendar.MINUTE, minute);
			return DateTools.dateConversString(cal.getTime(), "HH:mm");
		}catch (Exception e){
			logger.error("时间转换错误 DateTools.addMinute()"+e.getMessage());
		}
		return "";
	}

	public static String addSecond(String startTime,int second){
		Calendar cal = Calendar.getInstance();
		try {
			Date d = DateTools.getDate(DateTools.getCurrentDate() + " " + startTime,"yyyy-MM-dd HH:mm");
			cal.setTime(d);
			cal.add(Calendar.SECOND, second);
			return DateTools.dateConversString(cal.getTime(), "HH:mm");
		}catch (Exception e){
			logger.error("时间转换错误 DateTools.addSecond()"+e.getMessage());
		}
		return "";
	}

	public static String TimeStamp2Date(String timestampString, String formats) {
		if (StringUtil.isEmpty(formats))
			formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
		return date;
	}

	public static int getAge(String birthDate) {
		int age = 0;
		if (StringUtil.isNotBlank(birthDate)) {
			Date now = new Date();
			String pat = "\\d{4}-\\d{2}-\\d{2}" ; 
			Pattern p = Pattern.compile(pat) ;    // 实例化Pattern类
			Matcher m = p.matcher(birthDate) ;    // 实例化Matcher类
			if(m.matches()){        // 进行验证的匹配，使用正则
				// 日期格式合法！
				SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
				SimpleDateFormat format_M = new SimpleDateFormat("MM");
				String birth_year = birthDate.substring(0, 4);
				String this_year = format_y.format(now);
				String birth_month = birthDate.substring(5, 7);
				String this_month = format_M.format(now);
				// 初步，估算
				age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
				// 如果未到出生月份，则age - 1
				if (this_month.compareTo(birth_month) < 0) {
					age -= 1;
				}
				if (age < 0) {
					age = 0;
				}
			}
		}
		return age;
	}


	 // 获得当天0点时间  
    public static Date getTimesmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();  
    }
    // 获得当天24点时间  
    public static Date getTimesnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.MILLISECOND, 0);  
        return cal.getTime();  
    } 
    // 获得本周一0点时间  
    public static Date getTimesWeekmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
        return cal.getTime();  
    }  
  
    // 获得本周日24点时间  
    public static Date getTimesWeeknight() {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(getTimesWeekmorning());  
        cal.add(Calendar.DAY_OF_WEEK, 7);  
        return cal.getTime();  
    }  
    // 获得本月第一天0点时间  
    public static Date getTimesMonthmorning() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
        return cal.getTime();  
    }  
  
    // 获得本月最后一天24点时间  
    public static Date getTimesMonthnight() {  
        Calendar cal = Calendar.getInstance();  
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
        cal.set(Calendar.HOUR_OF_DAY, 24);  
        return cal.getTime();  
    }  
	
	
	
	
	
	
	
	
	
	
	
	//根据指定日期获取上周结束日期
	/**
	 * @param i   多少小时
	 * @return
	 * @throws Exception
	 */
	public static Long getLastOneHourTime(int i) throws Exception{
	      Calendar cal = Calendar.getInstance();
	      cal.setTime(new Date());
	      cal.add(Calendar.HOUR, i);
//	      cal.add(Calendar.SECOND, 5);
	      Long value=getUnixTime(dateConversString(cal.getTime(),"yyyy-MM-dd HH:mm:ss"))/1000;
	      System.out.println(value);
        return  value;
	}
	
	public static Long getUnixTime(String dateTime) throws ParseException{
		return new java.text.SimpleDateFormat ("yyyy-MM-dd HH:mm:ss").parse(dateTime).getTime();
	}
	
	
	
	// 判断两个日期是否是同一天
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}
	// 判断两个日期字符串是否是同一天
	public static boolean isSameDate(String data1, String data2, String format) {
		Date date1 = stringConversDate(data1, format);
		Date date2 = stringConversDate(data2, format);
		return isSameDate(date1, date2);
	}


	public static String timeStampToDate(long timeStamp,String pattern){
		Date date = new Date(timeStamp);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String dateStr = simpleDateFormat.format(date);
		return dateStr;
	}
	
	
	private static SimpleDateFormat miniSDF = new SimpleDateFormat("HH:mm:ss");

	public static String timeAddMinutes(String beginTime, Integer minutes) throws ParseException {
		Date date = miniSDF.parse(beginTime);
		Calendar nowTime = Calendar.getInstance();
		nowTime.setTime(date);
		nowTime.add(Calendar.MINUTE, minutes);
		String endTime = miniSDF.format(nowTime.getTime());
		return endTime.substring(0, 5);
	}
	
}
	
