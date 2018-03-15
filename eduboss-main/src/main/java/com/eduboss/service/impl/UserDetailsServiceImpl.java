/**
 * 
 */
package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.eduboss.common.Constants;
import com.eduboss.common.StateOfEmergency;
import com.eduboss.domain.*;
import com.eduboss.utils.JedisUtil;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.eduboss.common.RoleCode;
import com.eduboss.domainVo.ResourceGrantedVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.TokenUtil;

/**
 * @author lmj
 */

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		
		User user = userService.getUserByEmployeeNo(username);
		if (user == null) return null;
		
		List<Role> roles = userService.getRoleByUserId(user.getUserId());
		user.setRole(roles);

		//判断用户是否是紧急状态用户
		List<Organization> orgs = userService.getUserOrganizationList(user.getUserId());//旧数据用这个findOrganizationByUserId
		Boolean stateOfEmergencyFlag = userService.isUserStateOfEmergency(orgs);


		user.setOrganization(orgs);
//        userDao.getHibernateTemplate().initialize(user.getOrganization());
		
		List<ResourceGrantedVo> resources = new ArrayList<ResourceGrantedVo> ();
		Short roleLevel=0;
		String roleCodes="";
//		ArrayList<String> roleIds = new ArrayList<String>();
				
		Boolean flag = false;
		Boolean hasReceptionist = false;
		Role receptionistRole = null;
		List<Role> roleList = new ArrayList<>();
				
		for (Role role : roles) {
//			roleIds.add(role.getRoleId());
//			resources.addAll(userService.getResourcesByRoleId(role.getRoleId()));  //不用循环查，太慢
			//硬编码 判断是否有前台权限
			if(!flag){
				hasReceptionist = hasReceptionist(role);
				if(hasReceptionist){
					flag = true;
					receptionistRole = role;
					roleList.add(receptionistRole);
				}
			}
			if(role.getRoleId().equals("ROL0000000146")){
				roleList.add(role);
			}
						
			if (role.getRoleCode() != null) {
				if(roleCodes.equals("")){
					roleCodes+=role.getRoleCode().getValue();
				}else{
					roleCodes+=","+role.getRoleCode().getValue();
				}
			}
			if (roleLevel == 0) {
				roleLevel = role.getRoleLevel();
			}else if(role.getRoleLevel()<roleLevel){
				roleLevel=role.getRoleLevel();
			}
		}
		//hasReceptionist =false;
		if(hasReceptionist && roles.size()>2){ //基本权限角色 前台角色 +其他角色
			//具有可切换模式下的多个模式 默认普通模式
			//roles.remove(receptionistRole);
			//如果没法刷新Authentication 则注释下面这行代码 SpringSecurity还是具有全部权限 但是页面不显示 通过 修改roleList 来控制菜单
			if (stateOfEmergencyFlag){
				resources = emergencyRoleResources(roles, resources);
			}else {
				resources.addAll(HibernateUtils.voListMapping(userService.getResourcesByUserIdRoleId(user.getUserId(),"",roles), ResourceGrantedVo.class));
			}

			user.setRoleCode(roleCodes);
			user.setRoleLevel(roleLevel);
			user.setRole(roles);
		}else{
			if (stateOfEmergencyFlag){
				resources = emergencyRoleResources(roles, resources);
			}else {
				resources.addAll(HibernateUtils.voListMapping(userService.getResourcesByUserId(user.getUserId(),""), ResourceGrantedVo.class));//一条语句返回查询所有权限信息
			}
			user.setRoleCode(roleCodes);
			user.setRoleLevel(roleLevel);
			user.setRole(roles);
		}
		
		resources.addAll(HibernateUtils.voListMapping(userService.getAllAnonymouslyResourceList(), ResourceGrantedVo.class));
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.addAll(resources);
		user.setAuthorities(authorities);
		user.setToken(TokenUtil.genToken(user));
		userService.updateUserToken(user);

		JedisUtil.set(Constants.STATUS_EMERGENCY_+user.getUserId(), user.getUserId());

		return new UserDetailsImpl(user);
	}



	private List<ResourceGrantedVo> emergencyRoleResources(List<Role> roles, List<ResourceGrantedVo> resources) {
		Boolean specialRole = false;
		List<String> specialRoleList = new ArrayList<>();
		for (Role role : roles){
//			if (RoleCode.SPECIALROLE==role.getRoleCode()){
//				specialRole = true;
//				specialRoleList.add(role.getRoleId());
//			}
			//需求说写死的
			if (role.getRoleCode()==RoleCode.STUDY_MANAGER){//学管师
				specialRole = true;
				specialRoleList.add("specialRole");//校区管理学管
			}
			if (role.getRoleCode() == RoleCode.CAMPUS_DIRECTOR){//校区主任
				specialRole = true;
				specialRoleList.add("specialRole1");//校区管理主任
			}
			if (role.getRoleCode() == RoleCode.BREND_MENAGER){//分公司总经理
				specialRole = true;
				specialRoleList.add("specialRole2");//分公司管理经理
			}
		}
		List<Resource> resourceList= new ArrayList<>();
		if (specialRole){
//			String[] strings = new String[specialRoleList.size()];
//			String[] ids =(String[])specialRoleList.toArray(strings);
			for (String id :specialRoleList){
				List<Resource> resourcesByRoleId = userService.getResourcesByRoleId(id);
				resourceList.addAll(resourcesByRoleId);
			}
		}else {
			resourceList = userService.getResourcesByRoleId("ROL0000000146");
		}
		resources.addAll(HibernateUtils.voListMapping(resourceList, ResourceGrantedVo.class));
		return resources;
	}

	private Boolean hasReceptionist(Role role){
		if(role.getRoleCode()!=null && role.getRoleCode()==RoleCode.RECEPTIONIST){
			return true;
		}else{
			return false;
		}
	}
}
