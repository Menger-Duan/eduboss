package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.MiniClassStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.common.StudentStatus;
import com.eduboss.domain.Student;

public class MiniClassStudentVo {

	private String miniClassId;
	private String id;
//	private MiniClassStudentId id;
//	private MiniClassModalVo miniClass;
	private String miniClassCourseId;
	private Student student;
	private String studentName;
	private String studentId;
	private String signUpDate;
	private MiniClassStatus miniClassStudyStatus;
	private String  miniClassStudentChargeStatus;
	private MiniClassStudentChargeStatus miniClassStudentChargeStatusEnum;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String miniClassAttendanceStatus; 
	private String endDate;
	private String courseDate;
	private BaseStatus hasTeacherAttendance;
	ContractProductStatus contractProductStatus;
	private BigDecimal contractProductRemainingAmount=BigDecimal.ZERO;
	private String firstSchoolTime;//第一次上课时间
	private String contractProductId;
	private BigDecimal miniClassSurplusFinance;// 小班剩余资金
	private BigDecimal miniClassSurplusCourseHour;// 小班剩余课时
	private BigDecimal miniClassTotalCharged;// 累计扣费
	private String continueMiniClassId;
    private String extendMiniClassId;
    private String absentRemark; // 缺勤备注
	private String supplementDate; // 补课日期
	private String productName;
	private String studentContact;//学生电话
	
	private String inClassStatus;//学生在班状态
	
	private StudentStatus stuStatus; //学生状态

	private String productType;

	private Integer isFrozen; // 0：冻结，1：不冻结

	private String xuBaoQingKuang; //小班学生续报情况;

	public Integer getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//	public MiniClassStudentId getId() {
//		return id;
//	}
//	public void setId(MiniClassStudentId id) {
//		this.id = id;
//	}
//	public MiniClassModalVo getMiniClass() {
//		return miniClass;
//	}
//	public void setMiniClass(MiniClassModalVo miniClass) {
//		this.miniClass = miniClass;
//	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public String getSignUpDate() {
		return signUpDate;
	}
	public void setSignUpDate(String signUpDate) {
		this.signUpDate = signUpDate;
	}
	public MiniClassStatus getMiniClassStudyStatus() {
		return miniClassStudyStatus;
	}
	public void setMiniClassStudyStatus(MiniClassStatus miniClassStudyStatus) {
		this.miniClassStudyStatus = miniClassStudyStatus;
	}
	public String getMiniClassStudentChargeStatus() {
		return miniClassStudentChargeStatus;
	}
	public void setMiniClassStudentChargeStatus(String miniClassStudentChargeStatus) {
		this.miniClassStudentChargeStatus = miniClassStudentChargeStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}
	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}
	public String getMiniClassAttendanceStatus() {
		return miniClassAttendanceStatus;
	}
	public void setMiniClassAttendanceStatus(String miniClassAttendanceStatus) {
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public MiniClassStudentChargeStatus getMiniClassStudentChargeStatusEnum() {
		return miniClassStudentChargeStatusEnum;
	}
	public void setMiniClassStudentChargeStatusEnum(
			MiniClassStudentChargeStatus miniClassStudentChargeStatusEnum) {
		this.miniClassStudentChargeStatusEnum = miniClassStudentChargeStatusEnum;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}
	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}
	public ContractProductStatus getContractProductStatus() {
		return contractProductStatus;
	}
	public void setContractProductStatus(ContractProductStatus contractProductStatus) {
		this.contractProductStatus = contractProductStatus;
	}
	
	public BigDecimal getContractProductRemainingAmount() {
		return contractProductRemainingAmount;
	}
	public void setContractProductRemainingAmount(
			BigDecimal contractProductRemainingAmount) {
		this.contractProductRemainingAmount = contractProductRemainingAmount;
	}
	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}
	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public BigDecimal getMiniClassSurplusFinance() {
		return miniClassSurplusFinance;
	}
	public void setMiniClassSurplusFinance(BigDecimal miniClassSurplusFinance) {
		this.miniClassSurplusFinance = miniClassSurplusFinance;
	}
	public BigDecimal getMiniClassSurplusCourseHour() {
		return miniClassSurplusCourseHour;
	}
	public void setMiniClassSurplusCourseHour(BigDecimal miniClassSurplusCourseHour) {
		this.miniClassSurplusCourseHour = miniClassSurplusCourseHour;
	}
	public BigDecimal getMiniClassTotalCharged() {
		return miniClassTotalCharged;
	}
	public void setMiniClassTotalCharged(BigDecimal miniClassTotalCharged) {
		this.miniClassTotalCharged = miniClassTotalCharged;
	}
	public String getContinueMiniClassId() {
		return continueMiniClassId;
	}
	public void setContinueMiniClassId(String continueMiniClassId) {
		this.continueMiniClassId = continueMiniClassId;
	}
	public String getExtendMiniClassId() {
		return extendMiniClassId;
	}
	public void setExtendMiniClassId(String extendMiniClassId) {
		this.extendMiniClassId = extendMiniClassId;
	}
	public String getAbsentRemark() {
		return absentRemark;
	}
	public void setAbsentRemark(String absentRemark) {
		this.absentRemark = absentRemark;
	}
	public String getSupplementDate() {
		return supplementDate;
	}
	public void setSupplementDate(String supplementDate) {
		this.supplementDate = supplementDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getStudentContact() {
		return studentContact;
	}
	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
	
	public String getInClassStatus() {
		return inClassStatus;
	}
	public void setInClassStatus(String inClassStatus) {
		this.inClassStatus = inClassStatus;
	}
	public StudentStatus getStuStatus() {
		return stuStatus;
	}
	public void setStuStatus(StudentStatus stuStatus) {
		this.stuStatus = stuStatus;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getXuBaoQingKuang() {
		return xuBaoQingKuang;
	}

	public void setXuBaoQingKuang(String xuBaoQingKuang) {
		this.xuBaoQingKuang = xuBaoQingKuang;
	}
}
