package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.common.constant.ProductConstant;
import com.mmry.glshoop.product.dao.AttrAttrgroupRelationDao;
import com.mmry.glshoop.product.dao.AttrDao;
import com.mmry.glshoop.product.dao.CategoryDao;
import com.mmry.glshoop.product.entity.AttrAttrgroupRelationEntity;
import com.mmry.glshoop.product.entity.AttrEntity;
import com.mmry.glshoop.product.service.AttrAttrgroupRelationService;
import com.mmry.glshoop.product.service.AttrService;
import com.mmry.glshoop.product.service.CategoryService;
import com.mmry.glshoop.product.vo.AttrAttrGroupRelationVO;
import com.mmry.glshoop.product.vo.AttrGroupVo;
import org.springframework.beans.BeanUtils;
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

import com.mmry.glshoop.product.dao.AttrGroupDao;
import com.mmry.glshoop.product.entity.AttrGroupEntity;
import com.mmry.glshoop.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrDao attrDao;
    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    AttrAttrgroupRelationService relationService;
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public AttrGroupEntity getByIdContaincategoryPath(Long attrGroupId) {
        AttrGroupEntity entity = attrGroupDao.selectById(attrGroupId);
        //根据CatelogId值获取category菜单
        List<Long> categoryIds = categoryService.getCategoryIds(entity.getCatelogId());
        Collections.reverse(categoryIds);
        entity.setCategoryPath(categoryIds);
        return entity;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, String categoryId) {
        IPage<AttrGroupEntity> page;
        LambdaQueryWrapper<AttrGroupEntity> wrapper;
        String key = (String) params.get("key");
        System.out.println("categoryId : " + categoryId);
        if (categoryId.equals("0")) {
            if (key != "" && key != null) {
                wrapper = new LambdaQueryWrapper<AttrGroupEntity>().
                        like(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key);
            } else
                wrapper = new LambdaQueryWrapper<>();
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        } else {
            if (key != "" && key != null) {
                wrapper = new LambdaQueryWrapper<AttrGroupEntity>().
                        eq(AttrGroupEntity::getCatelogId, categoryId).
                        and(w -> w.like(AttrGroupEntity::getAttrGroupName, key).or().like(AttrGroupEntity::getAttrGroupId, key));
            } else
                wrapper = new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, categoryId);
            page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
        }

        return new PageUtils(page);
    }

    @Override
    public List<AttrEntity> ListAttr(Long attrGroupId) {
        List<Object> objects = relationDao
                .selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupId)
                        .select(AttrAttrgroupRelationEntity::getAttrId));
        List<Long> attrIds = new ArrayList<>();
        for (Object object : objects)
            attrIds.add((Long) object);

        return attrDao.selectList(new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId, attrIds));
    }

    @Override
    public List<AttrEntity> deleteBatch(List<AttrAttrGroupRelationVO> idss) {
        relationDao.deleteBatch(idss);
        return null;
    }

    @Override
    public PageUtils queryNoBindAttr(Long attrGroupId, Map<String, Object> params) {
        Long catelogId = attrGroupDao.selectById(attrGroupId).getCatelogId();
        //获取所有该分类下所有分组
        List<Object> objects1 = this.listObjs(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        ArrayList<Long> attrGroupIds = new ArrayList<>();
        for (Object o : objects1)
            attrGroupIds.add((Long) o);
        //获得到该分类下所有分组已经绑定的attrId
        List<Object> objects = relationDao.selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds)
                .select(AttrAttrgroupRelationEntity::getAttrId));
        ArrayList<Long> attrIds = new ArrayList<>();
        for (Object o : objects)
            attrIds.add((Long) o);
        //排除已经绑定的attr 得到未绑定attr
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                .eq(AttrEntity::getCatelogId, catelogId)
                .eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE)
                .notIn(AttrEntity::getAttrId, attrIds);

        IPage page = attrService.queryNoBindAttr(wrapper, params);
        return new PageUtils(page);
    }

    @Override
    public void saveAttrsRelation(List<AttrAttrGroupRelationVO> relations) {
        List<AttrAttrgroupRelationEntity> relationEntities = relations.stream().map(relationVO -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(relationVO, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationService.saveBatch(relationEntities);
    }

    @Override
    public List<AttrGroupVo> ListAttrGroupWithAttr(Long catelogId) {
        //获取分类下所有的attrGroup
        List<AttrGroupEntity> attrGroupEntities = baseMapper.selectList(new LambdaQueryWrapper<AttrGroupEntity>().in(AttrGroupEntity::getCatelogId, catelogId));
        //如果该分类没有分组则返回null
        if (attrGroupEntities.size() == 0)
            return null;
        List<AttrGroupVo> attrGroupVos = attrGroupEntities.stream().map(e -> {
            AttrGroupVo attrGroupVo = new AttrGroupVo();
            BeanUtils.copyProperties(e,attrGroupVo);
            List<Object> objects = relationDao
                    .selectObjs(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                            .eq(AttrAttrgroupRelationEntity::getAttrGroupId, e.getAttrGroupId())
                            .select(AttrAttrgroupRelationEntity::getAttrId));
            if (objects.size() != 0) {
                List<Long> attrIds = new ArrayList<>();
                for (Object attrId : objects)
                    attrIds.add((Long) attrId);
                List<AttrEntity> attrEntities = attrDao.selectList(new LambdaQueryWrapper<AttrEntity>().in(AttrEntity::getAttrId, attrIds));
                attrGroupVo.setAttrs(attrEntities);
            }
            BeanUtils.copyProperties(e, attrGroupVo);
            return attrGroupVo;
        }).collect(Collectors.toList());
        return attrGroupVos;
    }


}