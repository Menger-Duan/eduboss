package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.dto.Response;

/**
 * 直播同步标识
 *
 */
@Entity
@Table(name = "live_sync_sign")
public class LiveSyncSign extends Response implements java.io.Serializable {

	// Fields

	private String id;
	private String recordId;
	
	
	public LiveSyncSign() {
		super();
	}

	public LiveSyncSign(String id, String recordId) {
		super();
		this.id = id;
		this.recordId = recordId;
	}



	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	
	@Column(name = "record_id", length = 32)
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@Override
	public String toString() {
		return "LiveSyncSign [id=" + id + ", recordId=" + recordId + "]";
	}

	
	

	
}