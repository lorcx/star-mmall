package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * 常量类
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    /**
     * 使用内部接口进行分组
     */
    public interface Role {
        int ROLE_CUSTOMER = 0;// 普通用户
        int ROLE_ADMIN = 1;// 管理员
    }

    /**
     * 排序
     */
    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("priece_desc", "priece_asc");
    }

    /**
     * 产品
     */
    public enum ProductStatusEnum {
        ON_SALE(1, "在线");
        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 购物车状态
     */
    public interface Cart {
        int CHECKED = 1;// 购物车选中状态
        int UN_CHECKED = 0;// 未选中
        String LIMIT_NUM_FATL = "LIMIT_NUM_FATL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }
}
