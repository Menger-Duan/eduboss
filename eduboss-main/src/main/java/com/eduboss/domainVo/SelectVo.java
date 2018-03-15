package com.eduboss.domainVo;

import java.util.List;

/**
 * 
 * @author xiaojinwang
 *
 */
public class SelectVo {
     
	private String id;
	private String name;
	private Integer order;
	private List<SelectVo> list;
	
	public SelectVo(){
		this.order = 0;		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SelectVo> getList() {
		return list;
	}

	public void setList(List<SelectVo> list) {
		this.list = list;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
}
