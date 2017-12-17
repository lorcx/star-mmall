package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @Author lx
 * @Date 2017/12/16 20:06
 */
public interface IOrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer id, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse getOrderDetail(Integer userId, Long orderNo);

    ServerResponse getOrderList(Integer id, int pageNum, int pageSize);

    ServerResponse manageList(int pageNum, int pageSize);

    ServerResponse manageDetail(Long orderNo);

    ServerResponse manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse manageSendGoods(Long orderNo);

    ServerResponse cancel(Integer userId, Long orderNo);
}
