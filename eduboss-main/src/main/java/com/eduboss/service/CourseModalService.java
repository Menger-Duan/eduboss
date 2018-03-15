package com.eduboss.service;

import com.eduboss.domainVo.CourseModalVo;
import com.eduboss.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface CourseModalService {

	Response saveCourseModal(CourseModalVo vo);

	List<CourseModalVo> getCourseModalList(CourseModalVo vo);

	CourseModalVo findModalByModalId(int modalId);

    Response deleteCourseModal(int modalId);
}
