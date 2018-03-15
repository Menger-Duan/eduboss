package com.eduboss.dao;

import com.eduboss.domain.UserOrganizationRole;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserOrganizationRoleDao extends GenericDAO<UserOrganizationRole, String> {


	List<UserOrganizationRole> findAllOrgRoleByUserId(String userId);
}
