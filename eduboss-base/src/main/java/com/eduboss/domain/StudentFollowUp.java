package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="STUDENT_FOLLOW_RECORD")
public class StudentFollowUp implements java.io.Serializable{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	
	private String id;
	private Student student;
	private String followUpTime;
	private String remark;
	private User followUpUser;
	private String nextTime;
	private String nextFollowUpRemark;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	private String modifyUserId;
	private String followUpResult;
	private String fileName;
	private String fileAliName;
	
	
	//Constructors
	
//	/** default constructor */
//	public StudentFollowUp(){}
//	
//	/** minimal constructor */
//	public StudentFollowUp(String id){
//		this.id=id;
//	}
//	
//	/** full constructor */
//	public StudentFollowUp(String id, Student student, String followUpTime,
//			String remark, User followUpUser, String nextTime,
//			String nextFollowUpRemark, String createUserId, String createTime,
//			String modifyTime, String modifyUserId, String followUpResult) {
//		this.id = id;
//		this.student = student;
//		this.followUpTime = followUpTime;
//		this.remark = remark;
//		this.followUpUser = followUpUser;
//		this.nextTime = nextTime;
//		this.nextFollowUpRemark = nextFollowUpRemark;
//		this.createUserId = createUserId;
//		this.createTime = createTime;
//		this.modifyTime = modifyTime;
//		this.modifyUserId = modifyUserId;
//		this.followUpResult = followUpResult;
//	}
//	
	//@ManyToOne(fetch=FetchType.LAZY)
	@Column(name="FOLLOW_UP_RESULT")
	public String getFollowUpResult() {
		return followUpResult;
	}
	

	public void setFollowUpResult(String followUpResult) {
		this.followUpResult = followUpResult;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	
	@Column(name = "FOLLOWUP_TIME", length = 20)
	public String getFollowUpTime() {
		return followUpTime;
	}
	public void setFollowUpTime(String followUpTime) {
		this.followUpTime = followUpTime;
	}
	
	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FOLLOWUP_USER")
	public User getFollowUpUser() {
		return followUpUser;
	}
	public void setFollowUpUser(User followUpUser) {
		this.followUpUser = followUpUser;
	}
	
	@Column(name="NEXT_FOLLOWUP_TIME",length=20)
	public String getNextTime() {
		return nextTime;
	}
	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}
	
	@Column(name = "NEXT_FOLLOWUP_REMAR", length = 500)
	public String getNextFollowUpRemark() {
		return nextFollowUpRemark;
	}
	public void setNextFollowUpRemark(String nextFollowUpRemark) {
		this.nextFollowUpRemark = nextFollowUpRemark;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}

	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	@Column(name = "FILE_ALI_NAME", length = 100)
	public String getFileAliName() {
		return fileAliName;
	}


	public void setFileAliName(String fileAliName) {
		this.fileAliName = fileAliName;
	}
}
