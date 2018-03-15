package com.eduboss.domainVo.EduPlatform;

import java.util.List;

public class BaseRelateDate2BossVo {

	private List<PublishVersion> publishVersion;
	private List<BookVersion> bookVersion;
	private List<Section> section;
	private List<Subject> subject;
	public static class PublishVersion {
		private Integer publishVersionId;
		private String publishVersionName; 
		public Integer getPublishVersionId() {
			return publishVersionId;
		}
		public void setPublishVersionId(Integer publishVersionId) {
			this.publishVersionId = publishVersionId;
		}
		public String getPublishVersionName() {
			return publishVersionName;
		}
		public void setPublishVersionName(String publishVersionName) {
			this.publishVersionName = publishVersionName;
		}
	}
	public static class BookVersion {
		private Integer bookVersionId;
		private String bookVersionName; 
		public Integer getBookVersionId() {
			return bookVersionId;
		}
		public void setBookVersionId(Integer bookVersionId) {
			this.bookVersionId = bookVersionId;
		}
		public String getBookVersionName() {
			return bookVersionName;
		}
		public void setBookVersionName(String bookVersionName) {
			this.bookVersionName = bookVersionName;
		}		
	}
	
	public static class Section {
		private Integer sectionId;
		private String sectionName; 

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
		
	}
	
	public static class Subject {
		private Integer subjectId;
		private String subjectName; 
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

		
	}



	public List<PublishVersion> getPublishVersion() {
		return publishVersion;
	}

	public void setPublishVersion(List<PublishVersion> publishVersion) {
		this.publishVersion = publishVersion;
	}

	public List<BookVersion> getBookVersion() {
		return bookVersion;
	}

	public void setBookVersion(List<BookVersion> bookVersion) {
		this.bookVersion = bookVersion;
	}

	public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

	public List<Subject> getSubject() {
		return subject;
	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}


	
	
}
