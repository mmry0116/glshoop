package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> catelogList(Long brandId);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    List<BrandEntity> listBrandsByCatId(Long catId);
}

