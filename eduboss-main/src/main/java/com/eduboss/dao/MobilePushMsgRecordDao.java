package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobilePushMsgSession;





public interface MobilePushMsgRecordDao extends GenericDAO<MobilePushMsgRecord, String> {

	
	/**
	 * 根据 session list 获取到相应的 records
	 * @param sessionList
	 * @return
	 */
	List<MobilePushMsgRecord> getRecordBySessions(
			List<MobilePushMsgSession> sessionList, int pageIndex, int pageCount);

    /**
     * 根据 page index  and page count  session list 获取到相应的 records
     * @param sessionList
     * @param pageIndex
     * @param pageCount
     * @return
     */
    List<MobilePushMsgRecord> getRecordBySessionsForPage(List<MobilePushMsgSession> sessionList, int pageIndex, int pageCount,String lastFetchTime);
    
    /**
     * 根据 page index  and page count  session list 获取到相应的 records
     * @param session
     * @param pageIndex
     * @param pageCount
     * @return
     */
    List<MobilePushMsgRecord> getConversationRecordBySessionsForPage(MobilePushMsgSession session, int pageIndex, int pageCount,String lastFetchTime);

    /**
     * 获取 在 lastFetchTime 之前的 record 信息
     * @param sessionList
     * @param lastFetchTime
     * @return
     */
    List<MobilePushMsgRecord> getLastestRecordBySessions(List<MobilePushMsgSession> sessionList, String lastFetchTime);
    //the common dao method had init in thd GenericDAO, add the special method in this class
	
}
