package com.mmry.glshoop.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.common.utils.R;
import com.mmry.glshoop.ware.dao.PurchaseDao;
import com.mmry.glshoop.ware.dao.PurchaseDetailDao;
import com.mmry.glshoop.ware.entity.PurchaseDetailEntity;
import com.mmry.glshoop.ware.entity.PurchaseEntity;
import com.mmry.glshoop.ware.feign.ProductFeignService;
import com.mmry.glshoop.ware.service.PurchaseService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.ware.dao.WareSkuDao;
import com.mmry.glshoop.ware.entity.WareSkuEntity;
import com.mmry.glshoop.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    WareSkuService wareSkuService;

    @Autowired
    PurchaseDetailDao purchaseDetailDao;

    /**
     * wareId： skuId
     *
     * @param params 包含参数 wareId  skuId
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(skuId))
            wrapper.eq(WareSkuEntity::getSkuId, skuId);
        if (StringUtils.isNotEmpty(wareId))
            wrapper.eq(WareSkuEntity::getWareId, wareId);
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateInfo(List<Long> purchaseDetailIds) {
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailDao.selectBatchIds(purchaseDetailIds);

        purchaseDetailEntities.forEach(e -> {
            Long skuId = e.getSkuId();
            Long wareId = e.getWareId();
            WareSkuEntity entity = new WareSkuEntity();
            entity.setWareId(wareId);
            entity.setSkuId(skuId);
            entity.setStock(e.getSkuNum());
            entity.setStockLocked(0);
            LambdaQueryWrapper<WareSkuEntity> wrapper = new LambdaQueryWrapper<WareSkuEntity>().eq(WareSkuEntity::getSkuId, skuId).eq(WareSkuEntity::getWareId, wareId);
            WareSkuEntity wareSkuEntity = baseMapper.selectOne(wrapper);
            if (wareSkuEntity == null || wareSkuEntity.getId() == null) {
                //远程调用product服务获取信息
                R info = productFeignService.info(skuId);
                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                entity.setSkuName((String) skuInfo.get("skuName"));
                baseMapper.insert(entity);
            } else {
                entity.setStock(wareSkuEntity.getStock() + entity.getStock());
                baseMapper.update(entity, wrapper);
            }
        });
    }

}