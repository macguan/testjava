package cn.com.dwsoft.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
 
public class ImportResultEntity{

                public String importFilePath;
                public String cfgCode;
                public String resultFilePath;
                public String logFilePath;
                public String errorLinesFilePath;
 
		public int totalTxtLineCount;
		public int correctTxtLineCount;
		public int errorTxtLineCount;
		
		public long startTimeMs;
		public long endTimeMs;		


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
	

        public int getTotalTxtLineCount() {
                return totalTxtLineCount;
        }
        public void setTotalTxtLineCount(int totalTxtLineCount ){
                this.totalTxtLineCount = totalTxtLineCount;
        }

        public int getCorrectTxtLineCount() {
                return correctTxtLineCount;
        }
        public void setCorrectTxtLineCount(int correctTxtLineCount ){
                this.correctTxtLineCount = correctTxtLineCount;
        }

        public int getErrorTxtLineCount() {
                return errorTxtLineCount;
        }
        public void setErrorTxtLineCount(int errorTxtLineCount ){
                this.errorTxtLineCount = errorTxtLineCount;
        }

        public String getImportFilePath() {
                return importFilePath;
        }
        public void setImportFilePath(String importFilePath) {
                this.importFilePath = importFilePath;
        }

        public String getCfgCode() {
                return cfgCode;
        }
        public void setCfgCode(String cfgCode) {
                this.cfgCode = cfgCode;
        }

        public String getResultFilePath() {
                return resultFilePath;
        }
        public void setResultFilePath(String resultFilePath) {
                this.resultFilePath = resultFilePath;
        }

        public String getLogFilePath() {
                return logFilePath;
        }
        public void setLogFilePath(String logFilePath) {
                this.logFilePath = logFilePath;
        }

        public String getErrorLinesFilePath() {
                return errorLinesFilePath;
        }
        public void setErrorLinesFilePath(String errorLinesFilePath) {
                this.errorLinesFilePath = errorLinesFilePath;
        }


}
     
