package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.StudentStatus;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.utils.DateTools;

public class StudentVo implements NameValue{

    private String id;
    private String name;
    private String sex;
    private String bothday;
    private String contact;
    private String provice;
    private String city;
    private String erea;
    private String schoolId;
    private String schoolName;

    private String schoolTempId;
    private String schoolTempName;

    private String schoolOrTemp;//显示school or schoolTemp   ： school ： 显示 合同的school字段     schoolTemp ： 显示合同 的schoolTemp 字段

    // private String grade;
    private String classNo;
    private String blCampusId;
    private String blCampusName;
    private String habit;
    private String gradeLevel;
    private String fatherName;
    private String fatherPhone;
    private String motherName;
    private String notherPhone;
    private String status;
    private String statusName;
    private String studyManegerId;
    private String studyManegerName;
    private String attanceNo;
    private String icCardNo;
    private String fingerInfo;
    private String createUserId;
    private String createUserName;
    private String createTime;
    private String modifyTime;
    private String modifyUserId;
    private String modifyUserName;
    private String remark;
    private String gradeName;
    private String gradeId;
    private String miniClassStudentChargeStatus;

    private int daysFromSignUp; //距离第一次签约多少天

    private int daysFromLastSignUp; //距离现在最近一次合同时间
    
    private String enrolDate;//入学日期
    private int courseCount;
    private String studentStatus;
    private String latestCustomerId;
    private String latestCustomerName;
    private String latestCustomerContact;
    
    private StudnetAccMvVo studentAccount;
    
    private String mobileUserId;

    private String oneOnOneStatus;
    private String oneOnOneStatusName;
    private String smallClassStatus;
    private String smallClassStatusName;
    private String oneOnManyStatus;
    private String oneOnManyStatusName;
    
    private BigDecimal remainingAmount; // 剩余金额
    private BigDecimal oneOnOneRemainingHour;// 剩余课时
    private BigDecimal miniRemainingHour;//小班剩余课时

    private BigDecimal oneOnOneAlreadyArrangeHour; //一对一已排的课时 ：未结算课时（未上课，老师已考勤，学管已确认）
    
    private String address;
    private String log;
    private String lat;
    
    private String studyManegerContact;
    private String blCampusContact;
    private String blCampusAddress;
    
    
    private String contractId;
    private String contractProductId;
    private String firstSchoolTime;//首次上课时间
    private BigDecimal quantity; //课时
    private BigDecimal paidAmount;//已分配资金
    
    // 一对多
    private String otmClassStudentChargeStatus; // 一对多学生扣费状态
    private BigDecimal otmRemainingHour; // 一对多剩余课时
    private String otmClassStatus; // 一对多班级状态
    private String otmClassStatusName; // 一对多班级名称
    
    private String brenchName;// 公司名称
//    private String brenchId;

    private String productId;
    private String productName;
    
    // 一对二，一对三，一对四 ，一对五 剩余课时
    private BigDecimal otTwoRemainingHour;
    private BigDecimal otThreeRemainingHour;
    private BigDecimal otFourRemainingHour;
    private BigDecimal otFiveRemainingHour;

  //对应的客户
    private String cusName;
    private String cusId;
    private String cusContact;
    private int con; //标记同名学生
	private int oth; //所有学生中是否有同学
    
    private String studentType;
    
    private String classes;
    
    private String curCampusStudyManagerId;

    private StudentStatus stuStatus;

    private String contractType;
    
    //判断学生能否被签单 跟转介绍有关 20170814 
    private Boolean transferCanSignUp;

    private String dayNumbers;
    
    private Boolean overtime;

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public StudentStatus getStuStatus() {
        return stuStatus;
    }

    public void setStuStatus(StudentStatus stuStatus) {
        this.stuStatus = stuStatus;
    }

    public String getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}

	public String getEnrolDate() {
        return enrolDate;
    }

    public void setEnrolDate(String enrolDate) {
        this.enrolDate = enrolDate;
    }


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLat() {
        return lat;
    }


    public String getContractProductId() {
        return contractProductId;
    }

    public void setContractProductId(String contractProductId) {
        this.contractProductId = contractProductId;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMiniClassStudentChargeStatus() {
        return miniClassStudentChargeStatus;
    }

    public void setMiniClassStudentChargeStatus(
            String miniClassStudentChargeStatus) {
        this.miniClassStudentChargeStatus = miniClassStudentChargeStatus;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the bothday
     */
    public String getBothday() {
        return bothday;
    }

    /**
     * @param bothday the bothday to set
     */
    public void setBothday(String bothday) {
        this.bothday = bothday;
    }

    /**
     * @return the provice
     */
    public String getProvice() {
        return provice;
    }

    /**
     * @param provice the provice to set
     */
    public void setProvice(String provice) {
        this.provice = provice;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the erea
     */
    public String getErea() {
        return erea;
    }

    /**
     * @param erea the erea to set
     */
    public void setErea(String erea) {
        this.erea = erea;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * @return the grade
     */
    // public String getGrade() {
    // return grade;
    // }
    /**
     * @param grade
     *            the grade to set
     */
    // public void setGrade(String grade) {
    // this.grade = grade;
    // }

    /**
     * @return the classNo
     */
    public String getClassNo() {
        return classNo;
    }

    /**
     * @param classNo the classNo to set
     */
    public void setClassNo(String classNo) {
        this.classNo = classNo;
    }

    /**
     * @return the blCampusId
     */
    public String getBlCampusId() {
        return blCampusId;
    }

    /**
     * @param blCampusId the blCampusId to set
     */
    public void setBlCampusId(String blCampusId) {
        this.blCampusId = blCampusId;
    }

    /**
     * @return the blCampusName
     */
    public String getBlCampusName() {
        return blCampusName;
    }

    /**
     * @param blCampusName the blCampusName to set
     */
    public void setBlCampusName(String blCampusName) {
        this.blCampusName = blCampusName;
    }

    /**
     * @return the habit
     */
    public String getHabit() {
        return habit;
    }

    /**
     * @param habit the habit to set
     */
    public void setHabit(String habit) {
        this.habit = habit;
    }

    /**
     * @return the gradeLevel
     */
    public String getGradeLevel() {
        return gradeLevel;
    }

    /**
     * @param gradeLevel the gradeLevel to set
     */
    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    /**
     * @return the fatherName
     */
    public String getFatherName() {
        return fatherName;
    }

    /**
     * @param fatherName the fatherName to set
     */
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    /**
     * @return the fatherPhone
     */
    public String getFatherPhone() {
        return fatherPhone;
    }

    /**
     * @param fatherPhone the fatherPhone to set
     */
    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }

    /**
     * @return the motherName
     */
    public String getMotherName() {
        return motherName;
    }

    /**
     * @param motherName the motherName to set
     */
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    /**
     * @return the notherPhone
     */
    public String getNotherPhone() {
        return notherPhone;
    }

    /**
     * @param notherPhone the notherPhone to set
     */
    public void setNotherPhone(String notherPhone) {
        this.notherPhone = notherPhone;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the studyManegerId
     */
    public String getStudyManegerId() {
        return studyManegerId;
    }

    /**
     * @param studyManegerId the studyManegerId to set
     */
    public void setStudyManegerId(String studyManegerId) {
        this.studyManegerId = studyManegerId;
    }

    /**
     * @return the studyManegerName
     */
    public String getStudyManegerName() {
        return studyManegerName;
    }

    /**
     * @param studyManegerName the studyManegerName to set
     */
    public void setStudyManegerName(String studyManegerName) {
        this.studyManegerName = studyManegerName;
    }

    /**
     * @return the attanceNo
     */
    public String getAttanceNo() {
        return attanceNo;
    }

    /**
     * @param attanceNo the attanceNo to set
     */
    public void setAttanceNo(String attanceNo) {
        this.attanceNo = attanceNo;
    }

    /**
     * @return the createUserId
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId the createUserId to set
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return the createUserName
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * @param createUserName the createUserName to set
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**
     * @return the createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the modifyTime
     */
    public String getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime the modifyTime to set
     */
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return the modifyUserId
     */
    public String getModifyUserId() {
        return modifyUserId;
    }

    /**
     * @param modifyUserId the modifyUserId to set
     */
    public void setModifyUserId(String modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    /**
     * @return the modifyUserName
     */
    public String getModifyUserName() {
        return modifyUserName;
    }

    /**
     * @param modifyUserName the modifyUserName to set
     */
    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getOneOnOneRemainingHour() {
        return oneOnOneRemainingHour;
    }

    public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
        this.oneOnOneRemainingHour = oneOnOneRemainingHour;
    }

    public String getStudyManegerContact() {
        return studyManegerContact;
    }

    public void setStudyManegerContact(String studyManegerContact) {
        this.studyManegerContact = studyManegerContact;
    }

    public String getBlCampusContact() {
        return blCampusContact;
    }

    public void setBlCampusContact(String blCampusContact) {
        this.blCampusContact = blCampusContact;
    }

    public String getBlCampusAddress() {
        return blCampusAddress;
    }

    public void setBlCampusAddress(String blCampusAddress) {
        this.blCampusAddress = blCampusAddress;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getFirstSchoolTime() {
        return firstSchoolTime;
    }

    public void setFirstSchoolTime(String firstSchoolTime) {
        this.firstSchoolTime = firstSchoolTime;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

	public StudnetAccMvVo getStudentAccount() {
		return studentAccount;
	}

	public void setStudentAccount(StudnetAccMvVo studentAccount) {
		this.studentAccount = studentAccount;
	}

//	public String getFingerInfo() {
//		return fingerInfo;
//	}
//
//	public void setFingerInfo(String fingerInfo) {
//		this.fingerInfo = fingerInfo;
//	}
	public String getFingerInfo() {
		return fingerInfo;
	}

	public void setFingerInfo(String fingerInfo) {
		this.fingerInfo = fingerInfo;
	}

	public BigDecimal getMiniRemainingHour() {
		return miniRemainingHour;
	}

	public void setMiniRemainingHour(BigDecimal miniRemainingHour) {
		this.miniRemainingHour = miniRemainingHour;
	}

	public String getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}

	public String getLatestCustomerName() {
		return latestCustomerName;
	}

	public void setLatestCustomerName(String latestCustomerName) {
		this.latestCustomerName = latestCustomerName;
	}

	public String getLatestCustomerContact() {
		return latestCustomerContact;
	}

	public void setLatestCustomerContact(String latestCustomerContact) {
		this.latestCustomerContact = latestCustomerContact;
	}

	public String getLatestCustomerId() {
		return latestCustomerId;
	}

	public void setLatestCustomerId(String latestCustomerId) {
		this.latestCustomerId = latestCustomerId;
	}

	@Override
	public String getValue() {
		return this.id;
	}

	public String getOneOnOneStatus() {
		return oneOnOneStatus;
	}

	public void setOneOnOneStatus(String oneOnOneStatus) {
		this.oneOnOneStatus = oneOnOneStatus;
	}

	public String getOneOnOneStatusName() {
		return oneOnOneStatusName;
	}

	public void setOneOnOneStatusName(String oneOnOneStatusName) {
		this.oneOnOneStatusName = oneOnOneStatusName;
	}

	public String getSmallClassStatus() {
		return smallClassStatus;
	}

	public void setSmallClassStatus(String smallClassStatus) {
		this.smallClassStatus = smallClassStatus;
	}

	public String getSmallClassStatusName() {
		return smallClassStatusName;
	}

	public void setSmallClassStatusName(String smallClassStatusName) {
		this.smallClassStatusName = smallClassStatusName;
	}

	public String getBrenchName() {
		return brenchName;
	}

	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
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

	public String getOneOnManyStatus() {
		return oneOnManyStatus;
	}

	public void setOneOnManyStatus(String oneOnManyStatus) {
		this.oneOnManyStatus = oneOnManyStatus;
	}

	public String getOneOnManyStatusName() {
		return oneOnManyStatusName;
	}

	public void setOneOnManyStatusName(String oneOnManyStatusName) {
		this.oneOnManyStatusName = oneOnManyStatusName;
	}

	public String getOtmClassStudentChargeStatus() {
		return otmClassStudentChargeStatus;
	}

	public void setOtmClassStudentChargeStatus(String otmClassStudentChargeStatus) {
		this.otmClassStudentChargeStatus = otmClassStudentChargeStatus;
	}

	public BigDecimal getOtmRemainingHour() {
		return otmRemainingHour;
	}

	public void setOtmRemainingHour(BigDecimal otmRemainingHour) {
		this.otmRemainingHour = otmRemainingHour;
	}

	public String getOtmClassStatus() {
		return otmClassStatus;
	}

	public void setOtmClassStatus(String otmClassStatus) {
		this.otmClassStatus = otmClassStatus;
	}

	public String getOtmClassStatusName() {
		return otmClassStatusName;
	}

	public void setOtmClassStatusName(String otmClassStatusName) {
		this.otmClassStatusName = otmClassStatusName;
	}

	public BigDecimal getOtTwoRemainingHour() {
		return otTwoRemainingHour;
	}

	public void setOtTwoRemainingHour(BigDecimal otTwoRemainingHour) {
		this.otTwoRemainingHour = otTwoRemainingHour;
	}

	public BigDecimal getOtThreeRemainingHour() {
		return otThreeRemainingHour;
	}

	public void setOtThreeRemainingHour(BigDecimal otThreeRemainingHour) {
		this.otThreeRemainingHour = otThreeRemainingHour;
	}

	public BigDecimal getOtFourRemainingHour() {
		return otFourRemainingHour;
	}

	public void setOtFourRemainingHour(BigDecimal otFourRemainingHour) {
		this.otFourRemainingHour = otFourRemainingHour;
	}

    public BigDecimal getOtFiveRemainingHour() {
        return otFiveRemainingHour;
    }

    public void setOtFiveRemainingHour(BigDecimal otFiveRemainingHour) {
        this.otFiveRemainingHour = otFiveRemainingHour;
    }

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getCusContact() {
		return cusContact;
	}

	public void setCusContact(String cusContact) {
		this.cusContact = cusContact;
	}

	public int getCon() {
		return con;
	}

	public void setCon(int con) {
		this.con = con;
	}
    

	public int getOth() {
		return oth;
	}

	

	public void setOth(int oth) {
		this.oth = oth;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}
	


	
    //	public String getBrenchId() {
//		return brenchId;
//	}
//
//	public void setBrenchId(String brenchId) {
//		this.brenchId = brenchId;
//	}


    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }


    public BigDecimal getOneOnOneAlreadyArrangeHour() {
        return oneOnOneAlreadyArrangeHour;
    }

    public void setOneOnOneAlreadyArrangeHour(BigDecimal oneOnOneAlreadyArrangeHour) {
        this.oneOnOneAlreadyArrangeHour = oneOnOneAlreadyArrangeHour;
    }

    public String getSchoolTempId() {
        return schoolTempId;
    }

    public void setSchoolTempId(String schoolTempId) {
        this.schoolTempId = schoolTempId;
    }

    public String getSchoolTempName() {
        return schoolTempName;
    }

    public void setSchoolTempName(String schoolTempName) {
        this.schoolTempName = schoolTempName;
    }

    public String getSchoolOrTemp() {
        return schoolOrTemp;
    }

    public void setSchoolOrTemp(String schoolOrTemp) {
        this.schoolOrTemp = schoolOrTemp;
    }
    
	public String getCurCampusStudyManagerId() {
		return curCampusStudyManagerId;
	}
	
	public void setCurCampusStudyManagerId(String curCampusStudyManagerId) {
		this.curCampusStudyManagerId = curCampusStudyManagerId;
	}

    public int getDaysFromSignUp() {
        daysFromSignUp = DateTools.daysOfTwo(this.createTime, DateTools.getCurrentDate());
        return daysFromSignUp;
    }

    public int getDaysFromLastSignUp() {
        return daysFromLastSignUp;
    }

    public void setDaysFromLastSignUp(int daysFromLastSignUp) {
        this.daysFromLastSignUp = daysFromLastSignUp;
    }

	public Boolean getTransferCanSignUp() {
		return transferCanSignUp;
	}

	public void setTransferCanSignUp(Boolean transferCanSignUp) {
		this.transferCanSignUp = transferCanSignUp;
	}

	public String getDayNumbers() {
		return dayNumbers;
	}

	public void setDayNumbers(String dayNumbers) {
		this.dayNumbers = dayNumbers;
	}

	public Boolean getOvertime() {
		return overtime;
	}

	public void setOvertime(Boolean overtime) {
		this.overtime = overtime;
	}

    
    
    
}
