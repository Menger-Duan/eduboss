package com.eduboss.domain;

import javax.persistence.*;

/**
 * 产品打包卖
 * Created by Administrator on 2017/5/26.
 */
@Entity
@Table(name = "product_package")
public class ProductPackage implements java.io.Serializable {
    private int id;

    private Product parentProduct; //

    private Product sonProduct;//

    private String createUserId;

    private String createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "parent_product_id")
    public Product getParentProduct() {
        return parentProduct;
    }

    public void setParentProduct(Product parentProduct) {
        this.parentProduct = parentProduct;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "son_product_id")
    public Product getSonProduct() {
        return sonProduct;
    }

    public void setSonProduct(Product sonProduct) {
        this.sonProduct = sonProduct;
    }

    @Column(name = "create_user_id")
    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Column(name = "create_time")
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}


/**
 * DROP TABLE if EXISTS `product_package`;
 CREATE TABLE `product_package`(
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `parent_product_id` varchar(32) DEFAULT NULL COMMENT '父级产品ID',
 `son_product_id` varchar(32) DEFAULT NULL COMMENT '子产品ID',
 PRIMARY KEY (`id`)
 )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */

