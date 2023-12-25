package com.mmry.glshoop.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.order.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 14:00:32
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

