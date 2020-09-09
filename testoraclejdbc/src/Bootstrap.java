package cn.com.dwsoft.main;

import cn.com.dwsoft.util.jdbc.JdbcUtil;
import java.util.Date;

public class Bootstrap {
	
    public static void main(String[] args) throws Exception {
		
		String sql = "select 'success' from dual";
		String retValue = JdbcUtil.getSqlStringResult("jdbc.properties",sql);
		System.out.println(retValue);
    }
}

