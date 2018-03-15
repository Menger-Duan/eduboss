package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.SchedulerExecuteStatus;

/**
 * 
 * @author lixuejun
 * @version v1.0
 * 2015-09-17
 */
@Entity
@Table(name = "scheduler_execute_log")
public class SchedulerExecuteLog {

	private String id;
	private String schedulerName;
	private String startTime;
	private String endTime;
	private SchedulerExecuteStatus status;
	
	public SchedulerExecuteLog() {
		super();
	}
	
	
	public SchedulerExecuteLog(String id, String schedulerName,
			String startTime, String endTime, SchedulerExecuteStatus status) {
		super();
		this.id = id;
		this.schedulerName = schedulerName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
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
	
	@Column(name = "SCHEDULER_NAME", length = 50)
	public String getSchedulerName() {
		return schedulerName;
	}
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	
	@Column(name = "START_TIME", length = 20)
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "END_TIME", length = 20)
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 32)
	public SchedulerExecuteStatus getStatus() {
		return status;
	}
	public void setStatus(SchedulerExecuteStatus status) {
		this.status = status;
	}
	
}
