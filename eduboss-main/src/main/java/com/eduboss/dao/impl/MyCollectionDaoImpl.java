package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MyCollectionDao;
import com.eduboss.domain.MyCollection;

/**@author wmy
 *@date 2015年11月13日下午2:34:07
 *@version 1.0 
 *@description
 */
@Repository("MyCollectionDao")
public class MyCollectionDaoImpl extends GenericDaoImpl<MyCollection, String> implements  MyCollectionDao {

}


