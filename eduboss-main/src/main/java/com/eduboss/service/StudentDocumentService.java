package com.eduboss.service;

import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.StudentDocument;
import com.eduboss.domainVo.StudentDocumentVo;
import com.eduboss.dto.DataPackage;

public interface StudentDocumentService {

	public void saveStudentDocument(StudentDocument StudentDocument, MultipartFile dataFile);

	public DataPackage getStudentDocument(StudentDocumentVo studentDocumentVo,DataPackage dataPackage);
	
}
