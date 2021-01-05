package cn.com.corecenter.initial;

import cn.com.corecenter.constants.CommonFuncConstants;
import cn.com.dwsoft.util.file.PropertiesParser;
import cn.com.chinet.common.utils.file.FileOperateUtil;

import java.io.File;

public class Initial {
	

	public static void init(String systemInitFilePath){
		
                System.out.println("....NOW INITIAL...");

		PropertiesParser paser = null;
		try{
			paser = new PropertiesParser(systemInitFilePath);
			if(paser!=null){
				
                                String _root_dir = paser.getInfoFromConfiguration("root_dir");
                                if(_root_dir != null && !_root_dir.equals("")){
                                   CommonFuncConstants.ROOTDIR = _root_dir;
				   
				  if(!FileOperateUtil.isFileExist(CommonFuncConstants.ROOTDIR))
                        		throw new Exception("root_dir "+CommonFuncConstants.ROOTDIR+" is not exist! mkdir it.");					
                                   //System.out.println("root_dir is :"+CommonFuncConstants.ROOTDIR);
                                }


                                String _pagesize = paser.getInfoFromConfiguration("pagesize");
                                if(_pagesize != null && !_pagesize.equals("")){
                                   try{
                                      CommonFuncConstants.PAGESIZE =Integer.parseInt(_pagesize);
                                      //System.out.println("pagesize is :"+CommonFuncConstants.PAGESIZE);
                                   }catch(Exception e){
                                      throw new Exception("pagesize must be a number!");
                                   }
                                }
                                 
                                String _debuglevel = paser.getInfoFromConfiguration("debuglevel");
                                if(_debuglevel != null && !_debuglevel.equals("")){

                                   if(_debuglevel.equals("0")){
                                      CommonFuncConstants.debugLevel =0;
                                   }
                                   else if(_debuglevel.equals("1")){
                                      CommonFuncConstants.debugLevel =1;
                                   }else {
                                      throw new Exception("debuglevel must be 0 or 1!");
                                   }
                                   //System.out.println("debuglevel is :"+CommonFuncConstants.debugLevel);
                                 
                                }	
			}
		
		}catch(Exception e){
			System.out.println(systemInitFilePath + " failed!");	 
                	e.printStackTrace();
                	System.exit(0);

         	}finally{
			dispConstants();
			if(paser != null) paser.closeConfiguration();
		        System.out.println("....INITIAL COMPLETE...");
		 }

				
	}
	
	public static void dispConstants(){
		System.out.println("root_dir is :"+CommonFuncConstants.ROOTDIR);
 		System.out.println("pagesize is :"+CommonFuncConstants.PAGESIZE);
		System.out.println("debuglevel is :"+CommonFuncConstants.debugLevel);

	}


        public static void showJdbcProperties(String jdbcFilePath){

                PropertiesParser paser = null;
                try{
                        paser = new PropertiesParser(jdbcFilePath);
                        if(paser!=null){
			String   driverClassName = paser.getInfoFromConfiguration("jdbc.driverClassName"); 
			String   url= paser.getInfoFromConfiguration("jdbc.url"); 
			String   user= paser.getInfoFromConfiguration("jdbc.username"); 
			String   password= paser.getInfoFromConfiguration("jdbc.password"); 
	
			System.out.println("jdbc.driverClassName:"+driverClassName);
			System.out.println("jdbc.url:"+url);
			System.out.println("jdbc.username:"+user);
			System.out.println("jdbc.password:"+password); 

                        }

                }catch(Exception e){
                 e.printStackTrace();

                }finally{

                        if(paser != null) paser.closeConfiguration();

                 }


        }

        public static void showSshProperties(String sshFilePath){

                PropertiesParser paser = null;
                try{
                        paser = new PropertiesParser(sshFilePath);
                        if(paser!=null){
                        String   host = paser.getInfoFromConfiguration("host");
                        String   port= paser.getInfoFromConfiguration("port");
                        String   username= paser.getInfoFromConfiguration("username");
                        String   password= paser.getInfoFromConfiguration("password");
			String   homedir= paser.getInfoFromConfiguration("homedir");

                        System.out.println("host:"+host);
                        System.out.println("port:"+port);
                        System.out.println("username:"+username);
                        System.out.println("password:"+password);
			System.out.println("homedir:"+homedir);

                        }

                }catch(Exception e){
                 e.printStackTrace();

                }finally{

                        if(paser != null) paser.closeConfiguration();

                 }


        }

        public static void showHadoopProperties(String hadoopFilePath){

                PropertiesParser paser = null;
                try{
                        paser = new PropertiesParser(hadoopFilePath);
                        if(paser!=null){
                        String   hiveserver2_ip = paser.getInfoFromConfiguration("hiveserver2.ip");
                        String   hiveserver2_port= paser.getInfoFromConfiguration("hiveserver2.port");
                        String   hiveserver2_urlparam= paser.getInfoFromConfiguration("hiveserver2.urlparam");
                        String   hive_database= paser.getInfoFromConfiguration("hive.database");
                        
                        System.out.println("hiveserver2.ip:"+hiveserver2_ip);
                        System.out.println("hiveserver2.port:"+hiveserver2_port);
                        System.out.println("hiveserver2.urlparam:"+hiveserver2_urlparam);
                        System.out.println("hive.database:"+hive_database);
                 
                        }

                }catch(Exception e){
                 e.printStackTrace();

                }finally{

                        if(paser != null) paser.closeConfiguration();

                 }


        }


}

