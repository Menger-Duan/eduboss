package com.eduboss.domainVo;

public class DistributableUserJobVo {

	private String relateJobId;
	private String relateJobName;
		
	/**
	 * 
	 */
	public DistributableUserJobVo() {
		super();
	}

	/**
	 * @param relateJobId
	 * @param relateJobName
	 */
	public DistributableUserJobVo(String relateJobId, String relateJobName) {
		super();
		this.relateJobId = relateJobId;
		this.relateJobName = relateJobName;
	}

	public String getRelateJobId() {
		return relateJobId;
	}

	public void setRelateJobId(String relateJobId) {
		this.relateJobId = relateJobId;
	}

	public String getRelateJobName() {
		return relateJobName;
	}

	public void setRelateJobName(String relateJobName) {
		this.relateJobName = relateJobName;
	}
	

	
	
}
