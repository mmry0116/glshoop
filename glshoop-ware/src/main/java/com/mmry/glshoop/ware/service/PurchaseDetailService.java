package com.mmry.glshoop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:10:09
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

