package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * @Author: lx
 * @Date: 2017/11/14 0014 22:18 
 */
public interface IUserService {
    ServerResponse<User> login(String username, String password);
}
