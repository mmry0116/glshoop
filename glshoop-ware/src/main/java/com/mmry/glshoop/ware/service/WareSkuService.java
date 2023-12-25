package com.mmry.glshoop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 商品库存
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:10:09
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateInfo(List<Long> purchaseDetailIds);
}

