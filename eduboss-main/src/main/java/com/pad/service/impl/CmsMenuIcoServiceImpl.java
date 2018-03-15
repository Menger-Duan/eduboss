package com.pad.service.impl;

import com.eduboss.dto.Response;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.pad.common.CmsUseType;
import com.pad.dao.CmsBindIcoDao;
import com.pad.dao.CmsMenuDao;
import com.pad.dao.CmsMenuIcoDao;
import com.pad.dto.CmsBindIcoVo;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsBindIco;
import com.pad.entity.CmsMenu;
import com.pad.entity.CmsMenuIco;
import com.pad.service.CmsMenuIcoService;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
public class CmsMenuIcoServiceImpl  implements CmsMenuIcoService {

    Logger log = Logger.getLogger(CmsMenuIcoService.class);

    @Autowired
    private CmsMenuIcoDao cmsMenuIcoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CmsBindIcoDao cmsBindIcoDao;
    @Autowired
    private CmsMenuDao cmsMenuDao;

    @Override
    public Response saveCmsMenuIco(CmsMenuIcoVo menuIcos) {
        Response res = new Response();
        CmsMenuIco ico;
        if(menuIcos.getId()!=null && menuIcos.getId()>0){
            ico= cmsMenuIcoDao.findById(menuIcos.getId());
//            if(ico.getStatus()!=null && "0".equals(ico.getStatus())){
//                res.setResultCode(-1);
//                res.setResultMessage("发布状态的图标不能修改！");
//                return res;
//            }
        }else {
            ico= HibernateUtils.voObjectMapping(menuIcos,CmsMenuIco.class);
            ico.setCreateUser(userService.getCurrentLoginUserId());
        }

        if(ico.getMenuId()!=null && ico.getMenuId()>0){
            CmsMenu menu = cmsMenuDao.findById(ico.getMenuId());
            menu.setShowType(menuIcos.getShowType());//设置显示模式。
        }

        ico.setModifyUser(userService.getCurrentLoginUserId());

        cmsMenuIcoDao.save(ico);
        cmsMenuIcoDao.commit();
        saveMultiBindIco(menuIcos.getBindIcoVos(),ico);

        return res;
    }

    /**
     * 发布图标
     * @param vo
     * @return
     */
    @Override
    public Response publicMenuIco(CmsMenuIcoVo vo) {

        Response res = new Response();

        if(vo.getId()==null || vo.getId()<=0){
            res.setResultCode(-1);
            res.setResultMessage("ID必传且不能为0");
            return res;
        }

        if(vo.getMenuLevel()==null || vo.getMenuLevel()<=0 || vo.getMenuLevel()>2){
            res.setResultCode(-1);
            res.setResultMessage("菜单级别传入有误"+vo.getMenuLevel());
            return res;
        }

        CmsMenuIco ico = cmsMenuIcoDao.findById(vo.getId());


        res= updatePublishStatus(vo.getMenuLevel(),ico.getMenuId());//取消前面已经发布过的图标

        List<CmsBindIco> list = cmsBindIcoDao.findAllBindIcoByIcoId(vo.getId());
        ico.setPublishUser(userService.getCurrentLoginUserId());
        ico.setStatus("0");//设置为发布状态

        //保存新的图标到菜单。
        for(CmsBindIco bind:list){
            CmsMenu menu= cmsMenuDao.findById(bind.getMenuId());
            menu.setIco(bind.getIcoUrl());
            menu.setGrayIco(bind.getGrayIco());
            cmsMenuDao.save(menu);
        }


        return res;
    }

    @Override
    public List<CmsMenuIcoVo> getMenuIcoList(CmsMenuIcoVo vo) {

       List<CmsMenuIco> list= cmsMenuIcoDao.findIcoByStatusAndLevel(vo);

       List<CmsMenuIcoVo> voList = HibernateUtils.voListMapping(list,CmsMenuIcoVo.class);



       for (CmsMenuIcoVo cvo :voList){
           List<CmsBindIcoVo> icoVos = getFullIcoVoList(cvo.getId(),vo.getMenuLevel(),vo.getMenuId());
           if(cvo.getMenuId()!=null && cvo.getMenuId()>0){
               CmsMenu menu = cmsMenuDao.findById(cvo.getMenuId());
               cvo.setShowType(menu.getShowType());//设置类型。
           }
           cvo.setBindIcoVos(icoVos);
       }

        return voList;
    }

    private List<CmsBindIcoVo> getFullIcoVoList(Integer icoId,Integer menuLevel,Integer menuId) {
        List<CmsBindIco> icoList= cmsBindIcoDao.findAllBindIcoByIcoId(icoId);
        List<CmsBindIcoVo> icoVoList = HibernateUtils.voListMapping(icoList,CmsBindIcoVo.class);
        CmsMenuVo mvo =new CmsMenuVo();
        mvo.setUseType(CmsUseType.VISITOR);
        mvo.setParentId(menuId);
        mvo.setLevel(menuLevel.toString());
        List<CmsMenu> list=cmsMenuDao.getCmsMenuListByParam(mvo);
        List<CmsBindIcoVo> returnList= new ArrayList<>();

        Map<Integer,CmsBindIcoVo> map = new HashMap<>();
        for(CmsBindIcoVo vo : icoVoList){
            map.put(vo.getMenuId(),vo);
        }
        if(icoVoList.size()!=list.size()){

            for (CmsMenu menu:list){
                if(map.get(menu.getId())!=null){
                    CmsBindIcoVo vo = map.get(menu.getId());
                    vo.setMenuName(menu.getName());
                    returnList.add(vo);
                }else{
                    CmsBindIcoVo vo = new CmsBindIcoVo();
                    vo.setMenuName(menu.getName());
                    vo.setMenuId(menu.getId());
                    returnList.add(vo);
                }
            }
        }else{
            for (CmsMenu menu:list) {
                if (map.get(menu.getId()) != null) {
                    CmsBindIcoVo vo = map.get(menu.getId());
                    vo.setMenuName(menu.getName());
                    returnList.add(vo);
                }
            }
        }

        return returnList;
    }

    public Response updatePublishStatus(Integer level,Integer menuId){
        Response res = new Response();
       List<CmsMenuIco> list= cmsMenuIcoDao.findIcoByStatusAndLevel(new CmsMenuIcoVo("0",level,menuId));
       if(list.size()>1){
           log.error("有点问题的样子，这里同一个级别的菜单不会超过一个！");
       }

       for(CmsMenuIco ico :list){
           ico.setStatus("1");//设置状态为未发布
           cmsMenuIcoDao.save(ico);
       }

        return res;
    }


    /**
     * 批量保存图标信息
     * @param vos
     * @param ico
     * @return
     */
    public Response saveMultiBindIco(List<CmsBindIcoVo> vos, CmsMenuIco ico){
        Response res = new Response();
        for(CmsBindIcoVo vo : vos){
            CmsBindIco bind = HibernateUtils.voObjectMapping(vo,CmsBindIco.class);
            bind.setIcoId(ico.getId());
            if(bind.getId()==null || bind.getId()<=0){
                bind.setCreateUser(userService.getCurrentLoginUserId());
            }

            bind.setModifyUser(userService.getCurrentLoginUserId());

            if(ico.getStatus()!=null && ico.getStatus().equals("0")){//发布状态的要更新图标
                CmsMenu menu = cmsMenuDao.findById(vo.getMenuId());
                menu.setIco(bind.getIcoUrl());
                menu.setGrayIco(bind.getGrayIco());
                cmsMenuDao.save(menu);
            }
            cmsBindIcoDao.save(bind);
        }

        return res;
    }
}
