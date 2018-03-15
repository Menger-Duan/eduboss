package com.eduboss.domainVo;

import com.eduboss.common.SchedulerExecuteStatus;

public class SchedulerExecuteLogVo {
	private String id;
	private String schedulerName;
	private String startTime;
	private String endTime;
	private SchedulerExecuteStatus status;
	private String chineseSchedulerName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public SchedulerExecuteStatus getStatus() {
		return status;
	}
	public void setStatus(SchedulerExecuteStatus status) {
		this.status = status;
	}
	public String getChineseSchedulerName() {
		return chineseSchedulerName;
	}
	public void setChineseSchedulerName(String chineseSchedulerName) {
		this.chineseSchedulerName = chineseSchedulerName;
	}
	
	
	
}
