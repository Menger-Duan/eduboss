package com.eduboss.dao;

import com.eduboss.domain.UserModifyLog;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserModifyLogDao extends GenericDAO<UserModifyLog, String> {
	List<UserModifyLog> findAllByUserId(String userId);
}
