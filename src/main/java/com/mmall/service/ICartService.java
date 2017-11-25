package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @Author lx
 * @Date 2017/11/25 20:20
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer id, String productIds);

    ServerResponse<CartVo> selectOrUnSelect(Integer id, Integer productId, int checked);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}
