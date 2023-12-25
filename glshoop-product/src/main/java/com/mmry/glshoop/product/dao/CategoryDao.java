package com.mmry.glshoop.product.dao;

import com.mmry.glshoop.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
