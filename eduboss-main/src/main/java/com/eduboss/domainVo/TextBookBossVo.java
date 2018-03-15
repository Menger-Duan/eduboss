package com.eduboss.domainVo;

import java.io.Serializable;

public class TextBookBossVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String createBy;
	private String createDttm;
	private String updateBy;
	private String updateDttm;
	private Integer referTimes;
	private Integer wordFlag;
	private Integer pptFlag;
	private Integer videoFlag;
	private String  deadline;
	private String publishDttm;
	private Integer teaWordFlag;
	private Integer stuWordFlag;
	private Integer stuAnswerFlag;
	private Integer stuCmcFlag;
	private String createDate;
	private String createByName;
	
	
	private Integer bossTag;//所属集团(1-boss 0-培优)
	private String keyWord;//教材名称 用于搜索
	private String name; // 教材名称
	private String year; // 年份
	private Integer sectionId; // 学段
	private String sectionName;//学段名称
	private Integer subjectId; // 科目
	private String subjectName;//科目名称
	private String publishVersion; // 版本
	private String publishVersionName;
	private String bookVersion; // 书本(逗号分隔)
	private String bookVersionName; // 书本书本名称(逗号分隔)
	private Integer wareType; // 教材类型：1-预习2-复习
	//private String wareTypeName;//教材类型名称
	private String classTypeId; // 适用班型id
	private String classTypeName; // 适用班型名称
	private String pressExtId; // 出版公司编号
	private String pressName; // 出版公司名称
	private String orgType; // 出版公司类型(组织机构类型)：OTHER-其他DEPARTMENT-部门CAMPUS-校区BRENCH-分公司GROUNP-集团PARGANA-大区
	private String season; // 季度：1-春季2-暑假3-秋季4-寒假
	private Integer wareStatus; // 状态：0-未创建(默认)1-未发布2-已发布3-已下架
	//private String wareStatusName;//状态名称
	private String supportContent;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateDttm() {
		return createDttm;
	}
	public void setCreateDttm(String createDttm) {
		this.createDttm = createDttm;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateDttm() {
		return updateDttm;
	}
	public void setUpdateDttm(String updateDttm) {
		this.updateDttm = updateDttm;
	}
	public Integer getReferTimes() {
		return referTimes;
	}
	public void setReferTimes(Integer referTimes) {
		this.referTimes = referTimes;
	}
	public Integer getWordFlag() {
		return wordFlag;
	}
	public void setWordFlag(Integer wordFlag) {
		this.wordFlag = wordFlag;
	}
	public Integer getPptFlag() {
		return pptFlag;
	}
	public void setPptFlag(Integer pptFlag) {
		this.pptFlag = pptFlag;
	}
	public Integer getVideoFlag() {
		return videoFlag;
	}
	public void setVideoFlag(Integer videoFlag) {
		this.videoFlag = videoFlag;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getPublishDttm() {
		return publishDttm;
	}
	public void setPublishDttm(String publishDttm) {
		this.publishDttm = publishDttm;
	}
	public Integer getTeaWordFlag() {
		return teaWordFlag;
	}
	public void setTeaWordFlag(Integer teaWordFlag) {
		this.teaWordFlag = teaWordFlag;
	}
	public Integer getStuWordFlag() {
		return stuWordFlag;
	}
	public void setStuWordFlag(Integer stuWordFlag) {
		this.stuWordFlag = stuWordFlag;
	}
	public Integer getStuAnswerFlag() {
		return stuAnswerFlag;
	}
	public void setStuAnswerFlag(Integer stuAnswerFlag) {
		this.stuAnswerFlag = stuAnswerFlag;
	}
	public Integer getStuCmcFlag() {
		return stuCmcFlag;
	}
	public void setStuCmcFlag(Integer stuCmcFlag) {
		this.stuCmcFlag = stuCmcFlag;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public Integer getBossTag() {
		return bossTag;
	}
	public void setBossTag(Integer bossTag) {
		this.bossTag = bossTag;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getSectionId() {
		return sectionId;
	}
	public void setSectionId(Integer sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getPublishVersion() {
		return publishVersion;
	}
	public void setPublishVersion(String publishVersion) {
		this.publishVersion = publishVersion;
	}
	public String getPublishVersionName() {
		return publishVersionName;
	}
	public void setPublishVersionName(String publishVersionName) {
		this.publishVersionName = publishVersionName;
	}
	public String getBookVersion() {
		return bookVersion;
	}
	public void setBookVersion(String bookVersion) {
		this.bookVersion = bookVersion;
	}
	public String getBookVersionName() {
		return bookVersionName;
	}
	public void setBookVersionName(String bookVersionName) {
		this.bookVersionName = bookVersionName;
	}
	public Integer getWareType() {
		return wareType;
	}
	public void setWareType(Integer wareType) {
		this.wareType = wareType;
	}
	public String getClassTypeId() {
		return classTypeId;
	}
	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}
	public String getClassTypeName() {
		return classTypeName;
	}
	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}
	public String getPressExtId() {
		return pressExtId;
	}
	public void setPressExtId(String pressExtId) {
		this.pressExtId = pressExtId;
	}
	public String getPressName() {
		return pressName;
	}
	public void setPressName(String pressName) {
		this.pressName = pressName;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public Integer getWareStatus() {
		return wareStatus;
	}
	public void setWareStatus(Integer wareStatus) {
		this.wareStatus = wareStatus;
	}
	public String getSupportContent() {
		return supportContent;
	}
	public void setSupportContent(String supportContent) {
		this.supportContent = supportContent;
	}
	
	

}
