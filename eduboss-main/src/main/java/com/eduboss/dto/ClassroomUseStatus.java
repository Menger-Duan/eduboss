package com.eduboss.dto;

public class ClassroomUseStatus {
	private String id;
	private String name;
	private String useSource;
	private String date;
	private String timeStart;
	private String timeEnd;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public String getUseSource() {
		return useSource;
	}
	public void setUseSource(String useSource) {
		this.useSource = useSource;
	}
	
}
