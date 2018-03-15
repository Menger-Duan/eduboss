package com.pad.service;


import com.eduboss.dto.Response;
import com.pad.dto.CmsMenuAuthVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service
public interface CmsMenuAuthService {


    Response saveMultiAuth(List<CmsMenuAuthVo> auths,Integer menuId);

    void deleteAuthByMenuId(Integer id);
}
