package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.eduboss.utils.DateTools;

public class MiniClassCourseConsumeVo {
	
	private String miniClassCourseId;
	private String courseTime;//上课时间
	private String courseEndTime;//结束时间
	private String courseDate;
	private String miniClassName;
	private Double courseHours;
	private Integer attendanceCount;//考勤数量
	private Integer newClassPeopleNum;// 未上课人数
	private Integer completeClassPeopleNum;// 已上课人数
	private Integer leaveClassPeopleNum;// 请假人数
	private Integer absentClassPeopleNum;// 缺勤人数
	private Integer studentCount;// 报名人数
	private Integer noAttendanceCount;// 未考勤数量
	
	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}
	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getMiniClassName() {
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	public Double getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}
	public Integer getAttendanceCount() {
		return attendanceCount;
	}
	public void setAttendanceCount(Integer attendanceCount) {
		this.attendanceCount = attendanceCount;
	}
	public Integer getNewClassPeopleNum() {
		return newClassPeopleNum;
	}
	public void setNewClassPeopleNum(Integer newClassPeopleNum) {
		this.newClassPeopleNum = newClassPeopleNum;
	}
	public Integer getCompleteClassPeopleNum() {
		return completeClassPeopleNum;
	}
	public void setCompleteClassPeopleNum(Integer completeClassPeopleNum) {
		this.completeClassPeopleNum = completeClassPeopleNum;
	}
	public Integer getLeaveClassPeopleNum() {
		return leaveClassPeopleNum;
	}
	public void setLeaveClassPeopleNum(Integer leaveClassPeopleNum) {
		this.leaveClassPeopleNum = leaveClassPeopleNum;
	}
	public Integer getAbsentClassPeopleNum() {
		return absentClassPeopleNum;
	}
	public void setAbsentClassPeopleNum(Integer absentClassPeopleNum) {
		this.absentClassPeopleNum = absentClassPeopleNum;
	}
	public Integer getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}	
	public Integer getNoAttendanceCount() {
		return noAttendanceCount;
	}
	public void setNoAttendanceCount(Integer noAttendanceCount) {
		this.noAttendanceCount = noAttendanceCount;
	}
//	public String getWeekStr() {
//		if(StringUtils.isNotEmpty(this.getCourseDate())){
//			StringBuffer sb = new StringBuffer();
//			
//			 //传人参数当前月第一天
//			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
//			//传人参数当前月最后一天
//			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());
//			 
//			Calendar cal =Calendar.getInstance();
//			
//			cal.setTime(DateTools.getDate(this.getCourseDate()));
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//			
//			Date startDate=cal.getTime();			
//			if(startDate.getTime()<firstDay.getTime()){
//				startDate=firstDay;
//			}
//			
//			sb.append(DateTools.getDateToString(startDate));
//			cal.add(Calendar.WEEK_OF_YEAR, 1);
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//			
//			Date endDate=cal.getTime();
//			if(endDate.getTime()>lastDay.getTime()){
//				endDate=lastDay;
//			}
//			
//			sb.append(" 至 ").append(DateTools.getDateToString(endDate));
//			return sb.toString();
//		}else{
//			return "";
//		}
//	}
	
	public String getWeekStr() {  
		if(StringUtils.isNotEmpty(this.getCourseDate())){		
			 //传人参数当前月第一天
			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
			//传人参数当前月最后一天
			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());
			 
			 StringBuffer sb = new StringBuffer();
			 Calendar cal = Calendar.getInstance();  
	         cal.setTime(DateTools.getDate(this.getCourseDate()));  
	         //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	         int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
	         if(1 == dayWeek) {  
	             cal.add(Calendar.DAY_OF_MONTH, -1);  
	         }		        
	         cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
	         int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
	         cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   		         
	         Date startDate=cal.getTime();
			if(startDate.getTime()<firstDay.getTime()){
				startDate=firstDay;
			}				
			sb.append(DateTools.getDateToString(startDate));	        
	        cal.add(Calendar.DATE, 6);
	        Date endDate=cal.getTime();					 
			if(endDate.getTime()>lastDay.getTime()){
				endDate=lastDay;
			}			
			sb.append(" 至 ").append(DateTools.getDateToString(endDate));
			return sb.toString();				
	        
		}else{
			return "";
		}        
           
     }  
	
	
	public String getMonthStr() {
		if(StringUtils.isNotEmpty(this.getCourseDate())){
			StringBuffer sb = new StringBuffer();
			
			 //传人参数当前月第一天
			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
			//传人参数当前月最后一天
			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());		
			
			sb.append(DateTools.getDateToString(firstDay));
			sb.append(" 至 ").append(DateTools.getDateToString(lastDay));
			return sb.toString();
		}else{
			return "";
		}
	}
	
}
