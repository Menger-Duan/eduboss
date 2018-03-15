package com.eduboss.domainVo;

import com.eduboss.domain.PromiseClassSubject;

import java.math.BigDecimal;


public class PromiseClassSubjectVo implements java.io.Serializable,Comparable<Object>{
	private int id ;
	private MiniClassVo miniClass;
	private String subjectId;
	private String subjectName;
	private BigDecimal courseHours;
	private String createTime;
	private String modifyTime;
	private String createUserId;
	private String modifyUserId;
	private String quarterId;
	private String quarterName;
	private PromiseStudentVo promiseStudent;
	private String oldMiniClassId;
	private int sort;


	private BigDecimal consumeCourseHours;

	public BigDecimal getConsumeCourseHours() {
		return consumeCourseHours;
	}

	public void setConsumeCourseHours(BigDecimal consumeCourseHours) {
		this.consumeCourseHours = consumeCourseHours;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MiniClassVo getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClassVo miniClass) {
		this.miniClass = miniClass;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public BigDecimal getCourseHours() {
		return courseHours;
	}

	public void setCourseHours(BigDecimal courseHours) {
		this.courseHours = courseHours;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getOldMiniClassId() {
		return oldMiniClassId;
	}

	public void setOldMiniClassId(String oldMiniClassId) {
		this.oldMiniClassId = oldMiniClassId;
	}

	public PromiseStudentVo getPromiseStudent() {
		return promiseStudent;
	}

	public void setPromiseStudent(PromiseStudentVo promiseStudent) {
		this.promiseStudent = promiseStudent;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof PromiseClassSubjectVo) {
			PromiseClassSubjectVo vo = (PromiseClassSubjectVo) o;
			int sort2=sort-vo.getSort();
			if(sort2!=0){
				return sort2;
			}
			String com=subjectId+quarterId;
			return com.compareTo(vo.getSubjectId()+vo.getSubjectId());
		}
		throw new ClassCastException("Cannot compare Pair with "
				+ o.getClass().getName());
	}


}
