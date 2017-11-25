package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车vo
 * @Author lx
 * @Date 2017/11/25 20:23
 */
public class CartVo {
    private List<CartProductVo> cartProductVoList;// 购物车中的商品
    private BigDecimal cartTotalPrice;
    private boolean allChecked;// 是否全选
    private String imageHost;

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public boolean isAllChecked() {
        return allChecked;
    }

    public void setAllChecked(boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
