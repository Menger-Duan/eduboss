package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.AppNewsManage;
import com.eduboss.domainVo.AppNewsManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

@Repository
public interface AppNewsManageDao extends GenericDAO<AppNewsManage, String> {
	
	public DataPackage  getNews(DataPackage dp,AppNewsManageVo vo);
	
	public void saveOrEditNewsManage(AppNewsManage appNewsManage);
	
	public AppNewsManageVo findAppNewsManageById(String id);
	
	public void delNewsManage(String id);
	
	public List<AppNewsManageVo> getImageUrl();
	
	public List<AppNewsManageVo> getNewsBanner(int num);

}
