package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 2016-12-15 科组管理
 * @author lixuejun
 *
 */
@Entity
@Table(name = "subject_group")
public class SubjectGroup {

	private int id;
	private DataDict name; //名称
	private Organization blBrench; // 所属分公司
	private Organization blCampus; // 所属校区
	private int version; // 版本
	private int rorder; // 排序
	private Set<RefSubjectGroup> refSubjectGroups;
	private String subjectDes; // 下属科目
	private String teacherDes; // 下属老师
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public SubjectGroup() {
		super();
	}
	
	public SubjectGroup(int id) {
		super();
		this.id = id;
	}
	
	public SubjectGroup(int id, DataDict name, Organization blBrench,
			Organization blCampus, int version, int rorder,
			Set<RefSubjectGroup> refSubjectGroups, String subjectDes,
			String teacherDes, String createTime, String createUserId,
			String modifyTime, String modifyUserId) {
		super();
		this.id = id;
		this.name = name;
		this.blBrench = blBrench;
		this.blCampus = blCampus;
		this.version = version;
		this.rorder = rorder;
		this.refSubjectGroups = refSubjectGroups;
		this.subjectDes = subjectDes;
		this.teacherDes = teacherDes;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NAME")
	public DataDict getName() {
		return name;
	}

	public void setName(DataDict name) {
		this.name = name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BRENCH_ID")
	public Organization getBlBrench() {
		return blBrench;
	}

	public void setBlBrench(Organization blBrench) {
		this.blBrench = blBrench;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Column(name = "VERSION", length = 6)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Column(name = "RORDER")
	public int getRorder() {
		return rorder;
	}

	public void setRorder(int rorder) {
		this.rorder = rorder;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "subjectGroup")
	@OrderBy("id ASC")
	public Set<RefSubjectGroup> getRefSubjectGroups() {
		return refSubjectGroups;
	}
	
	public void setRefSubjectGroups(Set<RefSubjectGroup> refSubjectGroups) {
		this.refSubjectGroups = refSubjectGroups;
	}

	@Column(name = "SUBJECT_DES", length = 500)
	public String getSubjectDes() {
		return subjectDes;
	}

	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}

	@Column(name = "TEACHER_DES", length = 500)
	public String getTeacherDes() {
		return teacherDes;
	}

	public void setTeacherDes(String teacherDes) {
		this.teacherDes = teacherDes;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

}
