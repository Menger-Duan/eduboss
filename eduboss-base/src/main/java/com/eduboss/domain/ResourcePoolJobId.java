package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.eduboss.common.ResourcePoolJobType;

public class ResourcePoolJobId implements Serializable {

	// Field
	private String organizationId;
	private String jobId;
	private ResourcePoolJobType type;
	
	// Constructors
	
	public ResourcePoolJobId() {
		super();
	}

	public ResourcePoolJobId(String organizationId, String jobId, ResourcePoolJobType type) {
		super();
		this.organizationId = organizationId;
		this.jobId = jobId;
		this.type = type;
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

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UserRoleId))
			return false;
		ResourcePoolJobId castOther = (ResourcePoolJobId) other;

		return ((this.getOrganizationId() == castOther.getOrganizationId()) || (this
				.getOrganizationId() != null && castOther.getOrganizationId() != null && this
				.getOrganizationId().equals(castOther.getOrganizationId())))
				&& ((this.getJobId() == castOther.getJobId()) || (this
						.getJobId() != null && castOther.getJobId() != null && this
						.getJobId().equals(castOther.getJobId()))
				&& ((this.getType() == castOther.getType()) || (this
						.getType() != null && castOther.getType() != null && this
							.getType().equals(castOther.getType()))));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getOrganizationId() == null ? 0 : this.getOrganizationId().hashCode());
		result = 37 * result
				+ (getJobId() == null ? 0 : this.getJobId().hashCode());
		result = 37 * result
				+ (getType() == null ? 0 : this.getType().hashCode());
		return result;
	}
	
}
