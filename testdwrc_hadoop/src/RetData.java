package cn.com.dwsoft.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RetData{

        public List<Map<String,String>> correctLines = null;

	public List<String> errorLines = null;

	public Boolean endFlag = false;

        public List<Map<String,String>> getCorrectLines() {
                return correctLines;
        }
        public void setCorrectLines(List<Map<String,String>> correctLines) {
                this.correctLines = correctLines;
        }

        public List<String> getErrorLines() {
                return errorLines;
        }
        public void setErrorLines(List<String> errorLines) {
                this.errorLines = errorLines;
        }

        public Boolean isEnd() {
                return endFlag;
        }
        public void setEndFlag(Boolean endFlag) {
                this.endFlag = endFlag;
        }




}	
