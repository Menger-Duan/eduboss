package com.eduboss.dao;

import com.eduboss.domain.AppUser;
import org.springframework.stereotype.Repository;


/**
 * @classname	AppUserDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2013-5-25 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface AppUserDao extends GenericDAO<AppUser, String> {

}
