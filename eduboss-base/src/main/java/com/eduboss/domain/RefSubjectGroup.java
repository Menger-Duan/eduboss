package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 2016-12-15 科组科目关联
 * @author lixuejun
 *
 */
@Entity
@Table(name = "ref_subject_group")
public class RefSubjectGroup {

	private int id;
	private SubjectGroup subjectGroup; // 科组
	private DataDict subject; // 科目
	private int version; // 版本
	private String teacherDes; // 下属老师
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public RefSubjectGroup() {
		super();
	}

	public RefSubjectGroup(int id) {
		super();
		this.id = id;
	}
	
	
	public RefSubjectGroup(SubjectGroup subjectGroup, DataDict subject,
			int version) {
		super();
		this.subjectGroup = subjectGroup;
		this.subject = subject;
		this.version = version;
	}

	public RefSubjectGroup(int id, SubjectGroup subjectGroup, DataDict subject, int version,
			String teacherDes, String createTime,
			String createUserId, String modifyTime, String modifyUserId) {
		super();
		this.id = id;
		this.subjectGroup = subjectGroup;
		this.subject = subject;
		this.version = version;
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
	@JoinColumn(name = "GROUP_ID")
	public SubjectGroup getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(SubjectGroup subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}
	
	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@Column(name = "VERSION")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "TEACHER_DES", length = 255)
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
