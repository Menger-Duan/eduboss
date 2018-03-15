package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.entity.CmsBindIco;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public interface CmsBindIcoDao extends GenericDAO<CmsBindIco,String> {

    List<CmsBindIco> findAllBindIcoByIcoId(Integer id);

    /**
     * 根据目录ID获取绑定的信息
     * @param id
     * @return
     */
    List<CmsBindIco> findInfoByMenuId(Integer id);

}