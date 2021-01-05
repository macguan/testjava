package cn.com.dwsoft.service;


import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Terminal;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.Completer;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.LineReader;

import cn.com.dwsoft.service.DwrcStaticService;
import cn.com.dwsoft.service.ImportStaticService;
import cn.com.dwsoft.service.SshStaticService;
import cn.com.dwsoft.entity.SshRemoteEntity;
import cn.com.dwsoft.service.HiveJdbcStaticService;

import cn.com.corecenter.initial.Initial;
import cn.com.corecenter.constants.CommonFuncConstants;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;


import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class JLineService {


	public static void cmdLine()throws IOException {
		
		Terminal terminal = TerminalBuilder.builder().system(true).build();
 
                Completer commandCompleter = new StringsCompleter("testconn","showproperties","showtables","execsql", "testinsert","importfile","exportsql","sftpdownload", "help","exit","quit");

		LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal)
                                    .completer(commandCompleter)
                                    .build();

		String prompt = "dwsoft> ";

		String line = null;
                String[] cmds = null;

                boolean firstRunFlag = true;
		do{



			try {
                                if(firstRunFlag){
                                  printWelcome();
                                  firstRunFlag = false;
                                  line = "help";
                                }else {
                                  line = reader.readLine(prompt);
                                  line = line.trim();
                                }

			if(line == null || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")|| line.equalsIgnoreCase("quit;") || line.equalsIgnoreCase("exit;"))
					break;
				
				// 开始解析,先把输入命令用空格拆分开	
				cmds = line.split(" ");
				
				// 命令(首词)判断
				if("testconn".equalsIgnoreCase(cmds[0])){
                                        //DwrcStaticService.testconn();
                                        testconn();
					//System.out.println("你的命令是:"+cmds[0]);
					//System.out.println("你的命令参数是:"+cmds[1]+" "+cmds[2]);
				}
                                else
                                if("showproperties".equalsIgnoreCase(cmds[0])){
                                         showproperties();
                                }

                                else
                                if("showtables".equalsIgnoreCase(cmds[0])||"showtables;".equalsIgnoreCase(cmds[0])){
                                        //DwrcStaticService.showtables();
                                        showtables();
                                }

				else
				if("execsql".equalsIgnoreCase(cmds[0])){
                                        //DwrcStaticService.testsql();
                                        inputSqlExec();
				}
				else
				if("testinsert".equalsIgnoreCase(cmds[0])){
                                        DwrcStaticService.testinsert();
				}
                                else
                                if("importfile".equalsIgnoreCase(cmds[0])){
					importFile();
                                        //ImportStaticService.importFile("stu2.txt","import_cfg_001.properties");
                                        //ImportStaticService.getCfgByPropertiesName("import_cfg_001.properties");
                                }
                                else
                                if("exportsql".equalsIgnoreCase(cmds[0])){
                                        inputSqlExport();
                                }
                                else
                                if("sftpdownload".equalsIgnoreCase(cmds[0])){
                                        sftpdownload();
                                }
				else
				if("help".equalsIgnoreCase(cmds[0])||"help;".equalsIgnoreCase(cmds[0])){
					printHelp();
				}
				else{
					System.out.println("Unknown command.");
					printHelp();
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
                                if(cmds!=null){
                                 System.out.println("Wrong arguments.");    
                                 printCmdHelp(cmds);
                                }
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}while(true);

		System.exit(0);
	}


        public static void testconn() throws IOException {

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String prompt = "\nchoose [1.mysqlorOracle, 2.hive]: ";
                String line = null;
                   try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
                                System.out.println("you must input 1 or 2\n");
                        }
                        else{
                                if(line.equals("1"))
					DwrcStaticService.testconn();
                                else if(line.equals("2"))
                                        HiveJdbcStaticService.testconn();
                                else
                                        System.out.println("you must input 1 or 2\n");

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }


        }


        public static void showproperties() throws IOException {

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String prompt = "\nchoose [1.jdbc.properties, 2.hadoop.properties 3.ssh_sftp.properties]: ";
		String line = null;
                   try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
                         	System.out.println("you must input 1,2 or 3\n");
                        }
                        else{
				if(line.equals("1"))
					Initial.showJdbcProperties("conf/jdbc.properties");
				else if(line.equals("2"))
					Initial.showHadoopProperties("conf/hadoop.properties");
                                else if(line.equals("3"))
                                        Initial.showSshProperties("conf/ssh_sftp.properties");
				else 
					System.out.println("you must input 1,2 or 3\n");

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }


	}


        public static void showtables() throws IOException {

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String prompt = "\nchoose [1.mysql, 2.hive]: ";
                String line = null;
                   try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
                                System.out.println("you must input 1 or 2\n");
                        }
                        else{
                                if(line.equals("1"))
					DwrcStaticService.showtables();
                                else if(line.equals("2"))
                                        HiveJdbcStaticService.showtables();
                                else
                                        System.out.println("you must input 1 or 2\n");

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }


        }


	public static void inputSqlExec() throws IOException {

		Terminal terminal1 = TerminalBuilder.builder().system(true).build();
		
		LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

        	String prompt = "input sql: ";

		String line = null;

                Boolean inputFlag = true;
                do{
		   try {
                	line = reader.readLine(prompt);
                	line = line.trim();

                	if(line == null || line.equalsIgnoreCase("")){
                         System.out.println("you must input a sql.For Example:");
                         System.out.println("select 1,2 from dual");
			}
                        else{
                          inputFlag = false;                 	
			
	                }

		   }catch(Exception e) {
			e.printStackTrace();
		   }

		}while(inputFlag);

            try {             
                 DwrcStaticService.dispSql(line," | ",CommonFuncConstants.PAGESIZE);
            }catch(Exception e) {
                 e.printStackTrace();
            }


	}


        public static void inputSqlExport() throws IOException {

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String prompt = "input sql: ";
                String line = null;
                Boolean inputFlag = true;
           

                String sql = null;
                String splitChars = "|";
                String pageFlag = "Y";
                do{
                  try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
                         System.out.println("you must input a sql.For Example:");
                         System.out.println("select 1,2 from dual");
                        }
                        else{
                          inputFlag = false;
                          sql = line;

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }

                }while(inputFlag);


                prompt = "input split char[|]: ";
                line = null;
                inputFlag = true;
                do{
                  try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(!line.equalsIgnoreCase("")&&!line.equalsIgnoreCase(",")
                          &&!line.equalsIgnoreCase("|")
                          &&!line.equalsIgnoreCase("null")){

                           System.out.println("you must input a split char as following:");
                           System.out.println(",");
                           System.out.println("null                  [means no split");
                           System.out.println("|");
                           System.out.println("                      [enter means default which is | ");
                        }
                        else{
                           inputFlag = false;
                           if(!line.equalsIgnoreCase("")
                            &&!line.equalsIgnoreCase("null")
                           ){
                              splitChars = line;
                           }else if(line.equalsIgnoreCase("null")){
                              splitChars = "";
                           }else {
                              // splitChars is default
                           } 

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }

                }while(inputFlag);

                String fileName = "new1.txt";
                prompt = "input export file name [new1.txt]: ";
                line = null;      
                inputFlag = true;
                do{          
	        try {
		    line = reader.readLine(prompt);
		    line = line.trim();

		    if(!line.equalsIgnoreCase("")){

                      	if(line.contains("\\")||line.contains("/")){
				System.out.println("\n"+line+" is error! \n export filename without directory path only!\n export file will be placed at "+CommonFuncConstants.getExportDirPath()+"\n ");
				inputFlag = true;
			}else{
				fileName = line;
				inputFlag = false;	
			}

		    }else {
			 // fileName is default
			 inputFlag = false;
			 
 		    }
						  
	        }catch(Exception e) {
		   e.printStackTrace();
	        }
                
                }while(inputFlag);

                prompt = "is paged ? Y/N [Y]: ";
                line = null;
                inputFlag = true;
                do{
                  try {
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(!line.equalsIgnoreCase("")&&!line.equalsIgnoreCase("y")
                          &&!line.equalsIgnoreCase("n")){

                           System.out.println("you must input : Y or N, or enter[which means default]");
                        }
                        else{
                           inputFlag = false;
                           if(line.equalsIgnoreCase("y")){
                              pageFlag = "Y";
                           }else if(line.equalsIgnoreCase("n")){
                              pageFlag = "N";
                           } else {
                              // pageFlag is default
                           }

                        }

                   }catch(Exception e) {
                        e.printStackTrace();
                   }

                }while(inputFlag);


                try { 
		   String filePath = CommonFuncConstants.getExportDirPath() + fileName;	
                   if(pageFlag.equalsIgnoreCase("y")){                                   
                     DwrcStaticService.exportFileBySql(sql,splitChars,CommonFuncConstants.PAGESIZE,filePath);
                   }else {
                     DwrcStaticService.exportFileBySql2(sql,splitChars,-1,filePath);
                   } 
                }catch(Exception e) {
                        e.printStackTrace();
                }


        }


        public static void importFile() throws Exception {

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String prompt = "input cfg file [import_cfg_001.properties]: ";
                String line = null;
                Boolean inputFlag = true;

                String cfgFileName = "import_cfg_001.properties";
                String fileName = null;

                do{
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
				inputFlag = false;
                                // default
                        }
                        else{
                          inputFlag = false;
                          cfgFileName = line;

                        }


                }while(inputFlag);
		
		prompt = "input data file [stu2.txt]: ";
                line = null;
                inputFlag = true;

                fileName = "stu2.txt";

                do{
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
                                inputFlag = false;
                                // default
                                
                        }
                        else{
                          inputFlag = false;
                          fileName = line;

                        }


                }while(inputFlag);

		ImportStaticService.importFile(fileName,cfgFileName);

	}

        private static void sftpdownload()  throws Exception {

                List<String> logLines = new ArrayList<String>();

                SshRemoteEntity remote = SshStaticService.getSshSftpProper("conf/ssh_sftp.properties");

                Terminal terminal1 = TerminalBuilder.builder().system(true).build();

                LineReader reader = LineReaderBuilder.builder()
                                    .terminal(terminal1)
                                    .completer(null)
                                    .build();

                String line = null;

                String prompt = "input remote directory["+remote.getHomedir()+"]:";
		String remoteDir = remote.getHomedir();

                line = reader.readLine(prompt);
                line = line.trim();

                if(line == null || line.equalsIgnoreCase("")){
                       // default
                }else 
			remoteDir = line;

		System.out.println("\nThe files at "+remoteDir+" is following: ");
		List<LsEntry> fileList = SshStaticService.getSftpFileList(remote,remoteDir,logLines);
                for(LsEntry fileObj:fileList){
                         String fileName = fileObj.getFilename();
                         System.out.println(fileName);
                }
		System.out.println("\n\n");


                Boolean inputFlag = true;
		prompt = "input remote filename:";
                String remoteFileName = null;
                do{
                        line = reader.readLine(prompt);
                        line = line.trim();

                        if(line == null || line.equalsIgnoreCase("")){
				System.out.println("you must input a remote filename.");
                        }
                        else{
                          inputFlag = false;
                          remoteFileName = line;
                        }


                }while(inputFlag);
                
                //SshStaticService.sftpDownload1File(remote.getHomedir()+"/"+"logs/nacos/","config.log",logLines);
		SshStaticService.sftpDownload1File(remoteDir,remoteFileName,logLines);
                
	}


        private static void printWelcome() {
 
		System.out.println("------------------------------------------------");
		System.out.println("|--------   dwsoft dwrc cli            --------|");
		System.out.println("|--------   common version          -----------|");
		System.out.println("|--------   ver 1.0                 -----------|");
		System.out.println("|--------   jdk 1.6/1.7/1.8             -------|");
		System.out.println("------------------------------------------------");

        }


	private static void printHelp() {
		System.out.println("Usage: [cmd] param1 [value]");
		System.out.println("cmd: testconn/showproperties/showtables/execsql/sftpdownload/importfile/exit/quit/help");
		System.out.println("Examples:");
		//System.out.println("\txxx param1 value");
                System.out.println("\ttestconn");
                System.out.println("\tshowproperties");
                System.out.println("\tshowtables");
		System.out.println("\texecsql");
                System.out.println("\timportfile");
                System.out.println("\texportsql");
                System.out.println("\tsftpdownload");
		System.out.println("\thelp");
		System.out.println("\texit");
	}

        private static void printCmdHelp(String[] cmds){
                                
                                System.out.println("Usage: [cmd] param1 [value]");
                
                                if("testconnn".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: testconn");
                                }
                                if("showtables".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: showtables");
                                }
                                else
                                if("execsql".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: execsql");
                                }
                                else
                                if("exportsql".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: exportsql");
                                }
                                else
                                if("reverse".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: reverse param1 value");
                                }
                                else
                                if("distcp".equalsIgnoreCase(cmds[0])){
                                        System.out.println("Usage: distcp param1 value");

                                }

                
        }

}



		
