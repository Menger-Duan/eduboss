package com.eduboss.dao;

import com.eduboss.domain.PromiseClassSubject;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.PromiseChargeSearchDto;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Repository
public interface PromiseClassSubjectDao extends GenericDAO<PromiseClassSubject,String>{


	DataPackage getPromiseSubjectList(DataPackage dp, Map<String, Object> paramMap);

	void updateNewPromiseStudentId(String newId,String oldId);

	List<PromiseClassSubject> getPromiseSubjectList(Map<String, Object> paramMap);

	List getPromiseSubjectDetailList(String promiseStudentId);


	Map getEcsContractInfo(String contractId);


	List getEcsContractChargeInfo(String contractProductId);


	List<PromiseClassSubject> findPromiseSubjectByClassIdAndStudentId(String miniClassId,String studentId);

	DataPackage getEcsContractChargeList(String contractProductId,PromiseChargeSearchDto dto,DataPackage dataPackage );

	List getEcsContractChargeInfoAfter2018(String contractProductId, BigDecimal price);
}
