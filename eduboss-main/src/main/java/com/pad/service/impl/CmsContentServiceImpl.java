package com.pad.service.impl;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.DataDictVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.DataDictService;
import com.eduboss.service.UserService;
import com.eduboss.utils.*;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;
import com.pad.common.CmsUseType;
import com.pad.dao.*;
import com.pad.dto.*;
import com.pad.entity.*;
import com.pad.service.CmsContentService;
import com.pad.service.CmsMenuService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service("CmsContentService")
public class CmsContentServiceImpl implements CmsContentService {

    Logger log = Logger.getLogger(CmsContentServiceImpl.class);

    @Autowired
    private CmsContentDao cmsContentDao;

    @Autowired
    private CmsSignDao cmsSignDao;

    @Autowired
    private CmsContentSignDao cmsContentSignDao;

    @Autowired
    private UserService userService;


    @Autowired
    private CmsContentFileDao cmsContentFileDao;

    @Autowired
    private CmsContentAuthDao cmsContentAuthDao;

    @Autowired
    private CmsMenuService cmsMenuService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private CmsMenuDao cmsMenuDao;

    @Override
    public Response saveInfo(CmsContentVo vo) {
        Response res = new Response();

        if(!checkNameRepeat(vo)){
            res.setResultCode(-1);
            res.setResultMessage("该目录下已存在同名文章，请重新命名！");
            return res;
        }

        CmsContent cmsContent = HibernateUtils.voObjectMapping(vo,CmsContent.class);
        if(vo.getId()==null || vo.getId()<=0){
            cmsContent.setCreateUser(userService.getCurrentLoginUserId());
        }
        cmsContent.setModifyUser(userService.getCurrentLoginUserId());
        cmsContentDao.save(cmsContent);

        if(vo.getSignIds()!=null) {
            res = saveSignByContent(cmsContent, vo.getSignIds());
        }
        if(vo.getType().equals(CmsContentType.DOCUMENT)) {
            saveFileByContent(cmsContent, vo.getFiles());
        }
        return res;
    }

    /**
     * 检查重名
     * @param vo
     * @return
     */
    public Boolean checkNameRepeat(CmsContentVo vo){
        List<CmsContent> list =  cmsContentDao.getCmsContentByParam(vo);
        int size = list.size();
        if(vo.getId()!=null && vo.getId()>0){
            for(CmsContent content:list){
                cmsContentDao.getHibernateTemplate().evict(content);
                if(vo.getId().equals(content.getId())){
                    size--;
                }
            }
        }

        if(size>0){
            return false;
        }
        return true;
    }


    /**
     * 保存标签
     * @param cmsContent
     * @param signIds
     * @return
     */
    public Response saveSignByContent(CmsContent cmsContent,List<Integer> signIds){
        Response res = new Response();
        List<CmsContentSign> list = cmsContentSignDao.findAllByContentId(cmsContent.getId());

        Map<Integer,Integer> signIdMap = new HashMap();


        for(Integer signid:signIds){
            signIdMap.put(signid,signid);
        }
        for(CmsContentSign sign: list){
            if(signIdMap.get(sign.getCmsSignId())!=null){
                signIdMap.remove(sign.getCmsSignId());
            }else{
                cmsContentSignDao.delete(sign);
            }
        }

        for (Integer key : signIdMap.keySet()){
            CmsContentSignVo vo = new CmsContentSignVo();
            vo.setCmsSignId(key);
            vo.setCmsContentId(cmsContent.getId());
            saveCmsContentSign(vo);
        }

        return res;
    }

    /**
     * 保存内容文档
     * @param cmsContent
     * @param files
     * @return
     */
    public Response saveFileByContent(CmsContent cmsContent,List<CmsContentFileVo> files){
        Response res = new Response();

        List<CmsContentFile> list = cmsContentFileDao.getAllContentFileByContentId(cmsContent.getId());

        Map<Integer,Integer> newMap = new HashMap();
        for (CmsContentFileVo vo : files){
            if(vo.getId()!=null && vo.getId()>0){
                newMap.put(vo.getId(),vo.getId());
            }else {
                CmsContentFile file = HibernateUtils.voObjectMapping(vo, CmsContentFile.class);
                file.setCmsContentId(cmsContent.getId());
                file.setCreateUser(userService.getCurrentLoginUserId());

                /**
                 * 转换在线预览路径
                 */
                String viewUrl = DocumentViewUtil.getDocumentHtmlView(file.getFilePath());
                if(viewUrl!=null){
                    file.setViewUrl(viewUrl);
                }else{
                    throw new ApplicationException("文档转换失败！请重新尝试！");
                }

                cmsContentFileDao.save(file);
            }
        }

        for(CmsContentFile file :list ){
            if(newMap.get(file.getId())==null){
                cmsContentFileDao.delete(file);
            }
        }


        return res;
    }


    @Override
    public Response saveCmsSign(CmsSignVo vo) {
        Response res = new Response();
        CmsSign sign = HibernateUtils.voObjectMapping(vo,CmsSign.class);
        if(sign.getId()==null || sign.getId()==0) {
            sign.setCreateUser(userService.getCurrentLoginUserId());
        }
        cmsSignDao.save(sign);
        return res;
    }

    @Override
    public Response saveCmsContentSign(CmsContentSignVo vo) {
        Response res = new Response();
        CmsContentSign sign = HibernateUtils.voObjectMapping(vo,CmsContentSign.class);
        sign.setCreateUser(userService.getCurrentLoginUserId());
        cmsContentSignDao.save(sign);
        return res;
    }

    @Override
    public List<CmsSignVo> getCmsSignList(CmsSignVo vo) {
        List<CmsSign> list = cmsSignDao.getCmsSignList(vo);
        return HibernateUtils.voListMapping(list,CmsSignVo.class);
    }

    @Override
    public Response deleteCmsContentFile(Integer id) {
        Response res = new Response();
        CmsContentFile file = cmsContentFileDao.findById(id);
        if(file!=null) {
            cmsContentFileDao.delete(file);
        }else{
            res.setResultMessage("找不到对应的记录！请刷新后再试试！");
            res.setResultCode(-1);
            res.setData(id);
        }
        return res;
    }

    @Override
    public List<CmsContentVo> getCmsContentList(CmsContentVo vo,CommonDto dto) {
        List<CmsContent> list = cmsContentDao.getCmsContentList(vo,dto);
        List<CmsContentVo> returnList = new ArrayList<>();
        for(CmsContent cc:list){
            CmsContentVo cvo = HibernateUtils.voObjectMapping(cc,CmsContentVo.class);
            Map map = cmsMenuService.getFistLevelName(cc.getMenuId());
            if(map != null) {
                cvo.setFmenuId(map.get("id"));
                cvo.setFmenuName(map.get("name"));
            }
            switch (cc.getType())
            {
                case LINK:
                        cvo.setFrontUrl(cvo.getContent());
                    break;
                case DOCUMENT:
                    List<CmsContentFile> fileList=cmsContentFileDao.getAllContentFileByContentId(cc.getId());
                    if(fileList.size()>0){
                        cvo.setFrontUrl(fileList.get(0).getViewUrl());
                    }
                    break;
                default:
                    cvo.setFrontUrl(PropertiesUtils.getStringValue("IPAD_CONTENT_URL") + cvo.getId());
            }

            returnList.add(cvo);
        }

        return returnList;
    }

    @Override
    public List<CmsContentVo> getIpadCmsContentList(CmsContentVo vo, CommonDto dto) {
        if(dto.getFrontId()==null || dto.getFrontId()==0){
            return getCmsContentList(vo,dto);
        }else{//ipad首页轮播图传ID加载。
            List<CmsContentVo> tuiList = getCmsContentList(vo,dto);
            vo.setIsFront(null);
            vo.setMenuId(dto.getFrontId());
            List<CmsContentVo> frontList = getCmsContentList(vo,dto);
            tuiList.addAll(frontList);
            return tuiList;
        }
    }

    @Override
    public Response saveCmsContentOrder(List<CmsContentVo> cmsContentVos) {
        Response res = new Response();
        for(CmsContentVo vo :cmsContentVos){
            if(vo.getId()==null || vo.getId()<=0){
                res.setResultCode(-1);
                res.setResultMessage("参数有误，id未传！");
                return res;
            }

            CmsContent content = cmsContentDao.findById(vo.getId());
            content.setOrderNum(vo.getOrderNum());
            cmsContentDao.save(content);
        }
        return res;
    }

    @Override
    public Response saveCmsCotentAuth(CmsContentVo cmsContentVo) {
        Response res = new Response();

        if(cmsContentVo.getId()==null && cmsContentVo.getId()<=0){
            res.setResultCode(-1);
            res.setResultMessage("传入ID有误");
            res.setData(cmsContentVo);
            return res;
        }

        CmsContent cmsContent = cmsContentDao.findById(cmsContentVo.getId());
        cmsContent.setAllCanSee(cmsContentVo.getAllCanSee());
        if(cmsContentVo.getAllCanSee()!=null && cmsContentVo.getAllCanSee()==1){
            res = saveMultiAuth(cmsContentVo.getAuths(),cmsContentVo.getId());
        }else{
            deleteAuthByContentId(cmsContentVo.getId());
        }

        return res;
    }


    @Override
    public Response saveMultiAuth(List<CmsContentAuth> auths, Integer contentId) {
        Response res = new Response();
        if(auths.size()>0){
            Map<String,Integer> authKeys=new HashMap();
            for (CmsContentAuth auth:auths){
                authKeys.put(auth.getOrgId(),auth.getCmsContentId());
            }

            List<CmsContentAuth> content = cmsContentAuthDao.findContentAuthByContentId(contentId);

            for(Iterator it = content.iterator(); it.hasNext();){
                CmsContentAuth me = (CmsContentAuth)it.next();
                if(authKeys.get(me.getOrgId())==null){
                    cmsContentAuthDao.delete(me);
                    it.remove();
                }else{
                    authKeys.remove(me.getOrgId());
                }
            }

            for (String key : authKeys.keySet()){
                CmsContentAuth info = new CmsContentAuth();
                info.setCmsContentId(authKeys.get(key));
                info.setOrgId(key);
                cmsContentAuthDao.save(info);
            }
        }else{
            log.info("修改了权限范围，没有一个权限。");
            res.setResultMessage("没有修改权限数据");
        }

        return res;
    }

    @Override
    public void deleteAuthByContentId(Integer id) {
        List<CmsContentAuth> oldAuths = cmsContentAuthDao.findContentAuthByContentId(id);
        cmsContentAuthDao.deleteAll(oldAuths);
    }



    @Override
    public Response publishContent(List<CmsContentVo> cmsContentVos) {
        Response res = new Response();

        Integer menuId = 0;
        for(CmsContentVo cmsContentVo :cmsContentVos) {
            if (cmsContentVo.getId() == null || cmsContentVo.getId() <= 0) {
                res.setResultCode(-1);
                res.setResultMessage("参数错误");
                res.setData(cmsContentVo);
                return res;
            }

            CmsContent cmsContent = cmsContentDao.findById(cmsContentVo.getId());
            if(menuId==0){
                menuId = cmsContent.getMenuId();
            }

            if (cmsContentVo.getStatus() != null && CmsContentStatus.PUBLISH.equals(cmsContentVo.getStatus())) {
                if (cmsContent.getStatus().equals(CmsContentStatus.PUBLISH)) {
                    continue;//已经发布的不做处理
                }
                cmsContent.setPublishTime(DateTools.getCurrentDateTime());
            } else if (cmsContentVo.getStatus() != null && CmsContentStatus.TO_PUBLISH.equals(cmsContentVo.getStatus())) {

                int days = DateTools.daysBetween(DateTools.getCurrentDate(), cmsContentVo.getAppointmentPublishTime());
                if (days > 30) {
                    res.setResultCode(-1);
                    res.setResultMessage("日期超过30天不能预约发布！");
                    res.setData(cmsContentVo.getAppointmentPublishTime());
                    return res;
                }
                cmsContent.setAppointmentPublishTime(cmsContentVo.getAppointmentPublishTime());

                MessageQueueUtils.postToMq(PropertiesUtils.getStringValue("mq.content.publish.key"), cmsContent.getId().toString(), cmsContentVo.getAppointmentPublishTime());
            } else if (cmsContentVo.getStatus() != null && CmsContentStatus.UN_PUBLISH.equals(cmsContentVo.getStatus())) {
                cmsContent.setAppointmentPublishTime(null);//取消清空预约发布日期
            }
            cmsContent.setStatus(cmsContentVo.getStatus());

            cmsContentDao.save(cmsContent);

        }
        CmsMenu menu = cmsMenuDao.findById(menuId);
        if(menu!=null && menu.getUseType().equals(CmsUseType.VISITOR) && menu.getParentId()!=null && menu.getParentId()==-1){
            CmsContentStatus [] types = {CmsContentStatus.TO_PUBLISH,CmsContentStatus.PUBLISH};
            List<CmsContent> contentList = cmsContentDao.getCmsContentByType(types,menuId);
            if(contentList.size()>8){
                throw new ApplicationException("首页轮播图不能发布超过8个，包括预发布跟发布状态的总数。");
            }
        }
        return res;
    }

    @Override
    public Response publishContent(String id) {
        Response res = new Response();
        CmsContent cmsContent = cmsContentDao.findById(Integer.valueOf(id));
        if(cmsContent!=null && CmsContentStatus.TO_PUBLISH.equals(cmsContent.getStatus()) && cmsContent.getAppointmentPublishTime()!=null){
            int days= DateTools.daysBetween(DateTools.getCurrentDate(),cmsContent.getAppointmentPublishTime());
            if(days!=0){
                return res;
            }
            cmsContent.setStatus(CmsContentStatus.PUBLISH);
            cmsContent.setPublishTime(DateTools.getCurrentDateTime());
            cmsContentDao.save(cmsContent);
        }
        return res;
    }

    @Override
    public Response deleteContent(Integer id) {
        Response res = new Response();

        if(id ==null || id <=0){
            res.setResultCode(-1);
            res.setResultMessage("参数有误！");
            res.setData(id);
            return res;
        }
        CmsContent cmsContent = cmsContentDao.findById(id);
        if(cmsContent.getStatus()!=null && !cmsContent.getStatus().equals(CmsContentStatus.UN_PUBLISH)){
            res.setResultCode(-1);
            res.setResultMessage("只有未发布状态才可以删除！");
            res.setData(id);
            return res;
        }

        List<CmsContentSign> cmsContentSigns= cmsContentSignDao.findAllByContentId(id);
        for(CmsContentSign cmsContentSign :cmsContentSigns){
            cmsContentSignDao.delete(cmsContentSign);
        }

        cmsContent.setIsDel(1);

        return res;
    }

    @Override
    public Response pushContentToFront(Integer id) {
        Response res = new Response();
        if(id ==null || id <=0){
            res.setResultCode(-1);
            res.setResultMessage("参数有误！");
            res.setData(id);
            return res;
        }

        CmsContent cmsContent = cmsContentDao.findById(id);
        res = checkCanPushToFront(cmsContent.getMenuId());

        if(res.getResultCode()!=0){
            return res;
        }
        cmsContent.setIsFront(0);
        cmsContentDao.save(cmsContent);
        return res;
    }

    @Override
    public Response cacelContentToFront(Integer id) {
        Response res = new Response();
        if(id ==null || id <=0){
            res.setResultCode(-1);
            res.setResultMessage("id不能为空或者0！");
            res.setData(id);
            return res;
        }
        CmsContent cmsContent = cmsContentDao.findById(id);
        cmsContent.setIsFront(1);
        cmsContentDao.save(cmsContent);
        return res;
    }

    @Override
    public Response copyContent(CmsCopyContentDto dto) {
        Response res = new Response();
        if(dto.getContentId()==null || dto.getNewMenuId()==null){
            res.setResultMessage("内容ID，跟目录ID必传！");
            res.setResultCode(-1);
            res.setData(dto);
            return res;
        }
        Organization org = userService.getBelongBranch();
        for(Integer id :dto.getContentId()) {
            CmsContent content = cmsContentDao.findById(id);
            for(Integer menuId:dto.getNewMenuId()) {
                if(dto.getNewBrenchId()==null || dto.getNewBrenchId().size()<=0){
                    saveCopyContentInfo(content,menuId,dto,org.getId());
                }else{
                    for(String branchId:dto.getNewBrenchId()){
                        saveCopyContentInfo(content,menuId,dto,branchId);
                    }
                }
            }
        }


        return res;
    }

    @Override
    public CmsContentVo getCmsContentById(Integer id) {
        CmsContent cmsContent = cmsContentDao.findById(id);
        if(cmsContent!=null) {
            CmsContentVo vo = HibernateUtils.voObjectMapping(cmsContent, CmsContentVo.class);
            List<CmsContentSign> signList = cmsContentSignDao.findAllByContentId(vo.getId());
            List<CmsContentAuth> authList = cmsContentAuthDao.findContentAuthByContentId(vo.getId());
            List<CmsContentFile> fileList = cmsContentFileDao.getAllContentFileByContentId(vo.getId());
            vo.setFiles(HibernateUtils.voListMapping(fileList, CmsContentFileVo.class));
            vo.setAuths(authList);
            vo.setSignList(signList);
            vo.setFrontUrl(PropertiesUtils.getStringValue("IPAD_CONTENT_URL")+vo.getId());
            return vo;
        }
        return null;
    }

    @Override
    public Response deleteCmsSign(Integer id) {
        Response res = new Response();

        if(id ==null || id <=0){
            res.setResultCode(-1);
            res.setResultMessage("参数有误！");
            res.setData(id);
            return res;
        }

        CmsSign sign= cmsSignDao.findById(id);

        if(sign==null){
            res.setResultCode(-1);
            res.setResultMessage("找不到对应的记录！");
            res.setData(id);
            return res;
        }

        List<CmsContentSign> signs = cmsContentSignDao.findAllBySignId(id);


        if(signs.size()>0){
            res.setResultCode(-1);
            res.setResultMessage("该标签已经使用，不能删除！");
            res.setData(id);
            return res;
        }

        cmsSignDao.delete(sign);

        return res;
    }

    /**
     * 批量保存标签数据
     * @param cmsSignVos
     * @return
     */
    @Override
    public Response saveMultiCmsSign(List<CmsSignMultiVo> cmsSignVos) {
        Response res = new Response();
        for(CmsSignMultiVo vo :cmsSignVos){
            res = saveCmsSignByType(vo);
            if(res.getResultCode()!=0){
                return res;
            }
        }
        return res;
    }

    @Override
    public Response deleteSignById(DataDictVo dataDict) {
        Response res = new Response();

        if(StringUtils.isBlank(dataDict.getId())){
            res.setResultCode(-1);
            res.setResultMessage("删除ID不能为空！");
            return res;
        }


        DataDict dd = dataDictService.findById(dataDict.getId());

        if(dd==null){
            res.setResultCode(-1);
            res.setResultMessage("找不到对应的标签类型！");
            return res;
        }

        List list = cmsSignDao.getCmsSignList(new CmsSignVo(dataDict.getId()));
        if(list.size()>0){
            res.setResultCode(-1);
            res.setResultMessage("类型下有标签，不能删除！");
            return res;
        }

        dataDictService.deleteDataDict(dd);
        return res;
    }

    @Override
    public synchronized Response  addContentVisitNum(Integer id) {
        Response res = new Response();
        CmsContent content = cmsContentDao.findById(id);
        cmsContentDao.getHibernateTemplate().lock(content, LockMode.UPGRADE);
        if(content.getVisitNum()==null){
            content.setVisitNum(1);
        }else {
            content.setVisitNum(content.getVisitNum() + 1);
        }
        return res;
    }

    /**
     * 批量保存标签数据  按类型
     * @param vo
     * @return
     */
    public Response saveCmsSignByType(CmsSignMultiVo vo){
        Response res = new Response();
        List<CmsSign> list = cmsSignDao.getCmsSignList(new CmsSignVo(vo.getType()));
        Map<Integer,Integer> voIds=new HashMap<>();
        for(CmsSignVo svo:vo.getSignVos()){
            if(svo.getId()!=null && svo.getId()>0) {
                voIds.put(svo.getId(), svo.getId());
            }else{
                saveCmsSign(svo);
            }
        }

        /**
         * 参数不存在的数据，从数据库删掉。
         */
        for(CmsSign sign:list){
            if(voIds.get(sign.getId())!=null){
                continue;
            }
            List<CmsContentSign> conSigns = cmsContentSignDao.findAllBySignId(sign.getId());
            if(conSigns.size()>0){
                res.setResultMessage("标签“"+sign.getValue()+"”还在使用不能删除！");
                res.setResultCode(-1);
                return res;
            }

            if(voIds.get(sign.getId())==null){
                cmsSignDao.delete(sign);
            }
        }

        return res;
    }


    public void saveCopyContentInfo(CmsContent content,Integer menuId,CmsCopyContentDto dto,String newOrgId ){
        CmsContent newContent = createNewContent(content);
        List clist = cmsContentDao.getCmsContentList(new CmsContentVo(menuId));
        newContent.setOrderNum(clist.size());//菜单排序号。
        newContent.setMenuId(menuId);
        newContent.setOrgType(dto.getNewType());
        cmsContentDao.save(newContent);

        if (dto.getNewType() != null && !content.getOrgType().equals(dto.getNewType())) {
            newContent.setAllCanSee(1);//不是所有分公司可见
            CmsContentAuth newAuth = new CmsContentAuth();
            newAuth.setCmsContentId(newContent.getId());
            newAuth.setOrgId(newOrgId);
            cmsContentAuthDao.save(newAuth);
        }else{
            List<CmsContentAuth> list= cmsContentAuthDao.findContentAuthByContentId(content.getId());
            for(CmsContentAuth auth : list){
                CmsContentAuth newAuth = new CmsContentAuth();
                newAuth.setCmsContentId(newContent.getId());
                newAuth.setOrgId(auth.getOrgId());
                cmsContentAuthDao.save(newAuth);
            }
        }

        if(content.getType().equals(CmsContentType.DOCUMENT)){
           List<CmsContentFile> list= cmsContentFileDao.getAllContentFileByContentId(content.getId());
           for(CmsContentFile file :list){
               CmsContentFile newFile = new CmsContentFile();
               newFile.setCmsContentId(newContent.getId());
               newFile.setFileName(file.getFileName());
               newFile.setFilePath(file.getFilePath());
               newFile.setFileId(file.getFileId());
               newFile.setViewUrl(file.getViewUrl());
               newFile.setFileSize(file.getFileSize());
               cmsContentFileDao.save(newFile);
           }
        }
    }


    public CmsContent createNewContent(CmsContent fcontent){
        CmsContent content= new CmsContent();
        content.setIsFront(1);
        content.setStatus(CmsContentStatus.UN_PUBLISH);
        content.setVisitNum(0);
        content.setTitle(fcontent.getTitle()+"副本");
        content.setContent(fcontent.getContent());
        content.setRemark(fcontent.getRemark());
        content.setFacePic(fcontent.getFacePic());
        content.setAllCanSee(fcontent.getAllCanSee());
        content.setCreateUser(userService.getCurrentLoginUserId());
        content.setType(fcontent.getType());
        return content;
    }

    public Response checkCanPushToFront(Integer menuId){
        Response res = new Response();
        List<CmsContent> list = cmsContentDao.findFrontContentByMenu(menuId);
        if(list.size()>=4){
            res.setResultCode(-1);
            res.setResultMessage("该目录已经超过4个首页推荐！");
        }
        return res;

    }


}
