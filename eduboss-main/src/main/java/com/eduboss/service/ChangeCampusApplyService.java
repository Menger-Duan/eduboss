package com.eduboss.service;



import org.springframework.stereotype.Service;


import com.eduboss.domain.ChangeCampusApply;
import com.eduboss.domainVo.ChangeCampusApplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

@Service
public interface ChangeCampusApplyService {

	//保存 一条转介绍客户审核记录
	public Response saveChangeCampusApply(ChangeCampusApplyVo campusApplyVo);
	
	//根据createUserID和custoemrId获取记录
	public ChangeCampusApply getChangeCampusApplyByUserIdAndCustomerId(String userId,String customerId);
	
	
	//分页查询 审核者查询所有的审核记录
	public DataPackage getChangeCampusApplys(DataPackage dp,ChangeCampusApplyVo campusApplyVo);
	
	
	//分页查询   查询咨询师提交的转校申请记录
	public DataPackage getChangeCampusCustomers(DataPackage dp,ChangeCampusApplyVo campusApplyVo);
	
	
	//审核转介绍客户
	public Response updateChangeCampusResult(ChangeCampusApplyVo campusApplyVo);
	
	
	//根据记录id 查询审核结果
	public ChangeCampusApplyVo loadChangeCampusResult(String id);
}
