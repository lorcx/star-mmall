package com.mmall.util;

import java.math.BigDecimal;

/**
 * @Author lx
 * @Date 2017/11/25 20:52
 */
public class BigDecimalUtil {
    private BigDecimalUtil() {

    }

    /**
     * 加
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2);
    }

    /**
     * 减
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2);
    }

    /**
     * 乘
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2);
    }

    /**
     * 除
     * @param d1
     * @param d2
     * @return
     */
    public static BigDecimal div(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2);
    }
}
