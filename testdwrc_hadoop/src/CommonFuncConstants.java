package cn.com.corecenter.constants;

import cn.com.dwsoft.util.file.PropertiesParser;
import java.io.File;

public class CommonFuncConstants {
	
	public static String ROOTDIR = null;

	public static String EXPORTDIR = "export";
        public static String IMPORTDIR = "import";

        public static Integer debugLevel = 0;

        public static Integer PAGESIZE = 5;

	
	static {

        	ROOTDIR = getCurPath();

	}


	private static String getCurPath(){
		File file1 = new File("test1");
		String _aaaPath =file1.getAbsolutePath();
                return _aaaPath.substring(0,_aaaPath.length()-5);
	}

	public static String getExportDirPath(){
		
		if(ROOTDIR.endsWith("/")){
			return ROOTDIR + EXPORTDIR + "/";
		}else if(ROOTDIR.endsWith("\\")){
			return ROOTDIR + EXPORTDIR + "\\";
		}else {
			return ROOTDIR + File.separator + EXPORTDIR + File.separator;
		}

	}

        public static String getImportDirPath(){

                if(ROOTDIR.endsWith("/")){
                        return ROOTDIR + IMPORTDIR + "/";
                }else if(ROOTDIR.endsWith("\\")){
                        return ROOTDIR + IMPORTDIR + "\\";
                }else {
                        return ROOTDIR + File.separator + IMPORTDIR + File.separator;
                }

        }


        public static String getImportTemplateDirPath(){

                if(ROOTDIR.endsWith("/")){
                        return ROOTDIR + IMPORTDIR + "/"+ "template" + "/";
                }else if(ROOTDIR.endsWith("\\")){
                        return ROOTDIR + IMPORTDIR + "\\"+ "template" + "\\";
                }else {
                        return ROOTDIR + File.separator + IMPORTDIR + File.separator+ "template" + File.separator;
                }

        }


}

