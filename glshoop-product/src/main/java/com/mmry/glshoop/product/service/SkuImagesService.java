package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:23
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

