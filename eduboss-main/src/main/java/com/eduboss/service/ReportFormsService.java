package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.UserRankInfo;

public interface ReportFormsService {
	/**
	 * 获取整页top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15Data(String startDate,String endDate, Boolean isQueryAll);

	/**
	 * 获取第一个表的top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15UserPerformance(String startDate,String endDate, Boolean isQueryAll);
	
	/**
	 * 获取第二个表的top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15ConsultorPerformance(String startDate,String endDate, Boolean isQueryAll);
	
	/**
	 * 获取第三个表的top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15StudyManagerPerformance(String startDate,String endDate, Boolean isQueryAll);
	
	/**
	 * 获取第四个表的top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15TeacherHours(String startDate,String endDate, Boolean isQueryAll);
	
	/**
	 * 获取第五个表的top15数据
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15StudyManagerHours(String startDate,String endDate, Boolean isQueryAll);
	
	/**
	 * 前三后二地分步返回整页top15数据 - 之前三
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<List<Map<String, Object>>> getTop15DataFirst3(String startDate,String endDate, Boolean isQueryAll);
	
	public List<Map<String, Object>> getRankWithOrgAndTimeSpan(String startDate,String endDate, Organization blBranch,RoleCode roleCode);
	
	public List<Map<String, Object>> getRankWithOrgAndTimeSpanOrgs(String startDate,String endDate, List<Organization> blBranchs,RoleCode roleCode);
	
	/**
	 * 前三后二地分步返回整页top15数据 - 之后二
	 * @param startDate
	 * @param endDate
	 * @param isQueryAll
	 * @return
	 */
	public List<Map<String, Object>> getTop15DataLast2(String startDate,String endDate, Boolean isQueryAll,String type);
	
	/**
	 * 获取校区 #收入# 的报表
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return 回传的是 时间段内的 本机构的 收入的 object list
	 */
	public List<Map> getIncomeRpData(String startDate, String endDate, String orgId);
	
	/**
	 * 获取校区 #消费# 的报表 是总价的
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return 回传的是 时间段内的 本机构的 收入的 object list
	 */
	public List<Map> getOutcomeRpData(String startDate, String endDate, String orgId);
	
	/**
	 * 获取校区 #消费# 的报表 一对一
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return 回传的是 时间段内的 本机构的 收入的 object list
	 */
	public List<Map> getOutcomeForOneRpData(String startDate, String endDate, String orgId);
	
	/**
	 * 获取校区 #消费# 的报表 小班
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return 回传的是 时间段内的 本机构的 收入的 object list
	 */
	public List<Map> getOutcomeForSmallRpData(String startDate, String endDate, String orgId);
	
	/**
	 * 获取校区 #消费# 的报表 其他
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return 回传的是 时间段内的 本机构的 收入的 object list
	 */
	public List<Map> getOutcomeForOtherRpData(String startDate, String endDate, String orgId);



	/**
	 * 包含 #收入#  #消费# 的数据
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return
	 */
	public List getOutcomeIncomeRpData(String startDate, String endDate,
			String orgId);



	/**
	 * 包含 按照 机构 开始时间 结束时间的 学生科目分布
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @return
	 */
	public List getStudentBySubjectRpData(String startDate, String endDate,
			String orgId);

	public List<Map<String, Object>> getTeacherHours(String startDate,
			String endDate, Organization org);

	public List<Map<String, Object>> getStudyManagerHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getTeacherHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);

	public List<Map<String, Object>> getStudyManagerHoursOrgs(String startDate,
			String endDate, List<Organization> orgs);
	
	
	//新增业务逻辑方法  小班 一对多+双师  总课消  分别区分老师与学管师
	//小班 老师课时
	public List<Map<String, Object>> getSmallClassTeacherHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getSmallClassTeacherHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	
	//小班 学管师课时
	public List<Map<String, Object>> getSmallClassStudyManagerHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getSmallClassStudyManagerHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	//其他（包括一对多 双师）老师的课时
	public List<Map<String, Object>> getOtherTeacherHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getOtherTeacherHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	
	//其他（包括一对多 双师）学管师的课时  双师是没有学管的
	public List<Map<String, Object>> getOtherStudyManagerHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getOtherStudyManagerHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	
	//老师的总课时 一对一+小班+一对多+双师
	public List<Map<String, Object>> getTotalTeacherHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getTotalTeacherHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	
	//学管的总课时 一对一+小班+一对多+双师
	public List<Map<String, Object>> getTotalStudyManagerHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getTotalStudyManagerHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);
	
	//新的欢迎页的课消统计 根据不同的产品类型 20170731 xiaojinwang
	public List<Map<String, Object>> getTop10CourseHoursByType(String startDate,String endDate, Boolean isQueryAll,String productType,String type);
	
	
	public List<Map<String, Object>> getOneTeacherHours(String startDate,
			String endDate, Organization org);

	public List<Map<String, Object>> getOneStudyManagerHours(String startDate,
			String endDate, Organization org);
	
	public List<Map<String, Object>> getOneTeacherHoursOrgs(String startDate,
			String endDate,List<Organization> orgs);

	public List<Map<String, Object>> getOneStudyManagerHoursOrgs(String startDate,
			String endDate, List<Organization> orgs);
	
	
	
	
}
