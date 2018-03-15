package com.eduboss.dao.impl;


import com.eduboss.dao.OrganizationFileDao;
import com.eduboss.domain.OrganizationFile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public class OrganizationFileDaoImpl extends GenericDaoImpl<OrganizationFile,String> implements OrganizationFileDao {

    @Override
    public List<OrganizationFile> getAllContentFileByOrgId(String orgId) {
        String hql =" from OrganizationFile where organizationId=:orgId";
        Map map = new HashMap();
        map.put("orgId",orgId);
        return this.findAllByHQL(hql,map);
    }
}