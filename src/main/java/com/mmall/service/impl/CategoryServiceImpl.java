package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author lx
 * @Date 2017/11/18 22:27
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加品类
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName, int parentId) {
        if (StringUtils.isBlank(categoryName)) {
            return ServerResponse.createBySuccess("添加分类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
     }

    /**
     * 修改品类名称
     * @param categoryId
     * @param categoryName
     * @return
     */
     public ServerResponse updateCategoryName(int categoryId, String categoryName) {
         if (StringUtils.isBlank(categoryName)) {
             return ServerResponse.createBySuccess("更新分类参数错误");
         }

         Category category = new Category();
         category.setName(categoryName);
         category.setId(categoryId);

         int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
         if (rowCount > 0) {
             return ServerResponse.createBySuccess("更新品类名称成功");
         }
         return ServerResponse.createByErrorMessage("更新品类名称失败");
     }

    /**
     * 查询父节点下的所有子节点不递归
     * @param categoryId
     * @return
     */
     public ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId) {
         List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
         if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前子分类");
         }
         return ServerResponse.createBySuccess(categoryList);
     }

    /**
     * 递归查询本节点的id和孩子节点的idea
     * @param categoryId
     * @return
     */
     public ServerResponse selectCategoryAndChildrenById(int categoryId) {
         Set<Category> categorySet = Sets.newHashSet();
         findChildCategory(categorySet, categoryId);

         List<Integer> categoryList = Lists.newArrayList();
         for (Category c : categorySet) {
             categoryList.add(c.getId());
         }
         return ServerResponse.createBySuccess(categoryList);
     }

    /**
     * 递归调用
     * @return
     */
     private Set<Category> findChildCategory(Set<Category> categorySet, int categoryId) {
         Category category = categoryMapper.selectByPrimaryKey(categoryId);
         if (category != null) {
             categorySet.add(category);
         }

         List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
         for (Category c : categoryList) {
             findChildCategory(categorySet, c.getId());
         }
         return categorySet;
     }
}
