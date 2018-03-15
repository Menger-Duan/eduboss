package com.eduboss.jedis;

import java.io.Serializable;
import java.math.BigDecimal;

import com.eduboss.common.MessageQueueType;

/** 
 * @author  author :Yao 
 * @date  2016年8月17日 下午12:00:53 
 * @version 1.0 
 */
public class FinanceMessage implements Serializable{
    private static final long serialVersionUID = 7792729L;
    private String flag;
    private String countDate;
    private String CampusId;
    private String userId;
    private String userName;
    private String campusName;
    private String brenchId;
    private String brenchName;
    private BigDecimal newMoney=BigDecimal.ZERO;
    private BigDecimal preMoney=BigDecimal.ZERO;
	private BigDecimal totalMoney=BigDecimal.ZERO;
	private BigDecimal refundMoney=BigDecimal.ZERO;
	private Boolean isOnline=false;
    
    public FinanceMessage() {
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
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

	public BigDecimal getNewMoney() {
		return newMoney;
	}

	public void setNewMoney(BigDecimal newMoney) {
		this.newMoney = newMoney;
	}

	public BigDecimal getPreMoney() {
		return preMoney;
	}

	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}

	public BigDecimal getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}

	public BigDecimal getRefundMoney() {
		return refundMoney;
	}

	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}

	public Boolean getOnline() {
		return isOnline;
	}

	public void setOnline(Boolean online) {
		isOnline = online;
	}
}
