package cn.com.chinet.common.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;


/**
 * 批次生成器
 * @author guanxiaoyu
 *
 */
public class BatchDateTimeUtil {

	public static final String YEARMONTHDAY = "yyyyMMdd";
	public static final String YEARMONTH = "yyyyMM";
	public static final String YEARMONTHHOURMIN = "yyyyMMddHHmm";
	public static final String YEARMONTHHOURMINSEC = "yyyyMMddHHmmss";
	public static final String YEARMONTHHOURMINSECMS = "yyyyMMddHHmmssSSS";
	public static final String HOURMINSECMS = "HHmmssSSS";
	public static final String HOURMINSEC = "HH:mm:ss";
	public static final String MINSECMS = "mmssSSS";
	
	public static final String DATEFORMAT = "yyyy年MM月";
	public static final String DATEFORMAT1 = "yyyy年MM月dd日";
	public static final String COMMONFORMAT = "yyyy-MM-dd";
	
	public static final String COMMONALLFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 转换日期型为指定格式的字符串
	 * @param date
	 * @param formatStr
	 * 例:"yyyy年MM月"
	 * @return
	 */
	public static String dateNumericDisp(Date date,String formatStr){
			if(date==null)
				return null;
			if(formatStr == null || formatStr.equals("") )
				formatStr = COMMONFORMAT;
	  		return new SimpleDateFormat(formatStr).format(date);
	}

	
	/**
	 * 转换日期字符串为Date型
	 * 缺省转换格式为yyyy-MM-dd
	 * @param str
	 * @param formatStr
	 * @return
	 */
	public static Date getDateFromStr(String str,String formatStr){
		if(str == null || str.equals(""))
			return null;
		if(formatStr == null || formatStr.equals(""))
			formatStr="yyyy-MM-dd";
		DateFormat format = new SimpleDateFormat(formatStr); 
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			System.out.println("日期转换出错,日期串:"+str);
			System.out.println("日期转换出错,格式串:"+formatStr);
			e.printStackTrace();
		}
		return date;
	}
	
	@SuppressWarnings("unused")
	public static boolean getBatchDate(String batch){
		if(batch==null)
			return false;
		SimpleDateFormat format = null;		
		format = new SimpleDateFormat(YEARMONTHDAY);
		try {
			Date data = format.parse(batch);
			return true;
		} catch (ParseException e) {
		}
		format = new SimpleDateFormat(YEARMONTH);
		try {
			Date data = format.parse(batch);
			return true;
		} catch (ParseException e) {
		}	
		format = new SimpleDateFormat(YEARMONTHHOURMINSEC);
		try {
			Date data = format.parse(batch);
			return true;
		} catch (ParseException e) {
		}
		format = new SimpleDateFormat(YEARMONTHHOURMINSECMS);
		try {
			Date data = format.parse(batch);
			return true;
		} catch (ParseException e) {
		}
		return false;
	}

	public static String getBatch(String dateFormat){
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(new Date()); 
	}
	
	public static String getCustomBatch(Date date,String dateFormat){
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date); 
	}
	
	public static String getBatch17(){
		return getBatch(YEARMONTHHOURMINSECMS);
	}

	public static String getBatch14(){
		return getBatch(YEARMONTHHOURMINSEC);
	}
	public static String getBatch12(){
		return getBatch(YEARMONTHHOURMIN);
	}
	public static String getBatch9(){
		return getBatch(HOURMINSECMS);
	}
	public static String getBatchTimeHMS(){
		return getBatch(HOURMINSEC);
	}
	
	public static String getBatch8(){
		return getBatch(YEARMONTHDAY);
	}
	public static String getBatch7(){
		return getBatch(MINSECMS);
	}
	

	public static String getBatch6(){
		return getBatch(YEARMONTH);
	}
	
	public static String[] season(String season){
		String[] t = { season.substring(0, 4), season.substring(0, 4),
				season.substring(0, 4) };
		switch (season.charAt(4)) {
		case '1':
			t[0] += "-01";
			t[1] += "-02";
			t[2] += "-03";
			break;
		case '2':
			t[0] += "-04";
			t[1] += "-05";
			t[2] += "-06";
			break;
		case '3':
			t[0] += "-07";
			t[1] += "-08";
			t[2] += "-09";
			break;
		case '4':
			t[0] += "-10";
			t[1] += "-11";
			t[2] += "-12";
			break;
		}
		return t;
	}
	

	
	
	/**
	 * @param format
	 * @param date
	 * @return
	 */
	public static String getFormatDate(String format,Date date){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String getyyyyMMdd(Date date){
		return getFormatDate("yyyy-MM-dd",date);
	}

	public static String getyyyyMMdd2(Date date){
		return getFormatDate("yyyyMMdd",date);
	}
	
	public static String getyyyyMMddHHmm(Date date){
		return getFormatDate("yyyy-MM-dd HH:mm",date);
	}
	
	public static String getyyyyMMddHHmmss(Date date){
		return getFormatDate("yyyyMMddHHmmss",date);
	}
	public static String getyyyyMMddHHmmss1(Date date){
		return getFormatDate("yyyy-MM-dd HH:mm:ss",date);
	}
	public static String getyyyyMM(Date date){
		return getFormatDate("yyyyMM",date);
	}
	
	public static String getSerial(int bitNum){
		Random ran = new Random();
		String retVal = "";
		for(int i=0;i<bitNum;i++){
			retVal += ran.nextInt(10)+"";
		}
		return retVal;
	}


	/**
	 * get first day in last month
	 */
	public static Date lastMonth(Date date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		String month = sdf.format(date);
		Calendar cal = new GregorianCalendar();

		try {
			cal.setTime(sdf.parse(month));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cal.add(Calendar.MONTH, -1);
		
		return cal.getTime();
	}

	/**
	 * get first day in next month
	 * @return
	 */
	public static Date nextMonth(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		String month = sdf.format(date);
		Calendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.MONTH, 1);
		return cal.getTime();
	}
	
	/**
	 * get first day in current month
	 * @return
	 */
	public static Date currentMonthFirstDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		String month = sdf.format(date);
		Calendar cal = new GregorianCalendar();
		try {
			cal.setTime(sdf.parse(month));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return cal.getTime();
	}
	
	/**
	 * get last day in current month
	 * @return
	 */
	public static Date currentMonthLastDay(Date date) {
		
		return getSpecifiedDayBefore(nextMonth(date)); // 取下月一日,然后再取上一天
		
	}
	
    /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(Date date) {
    	
        Calendar c = Calendar.getInstance();
        
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        return c.getTime();
        
    }


    
	/**
	 *
	 * @return
	 */
	public static String getCurrentAuditDate(){
		Date date = new Date();
		int year = Integer.parseInt(getFormatDate("yyyy",date));
		int month = Integer.parseInt(getFormatDate("MM",date));
		if(month!=1){
			month--;
		}else{
			month=12;
			year--;
		}
			
		String auditDate = null;
		if(month<10)
			auditDate=year+"-0"+month;
		else
			auditDate=year+"-"+month;
		
		return auditDate;
	}
	
	
	public static Long getCurentTimeLong(){
		return System.currentTimeMillis();
	}
	
	public static String getConsumeTime(Long oldTime){
		Long currentTime = getCurentTimeLong(); 
		Long consumeTime = currentTime-oldTime;
		Date date = new Date(consumeTime);		
		return getFormatDate("mm分ss秒SSS毫秒",date);
	}
	public static String getConsumeTimeEng(Long oldTime){
		Long currentTime = getCurentTimeLong(); 
		Long consumeTime = currentTime-oldTime;
		Date date = new Date(consumeTime);		
		return getFormatDate("mm:ss:SSS",date);
	}
	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 * @description 两个日期段之间的日期
	 * @version 1.0
	 * @author S.u.n.n.y
	 * @update 2013-12-30 下午06:25:19
	 */
	public static List<String> getDateBetween(String beginDate,String endDate) throws ParseException{
		List<String> dateList=new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date begin = sdf.parse(beginDate);
		Date end = sdf.parse(endDate);
		double between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了从毫秒,转换成秒
		double day = between / (24 * 3600);  // 从秒转换为天
		for (int i = 0; i <= day; i++) {
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(beginDate));
			cd.add(Calendar.DATE, i);// 增加一天
			dateList.add(sdf.format(cd.getTime()));
//			System.out.println(sdf.format(cd.getTime()));
		}
		return dateList;
	}
	
	public static void main(String[] args) {
		
		System.out.println(dateNumericDisp(new Date(),""));
		System.out.println(getDateFromStr("2014-04-01",""));
		
/*		System.out.println(getBatch(BatchDateTimeUtil.YEARMONTHDAY));
		System.out.println(getBatch(BatchDateTimeUtil.YEARMONTH));
		System.out.println(getBatch(BatchDateTimeUtil.YEARMONTHHOURMINSEC));
		System.out.println(getBatch(BatchDateTimeUtil.YEARMONTHHOURMINSECMS));*/
		
		//System.out.println(getBatchDate("201108"));
		/*System.out.println(new Date());
		System.out.println(getyyyyMMddHHmmss1(new Date()));
		
		System.out.println(getSerial(9));*/
		/*
		System.out.println(getyyyyMMdd2(getSpecifiedDayBefore(getDateFromStr("20130901",YEARMONTHDAY))));
		
		System.out.println(getyyyyMMdd2(getSpecifiedDayBefore(new Date())));
		*/
		//System.out.println(getBatch14());
		
		/*System.out.println(nextMonth(new Date()));  // 下月一日
		System.out.println(lastMonth(new Date()));  // 上月一日
		System.out.println(currentMonthFirstDay(new Date()));  // 本月一日
		System.out.println(currentMonthLastDay(new Date()));  // 本月一日
		*/
		//System.out.println(getyyyyMMddHHmmss(new Date()));
		//System.out.println(getSpecifiedDayBefore(new Date()));
		
		/*Date date = new Date();
		System.out.println(date.getTime());
		Long lnum = Long.parseLong("1390374780000");
		Date date1 = new Date(lnum);
		System.out.println(date1);*/
	}

}
