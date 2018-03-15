package com.eduboss.service;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.AppNewsManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

public interface AppNewsManageService {
	
	public DataPackage getNews(DataPackage dp,AppNewsManageVo vo);
	
	public void saveOrEditNewsManage(AppNewsManageVo vo,MultipartFile dataFile,String delImage,
			String x,String y,String w,String h,String x1,String y1,String w1,String h1);
	
	public AppNewsManageVo findAppNewsManageById(String id);
	
	public void delNewsManage(String id);
	
	public List<AppNewsManageVo> getImageUrl();

	public List<AppNewsManageVo> getNewsBanner(int num);
	
	public List<AppNewsManageVo> getNewsByType(String id,String type);

	public String getNewsbannerNum();
	
	public void setNewsbannerNum(String num);
}
