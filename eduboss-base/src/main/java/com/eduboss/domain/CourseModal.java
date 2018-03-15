package com.eduboss.domain;

import com.eduboss.common.WeekDay;

import javax.persistence.*;


@Entity
@Table(name = "course_modal")
public class CourseModal extends BasicDomain implements java.io.Serializable {

	// Fields
	private int id;
	private String modalName;  //名字
	private int techNum;    //讲数
	private DataDict productYear; //年份
	private DataDict productSeason; //季节
	private DataDict coursePhase; //期数
	private Organization branch;  //分公司
	private String suffixName;//后缀
	private String classTime;//上课时间

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="MODAL_NAME")
	public String getModalName() {
		return modalName;
	}

	public void setModalName(String modalName) {
		this.modalName = modalName;
	}

	@Column(name ="TECH_NUM")
	public int getTechNum() {
		return techNum;
	}

	public void setTechNum(int techNum) {
		this.techNum = techNum;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_YEAR")
	public DataDict getProductYear() {
		return productYear;
	}

	public void setProductYear(DataDict productYear) {
		this.productYear = productYear;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_SEASON")
	public DataDict getProductSeason() {
		return productSeason;
	}

	public void setProductSeason(DataDict productSeason) {
		this.productSeason = productSeason;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BRANCH_ID")
	public Organization getBranch() {
		return branch;
	}

	public void setBranch(Organization branch) {
		this.branch = branch;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "COURSE_PHASE")
	public DataDict getCoursePhase() {
		return coursePhase;
	}

	public void setCoursePhase(DataDict coursePhase) {
		this.coursePhase = coursePhase;
	}

	@Column(name ="suffix_name")
	public String getSuffixName() {
		return suffixName;
	}

	public void setSuffixName(String suffixName) {
		this.suffixName = suffixName;
	}

	@Column(name ="CLASS_TIME")
	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}
}