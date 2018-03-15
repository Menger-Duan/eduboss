package com.eduboss.domain;

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

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MiniClassRelationType;

@Entity
@Table(name = "mini_class_relation")
public class MiniClassRelation {

	private String id;
	private Student student;
	private MiniClass oldMiniClass;
	private MiniClass newMiniClass;
	private MiniClassRelationType relationType;
	private String createTime;
	private String createUserId;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OLD_MINI_CLASS_ID")
	public MiniClass getOldMiniClass() {
		return oldMiniClass;
	}
	public void setOldMiniClass(MiniClass oldMiniClass) {
		this.oldMiniClass = oldMiniClass;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NEW_MINI_CLASS_ID")
	public MiniClass getNewMiniClass() {
		return newMiniClass;
	}
	public void setNewMiniClass(MiniClass newMiniClass) {
		this.newMiniClass = newMiniClass;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "RELATION_TYPE", length = 32)
	public MiniClassRelationType getRelationType() {
		return relationType;
	}
	public void setRelationType(MiniClassRelationType relationType) {
		this.relationType = relationType;
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
}
