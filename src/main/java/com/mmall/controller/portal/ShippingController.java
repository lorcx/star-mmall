package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

/**
 * 收货地址管理
 * @Author lx
 * @Date 2017/11/26 12:31
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /**
     * 新增收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.add(user.getId(), shipping);
    }

    /**
     * 更新收货地址
     * @param session
     * @param shipping
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.update(user.getId(), shipping);
    }

    /**
     * 删除收货地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.del(user.getId(), shippingId);
    }

    /**
     * 查询收货地址
     * @param session
     * @param shippingId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.select(user.getId(), shippingId);
    }

    /**
     * 列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue ="10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.list(user.getId(), pageNum, pageSize);
    }

}
