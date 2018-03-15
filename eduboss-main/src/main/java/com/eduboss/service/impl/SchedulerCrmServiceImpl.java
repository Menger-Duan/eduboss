package com.eduboss.service.impl;

import org.springframework.stereotype.Service;
import com.eduboss.service.SchedulerCrmService;

@Service
public class SchedulerCrmServiceImpl implements SchedulerCrmService{

	

	
	
	
//	/**
//	 * 从无效客户手里释放客户资源 目前只是释放  无效的咨询师  外呼主管 外呼专员  网络主管 网络专员
//	 * 每天半夜3点执行 24小时制
//	 * 校区营运主任咨询师账号注销后，他手上的资源自动释放到校区资源池；TMK主管账号注销，他手上的资源自动释放到TMK资源池；网络则回到网络资源池
//	 * 备注:如果 所属的资源池找不到的话 不释放
//	 */
//	@Scheduled(cron="0 0 6 * * ?") //0/1 * * * * ? 半夜三点  0 */1 * * * ?一分钟一次
//	@Override
//	public void releaseCustomerByInvalidUser() {
//		ResourcePool resourcePool = null;
//		List<Customer> customers = null;	
//	    int wlzy_size =0;
//	    int wlzg_size =0;
//	    int whzy_size =0;
//	    int whzg_size =0;
//	    int zxs_size  =0;
//	    int scjl_size =0;
//	    int xqyyzr_size = 0;
//		Boolean isWLZY =false;
//		Boolean isWLZG =false;
//		Boolean isWHZY =false;
//		Boolean isWHZG =false;
//		Boolean isZXS =false;
//		Boolean isSCJL =false;
//		Boolean isXQYYZR =false;
//		String remark ="无效用户释放手上客户资源";
//		//JedisUtil.rpoplpush("invalidUserInfo".getBytes(), "temp-invalidUserInfo".getBytes());
//		byte[] object = JedisUtil.rpop("invalidUserInfo".getBytes());
//		if (object != null) {
//			Object value = JedisUtil.ByteToObject(object);
//			User user = userService.loadUserById(value.toString());
//			ResourcePool networkPool = resourcePoolService.getBelongBranchResourcePool("", OrganizationType.GROUNP.getValue());
//			String userId = user.getUserId();
//			List<Role> roles =userService.getRoleByUserId(userId);
//			if(roles!=null && roles.size()>0){
//				for(Role role:roles){	
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.NETWORK_SPEC)){
//						wlzy_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.NETWORK_MANAGER)){
//						wlzg_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.OUTCALL_SPEC)){
//						whzy_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.OUTCALL_MANAGER)){
//						whzg_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CONSULTOR)){
//						zxs_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.BREND_MERKETING_DIRECTOR)){
//						scjl_size ++;
//					}
//					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CAMPUS_OPERATION_DIRECTOR)){
//						xqyyzr_size ++;
//					}
//				}
//			}
//			isWLZY =false;isWLZG =false;isWHZY =false;isWHZG =false;isZXS =false;isSCJL =false;isXQYYZR =false;
//			if(wlzy_size>0){
//				isWLZY =true;
//			}
//			if(wlzg_size>0){
//				isWLZG =true;
//			}
//			if(whzy_size>0){
//				isWHZY = true;
//			}
//			if(whzg_size>0){
//				isWHZG = true;
//			}
//			if(zxs_size>0){
//				isZXS = true;
//			}
//			if(scjl_size>0){
//				isSCJL = true;
//			}
//			if(xqyyzr_size>0){
//				isXQYYZR = true;
//			}
//		
//			String roleSign = userService.getUserRoleSign(userId);			
//			customers = customerService.getCustomersByUserId(userId, roleSign);	
//			if(isWLZY||isWLZG){
//				//网络主管 或者网络专员 释放客户到网络资源池
//				resourcePool = networkPool;
//				if(resourcePool!=null && customers!=null){
//	                for(Customer customer:customers){
//                         releaseCustomer(userId, customer, resourcePool,remark,CustomerEventType.INVALIDUSER_RELEASE);
//	                }					
//				}				
//			}
//			if(isZXS||isXQYYZR){
//				//咨询师释放到所属校区资源池
//				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.CAMPUS.getValue());
//				if(resourcePool!=null && customers!=null){
//	                for(Customer customer:customers){
//                         releaseCustomer(userId, customer, resourcePool,remark,CustomerEventType.INVALIDUSER_RELEASE);
//	                }					
//				}				
//				
//			}
//			if(isWHZG||isWHZY){
//				//外呼主管或者外呼专员  释放到所属部门的外呼资源池
//				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.DEPARTMENT.getValue());
//				if(resourcePool!=null && customers!=null){
//	                for(Customer customer:customers){
//                         releaseCustomer(userId, customer, resourcePool,remark,CustomerEventType.INVALIDUSER_RELEASE);
//	                }					
//				}							
//			}
//			if(isSCJL){
//				//回到分公司资源池
//				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.BRENCH.getValue());
//				if(resourcePool!=null && customers!=null){
//	                for(Customer customer:customers){
//                         releaseCustomer(userId, customer, resourcePool,remark,CustomerEventType.INVALIDUSER_RELEASE);
//	                }					
//				}
//			}
//		}	
//	}
//	
//	private void releaseCustomer(String userId,Customer customer,ResourcePool resourcePool,String remark,CustomerEventType eventType){
//    	//customer.setBeforeDeliverTarget(userId);
//    	customer.setModifyTime(DateTools.getCurrentDateTime());
//    	customer.setModifyUserId("system");
//    	customer.setDeliverTarget(resourcePool.getOrganizationId());
//		customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
//		customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
//		customer.setDeliverTime(DateTools.getCurrentDateTime());
//		customerService.updateCustomer(customer);	
//		//添加分配人变动记录表
//		DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
//		record.setCustomerId(customer.getId());
//		record.setPreviousTarget(userId);
//		record.setCurrentTarget(resourcePool.getOrganizationId());
//		record.setRemark(remark);
//		record.setCreateUserId("system");
//		record.setCreateTime(DateTools.getCurrentDateTime());
//		String recordId =deliverTargetChangeService.saveDeliverTargetChangeRecord(record);	
//		
//
//        CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
//        dynamicStatus.setDynamicStatusType(eventType);
//        dynamicStatus.setDescription(remark);
//        if(customer.getResEntrance()!=null){
//               dynamicStatus.setResEntrance(customer.getResEntrance());
//        }
//        dynamicStatus.setStatusNum(1);
//		dynamicStatus.setTableName("delivertarget_change_record");
//        dynamicStatus.setTableId(recordId);
//        dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
//        customerEventService.addCustomerDynameicStatus(customer, dynamicStatus);
//	}
//	
//	@Scheduled(cron="0 */1 * * * ?") //0 0 6 * * ? 半夜三点  0 */1 * * * ?一分钟一次 0/1 * * * * ?
//	@Override
//	public void releaseCustomerByChangeCampus() {
//		ResourcePool resourcePool = null;
//		List<Customer> customers = null;
//		String remark="用户更换校区释放客户资源";
//		//JedisUtil.rpoplpush("invalidUserInfo".getBytes(), "temp-invalidUserInfo".getBytes());
//		byte[] object = JedisUtil.rpop("changeCampusUserInfo".getBytes());
//		if (object != null) {
//			Object value = JedisUtil.ByteToObject(object);
//			
//			String[] strings =value.toString().split("&");
//			String userId = strings[0];
//			String orgId = strings[1];
//			
//			String roleSign = userService.getUserRoleSign(userId);			
//			customers = customerService.getCustomersByUserId(userId, roleSign);	
//			
//			resourcePool = resourcePoolService.findResourcePoolById(orgId);
//			if(resourcePool!=null && customers!=null){
//                for(Customer customer:customers){
//                     releaseCustomer(userId, customer, resourcePool,remark,CustomerEventType.CHANGECAMPUS_RELEASE);
//                }					
//			}				
//		}
//	}
//	
//	public static void main(String[] args) {
//		String userId ="USE0000018094&ORG1234";
//		String[] uStrings =userId.split("&");
//		System.out.println(uStrings[0]);
//		System.out.println(uStrings[1]);
////		JedisUtil.lpush("invalidUserInfo".getBytes(), JedisUtil.ObjectToByte(userId));	
////		System.out.println("成功");
//		//JedisUtil.rpoplpush("invalidUserInfo".getBytes(), "temp-invalidUserInfo".getBytes());
////		byte[] object = JedisUtil.rpop("invalidUserInfo".getBytes());
////		if (object != null) {
////			Object value = JedisUtil.ByteToObject(object);
////			System.out.println(value);
////		}
//	}
	

}
