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
public class Skus {

    private List<Attr> attr; //sku的销售属性信息 pms_sku_sale_attr_value
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;

    private List<Images> images;

    private List<String> descar;

    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;


}