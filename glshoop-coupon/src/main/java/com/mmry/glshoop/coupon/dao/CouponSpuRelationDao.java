package com.mmry.glshoop.coupon.dao;

import com.mmry.glshoop.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:07:35
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
