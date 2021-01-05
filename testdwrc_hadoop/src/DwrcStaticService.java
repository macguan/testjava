package cn.com.dwsoft.service;

import cn.com.dwsoft.util.jdbc.JdbcUtil;
import cn.com.corecenter.constants.CommonFuncConstants;

import cn.com.dwsoft.util.string.StringDealUtil;

import cn.com.chinet.common.utils.file.FileOperateUtil;

import org.apache.commons.lang.math.NumberUtils;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

public class DwrcStaticService {

    public static void testconn() throws Exception{

   
                String sql = "SELECT 'connect success' FROM dual";
		System.out.println("\nissue following sql on db:"+sql);
                System.out.println("\nresult:");
                String retValue = JdbcUtil.queryForString("conf/jdbc.properties",sql);
                System.out.println(retValue);


    }

   public static void testsql() throws Exception{
          //      String sql = "select col_code ,audit_sn,col_disp_name,col_len from rc_audit_columns ";
                  String sql = "select * from rc_audit_columns ";
          dispSql(sql," | ",CommonFuncConstants.PAGESIZE);       
   } 

   public static void showtables() throws Exception{
          String sql = "show tables";
          dispSqlNopage(sql,"");
   }

    private static String checkSQL(String sql){
       
       sql = sql.trim();

       if(sql==null || sql.equals("")){
         System.out.println("sql is null");
         return null;
       }
       
      if(sql.endsWith(";")){
        sql = sql.substring(0,sql.length()-1);
      }

      return sql;

    }

    public static void dispSql(String sql,String splitChars,int pageSize) throws Exception{
           
           sql = checkSQL(sql);
           if(sql == null) {
              return;
           }
           
           int countRec = JdbcUtil.queryForCount("conf/jdbc.properties",sql);
           int countPage = (countRec +pageSize - 1) / pageSize;
              

           System.out.println("count of record is "+countRec);
           System.out.println("page size is "+countPage);   

           int start = 0;
           for(int curPage=1;curPage<=countPage;curPage++){
             System.out.println("----------------no."+curPage+" page--------------------");
             start = (curPage-1)*pageSize;
             List<Map<String,Object>> retList = JdbcUtil.queryForList("conf/jdbc.properties",sql,start,pageSize);
             if(retList != null && retList.size()>0 ){

                 List<String> keyList =   getKeyList(retList.get(0));

                 for(int i=0;i<retList.size();i++){

                   Map<String,Object> _rec = retList.get(i);

                   StringBuilder _sb = new StringBuilder();
                   for(int j=0;j<keyList.size();j++){
                      if(j == (keyList.size()-1))
                          _sb.append(_rec.get(keyList.get(j)));
                      else
                         _sb.append(_rec.get(keyList.get(j))+splitChars);
                   } // end of for j

                   System.out.println(_sb.toString());

                 } // end of for i  
             
             }else {
                 System.out.println("result is null");
             } 

           } // end of for curPage        
    

    }	

    public static void dispSqlNopage(String sql,String splitChars) throws Exception{

           sql = checkSQL(sql);
           if(sql == null) {
              return;
           }


             List<Map<String,Object>> retList = JdbcUtil.queryForList("conf/jdbc.properties",sql,0,-1);
             if(retList != null && retList.size()>0 ){

                 List<String> keyList =   getKeyList(retList.get(0));

                 for(int i=0;i<retList.size();i++){

                   Map<String,Object> _rec = retList.get(i);

                   StringBuilder _sb = new StringBuilder();
                   for(int j=0;j<keyList.size();j++){
                      if(j == (keyList.size()-1))
                          _sb.append(_rec.get(keyList.get(j)));
                      else
                         _sb.append(_rec.get(keyList.get(j))+splitChars);
                   } // end of for j

                   System.out.println(_sb.toString());

                 } // end of for i  

             }else {
                 System.out.println("result is null");
             }



    }


   public static void testexport() throws Exception{
          String sql = "select * from rc_audit_columns";
          String splitChars = "|";
          int pageSize = 5;
          String fileName = "aaa.txt";
          String filePath = CommonFuncConstants.getExportDirPath()+fileName;
          exportFileBySql(sql,splitChars,pageSize,filePath);
   }


    public static void exportFileBySql(String sql,String splitChars,int pageSize,String filename) 
                      throws Exception{

           sql = checkSQL(sql);
           if(sql == null) {
              return;
           }

           int countRec = JdbcUtil.queryForCount("conf/jdbc.properties",sql);
           int countPage = (countRec +pageSize - 1) / pageSize;


           System.out.println("count of record is "+countRec);
           System.out.println("page size is "+countPage);
	
	   String exportPath = FileOperateUtil.getPath(filename);	   
	   FileOperateUtil.checkAndCreateDir(exportPath);
          	
           FileOutputStream out = null;
           OutputStreamWriter outWriter = null;
           BufferedWriter bufWrite = null;
		
		   try {
		   
		           out = new FileOutputStream(filename);
                           outWriter = new OutputStreamWriter(out, "UTF-8");
                           bufWrite = new BufferedWriter(outWriter);
						
			   int start = 0;
			   for(int curPage=1;curPage<=countPage;curPage++){
				 System.out.println("----------------no."+curPage+" page--------------------");
				 start = (curPage-1)*pageSize;
				 List<Map<String,Object>> retList = JdbcUtil.queryForList("conf/jdbc.properties",sql,start,pageSize);
				 if(retList != null && retList.size()>0 ){
                                         
                                         List<String> writeLines = new ArrayList<String>();

					 List<String> keyList =   getKeyList(retList.get(0));

					 for(int i=0;i<retList.size();i++){

					   Map<String,Object> _rec = retList.get(i);

					   StringBuilder _sb = new StringBuilder();
					   for(int j=0;j<keyList.size();j++){
						  if(j == (keyList.size()-1))
							  _sb.append(_rec.get(keyList.get(j)));
						  else
							 _sb.append(_rec.get(keyList.get(j))+splitChars);
					   } // end of for j

					   //System.out.println(_sb.toString());
                                           writeLines.add(_sb.toString());
                                           
					 } // end of for i  

                                          
                                         for (int wi = 0; wi < writeLines.size(); wi++) {
                                            bufWrite.write(writeLines.get(wi) + "\r\n");
                                         }


				 }else {
					 System.out.println("result is null");
				 }

			   } // end of for curPage        

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("write " + filename + " error！");
                        }finally {
				try {
                                  System.out.println("-----------export file name: " + filename+"----------------");
 
					if(bufWrite != null) bufWrite.close();
					if(outWriter != null) outWriter.close();
					if(out != null) out.close();

				} catch (Exception e) {
						e.printStackTrace();
				}
                       }// end of finally


    }

    public static void exportFileBySql2(String sql,String splitChars,int pageSize,String filename) 
         throws Exception{


       List<Map<String,Object>> retList = JdbcUtil.queryForList("conf/jdbc.properties",sql,0,pageSize);
       
      List<String> writeLines = new ArrayList<String>();

      if(retList != null && retList.size()>0 ){
          System.out.println("result size is "+retList.size());

          List<String> keyList =   getKeyList(retList.get(0));
          for(int i=0;i<retList.size();i++){
             Map<String,Object> _rec = retList.get(i);
             
             StringBuilder _sb = new StringBuilder();
             for(int j=0;j<keyList.size();j++){
          
               if(j == (keyList.size()-1))
                  _sb.append(_rec.get(keyList.get(j)));
               else
                  _sb.append(_rec.get(keyList.get(j))+splitChars);
              } // end of for

              //System.out.println(_sb.toString());
              writeLines.add(_sb.toString());

          } // end of for

       writeLineFile(filename, writeLines);

       }else {
         System.out.println("result is null");
       }


    }

     private static void writeLineFile(String filename, List<String> lines){
       
	    	FileOutputStream out = null;
		OutputStreamWriter outWriter = null;
		BufferedWriter bufWrite = null;
		try {
			out = new FileOutputStream(filename);
			outWriter = new OutputStreamWriter(out, "UTF-8");
			bufWrite = new BufferedWriter(outWriter);
			
			for (int i = 0; i < lines.size(); i++) {
				bufWrite.write(lines.get(i) + "\r\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("读取" + filename + "出错！");
		}finally {
			try {
			    if(bufWrite != null) bufWrite.close();
				if(outWriter != null) outWriter.close();
				if(out != null) out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
              }// end of finally
				
	}



    public static void testinsert() throws Exception{

                List<String> sqls = new ArrayList<String>();
              
                String tb_name = "`rc_audit_columns`";
                String tb_cl_names = "(col_code ,audit_sn ,col_sql_name,col_disp_name,col_default_val,order_num,col_type,col_len,is_knkey ,is_hide,is_search_condition)";           

                int cl_num = 1;
                String topic_prefix = "002";
                String audit_prefix = "02";
                String cl_prefix = "CL_"+topic_prefix+"_"+audit_prefix+"_";
                String audit_sn = "AP_002_02";

sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'user_id', '用户ID', NULL, 1, 'string', 8, 1, 1, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'city_code', '业务区编码', NULL, 2, 'string', 8, 1, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'user_type_code,', '用户类型', NULL, 3, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'net_type_code', '网别', NULL, 4, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'brand_code', '当前产品品牌编码', NULL, 5, 'string', 8, 1, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'prepay_tag', '预付费标志', NULL, 6, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'state_code_svc', '服务状态编码', NULL, 7, 'string', 8, 1, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'start_date', '开始时间', NULL, 8, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'end_date', '结束时间', NULL, 9, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'max_owe_month', '最大欠费账期', NULL, 10, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'min_owe_month', '最小欠费账期', NULL, 11, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'owe_fee_all', '欠费总金额', NULL, 12, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'owe_month', '欠费时长', NULL, 13, 'string', 8, 0, 0, 0)");
sqls.add("INSERT INTO " + tb_name + tb_cl_names + " VALUES ( '"+cl_prefix+ cl_num++ +"', '"+audit_sn+"', 'owe_fee', '当前欠费', NULL, 14, 'string', 8, 0, 0, 0)");

                
                JdbcUtil.executeBatchSql("conf/jdbc.properties",sqls);
                
                //System.out.println(retValue);


    }

	private static  List<String>  getKeyList(Map<String,Object> _rec){
	  List<String> keyList = new ArrayList<String>();
	  	  
	  Iterator<String> keyIter = _rec.keySet().iterator();
	  while(keyIter.hasNext()){
		  keyList.add(keyIter.next());
	  }
	  
	  return keyList;
  }

}

