package com.eduboss.report;


/**
 * excel单元格合并类
 * @author qinjingkai
 *
 */
public class ReportMergedBean {
	
	public  static final  String MERGEDTYPE_HORIZONTAL="horizontal";
	public  static final  String MERGEDTYPE_VERTICAL="Vertical";
	
	private String text;
	private int mergedCellNum;
	private String type;
	
	
	public ReportMergedBean(String text,int mergedCellNum,String type){
		this.text=text;
		this.mergedCellNum=mergedCellNum;
		this.type=type;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getMergedCellNum() {
		return mergedCellNum;
	}
	public void setMergedCellCount(int mergedCellNum) {
		this.mergedCellNum = mergedCellNum;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
