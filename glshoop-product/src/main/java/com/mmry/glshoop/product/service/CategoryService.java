package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    CategoryEntity getOneContainChild(Long catelogId);

    void updateDetail(CategoryEntity category);

    List<Long> getCategoryIds(Long catelogId);
}

