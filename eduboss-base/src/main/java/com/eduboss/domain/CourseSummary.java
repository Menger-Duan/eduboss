package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CourseSummaryType;

/**
 * CourseSummary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "course_summary")
public class CourseSummary implements java.io.Serializable {

    // Fields

    private String courseSummaryId;
    private Student student;
    private User teacher;
    private User studyManager;
    private CourseRequirement courseRequirement;
    private User teachingManager;
    private DataDict subject;
    private DataDict grade;
    private String startDate;
    private String endDate;
    private Double spanHours;
    private String courseTime;
    private BigDecimal courseMinutes;// 课程分钟数
    private Set<Course> courses = new HashSet<Course>(0);

//    private WeekDay weekDay; // 星期，为空则为非周期性排课
    private String weekDay;
    private Integer weekInterval; // 星期间隔，间隔weekInterval周
    private Double planHours; // 每次课时

    private int delFlag; // 删除标志 0-否 1-是
    private CourseSummaryType type; // 排课类型（面向学生、面向老师）

    private Product product;
    // Constructors

    /**
     * default constructor
     */
    public CourseSummary() {
    }

    /**
     * minimal constructor
     */
    public CourseSummary(Student student, DataDict subject, DataDict grade, String startDate, String endDate, Double spanHours) {
        this.student = student;
        this.subject = subject;
        this.grade = grade;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spanHours = spanHours;
    }

    /**
     * full constructor
     */
    public CourseSummary(Student student, User teacher, User studyManager, CourseRequirement courseRequirement, User teachingManager, DataDict subject,
                         DataDict grade, String startDate, String endDate, Double spanHours, String courseTime, BigDecimal courseMinutes, Set<Course> courses) {
        this.student = student;
        this.teacher = teacher;
        this.studyManager = studyManager;
        this.courseRequirement = courseRequirement;
        this.teachingManager = teachingManager;
        this.subject = subject;
        this.grade = grade;
        this.startDate = startDate;
        this.endDate = endDate;
        this.spanHours = spanHours;
        this.courseTime = courseTime;
        this.courseMinutes = courseMinutes;
        this.courses = courses;
    }

    // Property accessors
    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
    @GeneratedValue(generator = "generator")
    @Column(name = "COURSE_SUMMARY_ID", unique = true, nullable = false, length = 32)
    public String getCourseSummaryId() {
        return this.courseSummaryId;
    }

    public void setCourseSummaryId(String courseSummaryId) {
        this.courseSummaryId = courseSummaryId;
    }

    //	@Column(name = "STUDENT_ID", nullable = false, length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEACHER_ID")
    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    //	@Column(name = "STADUY_MANAGER_ID", length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDY_MANAGER_ID")
    public User getStudyManager() {
        return this.studyManager;
    }

    public void setStudyManager(User studyManager) {
        this.studyManager = studyManager;
    }

    //	@Column(name = "COURSE_REQUIREMENT_ID", length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_REQUIREMENT_ID")
    public CourseRequirement getCourseRequirement() {
        return this.courseRequirement;
    }

    public void setCourseRequirement(CourseRequirement courseRequirement) {
        this.courseRequirement = courseRequirement;
    }

    //	@Column(name = "TEACHING_MANAGER_ID", length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEACHING_MANAGER_ID")
    public User getTeachingManager() {
        return this.teachingManager;
    }

    public void setTeachingManager(User teachingManager) {
        this.teachingManager = teachingManager;
    }

    //	@Column(name = "SUBJECT", nullable = false, length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJECT")
    public DataDict getSubject() {
        return this.subject;
    }

    public void setSubject(DataDict subject) {
        this.subject = subject;
    }

    //	@Column(name = "GRADE", nullable = false, length = 32)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRADE")
    public DataDict getGrade() {
        return this.grade;
    }

    public void setGrade(DataDict grade) {
        this.grade = grade;
    }

    @Column(name = "START_DATE", nullable = false, length = 32)
    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE", nullable = false, length = 32)
    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Column(name = "SPAN_HOURS", nullable = false, precision = 10)
    public Double getSpanHours() {
        return this.spanHours;
    }

    public void setSpanHours(Double spanHours) {
        this.spanHours = spanHours;
    }

    @Column(name = "COURSE_TIME", length = 20)
    public String getCourseTime() {
        return this.courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    @Column(name = "COURSE_MINUTES", precision = 10)
    public BigDecimal getCourseMinutes() {
        return courseMinutes;
    }

    public void setCourseMinutes(BigDecimal courseMinutes) {
        this.courseMinutes = courseMinutes;
    }

    @Transient
    //@JsonIgnore
    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "courseSummary")
    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    /*
    @Enumerated(EnumType.STRING)
    @Column(name = "week_day")
    public WeekDay getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDay weekDay) {
        this.weekDay = weekDay;
    }
    */

    @Column(name = "week_day")
    public String getWeekDay() {
    	return weekDay;
    }
    
    public void setWeekDay(String weekDay) {
    	this.weekDay = weekDay;
    }
    
    @Column(name = "week_interval")
    public Integer getWeekInterval() {
        return weekInterval;
    }

	public void setWeekInterval(Integer weekInterval) {
        this.weekInterval = weekInterval;
    }

    @Column(name = "plan_hours")
    public Double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Double planHours) {
        this.planHours = planHours;
    }

    @Column(name = "del_flag")
    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public CourseSummaryType getType() {
        return type;
    }

    public void setType(CourseSummaryType type){ this.type = type; }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PRODUCT_ID")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}