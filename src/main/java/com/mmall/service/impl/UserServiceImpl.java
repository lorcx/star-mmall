package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户service
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectLogin(username, md5Password);
        if (null == user) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse = checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        // MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccess("注册成功");
    }

    /**
     * 校验
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("email存在");
                }
            }
            return ServerResponse.createBySuccessMessage("校验成功");
        }
        return ServerResponse.createByErrorMessage("参数错误");
    }

    /**
     * 获取找回密码问题
     * @param username
     * @return
     */
    public ServerResponse selectQuestion(String username) {
        ServerResponse validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码问题是空的");
    }

    /**
     * 检查密码问题是否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明问题及问题答案使这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 忘记密码重置
     * @param username
     * @param newPassword
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，需要传token");
        }
        ServerResponse validResponse = checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.equals(forgetToken, token)) {
            int rowCount = userMapper.updatePasswordByUsername(username, MD5Util.MD5EncodeUtf8(newPassword));
            if (rowCount > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token过期或者不正确");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登录状态下修改密码
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户。
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码不正确");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("旧密码不正确");
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInfomation(User user) {
        // username 不能被更新
        // email也要进行校验
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已经被占用，请重新修改");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新用户信息成功", updateUser);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInfomation(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (null == user) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword("");
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 检查用户是否为管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
