package com.eduboss.dao;

import com.eduboss.domain.Organization;
import com.eduboss.domain.OrganizationHrms;
import com.eduboss.domainVo.OrganizationSearchDto;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrganizationHrmsDao extends GenericDAO<OrganizationHrms, String> {

    List getOrganizationByCondition(OrganizationSearchDto dto);

    List<OrganizationHrms> getOrgByParentId(String parentId);

    /**
     * 获取人事map组织架构信息
     * @param id
     * @return
     */
    List getOrganizationById(String id);
}
