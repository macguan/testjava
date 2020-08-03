package cn.com.dwsoft.main;

import jline.console.ConsoleReader;

import java.io.IOException;

public class TestJLine {


	public static void main(String[] args)throws IOException {
		

		ConsoleReader reader = new ConsoleReader();
		String line;

                boolean firstRunFlag = true;
		do{
			try {
                                if(firstRunFlag){
                                  printWelcome();
                                  firstRunFlag = false;
                                  line = "help";
                                }else {
                                  line = reader.readLine("> ");
                                }

			if(line == null || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit"))
					break;
				
				// 开始解析,先把输入命令用空格拆分开	
				String[] cmds = line.split(" ");
				
				// 命令(首词)判断
				if("get".equalsIgnoreCase(cmds[0])){
					System.out.println("你的命令是:"+cmds[0]);
					System.out.println("你的命令参数是:"+cmds[1]+" "+cmds[2]);
				}
				else
				if("mget".equalsIgnoreCase(cmds[0])){
					System.out.println("你的命令是:"+cmds[0]);
					System.out.println("你的命令参数是:"+cmds[1]+" "+cmds[2]);
				}
				else
				if("set".equalsIgnoreCase(cmds[0])){
					System.out.println("你的命令是:"+cmds[0]);
					System.out.println("你的命令参数是:"+cmds[1]+" "+cmds[2]);
				}
				else
				if("help".equalsIgnoreCase(cmds[0])){
					printHelp();
				}
				else{
					System.out.println("Unknown command.");
					printHelp();
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Wrong arguments.");
				printHelp();
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}while(true);

		System.exit(0);
	}

        private static void printWelcome() {
 
		System.out.println("------------------------------------------------");
		System.out.println("|--------   dwsoft cmd cli demo        --------|");
		System.out.println("|--------   common version          -----------|");
		System.out.println("|--------   ver 1.0                 -----------|");
		System.out.println("|--------   jdk 1.6/1.7/1.8             -------|");
		System.out.println("------------------------------------------------");

        }


	private static void printHelp() {
		System.out.println("Usage: [cmd] param1 [value]");
		System.out.println("cmd: get/set/exit/help");
		System.out.println("Examples:");
		System.out.println("\tset param1 value");
		System.out.println("\tget param1 key");
		System.out.println("\thelp");
		System.out.println("\texit");
	}

}



		
