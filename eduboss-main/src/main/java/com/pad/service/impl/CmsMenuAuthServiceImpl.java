package com.pad.service.impl;

import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.pad.dao.CmsMenuAuthDao;
import com.pad.dao.CmsMenuDao;
import com.pad.dto.CmsMenuAuthVo;
import com.pad.entity.CmsMenu;
import com.pad.entity.CmsMenuAuth;
import com.pad.service.CmsMenuAuthService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service
public class CmsMenuAuthServiceImpl  implements CmsMenuAuthService {

    Logger log = Logger.getLogger(CmsMenuAuthServiceImpl.class);

    @Autowired
    private CmsMenuAuthDao cmsMenuAuthDao;

    @Autowired
    private CmsMenuDao cmsMenuDao;

    @Override
    public Response saveMultiAuth(List<CmsMenuAuthVo> auths,Integer menuId) {
        Response res = new Response();
        if(auths.size()>0){
            Map<String,Integer> authKeys=new HashMap();
            for (CmsMenuAuthVo auth:auths){
                authKeys.put(auth.getOrgId(),auth.getCmsMenuId());
            }

            List<CmsMenuAuth> menu = cmsMenuAuthDao.findMenuAuthByMenuId(menuId);

            for(Iterator it =menu.iterator();it.hasNext();){
                CmsMenuAuth me = (CmsMenuAuth)it.next();
                if(authKeys.get(me.getOrgId())==null){
                    cmsMenuAuthDao.delete(me);
                    it.remove();
                }else{
                    authKeys.remove(me.getOrgId());
                }
            }

            for (String key : authKeys.keySet()){
                CmsMenuAuth info = new CmsMenuAuth();
                info.setCmsMenuId(authKeys.get(key));
                info.setOrgId(key);
                cmsMenuAuthDao.save(info);
            }
        }else{
            deleteAuthByMenuId(menuId);
            log.info("修改了权限范围，没有一个权限。");
            res.setResultMessage("没有修改权限数据");
        }

        return res;
    }

    @Override
    public void deleteAuthByMenuId(Integer id) {
        List<CmsMenuAuth> oldAuths = cmsMenuAuthDao.findMenuAuthByMenuId(id);
        cmsMenuAuthDao.deleteAll(oldAuths);
    }
}
