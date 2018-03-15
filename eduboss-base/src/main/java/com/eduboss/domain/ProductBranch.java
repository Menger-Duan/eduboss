package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_BRANCH")
public class ProductBranch implements java.io.Serializable {

	private int id;
	private Product product;
	private Organization branch;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BRANCH_ID")
	public Organization getBranch() {
		return branch;
	}

	public void setBranch(Organization branch) {
		this.branch = branch;
	}
}