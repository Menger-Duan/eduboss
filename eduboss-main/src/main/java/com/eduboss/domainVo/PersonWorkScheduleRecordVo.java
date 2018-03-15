package com.eduboss.domainVo;

import java.util.HashSet;
import java.util.Set;

import com.eduboss.common.WorkRemindTime;

public class PersonWorkScheduleRecordVo {
	private String id;
	private String title;
	private String content;
	private String startDate;
	private String endDate;
	private String scheduleStartTime;
	private String scheduleEndTime;
	private String iconStr;
	private String colorStr;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	
	private String workPriority;  //优先级
	private String workEventType; //工作类型
	private WorkRemindTime workRemindTime; //提醒时间
	private String isDay;//全天事件
	
	private Set<String> workDates=new HashSet<String>(); //手机端做日期标记

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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

	public String getScheduleStartTime() {
		return scheduleStartTime;
	}

	public void setScheduleStartTime(String scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}

	public String getScheduleEndTime() {
		return scheduleEndTime;
	}

	public void setScheduleEndTime(String scheduleEndTime) {
		this.scheduleEndTime = scheduleEndTime;
	}

	public String getIconStr() {
		return iconStr;
	}

	public void setIconStr(String iconStr) {
		this.iconStr = iconStr;
	}

	public String getColorStr() {
		return colorStr;
	}

	public void setColorStr(String colorStr) {
		this.colorStr = colorStr;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getWorkPriority() {
		return workPriority;
	}

	public void setWorkPriority(String workPriority) {
		this.workPriority = workPriority;
	}

	public String getWorkEventType() {
		return workEventType;
	}

	public void setWorkEventType(String workEventType) {
		this.workEventType = workEventType;
	}

	public WorkRemindTime getWorkRemindTime() {
		return workRemindTime;
	}

	public void setWorkRemindTime(WorkRemindTime workRemindTime) {
		this.workRemindTime = workRemindTime;
	}

	public String getIsDay() {
		return isDay;
	}

	public void setIsDay(String isDay) {
		this.isDay = isDay;
	}

	public Set<String> getWorkDates() {
		return workDates;
	}

	public void setWorkDates(Set<String> workDates) {
		this.workDates = workDates;
	}
	
	
}
