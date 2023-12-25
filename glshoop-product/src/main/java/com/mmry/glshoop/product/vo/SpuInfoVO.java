package com.mmry.glshoop.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : mmry
 * @date : 2023/12/18 11:19
 */
@Data
public class SpuInfoVO {
    private Long id;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 所属分类id
     */
    private Long catalogId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     *
     */
    private BigDecimal weight;
    /**
     * 上架状态[0 - 下架，1 - 上架]
     */
    private Integer publishStatus;

    private Date createTime;

    private Date updateTime;

    private String catalogName; //分类名字

    private String brandName; //品牌名字
}
