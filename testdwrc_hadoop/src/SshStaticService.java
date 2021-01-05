package cn.com.dwsoft.service;

import cn.com.dwsoft.util.jdbc.JdbcUtil;
import cn.com.dwsoft.util.file.PropertiesParser;
import cn.com.corecenter.constants.CommonFuncConstants;

import cn.com.dwsoft.entity.SshRemoteEntity;

import cn.com.dwsoft.util.string.StringDealUtil;

import cn.com.chinet.common.utils.file.FileOperateUtil;

import org.apache.commons.lang.math.NumberUtils;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.InputStream;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class SshStaticService {

    private static class MyUserInfo implements UserInfo{
        @Override
        public String getPassphrase() {
            System.out.println("getPassphrase");
            return null;
        }
        @Override
        public String getPassword() {
            System.out.println("getPassword");
            return null;
        }
        @Override
        public boolean promptPassword(String s) {
            System.out.println("promptPassword:"+s);
            return false;
        }
        @Override
        public boolean promptPassphrase(String s) {
            System.out.println("promptPassphrase:"+s);
            return false;
        }
        @Override
        public boolean promptYesNo(String s) {
            System.out.println("promptYesNo:"+s);
            return true;//notice here!
        }
        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:"+s);
        }
    }

	public static void test1(){

	    String USER="machinemonitor";
	    String PASSWORD="Dwsoft123";
	    String HOST="192.168.10.188";
	    int DEFAULT_SSH_PORT=22;
	    
	    try {
		JSch jsch = new JSch();	
			
		Session session = jsch.getSession(USER,HOST,DEFAULT_SSH_PORT);
		session.setPassword(PASSWORD);
	     
		session.setConfig("StrictHostKeyChecking", "no");
	        session.setUserInfo(new MyUserInfo());
	     	session.connect(30000);
  
  	     	if(session.isConnected()){
	       		Channel channel=session.openChannel("shell");
        
	        	channel.setInputStream(System.in);
	        	channel.setOutputStream(System.out);
	   
	        	channel.connect(3*1000);
		        
	        //session.disconnect();
	     	}	
	        
	} catch(Exception e){
            System.out.println(e);
        }

    }

        public static SshRemoteEntity getSshSftpProper(String propertiesFilePath) throws Exception{
				
		SshRemoteEntity remote = null;
				
                PropertiesParser paser = new PropertiesParser(propertiesFilePath);
                if(paser!=null){
                        String   host= paser.getInfoFromConfiguration("host");
                        String   port= paser.getInfoFromConfiguration("port");
                        String   username= paser.getInfoFromConfiguration("username");
			String   password= paser.getInfoFromConfiguration("password");
			String   homedir= paser.getInfoFromConfiguration("homedir");

                        if(CommonFuncConstants.debugLevel == 0){
                                System.out.println("--------------------------connect info------------------------");
                                System.out.println("Now.get Connect from ssh/sftp.");
                                System.out.println("host:"+host);
                                System.out.println("port:"+port);
                                System.out.println("username:"+username);
                                System.out.println("password:"+password);
				System.out.println("homedir:"+homedir);

                        }

			remote = new SshRemoteEntity(host,Integer.parseInt(port),username,password);
			if(homedir!=null && !homedir.equals(""))
				remote.setHomedir(homedir);
						
                        paser.closeConfiguration();

                        return remote;
                }else
                        return null;

        }


        public static void goSSHCmd(SshRemoteEntity remote,String cmd,List<String> logLines){


	    System.out.println("Now goto ssh window ............");
	    System.out.println("will issue following command:");
	    System.out.println(cmd);
	    System.out.println("waiting ...\n\n");	
            try {
                JSch jsch = new JSch();

                Session session = jsch.getSession(remote.getUserName(),remote.getHost(),remote.getPort());
                session.setPassword(remote.getPassword());

                session.setConfig("StrictHostKeyChecking", "no");
		session.setUserInfo(new MyUserInfo());

		if(remote.getConnect_session_timeout()>0)
                	session.connect(remote.getConnect_session_timeout());
		else
			session.connect();

                if(session.isConnected()){

        	   	Channel channel=session.openChannel("exec");
            		((ChannelExec)channel).setCommand(cmd);

                        //channel.setInputStream(System.in);
                        channel.setInputStream(null);

                        //channel.setOutputStream(System.out);

                        //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
                        //((ChannelExec)channel).setErrStream(fos);
			((ChannelExec)channel).setErrStream(System.err);

			InputStream channelIS=channel.getInputStream();

			//channel.connect(3*1000);
			channel.connect();

			byte[] tmp=new byte[1024];
            		while(true){
				
				// At first, channelIS.available() == 0
				// Only after command is ending. The channelIS.available()>0 			
                		while(channelIS.available()>0){
					
                    			int readCount=channelIS.read(tmp, 0, 1024); // read 1k bytes from channel
                    		
					if(readCount < 0 )
					   break; // read to end. break while
                    			
					String line = new String(tmp, 0, readCount);
					System.out.print(line); //disp read content
					logLines.add(line);
                		}
				
				// Only after command is ending. 
				// And channel.isClosed()
				// And channelIS.read() to end.
				// To break the while
				if(channel.isClosed()){
                    			if(channelIS.available()>0) 
						continue;
                    			System.out.println("exit-status: "+channel.getExitStatus());
                    			break; // break while(true);
                		}
				
				// to wait until command is ending.
                		try{
					Thread.sleep(1000);  // waiting for commandd issuce whick is taken time.

				}catch(Exception ee){
					ee.printStackTrace();
				} 

           		 } // end of while
		
			channelIS.close();
			channel.disconnect();
		        session.disconnect();

                } // if(session.isConnected()){

        } catch(Exception e){
            System.out.println(e);
        }

    }



        public static List<LsEntry> getSftpFileList(SshRemoteEntity remote,String homeDir
				,List<String> logLines){


            System.out.println("[Now goto sftp window] ............");
            System.out.println("[waiting] ...");

	    Session session = null;
	
	    List<LsEntry> fileList = null;

            try {
                JSch jsch = new JSch();

                session = jsch.getSession(remote.getUserName(),remote.getHost(),remote.getPort());
                session.setPassword(remote.getPassword());

                session.setConfig("StrictHostKeyChecking", "no");
                session.setUserInfo(new MyUserInfo());

                if(remote.getConnect_session_timeout()>0)
                        session.connect(remote.getConnect_session_timeout());
                else
                        session.connect();

                if(session.isConnected()){
		
			System.out.println("[session is created!]");				
                        logLines.add("[session is created!]");

			ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
			//channelSftp.setFilenameEncoding("gbk");
			            
			channelSftp.connect();
                        
                        if(channelSftp.isConnected()) { 
	                        System.out.println("[sftp channel is created!]");
        	                logLines.add("[sftp channel is created!]");
		
				fileList = _getSftpFileList(channelSftp,homeDir);
				/*
				for(LsEntry fileObj:fileList){
					String fileName = fileObj.getFilename();
					System.out.println(fileName);
                                        logLines.add(fileName);
				}*/
		
 	                        channelSftp.quit();
				channelSftp.disconnect();
   
			 } else {
				System.out.println("sftp error! connection is unopen!");

			}	

                } 


        } catch(Exception e){
            e.printStackTrace();
            logLines.add(e.getMessage());

        } finally{

		if(session!=null && session.isConnected()){
                         session.disconnect();
                 }

	}

	return fileList;


    }


        public static void sftpDownload(SshRemoteEntity remote
					,String remoteDir,String remoteFileName
					,String localpath,String localFileName
                                	,List<String> logLines){


            System.out.println("[Now goto sftp window] ............");
	    System.out.println(remote.getHost()+":"+remote.getPort()+"  "+remote.getUserName());
            System.out.println("[waiting] ...");

            Session session = null;


            try {
                JSch jsch = new JSch();

                session = jsch.getSession(remote.getUserName(),remote.getHost(),remote.getPort());
                session.setPassword(remote.getPassword());

                session.setConfig("StrictHostKeyChecking", "no");
                session.setUserInfo(new MyUserInfo());

                if(remote.getConnect_session_timeout()>0)
                        session.connect(remote.getConnect_session_timeout());
                else
                        session.connect();

                if(session.isConnected()){

                        System.out.println("[session is created!]");
                        logLines.add("[session is created!]");

                        ChannelSftp channelSftp = (ChannelSftp)session.openChannel("sftp");
                        
                        channelSftp.connect();

                        if(channelSftp.isConnected()) {
                                System.out.println("[sftp channel is created!]");
                                logLines.add("[sftp channel is created!]");
				
				String saveFileFullPath = FileOperateUtil.addPathSuffix(localpath) + FileOperateUtil.dropPathPrefix(localFileName);
                                _sftpDownload(remoteDir,FileOperateUtil.dropPathPrefix(remoteFileName),saveFileFullPath,channelSftp);

                                channelSftp.quit();
                                channelSftp.disconnect();

                         } else {
                                System.out.println("sftp error! connection is unopen!");

                        }

                }else {
			 System.out.println("sftp error! session is unopen!");
		}


        } catch(Exception e){
            e.printStackTrace();
            logLines.add(e.getMessage());

        } finally{

                if(session!=null && session.isConnected()){
                         session.disconnect();
                 }

        }



    }



	private static List<LsEntry> _getSftpFileList(ChannelSftp channelSftp
		,String directory) throws Exception{

                 List<LsEntry> retList = new ArrayList<LsEntry>();

                 Vector vector  = channelSftp.ls(directory);

                 for(Object obj :vector){
                          if(obj instanceof com.jcraft.jsch.ChannelSftp.LsEntry)
			  {
				LsEntry lsObj = (LsEntry)obj;
				if(!lsObj.getAttrs().isDir()){
					String fileName = lsObj.getFilename();
					if(!fileName.startsWith(".")) 
						retList.add(lsObj);
				}
                          }
                 }
		
		return retList;
	}

	 private static void _sftpDownload(String directory, String downloadFile, String saveFile
		, ChannelSftp channelSftp) throws Exception{

		File file = null;
                FileOutputStream fs = null;  	

		try{
   			channelSftp.cd(directory);

			file = new File(saveFile);
			fs = new FileOutputStream(file);

			
                        String remoteFileFullPath = FileOperateUtil.addPathSuffix(directory) + FileOperateUtil.dropPathPrefix(downloadFile);
			System.out.println("[download remote sftp file]:"+remoteFileFullPath);
			channelSftp.get(downloadFile, fs);
			System.out.println("[to local]:"+file.getAbsolutePath());

	        } catch(Exception e){
        	    	e.printStackTrace();
			throw new Exception(e.getMessage());
        	} finally{
			try{
			
                        	if(fs!= null) fs.close();
			}catch(Exception e){
				e.printStackTrace();
			}

        	}	


 	}	


       public static void sftpDownload1File(String remoteDir,String remoteFileName,List<String> logLines) throws Exception{

                SshRemoteEntity remote = getSshSftpProper("conf/ssh_sftp.properties");
                
                //String remoteDir = remote.getHomedir()+"/"+"logs/nacos/";
                //String remoteFileName = "config.log";
                String localpath = "export/";
                String localFileName = remoteFileName;
                sftpDownload(remote
                            ,remoteDir,remoteFileName
                            ,localpath,localFileName
                            ,logLines);		

	}

	public static void main(String[] args) throws Exception{
		
		List<String> logLines = new ArrayList<String>();

		//SshRemoteEntity remote = new SshRemoteEntity("192.168.10.191",22,"machinemonitor","Dwsoft123");
		SshRemoteEntity remote = getSshSftpProper("conf/ssh_sftp.properties");

		sftpDownload1File(remote.getHomedir()+"/"+"logs/nacos/","config.log",logLines);

		//goSSHCmd(remote,"~/gogo.sh",logLines);
	
		/*
                List<LsEntry> fileList = getSftpFileList(remote,remote.getHomedir(),logLines);
                for(LsEntry fileObj:fileList){
                         String fileName = fileObj.getFilename();
                         System.out.println(fileName);
                         logLines.add(fileName);
                }
		*/

		/*
		String remoteDir = remote.getHomedir()+"/"+"logs/nacos/";
		String remoteFileName = "config.log";
		String localpath = "export/";
		String localFileName = "config.log";
		sftpDownload(remote
			    ,remoteDir,remoteFileName
			    ,localpath,localFileName
			    ,logLines);
		*/


/*
		for(int i=0;i<logLines.size();i++){
			System.out.println("no."+i) ;
			System.out.println(logLines.get(i));
		}
*/


	}	


}
