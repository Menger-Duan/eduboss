package com.eduboss.domainVo;

/**
 * 年纪占有率统计对象 报表使用
 * @author qinjingkai
 *
 */
public class GradeProportiStatObj {
	
	public String type;
	
	public String gradeId;
	
	public String gradeName;
	
	public Integer statiNum;

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public Integer getStatiNum() {
		return statiNum;
	}

	public void setStatiNum(Integer statiNum) {
		this.statiNum = statiNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
