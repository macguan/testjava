package cn.com.chinet.common.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.com.chinet.common.utils.date.BatchDateTimeUtil;
import cn.com.security.ss.exception.ServiceException;
import cn.com.security.ss.exception.WEException;

public class FileOperateUtil {
	private static Log logger = LogFactory.getLog(FileOperateUtil.class);
	
	/**
	 * 判断文件是否存在
	 * @param filePath
	 * @return
	 */
	public static boolean isFileExist(String filePath){
		if(StringUtils.isBlank(filePath))
			return false;
		File file = new File(filePath);
		return file.exists();
	}

	public static boolean isFileExist(String filePath ,String[] fileName){
		if(fileName != null){
			for(int i=0;i<fileName.length;i++){
				if(StringUtils.isBlank(fileName[i])){
					return false;
				}
				String fileNameTemp = fileName[i].trim();
				String fullFileName = filePath+File.separator+fileNameTemp;
				fullFileName = fullFileName.trim();
				File file = new File(fullFileName);
				if(!file.exists()){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否存在,存在就删除它
	 * @param filePath
	 * @return
	 */
	public static void checkFileNoExist(String filePath){
		if(isFileExist(filePath))
			new File(filePath).delete();
	}
	
		
	/**
	 * 判断目录是否存在,不存在就建立目录
	 * 允许按目录树建立目录
	 * @param dirPath
	 * @return
	 */
	public static boolean checkAndCreateDir(String dirPath){
		if(StringUtils.isBlank(dirPath))
			return false;
		File dirFile = new File(dirPath);
		if(!dirFile.exists()){
			dirFile.mkdirs();     // 注意,带s的方法, 是建立目录树,即父目录不存在也可连带建立
			return true;
		}else if(dirFile.isDirectory()){
			return true;
		}else
			return false;
	}
	
	/**
	 * 清空目录下文件,但不删除目录
	 * @param filePath
	 * @return
	 */
	public static boolean clearDir(String pathStr){
		if(StringUtils.isBlank(pathStr))
			return false;
		File path = new File(pathStr);
		if (path.exists()){
			File[] files =path.listFiles();
			for(File temp:files)
				if(temp.exists())
					temp.delete();
			return true;
		}else
			return false;
	}
	
	/**
	 * 读UTF8编码文件入内存字符串
	 * @param sqlFile
	 * @return
	 * @throws IOException
	 */
   public static String readUTF8File2String(String filePath) throws IOException {
	   InputStreamReader fileIn = null;
	   BufferedReader breader = null;
        try {
			fileIn = new InputStreamReader(
					new FileInputStream(filePath),"UTF-8");   
			breader = new BufferedReader(fileIn);
			
			StringBuffer sb = new StringBuffer();
			String str = null;
			while(( str = breader.readLine()) != null) {
				//logger.info("文字内容:"+str);
				sb.append(str);
			} 
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}finally{
			breader.close();
			fileIn.close();
		}
   }

	/**
	 * 读UTF8文件入内存,转换成HTML各式的字符串(回车转为<br />)
	 * @param sqlFile
	 * @return
	 * @throws IOException
	 */
  public static String readUTF8File2HTMLString(String filePath) throws IOException  {
	  
	  InputStreamReader fileIn = null;
	  BufferedReader breader = null;
       try {
    	   fileIn = new InputStreamReader(
    			   new FileInputStream(filePath),"UTF-8");   
		   breader = new BufferedReader(fileIn);
		   StringBuffer sb = new StringBuffer();
		   String str = null;
		   while(( str = breader.readLine()) != null) {
		   	//logger.info("文字内容:"+str);
		   	sb.append(str+"<br />");
		   } 
		   
		   return sb.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}finally{
			breader.close();
			fileIn.close();
		}
	}
 
	/**
	 * 读UTF8文件入内存,转换成HTML各式的字符串(回车转为<br />)
	 * @param sqlFile
	 * @return
	 * @throws IOException
	 */
  public static List<String> readUTF8File2List(String filePath) throws IOException  {
	  
	  InputStreamReader fileIn = null;
	  BufferedReader breader = null;
     try {
  	   	   fileIn = new InputStreamReader(
  	   			   new FileInputStream(filePath),"UTF-8");   
		   breader = new BufferedReader(fileIn);
		   List<String> strList = new ArrayList<String>();
		   String str = null;
		   while(( str = breader.readLine()) != null) {
		   	//logger.info("文字内容:"+str);
			strList.add(str);
		   } 
		   
		   return strList;
		   
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}finally{
			breader.close();
			fileIn.close();
		}
	}

  /**
   * 读文件,入内存,不设编码,缺省编码是
   * @param filePath
   * @return
   * @throws Exception
   */
   public static String readFile2String(String filePath) throws Exception {
	   InputStream fileIn = null;
	   try {
		   fileIn = new FileInputStream(filePath); 
		   
		   StringBuffer sb = new StringBuffer(); 
		   byte[] buff = new byte[1024];
		   int byteRead = 0;
		   while ((byteRead = fileIn.read(buff)) != -1) {
		   	String temp = new String(buff, 0, byteRead);
		   	//temp = new String(temp.getBytes(),"UTF-8");
		       sb.append(temp);
		   }
				   
		   return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e; 
		} finally{
			fileIn.close();
		}
  }
   
/*   public static String readFile2String(String filePath) throws IOException {
	   BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
       InputStream fileIn = new FileInputStream(filePath);        
       StringBuffer sb = new StringBuffer();
       byte[] buff = new byte[1024];
       int byteRead = 0;
       while ((byteRead = fileIn.read(buff)) != -1) {
       	String temp = new String(buff, 0, byteRead);
       	//temp = new String(temp.getBytes(),"UTF-8");
           sb.append(temp);
       }
       return sb.toString();
  }*/
   
   public static void copyFile(File srcFile, File destFile) throws IOException, WEException{
		if((checkFile(srcFile)==1)&&(checkFile(destFile)>=0)){
			// 如果目的文件不存在,先建新文件,再拷贝
			if(checkFile(destFile)==0){	
				
				destFile.createNewFile();
				// 用APACHE common-io.jar的文件COPY功能
				FileUtils.copyFile(srcFile, destFile);
				logger.info("文件拷贝");
			}// 如果目的文件存在,就是覆盖拷贝,如果目的文件是目录
			else if(checkFile(destFile)==1){
				FileUtils.copyFile(srcFile, destFile);
				logger.info("目的文件"+destFile.getName()+"存在,覆盖拷贝");
			}// 如果目的文件存在,且是目录,则无法拷贝
			else{
				logger.info("目的文件名与目录重名,无法拷贝,请先删目录");
				throw new WEException("目的文件名与目录重名,无法拷贝,请先删目录");
			}
		}else if((checkFile(srcFile)!=1)){
				logger.info("源文件不存在或不是文件");
				throw new WEException("源文件不存在或不是文件");
		}
		else if((checkFile(destFile)==-1)){
				logger.info("目的文件为空");
				throw new WEException("目的文件为空");
		}
			
	}

	public static boolean rawFileCopy(InputStream in,OutputStream out){
		int bytesRead;
		byte[] buffer = new byte[8192];
		try {
			// 一次读8k字节,COPY
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
		}
		return true;
		
	}
	
	public static void copyFile2Dir(String dirPath,String fileName,File file1) {

		FileInputStream fis = null;
		FileOutputStream fo = null;
			File file = file1;
			try {
				fis = new FileInputStream(file);
				try {
					createDir(dirPath);
				} catch (WEException e) {
					e.printStackTrace();
				}
				fo = new FileOutputStream(new File(dirPath, fileName));
				byte[] buf = new byte[4096*5];//最大为10M
				int len = -1;
				while ((len = fis.read(buf)) != -1) {
					fo.write(buf, 0, len);
				}
				fo.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		try {
			if (fis != null) {
				fis.close();
				fo.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void delFile(String filePath) {
		File srcFile = new File(filePath);
		try{
			if((checkFile(srcFile)==1)){
				FileUtils.forceDelete(srcFile);
				logger.info(filePath+"被删除");
			}else if((checkFile(srcFile)==2)){
					logger.warn(filePath+"是目录");
					throw new WEException(filePath+"是目录");
			}else if((checkFile(srcFile)<=0)){
				logger.warn("文件不存在或为空");
				throw new WEException("文件不存在或为空");
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}catch(WEException wee){
			wee.printStackTrace();
		}
			
	}
	
	/**
	 * 建目录
	 * 如果路径不存在,则建目录,如果路径存在且目录,则不发生变化,保留目录不变,//如果路径存在且是文件,则先删文件,再建目录
	 * @author guanxiaoyu
	 * @param pathString
	 * @throws IOException 
	 * @throws WEException
	 * @throws IOException
	 * @throws WEException 
	 */
	public static void createDir(String pathString) throws IOException, WEException{
		if(StringUtils.isBlank(pathString))
			throw new WEException("路径为空");
		File path1= new File(pathString);
		int checkResult = checkPath(path1);
		if(checkResult != -1){
			//如果路径不存在,则建目录		
			if(checkResult==0){
				FileUtils.forceMkdir(path1);
				logger.info("建目录"+pathString);
			}
			//如果路径存在且目录,则不发生变化,保留目录不变
			else if((checkResult==2)){
				return ;
			//如果路径存在且是文件,//则先删文件,再建目录
			}else if((checkResult==1)){
				logger.info("建目录失败,有同名文件存在");
				throw new WEException("建目录失败,有同名文件存在");
				//path1.delete();
				//FileUtils.forceMkdir(path1);
			}
				
		}

	}

	
	/**
	 * 整体删目录,递归删目录下的子目录和文件
	 * @author guanxiaoyu
	 * @param pathString
	 * @throws IOException
	 * @throws WEException
	 */
	public static void delDir(String filepath) throws IOException{
		File f = new File(filepath);//定义文件路径 
		
		if(f.exists() && f.isDirectory()){//判断是文件还是目录   

			   if(f.listFiles().length==0){//若目录下没有文件则直接删除   
			       f.delete();   
			    }else{
			    	//若有则把文件放进数组，并判断是否有下级目录   
			      File delFile[]=f.listFiles();   
			      int i =f.listFiles().length;   
			      for(int j=0;j<i;j++){   
			         if(delFile[j].isDirectory()){   
			             delDir(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径   
			            }
			         delFile[j].delete();//删除文件
			      } //end of for
			     }   // end of else
			   
		}   // end of if      
	} 
	/**
	 * 检查目录路径,是否存在,不存在就创建它
	 * @param list
	 * @throws ServiceException
	 */
	public static void isNotExistNewBuild(List<String> list) {
		if(list==null||list.size()==0){
			return;
		}
		for(String path : list){
			File file = new File(path);
			if(!file.exists()){
				file.mkdir();
			}
		}
	}
	
	/**
	 * 检查路径
	 * 先判空返回-1,再判是否存在返回0,再判存在是文件返回1或目录返回2
	 * @author guanxiaoyu
	 * @param file1
	 * @return
	 */
	public static int checkPath(File file1){
		if(file1 == null)
			return -1;
		if(file1.exists()){
			if(file1.isFile())
				return 1;
			if(file1.isDirectory())
				return 2;
		}
		return 0;
	}
	
	/**
	 * 检查源文件的合法性
	 * @author guanxiaoyu
	 * @param srcFile
	 * @return
	 */
	public static int checkFile(File srcFile){
		if(srcFile == null)
			return -1;
		if(!srcFile.exists())
			return 0;
		if(srcFile.isFile())
			return 1;
		else
			return 2;
	}

	/**
	 * 读取指定目录下的文件名列表
	 * 
	 */
	public static List<String> listFile(String path)
	{
		List<String> fileNameList = new ArrayList<String>();
		
		File filePath = new File(path);
		if(filePath.isDirectory())
		{
			String[] files = filePath.list();
			CollectionUtils.addAll(fileNameList, files);
		}
		return fileNameList;
	}
	
	/**
	 * 读取指定目录下，指定Ext扩展名的文件名列表。
	 * 
	 */
	public static List<String> listFile(String path, final String...exts)
	{
		List<String> fileNameList = new ArrayList<String>();
		
		File filePath = new File(path);
		if(filePath.isDirectory())
		{
			String[] files = filePath.list(new FilenameFilter(){
				public boolean accept(File dir, String name)
				{
					for(String ext : exts)
					{
						if(StringUtils.endsWithIgnoreCase(name, ext))
						{
							return true;
						}
					}
					
					return false;
				}
			});
			
			CollectionUtils.addAll(fileNameList, files);
		}
		
		return fileNameList;
	}
	
	/**
	 * 将字符串写入文件
	 * @param filePath 文件绝对路径
	 * @param str 需要写入的字符串
	 * @param append 是否追加到现有文件，true 追加；false 不追加
	 * @return true 写入成功；false 写入失败
	 * @throws IOException
	 *
	 */
	public static boolean write(String filePath, String str, boolean append) throws IOException
	{
		File file = new File(filePath);
		if(!file.exists())
		{
			if(!file.createNewFile())
			{
				return false;
			}
		}
		
		FileWriter writer = new FileWriter(file, append);
		try
		{
			writer.write(str);
			writer.flush();
		}
		finally
		{
			if(writer != null)
			{
				writer.close();
			}
		}
		
		return true;
	}
	
	/**
	 * 在文件中写入一行字符串
	 *
	 */
	public static boolean writeLine(String filePath, String str, boolean append) throws IOException
	{
		return write(filePath, str+"\n", append);
	}
	
	/**
	 * 将一个字符串列表写入文件
	 * @param filePath
	 * @param strList
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public static boolean writeList(String filePath, List<String> strList, boolean append) throws IOException
	{
		File file = new File(filePath);
		if(!file.exists())
		{
			if(!file.createNewFile())
			{
				return false;
			}
		}
		
		FileWriter writer = new FileWriter(file, append);
		try
		{
			for(String _line:strList){
				writer.write(_line+"\r\n");  // windows文本文件是\r\n, linux是\n, 因为写出的文件经常用于下载到windows下,所以用windows
			}
			writer.flush();
		}
		finally
		{
			if(writer != null)
			{
				writer.close();
			}
		}
		
		return true;
	}
	
	/**
	 * 文件改名、移动，注意文件名必须是绝对路径
	 * 当源文件不存在、目标文件已经存在、目标文件没有操作权限时返回false
	 */
	public static boolean renameFile(String srcName, String destName)
	{
		return renameFile(srcName, destName, false);
	}
	
	/**
	 * 文件改名、移动，注意文件名必须是绝对路径
	 * 当源文件不存在、目标文件已经存在、目标文件没有操作权限时返回false
	 * isOver = true 时，如果目标文件存在则删除目标文件。
	 */
	public static boolean renameFile(String srcName, String destName, boolean isOver)
	{
		File srcFile=new File(srcName);
		if(srcFile.exists())
		{
			File destFile = new File(destName);
			if(destFile.exists())
			{
				if(isOver)
				{
					destFile.delete();
				}else{
					return false;
				}
			}
			
			return srcFile.renameTo(destFile);
		}
		return false;
	}
	
	
	//-----------------------------------------------------------------
	
	/**
	 * 在路径尾部加上/
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public static String addPathSuffix(String path){
		if(StringUtils.isBlank(path))
			return "";
		
		path = path.replace("\\","/");
		if(!path.endsWith("/")){
			return path + "/";
		}else
			return path;			
	}
	

	
	/**
	 * 在路径尾部去掉/
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public static String dropPathSuffix(String path){
		if(StringUtils.isBlank(path))
			return "";
		path = path.replace("\\","/");
		if(path.endsWith("/")){
			return StringUtils.substring(path, 0,path.length()-1);
		}else
			return path;			
	}
	
	/**
	 * 路径开头添加/
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public static String addPathPrefix(String path){
		if(StringUtils.isBlank(path))
			return "";
		path = path.replace("\\","/");
		if(!path.startsWith("/")){
			return "/" + path;
		}else
			return path;			
	}
	/**
	 * 路径开头去掉/
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public static String dropPathPrefix(String path){
		if(StringUtils.isBlank(path))
			return "";
		path = path.replace("\\","/");
		if(path.startsWith("/")){
			return StringUtils.substring(path, 1);
		}else
			return path;			
	}
	
	/**
	 * 转换路径中的斜杠为当前路径杠
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public static String transPathSPT(String path){
		if(StringUtils.isBlank(path))
			return "";	
		if(StringUtils.contains(path,'/'))
			path = StringUtils.replaceChars(path, '/', File.separatorChar);
		if(StringUtils.contains(path,'\\'))
			path = StringUtils.replaceChars(path, '\\', File.separatorChar);;
		return path;
	}
	
	
	//---------------------------文件名和路径操作---------------------------------------------------//
	/**
	 * 获取文件路径(不带文件名)
	 */
	public static String getPath(String fullFileName){
		if(StringUtils.isBlank(fullFileName))
			return "";
		fullFileName = fullFileName.replace("\\","/");
		if(fullFileName.contains("/")){
			return fullFileName.substring(0,fullFileName.lastIndexOf("/")+1);				
		}else{
			return "";				
		}
	}
	/**
	 * 获取文件文件名(不带路径)
	 */
	public static String getFileName(String fullFileName){
		if(StringUtils.isBlank(fullFileName))
			return "";
		fullFileName = fullFileName.replace("\\","/");
		if(fullFileName.contains("/")){
			return fullFileName.substring(fullFileName.lastIndexOf("/")+1,fullFileName.length());				
		}else{
			return fullFileName;				
		}
	}

	/**
	 * 获取文件文件名(不带路径,不带扩展名)
	 */
	public static String getFileName_noExtend(String fullFileName){
		String fileName = getFileName(fullFileName);
		if(StringUtils.isBlank(fileName))
			return "";
		// 用lastIndex针对文件名中带多个.的情况
		int pointPos = fileName.lastIndexOf(".");
		// 找到最后一个.
		if(pointPos != -1){
			return fileName.substring(0,pointPos);
		}
		// 不含有.
		else{
			return fileName;
		}
		
	}
	
	/**
	 * 获取文件扩展名
	 * @param fullFileName
	 * @return
	 */
	public static String getExtendName(String fullFileName){
		if(StringUtils.isBlank(fullFileName)||!fullFileName.contains(".")){
			return "";
		}else{
			//return fullFileName.split("\\.")[1];   // 不严密,如果出现如 test4.0.xls时,就会取错
			String[] _ss = fullFileName.split("\\.");
			return _ss[_ss.length-1];
		}
		
	}
	
	/**
	 * 获取无批次文件名(把批次从文件名中去掉)
	 * 注: 本方法不做判断,就假定入参一定是包含批次号的,所以要求调用时小心
	 * @param fileName
	 * @param batchLength
	 * @return
	 */
	public static String getNoBatchName(String fileName,Integer batchLength){
		String extName = getExtendName(fileName);
		String retFileName = getFileName_noExtend(fileName);
		
		return retFileName.substring(0,retFileName.length()-batchLength)+"."+extName;
	}
	
	//--------------------------------------------------------------------------------
	
	/**
	 * 文件名加17位批次号
	 * @param fileName
	 * @return
	 */
	public static String fileNameAddBatch(String fileName,Integer batchType){
		if(StringUtils.isBlank(fileName))
			return "";
		if(batchType==17)
			return getFileName_noExtend(fileName)+BatchDateTimeUtil.getBatch17()
				+"."+getExtendName(fileName);
		else if(batchType==9)
			return getFileName_noExtend(fileName)+BatchDateTimeUtil.getBatch9()
			+"."+getExtendName(fileName);
		else
			return getFileName_noExtend(fileName)+BatchDateTimeUtil.getBatch7()
			+"."+getExtendName(fileName);
	}

	/**
	 * 生成批次文件名+后三位随机数
	 * @param fileName
	 * @return
	 */
	public static String batchFileName(Integer batchType,String extendName){
		String fileName = "";
		if(batchType==17)
			fileName=BatchDateTimeUtil.getBatch17();
		else if(batchType==9)
			fileName=BatchDateTimeUtil.getBatch9();
		else
			fileName=BatchDateTimeUtil.getBatch7();
		
		fileName += new Random().nextInt(100);
		
		if(StringUtils.isNotBlank(extendName))
			fileName += "."+extendName;
		return fileName;
	}
	

	/**
	 * 文件名检查
	 * 检查是否存在同名文件,存在就在文件名后加随机数,然后递归继续检查,直至没有同名文件
	 */
	public static String checkFileName(String path,String fileName){
		if(StringUtils.isBlank(path)||StringUtils.isBlank(fileName))
			return "";
		String fullPath = addPathSuffix(path)+fileName;
		if(isFileExist(fullPath)){
			Random ran = new Random();			
			return checkFileName(path,getFileName_noExtend(fileName)+ran.nextInt(100)
						+"."+getExtendName(fileName));
		}else
			return fileName;
	}

	//------------------------------------------------------------------------------
	/**
	  * 判断当前操作系统是不是window
	  * 
	  */
	 public static boolean isWindows(){
	  boolean flag = false;
	  if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
	   flag = true;
	  }
	  return flag;
	 }

	 public static String getOSName(){
		 return System.getProperties().getProperty("os.name");
		}
	 
	public void xxx() {
		File test = new File("test.txt");
		long fileLength = test.length();
		LineNumberReader rf = null;
		try {
			rf = new LineNumberReader(new FileReader(test));
			if (rf != null) {
				int lines = 0;
				rf.skip(fileLength);
				lines = rf.getLineNumber();
				rf.close();
			}
		} catch (IOException e) {
			if (rf != null) {
				try {
					rf.close();
				} catch (IOException ee) {
				}
			}
		}
	}
	
	public static void main(String args[]) throws IOException, WEException{
		/*String fn = fileNameAddBatch("filename.xls",17);		
		System.out.println(fn);
		String fnNoBatch= getNoBatchName(fn,17);
		System.out.println(fnNoBatch);*/
		//System.out.println(isWindows());
		//System.out.println(getOSName());
/*		String path = "\\test/test1\\hello.txt";
		System.out.println(getPath(path)+"|"+getFileName(path)+"|"+getFileName_noExtend(path)+"|"+getExtendName(path));
		path = "hello.txt";
		System.out.println(getPath(path)+"|"+getFileName(path)+"|"+getFileName_noExtend(path)+"|"+getExtendName(path));
		path = "\\test/test1\\hello";
		System.out.println(getPath(path)+"|"+getFileName(path)+"|"+getFileName_noExtend(path)+"|"+getExtendName(path));
*/
		System.out.println(getFileName("/xxx1/yyy2/xxx.excel"));
		System.out.println(getPath("/xxx1/yyy2/xxx.excel"));
		System.out.println(getFileName("xxx.excel"));
		System.out.println(getPath("xxx.excel"));
		
	}
}
