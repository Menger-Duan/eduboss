package com.eduboss.domain;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.WorkRemindTime;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "person_work_schedule_record")
public class PersonWorkScheduleRecord implements java.io.Serializable {

	// Fields

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
	
	
	
	public PersonWorkScheduleRecord() {
		super();
	}

	public PersonWorkScheduleRecord(String id, String title, String content,
			String startDate, String endDate, String scheduleStartTime, String scheduleEndTime,
			String iconStr, String colorStr, String createUserId,
			String createTime, String modifyTime,String workPriority,String workEventType,WorkRemindTime workRemindTime,
			String isDay) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.startDate = startDate;
		this.endDate = endDate;
		this.scheduleStartTime = scheduleStartTime;
		this.scheduleEndTime = scheduleEndTime;
		this.iconStr = iconStr;
		this.colorStr = colorStr;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.workEventType=workEventType;
		this.workPriority=workPriority;
		this.workRemindTime=workRemindTime;
		this.isDay=isDay;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "TITLE", length = 20)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT", length = 20)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="START_DATE", precision = 10)
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name="END_DATE", precision = 10)
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Column(name="SCHEDULE_START_TIME", precision = 20)
	public String getScheduleStartTime() {
		return scheduleStartTime;
	}
	
	public void setScheduleStartTime(String scheduleStartTime) {
		this.scheduleStartTime = scheduleStartTime;
	}
	
	@Column(name="SCHEDULE_END_TIME", precision = 20)
	public String getScheduleEndTime() {
		return scheduleEndTime;
	}
	
	public void setScheduleEndTime(String scheduleEndTime) {
		this.scheduleEndTime = scheduleEndTime;
	}
	
	@Column(name="ICON_STR", precision = 50)
	public String getIconStr() {
		return iconStr;
	}


	public void setIconStr(String iconStr) {
		this.iconStr = iconStr;
	}

	@Column(name="COLOR_STR", precision = 50)
	public String getColorStr() {
		return colorStr;
	}

	public void setColorStr(String colorStr) {
		this.colorStr = colorStr;
	}

	@Column(name="CREATE_USER_ID", precision = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "WORK_PRIORITY", length = 32)
	public String getWorkPriority() {
		return workPriority;
	}

	public void setWorkPriority(String workPriority) {
		this.workPriority = workPriority;
	}

	@Column(name = "WORK_TYPE", length = 32)
	public String getWorkEventType() {
		return workEventType;
	}

	public void setWorkEventType(String workEventType) {
		this.workEventType = workEventType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "WORK_REMIND_TIME", length = 32)
	public WorkRemindTime getWorkRemindTime() {
		return workRemindTime;
	}

	public void setWorkRemindTime(WorkRemindTime workRemindTime) {
		this.workRemindTime = workRemindTime;
	}

	@Column(name = "IS_DAY", length = 10)
	public String getIsDay() {
		return isDay;
	}

	public void setIsDay(String isDay) {
		this.isDay = isDay;
	}
	
	
	
	
	
	

}