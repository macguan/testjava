package cn.com.dwsoft.service;

 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import cn.com.dwsoft.util.hadoop.HiveJdbcUtil;
import cn.com.dwsoft.util.file.PropertiesParser;
import cn.com.corecenter.constants.CommonFuncConstants;

public class HiveJdbcStaticService {



 	public static void testconn() throws Exception{


                String sql = "SELECT 'connect success'";
 
                System.out.println("\nissue following sql on db:"+sql);
 
                System.out.println("\nresult:");
                HiveJdbcUtil.queryForList("conf/hadoop.properties",sql);

   	 }	
 

	
    	public static void showtables() {

 	        //long startTime=System.currentTimeMillis();

        	String sql = "show tables";
        	HiveJdbcUtil.queryForList("conf/hadoop.properties",sql);
        
        	//long endTime=System.currentTimeMillis();

       		//System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

    	}	

	


   public static void main(String[] args) { 

	long startTime=System.currentTimeMillis();

	//String sql = "select * from stu2";
        //String sql = "show tables";
        //queryForList("conf/hadoop.properties",sql);

	long endTime=System.currentTimeMillis();
	System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }	

}
