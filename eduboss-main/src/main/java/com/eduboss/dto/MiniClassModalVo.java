package com.eduboss.dto;

import com.eduboss.common.WeekDay;

import java.util.TreeSet;

public class MiniClassModalVo{
	private String miniClassId;
	private String productId;
	private String productName;
	private String name;
	private String blCampusId;
	private String blCampusName;
	private String teacherId;
	private String teacherName;
	private String startDate;
	private String endDate;
	private String classTime;
	private Double totalClassHours;
	private String studyManegerId;
	private String studyManegerName;
	private int miniClassPeopleNum; //已报读人数
	private Integer peopleQuantity;//招生人数
	private Double everyCourseClassNum;
	private String statusName;
	private String classroomId;
	private String classroomName;
	private Integer classTimeLength;
	private String productVersionId;
	private String productQuarterId;
	private String phaseId;
	private String phaseName;

	private TreeSet<WeekDay> courseWeek;
	private String coursePhase;
	private String courseWeekId;
	private String coursePhaseId;
	private String classRoomMember;

	private String brenchId;
	private String type;
	private int modalId;
    private String modalName;

	private String classTypeId;
	private String classTypeName;
	private String isModal;

	private String blBrenchId;
	private String blBrenchName;

	private String gradeId;
	private String subjectId;

	private String [] blcampusIds;

	private String createTime;
	private String modifyTime;

	private String [] teacherIds;


	public String[] getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String[] teacherIds) {
		this.teacherIds = teacherIds;
	}

	public TreeSet<WeekDay> getCourseWeek() {
		return courseWeek;
	}

	public void setCourseWeek(TreeSet<WeekDay> courseWeek) {
		this.courseWeek = courseWeek;
	}

	public String[] getBlcampusIds() {
		return blcampusIds;
	}

	public void setBlcampusIds(String[] blcampusIds) {
		this.blcampusIds = blcampusIds;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getBlBrenchId() {
		return blBrenchId;
	}

	public void setBlBrenchId(String blBrenchId) {
		this.blBrenchId = blBrenchId;
	}

	public String getBlBrenchName() {
		return blBrenchName;
	}

	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getIsModal() {
		return isModal;
	}

	public void setIsModal(String isModal) {
		this.isModal = isModal;
	}

	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	public String getMiniClassId() {
		return miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public Double getTotalClassHours() {
		return totalClassHours;
	}

	public void setTotalClassHours(Double totalClassHours) {
		this.totalClassHours = totalClassHours;
	}

	public String getStudyManegerId() {
		return studyManegerId;
	}

	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}

	public String getStudyManegerName() {
		return studyManegerName;
	}

	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
	}

	public int getMiniClassPeopleNum() {
		return miniClassPeopleNum;
	}

	public void setMiniClassPeopleNum(int miniClassPeopleNum) {
		this.miniClassPeopleNum = miniClassPeopleNum;
	}

	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}

	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	public String getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(String productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductQuarterId() {
		return productQuarterId;
	}

	public void setProductQuarterId(String productQuarterId) {
		this.productQuarterId = productQuarterId;
	}

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getClassRoomMember() {
		return classRoomMember;
	}

	public void setClassRoomMember(String classRoomMember) {
		this.classRoomMember = classRoomMember;
	}

	public String getCoursePhase() {
		return coursePhase;
	}

	public void setCoursePhase(String coursePhase) {
		this.coursePhase = coursePhase;
	}


	public String getCourseWeekId() {
		return courseWeekId;
	}

	public void setCourseWeekId(String courseWeekId) {
		this.courseWeekId = courseWeekId;
	}

	public String getCoursePhaseId() {
		return coursePhaseId;
	}

	public void setCoursePhaseId(String coursePhaseId) {
		this.coursePhaseId = coursePhaseId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getModalId() {
		return modalId;
	}

	public void setModalId(int modalId) {
		this.modalId = modalId;
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
}
