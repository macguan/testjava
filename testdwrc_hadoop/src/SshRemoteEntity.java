package cn.com.dwsoft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SshRemoteEntity{

        public String host;

	public int port;

	public String type = "ssh";

	public String userName;

	public String password;

	public String homedir;

	public int connect_session_timeout;

	public long startTimeMs;
	public long endTimeMs;

	
        public SshRemoteEntity(String host, int port, String userName,String password) {
                super();
                this.host = host;
                this.port = port;
                this.userName = userName;
                this.password = password;
        }

	public long getIssueTimeMs(){
		return endTimeMs - startTimeMs;
	}

	public String getIssueTimeHuman(){
		long milliseconds = getIssueTimeMs();
		long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
		
		return " "+ minutes+" Mins "+ seconds+ " Secs ["+milliseconds+"]";

	}

	public String getStartTimeHuman () {
   	 	Date date = new Date();
    		date.setTime(this.startTimeMs);
    		return new SimpleDateFormat().format(date);
	}	
        public String getEndTimeHuman () {
                Date date = new Date();
                date.setTime(this.endTimeMs);
                return new SimpleDateFormat().format(date);
        }

        public long getStartTimeMs() {
                return startTimeMs;
        }
        public void setStartTimeMs(long startTimeMs) {
                this.startTimeMs = startTimeMs;
        }

        public long getEndTimeMs() {
                return endTimeMs;
        }
        public void setEndTimeMs(long endTimeMs ){
                this.endTimeMs = endTimeMs;
        }


        public String getHost() {
                return host;
        }
        public void setHost(String host) {
                this.host = host;
        }

        public int getPort() {
                return port;
        }
        public void setPort(int port) {
                this.port = port;
        }

        public String getUserName() {
                return userName;
        }
        public void setUserName(String userName) {
                this.userName = userName;
        }

        public String getPassword() {
                return password;
        }
        public void setPassword(String password) {
                this.password = password;
        }

        public String getType() {
                return type;
        }
        public void setType(String type) {
                this.type = type;
        }

        public String getHomedir() {
		if(this.homedir != null && !this.homedir.equals(""))
                	return homedir;
		else 
			return "/home/"+this.userName;
        }
        public void setHomedir(String homedir) {
                this.homedir = homedir;
        }

        public int getConnect_session_timeout() {
                return connect_session_timeout;
        }
        public void setConnect_session_timeout(int connect_session_timeout) {
                this.connect_session_timeout = connect_session_timeout;
        }



}	
