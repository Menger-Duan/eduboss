package com.eduboss.service.impl.rpc;

import org.boss.rpc.base.dto.UserRpcVo;
import org.boss.rpc.eduboss.service.UserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.User;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;

@Service("userRpcImpl")
public class UserRpcImpl implements UserRpc {
	
	@Autowired
	private UserService userService;

	@Override
	public UserRpcVo findUserByAccount(String account) {
		UserRpcVo userRpc = null;
		User user = userService.findUserByAccount(account);
		if (user != null) {
			userRpc = HibernateUtils.voObjectMapping(user, UserRpcVo.class);
			userRpc.setRelateUserNo(user.getUserId());
		}
		return userRpc;
	}

}
