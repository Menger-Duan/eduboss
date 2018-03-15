package com.pad.service;


import com.eduboss.dto.Response;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service
public interface UserIpadPwdService {

    Response setNewPadPassword(String userId, String password);

    Response checkPadPwd(String userId, String password);
}
