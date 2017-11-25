package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductManageService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 商品管理
 * @Author lx
 * @Date 2017/11/19 13:31
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductManageController.class);
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductManageService iProductManageService;
    @Autowired
    private IFileService iFileService;

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
     * @return
     */
    @RequestMapping("/upload.do")
    @ResponseBody
    public ServerResponse updload(HttpSession session,
                                  @RequestParam(value = "upload_file", required = false)MultipartFile file) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            return ServerResponse.createByErrorMessage("用户未登录，请登录。");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            //文件上传
            String path = session.getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap(); // guava
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            LOGGER.info("文件上传返回 : {}", fileMap);
            return ServerResponse.createBySuccess(fileMap);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    /**
     * 富文本图片上传
     * @param session
     * @param file
     * @return
     */
    @RequestMapping("/richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session,
                                 HttpServletResponse response,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        Map resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null == user) {
            resultMap.put("success", false);
            resultMap.put("msg", "请先登录管理员");
            return resultMap;
        }

        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            String path = session.getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }

            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);

            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            LOGGER.info("文件上传返回 : {}", resultMap);
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
