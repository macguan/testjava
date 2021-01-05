package cn.com.dwsoft.main;

import cn.com.corecenter.initial.Initial;
import cn.com.dwsoft.util.jdbc.JdbcUtil;
import cn.com.dwsoft.service.DwrcStaticService;
import cn.com.dwsoft.service.ImportStaticService;
import cn.com.dwsoft.service.JLineService;

import java.util.Date;

public class Bootstrap {

    public static void test() throws Exception{

                String sql = "select 'success' from dual";
                String retValue = JdbcUtil.queryForString("jdbc.properties",sql);
   
                System.out.println(retValue);


    }
	
    public static void main(String[] args) throws Exception {
	
               Initial.init("conf/system_initial.properties");
               

		if(args.length == 3){
			if(args[0].equalsIgnoreCase("-import")){
				//System.out.println(args[1]);
				//System.out.println(args[2]);
                                ImportStaticService.importFile(args[1],args[2]); 
			}
		}else{
			JLineService.cmdLine();
		}
               //test();
               //DwrcStaticService.test1();

    }
}

