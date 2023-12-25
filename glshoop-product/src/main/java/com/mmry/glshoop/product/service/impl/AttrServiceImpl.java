package com.mmry.glshoop.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mmry.common.constant.ProductConstant;
import com.mmry.common.utils.Constant;
import com.mmry.glshoop.product.dao.*;
import com.mmry.glshoop.product.entity.*;
import com.mmry.glshoop.product.service.CategoryService;
import com.mmry.glshoop.product.service.ProductAttrValueService;
import com.mmry.glshoop.product.vo.AttrRespVO;
import com.mmry.glshoop.product.vo.AttrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    ProductAttrValueDao productAttrValueDao;
    @Autowired
    ProductAttrValueService productAttrValueService;
    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(AttrVO attrVO) {
        AttrEntity attrEntity = new AttrEntity();
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        BeanUtils.copyProperties(attrVO, attrEntity);
        baseMapper.insert(attrEntity);
        //插入一条属性和属性分组的关联关系数据
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrVO.getAttrGroupId());
        relationEntity.setAttrSort(1);
        relationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseListPage(Map<String, Object> params, Long catelogId, String attrType) {
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.equalsIgnoreCase(attrType, "sale")) {
            wrapper.eq(AttrEntity::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        }
        if (catelogId != 0)
            wrapper.eq(AttrEntity::getCatelogId, catelogId);
        if (!StringUtils.isEmpty(key))
            wrapper.and(w -> w.like(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key));
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> attrEntities = page.getRecords();
        List<AttrRespVO> detailEntities = attrEntities.stream().map(attrEntity -> {
            Long attrId = attrEntity.getAttrId();
            Long cateId = attrEntity.getCatelogId();
            AttrRespVO attrRespVO = new AttrRespVO();
            BeanUtils.copyProperties(attrEntity, attrRespVO);
            String cateName = categoryDao
                    .selectOne(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, cateId))
                    .getName();
            //如果是销售属性 则没有分组关联的分组
            if (attrEntity.getAttrType() != ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
                if (relationEntity != null) {
                    Long attrGroupId = relationEntity.getAttrGroupId();
                    //如果有groupId才去表，
                    if (attrGroupId != null) {
                        String attrGroupName = attrGroupDao
                                .selectOne(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getAttrGroupId, attrGroupId))
                                .getAttrGroupName();
                        attrRespVO.setGroupName(attrGroupName);
                    }
                }

            }

            attrRespVO.setCatelogName(cateName);
            //设置分组名字

            return attrRespVO;
        }).collect(Collectors.toList());
        pageUtils.setList(detailEntities);
        return pageUtils;
    }

    @Override
    public AttrEntity getDetail(Long attrId) {
        AttrRespVO attrRespVO = new AttrRespVO();
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVO);
        //如果不是销售属性则设置分组id
        if (attrEntity.getAttrType() != ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
            AttrAttrgroupRelationEntity entity = relationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
            if (entity != null) {
                Long attrGroupId = entity.getAttrGroupId();
                attrRespVO.setAttrGroupId(attrGroupId);
            }
        }
        //设置分组id和分类地址
        attrRespVO.setCatelogPath(categoryService.getCategoryIds(attrEntity.getCatelogId()));
        return attrRespVO;
    }

    @Transactional
    @Override
    public void updateDetail(AttrVO attrVO) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVO, attrEntity);
        baseMapper.updateById(attrEntity);
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        int count = relationDao.selectCount(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrVO.getAttrId()));
        //如果是从基础属性该为销售属性 需要将属性关联的属性分组信息删除
        if (count > 0 && attrVO.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode())
            relationDao.delete(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrVO.getAttrId()));
        //如果是销售属性则不需要修改属性分组信息
        if (attrVO.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode() || attrVO.getAttrGroupId() == null)
            return;
        //判断是否有该属性id关联的关联属性分组额数据
        if (count > 0) {
            relationEntity.setAttrGroupId(attrVO.getAttrGroupId());
            relationDao.update(relationEntity, new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrVO.getAttrId()));
        } else {
            relationEntity.setAttrId(attrVO.getAttrId());
            relationEntity.setAttrGroupId(attrVO.getAttrGroupId());
            relationDao.insert(relationEntity);
        }

    }

    @Override
    public IPage queryNoBindAttr(LambdaQueryWrapper<AttrEntity> wrapper, Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        return page;
    }

    @Override
    public List<ProductAttrValueEntity> listforspu(Long spuId) {
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService
                .list(new LambdaQueryWrapper<ProductAttrValueEntity>().eq(ProductAttrValueEntity::getSpuId, spuId));
        return productAttrValueEntities;
    }

    @Transactional
    @Override
    public void updateBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValues) {
        List<Long> ids = productAttrValues.stream().map(e -> e.getAttrId()).collect(Collectors.toList());
        //先删除要修改的productAttrValue
        productAttrValueService.remove(new LambdaQueryWrapper<ProductAttrValueEntity>()
                .eq(ProductAttrValueEntity::getSpuId,spuId)
                .in(ProductAttrValueEntity::getAttrId,ids));
        productAttrValues.forEach(e->e.setSpuId(spuId));
        //批量修改
        productAttrValueService.saveBatch(productAttrValues);
    }

}