package com.pad.service.impl;

import com.eduboss.dto.Response;
import com.pad.dao.UserIpadPwdDao;
import com.pad.entity.UserIpadPwd;
import com.pad.service.UserIpadPwdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service
public class UserIpadPwdServiceImpl implements UserIpadPwdService {


    @Autowired
    private UserIpadPwdDao userIpadPwdDao;



    @Override
    public Response setNewPadPassword(String userId, String password) {
        Response res = new Response();
        UserIpadPwd pwd = userIpadPwdDao.findById(userId);
        if(pwd!=null){
            pwd.setPwd(password);
        }else{
            pwd=new UserIpadPwd();
            pwd.setUserId(userId);
            pwd.setPwd(password);
        }
        userIpadPwdDao.save(pwd);
        res.setResultMessage("设置成功！");
        return res;
    }

    @Override
    public Response checkPadPwd(String userId, String password) {
        Response res = new Response();
        UserIpadPwd pwd = userIpadPwdDao.findById(userId);
        if(pwd==null){
            res.setResultCode(-1);
            res.setResultMessage("找不到对应的密码信息，请先设置密码！");
            return res;
        }

        if(password.equals(pwd.getPwd())){
            res.setResultMessage("校验成功！");
        }else{
            res.setResultCode(-1);
            res.setResultMessage("校验失败！");
        }
        return res;
    }
}
