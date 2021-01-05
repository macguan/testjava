package cn.com.dwsoft.entity;


public class ImportColumnCfgEntity{
	
	private String colSqlName;

	private String colChineseName;

	private String colType = "varchar";

	private int splitedNo = 1;

	public ImportColumnCfgEntity(String colSqlName, String colChineseName, String colType,int splitedNo) {
		super();
		this.colSqlName = colSqlName;
		this.colChineseName = colChineseName;
		this.colType = colType;
		this.splitedNo = splitedNo;
	}

	public String getColSqlName() {
		return colSqlName;
	}
	public void setColSqlName(String colSqlName) {
		this.colSqlName = colSqlName;
	}

        public String getColChineseName() {
                return colChineseName;
        }
        public void setColChineseName(String colChineseName) {
                this.colChineseName = colChineseName;
        }

        public String getColType() {
                return colType;
        }
        public void setColType(String colType) {
                this.colType = colType;
        }

        public int getSplitedNo() {
                return splitedNo;
        }
        public void setSplitedNo(int splitedNo) {
                this.splitedNo = splitedNo;
        }

}
