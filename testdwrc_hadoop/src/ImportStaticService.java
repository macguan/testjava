package cn.com.dwsoft.service;

import cn.com.dwsoft.util.jdbc.JdbcUtil;

import cn.com.corecenter.constants.CommonFuncConstants;
import cn.com.chinet.common.utils.file.FileOperateUtil;
import cn.com.chinet.common.utils.date.BatchDateTimeUtil;

import cn.com.dwsoft.entity.ImportCfgEntity;
import cn.com.dwsoft.entity.ImportColumnCfgEntity;
import cn.com.dwsoft.entity.ImportResultEntity;
import cn.com.dwsoft.entity.RetData;

import cn.com.dwsoft.util.file.PropertiesParser;

import cn.com.dwsoft.util.string.StringDealUtil;

import cn.com.security.ss.exception.WEException;

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

import java.io.BufferedReader;
import java.io.FileReader;


public class ImportStaticService {


    public static void importFile(String filePath,String cfgFileName) throws Exception{

                List<String> logLines = new ArrayList<String>();

                exportLogs("-------------------check data file---------------------------------",logLines);

		if(!FileOperateUtil.isFileExist(filePath)){ 
			exportLogs(filePath+" is not exist!",logLines);
			throw new Exception(filePath+" is not exist!");
		}
		exportLogs(filePath,logLines);
                exportLogs("data file exist.",logLines);

		String curBatch = BatchDateTimeUtil.getBatch12();
		String curDataDir = FileOperateUtil.getPath(filePath);
                String _dataFileName = FileOperateUtil.getFileName(filePath);
                String curBatchName = FileOperateUtil.getFileName_noExtend(filePath)+"_"+curBatch;
		String curResultFileName = curDataDir + curBatchName + ".result"+".txt";
		String curLogFileName = curDataDir + curBatchName + ".log";
		String curErrorLinesFileName = curDataDir + curBatchName + ".errorline"+".txt";

		ImportResultEntity _result= new ImportResultEntity();
		
		_result.setImportFilePath(filePath);
		_result.setResultFilePath(curResultFileName);
		_result.setLogFilePath(curLogFileName);
		_result.setErrorLinesFilePath(curErrorLinesFileName );

		_result.setStartTimeMs(System.currentTimeMillis());
                exportLogs("-------------------check cfg(import template) file-----------------",logLines);	
		if(cfgFileName.contains("\\")||cfgFileName.contains("/")){
			exportLogs("\n"+cfgFileName+" is error! \n filename without directory path only!\n cfg file must be placed at "+CommonFuncConstants.getImportTemplateDirPath()+"\n ",logLines);
			throw new Exception("\n"+cfgFileName+" is error! \n filename without directory path only!\n cfg file must be placed at "+CommonFuncConstants.getImportTemplateDirPath()+"\n ");
		}		


		String cfgFilePath = CommonFuncConstants.getImportTemplateDirPath()+cfgFileName;
		if(!FileOperateUtil.isFileExist(cfgFilePath)) {
			exportLogs(cfgFilePath+" is not exist!",logLines);
			throw new Exception(cfgFilePath+" is not exist!");
		}
                exportLogs(cfgFilePath,logLines);
                exportLogs("cfg file exist.",logLines);

                ImportCfgEntity _cfg = null;
		try {   
			_cfg = getCfgByPropertiesName(cfgFilePath);
			if(_cfg.getDisp_modeFlag())
				dispImportCfg(_cfg,logLines);
		}catch (Exception e) {
			e.printStackTrace();
			exportLogs(e.getMessage(),logLines);
			//throw new Exception(e.getMessage());
                }
		

		List<String> totalErrorLines = new ArrayList<String>();
		totalErrorLines.add("-------------------Found following lines with errors----------------------------------");

		if(_cfg != null){
			_result.setCfgCode(_cfg.getCfgCode());

/*
		List<ImportColumnCfgEntity> keyList = new ArrayList<ImportColumnCfgEntity>();
		keyList.add(new ImportColumnCfgEntity("sname","姓名","varchar",1));
		keyList.add(new ImportColumnCfgEntity("age","年龄","int",2));
		keyList.add(new ImportColumnCfgEntity("sid","编号","varchar",3));
		
		_cfg.setCfgCode("cfg_001");
                _cfg.setCfgName("cfg_001");
		_cfg.setSplitChar("\\|");
                _cfg.setDbTableName("stu1");
                _cfg.setColCfgList(keyList); 
*/


		FileReader fr = null;
                BufferedReader br = null;
		Boolean continueFlag = true;
		try {

	                fr = new FileReader(filePath);
                        br = new BufferedReader(fr);

			int page_import_size  = _cfg.getPage_import_size();
                        if(!_cfg.getPage_import_modeFlag())
                          page_import_size  = 500000;

                        int current_page_num = 1;

			exportLogs("-------------------start read txt file-----------------------------",logLines);
                    while(continueFlag){
			exportLogs("--------current read page:"+current_page_num+"--------------------------",logLines);

			RetData retVal = readFile2List(br,_cfg,page_import_size,logLines);
			List<Map<String,String>> contentLines = retVal.getCorrectLines();
                	List<String> errorLines = retVal.getErrorLines();
                        if(errorLines != null && errorLines.size()>0){
                                totalErrorLines.addAll(errorLines);
                        }

			if(retVal.isEnd()){
				continueFlag = false;
                        } else {
				current_page_num++;
			}

			_result.setCorrectTxtLineCount(_result.getCorrectTxtLineCount()+contentLines.size());

			exportLogs("### [READ TXT] total correct count:"+contentLines.size(),logLines);
                	if(_cfg.getDisp_modeFlag())
                        	dispImportLines(contentLines, _cfg,logLines);
			exportLogs("### [READ TXT] total error count:"+errorLines.size(),logLines);
			dispLines(errorLines,logLines);		

                	List<String> sqlList = convertLineToInsertSqlLines(contentLines, _cfg);
			exportLogs("### [INSERT DB] generate sql command. total count:"+sqlList.size(),logLines);
                	if(_cfg.getDisp_modeFlag()) 
				dispLines(sqlList,logLines);

                	execSqlLines(sqlList);
                	exportLogs("### [INSERT DB] execute insert. total count:"+sqlList.size(),logLines);
	
		   }// end of while(continue)

		} catch (Exception e) {
                	e.printStackTrace();
                } finally {

                        try {
                        	if (fr != null) fr.close();
                                if (br != null) br.close();
				

                        } catch (Exception e) {
                                e.printStackTrace();
                       	} // end of try


		} // end of finally



		}// end of if(_cfg != null){
		else {
			_result.setCfgCode(null);
		}		

                exportLogs("-------------------final complete----------------------------------",logLines);
		
		if(totalErrorLines.size()>1){
			_result.setErrorTxtLineCount(totalErrorLines.size()-1);
			FileOperateUtil.writeList(_result.getErrorLinesFilePath(),totalErrorLines,true);
		}
		_result.setTotalTxtLineCount(_result.getCorrectTxtLineCount()+_result.getErrorTxtLineCount());

		System.out.println("curLogFileName:"+curLogFileName);		
		FileOperateUtil.writeList(_result.getLogFilePath(),logLines,false);

		_result.setEndTimeMs(System.currentTimeMillis());
		

		List<String> resultLines = new ArrayList<String>();

                exportLogs(" start time : "+_result.getStartTimeHuman(),resultLines);
                exportLogs(" end time : "+_result.getEndTimeHuman(),resultLines);
                exportLogs(" Elapsed time : "+_result.getIssueTimeHuman(),resultLines);

               	exportLogs(" txt file lines : "+_result.getTotalTxtLineCount(),resultLines);
                exportLogs(" correct lines : "+_result.getCorrectTxtLineCount(),resultLines);
                exportLogs(" error lines : "+_result.getErrorTxtLineCount(),resultLines);

                exportLogs(" import template cfg code : "+_result.getCfgCode(),resultLines);
                exportLogs(" import data file : "+_result.getImportFilePath(),resultLines);
                exportLogs(" result file : "+_result.getResultFilePath(),resultLines);
                exportLogs(" log file : "+_result.getLogFilePath(),resultLines);
                exportLogs(" error lines file : "+_result.getErrorLinesFilePath(),resultLines);
		
		FileOperateUtil.writeList(_result.getResultFilePath(),resultLines,false);

	}



    public static ImportCfgEntity getCfgByPropertiesName(String filePath) throws Exception{


                ImportCfgEntity _cfg = null;

                PropertiesParser paser = null;
                try{
			 paser = new PropertiesParser(filePath);
        		 if(paser!=null){
                 		String   cfgcode= paser.getInfoFromConfiguration("cfgcode");
				if(cfgcode == null || cfgcode.equals("")){
					throw new Exception(filePath+ " error! cfgcode is null.");
				}	
 
                                String   cfgname= paser.getInfoFromConfiguration("cfgname"); 
                                if(cfgname == null || cfgname.equals("")){
                                        throw new Exception(filePath+ " error! cfgname is null.");
                                }

                                String   disp_mode= paser.getInfoFromConfiguration("disp_mode");
                                Boolean  disp_modeFlag = false;
                                if(disp_mode != null && !disp_mode.equals("")){
                                     if(disp_mode.equalsIgnoreCase("true"))
                                         disp_modeFlag = true;
                                     else if(disp_mode.equalsIgnoreCase("false"))
                                         disp_modeFlag = false;
                                     else
                                        throw new Exception(filePath+ " error! disp_mode must be true or false.");
                                }
					

                                String   page_import_mode= paser.getInfoFromConfiguration("page_import_mode");
                                Boolean  page_import_modeFlag = false;
                                if(page_import_mode != null && !page_import_mode.equals("")){
                                     if(page_import_mode.equalsIgnoreCase("true"))
                                         page_import_modeFlag = true;
                                     else if(page_import_mode.equalsIgnoreCase("false"))
                                         page_import_modeFlag = false;
                                     else
                                        throw new Exception(filePath+ " error! page_import_mode must be true or false.");
                                }

				String   page_import_size  = paser.getInfoFromConfiguration("page_import_size");
				int page_import_sizeVal = 0;
				if(page_import_size != null && !page_import_size.equals("")){
                                        try{
                                                page_import_sizeVal = Integer.parseInt(page_import_size);
						
                                        }catch(Exception e){
                                                e.printStackTrace();
                                                throw new Exception(" page_import_size error! it must be a number.");
                                        }
                                        
                                }


                                String   dbtablename= paser.getInfoFromConfiguration("dbtablename"); 
                                if(dbtablename == null || dbtablename.equals("")){
                                        throw new Exception(filePath+ " error! dbtablename is null");
                                }


                                String   splitchar= paser.getInfoFromConfiguration("splitchar");
                                if(splitchar == null || splitchar.equals("")){
                                        throw new Exception(filePath+ " error! splitchar error");
                                }else if(!splitchar.equalsIgnoreCase("bar")&&!splitchar.equalsIgnoreCase("comma")){
                                        throw new Exception(filePath+ " error! splitchar must be bar(means |) or comma(means ,)");
                                }


                                List<ImportColumnCfgEntity> keyList = new ArrayList<ImportColumnCfgEntity>();

                                String   cfgcol= paser.getInfoFromConfiguration("cfgcol");
                                if(cfgcol == null || cfgcol.equals("")){
                                        throw new Exception(filePath+ " error! cfgcol is null");
                                }


                                String[] cols = cfgcol.split(",");
                                for(int j=0;j<cols.length;j++){
                                        String _item = null;
                                        String _itemval= null;

                                        _item = cols[j];
                                        if(_item == null || _item.equals("")){
                                                throw new Exception(filePath+ " error! cfgcol error");
                                        }                                        

					_itemval= paser.getInfoFromConfiguration(_item);
                                        if(_itemval == null || _itemval.equals("")){
	                                        throw new Exception(filePath+ " error!"+_item+" must have a config line.such as:sname=sname|sname|varchar|1" );
        	                        }

                                        String[] valas = _itemval.split("\\|");
                                        if(valas.length<4){
						throw new Exception(filePath+ " error!"+_item+" must have a config line.such as:sname=sname|sname|varchar|1");	
					}
					if(valas[0] == null || valas[0].equals("")){
                                                throw new Exception(filePath+ " error!"+_item+" must have a config line.such as:sname=sname|sname|varchar|1");
                                        } 
                                        if(valas[2] == null || valas[2].equals("")){
                                                throw new Exception(filePath+ " error!"+_item+" must have a config line.such as:sname=sname|sname|varchar|1");
                                        }
                                        if(valas[3] == null || valas[3].equals("")){
                                                throw new Exception(filePath+ " error!"+_item+" must have a config line.such as:sname=sname|sname|varchar|1");
                                        }
                                        
					try{
						Integer.parseInt(valas[3]);
					}catch(Exception e){
			                        e.printStackTrace();
                        			throw new Exception(_itemval+" error! last "+valas[3]+" must be a number.");
					}
		
                                        keyList.add(new ImportColumnCfgEntity(valas[0],valas[1],valas[2]
                                                        ,Integer.parseInt(valas[3])));
                                }

                                _cfg = new ImportCfgEntity();

                                _cfg.setCfgCode(cfgcode);
                                _cfg.setCfgName(cfgname);
				_cfg.setDisp_modeFlag(disp_modeFlag);
				_cfg.setPage_import_modeFlag(page_import_modeFlag);
				if(page_import_sizeVal > 0) _cfg.setPage_import_size(page_import_sizeVal);

                                if(splitchar.equalsIgnoreCase("bar"))
                                        _cfg.setSplitChar("\\|");
                                else if(splitchar.equalsIgnoreCase("comma"))
                                        _cfg.setSplitChar(",");

                                _cfg.setDbTableName(dbtablename);

                                _cfg.setColCfgList(keyList);


                        } // end of if
                
			return _cfg;

                }catch(WEException e){
			throw new Exception(e.getMessage());

                }finally{

                        if(paser != null) paser.closeConfiguration();


                } // end of finally

    }


    private static void dispImportCfg(ImportCfgEntity _cfg,List<String> logLines){

	exportLogs("----------------import template cfg---------------------------------",logLines);
                        if(_cfg != null){
                                
                                exportLogs("CfgCode="+_cfg.getCfgCode(),logLines);
                                exportLogs("CfgName="+_cfg.getCfgName(),logLines);
                                exportLogs("SplitChar="+_cfg.getSplitChar(),logLines);
                                exportLogs("DbTableName="+_cfg.getDbTableName(),logLines);
 
                               
				exportLogs("disp_mode="+_cfg.getDisp_modeFlag(),logLines);
				exportLogs("page_import_mode="+_cfg.getPage_import_modeFlag(),logLines);
                                exportLogs("page_import_size="+_cfg.getPage_import_size(),logLines);
 
                                exportLogs("--------- column config: ",logLines);                               
                                for(int i=0;i<_cfg.getColCfgList().size();i++){
                                        ImportColumnCfgEntity _colcfg = _cfg.getColCfgList().get(i);
                                        exportLogs(_colcfg.getColSqlName()+"|"+_colcfg.getColChineseName()+"|"+_colcfg.getColType()+"|"+_colcfg.getSplitedNo(),logLines);                        

                                }
	
			}	

       exportLogs("-------------------end---------------------------------------------",logLines);

	}

    private static void dispImportLines(List<Map<String,String>> contentLines,ImportCfgEntity _cfg,List<String> logLines){
                                        
		List<ImportColumnCfgEntity> keyList = _cfg.getColCfgList();

                for(Map<String,String> _lineMap:contentLines){
		     StringBuilder _sb = new StringBuilder();	
                     for(int j=0;j<keyList.size();j++){
                        _sb.append(keyList.get(j).getColChineseName()).append(":").append(_lineMap.get(keyList.get(j).getColSqlName())).append("|");
                        //exportLogs(keyList.get(j).getColChineseName()+":"+_lineMap.get(keyList.get(j).getColSqlName())+"|");
                     }
                    exportLogs(_sb.toString(),logLines);
                }


   }	

    private static void dispLines(List<String> lines,List<String> logLines){

                for(String _line:lines){

			exportLogs(_line,logLines);
                }


   }

    private static void exportLogs(String line,List<String> logLines){

	System.out.println(line);
	logLines.add(line);


   }



    private static void execSqlLines(List<String> sqlLines){
		/*
                for(String _line:sqlLines){

                    System.out.println(_line);
                }*/

		//JdbcUtil.executeBatchSql("conf/jdbc.properties",sqlLines);
		JdbcUtil.executeUpdateSql("conf/jdbc.properties",sqlLines);

   }

   private static RetData readFile2List(BufferedReader br,ImportCfgEntity _cfg,int pageSize,List<String> logLines)  throws Exception{

                RetData retVal = new RetData();

                List<Map<String,String>> correctLines = new ArrayList<Map<String,String>>();
                List<String> errorLines = new ArrayList<String>();
		
		int linenum=1;
                String _line = null;
                Map<String,String> _lineMap = null;

                while ( linenum <= pageSize && (_line = br.readLine()) != null ) {
                           _lineMap = readLine2Map(_line,_cfg,logLines);
                           if(_lineMap != null)
                               correctLines.add(_lineMap);
                           else
                               errorLines.add(_line);

			   linenum++;
                 }

                 retVal.setCorrectLines(correctLines);
                 retVal.setErrorLines(errorLines);
		 if(_line == null)
		 	retVal.setEndFlag(true);
                   else
			retVal.setEndFlag(false);	

                 return retVal;

   }
	

    private static RetData readFile2List_old(String filePath,ImportCfgEntity _cfg,List<String> logLines) throws Exception{

		FileReader fr = null;
		BufferedReader br = null;
		
		RetData retVal = new RetData();

                List<Map<String,String>> correctLines = new ArrayList<Map<String,String>>();
		List<String> errorLines = new ArrayList<String>();

     		try {
				fr = new FileReader(filePath);
				br = new BufferedReader(fr);

				String _line = null;
				Map<String,String> _lineMap = null;
				while ((_line = br.readLine()) != null) {
                                        _lineMap = readLine2Map(_line,_cfg,logLines);
					if(_lineMap != null) 
						correctLines.add(_lineMap);
					else
						errorLines.add(_line);
				}
				

			} catch (Exception e) {
					e.printStackTrace();
			} finally {
                     		try {
					if (fr != null) fr.close();				 
					if (br != null) br.close();
                             
                      		} catch (Exception e) {
                             		e.printStackTrace();
                      		} // end of try

				retVal.setCorrectLines(correctLines);
				retVal.setErrorLines(errorLines);

				return retVal;

            		 } // end of finally	

    }

    private static Map<String,String> readLine2Map(String line,ImportCfgEntity _cfg,List<String> logLines){
	
        if(line==null || line.equals("")){
 		exportLogs("[error line]: line is null",logLines);               
		return null;
	}

	Map<String,String> retMap = new HashMap<String,String>();
	
	try{

	  String[] vals = line.split(_cfg.getSplitChar());
        /*
	System.out.println("keyList size="+keyList.size());
        System.out.println("vals size="+vals.length);
        for(int i=0;i<vals.length;i++){
		System.out.println(vals[i]);
        }
	for(int i=0;i<vals.length;i++){
                //System.out.println("keyList ready to get("+i+")");
                String _key = keyList.get(i).getColSqlName();
                String _val = vals[i];
		retMap.put(_key,_val);
	}*/

          for(int i=0;i<_cfg.getColCfgList().size();i++){
		
		ImportColumnCfgEntity _colcfg = _cfg.getColCfgList().get(i);
                String _key = _colcfg.getColSqlName();
                String _val = vals[_colcfg.getSplitedNo()-1]; // array is 0base

		_val = _val.trim();

                if(_colcfg.getColType().equalsIgnoreCase("varchar")){
                   _val = "'"+ _val + "'";
                }
                retMap.put(_key,_val);

 	  }

	} catch (Exception e) {
           
           //e.printStackTrace();
	   exportLogs("[error line]:"+line,logLines);	
	   retMap = null;
        }

	return retMap;

    }

    private static List<String> convertLineToInsertSqlLines(List<Map<String,String>> contentLines,
                                        ImportCfgEntity _cfg){

                List<String> retSqlList = new ArrayList<String>();

                for(Map<String,String> _lineMap:contentLines){

			StringBuilder _sb1 = new StringBuilder("insert into ");
                        _sb1.append(_cfg.getDbTableName()).append(" ( ");
                        StringBuilder _sb2 = new StringBuilder(" values ( ");
		        for(int j=0;j<_cfg.getColCfgList().size();j++){

                		ImportColumnCfgEntity _colcfg = _cfg.getColCfgList().get(j);
                		String _key = _colcfg.getColSqlName();
                		String _val = _lineMap.get(_key);
                                
				if(j != _cfg.getColCfgList().size()-1) {
                                  _sb1.append(_key).append(",");
                                  _sb2.append(_val).append(",");
				}else{
                                  _sb1.append(_key).append(" )");
                                  _sb2.append(_val).append(" )");
                                }
                           
        		}
                        retSqlList.add(_sb1.append(_sb2).toString());	

                }

                return retSqlList;

   }

}
