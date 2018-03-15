package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 
 * @author xiaojinwang 2017-03-02
 * 实体类  用户职位 可以分配的职位关联表
 */

@Entity
@Table(name="distributable_user_job")
public class DistributableUserJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DistributableUserJobPk id;
	private String jobName;
	private String relateJobName;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	/**
	 * 
	 */
	public DistributableUserJob() {
		super();
	}
	
	
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "jobId", column = @Column(name = "job_id", nullable = false, length = 32)),
			@AttributeOverride(name = "relateJobId", column = @Column(name = "relate_job_id", nullable = false, length = 32)) })
	public DistributableUserJobPk getId() {
		return id;
	}



	public void setId(DistributableUserJobPk id) {
		this.id = id;
	}

	@Column(name="job_name", length=32)
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name="relate_job_name", length=32)
	public String getRelateJobName() {
		return relateJobName;
	}
	public void setRelateJobName(String relateJobName) {
		this.relateJobName = relateJobName;
	}
	@Column(name="create_user_id", length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name="create_time", length=20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name="modify_user_id", length=32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name="modify_time", length=20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}


	
	
	
	
}
