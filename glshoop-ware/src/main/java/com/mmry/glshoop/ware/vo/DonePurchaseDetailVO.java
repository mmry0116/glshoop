package com.mmry.glshoop.ware.vo;

import lombok.Data;

/**
 * @author : mmry
 * @date : 2023/12/20 14:56
 */
@Data
public class DonePurchaseDetailVO {
    private Long itemId; //采购单id
    private int status ; //采购项状态 3：完成 4：异常
    private String reason; //如果采购项出现异常 这里填写异常内容
}
