package com.pad.service;


import com.eduboss.dto.Response;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.entity.CmsMenuIco;
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
public interface CmsMenuIcoService {

    /**
     * 批量保存图标信息。
     * @param vo
     * @return
     */
    Response saveCmsMenuIco(CmsMenuIcoVo vo);

    /**
     * 发布图标
     * @param vo
     * @return
     */
    Response publicMenuIco(CmsMenuIcoVo vo);

    /**
     * 获取图标列表
     * @param vo
     * @return
     */
    List<CmsMenuIcoVo> getMenuIcoList(CmsMenuIcoVo vo);
}
