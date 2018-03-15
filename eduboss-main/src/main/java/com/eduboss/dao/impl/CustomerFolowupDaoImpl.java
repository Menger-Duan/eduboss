package com.eduboss.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerFolowupDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.CustomerFolowup;
import com.eduboss.service.UserService;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("CustomerFolowupDao")
public class CustomerFolowupDaoImpl extends GenericDaoImpl<CustomerFolowup, String> implements CustomerFolowupDao {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	//*******************************前台来访客户管理 start*****************************************************//
	
		/**
		 * @author tangyuping
		 * 前台客户登记记录,获取最近30天的客户登记记录
		 *//*
		@Override
		public DataPackage findPageCustomerForReceptionistWb(ReceptionistCustomerVo vo, DataPackage dp) {
			StringBuilder hql=new StringBuilder();	
			StringBuilder hqlWhere=new StringBuilder();
			
			hql.append("select DISTINCT c  from CustomerFolowup cf, Customer c");
			
			hqlWhere.append(" where cf.customer.id = c.id and cf.appointmentType = 'APPOINTMENT' and cf.meetingConfirmTime is not null ");
			hqlWhere.append(" and c.dealStatus <> 'INVALID' ");//去除无效客户
			if(StringUtils.isNotBlank(vo.getStartDate())){
				hqlWhere.append(" and cf.meetingConfirmTime >= '"+vo.getStartDate()+" 0000' ");
			}
			if(StringUtils.isNotBlank(vo.getEndDate())){
				hqlWhere.append(" and cf.meetingConfirmTime <= '"+vo.getEndDate()+" 2359' ");		
			}
			if(StringUtils.isNotBlank(vo.getRecordUserName())){
				hqlWhere.append(" and c.recordUserId in (select userId User where name like '%"+vo.getRecordUserName()+"%' ) ");
			}
			if(StringUtils.isNotBlank(vo.getName())){
				hqlWhere.append(" and c.name like '%"+vo.getName()+"%' ");
			}
			if(StringUtils.isNotBlank(vo.getContact())){
				hqlWhere.append(" and c.contact like '%"+vo.getContact()+"%' ");
				
			}
			if(StringUtils.isNotBlank(vo.getResEntranceId())){
				hqlWhere.append(" and c.resEntrance = '"+vo.getResEntranceId()+"' ");
			}
			
			if(StringUtils.isNotBlank(vo.getCusType())){
				hqlWhere.append(" and c.cusType ='"+vo.getCusType()+"' ");
			}
			
			if(StringUtils.isNotBlank(vo.getCusOrg())){
				hqlWhere.append(" and c.cusOrg = '"+vo.getCusOrg()+"' ");
			}
			if(StringUtils.isNotBlank(vo.getDealStatus())){
				hqlWhere.append(" and c.dealStatus = '"+vo.getDealStatus()+"' ");
			}
			//获取最近30天登记记录，		
			if(StringUtils.isBlank(vo.getStartDate()) && StringUtils.isBlank(vo.getEndDate())){
				String end=DateTools.getCurrentDate();
				String start=DateTools.addDateToString(end,-30);//得到当前日期减30
				hqlWhere.append(" and cf.meetingConfirmTime >= '"+start+" 0000' and cf.meetingConfirmTime <= '"+end+" 2359' ");
			}
			Organization currentBlCampus = userService.getBelongCampus();
			//前台或校区主任可以看到自己校区的客户 或者从本校区转出但是还没有接收的数据
			if (userService.isCurrentUserRoleCode(RoleCode.RECEPTIONIST)
					|| userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)) {
				hqlWhere.append(" and ( c.blSchool = '"+currentBlCampus.getId()+"' ");
			}else{
				//其他人员只能看到自己根据的客户
				hqlWhere.append(" and ( c.deliverTarget = '"+userService.getCurrentLoginUser().getUserId()+"' ");
			}
			hqlWhere.append(" or (  c.transferFrom ='"+currentBlCampus.getId()+"' and c.transferStatus = '0' ) )");
			
			//同一个客户在30天内多次登记只取最近一次登记记录即可
			hqlWhere.append(" ORDER BY cf.meetingConfirmTime DESC ");
			
				
			dp = super.findPageByHQL(hql.append(hqlWhere).toString(), dp, true);	
			dp.setRowCount(findCountHql("select count(DISTINCT c.id) " + hql.substring(hql.indexOf("from"))));
			List<Customer> list = (List<Customer>) dp.getDatas();
			List<ReceptionistCustomerVo> voList=new ArrayList<ReceptionistCustomerVo>();
			if(list != null && list.size()>0){
				for(Customer customer : list){
					if(customer != null){
						StringBuilder sql = new StringBuilder();		
						sql.append(" select * from customer_folowup where CUSTOMER_ID='"+customer.getId()+"' AND MEETING_CONFIRM_TIME IS NOT NULL AND  APPOINTMENT_TYPE = 'APPOINTMENT' ORDER BY MEETING_CONFIRM_TIME DESC ");						
						CustomerFolowup follow=(CustomerFolowup)this.findBySql(sql.toString()).get(0);
						
						ReceptionistCustomerVo rvo = new ReceptionistCustomerVo();
						rvo=HibernateUtils.voObjectMapping(customer, ReceptionistCustomerVo.class);
						if(follow.getMeetingConfirmUser() != null){
							rvo.setMeetingConfirmUserId(follow.getMeetingConfirmUser().getUserId());
							rvo.setMeetingConfirmUserName(follow.getMeetingConfirmUser().getName());
							Organization blCampus = userService.getBelongCampusByUserId(follow.getMeetingConfirmUser().getUserId());
							rvo.setMeetingConfirmBlCampus(blCampus == null ? "" : blCampus.getId()); //登记人校区
						}
						UserDeptJob userDeptJob=userDeptJobDao.findDeptJobByParam(rvo.getRecordUserId(), 0);
						if(userDeptJob != null ){
							rvo.setRecordUserDept(userDeptJob.getId().getDeptId());
							Organization dept = organizationDao.findById(userDeptJob.getId().getDeptId());
							rvo.setRecordUserDeptName(dept.getName());
						}
						if(StringUtils.isNotEmpty(rvo.getDeliverTarget())){
							if(CustomerDeliverType.PERSONAL_POOL.equals(rvo.getDeliverType())){
								rvo.setDeliverTargetName(userService.getUserById(rvo.getDeliverTarget()).getName());
							}else if(CustomerDeliverType.CUSTOMER_RESOURCE_POOL.equals(rvo.getDeliverType()) ){
								rvo.setDeliverTargetName(organizationDao.findById(rvo.getDeliverTarget()).getCustomerPoolName());
							}
						}
						rvo.setMeetingConfirmTime(follow.getMeetingConfirmTime());					
						voList.add(rvo);					
					}
				}
			}
			
			dp.setDatas(voList);
			return dp;
		}*/
		
		//*******************************前台来访客户管理 end*****************************************************//
		
}
