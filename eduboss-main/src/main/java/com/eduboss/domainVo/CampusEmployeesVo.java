package com.eduboss.domainVo;

import java.math.BigDecimal;

public class CampusEmployeesVo {
	
	/**
	 * CONSULTOR, CONSULTOR_DIRECTOR,  STUDY_MANAGER, STUDY_MANAGER_HEAD
	 * 
	 * 
	 * 
	 */
	private String campusId;
	
	private String campusName;
	
	private Integer consultorNum;
	
	private Integer studyManageNum;
	
	
	private Integer partTimeTeacherNum;
	
	private Integer fullTimeTeacherNum;
	
	private Integer otherNum;
	
	private Integer btypeTeacherNum;
	
	
	private String fullPartRat;
	
	private Integer resignNum;
	
	
	private BigDecimal resignRat;
	
	
	private BigDecimal teacherResignRat;
	
	private Integer employeesNum;
	
	private String roleCode;


	public String getCampusName() {
		return campusName;
	}


	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}


	public Integer getConsultorNum() {
		return consultorNum;
	}


	public void setConsultorNum(Integer consultorNum) {
		this.consultorNum = consultorNum;
	}


	public Integer getStudyManageNum() {
		return studyManageNum;
	}


	public void setStudyManageNum(Integer studyManageNum) {
		this.studyManageNum = studyManageNum;
	}


	public Integer getPartTimeTeacherNum() {
		return partTimeTeacherNum;
	}


	public void setPartTimeTeacherNum(Integer partTimeTeacherNum) {
		this.partTimeTeacherNum = partTimeTeacherNum;
	}


	public Integer getFullTimeTeacherNum() {
		return fullTimeTeacherNum;
	}


	public void setFullTimeTeacherNum(Integer fullTimeTeacherNum) {
		this.fullTimeTeacherNum = fullTimeTeacherNum;
	}


	public Integer getOtherNum() {
		return otherNum;
	}


	public void setOtherNum(Integer otherNum) {
		this.otherNum = otherNum;
	}


	public Integer getBtypeTeacherNum() {
		return btypeTeacherNum;
	}


	public void setBtypeTeacherNum(Integer btypeTeacherNum) {
		this.btypeTeacherNum = btypeTeacherNum;
	}


	public String getFullPartRat() {
		return fullPartRat;
	}


	public void setFullPartRat(String fullPartRat) {
		this.fullPartRat = fullPartRat;
	}


	public Integer getResignNum() {
		return resignNum;
	}


	public void setResignNum(Integer resignNum) {
		this.resignNum = resignNum;
	}


	public BigDecimal getResignRat() {
		return resignRat;
	}


	public void setResignRat(BigDecimal resignRat) {
		this.resignRat = resignRat;
	}


	public BigDecimal getTeacherResignRat() {
		return teacherResignRat;
	}


	public void setTeacherResignRat(BigDecimal teacherResignRat) {
		this.teacherResignRat = teacherResignRat;
	}


	public String getCampusId() {
		return campusId;
	}


	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}


	public Integer getEmployeesNum() {
		return employeesNum;
	}


	public void setEmployeesNum(Integer employeesNum) {
		this.employeesNum = employeesNum;
	}


	public String getRoleCode() {
		return roleCode;
	}


	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	
	
	
	

}
