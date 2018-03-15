package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Yao
 * 分校收入
 * 2015-04-13
 */
public class CampusIncomeVo {

	private String campusId;
	private String campusName;
	private BigDecimal total;
	private BigDecimal quantity;
	
	Set<CampusIncomeVo> ciList=new HashSet<CampusIncomeVo>();
	
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public Set<CampusIncomeVo> getCiList() {
		return ciList;
	}
	public void setCiList(Set<CampusIncomeVo> ciList) {
		this.ciList = ciList;
	}
	public BigDecimal getTotal() {
		for (Iterator iterator = ciList.iterator(); iterator.hasNext();) {
			CampusIncomeVo vo = (CampusIncomeVo) iterator.next();
			total.add(vo.getTotal());
		}
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getQuantity() {
		for (Iterator iterator = ciList.iterator(); iterator.hasNext();) {
			CampusIncomeVo vo = (CampusIncomeVo) iterator.next();
			quantity.add(vo.getQuantity());
		}
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
