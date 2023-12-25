package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.glshoop.product.dao.BrandDao;
import com.mmry.glshoop.product.dao.CategoryDao;
import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.product.dao.CategoryBrandRelationDao;
import com.mmry.glshoop.product.entity.CategoryBrandRelationEntity;
import com.mmry.glshoop.product.service.CategoryBrandRelationService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> catelogList(Long brandId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }

    @Override
    @Transactional
    public void saveDetail(CategoryBrandRelationEntity relation) {
        String categoryName = categoryDao
                .selectOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, relation.getCatelogId()))
                .getName();
        String brandName = brandDao
                .selectOne(new LambdaQueryWrapper<BrandEntity>().eq(BrandEntity::getBrandId, relation.getBrandId()))
                .getName();
        relation.setBrandName(brandName);
        relation.setCatelogName(categoryName);
        this.baseMapper.insert(relation);
    }

    @Override
    public List<BrandEntity> listBrandsByCatId(Long catId) {
        List<Object> objects = baseMapper
                .selectObjs(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                        .eq(CategoryBrandRelationEntity::getCatelogId, catId)
                        .select(CategoryBrandRelationEntity::getBrandId));
        if (objects.size() == 0)
            return null;
        ArrayList<Long> brandIds = new ArrayList<>();
        for (Object id : objects)
            brandIds.add((Long) id);
        return brandDao.selectList(new LambdaQueryWrapper<BrandEntity>().in(BrandEntity::getBrandId, brandIds));
    }

}