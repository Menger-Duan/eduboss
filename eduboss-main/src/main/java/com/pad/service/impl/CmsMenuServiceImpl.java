package com.pad.service.impl;

import com.eduboss.common.OrganizationType;
import com.eduboss.domain.Organization;
import com.eduboss.dto.Response;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.google.gson.Gson;
import com.pad.dao.*;
import com.pad.dto.CmsContentVo;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsBindIco;
import com.pad.entity.CmsMenu;
import com.pad.entity.CmsMenuAuth;
import com.pad.entity.CmsMenuIco;
import com.pad.service.CmsMenuAuthService;
import com.pad.service.CmsMenuService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class CmsMenuServiceImpl implements CmsMenuService {


    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CmsMenuService.class);

    @Autowired
    private CmsMenuDao cmsMenuDao;

    @Autowired
    private UserService userService;

    @Autowired
    private CmsMenuAuthService cmsMenuAuthService;


    @Autowired
    private CmsMenuAuthDao cmsMenuAuthDao;


    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private CmsContentDao cmsContentDao;

    @Autowired
    private CmsMenuIcoDao cmsMenuIcoDao;

    @Autowired
    private CmsBindIcoDao cmsBindIcoDao;

    @Override
    public Response saveCmsMenu(CmsMenuVo cmsMenu) {
        Response res = new Response();
        if(!checkNameRepeat(cmsMenu)){
            res.setResultCode(-1);
            res.setResultMessage("改目录名字已存在，请重新命名！");
            return res;
        }

        CmsMenu menu= HibernateUtils.voObjectMapping(cmsMenu,CmsMenu.class);
        if(cmsMenu.getId()!=null) {
            menu.setModifyUser(userService.getCurrentLoginUserId());
        }else{
            if(StringUtils.isNotBlank(cmsMenu.getLevel()) && "1".equals(cmsMenu.getLevel())){
                menu.setStatus("0");//一级目录默认可见
            }else{
                menu.setStatus("1");//二级目录默认不可见
            }
            menu.setCreateUser(userService.getCurrentLoginUserId());
        }
        cmsMenuDao.save(menu);


        /***
         * 新增目录图标处理
         */
        if(cmsMenu.getId()==null || cmsMenu.getId()==0){
            saveNewCmsMenuIco(menu);
            if(menu.getAllCanSee()!=null && menu.getAllCanSee()==1) {
                saveMenuAuthByParentId(menu.getId(), menu.getParentId());
            }
        }

        return res;
    }

    private void saveMenuAuthByParentId(Integer id, Integer parentId) {
        List<CmsMenuAuth> list =cmsMenuAuthDao.findMenuAuthByMenuId(parentId);
        for(CmsMenuAuth auth:list){
            CmsMenuAuth newAuth = new CmsMenuAuth();
            newAuth.setOrgId(auth.getOrgId());
            newAuth.setCmsMenuId(id);
            cmsMenuAuthDao.save(newAuth);
        }
    }

    /**
     * 检查重名
     * @param vo
     * @return
     */
    public Boolean checkNameRepeat(CmsMenuVo vo){
       List<CmsMenu> list =  cmsMenuDao.getCmsMenuListByParam(vo);


       int size = list.size();
       if(vo.getId()!=null && vo.getId()>0){
            for(CmsMenu menu:list){
                cmsMenuDao.getHibernateTemplate().evict(menu);
                if(vo.getId().equals(menu.getId())){
                    size--;
                }
            }
       }

       if(size>0){
           return false;
       }
       return true;
    }

    private void saveNewCmsMenuIco(CmsMenu menu) {
        String currentUserId = userService.getCurrentLoginUserId();
        CmsMenuIcoVo vo = new CmsMenuIcoVo();
        vo.setStatus("0");

        if(menu.getLevel()!=null && "1".equals(menu.getLevel())){
            vo.setMenuLevel(1);
        }else{
            vo.setMenuId(menu.getParentId());
            vo.setMenuLevel(2);
        }

       List<CmsMenuIco> icos =  cmsMenuIcoDao.findIcoByStatusAndLevel(vo);

        if(icos.size()>0){
            for(CmsMenuIco ico :icos){
                saveMenuIco(ico,menu,currentUserId);
            }
        }else{
            CmsMenuIco menuIco = new CmsMenuIco();
            menuIco.setStatus("0");
            menuIco.setPublishUser(currentUserId);
            menuIco.setVersion(1);
           menuIco.setMenuLevel(vo.getMenuLevel());
           if(vo.getMenuLevel()==2) {
               menuIco.setMenuId(vo.getMenuId());
           }
           menuIco.setCreateUser(currentUserId);

            cmsMenuIcoDao.save(menuIco);
            //保存图标详情
            saveMenuIco(menuIco,menu,currentUserId);

        }

    }

    public void saveMenuIco(CmsMenuIco ico,CmsMenu menu,String currentUserId){
        CmsBindIco bindIco = new CmsBindIco();
        bindIco.setCreateUser(currentUserId);
        bindIco.setIcoId(ico.getId());
        bindIco.setMenuId(menu.getId());
        bindIco.setGrayIco(menu.getGrayIco());
        bindIco.setIcoUrl(menu.getIco());
        cmsBindIcoDao.save(bindIco);
    }

    @Override
    public List<CmsMenuVo> getCmsMenuList(CmsMenuVo cmsMenu) {
        List<CmsMenu> list = cmsMenuDao.getCmsMenuList(cmsMenu);
        List<CmsMenuVo> vos = HibernateUtils.voListMapping(list,CmsMenuVo.class);
        for(CmsMenuVo menu:vos){
            if(menu.getAllCanSee()!=0) {
                List<CmsMenuAuth> auths = cmsMenuAuthDao.findMenuAuthByMenuId(menu.getId());
                String orgId = "";String orgName = "";
                for (CmsMenuAuth auth : auths) {
                    Organization org = organizationService.findById(auth.getOrgId());
                    orgId += org.getId() + ";";
                    orgName += org.getName() + ";";
                }
                if(orgId.length()>0){
                    orgId=orgId.substring(0,orgId.length()-1);
                    orgName=orgName.substring(0,orgName.length()-1);
                }
                menu.setOrgId(orgId);
                menu.setOrgName(orgName);
            }else{
                menu.setOrgName("全部分公司");
            }
        }
        return vos;
    }

    @Override
    public List<CmsMenuVo> getCmsMenuListForIpad(CmsMenuVo cmsMenu) {
        List<CmsMenu> list = cmsMenuDao.getCmsMenuList(cmsMenu);
        List<CmsMenuVo> vos = HibernateUtils.voListMapping(list,CmsMenuVo.class);
        for(CmsMenuVo menu:vos){
            if(org.apache.commons.lang3.StringUtils.isNotBlank(menu.getStatus()) && menu.getStatus().equals("0")) {
                cmsMenu.setParentId(menu.getId());
                cmsMenu.setLevel(null);
                List<CmsMenuVo> sonMenu = getCmsMenuListForIpad(cmsMenu);
                menu.setSonMenu(sonMenu);
            }
        }
        return vos;
    }

    @Override
    public Response saveCmsMenuAuth(CmsMenuVo cmsMenu) {
        Response res = new Response();

        if(cmsMenu.getId()==null || cmsMenu.getId()<=0){
            res.setResultCode(-1);
            res.setResultMessage("传入ID有误");
            res.setData(cmsMenu);
            return res;
        }

        if(cmsMenu.getParentId()!=null && cmsMenu.getParentId()==-1){
            res.setResultCode(-1);
            res.setResultMessage("首页不能修改权限！");
            return res;
        }

        CmsMenu menu = cmsMenuDao.findById(cmsMenu.getId());
        Gson g = new Gson();
        logger.info(menu.getId()+"菜单"+g.toJson(menu));
        menu.setAllCanSee(cmsMenu.getAllCanSee());
        logger.info(menu.getId()+"菜单2"+g.toJson(menu));
        if(cmsMenu.getAllCanSee()!=null && cmsMenu.getAllCanSee()==1){
            res = cmsMenuAuthService.saveMultiAuth(cmsMenu.getAuths(),cmsMenu.getId());
        }else{
            cmsMenuAuthService.deleteAuthByMenuId(cmsMenu.getId());
        }
        logger.info(menu.getId()+"菜单3"+g.toJson(menu));
        cmsMenuDao.save(menu);
        cmsMenuDao.commit();
        res.setData(menu);
        return res;
    }

    @Override
    public Response saveCmsMenuOrder(List<CmsMenuVo> cmsMenu) {
        Response res = new Response();
        for(CmsMenuVo vo :cmsMenu){
            if(vo.getId()==null || vo.getId()<=0){
                res.setResultCode(-1);
                res.setResultMessage("参数有误，id未传！");
                return res;
            }

            CmsMenu menu = cmsMenuDao.findById(vo.getId());
            menu.setOrderNum(vo.getOrderNum());
            cmsMenuDao.save(menu);
        }

        return res;
    }

    @Override
    public List<CmsMenuVo> getCmsMenuByOrgId(String id,Integer level) {
        List<CmsMenu> list = cmsMenuDao.getCmsMenuByOrgId(id,level);
        return HibernateUtils.voListMapping(list,CmsMenuVo.class);
    }

    @Override
    public List<Organization> getAllBrenchAndGroup() {
        Organization org = userService.getBelongBranch();
        if(org.getOrgType().equals(OrganizationType.BRENCH)){
            List<Organization> list = new ArrayList<>();
            list.add(org);
            return list;
        }
        return organizationService.getGroupAndAllBrench();
    }

    @Override
    public Response cacelMenuBySelf(Integer id, String branchId) {
        Response res = new Response();

        CmsMenu menu = cmsMenuDao.findById(id);

        if(menu.getAllCanSee()==0){
            res.setResultCode(-1);
            res.setResultMessage("当前菜单集团设置为所有分公司可见，如果分公司自己要取消可见，需要向集团申请！");
            return res;
        }


        List<CmsMenuAuth> list= cmsMenuAuthDao.findMenuAuthByMenuId(id);
        if(list !=null && list.size()>0) {
            for (CmsMenuAuth auth : list) {
                if (auth.getOrgId().equals(branchId)) {
                    cmsMenuAuthDao.delete(auth);
                }
            }
        }

        return res;
    }

    @Override
    public Response saveCmsMenuContentNum(Integer id, Integer showContentNum) {
        Response res = new Response();

        CmsMenu menu = cmsMenuDao.findById(id);

        if(menu==null){
            res.setResultCode(-1);
            res.setResultMessage("找不到对应的菜单！");
            return res;
        }

        menu.setShowContentNum(showContentNum);
        cmsMenuDao.save(menu);
        return res;
    }

    @Override
    public Response deleteCmsMenu(Integer id) {
        Response res = new Response();
        CmsMenu menu = cmsMenuDao.findById(id);
        if(menu==null){
            res.setResultCode(-1);
            res.setResultMessage("找不到对应的菜单！");
            return res;
        }

        if(menu.getLevel()!=null && "1".equals(menu.getLevel())){
            res.setResultCode(-1);
            res.setResultMessage("1级目录不能删除！");
            return res;
        }

        List list=cmsContentDao.getCmsContentList(new CmsContentVo(id));

        if(list.size()>0){
            res.setResultCode(-1);
            res.setResultMessage("菜单下面有图文信息，不能删除！");
            return res;
        }
        //可见范围删除
        cmsMenuAuthService.deleteAuthByMenuId(id);
        deleteMenuIcos(id);//删除图标信息。
        cmsMenuDao.delete(menu);
        return res;
    }

    private void deleteMenuIcos(Integer id) {
        List<CmsBindIco> bindList =cmsBindIcoDao.findInfoByMenuId(id);
        for(CmsBindIco bindIco : bindList){
            List<CmsBindIco> bindMenuList =cmsBindIcoDao.findAllBindIcoByIcoId(bindIco.getIcoId());
            cmsBindIcoDao.delete(bindIco);

            if(bindMenuList.size()<=1){
                CmsMenuIco menuIco = cmsMenuIcoDao.findById(bindIco.getIcoId());
                cmsMenuIcoDao.delete(menuIco);
            }
        }

    }

    @Override
    public Response setShowMenuContent(CmsMenuVo cmsMenu) {
        Response res = new Response();

        if(StringUtils.isBlank(cmsMenu.getStatus())){
            res.setResultCode(-1);
            res.setResultMessage("参数有误，status不能为空！");
            return res;
        }

        String twoLevelMenuStatus="1";
        if(cmsMenu.getStatus().equals("1")){//一级菜单隐藏  二级菜单显示。
            twoLevelMenuStatus="0";
        }

        CmsMenu menu = cmsMenuDao.findById(cmsMenu.getId());

        if("1".equals(menu.getLevel())){
            menu.setStatus(cmsMenu.getStatus());
            List<CmsMenu> cmsMenuList =cmsMenuDao.getCmsMenuByParentId(menu.getId());
            for(CmsMenu m:cmsMenuList){//二级菜单  显示状态 改变
                m.setStatus(twoLevelMenuStatus);
                cmsMenuDao.save(m);
            }
            cmsMenuDao.save(menu);
        }else{
            CmsMenu parentmenu = cmsMenuDao.findById(menu.getParentId());
            parentmenu.setStatus(twoLevelMenuStatus);
            List<CmsMenu> cmsMenuList =cmsMenuDao.getCmsMenuByParentId(menu.getParentId());
            for(CmsMenu m:cmsMenuList){//二级菜单  显示状态 改变
                m.setStatus(cmsMenu.getStatus());
                cmsMenuDao.save(m);
            }
            cmsMenuDao.save(parentmenu);
        }


        return res;
    }

    @Override
    public List<CmsMenuVo> findMenuByParentId(Integer id) {
        List<CmsMenu> menus = cmsMenuDao.getCmsMenuByParentId(id);
        return HibernateUtils.voListMapping(menus,CmsMenuVo.class);
    }

    @Override
    public Map<Object, Object> getFistLevelName(Integer menuId) {
        List<Map<Object, Object>> list = cmsMenuDao.getFistLevelName(menuId);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public Response getBrenchAndGroup() {
        Response res = new Response();
        List<Organization> list = new ArrayList<>();
        Organization group = userService.getBelongGroup();
        list.add(group);
        Organization org = userService.getBelongBranch();
        if(org.getOrgType().equals(OrganizationType.BRENCH)){
            list.add(org);
        }
        res.setData(list);
        return res;
    }
}
