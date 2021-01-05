package cn.com.dwsoft.util.hadoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import cn.com.dwsoft.util.file.PropertiesParser;
import cn.com.corecenter.constants.CommonFuncConstants;

public class HiveJdbcUtil {
    

	
    	public static Connection getConn(String propertiesFilePath) throws Exception{

		Connection conn = null;

		PropertiesParser paser = new PropertiesParser(propertiesFilePath);
		if(paser!=null){

			String driverName ="org.apache.hive.jdbc.HiveDriver";

			String   hiveserver2_ip= paser.getInfoFromConfiguration("hiveserver2.ip"); 
			String   hiveserver2_portStr= paser.getInfoFromConfiguration("hiveserver2.port");
			int   hiveserver2_port = Integer.parseInt(hiveserver2_portStr);
			String   hiveserver2_urlparam= paser.getInfoFromConfiguration("hiveserver2.urlparam");

			String   hive_database= paser.getInfoFromConfiguration("hive.database");

                	//String Url="jdbc:hive2://192.168.10.191:10000/fengkong?mapred.job.queue.name=dw";
	                //String hiveserver2 = "192.168.10.191";
        	        //int hiveserver2_port = 10000;
                	//String database = "fengkong";
                                                                                
                	String url_param="";
			if(hiveserver2_urlparam!=null && !hiveserver2_urlparam.equals(""))
				url_param="?"+"mapred.job.queue.name=dw";
                
                	String url = "jdbc:hive2://"+hiveserver2_ip+":"+hiveserver2_port + "/"+hive_database+ url_param;

	                Class.forName(driverName);
	                conn = DriverManager.getConnection(url,"","");
                        if(CommonFuncConstants.debugLevel == 1){
			    	System.out.println("--------------------------connect info------------------------");
				System.out.println("hiveserver2.ip:"+hiveserver2_ip);
				System.out.println("hiveserver2.port:"+hiveserver2_port);
				System.out.println("hiveserver2.urlparam:"+hiveserver2_urlparam);
				System.out.println("hive.database:"+hive_database);
				
				System.out.println("\n");			
                        }

			
			paser.closeConfiguration();

			return conn;

		}		
	        else {
			return null;
		}


    	}	

	

    public static void queryForList(String propertiesFilePath,String sql){

        System.out.println(sql);

	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;

        try {
	    conn = getConn(propertiesFilePath);
	
	    ps = conn.prepareStatement(sql);	
            rs=ps.executeQuery();
            
	    int columns=rs.getMetaData().getColumnCount();
            while(rs.next())
            {
                for(int i=1;i<=columns;i++)
                {
                    System.out.print(rs.getString(i));  
                    System.out.print("\t\t");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
		try{
			if (rs != null)  rs.close();
                        if (ps != null)  ps.close();
			if (conn != null)  conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
 
    }

    public static void executeHiveCmd(String propertiesFilePath,String sql){

        System.out.println(sql);

        Connection conn = null;
	Statement stmt = null;

        try {
            conn = getConn(propertiesFilePath);
	
            stmt = conn.createStatement();

	    //sql = "drop table if exists " + tableName );
            //sql = "create table " + tableName
	    //sql = "load data local inpath '" + filepath + "' into table " + tableName;
            System.out.println("Running: " + sql);
            stmt.execute(sql);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
                try{
                        if (stmt != null)  stmt.close();
                        if (conn != null)  conn.close();
                }catch(Exception e){
                        e.printStackTrace();
                }
        }

    }


   public static void main(String[] args) { 

	long startTime=System.currentTimeMillis();

	//String sql = "select * from stu2";
        String sql = "show tables";
        queryForList("conf/hadoop.properties",sql);

	long endTime=System.currentTimeMillis();
	System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
    }	

}
