package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.AttrEntity;
import com.mmry.glshoop.product.entity.AttrGroupEntity;
import com.mmry.glshoop.product.vo.AttrAttrGroupRelationVO;
import com.mmry.glshoop.product.vo.AttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AttrGroupEntity getByIdContaincategoryPath(Long attrGroupId);

    PageUtils queryPage(Map<String, Object> params, String categoryId);

    List<AttrEntity> ListAttr(Long attrGroupId);

    List<AttrEntity> deleteBatch(List<AttrAttrGroupRelationVO> idss);

    PageUtils queryNoBindAttr(Long attrGroupId,Map<String, Object> params);

    void saveAttrsRelation(List<AttrAttrGroupRelationVO> relations);

    List<AttrGroupVo> ListAttrGroupWithAttr(Long catelogId);
}

