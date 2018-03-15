package com.eduboss.domainVo;

import java.util.List;

public class SubjectGroupSummaryVo {

	private int groupSum;
	private int subjectSum;
	private int teahcerSum;
	private List<SubjectGroupVo> subjectGroups;
	public int getGroupSum() {
		return groupSum;
	}
	public void setGroupSum(int groupSum) {
		this.groupSum = groupSum;
	}
	public int getSubjectSum() {
		return subjectSum;
	}
	public void setSubjectSum(int subjectSum) {
		this.subjectSum = subjectSum;
	}
	public int getTeahcerSum() {
		return teahcerSum;
	}
	public void setTeahcerSum(int teahcerSum) {
		this.teahcerSum = teahcerSum;
	}
	public List<SubjectGroupVo> getSubjectGroups() {
		return subjectGroups;
	}
	public void setSubjectGroups(List<SubjectGroupVo> subjectGroups) {
		this.subjectGroups = subjectGroups;
	}
	
}
