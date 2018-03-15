package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.DistributableRoleDao;
import com.eduboss.domain.DistributableRole;
import com.eduboss.domain.RoleResource;
import com.eduboss.service.DistributableRoleService;
import com.eduboss.service.UserService;


@Service("DistributableRoleService")
public class DistributableRoleServiceImpl implements DistributableRoleService{

	@Autowired
	private DistributableRoleDao distributableRoleDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public List<DistributableRole> findDistributableRolesByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return distributableRoleDao.findDistributableRolesByRoleId(roleId);
	}

	@Override
	public void updateDistributableRoleList(String roleId, List<String> list) {
		// TODO Auto-generated method stub
		List<DistributableRole> roles = distributableRoleDao.findDistributableRolesByRoleId(roleId);
		if(list!=null && list.size() > 0) {
			for (DistributableRole dr : roles) {
				if (list.contains(dr.getRelateRoleId())) {
					list.remove(dr.getRelateRoleId());
				} else {
					distributableRoleDao.delete(dr);//delete if not in the selectResIds
				}
			}
			if (list.size() > 0) {
				for (String relateId : list) {
					DistributableRole dr=new DistributableRole();
					dr.setRelateRoleId(relateId);
					dr.setRoleId(roleId);
					dr.setCreateUserId(userService.getCurrentLoginUser().getUserId());
					dr.setModifyUserId(userService.getCurrentLoginUser().getUserId());
					distributableRoleDao.save(dr);
				}
			}
		}else {
			distributableRoleDao.deleteAll(roles);
		}
		
	}
}
