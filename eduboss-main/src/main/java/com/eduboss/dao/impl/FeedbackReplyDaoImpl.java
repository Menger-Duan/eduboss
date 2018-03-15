package com.eduboss.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.FeedBackDao;
import com.eduboss.dao.FeedbackReplyDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.eduboss.domain.FeedBack;
import com.eduboss.domain.FeedbackReply;
import com.eduboss.domain.Organization;
import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.domainVo.SystemNoticeReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class FeedbackReplyDaoImpl extends GenericDaoImpl<FeedbackReply, String> implements FeedbackReplyDao {
	
	@Autowired
	private FeedBackDao feedBackDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	/**
	 * 查看反馈回复
	 */
	

	@Override
	public DataPackage getReplyByFid(DataPackage dataPackage,String fid){
		Map<String, Object> params = Maps.newHashMap();
		params.put("feedbackId", fid);
		StringBuffer hql=new StringBuffer();
		hql.append(" from FeedbackReply where feedBack.id= :feedbackId ");
		hql.append(" order by createTime desc ");
		dataPackage=this.findPageByHQL(hql.toString(),dataPackage,false,params);
		
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(1) from feedback_reply where feedback_id = :feedbackId ");
		int rowCount=this.findCountSql(countSql.toString(),params);
		
		
		
		List<FeedbackReply> list=(List<FeedbackReply>)dataPackage.getDatas();
		List<FeedbackReplyVo> volist=new ArrayList<FeedbackReplyVo>();
		for(FeedbackReply reply:list){
			FeedbackReplyVo vo=HibernateUtils.voObjectMapping(reply, FeedbackReplyVo.class);
			Organization org=organizationDao.findById(vo.getUserOrgId());
			vo.setUserOrgName(org.getName());
			volist.add(vo);
		}
		dataPackage.setDatas(volist);
		dataPackage.setRowCount(rowCount);
		return dataPackage;
	}
	
	/**
	 * 删除反馈回复
	 */
	@Override
	public void deleteFeedbackReply(FeedbackReply feedbackReply){
		Map<String, Object> params = Maps.newHashMap();
		params.put("feedbackId", feedbackReply.getId());
		StringBuffer hql=new StringBuffer();
		hql.append("delete from FeedbackReply where id = :feedbackId ");
		this.excuteHql(hql.toString(),params);
	}
	
	/**
	 * 添加回复,修改反馈回复状态
	 */
	@Override
	public void addFeedbackReply (FeedbackReply feedbackreply){
		feedbackreply.setUser(userService.getCurrentLoginUser());
		feedbackreply.setCreateTime(DateTools.getCurrentDateTime());
		this.save(feedbackreply);
		
		FeedBackVo feedbackVo=HibernateUtils.voObjectMapping(feedbackreply.getFeedBack(), FeedBackVo.class);
		FeedBack feedback=feedBackDao.findById(feedbackVo.getId());
		feedback.setReplyTime(DateTools.getCurrentDateTime());
		feedback.setIsBack("1");		
		feedBackDao.save(feedback);
		
	}
	
}
