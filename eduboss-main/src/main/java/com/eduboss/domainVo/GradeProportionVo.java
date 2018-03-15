package com.eduboss.domainVo;

import java.math.BigDecimal;

public class GradeProportionVo {
	
	private String type;
	
	/**小一至五占比*/
	private  BigDecimal primaryOneToFiveRat; 
	/**
	 * 小六占比
	 */
	private  BigDecimal primarySixRat; 
	/**
	 * 初一占比
	 */
	private BigDecimal juniorOneRat;
	
	/**
	 * 初二占比
	 */
	private BigDecimal juniorTwoRat;
	
	
	/**
	 * 初三占比
	 */
	private BigDecimal juniorThreeRat;
	
	/**
	 * 高1占比
	 */
	private BigDecimal seniorOneRat;
	
	
	/**
	 * 高2占比
	 */
	private BigDecimal seniorTwoRat;
	
	
	/**
	 * 高3占比
	 */
	private BigDecimal seniorThreeRat;


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public BigDecimal getPrimaryOneToFiveRat() {
		return primaryOneToFiveRat;
	}


	public void setPrimaryOneToFiveRat(BigDecimal primaryOneToFiveRat) {
		this.primaryOneToFiveRat = primaryOneToFiveRat;
	}


	public BigDecimal getPrimarySixRat() {
		return primarySixRat;
	}


	public void setPrimarySixRat(BigDecimal primarySixRat) {
		this.primarySixRat = primarySixRat;
	}


	public BigDecimal getJuniorOneRat() {
		return juniorOneRat;
	}


	public void setJuniorOneRat(BigDecimal juniorOneRat) {
		this.juniorOneRat = juniorOneRat;
	}


	public BigDecimal getJuniorTwoRat() {
		return juniorTwoRat;
	}


	public void setJuniorTwoRat(BigDecimal juniorTwoRat) {
		this.juniorTwoRat = juniorTwoRat;
	}


	public BigDecimal getJuniorThreeRat() {
		return juniorThreeRat;
	}


	public void setJuniorThreeRat(BigDecimal juniorThreeRat) {
		this.juniorThreeRat = juniorThreeRat;
	}


	public BigDecimal getSeniorOneRat() {
		return seniorOneRat;
	}


	public void setSeniorOneRat(BigDecimal seniorOneRat) {
		this.seniorOneRat = seniorOneRat;
	}


	public BigDecimal getSeniorTwoRat() {
		return seniorTwoRat;
	}


	public void setSeniorTwoRat(BigDecimal seniorTwoRat) {
		this.seniorTwoRat = seniorTwoRat;
	}


	public BigDecimal getSeniorThreeRat() {
		return seniorThreeRat;
	}


	public void setSeniorThreeRat(BigDecimal seniorThreeRat) {
		this.seniorThreeRat = seniorThreeRat;
	}
	
	
   
}
