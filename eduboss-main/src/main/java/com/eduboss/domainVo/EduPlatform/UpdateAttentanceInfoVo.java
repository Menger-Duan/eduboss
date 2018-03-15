package com.eduboss.domainVo.EduPlatform;

import java.util.List;

import com.eduboss.domainVo.AttentanceInfoVo;

public class UpdateAttentanceInfoVo {

	private List<AttentanceInfoVo> infos;
	
	private String type;
	
	private String chargeUserId;
	
	public static class Type{
		public static String UPDATE ="UPDATE";
		public static String CHARGE ="CHARGE";
	}

	public List<AttentanceInfoVo> getInfos() {
		return infos;
	}

	public void setInfos(List<AttentanceInfoVo> infos) {
		this.infos = infos;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChargeUserId() {
		return chargeUserId;
	}

	public void setChargeUserId(String chargeUserId) {
		this.chargeUserId = chargeUserId;
	}

    
	
}
