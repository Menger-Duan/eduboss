/**
 * 
 */
package com.eduboss.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.domainVo.OrganizationVo;
import com.eduboss.dto.Response;

import com.pad.dto.IpadLoginDto;
import org.springframework.stereotype.Service;

import com.eduboss.common.MobileUserType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PrintType;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.SelectVo;
import com.eduboss.domainVo.StudentSchoolVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.LoginResponse;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.exception.ApplicationException;


/**
 * @classname	CommonService.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-11-13 12:04:19
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
@Service
public interface CommonService {
	
	public LoginResponse login(String userName, String password, MobileUserType mobileUserType) throws ApplicationException;

	public Map<String, String> checkLoginInfoByLoginPlat(String userName, String password,String type);
	
	public DataPackage getSelectOptions(DataDict option, DataPackage dp) throws ApplicationException;
	
	/**
	 * 根据用户名和组织架构父ID返回校区列表,如果orgId为空，返回所有校区
	 */
	public List<Organization> getCapumsByOrgLevel(String userName, String orgId);
	
	/**
	 * 根据id获取组织架构下 指定类型下的所有所有数据
	 * @param orgType 
	 * @param orgId
	 * @return
	 */
	public List<Organization> getOrganizatonByOrgIdAndType(OrganizationType orgType, String orgId);
	
	/**
	 * 根据id获取组织架构下 指定类型下的所有所有数据
	 * @param orgTypes
	 * @param orgId
	 * @return
	 */
	public List<Organization> getOrganizatonByOrgIdAndType(List<OrganizationType> orgTypes, String orgId);
	
	public List<Organization> loadOrganizatonByOrgIdAndType(String orgType, String orgId);
	
	/**
	 * 获取所有分公司
	 * @return
	 */
	public List<Organization> getAllBrench(String organizationId);
	
	
	public List<DataDict> getDataDictByParentId(String parentId);

	public SelectOptionResponse getSelectOption(String selectOptionCategory, String parentId);

	/**
	 * 记录打印信息
	 * @param printType
	 * @param businessId
	 */
	public void recordPrintInfo(PrintType printType, String businessId);
	
	/**
	 * 获取所有的城市
	 * @return
	 */
	public List<DataDict> getDataDictAllCity();
	
	public List<List<Organization>> getBrenchCampusByLimit(String organizationId);
	
	public List<List<Organization>> getCampusByLoginUser();
	
	public SelectOptionResponse getReceptionistFollowUpTargetForSelection() ;

	Map<String, Map> getCampusByLoginUserForMobile();

	public List getReceptionistGroupByCampus(String showBelong,String[] job);

	/**
	 * 获取所有校区
	 * @return
     */
	Map<String,Map> getAllCampus();
	
	/**
	 * 获得用户所属分公司，包括跨校区分公司
	 * @param orgType
	 * @return
	 */
	public List<Organization> getBrenchIncludeOtherCampus(OrganizationType orgType);
	
	public List<StudentSchoolVo> getStuSchools(String blCampus);

	/**
	 *
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	public List<StudentSchoolVo> getStudentSchool(String provinceId, String cityId);


	/**
	 * 专门为找到所属分公司的所有校区
	 * @return
	 */
	List<Organization> selectOrgOptionOfBranch();
    
    /**
     * 根据学生查询所有关联校区
     * @param studentId
     * @return
     */
    List<List<Organization>> getAllStudentCampus(String studentId);
    
    /**
     * 获取当前时间
     * @return
     */
    String getCurrentDateTime();

    
	public List<SelectVo> getBranchCampus();
	
	/**
	 * 获取当前登录者所在分公司的营运经理
	 * @param showBelong
	 * @param job
	 * @return
	 */
	public List getUsersByRoleSign(String showBelong,String[] job);
	
	/**
	 * 获取所有的校区
	 */
	public List getAllCampusForSelection(String orgType);


	/**
	 * 转换组织架构
	 * @param campusByLoginUser
	 * @return
	 */
	List<List<OrganizationVo>> mappingOrganizationVo(List<List<Organization>> campusByLoginUser);

	/**
	 *
	 * @param provinceId
	 * @param cityId
	 * @return
	 */
	List<OrganizationVo> getOrganizationByRegionId(String provinceId, String cityId);

    List<List<Organization>> getCampusByLoginUserNew();

    int sendVerifyCode(String contact,String phoneNum);
    
	Response verifyUserCode(String customerId,String verifyCode, int id);
	
	String getVerification(HttpServletRequest request, String phone);
	/**
	 * 判断用户是否是咨询师、营主
	 * @author: duanmenrun
	 * @Title: judgeUserIdentity
	 * @Description: TODO
	 * @return
	 */
	public Response judgeUserIdentity(String userId);



	/**
	 *  如果一个用户分配了“分公司建班人”的角色，
	 *  那么这个用户的数据权限范围无论是在“分公司”还是在“校区”，
	 *  都可以在小班管理界面建立用户所在分公司所有校区的小班，
	 *  并且可以查看其所在分公司的所有校区的小班
	 * @return
	 */
	List<List<Organization>> getCampusForSmallClassCourse();

    IpadLoginDto ipadLogin(String account, String passwordMd5);
}
