package com.mmry.glshoop.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.coupon.entity.CouponSpuRelationEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:07:35
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

