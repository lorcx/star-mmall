package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductManageService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 商品管理
 * @Author lx
 * @Date 2017/11/19 13:31
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductManageService iProductManageService;

    /**
     * 保存产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //增加产品
            return iProductManageService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 修改商品销售状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("/set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //增加产品
            return iProductManageService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取商品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取产品
            return iProductManageService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 获取商品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取产品列表
            return iProductManageService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 检索商品列表
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse productSearch(HttpSession session,
                                  String productName,
                                  Integer productId,
                                  @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //获取产品列表
            return iProductManageService.productSearch(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    public ServerResponse updload(HttpSession session,
                                  @RequestParam(value = "upload_file", required = false)MultipartFile file,
                                  HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //文件上传

            return null;
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
}
