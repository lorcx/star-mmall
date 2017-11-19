package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductManageService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lx
 * @Date 2017/11/19 13:44
 */
@Service("iProductManageService")
public class ProductManageServiceImpl implements IProductManageService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 保存或修改商品
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (null != product) {
            // 子图第一个设置为主图
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImgArr = product.getSubImages().split(",");
                if (subImgArr.length > 0) {
                    product.setMainImage(subImgArr[0]);
                }
            }

            if (product.getId() == null) {
                int rowCount = productMapper.insert(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("新增产品失败");
                }
            } else {
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("更新产品失败");
                }

            }
        }
        return ServerResponse.createByErrorMessage("新增或修改产品参数不正确，product == null");
    }

    /**
     * 修改商品销售状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product p = new Product();
        p.setId(productId);
        p.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(p);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改商品销售状态成功");
        } else {
            return ServerResponse.createByErrorMessage("修改商品销售状态失败");
        }
    }

    /**
     * 获取商品详情
     * @param productId
     * @return
     */
    @Override
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
        if (null == productId) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (null == product) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    /**
     * 生成产品vo
     * @param product
     * @return
     */
    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo pdVo = new ProductDetailVo();
        pdVo.setCategoryId(product.getCategoryId());
        pdVo.setId(product.getId());
        pdVo.setDetail(product.getDetail());
        pdVo.setMainImage(product.getMainImage());
        pdVo.setSubImages(product.getSubImages());
        pdVo.setStatus(product.getStatus());
        pdVo.setStock(product.getStock());

        pdVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.lxmall.com/"));
        pdVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        pdVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return pdVo;
    }


    /**
     * 获取商品详情
     * pagehelper使用aop做分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectList();

        // 转换vo
        List<ProductListVo> productVoList = Lists.newArrayList();
        for (Product p : productList) {
            productVoList.add(assembleProductListVo(p));
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    /**
     * 将produ转成productlistvo
     * @param product
     * @return
     */
    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo plVo = new ProductListVo();
        plVo.setCategoryId(product.getCategoryId());
        plVo.setId(product.getId());
        plVo.setName(product.getName());
        plVo.setMainImage(product.getMainImage());
        plVo.setSubTitle(product.getSubtitle());
        plVo.setMainImage(product.getMainImage());
        plVo.setStatus(product.getStatus());
        plVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.lxmall.com/"));
        return null;
    }

    /**
     * 检索产品
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ServerResponse<PageInfo> productSearch(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectListByNameAndId(productName, productId);

        // 转换vo
        List<ProductListVo> productVoList = Lists.newArrayList();
        for (Product p : productList) {
            productVoList.add(assembleProductListVo(p));
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productVoList);
        return ServerResponse.createBySuccess(pageResult);
    }
}
