package cn.com.dwsoft.util.string;

import org.apache.commons.lang.StringUtils;

public class StringDealUtil {

	/**
	 * 将字符串去空格,全转成小写
	 * @param str
	 * @return
	 */
	public static String formatStr(String str){
		if(StringUtils.isBlank(str))
			return null;
		str = str.trim();
		str = str.toLowerCase();		
		return str;

	}

	/**
	 * 如果为null,或"null",就转为"",否则原样返回
	 * @param str
	 * @return
	 */
	public static String strNullDeal(String str){
		
		if(str == null || str.equals("null") || str.equals("NULL")){
			str = "";
		}

		return str;
		
	}
	

	/**
	 * 是否含有\r\n
	 * @param str
	 * @return
	 */
	public static Integer isRN(String str){
		if(StringUtils.isBlank(str))
			return -1;
		return str.indexOf("\r\n");
	}

	/**
	 * 是否含有\r
	 * @param str
	 * @return
	 */
	public static Integer isR(String str){
		if(StringUtils.isBlank(str))
			return -1;
		return str.indexOf("\r");
	}
	
	/**
	 * 是否含有\n
	 * @param str
	 * @return
	 */
	public static Integer isN(String str){
		if(StringUtils.isBlank(str))
			return -1;
		return str.indexOf("\n");
	}
	
	/**
	 * 字符串替换
	 * @param str
	 * @param replacedStr
	 * @param replaceStr
	 * @return
	 */
	public static String replaceStr(String str,String replacedStr,String replaceStr){
		if(StringUtils.isBlank(str))
			return null;
		return str.replace(replacedStr, replaceStr);
	}
	
	/**
	 * 字符串替换\r,\n,\r\n
	 * @param str
	 * @param replaceStr
	 * @return
	 */
	public static String replaceRNT(String str,String replaceStr){
		if(StringUtils.isBlank(str))
			return null;
		str = replaceStr(str,"\r\n",replaceStr);
		str = replaceStr(str,"\n",replaceStr);
		str = replaceStr(str,"\r",replaceStr);
		return str;
	}
	
	public static void main(String[] args) {
		String testStr="\r\n"+"aaa"+"\r\n"+"bbb"+"\r\n"+"ccc";
		
		System.out.println("<"+replaceRNT(testStr,"")+">");
		System.out.println("<"+replaceRNT(testStr,"1")+">");
		System.out.println("<"+replaceRNT(testStr,"<br/>")+">");
	}

}
