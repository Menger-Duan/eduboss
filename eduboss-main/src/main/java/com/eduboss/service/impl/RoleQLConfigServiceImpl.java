package com.eduboss.service.impl;

import java.util.*;
import java.util.regex.Matcher;

import com.eduboss.common.*;
import com.eduboss.dao.UserOrganizationRoleDao;
import com.eduboss.domain.*;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.service.OrganizationService;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import javafx.beans.binding.StringBinding;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.RoleQLConfigDao;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.RoleQLConfigVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;

/**
 * Created by Administrator on 2014/12/11.
 */
@Service
public class RoleQLConfigServiceImpl implements RoleQLConfigService{

    private Logger log = Logger.getLogger(RoleQLConfigServiceImpl.class);

    @Autowired
    private RoleQLConfigDao roleQLConfigDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserOrganizationRoleDao userOrganizationRoleDao;

    @Override
    public void save(RoleQLConfig roleQLConfig) {
        roleQLConfigDao.save(roleQLConfig);
    }

    @Override
    public void delete(RoleQLConfig roleQLConfig) {
        roleQLConfigDao.delete(roleQLConfig);
    }

    @Override
    public DataPackage findPage(DataPackage dp, RoleQLConfigVo roleQLConfigVo) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        if(roleQLConfigVo.getExpressions() != null){
            for(String expression : roleQLConfigVo.getExpressions()){
                criterions.add(roleQLConfigDao.buildCriterion(expression));
            }
        }
        List<Order> orders = new ArrayList<Order>();
        return roleQLConfigDao.findPageByCriteria(dp,orders,criterions);
    }

    @Override
    public RoleQLConfig findById(String id) {
        return roleQLConfigDao.findById(id);
    }


    @Override
    public String getAllOrgAppendSql() {
        User currentLoginUser = userService.getCurrentLoginUser();
        List<UserOrganizationRole> orgRoleList=userOrganizationRoleDao.findAllOrgRoleByUserId(currentLoginUser.getUserId());
        StringBuilder returnString = new StringBuilder(" (select id from Organization where 1=2 ");
        for(UserOrganizationRole orgRole:orgRoleList) {
                returnString.append(" or  orgLevel like  '"+orgRole.getOrganization().getOrgLevel()+"%'");
        }
        returnString.append(" )");
        return returnString.toString();
    }



    @Override
    public List<RoleQLConfig> findAllByNameAndType(String name, String type) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        if(StringUtils.isNotBlank(name)) {
            criterions.add(Restrictions.eq("name", name));
        }
        criterions.add(Restrictions.eq("type",type));
        User currentLoginUser = userService.getCurrentLoginUser();
        List<Role> roles =  currentLoginUser!= null?userService.getCurrentLoginUser().getRole():new ArrayList<Role>();
        List<String> roleIds = new ArrayList<String>();
        Boolean flag =false;//是否紧急状态
        if (currentLoginUser!=null){
            //查看用户是不是处于紧急状态
            List<Organization> orgs =userService.findOrganizationByUserId(currentLoginUser.getUserId());
            flag = userService.isUserStateOfEmergency(orgs);
        }
        for(Role role : roles){
            roleIds.add(role.getRoleId());
            if (flag){
                //紧急状态  加上特殊角色
                if (role.getRoleCode() == RoleCode.STUDY_MANAGER){//学管师
//                specialRoleIds.append("'specialRole',");//校区管理学管
                    roleIds.add("specialRole");
                }
                if (role.getRoleCode() == RoleCode.CAMPUS_DIRECTOR) {//校区主任
//                specialRoleIds.append("'specialRole1',");//校区管理主任
                    roleIds.add("specialRole1");
                }
                if (role.getRoleCode() == RoleCode.BREND_MENAGER) {//分公司总经理
//                specialRoleIds.append("'specialRole2',");//分公司管理经理
                    roleIds.add("specialRole2");
                }
            }
        }
        if (roleIds.size() > 0) {
            criterions.add(Restrictions.disjunction().add(Restrictions.in("roleId", roleIds)).add(Restrictions.isNull("roleId")).add(Restrictions.eq("roleId","")));
        }

        List<RoleQLConfig> configs =  roleQLConfigDao.findAllByCriteria(criterions);
        roleQLConfigDao.getHibernateTemplate().evict(configs);
        // 当前用户是否拥有未进行权限配置的角色
        boolean hasUnconfiguredRole = false;
        for(Role role : roles){
            boolean thisRoleHasConfigs = false;
            for(RoleQLConfig config : configs){  // laoshi ,  xueguan
                if(role.getRoleId().equals(config.getRoleId())){
                    thisRoleHasConfigs = true;
                    break;
                }
            }
            if(!thisRoleHasConfigs){
                hasUnconfiguredRole = true;
                break;
            }
        }
        Iterator<RoleQLConfig> it = configs.iterator();
        while(it.hasNext()){
            RoleQLConfig config = it.next();
            // 当前用户没有未进行权限配置的角色，则跳过所有isOtherRole的配置
            roleQLConfigDao.getHibernateTemplate().evict(config);
            if(!hasUnconfiguredRole && config.getIsOtherRole() == BaseStatus.TRUE){
                it.remove();
                continue;
            }
            // 值表达式解析 ${user.userId}
            Matcher matcher = elExpressionPattern.matcher(config.getValue());
            while(matcher.find()){
                String group = matcher.group();
                String key = matcher.group(1);
                if("isForbidden".equals(key) && getExpressionValue(key)==null){
                    //紧急状态用户学生取数
                    it.remove();
                    continue;
                }else{
                    config.setValue(config.getValue().replace(group, getExpressionValue(key).toString()));
                }
            }
        }

        return configs;
    }

    /**
     * 根据key返回当前权限配置值并按照and或or拼接
     * and直接
     *
     * @param name
     * @return
     */
    @Override
    public String getValueResult(String name,String type) {
        StringBuffer result = new StringBuffer();
        StringBuffer or = new StringBuffer();
        List<RoleQLConfig> configs = findAllByNameAndType(name, type);
        for(RoleQLConfig config : configs){
            if("and".equals(config.getJoiner())){
                result.append(" and ").append(config.getValue());
            }else{
                or.append(" or ").append(config.getValue());
            }
        }
        if(or.length() > 0) {
            result.append(" and (").append(or.toString().replaceFirst("or", "")).append(")");
        }
        return result.toString();
    }

    /***************************************************新权限**************************************************************/

    public StringBuilder getAppendSql(Map<String,RoleQLConfig> configMap,String roleId,StringBuilder returnString,Map map){
        if(configMap.get(roleId)!=null){
            RoleQLConfig rql= configMap.get(roleId);
            String realSql = rql.getValue();
            Matcher matcher = elExpressionPattern.matcher(rql.getValue());
            while(matcher.find()){
                String group = matcher.group();
                String key = matcher.group(1);
                if( map.get(key)==null){
                    log.error(key+"值不存在！！！有问题。");
                    continue;
                }
                realSql= realSql.replace(group, map.get(key).toString());
            }
            returnString.append(realSql);
        }
        return returnString;
    }

    /**
     * 根据key返回当前权限所有配置
     * @param vo   搜索对象
     * @param keyMap 替换数据库中的配置参数
     * @return
     */
    @Override
    public String findAllByNameAndType(RoleQLConfigSearchVo vo,Map keyMap) {
        /**
         * 获取对应的sql配置
         * 转换成MAP 用来排查Role存不存在
         */
        //RoleQLConfigSearchVo vo = new RoleQLConfigSearchVo(name,type,resourceId);
        List<RoleQLConfig> configs =  roleQLConfigDao.findRoleQlByCondition(vo);
        roleQLConfigDao.getHibernateTemplate().evict(configs);
        Map<String,RoleQLConfig> configMap = new HashMap<>();
        for(RoleQLConfig config:configs){
            roleQLConfigDao.getHibernateTemplate().evict(config);
            if(StringUtils.isNotBlank(config.getRoleId())){
                configMap.put(config.getRoleId(),config);
            }
        }


        User currentLoginUser = userService.getCurrentLoginUser();

        keyMap.put("userId",currentLoginUser.getUserId());

        List<UserOrganizationRole> orgRoleList=userOrganizationRoleDao.findAllOrgRoleByUserId(currentLoginUser.getUserId());
        Boolean flag =false;//是否紧急状态
        Boolean roleFlag =false;//是否有角色。
        StringBuilder returnString = new StringBuilder(" and ( 1=2 ");
        Map<String,RoleCode> roleCodeMap=new HashMap<>();

        for(UserOrganizationRole orgRole:orgRoleList) {
            /******判断是否有紧急状态的组织架构*****/
            if (!flag && orgRole.getOrganization().getStateOfEmergency() == StateOfEmergency.EMERGENCY) {
                flag = true;
            }
            /**
             * 判断是否有紧急角色
             */
            if (!roleFlag && (configMap.get("specialRole") != null
                                || configMap.get("specialRole1") != null
                                || configMap.get("specialRole2") != null ) &&
                        (orgRole.getRole().getRoleCode() == RoleCode.STUDY_MANAGER
                                || orgRole.getRole().getRoleCode() == RoleCode.CAMPUS_DIRECTOR
                                || orgRole.getRole().getRoleCode() == RoleCode.BREND_MENAGER)
                    ) {
                        roleFlag=true;
                        }




            if (configMap.get(orgRole.getRole().getRoleId()) != null) {
                /**
                 * 处理正常的角色Sql
                 */
                RoleQLConfig rql = configMap.get(orgRole.getRole().getRoleId());
                if ("and".equals(rql.getJoiner())) {
                    returnString.append(" and (");
                } else {
                    returnString.append(" or (");
                }

                keyMap.put("orgId",orgRole.getOrganization().getId());
                keyMap.put("orgLevel",orgRole.getOrganization().getOrgLevel());
                if(orgRole.getOrganization().getBelong()!=null) {
                    Organization belong = organizationDao.findById(orgRole.getOrganization().getBelong());
                    keyMap.put("orgLevelBelong",belong.getOrgLevel());
                }

                returnString = getAppendSql(configMap, orgRole.getRole().getRoleId(), returnString,keyMap);

                returnString.append(" )");
            }
        }
        returnString.append(" )");


        /**
         * 处理紧急状态Sql，紧急状态默认为AND
         */
        if (flag && roleFlag) {
                //紧急状态  加上特殊角色
                returnString.append(" and ");
                returnString = getAppendSql(configMap, "specialRole", returnString,keyMap);
        }
        return returnString.toString();
    }

    @Override
    public String getAppendSqlByOrgAndRoleByConfig(RoleQLConfigSearchVo vo, Map keyMap) {
        if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && Constants.ZERO.equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))){
            return findAllByNameAndType(vo,keyMap);
        }else{
           return getValueResult(vo.getName(),vo.getOldType());
        }
    }

    @Override
    public String getAppendSqlByAllOrg(String name,String type,String alias){
        if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && Constants.ZERO.equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))){
            return " and "+alias+" in " +getAllOrgAppendSql();
        }else{
            return getValueResult(name,type);
        }
    }

    /***************************************************新权限END**************************************************************/


    @Override
    public Object getExpressionValue(String key){
        User user=userService.getCurrentLoginUser();


        if("userId".equals(key)){
            return user.getUserId();
        }else if("userName".equals(key)){
            return user.getName();
        }else if("isForbidden".equals(key)){
            //紧急状态用户学生,合同取数
            //查询当前用户是否已经是紧急状态，如果不是就返回null,如果是就返回1=1
            List<Organization> orgs = userService.findOrganizationByUserId(user.getUserId());
            Boolean stateOfEmergencyFlag = userService.isUserStateOfEmergency(orgs);
            if (stateOfEmergencyFlag){
                return "1=1";
            }else {
                return null;
            }
        }else if("blOrganizationId".equals(key)){
            return user.getOrganizationId();
        }else if("blOrgCampusId".equals(key)){
        	return userService.getBelongCampus().getId();
        }else if("blCampusId".equals(key)){
            return userService.getBelongCampus().getOrgLevel();
        }else if("blBrenchId".equals(key)){
            return userService.getBelongBranch().getOrgLevel();
        }else if("blBrench".equals(key)){
            return userService.getBelongBranch().getId();
        }else if("foreachOrganization".equals(key)){
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                sql.append("   (");
                sql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                    sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
                sql.append(" )");
            }
            return sql.toString();
//            return userService.getBelongBrench().getOrgLevel();
        }else if("foreachOrganization2".equals(key)){//用来匹配有两种组织架构要同时兼容的
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                sql.append("   (");
                sql.append(" o2.orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                    sql.append(" or o2.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
                sql.append(" )");
            }
            return sql.toString();
//            return userService.getBelongBrench().getOrgLevel();
        }else if("foreachOrganizationHql".equals(key)) {
        	StringBuffer hql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                hql.append("   (");
                hql.append(" organization.orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                	hql.append(" or organization.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
                hql.append(" )");
            }
            return hql.toString();
        }else if ("courseForOneWeek".equals(key)){
            StringBuffer hql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if (userOrganizations != null && userOrganizations.size()>0){
                Organization org = userOrganizations.get(0);
                hql.append("   (");
                hql.append(" course.blCampusId.orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                    hql.append(" or course.blCampusId.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
                hql.append(" )");
            }
            return hql.toString();
        } else if("managerForMarketConsultStudy".equals(key)){
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                boolean organizationIsDEPARTMENT=false;  //标记 是否全是部门
                int count=0;
                for (Organization o:userOrganizations) {
                    if(o.getOrgType()==OrganizationType.DEPARTMENT)
                    {
                        count++;
                    }
                }
                if(count==userOrganizations.size()){
                    organizationIsDEPARTMENT=true;
                }
                if(organizationIsDEPARTMENT)//如果该组织架构是部门的才需要跳上一级，否则是该组织架构下面的所有
                {
                    Organization org = userOrganizations.get(0);
                    org= organizationDao.findById(org.getParentId());
                    sql.append("   (");
                    sql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
                    for(int i = 1; i < userOrganizations.size(); i++){
                        sql.append(" or o.orgLevel like '").append(organizationDao.findById(userOrganizations.get(i).getParentId()).getOrgLevel()).append("%'");
                    }
                    sql.append(" )");
                }else
                {
                    Organization org = userOrganizations.get(0);
                    sql.append("   (");
                    sql.append(" o.orgLevel like '").append(org.getOrgLevel()).append("%'");
                    for(int i = 1; i < userOrganizations.size(); i++){
                        sql.append(" or o.orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                    }
                    sql.append(" )");

                }
            }
            return sql.toString();
        }else if("eachOrganizationCampus".equals(key)){//所有所属组织架构
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                sql.append(" select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                    sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
            }else{
            	return userService.getBelongCampus().getOrgLevel();
            }
            return sql.toString();
        }else if("eachOrganizationBrench".equals(key)){//所有所属组织架构分公司
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationService.findAllBrenchByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                sql.append(" select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%'");
                for(int i = 1; i < userOrganizations.size(); i++){
                    sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
                }
            }else{
                return userService.getBelongCampus().getOrgLevel();
            }
            return sql.toString();
        }else if("belongOrganization".equals(key)){//看到组织架构中统计归属所在的组织数据
            StringBuffer sql = new StringBuffer();
            List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
            if(userOrganizations != null && userOrganizations.size() > 0){
                Organization org = userOrganizations.get(0);
                Organization belongOrg=new Organization();
                if(org.getBelong()!=null && StringUtils.isNotBlank(org.getBelong())){
                	belongOrg=organizationDao.findById(org.getBelong());
                	sql.append(" select id from Organization where orgLevel like '").append(belongOrg.getOrgLevel()).append("%'");
                }else{
                	sql.append(" select id from Organization where orgLevel like '").append(org.getOrgLevel()).append("%'");
                }                             
                for(int i = 1; i < userOrganizations.size(); i++){
                	org=userOrganizations.get(i);
                	 if(org.getBelong()!=null && StringUtils.isNotBlank(org.getBelong())){
                		 belongOrg=organizationDao.findById(org.getBelong());
                		 sql.append(" or orgLevel like '").append(belongOrg.getOrgLevel()).append("%'"); 
                	 }else{
                		 sql.append(" or orgLevel like '").append(org.getOrgLevel()).append("%'");
                	 }
                    
                }
            }    
            return sql.toString();
        }else{
            throw new ApplicationException("角色权限配置的值表达式找不到对应key：" + key );
        }
       
    }

    @Override
    public String replaceExpression(String source) {//// FIXME: 2016/6/29 
        Matcher matcher = elExpressionPattern.matcher(source);
        String ret = "";
        while(matcher.find()){
            String group = matcher.group();
            String key = matcher.group(1);
            source = source.replace(group, getExpressionValue(key).toString());
        }
        return source;
    }
}
