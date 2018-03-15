package com.eduboss.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.common.AppUploadType;
import com.eduboss.domain.Course;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.service.AppUploadCallBackService;
import com.eduboss.service.CourseService;
import com.eduboss.service.OtmClassService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.TwoTeacherClassService;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.StringUtil;


@Service("appUploadCallBackService")
public class UploadCallBackServiceImpl implements AppUploadCallBackService {
	
	private final static Logger log = Logger.getLogger(UploadCallBackServiceImpl.class);
	@Autowired
	private CourseService courseService;
	@Autowired
	private TwoTeacherClassService twoTeacherClassService;
	@Autowired
	private SmallClassService smallClassService;
	@Autowired
	private OtmClassService otmClassService;
	

	@Override
	public void uploadCallBackService(String ossCallbackBody) {
		// TODO Auto-generated method stub
		JSONObject jsonObject = JSON.parseObject(ossCallbackBody); 
		if (jsonObject.get("object") != null){
			if(jsonObject.get("x:uploadid") != null && jsonObject.get("x:uploadtype")!=null){
				String uploadId = jsonObject.getString("x:uploadid");
				String uploadType = jsonObject.getString("x:uploadtype");
				String fileName = jsonObject.getString("x:filename");
				CallBack callBack = null;
				if(uploadType.equals(AppUploadType.ONEONONE.getValue())){
					callBack = new OneOnOne();
					callBack.doCallBack(uploadId,fileName);
				}
				if(uploadType.equals(AppUploadType.MINICLASS.getValue())){
					callBack = new MiniClass();
					callBack.doCallBack(uploadId,fileName);
				}
				if(uploadType.equals(AppUploadType.OTM.getValue())){
					callBack = new Otm();
					callBack.doCallBack(uploadId,fileName);
				}
				if(uploadType.equals(AppUploadType.TWOTEACHER.getValue())){
					callBack = new TwoTeacher();
					callBack.doCallBack(uploadId,fileName);
				}
				if(uploadType.equals(AppUploadType.OTHER.getValue())){
					callBack = new Other();
					callBack.doCallBack(uploadId,fileName);
				}
			
				//区分类型  Course course = courseService.getCourseByCourseId(courseId);
			}
		}
	}
	
	
	interface CallBack{
		public void doCallBack(String uploadId,String fileName);
	}
	
	class OneOnOne implements CallBack{
		public void doCallBack(String uploadId,String fileName){
			log.info("uploadId:"+uploadId);
//			Course course = courseService.getCourseByCourseId(uploadId);
//			course.setAttendacePicName(fileName);
//			courseService.updateCourse(course);
			courseService.updateCoursePicName(uploadId, fileName);
		}
	}
	class TwoTeacher implements CallBack{
		public void doCallBack(String uploadId,String fileName){
			System.out.println("uploadId:"+uploadId);
			Integer twoTeacherCourseId = Integer.valueOf(uploadId.split("_")[0]);
			Integer twoTeacherClassTwoId = Integer.valueOf(uploadId.split("_")[1]);
			twoTeacherClassService.updatePicByCourseAndClassId(twoTeacherCourseId,twoTeacherClassTwoId,fileName);
		}
	}
	class Otm implements CallBack{
		public void doCallBack(String uploadId,String fileName){
			System.out.println("uploadId:"+uploadId);
			OtmClassCourse otmClassCourse = otmClassService.getOtmClassCourseById(uploadId);
			otmClassCourse.setAttendacePicName(fileName);
			otmClassService.updateOtmClassCourse(otmClassCourse);
		}
	}
	class MiniClass implements CallBack{
		public void doCallBack(String uploadId,String fileName){
			System.out.println("uploadId:"+uploadId);
			MiniClassCourse miniClassCourse = smallClassService.getMiniClassCourseById(uploadId);
			miniClassCourse.setAttendacePicName(fileName);
			smallClassService.updateMiniClassCourse(miniClassCourse);
		}
	}
	class Other implements CallBack{
		
		//如果是上传用户头像，不用修改用户数据库，callBack不做处理，默认是mobileUserId.jpg	
		public void doCallBack(String uploadId,String fileName){
			System.out.println("uploadId:"+uploadId);
		}
	}

}
