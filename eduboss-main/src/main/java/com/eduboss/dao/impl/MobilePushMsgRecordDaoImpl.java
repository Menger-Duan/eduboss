package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.common.SessionType;
import com.eduboss.dao.MobilePushMsgRecordDao;
import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;
import com.eduboss.dto.DataPackage;




@Repository("MobilePushMsgRecordDao")
public class MobilePushMsgRecordDaoImpl extends GenericDaoImpl<MobilePushMsgRecord, String> implements MobilePushMsgRecordDao {

	private static final Logger log = LoggerFactory.getLogger(MobilePushMsgRecordDaoImpl.class);

	@Override
	public List<MobilePushMsgRecord> getRecordBySessions(List<MobilePushMsgSession> sessionList,int pageNo, int pageSize) {
		if(sessionList.size() > 0) {
			StringBuffer hql = new StringBuffer();
			Map<String, Object> params = Maps.newHashMap();
			if(sessionList.size()>1){
				hql.append(
						"from MobilePushMsgRecord as record where record.session.id in ( :ids ) order by createTime desc");
			}else{
				hql.append(
						"from MobilePushMsgRecord as record where record.session.id = :ids  order by createTime desc");
			}

            List<String> ids = new ArrayList<String>(); 
			for(MobilePushMsgSession session : sessionList) {
				ids.add(session.getId());
			}
            params.put("ids", ids);
            DataPackage dataPackage = new DataPackage();
            dataPackage.setPageNo(pageNo);
            dataPackage.setPageSize(pageSize);
			
			return (List<MobilePushMsgRecord>)super.findPageByHQL(hql.toString(), dataPackage,true,params).getDatas();
		} else {
			return new ArrayList<MobilePushMsgRecord>();
		}
	}

	@Override
    public List<MobilePushMsgRecord> getRecordBySessionsForPage(List<MobilePushMsgSession> sessionList, int pageNo, int pageSize,String lastFetchTime) {
        if(sessionList.size() > 0) {
            StringBuffer hql = new StringBuffer();
			Map<String, Object> params = Maps.newHashMap();
            if(sessionList.size()>1){
            	hql.append("from MobilePushMsgRecord as record where record.session.id in ( :ids )");
            }else{
            	hql.append("from MobilePushMsgRecord as record where record.session.id in = :ids ");
            }
            
            List<String> ids = new ArrayList<String>(); 
			for(MobilePushMsgSession session : sessionList) {
				ids.add(session.getId());
			}
            params.put("ids", ids);
            DataPackage dataPackage = new DataPackage();
            dataPackage.setPageNo(pageNo);
            dataPackage.setPageSize(pageSize);
            //刷新看下有没有新增记录
            if(StringUtils.isNotBlank(lastFetchTime) && pageNo==0){
         	    hql.append("  and createTime> :lastFetchTime ");
         	    params.put("lastFetchTime", lastFetchTime);
            }    
            hql.append(" order by createTime desc ");
            
            return (List<MobilePushMsgRecord>)super.findPageByHQL(hql.toString(), dataPackage,true,params).getDatas();
        } else {
            return new ArrayList<MobilePushMsgRecord>();
        }
    }
    
    @Override
    public List<MobilePushMsgRecord> getConversationRecordBySessionsForPage(MobilePushMsgSession session, int pageNo, int pageSize,String lastFetchTime) {
        if(session !=null) {
        	Set<MobileUser> mobileUsers=session.getMobileUsers();
        	if(mobileUsers!=null){
        		 StringBuffer hql = new StringBuffer();
        		 Map<String, Object> params = Maps.newHashMap();
        		 if(mobileUsers.size()>1){
        			 hql.append("from MobilePushMsgRecord as record where record.sender.id in ( :ids )");
        		 }else{
        			 hql.append("from MobilePushMsgRecord as record where record.sender.id in = :ids ");
        		 }
                 
        		 List<String> ids = new ArrayList<String>(); 
                 for(MobileUser user : mobileUsers) {
                	 ids.add(user.getId());
                 }
 
                 hql.append(" and record.session.id= :sessionId ");
                 params.put("ids", ids);
                 params.put("sessionId", session.getId());
                 //刷新看下有没有新增记录
                 if(StringUtils.isNotBlank(lastFetchTime) && pageNo==0){
                	   hql.append("  and createTime> :lastFetchTime "); 
                	   params.put("lastFetchTime", lastFetchTime);
                 }                           
                 hql.append(" order by createTime desc");  
                 DataPackage dataPackage = new DataPackage();
                 dataPackage.setPageNo(pageNo);
                 dataPackage.setPageSize(pageSize);
                 return (List<MobilePushMsgRecord>)super.findPageByHQL(hql.toString(), dataPackage,true,params).getDatas();
        	}else {
                return new ArrayList<MobilePushMsgRecord>();
            }
           
        } else {
            return new ArrayList<MobilePushMsgRecord>();
        }
    }

    @Override
    public List<MobilePushMsgRecord> getLastestRecordBySessions(List<MobilePushMsgSession> sessionList, String lastFetchTime) {
        if(sessionList.size() > 0) {
            StringBuffer hql = new StringBuffer();
            Map<String, Object> params = Maps.newHashMap();
            if(sessionList.size()>1){
            	 hql.append(
                         "from MobilePushMsgRecord as record where record.session.id in ( :ids ) ");
            }else{
            	 hql.append(
                         "from MobilePushMsgRecord as record where record.session.id in = :ids ");
            }
           
            List<String> ids = new ArrayList<String>(); 
    		for(MobilePushMsgSession session : sessionList) {
    			ids.add(session.getId());
    		}
            params.put("ids", ids);


            if(StringUtil.isNotEmpty(lastFetchTime)){
                hql.append(" and  createTime > :lastFetchTime ");
                params.put("lastFetchTime", lastFetchTime);
            }
            hql.append( " order by createTime desc");
            return super.findAllByHQL(hql.toString(),params);
        } else {
            return new ArrayList<MobilePushMsgRecord>();
        }
    }

}
