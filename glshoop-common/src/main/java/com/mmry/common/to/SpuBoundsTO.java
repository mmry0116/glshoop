package com.mmry.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : mmry
 * @date : 2023/12/17 13:32
 */
@Data
public class SpuBoundsTO {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
