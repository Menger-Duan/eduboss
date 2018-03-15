package com.eduboss.jedis;

import java.io.Serializable;
import java.math.BigDecimal;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;

/** 
 * @author  author :Yao 
 * @date  2016年8月17日 下午12:00:53 
 * @version 1.0 
 */
public class IncomeMessage implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	
	private String flag;
    private String countDate;
    private String CampusId;
    private String campusName;
    private String brenchId;
    private String brenchName;
    private ProductType type;
	private ChargeType chargeType;
	private PayType payType; //资金来源（REAL：实收金额，PROMOTION：优惠金额）
	private ChargePayType chargePayType; // 扣费，冲销类型
    
    private BigDecimal amount=BigDecimal.ZERO;
    private BigDecimal quantity=BigDecimal.ZERO;
    
    public IncomeMessage() {
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

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public ChargePayType getChargePayType() {
		return chargePayType;
	}

	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

    
}
