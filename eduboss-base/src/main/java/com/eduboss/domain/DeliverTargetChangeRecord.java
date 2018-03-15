package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author xiaojinwang
 * @description 分配目标变动记录表
 * @date 2016-09-02
 *
 */
@Entity
@Table(name = "delivertarget_change_record")
public class DeliverTargetChangeRecord implements java.io.Serializable{
	
	private static final long serialVersionUID = 7328992132022987445L;
	
	private String id;
	private String customerId;
	private String previousTarget;
	private String currentTarget;
	private String remark;
	private String createUserId;
	private String createTime;
	
	public DeliverTargetChangeRecord(){
	}
	
	////	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "customer_id", length = 32)
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	@Column(name = "previous_target", length = 32,unique = true)
	public String getPreviousTarget() {
		return previousTarget;
	}
	public void setPreviousTarget(String previousTarget) {
		this.previousTarget = previousTarget;
	}
	@Column(name = "current_target", length = 32,unique = true)
	public String getCurrentTarget() {
		return currentTarget;
	}
	public void setCurrentTarget(String currentTarget) {
		this.currentTarget = currentTarget;
	}
	@Column(name = "remark", length = 32,unique = true)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "create_user_id", length = 32,unique = true)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "create_time", length = 32,unique = true)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
