package com.eduboss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.StudentReturnDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.StudentReturnFee;
import com.eduboss.domainVo.StudentReturnFeeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.StudentReturnService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Service("com.eduboss.service.StudentReturnService")
public class StudentReturnServiceImpl implements StudentReturnService {
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private StudentReturnDao studentReturnDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Autowired
	private DataDictDao dataDictDao;
	
	
	@Override
	public void saveStudentReturn(StudentReturnFee studentReturnFee) {
			studentReturnDao.save(studentReturnFee);
			studentReturnDao.flush();
	}

	/**
	 * 返回学生退费记录
	 * @param studentReturnVo
	 * @param dp
	 * @param modelVo
	 * @return
	 */
	@Override
	public DataPackage getStudentReturnList(StudentReturnFeeVo studentReturnVo,
			DataPackage dp,ModelVo modelVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from StudentReturnFee s ";
		String hqlWhere="";
		
		if(StringUtils.isNotEmpty(modelVo.getStartDate())) {
			hqlWhere+=" and createTime >= :startDate ";
			params.put("startDate", modelVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotEmpty(modelVo.getEndDate())) {
			hqlWhere+=" and createTime <= :endDate ";
			params.put("endDate", modelVo.getEndDate() + " 23:59:59");
		}
		if(StringUtils.isNotEmpty(studentReturnVo.getStudentName())) {
			hqlWhere+=" and student.name like :studentName ";
			params.put("studentName", "%" + studentReturnVo.getStudentName() + "%");
		}
		if(StringUtils.isNotEmpty(studentReturnVo.getCreateUser())) {
			hqlWhere+=" and createUser.name like :createUserName ";
			params.put("createUserName", "%" + studentReturnVo.getCreateUser() + "%");
		}
		if(StringUtils.isNotEmpty(studentReturnVo.getCampus())) {
			hqlWhere+=" and campus.name like :campusName ";
			params.put("campusName", "%" + studentReturnVo.getCampus() + "%");
		}
		if (userService.getCurrentLoginUser().getUserId().equals("112233")) {
		    hqlWhere+=" and createUser.userId = '112233' ";
		} else {
		    if(StringUtils.isNotEmpty(studentReturnVo.getCampusId())) {
		        hqlWhere+=" and campus.id = :campusId ";
		        params.put("campusId", studentReturnVo.getCampusId());
		    }
		}
		if (StringUtils.isNotEmpty(studentReturnVo.getGradeId())){
			hqlWhere+=" and student.gradeDict.id = '"+studentReturnVo.getGradeId()+"' ";
		}
		
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","campus.orgLevel");
		sqlMap.put("stuId","student.id");
		sqlMap.put("manegerId","student.studyManegerId");
		sqlMap.put("returnUser","createUser.userId");
		sqlMap.put("signUser","contract.signStaff.userId");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("退费记录","nsql","hql");
		hqlWhere+=roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap);


		if(!"".equals(hqlWhere)){
			hqlWhere="where "+hqlWhere.substring(4);
		}
		hql+=hqlWhere;
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by s."+dp.getSidx()+" "+dp.getSord();
		} 
		dp=studentReturnDao.findPageByHQL(hql, dp, true, params);
		List<StudentReturnFee> list = (List<StudentReturnFee>)dp.getDatas() ;
		List<StudentReturnFeeVo> voList = HibernateUtils.voListMapping(list, StudentReturnFeeVo.class);
		dp.setDatas(voList);
		return dp;
	}


	@Override
	public void saveStudentReturn(String studentReturnId, String returnType,
			String returnReason,String accountName,String account) {
		StudentReturnFee domain=studentReturnDao.findById(studentReturnId);
		DataDict re =null;
		if (returnType!=null){
			re = dataDictDao.findById(returnType);
		}
		domain.setReturnType(re);
		domain.setReturnReason(returnReason);
		domain.setAccountName(accountName);
		domain.setAccount(account);
		studentReturnDao.save(domain);
	}
	
	
}
