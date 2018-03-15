package com.eduboss.mail.pojo;

public class MailInfoView {

	private String mid;  //信件标识 (字符串)
	private Integer msid;  //MSID (整数)
	private Integer fid;  //文件夹标识 (整数), 比如 1 表示收件箱
	private Long flag;  //信件标记集 (长整数), 每个位代表不同含义, 其中第 7 个位表示是否有附件, 可使用这样的位运算 (flag & (1 << 7)) != 0 计算得出
	private String from;  //发件人 (字符串)
	private String to;  //收件人 (字符串)
	private String subject;  //主题 (字符串)
	private Integer size;  //信件大小 (整数)
	private String date;  //信件日期, 格式为 (yyyy-MM-dd HH:mm:ss), 时区以服务器为准
	private String fromUserDeptName;
	
	//userDefine
	private String readStatus = "1";  //信件读取状态:0未读，1已读（默认）
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public Integer getMsid() {
		return msid;
	}
	public void setMsid(Integer msid) {
		this.msid = msid;
	}
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public Long getFlag() {
		return flag;
	}
	public void setFlag(Long flag) {
		this.flag = flag;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}
	public String getFromUserDeptName() {
		return fromUserDeptName;
	}
	public void setFromUserDeptName(String formUserDeptName) {
		this.fromUserDeptName = formUserDeptName;
	}
	
}
