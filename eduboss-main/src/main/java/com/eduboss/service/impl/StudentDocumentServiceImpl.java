package com.eduboss.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.dao.StudentDocumentDao;
import com.eduboss.domain.StudentDocument;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentDocumentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.StudentDocumentService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service
public class StudentDocumentServiceImpl implements StudentDocumentService {

	@Autowired
	private StudentDocumentDao studentDocumentDao;
	@Autowired
	private UserService userService;
    
	private final static ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public void saveStudentDocument(StudentDocument StudentDocument, MultipartFile dataFile) {
		StudentDocument.setCreateTime(DateTools.getCurrentDateTime());
		User followUpUser= userService.getCurrentLoginUser();
		StudentDocument.setCreateUserId(followUpUser.getUserId());
		String fileName=dataFile.getOriginalFilename().substring  
				(0,dataFile.getOriginalFilename().lastIndexOf("."));//上传的文件名
		String puff=dataFile.getOriginalFilename().replace(fileName, "");
		String aliName = UUID.randomUUID().toString()+puff;//阿里云上面的文件名
		StudentDocument.setDocumentPath(fileName);
		StudentDocument.setAliName(aliName);
		this.saveFileToRemoteServer(dataFile, aliName);
		studentDocumentDao.save(StudentDocument);
		
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



	@Override
	public DataPackage getStudentDocument(StudentDocumentVo studentDocumentVo,
			DataPackage dataPackage) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from StudentDocument where 1 = 1 ");
		if(StringUtil.isNotBlank(studentDocumentVo.getStudentId())){
			hql.append(" and student.id = :studentId ");
			params.put("studentId", studentDocumentVo.getStudentId());
		}
		dataPackage = studentDocumentDao.findPageByHQL(hql.toString(), dataPackage,true,params);
		List<StudentDocument> list = (List<StudentDocument>) dataPackage.getDatas();
		List<StudentDocumentVo> voList = new ArrayList<StudentDocumentVo>();
		for(StudentDocument studentDocument : list){
			StudentDocumentVo vo = HibernateUtils.voObjectMapping(studentDocument, StudentDocumentVo.class);
			vo.setDocumentRealPath(PropertiesUtils.getStringValue("oss.access.url.prefix")+""+vo.getAliName());
//			System.out.println("文件路径："+vo.getDocumentRealPath());
			voList.add(vo);
		}
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	

}
