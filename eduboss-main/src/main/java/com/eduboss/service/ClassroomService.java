package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.Classroom;
import com.eduboss.dto.ClassroomUseStatus;

public interface ClassroomService {
	/**
	 * 根据教室名称获取教室
	 * @param namePattern
	 * @return
	 */
	public List<Classroom> getClassroomNames(String namePattern);
	
	/**
	 * 根据条件获取教室使用状态
	 * @param vo
	 * @return
	 */
	public List<ClassroomUseStatus> getClassroomStatus(ClassroomUseStatus vo);
}
