package com.eduboss.service.impl.rpc;

import java.math.BigDecimal;
import java.util.List;

import org.boss.rpc.base.dto.ProductRpcVo;
import org.boss.rpc.eduboss.service.ProductRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.Product;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.service.ProductService;
import com.eduboss.utils.HibernateUtils;

@Service("productRpcImpl")
public class ProductRpcImpl implements ProductRpc {

    @Autowired
    private ProductService productService;
    
    @Override
    public List<ProductRpcVo> listProductByMiniClassId(String miniClassId) {
        List<ProductRpcVo> result = null;
        List<Product> list = productService.listAllSubProductsByMiniClassId(miniClassId);
        if (list != null && !list.isEmpty()) {
            result = HibernateUtils.voListMapping(list, ProductRpcVo.class);
        }
        return result;
    }

    @Override
    public ProductRpcVo findProductById(String productId) {
        ProductRpcVo result = null;
        ProductVo product = productService.findProductById(productId);
        if (product != null) {
            result = HibernateUtils.voObjectMapping(product, ProductRpcVo.class);
            result.setPrice(new BigDecimal(product.getPrice()));
        }
        return result;
    }

}
