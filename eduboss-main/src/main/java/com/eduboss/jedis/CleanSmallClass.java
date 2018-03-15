package com.eduboss.jedis;

import java.io.Serializable;

public class CleanSmallClass implements Serializable {
	private static final long serialVersionUID = -4053509917807172168L;
	private String productVersion;
	private String productQuarter;
	
	public CleanSmallClass(){}
	
	public CleanSmallClass(String version, String quarter){
		this.productVersion = version;
		this.productQuarter = quarter;
		
	}
	
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
	public String getProductQuarter() {
		return productQuarter;
	}
	public void setProductQuarter(String productQuarter) {
		this.productQuarter = productQuarter;
	}

	
	

}

