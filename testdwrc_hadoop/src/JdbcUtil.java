package cn.com.dwsoft.util.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import cn.com.dwsoft.util.file.PropertiesParser;
import cn.com.corecenter.constants.CommonFuncConstants;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;


public class JdbcUtil {

	
	public static String db_type ="mysql";

/*
	private static Connection getConn(String jdbcpropertiesFilePath) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
*/
        private static Connection getConn(String jdbcpropertiesFilePath) throws Exception{
		
		PropertiesParser paser = new PropertiesParser(jdbcpropertiesFilePath);
		if(paser!=null){
			Class.forName(paser.getInfoFromConfiguration("jdbc.driverClassName")).newInstance(); 
			String   url= paser.getInfoFromConfiguration("jdbc.url"); 
			String   user= paser.getInfoFromConfiguration("jdbc.username"); 
			String   password= paser.getInfoFromConfiguration("jdbc.password"); 

                        String   _db_type= paser.getInfoFromConfiguration("jdbc.type");
			if(_db_type != null&&!_db_type.equals("")){
                            	if(_db_type.equalsIgnoreCase("oracle")||_db_type.equalsIgnoreCase("mysql")){
					db_type = _db_type;
					//System.out.println("db_type is "+db_type);
				}else {
					throw new Exception("jdbc.type must be mysql or oracle.");
				}			
			}                       

                        if(CommonFuncConstants.debugLevel == 1){
				System.out.println("--------------------------connect info------------------------");     
                        	System.out.println("Now.get Connect from DB.");
                        	System.out.println("jdbc.driverClassName:"+paser.getInfoFromConfiguration("jdbc.driverClassName"));
                        	System.out.println("jdbc.url:"+url);
                        	System.out.println("jdbc.username:"+user);
                        	//System.out.println("jdbc.password:"+password);
                        	System.out.println("\n");

                        }	


			Connection   conn=   DriverManager.getConnection(url,user,password); 
			
			paser.closeConfiguration();
                        
                        //System.out.println("--------------------------   result   ------------------------");	
		
			return conn;
		}else
			return null;
				
	}


	public static void executeBatchSql(String jdbcpropertiesFilePath,List<String> sqlList) {

                Connection conn = null;
                Statement   stmt= null;
                ResultSet rs= null;
                String returnValue = null;

                try {
                conn =  getConn(jdbcpropertiesFilePath);

                stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                //for(String temp:sqlList)
                //       stmt.executeUpdate(temp);

                for(String temp:sqlList){
                     stmt.addBatch(temp);
                  }

                stmt.executeBatch();                
               
                // java.sql.SQLException: Can''t call commit when autocommit=true
                //conn.commit();
                //conn.setAutoCommit(true);

                System.out.println("successful completed!");
                System.out.println(sqlList.size()+" recs are inserted");

                }catch(Exception e){
                 e.printStackTrace();

                }finally{
                  try{
                    if (stmt != null)  stmt.close();
                   } catch (Exception e) {
                      e.printStackTrace();
                   } finally {
                        try {
                         if (conn != null)  conn.close();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }

                    }

                 }


    }			




	

        public static Integer queryForInt(String jdbcpropertiesFilePath,String sql) {

                Connection conn = null;
                Statement   stmt= null;
                ResultSet rs= null;
                Integer returnValue = 0;

                try {
                conn =  getConn(jdbcpropertiesFilePath);

                stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                rs=stmt.executeQuery(sql);

                if(rs.next())
                        returnValue = rs.getInt(1);

                 rs.close();

                }catch(Exception e){
                 e.printStackTrace();

                }finally{
                  try{
                    if (rs != null)  rs.close();
                    if (stmt != null)  stmt.close();
                   } catch (Exception e) {
                      e.printStackTrace();
                   } finally {
                        try {
                         if (conn != null)  conn.close();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }
                    }

                 }

                return returnValue;

        }

        public static void executeUpdateSql(String jdbcpropertiesFilePath,List<String> sqlList) {

                Connection conn = null;
                Statement   stmt= null;
                ResultSet rs= null;
                String returnValue = null;

                try {
                conn =  getConn(jdbcpropertiesFilePath);

                stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				
				List<String> errorSqlList = new ArrayList<String>();
                for(String temp:sqlList){
					try{
                       stmt.executeUpdate(temp);
					 }catch(Exception e){
						e.printStackTrace();
						System.out.println("error sql:"+temp);
						errorSqlList.add(temp);
					  }
				}	
                System.out.println("successful completed!");
                System.out.println(sqlList.size()+" recs are inserted successfully");
				System.out.println(errorSqlList.size()+" recs are inserted failed");

                }catch(Exception e){
                 e.printStackTrace();

                }finally{
                  try{
                    if (stmt != null)  stmt.close();
                   } catch (Exception e) {
                      e.printStackTrace();
                   } finally {
                        try {
                         if (conn != null)  conn.close();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }

                    }

                 }


    }
	
	public static String queryForString(String jdbcpropertiesFilePath,String sql) {
	
        	Connection conn = null;
                Statement   stmt= null;
                ResultSet rs= null;
                String returnValue = null;
      
                try {               
                conn =  getConn(jdbcpropertiesFilePath);
		
		stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE); 
		
                rs=stmt.executeQuery(sql);  
		
		returnValue = "";
		if(rs.next())
			returnValue = rs.getString(1);
 	        
                 rs.close();
                
                }catch(Exception e){
                 e.printStackTrace();

                }finally{
                  try{
                    if (rs != null)  rs.close();
                    if (stmt != null)  stmt.close();
                   } catch (Exception e) {
                      e.printStackTrace();
                   } finally {
                        try {
                         if (conn != null)  conn.close();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }

                    }

                 }
		
                return returnValue;

	}
	

	private static String buildPageSql(String dbType,String sql, String idName, int start, int limit) {

		if(StringUtils.equalsIgnoreCase(dbType, "oracle")){
			
			return buildPageSqlForOracle(sql, start, limit);
		}else if(StringUtils.equalsIgnoreCase(dbType, "mysql")){
			
			return buildPageForMySql(sql, start, limit);
		}else if(StringUtils.equalsIgnoreCase(dbType, "sqlserver")){
			
			return buildPageForSqlServer(sql, idName,start, limit);
		}
		
		else{
			
			return buildPageSqlForOracle(sql, start, limit);
		}
		
	}
	
	private static String buildPageSqlForOracle(String sql, int start, int limit) {
		StringBuilder pageSql = new StringBuilder();
		pageSql.append("SELECT * FROM (")
					.append("SELECT ROWNUM RN,PAGE1.* FROM (").append(sql).append(") PAGE1 ")
			  .append(") PAGE2 ")
			  .append(" WHERE PAGE2.RN>").append(start)
			  .append(" AND PAGE2.RN<=").append(start + limit);
		return pageSql.toString();
	}

	private static String buildPageForMySql(String sql,int start,int limit)
	{
		StringBuilder pageSql = new StringBuilder();
		
		pageSql.append("SELECT * FROM (").append(sql).append(") PAGE1 ")
		       .append(" limit ").append(start).append(",").append(limit);
		
		return pageSql.toString();
	}
	
	private static String buildPageForSqlServer(String sql,String idName,int start,int limit)
	{
		StringBuilder pageSql = new StringBuilder();
		pageSql.append("SELECT * FROM (")
					.append("SELECT row_number() over(order by PAGE1.").append(idName).append(") RN,* FROM (").append(sql).append(") PAGE1 ")
			  .append(") PAGE2 ")
			  .append(" WHERE PAGE2.RN>").append(start)
			  .append(" AND PAGE2.RN<=").append(start + limit);
		return pageSql.toString();	
	}

        public static List<Map<String,Object>> queryForList(String jdbcpropertiesFilePath,String sql
                  ,int start,int pageSize) {

                Connection conn = null;
                Statement   stmt= null;
                ResultSet rs= null;
                List<Map<String,Object>> retList = null;

                if(pageSize > 0){
			sql = buildPageSql(db_type,sql, null, start, pageSize);
                  // sql = sql + " limit "+start + ","+pageSize;
                }

                System.out.println("Start to issue following sql:");
                System.out.println(sql);


                try {
                conn =  getConn(jdbcpropertiesFilePath);

                stmt=conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

                rs=stmt.executeQuery(sql);
                
                retList = convertList(rs); 

                }catch(Exception e){
                 e.printStackTrace();

                }finally{
                  try{
                    if (rs != null)  rs.close();
                    if (stmt != null)  stmt.close();
                   } catch (Exception e) {
                      e.printStackTrace();
                   } finally {
                        try {
                         if (conn != null)  conn.close();
                        } catch (Exception e) {
                         e.printStackTrace();
                        }

                    }

                 }

                return retList;

        }

       public static Integer queryForCount(String jdbcpropertiesFilePath,String sql) {
           String countSql =  "select count(*) from ("+sql+") ct1";
           System.out.println(countSql);            
 
          return queryForInt(jdbcpropertiesFilePath,countSql);

       }

   private static List<Map<String,Object>> convertList(ResultSet rs) throws SQLException {
 
      List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
      
      ResultSetMetaData md = rs.getMetaData();
      int columnCount = md.getColumnCount(); 
 
      while (rs.next()) { 
	Map<String,Object> rowData = new HashMap<String,Object>();
		
	for (int i = 1; i <= columnCount; i++) {
		rowData.put(md.getColumnName(i), rs.getObject(i));
	}
	list.add(rowData);
      } 

     return list;
 
  } 
	
	public static void main(String[] args) throws Exception {
		String sql = "select 'success' from dual";
		String retValue = queryForString("jdbc.properties",sql);
		System.out.println(retValue);

	}

}
