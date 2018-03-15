package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.MyCollection;
import com.eduboss.domain.Resource;
import com.eduboss.domainVo.MyCollectionVo;
import com.eduboss.exception.ApplicationException;

/**@author wmy
 *@date 2015年11月13日下午2:37:03
 *@version 1.0 
 *@description
 */
public interface MyCollectionService {

	public MyCollection findById(String id);
	
	public void saveOrUpdateMyCollection(MyCollectionVo myCollectionVo) throws ApplicationException;
	
	public Map<String, List<MyCollectionVo>> getMyCollectionByUserId(String userId);

	public void deleteById(String id);

	public void deleteByResId(String resId);
	
	public void deleteNotInNewResources(String userId, List<Resource> newResourceList);
}


