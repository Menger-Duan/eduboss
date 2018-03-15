package com.eduboss.service.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eduboss.common.NewsType;
import com.eduboss.common.UploadFileStatus;
import com.eduboss.dao.AppNewsManageDao;
import com.eduboss.domain.AppNewsManage;
import com.eduboss.domain.UploadFileRecord;
import com.eduboss.domainVo.AppNewsManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AppNewsManageService;
import com.eduboss.service.UploadFileService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("AppNewsManageService")
public class AppNewsManageServiceImpl implements AppNewsManageService {

	@Autowired
	private AppNewsManageDao appNewsManageDao;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	@Autowired
	private UserService userService;
	
	String uploadFile="";
	String cutImageSuccess="";
	
	@Override
	public DataPackage getNews(DataPackage dp, AppNewsManageVo vo) {
		return appNewsManageDao.getNews(dp, vo);
	}

	@Override
	public void saveOrEditNewsManage(AppNewsManageVo vo,
			MultipartFile dataFile,String delImage,String x,String y,String w,String h,
			String x1,String y1,String w1,String h1) {
		uploadFile="";
		AppNewsManage appNewsManage=new AppNewsManage();		
		if(vo.getId()!=null && StringUtils.isNotBlank(vo.getId())){
			appNewsManage=appNewsManageDao.findById(vo.getId());
			appNewsManage.setTitle(vo.getTitle());
			appNewsManage.setValideStatus(vo.getValideStatus());			
			appNewsManage.setModifyTime(DateTools.getCurrentDateTime());
			appNewsManage.setModifyUser(userService.getCurrentLoginUser());
			appNewsManage.setAssistantTitle(vo.getAssistantTitle());
			appNewsManage.setPublishUser(vo.getPublishUser());
			//对内容做处理
			String content=vo.getContent();	
			if(content!=null && StringUtils.isNotBlank(content)){
				try {
					appNewsManage.setContent(URLDecoder.decode(content,"UTF-8"));				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}else{
					appNewsManage.setContent(null);
			}	
			if(vo.getContentUrl()!=null && StringUtils.isNotBlank(vo.getContentUrl())){
				appNewsManage.setContentStatus("isUrl");
				appNewsManage.setContentUrl(vo.getContentUrl());
			}else{
				appNewsManage.setContentStatus("isContent");
				appNewsManage.setContentUrl(null);
			}
			
			if(delImage!=null && StringUtils.isNotBlank(delImage)){
				appNewsManage.setCoverImageName(null);
				appNewsManage.setCoverImagePath(null);
			}
			//修改的时候 删掉此新闻对应的 UEditor原上传文件
			delFromAppNewsManageFile(appNewsManage.getId());
		}else{
			appNewsManage=HibernateUtils.voObjectMapping(vo, AppNewsManage.class);
			appNewsManage.setCreateTime(DateTools.getCurrentDateTime());
			appNewsManage.setCreateUser(userService.getCurrentLoginUser());
			appNewsManage.setModifyUser(null);
			//对内容做处理
			String content=appNewsManage.getContent();	
			if(StringUtils.isNotBlank(content)){
				try {
					appNewsManage.setContent(URLDecoder.decode(content,"UTF-8"));				
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}	
			if(appNewsManage.getContentUrl()!=null && StringUtils.isNotBlank(appNewsManage.getContentUrl())){
				appNewsManage.setContentStatus("isUrl");
			}else{
				appNewsManage.setContentStatus("isContent");
			}
		}	
		if(dataFile!=null && dataFile.getSize()==0){
			String aliName=appNewsManage.getCoverImagePath();
			InputStream in=null;
			OutputStream out=null;
			if(appNewsManage.getCoverImageScreenshot()!=null && StringUtils.isNotBlank(appNewsManage.getCoverImageScreenshot())){				
				in=AliyunOSSUtils.get(appNewsManage.getCoverImageScreenshot());
			}else {
				in=AliyunOSSUtils.get(appNewsManage.getCoverImagePath());				
			}
			byte[] buff = new byte[4096];
			CommonsMultipartFile cf= (CommonsMultipartFile)dataFile; 
	        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	        File file = fi.getStoreLocation();
				try {
					out = new FileOutputStream(file);
					int len = 0;
					while ((len = in.read(buff)) > 0) {
						out.write(buff, 0, len);
					}
				} catch (FileNotFoundException e) {				
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
				
				if(StringUtils.isNotBlank(x) && StringUtils.isNotBlank(y)&& StringUtils.isNotBlank(w)&& StringUtils.isNotBlank(h)){							    		        
					cutImageSuccess="";
					this.cutImage(file, Math.round(Float.valueOf(x)) , Math.round(Float.valueOf(y)),Math.round(Float.valueOf(w)), Math.round(Float.valueOf(h)));
					if(StringUtils.isBlank(cutImageSuccess)){
						throw new ApplicationException("截取图片失败");
					}
					if(uploadFile!=null && StringUtils.isNotBlank(uploadFile)){
						try {
							AliyunOSSUtils.put("COPPER_"+aliName, uploadFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					appNewsManage.setCoverImageScreenshot("COPPER_"+aliName);
				}
				
				if(StringUtils.isNotBlank(x1) && StringUtils.isNotBlank(y1)&& StringUtils.isNotBlank(w1)&& StringUtils.isNotBlank(h1)){				
					this.cutImage(file, Math.round(Float.valueOf(x1)) , Math.round(Float.valueOf(y1)),Math.round(Float.valueOf(w1)), Math.round(Float.valueOf(h1)));
					cutImageSuccess="";
					if(StringUtils.isBlank(cutImageSuccess)){
						throw new ApplicationException("截取图片失败");
					}
					if(uploadFile!=null && StringUtils.isNotBlank(uploadFile)){
						try {
							AliyunOSSUtils.put("COPPER_ONE_"+aliName, uploadFile);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						appNewsManage.setCoverImageScreenshotOne("COPPER_ONE_"+aliName);
					}
					
				}
				
			
		}
		//上传图片
		else if(dataFile!=null && dataFile.getSize()>0){
			String fileName=dataFile.getOriginalFilename();//上传的文件名
			String puff=dataFile.getOriginalFilename().replace(fileName, "");
			String aliName = UUID.randomUUID().toString()+puff;//阿里云上面的文件名
			appNewsManage.setCoverImagePath("COP_"+aliName);
			appNewsManage.setCoverImageName(fileName);			 
			this.saveFileToRemoteServer(dataFile, "COP_"+aliName);	
			if(StringUtils.isNotBlank(x) && StringUtils.isNotBlank(y)&& StringUtils.isNotBlank(w)&& StringUtils.isNotBlank(h)){			
				CommonsMultipartFile cf= (CommonsMultipartFile)dataFile; 
		        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
		        File file = fi.getStoreLocation();
		        cutImageSuccess="";		        
				this.cutImage(file, Math.round(Float.valueOf(x)) , Math.round(Float.valueOf(y)),Math.round(Float.valueOf(w)), Math.round(Float.valueOf(h)));
				if(StringUtils.isBlank(cutImageSuccess)){
					throw new ApplicationException("截取图片失败");
				}
				if(uploadFile!=null && StringUtils.isNotBlank(uploadFile)){
					try {
						AliyunOSSUtils.put("COPPER_"+aliName, uploadFile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				appNewsManage.setCoverImageScreenshot("COPPER_"+aliName);
			}
			
			if(StringUtils.isNotBlank(x1) && StringUtils.isNotBlank(y1)&& StringUtils.isNotBlank(w1)&& StringUtils.isNotBlank(h1)){				
				CommonsMultipartFile cf= (CommonsMultipartFile)dataFile; 
		        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
		        File file = fi.getStoreLocation();
		        cutImageSuccess="";		        
				this.cutImage(file, Math.round(Float.valueOf(x1)) , Math.round(Float.valueOf(y1)),Math.round(Float.valueOf(w1)), Math.round(Float.valueOf(h1)));
				if(StringUtils.isBlank(cutImageSuccess)){
					throw new ApplicationException("截取图片失败");
				}
				if(uploadFile!=null && StringUtils.isNotBlank(uploadFile)){
					try {
						AliyunOSSUtils.put("COPPER_ONE_COP_"+aliName, uploadFile);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				appNewsManage.setCoverImageScreenshotOne("COPPER_ONE_COP_"+aliName);
			}
		}
				
		appNewsManageDao.saveOrEditNewsManage(appNewsManage);			
		//发布的是分公司信息，取到接收信息的分公司
		if(appNewsManage.getType()==NewsType.BRENCHINFO && StringUtils.isNotBlank(vo.getBrenchId())){
			
			Map<String, Object> params = Maps.newHashMap();
			
			if(StringUtils.isNotBlank(vo.getId())){
				//修改
				String sqlDel="DELETE from app_news_manage_brench where app_id= :appId ";
				params.put("appId", vo.getId());
				appNewsManageDao.excuteSql(sqlDel,params);
			}
			String ids[]=vo.getBrenchId().split(",");
			int i = 0;
			params.put("appNewsManageId", appNewsManage.getId());
			if(ids.length>0){
				for(String id:ids){
					i=i+1;					
					params.put("id"+i, id);
					String sql="insert into app_news_manage_brench(app_id,brench_id) values(:appNewsManageId,:id"+i+" "+")";
					appNewsManageDao.excuteSql(sql,params);			
				}
			}
			
		}
			
			//UEditor上传文件处理
			String filePaths = uploadFileService.listUploadFileFromContent(appNewsManage.getContent());
			if(StringUtils.isNotBlank(filePaths)) {
				Map<String, Object> param = Maps.newHashMap();
				uploadFileService.updateUploadFileStatus(filePaths, UploadFileStatus.INUSE.getValue());  //更新上传的文件状态为在使用
				List<UploadFileRecord> uploadFileRecordList = uploadFileService.getListByFilePath(filePaths);
				int j = 0;
				param.put("appNewsManageId", appNewsManage.getId());
		        for(UploadFileRecord uploadFileReocrd : uploadFileRecordList){
		        	j = j+1;
		        	param.put("id"+j, uploadFileReocrd.getId());
		        	String sql = "insert into app_news_manage_file(app_id, file_id) VALUES(:appNewsManageId , :id"+j+" "+")";
		        	appNewsManageDao.excuteSql(sql,param);
		        }
			}
		
	}
	
	private void delFromAppNewsManageFile(String appNewsId) {
		String selectFilePathSql = " select ufr.file_path from "
				+ " (select * from app_news_manage_file where app_id = '"
				+ appNewsId + "') snf"
				+ " left join upload_file_record ufr on ufr.id = snf.file_id";
		List<String> filePathList = (List<String>) appNewsManageDao.getCurrentSession().createSQLQuery(selectFilePathSql).list();
		if (filePathList != null && filePathList.size() > 0) {
			StringBuffer filePathSb = new StringBuffer();
			for (String str : filePathList) {
				filePathSb.append("," + str);
			}
			if (StringUtils.isNotBlank(filePathSb)) {
				uploadFileService.updateUploadFileStatus(filePathSb.toString()
						.substring(1), UploadFileStatus.UNUSED.getValue()); // 更新此新闻对应的以前的的文件状态为未使用（未使用的文件将会通过跑批删除）
			}
			// 删除旧的文件关联记录UEditor
			Map<String, Object> params = Maps.newHashMap();
			String delSql = "delete from app_news_manage_file where app_id = :appId ";
			params.put("appId", appNewsId);
			//删除阿里云图片
//			AppNewsManage appNewsManage=appNewsManageDao.findById(appNewsId);
//			if(appNewsManage.getCoverImagePath()!=null && StringUtils.isNotBlank(appNewsManage.getCoverImagePath())){
//				AliyunOSSUtils.remove(appNewsManage.getCoverImagePath());
//			}
			appNewsManageDao.excuteSql(delSql,params);
		}
	}

	@Override
	public AppNewsManageVo findAppNewsManageById(String id) {
		return appNewsManageDao.findAppNewsManageById(id);
	}

	@Override
	public void delNewsManage(String id) {
		delFromAppNewsManageFile(id);
		appNewsManageDao.delNewsManage(id);
		
		
	}
	
	/**
	 * 把上传的档案存到阿里云
	 * @param myfile1
	 * @param fileName
	 */
	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName)  {
		try {
			AliyunOSSUtils.put(fileName, myfile1.getInputStream(), myfile1.getSize());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}
	
	/**
     * 图片切割
     * @param imagePath  原图地址
     * @param x  目标切片坐标 X轴起点
     * @param y  目标切片坐标 Y轴起点
     * @param w  目标切片 宽度
     * @param h  目标切片 高度
     */	    
	public void cutImage(File dataFile, int x ,int y ,int w,int h){
	 try {
	     Image img;
	     ImageFilter cropFilter;
	     
	     String rootPath=getClass().getResource("/").getFile().toString();
	     uploadFile=rootPath.substring(0,rootPath.lastIndexOf("WEB-INF"))+"uploadfile/"+dataFile.getName();
	     // 读取源图像
	     BufferedImage bi = ImageIO.read(dataFile);
	     int srcWidth = bi.getWidth();      // 源图宽度
	     int srcHeight = bi.getHeight();    // 源图高度

	     //若原图大小大于切片大小，则进行切割
	     if (srcWidth >= w && srcHeight >= h) {
	     Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);

		  cropFilter = new CropImageFilter(x, y, w, h);
		  img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
		  BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
		  Graphics g = tag.getGraphics();
		  g.drawImage(img, 0, 0, null); // 绘制缩小后的图
		  g.dispose();
		  // 输出为文件
		  ImageIO.write(tag, "JPEG", new File(uploadFile));	
		  cutImageSuccess="success";
	     }	
		 } catch (IOException e) {
		     e.printStackTrace();
		 }
	}
	
	/**
	 * 获取五大类每条最新数据
	 */
	@Override
	public List<AppNewsManageVo> getImageUrl() {
		return appNewsManageDao.getImageUrl();
	}
	
	/**
	 * APP根据设置条数获取新闻banner
	 */
	
	public List<AppNewsManageVo> getNewsBanner(int num){
		return appNewsManageDao.getNewsBanner(num);
	}
	
	/**
	 * APPh5显示指定类型新闻的数据
	 */
	public List<AppNewsManageVo> getNewsByType(String id,String type){
		AppNewsManage appNews=appNewsManageDao.findById(id);
		String createTime=appNews.getCreateTime();
		String previousSql=new String();
		String nextSql=new String();
		Map<String, Object> params = Maps.newHashMap();
		params.put("createTime", createTime);
		params.put("type", type);
		previousSql="select * from app_news_manage where CREATE_TIME< :createTime and type= :type and valide_status='VALID' order by CREATE_TIME DESC LIMIT 1";
		nextSql="select * from app_news_manage where CREATE_TIME> :createTime and type= ::type and valide_status='VALID' order by CREATE_TIME  ASC LIMIT 1";
		List<AppNewsManage> previousList=appNewsManageDao.findBySql(previousSql,params);
		List<AppNewsManage> nextList=appNewsManageDao.findBySql(nextSql,params);
		List<AppNewsManageVo> volist=new ArrayList<AppNewsManageVo>();
		if(previousList!=null && previousList.size()>0){
			AppNewsManageVo vo=HibernateUtils.voObjectMapping(previousList.get(0),AppNewsManageVo.class);
			volist.add(vo);
		}
		if(nextList!=null && nextList.size()>0){
			AppNewsManageVo vo=HibernateUtils.voObjectMapping(nextList.get(0),AppNewsManageVo.class);
			volist.add(vo);
		}
		
//		String sql="select * from app_news_manage where id!='"+id+"' and TYPE='"+type+"' and valide_status='VALID' ORDER BY CREATE_TIME DESC LIMIT 2 ";
//		List<AppNewsManage> list=appNewsManageDao.findBySql(sql);
		
		return volist;
	}

	/**
	 * 获取封面图片数
	 */
	@Override
	public String getNewsbannerNum() {
		String sql="select num from app_news_banner_num";
		List<String> list=(List<String>)appNewsManageDao.getCurrentSession().createSQLQuery(sql).list();
		return list.get(0);
	}

	/**
	 * 设置封面图片数
	 */
	@Override
	public void setNewsbannerNum(String num) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("num", num);
		String sql="UPDATE app_news_banner_num SET num= :num where id='123123123' ";
		appNewsManageDao.excuteSql(sql,params);
	}
	
	
	
	

}
