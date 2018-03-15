package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;

@Repository
public interface OtmClassCourseDao extends GenericDAO<OtmClassCourse, String>  {

	/**
	 * @param OtmClassCourse
	 */
	@Override
	void save(OtmClassCourse OtmClassCourse);

	/**
	 * 根据一对多更新一对多课程
	 * @param otmClass
	 * @throws Exception
	 */
	public void updateOtmClassCourse(OtmClass otmClass) throws Exception;
	
	/**
	 * 通过一对多ID查出所有一对多课程，按课程时间倒序
	 * @param otmClassId
	 * @return
	 */
	public List<OtmClassCourse> getOtmClassCourseListByOtmClassId(String otmClassId);
	
	/**
	 * 通过一对多ID查出所有一对多课程，按课程时间倒序，返回类型为DataPackage
	 * @param otmClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseListByOtmClassId(String otmClassId, String firstSchoolDate, DataPackage dp);
	
	/**
	 * 一对多课程详情
	 * @param otmClassId
	 * @param dp
	 * @param modelVo
	 * @return
	 */
	public DataPackage getOtmClassCourseDetail(String otmClassId, DataPackage dp,ModelVo modelVo);
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp);
	
	/**
	 * 一对多课程列表手机接口
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseListForMobile(OtmClassCourseVo otmClassCourseVo,
			DataPackage dp);
	
	/**
	 * 一对多课程审批汇总(课时)
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	public DataPackage getOtmClassCourseAuditAnalyze(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes);


	public List getOtmClassCourseAuditSalaryNums( BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes);



	/**
	 *一对多课程审批汇总(小时)
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @param otmClassTypes
	 * @param subject
	 * @return
	 */
	public DataPackage getOtmClassCourseAuditAnalyzeXiaoShi(DataPackage dp, BasicOperationQueryVo vo, String auditStatus, String otmClassTypes);

	/**
	 * 一对多课程审批汇总工资
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	public DataPackage otmClaCourseAuditAnalyzeSalary(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan);
	
	/**
	 * 一对多审批列表
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @return
	 */
	public DataPackage otmClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String subject);


}
