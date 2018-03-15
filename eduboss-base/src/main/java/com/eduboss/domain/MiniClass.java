package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eduboss.utils.DateTools;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.EnrollStatus;
import com.eduboss.common.SaleChannel;
import com.eduboss.common.MiniClassCourseType;
import com.eduboss.common.MiniClassStatus;

/**
 * MiniClass entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mini_class")
public class MiniClass implements java.io.Serializable {

	// Fields

	private String miniClassId;
	private Product product;
	private String name;
	private Organization blCampus;
	private DataDict miniClassType;
	private DataDict grade;
	private DataDict subject;
	private User teacher;
	private String startDate;
	private String endDate;
	private String classTime;
	private Double totalClassHours;
	private Double consume;
	private Double unitPrice;
	private String remark;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private User studyManeger;
	private String courseWeekday;
	private Double everyCourseClassNum;
	private MiniClassStatus status;
//	private String classroom;
	private ClassroomManage classroom;
	private BaseStatus constraintPeopleQuantity;
	private Integer peopleQuantity;
	//private Set<MiniClassCourse> miniClassCourses = new HashSet<MiniClassCourse>(0);
	//private Set<MiniClassEnroll> miniClassEnrolls = new HashSet<MiniClassEnroll>(0);
	private Integer classTimeLength;
	private MiniClassCourseType miniClassCourseType;
	private EnrollStatus recruitStudentStatus; //招生状态 由以前的datadict改为enum

	private Integer minClassMember;//最低开班人数

	private Integer profitMember;//盈利点人数
	
	
	private Set<MiniClassStudent> miniClassStudents ;
	
	private DataDict phase;
	
	private Double arrangedHours;  //已排课时
	private Double canceledHours; //已取消课程
	
	//小班绑定教材
	//新增字段 教材id 教材名字   和产品一对一关系   add 2016-11-08
	private Integer textBookId;
	private String textBookName;
	
	private Boolean bindTextBook;
	
	private int campusSale;
	private int onlineSale;
	private String campusContact;
	private int onShelves;
	private String saleType;

	private CourseModal modal;

	private int isModal;
	private Integer courseModaleId;


	//新增字段
	//允许超额人数
	private Integer allowedExcess;
//	//课程模板id
//	private CourseModal courseModal;


	private String productIdArray;
	
	private String courseStartTime;
	
	private String courseEndTime;

	// Constructors

	/** default constructor */
	public MiniClass() {
	}

	// Property accessors
	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator="generator")
	@Column(name = "MINI_CLASS_ID", unique = true, nullable = false, length = 32)
	public String getMiniClassId() {
		return this.miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCE_ID")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_TYPE")
	public DataDict getMiniClassType() {
		return this.miniClassType;
	}

	public void setMiniClassType(DataDict miniClassType) {
		this.miniClassType = miniClassType;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", length = 20)
	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	//@ManyToOne(fetch=FetchType.LAZY)
	@Column(name = "CLASS_TIME")
	public String getClassTime() {
		return this.classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	@Column(name = "TOTAL_CLASS_HOURS", precision = 9)
	public Double getTotalClassHours() {
		return this.totalClassHours;
	}

	public void setTotalClassHours(Double totalClassHours) {
		this.totalClassHours = totalClassHours;
	}

	@Column(name = "CONSUME", precision = 9)
	public Double getConsume() {
		return this.consume;
	}

	public void setConsume(Double consume) {
		this.consume = consume;
	}

	@Column(name = "UNIT_PRICE", precision = 9)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANEGER_ID")
	public User getStudyManeger() {
		return studyManeger;
	}

	public void setStudyManeger(User studyManeger) {
		this.studyManeger = studyManeger;
	}
	
	@Column(name = "COURSE_WEEKDAY" , length=32)
	public String getCourseWeekday() {
		return courseWeekday;
	}

	public void setCourseWeekday(String courseWeekday) {
		this.courseWeekday = courseWeekday;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Column(name = "EVERY_COURSE_CLASS_NUM", precision = 9)
	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}

	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	public MiniClassStatus getStatus() {
		return status;
	}

	public void setStatus(MiniClassStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CLASSROOM")
	public ClassroomManage getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomManage classroom) {
		this.classroom = classroom;
	}	
//	public String getClassroom() {
//		return classroom;
//	}
//
//	public void setClassroom(String classroom) {
//		this.classroom = classroom;
//	}

	@Enumerated(EnumType.STRING)
	@Column(name="CONSTRAINT_PEOPLE_QUANTITY")
	public BaseStatus getConstraintPeopleQuantity() {
		return constraintPeopleQuantity;
	}

	public void setConstraintPeopleQuantity(BaseStatus constraintPeopleQuantity) {
		this.constraintPeopleQuantity = constraintPeopleQuantity;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "miniClass")
	public Set<MiniClassStudent> getMiniClassStudents() {
		return miniClassStudents;
	}

	public void setMiniClassStudents(Set<MiniClassStudent> miniClassStudents) {
		this.miniClassStudents = miniClassStudents;
	}

	@Column(name = "PEOPLE_QUANTITY", length = 11)
	public Integer getPeopleQuantity() {
		if(peopleQuantity==null)
			peopleQuantity=0;
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}
	
	
	@Column(name="CLASS_TIME_LENGTH",precision = 10)
	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MINI_CLASS_COURSE_TYPE", length = 20)
	public MiniClassCourseType getMiniClassCourseType() {
		return miniClassCourseType;
	}

	public void setMiniClassCourseType(MiniClassCourseType miniClassCourseType) {
		this.miniClassCourseType = miniClassCourseType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "RECRUIT_STUDENT_STATUS")
	public EnrollStatus getRecruitStudentStatus() {
		return recruitStudentStatus;
	}

	public void setRecruitStudentStatus(EnrollStatus recruitStudentStatus) {
		this.recruitStudentStatus = recruitStudentStatus;
	}

	@Column(name = "MIN_CLASS_MEMBER", length = 11)
	public Integer getMinClassMember() {
		return minClassMember;
	}

	public void setMinClassMember(Integer minClassMember) {
		this.minClassMember = minClassMember;
	}

	@Column(name = "PROFIT_MEMBER", length = 11)
	public Integer getProfitMember() {
		return profitMember;
	}

	public void setProfitMember(Integer profitMember) {
		this.profitMember = profitMember;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PHASE")
	public DataDict getPhase() {
		return phase;
	}

	public void setPhase(DataDict phase) {
		this.phase = phase;
	}

	
	@Column(name = "ARRANGED_HOURS", precision = 9)
	public Double getArrangedHours() {
		return arrangedHours;
	}

	public void setArrangedHours(Double arrangedHours) {
		this.arrangedHours = arrangedHours;
	}

	@Column(name = "CANCELED_HOURS", precision = 9)
	public Double getCanceledHours() {
		return canceledHours;
	}

	public void setCanceledHours(Double canceledHours) {
		this.canceledHours = canceledHours;
	}
	
	@Column(name = "TEXTBOOK_ID")
	public Integer getTextBookId() {
		return textBookId;
	}

	public void setTextBookId(Integer textBookId) {
		this.textBookId = textBookId;
	}

	@Column(name = "TEXTBOOK_NAME", length = 32)
	public String getTextBookName() {
		return textBookName;
	}

	public void setTextBookName(String textBookName) {
		this.textBookName = textBookName;
	}

	@Transient
	public Boolean getBindTextBook() {
		return bindTextBook;
	}

	public void setBindTextBook(Boolean bindTextBook) {
		this.bindTextBook = bindTextBook;
	}
	
	@Column(name = "CAMPUS_SALE")
	public int getCampusSale() {
		return campusSale;
	}

	public void setCampusSale(int campusSale) {
		this.campusSale = campusSale;
	}

	@Column(name = "ONLINE_SALE")
	public int getOnlineSale() {
		return onlineSale;
	}

	public void setOnlineSale(int onlineSale) {
		this.onlineSale = onlineSale;
	}

	@Column(name = "CAMPUS_CONTACT", length = 32)
	public String getCampusContact() {
		return campusContact;
	}

	public void setCampusContact(String campusContact) {
		this.campusContact = campusContact;
	}

	@Column(name = "ON_SHELVES")
	public int getOnShelves() {
		return onShelves;
	}

	public void setOnShelves(int onShelves) {
		this.onShelves = onShelves;
	}


	@Transient
	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}


	/*	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "miniClass")
	public Set<MiniClassCourse> getMiniClassCourses() {
		return this.miniClassCourses;
	}

	public void setMiniClassCourses(Set<MiniClassCourse> miniClassCourses) {
		this.miniClassCourses = miniClassCourses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "miniClass")
	public Set<MiniClassEnroll> getMiniClassEnrolls() {
		return this.miniClassEnrolls;
	}

	public void setMiniClassEnrolls(Set<MiniClassEnroll> miniClassEnrolls) {
		this.miniClassEnrolls = miniClassEnrolls;
	}*/

	@Transient
	public String getTimeSpace() {
		Double i =classTimeLength*everyCourseClassNum;//分钟
		Double second = i * 60;
		return classTime+"-"+DateTools.addSecond(classTime, Integer.valueOf(second.intValue()));
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "modal_id")
	public CourseModal getModal() {
		return modal;
	}

	public void setModal(CourseModal modal) {
		this.modal = modal;
	}

	@Column(name = "IS_MODAL")
	public int getIsModal() {
		return isModal;
	}

	public void setIsModal(int isModal) {
		this.isModal = isModal;
	}

	@Column(name = "allowed_excess")
	public Integer getAllowedExcess() {
		if(allowedExcess==null)
			allowedExcess=0;
		return allowedExcess;

	}

	public void setAllowedExcess(Integer allowedExcess) {
		this.allowedExcess = allowedExcess;
	}

	@Transient
	public Integer getCourseModaleId() {
		return courseModaleId;
	}

	public void setCourseModaleId(Integer courseModaleId) {
		this.courseModaleId = courseModaleId;
	}



//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name = "course_modal_id")
//	public CourseModal getCourseModal() {
//		return courseModal;
//	}
//
//	public void setCourseModal(CourseModal courseModal) {
//		this.courseModal = courseModal;
//	}
	@Transient
	public String getProductIdArray() {
		return productIdArray;
	}

	public void setProductIdArray(String productIdArray) {
		this.productIdArray = productIdArray;
	}

	@Column(name = "course_start_time", length = 20)
    public String getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(String courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    @Column(name = "coures_end_time", length = 20)
    public String getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(String courseEndTime) {
        this.courseEndTime = courseEndTime;
    }
	
}