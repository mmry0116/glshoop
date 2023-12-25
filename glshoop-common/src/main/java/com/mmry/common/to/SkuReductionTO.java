package com.mmry.common.to;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : mmry
 * @date : 2023/12/17 13:49
 */
@Data
public class SkuReductionTO implements Serializable {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus; //打折状态是否参与其他优惠

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
