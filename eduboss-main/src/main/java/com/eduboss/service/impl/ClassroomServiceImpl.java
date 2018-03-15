package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.ClassroomDao;
import com.eduboss.domain.Classroom;
import com.eduboss.dto.ClassroomUseStatus;
import com.eduboss.service.ClassroomService;

@Service("ClassroomService")
public class ClassroomServiceImpl implements ClassroomService {

	@Autowired
	private ClassroomDao classroomDao;
	
	@Override
	public List<Classroom> getClassroomNames(String namePattern) {
		return classroomDao.getClassroomNames(namePattern);
	}

	@Override
	public List<ClassroomUseStatus> getClassroomStatus(ClassroomUseStatus vo) {
		return classroomDao.getClassroomStatus(vo);
	}

}
