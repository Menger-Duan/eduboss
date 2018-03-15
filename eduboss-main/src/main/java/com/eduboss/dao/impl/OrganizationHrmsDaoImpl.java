package com.eduboss.dao.impl;

import com.eduboss.dao.OrganizationHrmsDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OrganizationHrms;
import com.eduboss.domainVo.OrganizationSearchDto;
import com.eduboss.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrganizationHrmsDaoImpl extends GenericDaoImpl<OrganizationHrms, String> implements OrganizationHrmsDao {
	private static final Logger log = LoggerFactory.getLogger(OrganizationHrmsDaoImpl.class);


    @Override
    public List getOrganizationByCondition(OrganizationSearchDto dto) {
        Map<String, Object> params = new HashMap<String, Object>();

        StringBuilder sql = new StringBuilder("select hrms.id,(case when o.`name` is not null then o.`name` else hrms.name end ) name,hrms.parent_Id parentId,hrms.org_Level orgLevel,hrms.org_type orgType ");
        sql.append(" ,(case when o.boss_use is not null and o.boss_use=0 then 0 else 1 end ) bossUse,hrms.id hrms_id,hrms.status");
        sql.append(" from organization_hrms hrms left join organization o  on hrms.id = o.hrms_id WHERE 1=1");

        if(StringUtils.isNotBlank(dto.getParentId())){
            sql.append(" and hrms.parent_ID= :parentId");
            params.put("parentId",dto.getParentId());
        }

        if(StringUtils.isNotBlank(dto.getOrgTypes())){
            String[] types = StringUtil.replaceSpace(dto.getOrgTypes()).split(",");
            sql.append(" and  hrms.org_Type in (:types)");
            params.put("types", types);
        }

        if(StringUtils.isNotBlank(dto.getName())){
            sql.append(" and hrms.name like :name");
            params.put("name","%"+dto.getName()+"%");
        }

        if(StringUtil.isNotBlank(dto.getId())){
            sql.append(" or hrms.id = :hrmsId");
            params.put("hrmsId",dto.getId());
        }

        if(dto.getLevel()!=null && dto.getLevel()>0){
            sql.append(" and length(hrms.org_Level)<="+dto.getLevel()*4);
        }

        sql.append(" order by length(hrms.org_Level), hrms.org_Order,hrms.name");
        return this.findMapBySql(sql.toString(), params);
    }


    @Override
    public List<OrganizationHrms> getOrgByParentId(String parentId) {
        String sql = "select * from organization_hrms where parent_id = :parentId";
        Map param = new HashMap();
        param.put("parentId",parentId);

        return this.findBySql(sql,param);
    }

    @Override
    public List getOrganizationById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select hrms.id as hrmsId,o.id as bossId ,");
        sql.append(" o.`name` as bossName,hrms.`name` as hrmsName,");
        sql.append(" hrms.org_type as orgType,p.name as parentName,");
        sql.append(" hrms.sub_org_type as subOrgType,o.boss_use bossUse,");
        sql.append(" o.remark,hrms.status");
        sql.append(" from  organization_hrms hrms ");
        sql.append(" left join organization_hrms p on hrms.parent_id = p.id");
        sql.append(" left join  organization o  on hrms.id = o.hrms_id");
        sql.append(" where hrms.id = :hrmsId");
        Map param = new HashMap();
        param.put("hrmsId",id);

        return this.findMapBySql(sql.toString(),param);
    }
}
