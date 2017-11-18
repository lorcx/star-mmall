package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @Author lx
 * @Date 2017/11/18 22:26
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, int parentId);

    ServerResponse updateCategoryName(int categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(int categoryId);

    ServerResponse selectCategoryAndChildrenById(int categoryId);
}
