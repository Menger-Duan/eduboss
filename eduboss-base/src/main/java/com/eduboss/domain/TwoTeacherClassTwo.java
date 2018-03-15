package com.eduboss.domain;

import com.eduboss.common.MiniClassStatus;

import javax.persistence.*;

@Entity
@Table(name = "TWO_TEACHER_CLASS_TWO")
public class TwoTeacherClassTwo implements java.io.Serializable {

	private int id;
	private TwoTeacherClass twoTeacherClass;
	private String name;
	private Organization blCampus;
	private ClassroomManage classroom;
	private User teacher;
	private Integer peopleQuantity;
	private MiniClassStatus status;

	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String remark;
	private String qq;
	private String webChat;

	public TwoTeacherClassTwo() {
	}

	public TwoTeacherClassTwo(int twoClassId) {
		this.id=twoClassId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLASS_TWO_ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID")
	public TwoTeacherClass getTwoTeacherClass() {
		return twoTeacherClass;
	}

	public void setTwoTeacherClass(TwoTeacherClass twoTeacherClass) {
		this.twoTeacherClass = twoTeacherClass;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	public MiniClassStatus getStatus() {
		return status;
	}

	public void setStatus(MiniClassStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CLASS_ROOM_ID")
	public ClassroomManage getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomManage classroom) {
		this.classroom = classroom;
	}	

	@Column(name = "PEOPLE_QUANTITY", length = 11)
	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	@Column(name = "QQ")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "WEB_CHAT")
	public String getWebChat() {
		return webChat;
	}

	public void setWebChat(String webChat) {
		this.webChat = webChat;
	}
}