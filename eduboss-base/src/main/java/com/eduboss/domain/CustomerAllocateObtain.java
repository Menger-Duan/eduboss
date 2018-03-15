package com.eduboss.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author duanmenrun 20171110
 * 
 */
@Entity
@Table(name = "customer_allocate_obtain")
public class CustomerAllocateObtain {

    private String id; 
    
    private String customerId;
    
    private String allocateTime;
    
    private String obtainTime;
    
    private String type;
    
    private int status;//0未获取，1已获取

    public CustomerAllocateObtain() {
        super();
    }

  

	public CustomerAllocateObtain(String id, String customerId, String allocateTime, String obtainTime, String type,
			int status) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.allocateTime = allocateTime;
		this.obtainTime = obtainTime;
		this.type = type;
		this.status = status;
	}



	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "customer_id", length = 32)
    public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "allocate_time")
	public String getAllocateTime() {
		return allocateTime;
	}

	public void setAllocateTime(String allocateTime) {
		this.allocateTime = allocateTime;
	}
	
	@Column(name = "obtain_time")
	public String getObtainTime() {
		return obtainTime;
	}

	public void setObtainTime(String obtainTime) {
		this.obtainTime = obtainTime;
	}
	
	@Column(name = "type", length = 32)
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "status", length = 1)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CustomerAllocateObtain [id=" + id + ", customerId=" + customerId + ", allocateTime=" + allocateTime
				+ ", obtainTime=" + obtainTime + ", type=" + type + ", status=" + status + "]";
	}
    
}
