package com.eduboss.service;

import java.util.List;

import com.eduboss.domainVo.SystemDegradeVo;
import com.eduboss.dto.DataPackage;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.Organization;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.UserJob;

public interface SystemConfigService {
	
	public List getSystemConfigList(SystemConfig systemConfig); 
	
	public void savaOrUpdateSystemConfig(SystemConfig systemConfig);
	
	public SystemConfig getSystemPath(SystemConfig systemConfig);
	
	//系统信息配置
	public String saveOrUpdateSystemConfigForImg(SystemConfig systemConfig, MultipartFile myfile1,
			 String systemPath,String systemPathId);
	
	//系统首页配置
	public String saveOrUpdateSystemConfigForImg(SystemConfig systemConfig, MultipartFile myfile1,MultipartFile myfile2) throws Exception;

	public void updateSystemConfigValueList(SystemConfig[] sysConfList);

	//邮件系统配置
	public void initOrgSignForMailSys(String sysId, Organization[] orgList);
	public void initJobSignForMailSys(String sysId, UserJob[] jobList);

	public void saveSystemDegrade(SystemDegradeVo vo);

	public DataPackage findSystemDegradeVoList(SystemDegradeVo vo,DataPackage dp);

	SystemDegradeVo findSystemDegradeById(int id);
}
