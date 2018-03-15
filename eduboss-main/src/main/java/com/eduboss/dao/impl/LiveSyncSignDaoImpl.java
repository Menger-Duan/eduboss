package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.LiveSyncSignDao;
import com.eduboss.domain.LiveSyncSign;

@Repository("LiveSyncSignDaoImpl")
public class LiveSyncSignDaoImpl extends GenericDaoImpl<LiveSyncSign,String> implements LiveSyncSignDao {


}
