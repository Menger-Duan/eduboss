package com.eduboss.utils;

/**
 * 各个功能列表多角色查询权限控制
 * @author ndd
 *
 */
public class RoleCodeAuthoritySearchUtil {
	
//	/**
//	 * 批量修改课程课程表权限查询
//	 * @param user 查询用户对象（user的Role必须要设值）
//	 * @param campus 归属组织架构(校区)
//	 * @return hql 条件
//	 */
//	public static String getCourseRoleCodeAuthority(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.TEATCHER.equals(role.getRoleCode())){//老师只能看到自己的
//				hqlWhere.append(" or teacher.userId='"+user.getUserId()+"'");
//			}else if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){//学管师只能看到自己的
//				hqlWhere.append(" or studyManager.userId='"+user.getUserId()+"'");
//			}else if(!isAddOrg){
//				isAddOrg=true;
//				String hql=getOrganizationHql(campus, "student.blCampusId");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationHql(campus, "student.blCampusId");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
//	/**
//	 * 走组织架构查询条件
//	 * @param campus
//	 * @param columnName
//	 * @return
//	 */
//	public static String getOrganizationHql(Organization campus,String columnName){
//		if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
//			String orgLevel = campus.getOrgLevel();
//			return " "+columnName+" in (select id from Organization where orgLevel like '"+orgLevel+"%') ";
//		}
//		return "";
//	}
	
//	/**
//	 * 走组织架构查询条件
//	 * @param campus
//	 * @param columnName
//	 * @return
//	 */
//	public static String getOrganizationSQL(Organization campus,String columnName){
//		if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
//			String orgLevel = campus.getOrgLevel();
//			return " "+columnName+" in (select id from organization where orgLevel like '"+orgLevel+"%') ";
//		}
//		return "";
//	}
	
//	/**
//	 * 走组织架构查询条件
//	 * @param campus
//	 * @param columnName 归属组织架构是对象（columnName 是对象）
//	 * @return
//	 */
//	public static Criterion getOrganizationCriterionByOrgObject(Organization organization,String columnName){
//		if (organization != null && StringUtils.isNotEmpty( organization.getOrgLevel())) {
//			String orgLevel = organization.getOrgLevel();
//			return Expression.like(columnName+".orgLevel",orgLevel,MatchMode.START);
//		}
//		return null;
//	}
	
	
//	/**
//	 * 学生表多角色权限查询
//	 * @param user 查询用户对象（user的Role必须要设值）
//	 * @param campus 归属组织架构(校区)
//	 * @return hql 条件
//	 */
//	public static String getStudentRoleCodeAuthority(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){//学管-学生属性中的学管是自己的
//				hqlWhere.append( " or s.studyManegerId='"+user.getUserId()+"'");
////			} else if(RoleCode.CONSULTOR.equals(role.getRoleCode())
////					||RoleCode.CONSULTOR_DIRECTOR.equals(role.getRoleCode())){//咨询师或咨询主管-自己签合同的学生
////				hqlWhere.append( " or s.id in (select student.id from Contract where signStaff.userId='"+user.getUserId()+"' ) ");
//			} else if(RoleCode.TEATCHER.equals(role.getRoleCode())){//老师-有课程关系的学生
//				hqlWhere.append( " or s.id in (select student.id from Course where teacher.userId='"+user.getUserId()+"' )");
//			} else if(!isAddOrg){
//				isAddOrg=true;
//				String hql=getOrganizationHql(campus, "s.blCampusId");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationHql(campus, "s.blCampusId");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
//	/**
//	 * 小班课程列表
//	 * @param user 查询用户对象（user的Role必须要设值）
//	 * @param campus 归属组织架构(校区)
//	 * @return hql 条件
//	 */
//	public static String getMiniClassCourseRoleCodeAuthority(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){//学管-学生属性中的学管是自己的
//				hqlWhere.append( " or  miniClass.studyManeger.userId='"+user.getUserId()+"'");
//			} else if(RoleCode.TEATCHER.equals(role.getRoleCode())){//老师-有课程关系的学生
//				hqlWhere.append( " or  miniClass.teacher.userId='"+user.getUserId()+"'");
//				isAddOrg=true;
//				String hql=getOrganizationHql(campus, "miniClass.blCampus.id");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationHql(campus, "miniClass.blCampus.id");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
	
//	/**
//	 * 校区课表权限控制
//	 * @param user 查询用户对象（user的Role必须要设值）
//	 * @param campus 归属组织架构(校区)
//	 * @return hql 条件
//	 */
//	public static String getCourseSQLRoleCodeAuthoritySQL(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){//学管-学生属性中的学管是自己的
//				hqlWhere.append( " or s.USER_ID='"+user.getUserId()+"'");
//			}else if(RoleCode.TEATCHER.equals(role.getRoleCode())){//老师-有课程关系的学生
//				hqlWhere.append( " or a.TEACHER_ID='"+user.getUserId()+"'");
//			} else if(!isAddOrg){
//				isAddOrg=true;
//				String hql=getOrganizationSQL(campus, "e.BL_CAMPUS_ID");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationSQL(campus, "e.BL_CAMPUS_ID");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
	
//	public static String findPageContractRoleCodeAuthority(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){// 学管 只看到自己的
//				hqlWhere.append(" or student.studyManegerId = '").append(user.getUserId()).append("' ");
//				hqlWhere.append(" or  signStaff.id = '").append(user.getUserId()).append("' ");
//			} else if(RoleCode.CONSULTOR.equals(role.getRoleCode())){//咨询师
//				hqlWhere.append(" or  signStaff.id = '").append(user.getUserId()).append("' ");
//			}else if(!isAddOrg){
//				isAddOrg=true;
//				String hql=getOrganizationHql(campus, "student.blCampusId");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationHql(campus, "student.blCampusId");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
//	/**
//	 * 查看扣费
//	 * @param user
//	 * @param campus
//	 * @return
//	 */
//	public static String findPageChargeRecordRoleCodeAuthority(User user,Organization campus){
//		List<Role> userRoles = user.getRole();
//		StringBuffer hqlWhere=new StringBuffer("");
//		boolean isAddOrg=false;//是否已加组织架构查询条件
//		for (Role role : userRoles) {
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){// 学管 只看到自己的
//				hqlWhere.append(" or student.studyManegerId = '").append(user.getUserId()).append("' ");
//				hqlWhere.append(" or  contract.signStaff.id = '").append(user.getUserId()).append("' ");
//			}else if(RoleCode.CONSULTOR.equals(role.getRoleCode())){//咨询师
//				hqlWhere.append(" or  contract.signStaff.id = '").append(user.getUserId()).append("' ");
//			} else if(!isAddOrg){
//				isAddOrg=true;
//				String hql=getOrganizationHql(campus, "contract.student.blCampusId");
//				if(StringUtils.isNotEmpty(hql))
//					hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql=getOrganizationHql(campus, "contract.student.blCampusId");
//			if(StringUtils.isNotEmpty(hql))
//				hqlWhere.append(" or "+hql);
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
	
//	/**
//	 * 查看收费
//	 * @param user
//	 * @param campus
//	 * @return
//	 */
//	public static String findPageFundsChangeRoleCodeAuthority(User user,Organization campus){
//		List<Role> roles = user.getRole();
//		StringBuffer hqlWhere = new StringBuffer("");
//		boolean isAddOrg=false; //
//		for(Role role:roles){
//			if(RoleCode.STUDY_MANAGER.equals(role.getRoleCode())){//学管只看到自己的
//				hqlWhere.append(" or contract.student.studyManegerId = '").append(user.getUserId()).append("' ");
//				hqlWhere.append(" or contract.signStaff.id = '").append(user.getUserId()).append("' ");
//			}else if(RoleCode.CONSULTOR.equals(role.getRoleCode())){
//				hqlWhere.append(" or contract.signStaff.id ='").append(user.getUserId()).append("' ");
//			}else if(!isAddOrg){
//				isAddOrg=true;
//				String hql =getOrganizationHql(campus,"contract.student.blCampusId");
//				if(StringUtils.isNotEmpty(hql)){
//					hqlWhere.append(" or "+hql);
//				}
//			}
//		}
//		if(hqlWhere.length()==0 && !isAddOrg){
//			String hql =getOrganizationHql(campus,"contract.student.blCampusId");
//			if(StringUtils.isNotEmpty(hql)){
//				hqlWhere.append(" or "+hql);
//			}
//		}
//		if(hqlWhere.length()>0){
//			return " and ("+hqlWhere.substring(3)+") ";
//		}
//		return "";
//	}
}
