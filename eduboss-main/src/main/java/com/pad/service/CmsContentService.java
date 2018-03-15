package com.pad.service;


import com.eduboss.domainVo.DataDictVo;
import com.eduboss.dto.MessagePushVo;
import com.eduboss.dto.Response;
import com.pad.dto.*;
import com.pad.entity.CmsContent;
import com.pad.entity.CmsContentAuth;
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
public interface CmsContentService {
    Response saveInfo(CmsContentVo cmsContent);

    /**
     * 保存标签信息
     * @param vo
     * @return
     */
    Response saveCmsSign(CmsSignVo vo);

    Response saveCmsContentSign(CmsContentSignVo vo);

    List<CmsSignVo> getCmsSignList(CmsSignVo vo);

    Response deleteCmsContentFile(Integer id);

    List<CmsContentVo> getCmsContentList(CmsContentVo vo,CommonDto dto);

    List<CmsContentVo> getIpadCmsContentList(CmsContentVo vo,CommonDto dto);

    Response saveCmsContentOrder(List<CmsContentVo> cmsContentVos);

    Response saveCmsCotentAuth(CmsContentVo cmsContentVo);

    Response saveMultiAuth(List<CmsContentAuth> auths, Integer contentId);

    void deleteAuthByContentId(Integer id);

    Response publishContent(List<CmsContentVo> cmsContentVo);

    Response publishContent(String id);

    Response deleteContent(Integer id);

    Response pushContentToFront(Integer id);

    Response cacelContentToFront(Integer id);

    Response copyContent(CmsCopyContentDto dto);

    CmsContentVo getCmsContentById(Integer id);

    Response deleteCmsSign(Integer id);

    Response saveMultiCmsSign(List<CmsSignMultiVo> cmsSignVos);

    Response deleteSignById(DataDictVo dataDict);

    Response addContentVisitNum(Integer id);
}
