package com.pad.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsContentFileVo implements Serializable{

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsContentId;

	private Integer fileId;

	private Date createTime;

	private String createUser;

	private String filePath;

	private String fileName;

	private String fileSize;

	private String aliSaveTime;

	public String getAliSaveTime() {
		return aliSaveTime;
	}

	public void setAliSaveTime(String aliSaveTime) {
		this.aliSaveTime = aliSaveTime;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmsContentId() {
		return cmsContentId;
	}

	public void setCmsContentId(Integer cmsContentId) {
		this.cmsContentId = cmsContentId;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

}
