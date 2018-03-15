package com.eduboss.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.eduboss.common.ResourcePoolJobType;

/**
 * 
 * @author lixuejun
 *
 */
@Entity
@Table(name="resource_pool_job")
public class ResourcePoolJob implements Serializable {

	private int  id;
	private UserJob userJob;
//	private ResourcePoolJobType type;
	private BigDecimal oneTimeResource;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String organizationId;
	private String jobId;
	private ResourcePoolJobType type;
	
	//Contructors
	public ResourcePoolJob() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_ID", nullable = false, insertable = false, updatable = false)
	public UserJob getUserJob() {
		return userJob;
	}

	public void setUserJob(UserJob userJob) {
		this.userJob = userJob;
	}

//	@Enumerated(EnumType.STRING)
//	@Column(name="TYPE")
//	public ResourcePoolJobType getType() {
//		return type;
//	}
//	public void setType(ResourcePoolJobType type) {
//		this.type = type;
//	}
	
	@Column(name = "ONE_TIME_RESOURCE", precision = 4)
	public BigDecimal getOneTimeResource() {
		return oneTimeResource;
	}
	public void setOneTimeResource(BigDecimal oneTimeResource) {
		this.oneTimeResource = oneTimeResource;
	}
	
	@Column(name="CREATE_USER_ID", length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name="CREATE_TIME", length=20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name="MODIFY_USER_ID", length=32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name="MODIFY_TIME", length=20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "ORGANIZATION_ID", nullable = false, length = 32)
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "JOB_ID", nullable = false, length = 32)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="TYPE")
	public ResourcePoolJobType getType() {
		return type;
	}

	public void setType(ResourcePoolJobType type) {
		this.type = type;
	}
}
