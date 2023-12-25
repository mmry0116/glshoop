/**
  * Copyright 2023 bejson.com 
  */
package com.mmry.glshoop.product.vo.skuvo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2023-12-16 15:11:0
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SpuSaveVO {

    private String spuName;  //spu基本信息
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;

    private List<String> decript;  // 商品描述图片 pms_spu_info_desc

    private List<String> images;  //  展示图片集

    private Bounds bounds;

    private List<BaseAttrs> baseAttrs; //spu基本属性 pms_product_sttr_value

    private List<Skus> skus;

}