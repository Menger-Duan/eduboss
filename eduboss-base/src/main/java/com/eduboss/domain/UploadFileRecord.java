package com.eduboss.domain;

import java.io.Serializable;

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

import com.eduboss.common.UploadFileStatus;

/**@author wmy
 *@date 2015年11月4日上午9:46:11
 *@version 1.0 
 *@description
 */
@Entity
@Table(name = "upload_file_record")
public class UploadFileRecord implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6715833446889153513L;

	private String id;
	
	private String filePath;
	
	private String createTime;
	
	private String modifyTime;
	
	private String fileSize;
	
	private UploadFileStatus fileStatus;
	
	private String realFileName;
	
	private User uploadUser;
	
	public UploadFileRecord() {
		
	}
	
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

	@Column(name = "FILE_PATH", length = 100)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "FILE_STATUS", length = 20)
	public UploadFileStatus getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(UploadFileStatus fileStatus) {
		this.fileStatus = fileStatus;
	}
	@Column(name = "FILE_SIZE", length = 20)
	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	@Column(name = "REAL_FILE_NAME", length = 100)
	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UPLOAD_USER_ID")
	public User getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(User uploadUser) {
		this.uploadUser = uploadUser;
	}
	
}


