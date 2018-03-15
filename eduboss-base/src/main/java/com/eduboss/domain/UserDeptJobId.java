package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserDeptJobId implements java.io.Serializable {

	// Fields

	private String userId;
	private String deptId;
	private String jobId;

	// Constructors

	/** default constructor */
	public UserDeptJobId() {
	}

	/** minimal constructor */
	public UserDeptJobId(String userId, String deptId,String jobId) {
		this.userId = userId;
		this.deptId = deptId;
		this.jobId = jobId;
	}

	// Property accessors

	@Column(name = "USER_ID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "DEPT_ID", nullable = false, length = 32)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Column(name = "JOB_ID", nullable = false, length = 32)
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserDeptJobId))
			return false;
		UserDeptJobId castOther = (UserDeptJobId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this
				.getUserId() != null && castOther.getUserId() != null && this
				.getUserId().equals(castOther.getUserId())))
				&& ((this.getDeptId() == castOther.getDeptId()) || (this
						.getDeptId() != null && castOther.getDeptId() != null && this
						.getDeptId().equals(castOther.getDeptId())))
				&& ((this.getJobId() == castOther.getJobId()) || (this
								.getJobId() != null && castOther.getJobId() != null && this
								.getJobId().equals(castOther.getJobId())));
	}


	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getDeptId() == null ? 0 : this.getDeptId().hashCode());
		result = 37 * result
				+ (getJobId() == null ? 0 : this.getJobId().hashCode());
		return result;
	}

}