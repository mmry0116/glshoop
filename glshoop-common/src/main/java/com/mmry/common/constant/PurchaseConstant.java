package com.mmry.common.constant;

/**
 * @author : mmry
 * @date : 2023/12/19 13:35
 */
public class PurchaseConstant {
    public enum purchaseStatus {
        PURCHASE_CREATED_STATUS("新建", 0),
        PURCHASE_ALLOCATED_STATUS("已分配", 1),
        PURCHASE_RECEIVE_STATUS("以领取", 2),
        PURCHASE_COMPLETE_STATUS("已完成", 3),
        PURCHASE_ERROR_STATUS("有异常", 4);

        purchaseStatus(String status, int code) {
            this.status = status;
            this.code = code;
        }

        private String status;
        private int code;

        public String getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }
    }

    public enum purchaseDetailStatus {
        PURCHASE_CREATED_STATUS("新建", 0),
        PURCHASE_ALLOCATED_STATUS("已分配", 1),
        PURCHASE_BUYING_STATUS("正在采购", 2),
        PURCHASE_COMPLETE_STATUS("已完成", 3),
        PURCHASE_FAIL_STATUS("失败", 4);

        purchaseDetailStatus(String status, int code) {
            this.status = status;
            this.code = code;
        }

        private String status;
        private int code;

        public String getStatus() {
            return status;
        }

        public int getCode() {
            return code;
        }
    }
}
