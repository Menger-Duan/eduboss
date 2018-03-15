package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.SystemNoticeReplyDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domainVo.SystemNoticeReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Repository
public class SystemNoticeReplyDaoImpl extends GenericDaoImpl<SystemNoticeReply,String> implements SystemNoticeReplyDao{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Override
	@SuppressWarnings("unchecked")
	public DataPackage getSystemNoticeReplyList(
			SystemNoticeReplyVo systemNoticeReplyVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql=new StringBuilder("from SystemNoticeReply where systemNotice.id= :systemNoticeId order by createTime desc");
		params.put("systemNoticeId", systemNoticeReplyVo.getSystemNoticeId());
		dp = super.findPageByHQL(hql.toString(), dp, true, params);
		List<SystemNoticeReply> list=(List<SystemNoticeReply>)dp.getDatas();
		
		List<SystemNoticeReplyVo> voList = new ArrayList<SystemNoticeReplyVo>();
		for(SystemNoticeReply reply : list){
			SystemNoticeReplyVo vo = HibernateUtils.voObjectMapping(reply, SystemNoticeReplyVo.class);
			Organization organization = organizationDao.findById(vo.getUserOrgId());
			vo.setUserOrg(organization.getName());
			voList.add(vo);
		}
		dp.setDatas(voList);
		return dp;
	}

	@Override
	public void deleteSystemNoticeReply(SystemNoticeReply reply) {
		Session session =null;
        
        try
        {
           session= this.getHibernateTemplate().getSessionFactory().getCurrentSession();
           session.beginTransaction();
           session.delete(reply);
           session.getTransaction().commit();
           
        }catch(Exception e){
           e.printStackTrace();
           session.getTransaction().rollback();
        }finally{
        	this.getHibernateTemplate().getSessionFactory().close();
        }
	}

	

}
