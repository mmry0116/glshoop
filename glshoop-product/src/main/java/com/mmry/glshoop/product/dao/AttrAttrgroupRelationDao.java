package com.mmry.glshoop.product.dao;

import com.mmry.glshoop.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mmry.glshoop.product.vo.AttrAttrGroupRelationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void deleteBatch(@Param("idss") List<AttrAttrGroupRelationVO> idss);
}
