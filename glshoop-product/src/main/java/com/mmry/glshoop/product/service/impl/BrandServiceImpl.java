package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.glshoop.product.dao.CategoryBrandRelationDao;
import com.mmry.glshoop.product.dao.CategoryDao;
import com.mmry.glshoop.product.entity.CategoryBrandRelationEntity;
import com.mmry.glshoop.product.entity.CategoryEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.product.dao.BrandDao;
import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<BrandEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            wrapper.like(BrandEntity::getBrandId, key).or().like(BrandEntity::getName, key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        baseMapper.updateById(brand);
        if (StringUtils.isEmpty(brand.getName()))
            return;
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandName(brand.getName());
        relationDao.update(
                relationEntity,
                new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId,brand.getBrandId())
        );
    }


}