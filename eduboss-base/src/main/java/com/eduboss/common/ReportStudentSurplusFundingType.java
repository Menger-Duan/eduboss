package com.eduboss.common;

/**
 * Created by yanliang on 8/7/15.
 */
public enum ReportStudentSurplusFundingType {

	STUDENT("STUDENT", "学生"),//学生
	STUDY_MANAGER("STUDY_MANAGER", "班主任"),//班主任（学管）
	CAMPUS("CAMPUS", "校区"),//校区
	BRANCH("BRANCH", "分公司"),//分公司
	GROUP("GROUP", "集团");//集团

	private String value;
	private String name;

	private ReportStudentSurplusFundingType(String value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public String toString() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
