package com.mmry.glshoop.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.ware.dao.PurchaseDetailDao;
import com.mmry.glshoop.ware.entity.PurchaseDetailEntity;
import com.mmry.glshoop.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    /**
     * @param params 包含参数 wareId skuId key ...
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        String wareId = (String) params.get("wareId");
        String skuId = (String) params.get("skuId");
        if (StringUtils.isNotEmpty(key))
            wrapper.and(w -> w.eq(PurchaseDetailEntity::getId, key).or().eq(PurchaseDetailEntity::getPurchaseId, key));
        if (StringUtils.isNotEmpty(wareId))
            wrapper.eq(PurchaseDetailEntity::getSkuId, wareId);
        if (StringUtils.isNotEmpty(skuId))
            wrapper.eq(PurchaseDetailEntity::getSkuId, skuId);
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),wrapper
                );
        return new PageUtils(page);
    }

}