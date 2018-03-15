package com.eduboss.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mini_class_inventory")
public class MiniClassInventory implements Serializable {

	private static final long serialVersionUID = 4040774209451218694L;
	private Integer id;
	private String miniClassId;
	private Integer maxQuantity;
	private Integer normalQuantity;
	private Integer usedQuantity;
	
	public MiniClassInventory() {
		super();
	}
	
	public MiniClassInventory(Integer id, String miniClassId,
            Integer maxQuantity, Integer normalQuantity, Integer usedQuantity) {
        super();
        this.id = id;
        this.miniClassId = miniClassId;
        this.maxQuantity = maxQuantity;
        this.normalQuantity = normalQuantity;
        this.usedQuantity = usedQuantity;
    }

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "MINI_CLASS_ID", length=32)
	public String getMiniClassId() {
		return miniClassId;
	}
	
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	
	@Column(name = "MAX_QUANTIY")
	public Integer getMaxQuantity() {
		return maxQuantity;
	}
	
	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	
	@Column(name = "NORMAL_QUANTIY")
	public Integer getNormalQuantity() {
        return normalQuantity;
    }

    public void setNormalQuantity(Integer normalQuantity) {
        this.normalQuantity = normalQuantity;
    }

    @Column(name = "USED_QUANTITY")
	public Integer getUsedQuantity() {
		return usedQuantity;
	}
	
	public void setUsedQuantity(Integer usedQuantity) {
		this.usedQuantity = usedQuantity;
	}

    @Override
    public String toString() {
        return "MiniClassInventory [id=" + id + ", miniClassId=" + miniClassId
                + ", maxQuantity=" + maxQuantity + ", normalQuantity="
                + normalQuantity + ", usedQuantity=" + usedQuantity + "]";
    }
	
}
