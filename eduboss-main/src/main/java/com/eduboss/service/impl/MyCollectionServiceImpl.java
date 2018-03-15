package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.MyCollectionDao;
import com.eduboss.dao.ResourceDao;
import com.eduboss.domain.MyCollection;
import com.eduboss.domain.Resource;
import com.eduboss.domain.User;
import com.eduboss.domainVo.MyCollectionVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MyCollectionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

/**@author wmy
 *@date 2015年11月13日下午2:38:09
 *@version 1.0 
 *@description
 */
@Service
public class MyCollectionServiceImpl implements MyCollectionService{

	@Autowired
	private MyCollectionDao myCollectionDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private UserService userService;

	@Override
	public void saveOrUpdateMyCollection(MyCollectionVo myCollectionVo)
			throws ApplicationException {
		if(StringUtils.isBlank(myCollectionVo.getId())) {
			Resource rescoure = resourceDao.findById(myCollectionVo.getResId());
			User user = userService.getCurrentLoginUser();
			MyCollection myCollection = new MyCollection();
			myCollection.setResource(rescoure);
			myCollection.setUser(user);
			myCollectionDao.save(myCollection);
		}
	}

	@Override
	public void deleteById(String id) {
		if(StringUtils.isNotBlank(id)) {
			MyCollection myCollection = myCollectionDao.findById(id);
			if(myCollection != null) {
				myCollectionDao.delete(myCollection);	
			}
		}		
	}

	@Override
	public Map<String, List<MyCollectionVo>> getMyCollectionByUserId(String userId) {
		Map<String, List<MyCollectionVo>> dataItems = new HashMap<String, List<MyCollectionVo>>(); 
		if(StringUtils.isNotBlank(userId)){			
			List<Resource> resList = userService.getUserMenus();			
			Map<String, Object> params = new HashMap<String, Object>();
			StringBuffer hql = new StringBuffer();
			hql.append(" from MyCollection t where 1=1 and t.user.userId = :userId order by t.resource.parentId, t.resource.rorder asc");
			params.put("userId", userId);
			List<MyCollection> myCollectionList = myCollectionDao.findAllByHQL(hql.toString(), params);
			List<MyCollectionVo> allList =  HibernateUtils.voListMapping(myCollectionList, MyCollectionVo.class);
			for(MyCollectionVo vo : allList) {
				String topMenuName = vo.getResName();
				Resource topMenu = getTopMenu(vo.getResParentId(), resList);
				if(topMenu != null) {
					topMenuName = topMenu.getName();
				}
				if(dataItems.containsKey(topMenuName)){
					List<MyCollectionVo> voList = dataItems.get(topMenuName);
					voList.add(vo);
					dataItems.put(topMenuName, voList);
				}else{
					List<MyCollectionVo> newVoList = new ArrayList<MyCollectionVo>();
					newVoList.add(vo);
					dataItems.put(topMenuName, newVoList);
				}				
			}
		}
		return dataItems;
	}
	
	private Resource getTopMenu(String parentId, List<Resource> resList) {	
		Resource top_resource = null;
		while(StringUtils.isNotBlank(parentId) && !"-1".equals(parentId)){
			boolean flag = true;
			for(Resource resource : resList){
				if(parentId.equals(resource.getId())){
					flag = false;
					top_resource = resource;
					parentId = resource.getParentId();
				}
			}
			if(flag == true){
				parentId = "-1";
			}
		}
		return top_resource;
	}

	@Override
	public MyCollection findById(String id) {
		return myCollectionDao.findById(id);
	}

	@Override
	public void deleteByResId(String resId) {
		if(StringUtils.isNotBlank(resId)){
			User user = userService.getCurrentLoginUser();
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = "delete from my_collection where user_id = '" + user.getUserId() + "' and resource_id = :resId ";
			params.put("resId", resId);
			myCollectionDao.excuteSql(sql, params);
		}		
	}

	@Override
	public void deleteNotInNewResources(String userId,
			List<Resource> newResourceList) {
		if(StringUtils.isNotBlank(userId) && newResourceList != null && newResourceList.size() > 0){
			String resIdStr= "";
			for(Resource res : newResourceList) {
				resIdStr+=","+res.getId()+"";
			}
			if (resIdStr.length() > 0) {
				String[] resIdArr = resIdStr.substring(1).split(",");
				Map<String, Object> params = new HashMap<String, Object>();
				String sql = "delete from my_collection where user_id = :userId and resource_id not in (:resIds)";
				params.put("userId", userId);
				params.put("resIds", resIdArr);
				myCollectionDao.excuteSql(sql, params);
			}
		}
	}
	
}


