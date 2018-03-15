package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.StudentStatus;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Repository
public class StudentDaoImpl extends GenericDaoImpl<Student, String> implements StudentDao {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public void updateStudentManeger(String studentIds, String oldStudyManagerId, String newStudyManagerId) {
		Map<String, Object> studentParams = new HashMap<String, Object>();
		Map<String, Object> studyManagerParams = new HashMap<String, Object>();
		String hql=" update Student set studyManegerId = :newStudyManagerId where 1=1 ";
		String updateStudyManager="update StudentOrganization set studyManager.userId = :newStudyManagerId where studyManager.userId = :oldStudyManagerId and isMainOrganization='1' ";
		studentParams.put("newStudyManagerId", newStudyManagerId);
		studyManagerParams.put("newStudyManagerId", newStudyManagerId);
		studyManagerParams.put("oldStudyManagerId", oldStudyManagerId);
		if(!"all".equals(studentIds)){
			if (StringUtil.isNotBlank(studentIds)) {
				String[] ids = studentIds.split(",");
				if (ids.length > 1) {
					hql += " and id in (:ids) ";
					updateStudyManager+=" and student.id in (:ids) ";
					studentParams.put("ids", ids);
					studyManagerParams.put("ids", ids);
				} else {
					hql += " and id = :id ";
					updateStudyManager+=" and student.id = :id ";
					studentParams.put("id", ids[0]);
					studyManagerParams.put("id", ids[0]);
				}
			}
		} else {
			hql += " and studyManegerId = :oldStudyManagerId ";
			studentParams.put("oldStudyManagerId", oldStudyManagerId);
		}
		super.excuteHql(hql, studentParams);
		super.excuteHql(updateStudyManager, studyManagerParams);
//		super.excuteHql(courseHql);//血管今天以后的课程换学管
	}

	@Override
	public Student findByAttendanceNo(String attendanceNo,Organization org) {
		List<Student> students = super.findByCriteria(Expression.eq("attanceNo", attendanceNo),Expression.eq("blCampusId", org.getId()));
		if (students.size() == 0) {
			throw new ApplicationException(ErrorCode.STUDENT_NOT_FOUND_BY_ATTENDANCE_NO);
		}
		return students.get(0);
	}
	
	public boolean checkIfAttendanceNoExist(String studentId, String attendanceNo,String blCampusId) {
		List<Student> students = super.findByCriteria(Expression.eq("attanceNo", attendanceNo), Expression.ne("id",  studentId),Expression.eq("blCampusId", blCampusId));
		return students.size() > 0;
	}
	
	@Override
	public Student findByIcCardNo(String icCardNo) {
		List<Student> students = super.findByCriteria(Expression.eq("icCardNo", icCardNo));
		if (students.size() == 0) {
			throw new ApplicationException(ErrorCode.STUDENT_NOT_FOUND_BY_IC_CARD_NO);
		}
		return students.get(0);
	}
	
	/**
	 * 查找学生列表（自动搜索）
	 */
	public List<Map<Object,Object>> getStudentAutoComplate(String input,List<Organization> organization) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct s.id value,CONCAT(s.name, \"(\", CONCAT_WS(\",\", o.NAME, u.NAME), \")\")  label from student s ");
		sql.append(" left join student_organization so  on s.id = so.STUDENT_ID");
		sql.append(" left join organization o on o.id = so.ORGANIZATION_ID");
		sql.append(" left join user u on s.STUDY_MANEGER_ID = u.user_id ");
		
		if (StringUtils.isNotBlank(input)){
			sql.append(" where (s.id like :id or s.name like :name)");
			params.put("id", "%" + input + "%");
			params.put("name", "%" + input + "%");
		}else {
			sql.append(" where 1=1 ");
		}

		if(organization.size()>0){
			sql.append(" and (s.BL_CAMPUS_ID in (select id from Organization where orgLevel like :blCampusLevel) or (so.IS_MAIN_ORGANIZATION='0' and o.orgLevel like :orgLevel)");
			params.put("blCampusLevel", organization.get(0).getOrgLevel() + "%");
			params.put("orgLevel", organization.get(0).getOrgLevel() + "%");
		}
		for (int i =1;i<organization.size();i++) {
			sql.append(" or s.BL_CAMPUS_ID in (select id from Organization where orgLevel like :blCampusLevel" + i +") or (so.IS_MAIN_ORGANIZATION='0' and o.orgLevel like :orgLevel" + i + ")");
			params.put("blCampusLevel" + i, organization.get(i).getOrgLevel() + "%");
			params.put("orgLevel" + i, organization.get(i).getOrgLevel() + "%");
		}
		if(organization.size()>0){
			sql.append(")");
		}
		
		sql.append(" limit 20");
		return this.findMapBySql(sql.toString(), params);
	}
	
	/**
	 * 查找学生列表（自动搜索） ,hql 方式查询，当input中有英文单引号时sql编译会出错
	 */
	@Override
	public List<Student> getStudentAutoComplate(String input) {		
 		SimpleExpression idLike = Expression.like("id", input, MatchMode.ANYWHERE);
		SimpleExpression nameLike = Expression.like("name", input, MatchMode.ANYWHERE);
		return (List<Student>) this.findPageByCriteria(new DataPackage(0, 20), new ArrayList<Order>(), Expression.or(idLike, nameLike) ).getDatas();
	}
	
	/**
	 * 查找某校区学生列表（自动搜索） ,hql 方式查询，当input中有英文单引号时sql编译会出错
	 */
	@Override
	public List<Student> getStudentAutoComplateByCampusId(String input, String campusId) {
		SimpleExpression idLike = Expression.like("id", input, MatchMode.ANYWHERE);
		SimpleExpression nameLike = Expression.like("name", input, MatchMode.ANYWHERE);
		return (List<Student>) this.findPageByCriteria(new DataPackage(0, 20), new ArrayList<Order>(), Expression.or(idLike, nameLike), Expression.eq("blCampusId", campusId) ).getDatas();
	}

	@Override
	public boolean isExistsStudent(String name, String contact, String fatherPhone, String notherPhone) {
		List<Criterion> criteriaList = new ArrayList<Criterion>();
		criteriaList.add(Expression.eq("name", name));
		if(StringUtils.isNotEmpty(contact)){
			if(contact.indexOf(".")!=-1)
				contact=contact.substring(0,contact.indexOf("."));
			criteriaList.add(Expression.eq("contact", contact));
		}else if(StringUtils.isNotEmpty(fatherPhone)){
			criteriaList.add(Expression.eq("fatherPhone", fatherPhone));
		}else if(StringUtils.isNotEmpty(notherPhone)){
			criteriaList.add(Expression.eq("notherPhone", notherPhone));
		}
		List list=this.findAllByCriteria(criteriaList);
		if(list==null || list.size()==0){
			return false;
		}
		return true;
	}
	
	public List<Student> findStudentForCampus(Organization organization){
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from Student where id in (select student.id from StudentOrganization where organization.id = :organizationId)  and (studentStatus is null or studentStatus!=0) ";
		params.put("organizationId", organization.getId());
		return super.findLimitHql(hql, 9999, params);
	}
	
	
	@Override
	public List<Student> findStudentByCampus(Organization organization){
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from Student where id in (select student.id from StudentOrganization where organization.orgLevel like :organizationId)  and (studentStatus is null or studentStatus!=0) ";
		params.put("organizationId", organization.getOrgLevel()+"%");
		return super.findLimitHql(hql, 9999, params);
	}

    /**
     * 查找所有学生with课程数量
     *
     * @param courseVo
     * @return
     */
    @Override
    public List<StudentVo> findStudentWithCourseCount(CourseVo courseVo,Organization organization) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	List<Student> students = findStudentForCampus(organization);
		Map<String,StudentVo> map = new HashMap<String, StudentVo>(students.size());
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            StudentVo vo = new StudentVo();
            vo.setId(student.getId());
            vo.setName(student.getName());
            vo.setCourseCount(0);
            map.put(student.getId(),vo);
        }
        StringBuffer sql = new StringBuffer("select s.id id,s.name name, count(s.id) courseCount from course c LEFT JOIN student s on c.student_id = s.id LEFT JOIN user teacher on c.TEACHER_ID = teacher.USER_ID where 1=1 ");
        if(courseVo != null){
            if(StringUtils.isNotBlank(courseVo.getTeacherId())){
                sql.append(" and c.teacher_id = :teacherId ");
                params.put("teacherId", courseVo.getTeacherId());
            }
            if(StringUtils.isNotBlank(courseVo.getTeacherName())){
                sql.append(" and teacher.name = :teacherName ");
                params.put("teacherName", courseVo.getTeacherName());
            }
            if(StringUtils.isNotBlank(courseVo.getCourseDate())){
                sql.append(" and c.course_date = :courseDate ");
                params.put("courseDate", courseVo.getCourseDate());
            }
            if(StringUtils.isNotBlank(courseVo.getStartDate())){
                sql.append(" and c.course_date >= :startDate ");
                params.put("startDate", courseVo.getStartDate());
            }
            if(StringUtils.isNotBlank(courseVo.getEndDate())){
                sql.append(" and c.course_date <= :endDate");
                params.put("endDate", courseVo.getEndDate());
            }
            if(StringUtils.isNotBlank(courseVo.getCourseTime())){
                sql.append(" and c.course_time = :courseTime");
                params.put("courseTime", courseVo.getCourseTime());
            }
        }
        sql.append(" group by s.id,s.name ");
        List<Map<Object, Object>> list = super.findMapBySql(sql.toString(), params);
        
        for (Map<Object, Object> courseMap : list) {
        	String studentId = (String) courseMap.get("id");
        	if(map.containsKey(studentId)){
                map.get(studentId).setCourseCount(Integer.valueOf(courseMap.get("courseCount").toString()));
            }
        }
        return new ArrayList<StudentVo>(map.values());
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllStudentFingerInfo() {
		String hql=" select studentFingerNo,studentId,fingerInfo from StudentFingerInfo ";
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Student> getStudentForPerform(String input) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = userService.getCurrentLoginUser();
		Organization org = new Organization();
		if(user.getOrganization()!=null && user.getOrganization().size()>0){
			org = user.getOrganization().get(0);
		}
		DataPackage dp = new DataPackage();
		dp.setPageSize(5);
		StringBuilder hql = new StringBuilder();
		hql.append(" from Student where blCampus.orgLevel like :orgLevel ") ;
		params.put("orgLevel", org.getOrgLevel() + "%");
		if(StringUtils.isNotBlank(input)){
			hql.append(" and name like '%").append(input).append("%'");
		}
		dp = super.findPageByHQL(hql.toString(), dp, true, params);
		List<Student> list = (List<Student>) dp.getDatas();
		return list;
	}

	@Override
	public List<Student> findStudentByStaffId(String staffId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Student> stus = new ArrayList<Student>();
		// 一对一 老师角色的  学生
		StringBuffer hql_1on1_teacher = new StringBuffer();
		hql_1on1_teacher.append("select distinct  course.student from Course as course where course.teacher.userId = :staffId ");
		params.put("staffId", staffId);
		stus.addAll(super.findAllByHQL(hql_1on1_teacher.toString(), params));
		
		// 一对一 学管角色的  学生
		StringBuffer hql_1on1_studyManager = new StringBuffer();
		hql_1on1_studyManager.append("select distinct  course.student from Course as course where course.studyManager.userId = :staffId ");
		stus.addAll(super.findAllByHQL(hql_1on1_studyManager.toString(), params));
		
		// 小班 得到 老师角色的  学生
//		StringBuffer hql_mini_teacher = new StringBuffer();
//		hql_mini_teacher.append("select distinct course.teacher from MiniClassCourse as course ") 
//			.append("where exists ( from course.miniClass.miniClassStudents as miniStudent where miniStudent.student.id  = '" )
//			.append(staffId ).append("')");
//		stus.addAll(super.findAllByHQL(hql_mini_teacher.toString()));

		// 小班 班主任角色的  学生
//		StringBuffer hql_mini_studyHead= new StringBuffer();
//		hql_mini_studyHead.append("select distinct course.studyHead from MiniClassCourse as course ") 
//		.append("where exists ( from course.miniClass.miniClassStudents as miniStudent where miniStudent.student.id  = '" )
//		.append(staffId ).append("')");
//		stus.addAll(super.findAllByHQL(hql_mini_studyHead.toString()));
		return stus;
		
	}
	
	/* (non-Javadoc)
	 * @see com.eduboss.dao.StudentDao#findPageByHQLForAcc(java.lang.String, com.eduboss.dto.DataPackage, java.lang.String)
	 * 传入查询数据的sql,跟统计的sql
	 */
	@Override
	public DataPackage findPageByHQLForAcc(String hql,DataPackage dp,String countHql, Map<String, ?> params) {
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
		q.setMaxResults(dp.getPageSize());
		dp.setDatas(q.list());
		dp.setRowCount(findCountHql(countHql, params));
		return dp;
	}
	
	/**
	 * @param 查询所有学生校区ID（包括跨校区）
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getAllStudentOrganization() {
		String hql=" select distinct organization.id from StudentOrganization ";
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();
		
	}
	/**
	 * @param 查询该校区所有状态为"上课中"的学生ID
	 * @return
	 */
	@Override
	public List<Map<Object, Object>> getStudentIdByOrganizationId(String organizationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" SELECT s.ID FROM student s INNER JOIN STUDENT_ORGANIZATION o ON s.ID=o.STUDENT_ID  WHERE  s.`STATUS`='CLASSING' and o.ORGANIZATION_ID = :organizationId ";
		params.put("organizationId", organizationId);
		return findMapBySql(sql, params); 
		
	}
	
	/**
	 * @param 查询所有状态为"上课中"的学生ID
	 * @return
	 */
	@Override
	public List<Map<Object, Object>> getAllStudentId() {
		String sql=" SELECT s.ID FROM student s WHERE  s.`STATUS`='CLASSING'";	 
		return findMapBySql(sql, new HashMap<String, Object>()); 
	}
	
	/**
	 * 查询所有为上课中的学生指纹
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllStudentFingerInfoByStatus() {	   		
		String hql=" select f.studentFingerNo,f.studentId,f.fingerInfo from StudentFingerInfo f,Student s where f.studentId=s.id and s.status='"+StudentStatus.CLASSING+"'";
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();		
	}
	
	/**
	 * 查询所有今天有课的学生指纹
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getTodayStudentFingerInfo() {	   		
		String today="";
		try {
			today = DateTools.getCurrentDate("yyyyMMdd");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String hql=" select f.studentFingerNo,f.studentId,f.fingerInfo from StudentFingerInfo f,CourseConflict c where f.studentId=c.student.id and c.startTime>='"+today+"0000' and c.endTime<='"+today+"2359'";
		String hql=" select f.studentFingerNo,f.studentId,f.fingerInfo from StudentFingerInfo f where f.studentId in ( select distinct c.student.id from CourseConflict c where c.startTime>='"+today+"0000' and c.endTime<='"+today+"2359')";
		Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		return q.list();		
	}
	
	@Override
	public Map<Object, Object> getCusAndStuByStudentId(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder(" select s.ID as studentId,s.`NAME` as studentName,c.`NAME` as customerName,c.CONTACT as contract,s.GRADE_ID as gradeId,c.ID as customerId from student s ");	 
		sql.append(" left JOIN customer_student_relation csr on csr.STUDENT_ID=s.ID ");
		sql.append(" left JOIN customer c on c.ID=csr.CUSTOMER_ID ");
		sql.append(" where s.ID = :studentId ");
		params.put("studentId", studentId);
		List<Map<Object, Object>> list = findMapBySql(sql.toString(), params); 
		if(list.size()>0)
			return (Map<Object, Object>) list.get(0);
		else
			return null;
	}

	
	@Override
	public void upgrade(String cla,String course,String orgnizationId,OrganizationType type, String gradeNames) {
		String[] names = gradeNames.split(",");
		String currentDate=DateTools.getNineOneDate();
		StringBuffer baseSql = new StringBuffer();
		StringBuffer cBaseSql = new StringBuffer();
		if(StringUtil.isNotBlank(cla)){
			baseSql.append(" (case  ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '幼儿园' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '一年级' and CATEGORY = 'STUDENT_GRADE') ");			
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '一年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '二年级' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '二年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '三年级' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '三年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '四年级' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '四年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '五年级' and CATEGORY = 'STUDENT_GRADE')  ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '五年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '六年级' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '六年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初一' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '初一' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初二' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '初二' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初三' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '初三' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高一' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '高一' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高二' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '高二' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高三' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '高三' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" when GRADE_ID = (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') ");
			baseSql.append(" end) where 1=1 ");
			
		}
		
		if(StringUtil.isNotBlank(course)){
			cBaseSql.append(" (case  ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '幼儿园' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '一年级' and CATEGORY = 'STUDENT_GRADE') ");			
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '一年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '二年级' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '二年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '三年级' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '三年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '四年级' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '四年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '五年级' and CATEGORY = 'STUDENT_GRADE')  ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '五年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '六年级' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '六年级' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初一' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '初一' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初二' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '初二' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '初三' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '初三' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高一' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '高一' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高二' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '高二' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '高三' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '高三' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" when GRADE = (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') then (select id from data_dict where name = '毕业' and CATEGORY = 'STUDENT_GRADE') ");
			cBaseSql.append(" end) where 1=1 ");
		
		}
		//影响年级
		if(StringUtil.isNotBlank(cla)){
			StringBuffer sql=new StringBuffer();
			Map<String, Object> sqlParams = new HashMap<String, Object>();
			sql.append("update student set GRADE_ID =       ");
			sql.append(baseSql);
			if(type.getValue().equals("GROUNP") && StringUtil.isNotBlank(orgnizationId)){
				sql.append(" and BL_CAMPUS_ID in(select id from organization where parentId in (select id from organization where parentID = :parentID) and orgType = 'CAMPUS') ");
			}else if(!orgnizationId.isEmpty()&&!orgnizationId.equals("")){
				sql.append(" and BL_CAMPUS_ID in(select id from organization where parentID = :parentID and orgType = 'CAMPUS' )");
			}
			sqlParams.put("parentID", orgnizationId);
			if(names.length > 0){
				sql.append(" and ( GRADE_ID = :gradeId0 ");
				sqlParams.put("gradeId0", names[0]);
				for(int i=1; i<names.length; i++){
					sql.append(" or GRADE_ID = :gradeId" + i + " ");
					sqlParams.put("gradeId" + i, names[i]);
				}
				sql.append(" ) ");
			}
			
			super.excuteSql(sql.toString(), sqlParams);
		}
		//选择影响课程
		if(StringUtil.isNotBlank(course)){
			StringBuffer cql=new StringBuffer();
			Map<String, Object> cqlParams = new HashMap<String, Object>();
			cql.append("update course set GRADE = ");
			cql.append(cBaseSql);
			if(type.getValue().equals("GROUNP") && StringUtil.isNotBlank(orgnizationId)){
				cql.append(" and BL_CAMPUS_ID in(select id from organization where parentId in (select id from organization where parentID = :parentId) and orgType = 'CAMPUS') and COURSE_DATE >= :courseDate ");
			}else if(!orgnizationId.isEmpty()&&!orgnizationId.equals("")){
				cql.append(" and BL_CAMPUS_ID in(select id from organization where parentID = :parentId and orgType = 'CAMPUS' ) and COURSE_DATE >= :courseDate ");
			}
			cqlParams.put("parentId", orgnizationId);
			cqlParams.put("courseDate", DateTools.getCurrentDate());
			
			if(names.length > 0){
				cql.append(" and ( GRADE = :gradeId0 ");
				cqlParams.put("gradeId0", names[0]);
				for(int i=1; i<names.length; i++){
					cql.append(" or GRADE = :gradeId" + i + " ");
					cqlParams.put("gradeId" + i, names[i]);
				}
				cql.append(" ) ");
			}
			
			super.excuteSql(cql.toString(), cqlParams);
		}
		
		Map<String, Object> otmCourseParams = new HashMap<String, Object>();
		Map<String, Object> otmParams = new HashMap<String, Object>();
		StringBuffer otmSql=new StringBuffer();
		StringBuffer otmCourseSql = new StringBuffer();
		//一对多逻辑的  清理班级开始日期是当前日期之前的，并且删除班级对应的当前日期以后的课程
		otmCourseSql.append(" delete occ.* from otm_class_course occ ");
		otmCourseSql.append(" left join otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		otmCourseSql.append("  where oc.START_DATE < :startDate and oc.`STATUS`<>'CONPELETE' and occ.course_date>=:courseDate and course_status='NEW' ");
		otmCourseParams.put("startDate", currentDate);
		otmCourseParams.put("courseDate", currentDate);
		
		if(type.getValue().equals("GROUNP") && StringUtil.isNotBlank(orgnizationId)){
			otmCourseSql.append(" and oc.BL_CAMPUS_ID in(select id from organization where parentId in (select id from organization where parentID = :parentId) and orgType = 'CAMPUS') ");
		}else if(!orgnizationId.isEmpty()&&!orgnizationId.equals("")){
			otmCourseSql.append(" and oc.BL_CAMPUS_ID in(select id from organization where parentID = :parentId and orgType = 'CAMPUS' ) ");
		}
		otmCourseParams.put("parentId", orgnizationId);
		super.excuteSql(otmCourseSql.toString(), otmCourseParams);
		
		otmSql.append(" update otm_class set STATUS='CONPELETE' where START_DATE < :startDate and `STATUS`<>'CONPELETE' ");
		otmParams.put("startDate", currentDate);
		if(type.getValue().equals("GROUNP") && StringUtil.isNotBlank(orgnizationId)){
			otmSql.append(" and BL_CAMPUS_ID in(select id from organization where parentId in (select id from organization where parentID = :parentId) and orgType = 'CAMPUS') ");
		}else if(!orgnizationId.isEmpty()&&!orgnizationId.equals("")){
			otmSql.append(" and BL_CAMPUS_ID in(select id from organization where parentID = :parentId and orgType = 'CAMPUS' ) ");
		}
		otmParams.put("parentId", orgnizationId);
		super.excuteSql(otmSql.toString(), otmParams);
		
		
		
	}
	
	/**
	 * 根据学管查找其管理的学生
	 * @return
	 */
	public List<Student> getStudentListByStudyManager(String studyManagerId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select * from student where STUDY_MANEGER_ID = :studyManagerId";
		params.put("studyManagerId", studyManagerId);
		return super.findBySql(sql, params);
	}

    @Override
    public List<Student> findStuByManegerIdAndCampusId(String userId, String changeCampusId) {
		StringBuilder hql = new StringBuilder();
		Map param = new HashMap();
		hql.append(" from Student where studyManeger.userId = :userId and (studentStatus is null or studentStatus != 0) and blCampusId = :campusId ");
		param.put("userId",userId);
		param.put("campusId",changeCampusId);
		return this.findAllByHQL(hql.toString(),param);
    }

}
