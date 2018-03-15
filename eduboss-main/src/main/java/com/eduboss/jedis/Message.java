package com.eduboss.jedis;

import java.io.Serializable;
import java.math.BigDecimal;

import com.eduboss.common.MessageQueueType;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午12:00:53 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 7792729L;
    private int id;
    private String content;
    private String name;
    private MessageQueueType type;
    private String flag;
    private BigDecimal money;
    private String CampusId;
    private String userId;
    private String dateTime;
    private String userName;
    private String campusName;
    private String brenchId;
    private String brenchName;
    private Boolean isOnline=false;
    
    public Message() {
	}
    
    public Message(int i, String string) {
		this.id=i;
		this.content=string;
	}
	public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MessageQueueType getType() {
		return type;
	}
	public void setType(MessageQueueType type) {
		this.type = type;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getCampusId() {
		return CampusId;
	}
	public void setCampusId(String campusId) {
		CampusId = campusId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	public String getBrenchName() {
		return brenchName;
	}

	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
	}

	public Boolean getOnline() {
		return isOnline;
	}

	public void setOnline(Boolean online) {
		isOnline = online;
	}
}
