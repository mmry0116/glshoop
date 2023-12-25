package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.glshoop.product.dao.CategoryBrandRelationDao;
import com.mmry.glshoop.product.entity.CategoryBrandRelationEntity;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.product.dao.CategoryDao;
import com.mmry.glshoop.product.entity.CategoryEntity;
import com.mmry.glshoop.product.service.CategoryService;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> categoryAll = baseMapper.selectList(null);
        List<CategoryEntity> categories = categoryAll.stream()
                .filter(entity -> entity.getCatLevel() == 1).map(entity -> {
                    entity.setCategoryChildren(getCategoryChidren(categoryAll, entity));
                    return entity;
                })
                .sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort()) - (o2.getSort() == null ? 0 : o2.getSort()))
                .collect(Collectors.toList());

        return categories;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //删除前检查每个菜单是否关联其他
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 获取一个CategoryEntity信息 包括所有层级的children
     *
     * @param categoryId
     * @return
     */
    @Override
    public CategoryEntity getOneContainChild(Long categoryId) {
        //查出所有分类
        List<CategoryEntity> categoryAll = baseMapper.selectList(null);
        CategoryEntity category = baseMapper.selectOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, categoryId));
        category.setCategoryChildren(getCategoryChidren(categoryAll, category));
        return category;
    }

    private List<CategoryEntity> getCategoryChidren(List<CategoryEntity> entities, CategoryEntity parent) {

        return entities.stream()
                .filter(entity -> entity.getParentCid().longValue() == parent.getCatId().longValue())
                .map(entity -> {
                    entity.setCategoryChildren(getCategoryChidren(entities, entity));
                    return entity;
                })
                .sorted((o1, o2) -> (o1.getSort() == null ? 0 : o1.getSort()) - (o2.getSort() == null ? 0 : o2.getSort()))
                .collect(Collectors.toList());

    }

    @Override
    public void updateDetail(CategoryEntity category) {
        baseMapper.updateById(category);
        if (StringUtils.isEmpty(category.getName()))
            return;
        relationDao.updateDetail(category.getCatId(), category.getName());
    }

    @Override
    public List<Long> getCategoryIds(Long catelogId) {
        CategoryEntity category = baseMapper.selectById(catelogId);
        ArrayList<Long> categoryIds = new ArrayList<>();
        for (; ; ) {
            categoryIds.add(category.getCatId());
            if (category.getParentCid() == 0)
                break;
            category = baseMapper.selectOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, category.getParentCid()));

        }
        Collections.reverse(categoryIds);
        return categoryIds;
    }
}