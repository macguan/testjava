package cn.com.dwsoft.entity;

import cn.com.dwsoft.entity.ImportCfgEntity;

import java.util.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class ImportCfgEntity{
	
	public String cfgCode;

	public String cfgName;
	
	public String splitChar;

	public String dbTableName;

	public Boolean disp_modeFlag = false;

        public Boolean page_import_modeFlag = false;

	public int page_import_size = 5000;

        public List<ImportColumnCfgEntity> colCfgList = new ArrayList<ImportColumnCfgEntity>();

        public String getCfgCode() {
                return cfgCode;
        }
        public void setCfgCode(String cfgCode) {
                this.cfgCode = cfgCode;
        }

        public String getCfgName() {
                return cfgName;
        }
        public void setCfgName(String cfgName) {
                this.cfgName = cfgName;
        }

        public String getDbTableName() {
                return dbTableName;
        }       
        public void setDbTableName(String dbTableName) {
                this.dbTableName = dbTableName;
        }

        public String getSplitChar() {
                return splitChar;
        }
        public void setSplitChar(String splitChar) {
                this.splitChar = splitChar;
        }

        public List<ImportColumnCfgEntity> getColCfgList() {
                return colCfgList;
        }
        public void setColCfgList(List<ImportColumnCfgEntity> colCfgList) {
                this.colCfgList = colCfgList;
        }


        public Boolean getDisp_modeFlag() {
                return disp_modeFlag;
        }
        public void setDisp_modeFlag(Boolean disp_modeFlag) {
                this.disp_modeFlag = disp_modeFlag;
        }

        public Boolean getPage_import_modeFlag() {
                return page_import_modeFlag;
        }
        public void setPage_import_modeFlag(Boolean page_import_modeFlag) {
                this.page_import_modeFlag = page_import_modeFlag;
        }

        public int getPage_import_size() {
                return page_import_size;
        }
        public void setPage_import_size(int page_import_size) {
                this.page_import_size = page_import_size;
        }


}
