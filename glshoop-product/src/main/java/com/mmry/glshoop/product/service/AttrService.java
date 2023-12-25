package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.AttrEntity;
import com.mmry.glshoop.product.entity.ProductAttrValueEntity;
import com.mmry.glshoop.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:23
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(AttrVO attrVO);

    PageUtils queryBaseListPage(Map<String, Object> params, Long catelogId,String attrType);

    AttrEntity getDetail(Long attrId);

    void updateDetail(AttrVO attrVO);

    IPage queryNoBindAttr(LambdaQueryWrapper<AttrEntity> wrapper, Map<String, Object> params);

    List<ProductAttrValueEntity> listforspu(Long spuId);


    void updateBySpuId(Long spuId, List<ProductAttrValueEntity> productAttrValues);
}

