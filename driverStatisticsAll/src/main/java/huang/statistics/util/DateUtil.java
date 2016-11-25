package huang.statistics.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.tonetime.commons.util.StringUtils;

public class DateUtil {

	/**
	 * 从当前时间返回5分钟间隔
	 * @throws Exception 
	 * 
	 */
	public static List<Date> dateToDateMinute() throws Exception
	{
		Date day=new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//String a="2016-09-14 11:48:00";
		//Date day=sdf1.parse(a);
		
		
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(day);
	
		rightNow.add(Calendar.MINUTE, -5);
		Date day1=rightNow.getTime();

		List<Date> list=new ArrayList<Date>();
		list.add(day1);
		list.add(day);
		
		return list;
		
		
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 从字符串年月解析得到整月的年月日
	 * @throws Throwable 
	 */
	public static List<Date> StringToDateAllDay( String yearMonth,String pattern) throws Throwable
	{
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date dt=null;
		if (!StringUtils.isBlank(yearMonth)) {
			
			 dt = sdf.parse(yearMonth);
			
		}
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateDay =sdf1.format(dt);
	    
		String dateDay0[] = dateDay.split(" ");
		String dateDay1=dateDay0[0];
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateDay2=sdf2.parse(dateDay1);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dateDay2);
		rightNow.add(Calendar.MONTH, 1);
		rightNow.add(Calendar.DAY_OF_YEAR, -1);
		
		Date dateDay3=rightNow.getTime();
		String dateDay4=sdf1.format(dateDay3);
		
		String [] dateDay5=dateDay4.split(" ");
		
		String dateDay6=dateDay5[0];
		dateDay6=dateDay6+ " 23:59:59";
		Date dateDay7=sdf1.parse(dateDay6);
		
		System.out.println("riqi wei ------------"+dt);
		System.out.println("riqi wei ------------"+dateDay7);
		
		List<Date> list=new ArrayList<Date>();
		list.add(dt);
		list.add(dateDay7);
		return list;
	}
	/**
	 * 从字符串解析得到日期 00:00:00
	 */
	public static Date StringToDateStart(String day, String pattern)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if (!StringUtils.isBlank(day) && pattern.length() > 16) {
			day = day + " 00:00:00";
			Date dt = sdf.parse(day);
			return dt;
		} else if (!StringUtils.isBlank(day) && pattern.length() < 16) {
			Date dt = sdf.parse(day);
			return dt;
		}
		return null;
	}

	/**
	 * 从字符串解析得到日期 23:59:59
	 */
	public static Date StringToDateEnd(String day, String pattern)
			throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		if (!StringUtils.isBlank(day) && pattern.length() > 16) {
			day = day + " 23:59:59";

			Date dt = sdf.parse(day);
			return dt;

		} else if (!StringUtils.isBlank(day) && pattern.length() < 16) {
			Date dt = sdf.parse(day);
			return dt;
		}
		return null;
	}

	/*
	 * 从字符串解析到当天日期：00:00:00 到23:59:59
	 */

	public static List<Date> StringToTodayDate(String day, String pattern)
			throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		if (!StringUtils.isBlank(day)) {
			String startDay = day + " 00:00:00";
			String endDay = day + " 23:59:59";

			Date start = sdf.parse(startDay);
			Date end = sdf.parse(endDay);
			List<Date> list = new ArrayList<Date>();
			list.add(start);
			list.add(end);

			return list;

		}
		return null;
	}
	
	
	
	public static Date StringToday(String day, String pattern)
			throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		if (!StringUtils.isBlank(day)) {
			String startDay = day ;
			Date start = sdf.parse(startDay);

			return start;

		}
		return null;
	}
	
	
	
	
	
	/*
	 * 从日期解析到当天日期：00:00:00 到23:59:59
	 */

	public static List<Date> dateToTodayDate(Date day)
			throws Exception {

		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (day!=null) {
			String today = sdf.format(day);

			String startDay = today + " 00:00:00";
			String endDay = today + " 23:59:59";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date start = sdf1.parse(startDay);
			Date end = sdf1.parse(endDay);
			List<Date> list = new ArrayList<Date>();
			list.add(start);
			list.add(end);
			return list;
		}
		return null;
	}
	/*
	 * 返回过去了总天数；
	 */
	public static List<Date> StringToPastSumDay(String day, String pattern,
			int n) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar rightNow = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();

		System.out.println("----------------" + rightNow);
		if (!day.isEmpty()) {
			Date today = sdf.parse(day);
			rightNow.setTime(today);
			list.add(today);
		}
		for (int i = 1; i < n; i++) {

			rightNow.add(Calendar.DAY_OF_YEAR, -1);
			Date pastDay = rightNow.getTime();
			list.add(pastDay);
		}
		return list;
	}
	/*
	 * 从日期解析 返回过去了总天数；
	 */

	public static List<Date> dateToPastSumDay(Date day,int n)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();
		if (day!=null) {
			rightNow.setTime(day);
			list.add(day);
		}
		for (int i = 1; i < n; i++) {

			rightNow.add(Calendar.DAY_OF_YEAR, -1);
			Date pastDay = rightNow.getTime();
			list.add(pastDay);
		}
		
		
		
		return list;
	}
	
	
	/*
	 * 从日期解析 返回过去了n天；
	 */
	public static Date dateToPastDay(Date day,int n)
			throws Exception {
		Calendar rightNow = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();
		if (day!=null) {
			rightNow.setTime(day);
			list.add(day);
		}
			rightNow.add(Calendar.DAY_OF_YEAR, -n);
			Date pastDay = rightNow.getTime();
		return pastDay;
	}
	
	
	
	
	
	
	

	/*
	 * 返回过去 n 天的时间；
	 */
	public static Date StringToPastDay(String day, String pattern, int n)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar rightNow = Calendar.getInstance();

		if (!day.isEmpty()) {
			Date today = sdf.parse(day);
			rightNow.setTime(today);
			rightNow.add(Calendar.DAY_OF_YEAR, -n);
			Date pastDay = rightNow.getTime();

			return pastDay;
		}
		return null;
	}

	/*
	 * 返回过去了总月数；
	 */
	public static List<Date> StringToPastSumMonth(String day, String pattern) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar rightNow = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();
		System.out.println("----------------" + rightNow);
		if (!day.isEmpty()) {
			Date today = sdf.parse(day);
			rightNow.setTime(today);
			list.add(today);

			rightNow.add(Calendar.MONTH, -1);
			Date pastMonth1 = rightNow.getTime();
			list.add(pastMonth1);

			rightNow.setTime(today);
			rightNow.add(Calendar.MONTH, -3);
			Date pastMonth3 = rightNow.getTime();
			list.add(pastMonth3);

			rightNow.setTime(today);
			rightNow.add(Calendar.MONTH, -12);
			Date pastMonth12 = rightNow.getTime();
			list.add(pastMonth12);
		}
		return list;
	}
	
	/*
	 * 返回过去了总月数；
	 */
	public static List<Date> dateToPastSumMonth(Date today) throws Exception {
		
		Calendar rightNow = Calendar.getInstance();
		List<Date> list = new ArrayList<Date>();
		System.out.println("----------------" + rightNow);
		if (today!=null) {
			rightNow.setTime(today);
			list.add(today);

			rightNow.add(Calendar.MONTH, -1);
			Date pastMonth1 = rightNow.getTime();
			list.add(pastMonth1);

			rightNow.setTime(today);
			rightNow.add(Calendar.MONTH, -3);
			Date pastMonth3 = rightNow.getTime();
			list.add(pastMonth3);

			rightNow.setTime(today);
			rightNow.add(Calendar.MONTH, -12);
			Date pastMonth12 = rightNow.getTime();
			list.add(pastMonth12);
		}
		return list;
	}
	
	
	
	
	
	/*
	 * 两个日期间隔多少天；
	 */
	public static int  dateToDateSum(Date time1,Date time2) throws Exception {
		
		Calendar rightNow = Calendar.getInstance();
	
		return 1;
		
	}
	
	
	
	/***
	 * 返回间隔相差一分钟的时间集合
	 * 
	 */
	
	public static List<Date>  datetoDateOneMinute(Date startTime)
	{
		List<Date> list = new ArrayList<Date>();
		Calendar rightNow = Calendar.getInstance();
		
		rightNow.setTime(startTime);
		rightNow.add(Calendar.MINUTE, 1);
		
		Date endTime=rightNow.getTime();
		
		list.add(startTime);
		list.add(endTime);
		
		
		return list;
	}
	
	
	
	//-----------------------------------------------------------一个星期，一个月，三个月，一年，全部时间；
	
	public static List<Date>  stringToDateTimeFlag(String flag) throws ParseException
	{
		List<Date> list = new ArrayList<Date>();
		Calendar rightNow = Calendar.getInstance();
		Date today = new Date();
		Date pastDay=null;
		rightNow.setTime(today);
		if (flag.equals("week")) {
			rightNow.add(Calendar.DAY_OF_YEAR, -7);
			pastDay = rightNow.getTime();
		} else if (flag.equals("month1")) {
			rightNow.add(Calendar.MONTH, -1);
			pastDay = rightNow.getTime();

		} else if (flag.equals("month3")) {
			rightNow.add(Calendar.MONTH, -3);
			pastDay = rightNow.getTime();

		} else if (flag.equals("year")) {

			rightNow.add(Calendar.MONTH, -12);
			pastDay = rightNow.getTime();
		}
		else if (flag.equals("alltime")) {
			// rightNow.add(Calendar.DAY_OF_YEAR, -7);
			String past = "00-00-00";
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			Date start = sdf1.parse(past);
			pastDay = start;
		}
		list.add(pastDay);
		list.add(today);
		return list;
	}
	
	
	
	
	
	
	

}
