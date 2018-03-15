package com.eduboss.service.impl;

import java.io.IOException;
import java.util.*;

import com.eduboss.dao.SystemDegradeDao;
import com.eduboss.domain.*;
import com.eduboss.domainVo.SystemDegradeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.SystemConfigDao;
import com.eduboss.domainVo.SystemConfigVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.SystemConfigService;
import com.eduboss.service.UserJobService;
import com.eduboss.service.UserService;
import com.google.common.collect.Maps;

@Service
public class SystemConfigServiceImpl implements SystemConfigService{
	
	@Autowired
	private SystemConfigDao systemConfigDao;
	
	@Autowired
	private UserService userSerivce;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
    private UserJobService userJobService;

	@Autowired
	private SystemDegradeDao systemDegradeDao;
	
	
	@Override
	public List<SystemConfigVo> getSystemConfigList(SystemConfig systemConfig) {
		StringBuilder hql = new StringBuilder();
		DataPackage dp = new DataPackage();
		dp.setPageNo(0);
		dp.setPageSize(20);
		hql.append(" from SystemConfig where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(systemConfig.getTag())){
			hql.append(" and tag = :tag ");
			params.put("tag",systemConfig.getTag());
		}
		if(systemConfig.getGroup()!=null){
			hql.append("and group.id= :group ");
			params.put("group",systemConfig.getGroup().getId());
		}
		
		dp = systemConfigDao.findPageByHQL(hql.toString(), dp,true,params);
		List<SystemConfig> list = (List<SystemConfig>) dp.getDatas();
		List<SystemConfigVo> voList = new ArrayList<SystemConfigVo>();
		for(SystemConfig sc : list){
			SystemConfigVo vo = HibernateUtils.voObjectMapping(sc, SystemConfigVo.class);
			voList.add(vo);
		}
		return voList;
	}

	@Override
	public void savaOrUpdateSystemConfig(SystemConfig systemConfig) {
		if(StringUtils.isBlank(systemConfig.getId())){
			systemConfig.setId(null);
		}
		User user = new User();
//		if(systemConfig.getId()==null){
			systemConfig.setCreateTime(DateTools.getCurrentDateTime());
			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
			systemConfig.setCreateUser(user);
//		}else{
//			SystemConfig sc = systemConfigDao.findById(systemConfig.getId());
//			systemConfig.setCreateTime(sc.getCreateTime());
//			systemConfig.setCreateUser(sc.getCreateUser());
//			systemConfig.setModifyTime(DateTools.getCurrentDateTime());
//			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
//			systemConfig.setModifyUser(user);
//		}		
		systemConfigDao.save(systemConfig);
		
	}
	
	@Override
	public void updateSystemConfigValueList(SystemConfig[] sysConfList) {
		for(SystemConfig sysConf : sysConfList) {
			SystemConfig sysConfIndb = systemConfigDao.findById(sysConf.getId());
            if(sysConfIndb != null) {         	
            	sysConfIndb.setValue(sysConf.getValue());
            	sysConfIndb.setModifyTime(DateTools.getCurrentDateTime());
            	User user = new User();
    			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
    			sysConfIndb.setModifyUser(user);
            }
		}
		
	}

	@Override
	public SystemConfig getSystemPath(SystemConfig systemConfig) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemConfig where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(systemConfig.getTag())){
			hql.append(" and tag = :tag ");
			params.put("tag",systemConfig.getTag());
		}
		if(systemConfig.getGroup()!=null){
			hql.append("and group.id= :group ");
			params.put("group",systemConfig.getGroup().getId());
		}
		List<SystemConfig> list = systemConfigDao.findAllByHQL(hql.toString(),params);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}


	//系统信息配置
	public String saveOrUpdateSystemConfigForImg(SystemConfig systemConfig, MultipartFile myfile1,
			 String systemPath,String systemPathId){
		if(StringUtils.isBlank(systemConfig.getId())){
			systemConfig.setId(null);
		}
		//获取图片名字
		String picType = "";
		if(myfile1 != null && !myfile1.isEmpty()){
			String pic = myfile1.getOriginalFilename(); 
			picType = pic.substring(pic.lastIndexOf("."));//图片的格式 如jpg
		}
		
		//获取虚拟路径
		SystemConfig sc = new SystemConfig();
		sc.setTag("systemPath");
		sc.setGroup(systemConfig.getGroup());
		String realPath = "/tmp/uploadfile/image/";//默认路径
		SystemConfig path = this.getSystemPath(sc);
		if(path!=null && StringUtils.isNotBlank(path.getValue())){
			realPath = path.getValue();
		}
		
		String result=null;
		String fileName = systemConfig.getGroup().getId()+"_systemIcon"+picType;
//		realPath = servicePath + realPath;
		String loadName = "/uploadfile/image/";
		try {
			if(!myfile1.isEmpty()){
				boolean  isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(), realPath+fileName);//将表单提交过来的图片保存到TOMCAT下
				if(isUploadFinish){
					//if(StringUtils.isNotBlank(systemConfig.getId())){
//						systemConfig.setRemark(realPath+fileName);//保存图片路径
						systemConfig.setRemark(loadName+fileName);//保存图片路径
					//}
				}
			}
			systemConfigDao.saveOrUpdateSystemConfig(systemConfig);
			//systemConfigDao.save(systemConfig);
			result = "成功";
			
			if(StringUtils.isNotBlank(systemPath)){ //保存虚拟路径
				SystemConfig conf = new SystemConfig();
				if(StringUtils.isBlank(systemPathId)){
					conf.setId(null);
					conf.setName("文件虚拟路径");
					conf.setValue(systemPath);
					conf.setTag("systemPath");
					conf.setGroup(systemConfig.getGroup());
					systemConfigDao.saveOrUpdateSystemConfig(conf);
				}else{
					conf = systemConfigDao.findById(systemPathId);
					conf.setValue(systemPath);
					systemConfigDao.saveOrUpdateSystemConfig(conf);
				}
				
				result = "成功";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//系统首页配置
	public String saveOrUpdateSystemConfigForImg(SystemConfig systemConfig, MultipartFile myfile1,MultipartFile myfile2) throws Exception{
		//获取图片名字
		String picType = "";
		if(myfile1 != null && !myfile1.isEmpty()){
			String pic = myfile1.getOriginalFilename(); 
			picType = pic.substring(pic.lastIndexOf("."));//图片的格式 如jpg
		}
		String picType2 = "";
		if(myfile2 != null && !myfile2.isEmpty()){
			String pic2 = myfile2.getOriginalFilename(); 
			picType2 = pic2.substring(pic2.lastIndexOf("."));//图片的格式 如jpg
		}
		//获取虚拟路径
		SystemConfig sc = new SystemConfig();
		sc.setTag("systemPath");
		sc.setGroup(systemConfig.getGroup());
		String realPath = "/tmp/uploadfile/image/";//默认路径
		SystemConfig path = this.getSystemPath(sc);
		if(path!=null && StringUtils.isNotBlank(path.getValue())){
			realPath = path.getValue();
		}
		//根据ID获取对象
		SystemConfig systemCf = new SystemConfig();
		String systemUrl = null;
		if(StringUtils.isNotBlank(systemConfig.getId())){
			systemCf = systemConfigDao.findById(systemConfig.getId());
			systemCf.setName(systemConfig.getName());
			systemCf.setValue(systemConfig.getValue());
			systemUrl = systemCf.getRemark();
		}
		
		String result = "失败";
		String fileName1 = "loginLogo"+picType;
		String fileName2 = "loginBigPic"+picType2;
		String loadName = "/uploadfile/image/";
//				realPath = servicePath + realPath;
		try {
			String[] urls = new String[]{"",""};
			String[] arr = null;
			if(StringUtils.isNotBlank(systemUrl)){
				arr = systemUrl.split(",");
			}
			
			boolean  isUploadFinish=true,isUploadFinish2=true;
			if(!myfile1.isEmpty()){
				isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(),realPath+fileName1);//首页图标
				if(isUploadFinish && urls.length>1){
//							urls[0]=realPath+fileName1;
					urls[0]=loadName+fileName1;
				}
			}
			if(!myfile2.isEmpty()){
				isUploadFinish2 = FileUtil.readInputStreamToFile(myfile2.getInputStream(),realPath+fileName2);//首页图片
				if(isUploadFinish2){
//							urls[1]=realPath+fileName2;
					urls[1]=loadName+fileName2;
				}
			}
			if(StringUtils.isBlank(urls[0]) && arr != null && StringUtils.isNotBlank(arr[0]) ){
				urls[0] = arr[0];
			}
			if(StringUtils.isBlank(urls[1]) && arr!=null && arr.length>=2 && StringUtils.isNotBlank(arr[1]) ){
				urls[1] = arr[1];
			}
			
			if(isUploadFinish && isUploadFinish2 ){
				if(StringUtils.isNotBlank(systemConfig.getId())){
					systemCf.setRemark(urls[0]+","+urls[1]);
					systemCf.setName("首页图标");
					systemConfigDao.saveOrUpdateSystemConfig(systemCf);
				}else{
					systemConfig.setName("首页图标");
					systemConfig.setRemark(urls[0]+","+urls[1]);//保存图片路径
					systemConfigDao.saveOrUpdateSystemConfig(systemConfig);
				}
				result = "成功";
			}else{
				throw new ApplicationException("系统繁忙，请稍后再试");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void initOrgSignForMailSys(String sysId, Organization[] orgList) {
		for(Organization org : orgList) {
			Organization orgIndb = organizationDao.findById(org.getId());
			if(StringUtils.isBlank(orgIndb.getOrgSign())) {  //空则插入
				orgIndb.setOrgSign(userSerivce.getUniqueOrgSign(org.getOrgSign()));
			}
		}
		updateSysValue(sysId, "1");
	}

	@Override
	public void initJobSignForMailSys(String sysId, UserJob[] jobList) {
		for(UserJob job : jobList) {
			UserJob jobIndb = userJobService.findUserJobById(job.getId());
			if(StringUtils.isBlank(jobIndb.getJobSign())) {  //空则插入
				jobIndb.setJobSign(userJobService.getUniqueJobSign(job.getJobSign()));
			}
		}
		updateSysValue(sysId, "1");
	}
	
	private void updateSysValue(String sysId, String value) {
		SystemConfig sysConfIndb = systemConfigDao.findById(sysId);
        if(sysConfIndb != null) {         	
        	sysConfIndb.setValue(value);  //已初始化
        	sysConfIndb.setModifyTime(DateTools.getCurrentDateTime());
        	User user = new User();
			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
			sysConfIndb.setModifyUser(user);
        }
	}


	@Override
	public void saveSystemDegrade(SystemDegradeVo degradeVo) {
		SystemDegrade degrade= HibernateUtils.voObjectMapping(degradeVo,SystemDegrade.class);
		if(degradeVo.getId()>0){
			SystemDegrade domain =systemDegradeDao.findById(degradeVo.getId());
			domain.setModifyTime(DateTools.getCurrentDateTime());
			domain.setModifyUser(userSerivce.getCurrentLoginUser());
			degrade.setCreateTime(domain.getCreateTime());
			degrade.setCreateUser(domain.getCreateUser());
			systemDegradeDao.merge(degrade);
		}else {
			degrade.setCreateTime(DateTools.getCurrentDateTime());
			degrade.setCreateUser(userSerivce.getCurrentLoginUser());
			systemDegradeDao.save(degrade);
		}
		saveProjectsToJedis(degrade.getType(),degrade.getId(),degrade.getStatus());

	}

	private void saveProjectsToJedis(String type,int id,int status){
		Map<String,Object> pmap= new HashMap();
		String hql=" from SystemDegrade where type= '"+type+"'";
		if(status==1){
			hql+=" and id <>'"+id+"'";
		}
		List<SystemDegrade> list=systemDegradeDao.findAllByHQL(hql,pmap);
		Set<String> setValues=new HashSet<String>();
		for(SystemDegrade domain:list){
			if(StringUtils.isNotBlank(domain.getProjects())){
				String[] projects= domain.getProjects().split(",");
				for(String project:projects){
					setValues.add(project);
				}
			}
		}

		try {
			byte[] key= new byte[0];
			key = ObjectUtil.objectToBytes(type);
			JedisUtil.set(key, ObjectUtil.objectToBytes(setValues));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DataPackage findSystemDegradeVoList(SystemDegradeVo vo, DataPackage dp) {
		Map<String,Object> pmap= new HashMap();
		StringBuffer hql=new StringBuffer();
		hql.append(" from SystemDegrade where 1=1 ");
		if(StringUtils.isNotBlank(vo.getName())){
			hql.append(" and name like '%"+vo.getName()+"%'");
		}
		dp= systemDegradeDao.findPageByHQL(hql.toString(),dp,true,pmap) ;

		if(dp.getDatas().size()>0){
			dp.setDatas(HibernateUtils.voListMapping((List<SystemDegrade>) dp.getDatas(), SystemDegradeVo.class));
		}

		return dp;
	}


	@Override
	public SystemDegradeVo findSystemDegradeById(int id) {
		return HibernateUtils.voObjectMapping(systemDegradeDao.findById(id), SystemDegradeVo.class);
	}

}
