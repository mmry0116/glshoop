package com.mmry.glshoop.order.dao;

import com.mmry.glshoop.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 14:00:32
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
