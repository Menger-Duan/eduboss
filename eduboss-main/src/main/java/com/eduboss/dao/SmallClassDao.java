package com.eduboss.dao;

import com.eduboss.domain.User;
import com.eduboss.dto.MiniClassModalVo;
import org.springframework.stereotype.Repository;

import com.eduboss.domain.MiniClass;

import java.util.List;


@Repository
public interface SmallClassDao extends GenericDAO<MiniClass, String> {

    List<MiniClass> getSmallClassListOnModal(MiniClassModalVo vo,User user);

    List<MiniClass> getSmallClassListByTeacher(MiniClassModalVo vo, User user);

    String findProductIdByMiniClassId(String miniClassId);
}
