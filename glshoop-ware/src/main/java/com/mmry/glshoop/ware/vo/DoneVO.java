package com.mmry.glshoop.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : mmry
 * @date : 2023/12/20 14:54
 */
@Data
public class DoneVO {
    private Long id ; //采购单id
    private List<DonePurchaseDetailVO> items; //完成的采购项
}
