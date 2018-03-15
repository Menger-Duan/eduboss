package com.eduboss.dto;

import java.util.Date;



/**
 * 专门用于接收开始日期和结束日期参数的 VO
 * @author robinzhang
 *
 */
public class TimeVo {
	private String startDate;
	private String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
