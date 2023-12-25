package com.mmry.common.constant;

/**
 * @author : mmry
 * @date : 2023/12/13 10:47
 */
public class ProductConstant {

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        private int code;
        private String type;

        AttrEnum(int code, String type) {
            this.code = code;
            this.type = type;
        }

        public int getCode() {
            return code;
        }

        public String getType() {
            return type;
        }
    }
}
