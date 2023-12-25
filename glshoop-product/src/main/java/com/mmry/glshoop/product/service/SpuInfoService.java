package com.mmry.glshoop.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.product.entity.SpuInfoEntity;
import com.mmry.glshoop.product.vo.skuvo.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO spuSaveVO);

    PageUtils queryPageBycondition(Map<String, Object> params);
}

