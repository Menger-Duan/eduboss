package com.eduboss.dao;


import com.eduboss.domain.OrganizationFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public interface OrganizationFileDao extends GenericDAO<OrganizationFile,String> {

    List<OrganizationFile> getAllContentFileByOrgId(String orgId);

}