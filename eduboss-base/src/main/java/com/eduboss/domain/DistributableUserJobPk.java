package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class DistributableUserJobPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jobId;
	private String relateJobId;
	/**
	 * 
	 */
	public DistributableUserJobPk() {
		super();
	}
	@Column(name = "job_id", nullable = false, length = 32)
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	@Column(name = "relate_job_id", nullable = false, length = 32)
	public String getRelateJobId() {
		return relateJobId;
	}
	public void setRelateJobId(String relateJobId) {
		this.relateJobId = relateJobId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		result = prime * result + ((relateJobId == null) ? 0 : relateJobId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistributableUserJobPk other = (DistributableUserJobPk) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		if (relateJobId == null) {
			if (other.relateJobId != null)
				return false;
		} else if (!relateJobId.equals(other.relateJobId))
			return false;
		return true;
	}
	
	
	
	
}
