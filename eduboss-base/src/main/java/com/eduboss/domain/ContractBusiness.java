package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "CONTRACT_BUSINESS")
public class ContractBusiness {

	private int id;

	public ContractBusiness() {
		super();
	}

	public ContractBusiness(int id) {
		super();
		this.id = id;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
