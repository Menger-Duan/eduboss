package com.eduboss.service;

import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.dto.DataPackage;

/**
 * Created by xuwen on 2014/12/29.
 */
public interface ProductChooseViewService {

    public DataPackage findPage(DataPackage dp, ProductChooseVo productChooseVo, String isEcsProductCategory);

}
