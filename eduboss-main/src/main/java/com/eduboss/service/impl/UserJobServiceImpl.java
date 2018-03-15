package com.eduboss.service.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.domainVo.DistributableUserJobVo;
import com.eduboss.domainVo.UserJobVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.DistributableUserJobService;
import com.eduboss.service.UserJobService;
import com.eduboss.utils.HibernateUtils;

@Service
public class UserJobServiceImpl implements UserJobService {

    @Autowired
    private UserJobDao userJobDao;
    
    @Autowired
    private UserDeptJobDao userDeptJobDao;
    
	@Autowired
	private JdbcTemplateDao jdbcTemplateDao;
	
	@Autowired
	private DistributableUserJobService distributableUserJobService;

	/**
	 *
	 * @param dataPackage
	 * @param UserJob
	 * @return
	 */
    @Override
    @Transactional(readOnly = true)
    public DataPackage findPageUserJob(DataPackage dataPackage, UserJob UserJob) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        List<Order> orders = new ArrayList<Order>();
        
        if(StringUtils.isNotBlank(UserJob.getJobName())){
            criterions.add(Restrictions.like("jobName", UserJob.getJobName(), MatchMode.ANYWHERE));
        }
        
        if(StringUtils.isNotBlank(UserJob.getRemark())){
            criterions.add(Restrictions.like("remark", UserJob.getRemark(), MatchMode.ANYWHERE));
        }
        
        if(UserJob.getJobLevel()!=null && !UserJob.getJobLevel().equals("")){
        	criterions.add(Restrictions.eq("jobLevel", UserJob.getJobLevel()));
        }
        
        orders.add(Order.desc("id"));
        DataPackage dp = userJobDao.findPageByCriteria(dataPackage,orders,criterions);

        List<UserJob> UserJobs = HibernateUtils.voListMapping((List) dp.getDatas(), UserJob.class);
		Map<String, Object> params = new HashMap();
		for (UserJob userJob : UserJobs) {
			userJob.setUserCount(userDeptJobDao.findCountHql(" select count(*) from UserDeptJob where id.jobId='"+userJob.getId()+"'", params));
			userJob.setRealCount(userDeptJobDao.findCountSql("select count(*) from user u ,USER_DEPT_JOB ur where u.USER_ID=ur.user_ID and ur.JOB_ID='"+userJob.getId()+"' and u.enable_flag='0'", params));
        }

        dp.setDatas(UserJobs);
        return dp;
    }


    /**
     *
     * @param id
     */
    public Response deleteUserJob(String id) {
        UserJob UserJob = userJobDao.findById(id);
		List<UserDeptJob> userRoles = userDeptJobDao.findByCriteria(Expression.eq("id.jobId", id));
		
		if(userRoles.size()>0){
			return new Response(1, "该职位有关联员工不能删除！");
//			throw new ApplicationException("该职位有关联员工不能删除！");
		}
        
        if(UserJob==null){
            return new Response(1, "删除对象为空！");
        }
		Map<String, Object> params = new HashMap();
        params.put("id", id);
        userDeptJobDao.excuteHql("delete from UserDeptJob where id.jobId= :id ", params);
        userJobDao.delete(UserJob);
        return new Response();
    }

    @Override
    public void saveOrUpdateUserJob(UserJob userJob) {
  
    	//判断该职位是否存在
    	if (userJobDao.getUserJobCountByJobName(userJob) > 0 && StringUtils.isNotBlank(userJob.getJobName())) {
			throw new ApplicationException(ErrorCode.USERJOB_CONTACT_FOUND);
		}
    	
    	if(StringUtils.isBlank(userJob.getId())) {
    		//新建时重设jobSign
			userJob.setJobSign(getUniqueJobSign(userJob.getJobSign()));
    	}			
    	
        userJobDao.save(userJob);
    }
    
	public String getUniqueJobSign(String jobSign) {
		String newJobSign = jobSign;
		Map<String, Object> params = new HashMap();
		if(StringUtils.isNotBlank(jobSign)) {  
			//查找是否存在同样的jobSign
			String hql = " from UserJob uj where 1=1 and uj.jobSign like :jobSign ";
			params.put("jobSign", jobSign+"%");
			List<UserJob> jobList = userJobDao.findAllByHQL(hql, params);
			if(jobList != null && jobList.size() > 0) {  //避免重复，重设jobSign
				int max=100;
		        int min=10;
		        boolean flag = true;
		        while(flag == true) {
		        	//jobSign加上一个两位数的随机数
		        	newJobSign = jobSign + Integer.valueOf((new Random().nextInt(max - min) + min)).toString();
			        boolean isNew = true;
			        for(UserJob otherJob : jobList) {			        	
			        	if(newJobSign.equalsIgnoreCase(otherJob.getJobSign())) {
			        		isNew = false;
			        		break;
			        	}
			        }
			        if(isNew == true) flag = false;
			        else flag = true;
		        }
			}
		} 	
		return newJobSign;
	}


	@Override
	public UserJob findUserJobById(String id) {
		
		//增加关联职位
		List<UserJob> list = distributableUserJobService.findRelateUserJobByMainJobId(id);
		Set<DistributableUserJobVo> distributableUserJobs =  new HashSet<DistributableUserJobVo>();
		if(list!=null && list.size()>0){
			DistributableUserJobVo distributableUserJobVo = null;
			for(UserJob temp : list){
				distributableUserJobVo = new DistributableUserJobVo();
				distributableUserJobVo.setRelateJobId(temp.getId());
				distributableUserJobVo.setRelateJobName(temp.getJobName());
				distributableUserJobs.add(distributableUserJobVo);
			}
		}
		UserJob userJob = userJobDao.findById(id);
		userJob.setDistributableUserJobs(distributableUserJobs);
		return userJob;
	}
	
	@Override
	public DataPackage getJobListForSelection(UserJob userJob, DataPackage dp) {
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(userJob);
		return userJobDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "id", "asc"), criterionList);
	}
	
	/*@Override
	public List<UserJob> findAllUserJob() {
		String hql= " from UserJob where flag='0' ";
        List<UserJob> UserJobs = userJobDao.findAllByHQL(hql);

		for (UserJob userJob : UserJobs) {
			userJob.setUserCount(userDeptJobDao.findCountHql(" select count( distinct udj.id.userId) from UserDeptJob udj,User u where udj.id.userId=u.userId and u.ccpStatus=0 and udj.id.jobId='"+userJob.getId()+"'"));
        }
        return UserJobs;
	}*/
	
	@Override
	public List<UserJob> findAllUserJob() {
		String sql = "select * from USER_JOB WHERE FLAG = '0' ";
		Map<String, Object> params = new HashMap();
		List<UserJob> UserJobs = userJobDao.findBySql(sql, params);
//        List<UserJob> UserJobs = jdbcTemplateDao.queryForList(sql, null, UserJob.class);

		Map<String, Object> params1 = new HashMap();

		for (UserJob userJob : UserJobs) {
			userJob.setUserCount(userJobDao.findCountSql(" select count( distinct udj.USER_ID) from USER_DEPT_JOB udj,user u where udj.USER_ID=u.USER_ID and u.CCP_STATUS=0 and udj.JOB_ID='"+userJob.getId()+"'", params1));
        }
        return UserJobs;
	}

	
	@Override
	public List<UserJobVo> getAllUserJobForSync() {
		return HibernateUtils.voListMapping(userJobDao.findAll(), UserJobVo.class);
	}


	@Override
	public List<UserJob> findAllUserJobBySql(String sql, Map<String, Object> params) {
        return (List<UserJob>)userJobDao.findBySql(sql, params);
	}
}
