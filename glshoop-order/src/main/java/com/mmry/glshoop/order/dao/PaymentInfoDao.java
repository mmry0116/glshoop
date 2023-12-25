package com.mmry.glshoop.order.dao;

import com.mmry.glshoop.order.entity.PaymentInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付信息表
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 14:00:33
 */
@Mapper
public interface PaymentInfoDao extends BaseMapper<PaymentInfoEntity> {
	
}
