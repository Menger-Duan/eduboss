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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.StudentFileType;

/**
 * 学生档案
 * @author guohuaming
 *
 */
@Entity
@Table(name = "student_file")
public class StudentFile {
	
	private String id;
	private StudentFileType studentFileType; //档案类型
	private String docDescription;//描述
	private String fileName; //文件名
	private String saveName; //阿里云名
	private Student student; //学生
	private User creator; //创建者
	private String createTime;//创建时间
	private String realPath; 
	
	
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
		
	@Enumerated(EnumType.STRING)
	@Column(name = "FILE_TYPE", length = 32)
	public StudentFileType getStudentFileType() {
		return studentFileType;
	}
	
	public void setStudentFileType(StudentFileType studentFileType) {
		this.studentFileType = studentFileType;
	}	
	 
	
	@Column(name = "DESCRIPTION")
	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}
	
	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "SAVE_NAME", length = 100)
	public String getSaveName() {
		return saveName;
	}
	public void setSaveName(String saveName) {
		this.saveName = saveName;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
		
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="CREATOR_ID")
	public User getCreator() {
		return creator;
	}
	public void setCreator(User creator) {
		this.creator = creator;
	}
		
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Transient
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	
			   
}
