package com.eduboss.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.eduboss.common.EventType;

/**
 * 
 * @author xiaojinwang
 * @date 2017-02-18
 */
@Entity
@Table(name = "user_event_record")
public class UserEventRecord implements Serializable{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	//客户id
	private String customerId;
	//事件类型
	private EventType eventType;
	//操作人id
	private String userId;	
	//操作人姓名
	private String userName;
	//操作时间
	private String createTime;
	//用于统计
	private Integer statusNum;
	
	public UserEventRecord(){}

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "customer_id", length = 32)
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type")
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	@Column(name = "user_id", length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", length = 32)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "create_time", length = 32)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "status_num")
	public Integer getStatusNum() {
		return statusNum;
	}

	public void setStatusNum(Integer statusNum) {
		this.statusNum = statusNum;
	}
	
	
	
}
