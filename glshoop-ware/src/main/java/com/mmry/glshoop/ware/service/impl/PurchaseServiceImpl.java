package com.mmry.glshoop.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mmry.common.constant.PurchaseConstant;
import com.mmry.glshoop.ware.dao.PurchaseDetailDao;
import com.mmry.glshoop.ware.entity.PurchaseDetailEntity;
import com.mmry.glshoop.ware.entity.WareSkuEntity;
import com.mmry.glshoop.ware.service.PurchaseDetailService;
import com.mmry.glshoop.ware.service.WareSkuService;
import com.mmry.glshoop.ware.vo.DonePurchaseDetailVO;
import com.mmry.glshoop.ware.vo.DoneVO;
import com.mmry.glshoop.ware.vo.MergeVO;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.Query;

import com.mmry.glshoop.ware.dao.PurchaseDao;
import com.mmry.glshoop.ware.entity.PurchaseEntity;
import com.mmry.glshoop.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    WareSkuService wareSkuService;
    @Autowired
    PurchaseDetailDao purchaseDetailDao;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 查询未分配至的purchaseDetail
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryunReceivePage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PurchaseEntity::getStatus,
                PurchaseConstant.purchaseStatus.PURCHASE_CREATED_STATUS.getCode(),
                PurchaseConstant.purchaseStatus.PURCHASE_ALLOCATED_STATUS.getCode());
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVO mergeVO) {
        Long purchaseId = mergeVO.getPurchaseId();
        //如果没有采购单id 则新建采购单
        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(PurchaseConstant.purchaseStatus.PURCHASE_CREATED_STATUS.getCode());
            purchaseEntity.setCreateTime(new Date());
            baseMapper.insert(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        //更新采购详细表
        Long[] items = mergeVO.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(Arrays.asList(items));
        List<PurchaseDetailEntity> newEntities = purchaseDetailEntities.stream()
                .filter(e -> e.getStatus() <= PurchaseConstant.purchaseDetailStatus.PURCHASE_ALLOCATED_STATUS.getCode())
                .map(e -> {
                    e.setStatus(PurchaseConstant.purchaseDetailStatus.PURCHASE_ALLOCATED_STATUS.getCode());
                    e.setPurchaseId(finalPurchaseId);
                    return e;
                }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(newEntities);

        //修改采购表更新时间
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        baseMapper.updateById(purchaseEntity);
    }

    @Override
    public void receive(List<Long> purchaseIds) {
        List<PurchaseEntity> purchaseEntities = baseMapper.selectBatchIds(purchaseIds);
        //修改采购单状态为已领取
        List<Long> newPurchaseIds = new ArrayList<>();
        List<PurchaseEntity> newPurchaseEntities = purchaseEntities.stream()
                .filter(e -> e.getStatus() <= PurchaseConstant.purchaseStatus.PURCHASE_ALLOCATED_STATUS.getCode())
                .map(e -> {
                    newPurchaseIds.add(e.getId());
                    e.setUpdateTime(new Date());
                    e.setStatus(PurchaseConstant.purchaseStatus.PURCHASE_RECEIVE_STATUS.getCode());
                    return e;
                }).collect(Collectors.toList());

        if (newPurchaseEntities.size() > 0) {
            purchaseService.updateBatchById(newPurchaseEntities);
            //修改采购单对应的采购详情为正在采购中
            LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();
            List<Long> newPurcIds = newPurchaseEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
            wrapper.in(PurchaseDetailEntity::getPurchaseId, newPurcIds);
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailDao.selectList(wrapper);
            List<PurchaseDetailEntity> newPurchaseDetailEntitie = purchaseDetailEntities.stream()
                    .filter(e -> e.getStatus() <= PurchaseConstant.purchaseDetailStatus.PURCHASE_ALLOCATED_STATUS.getCode())
                    .map(e -> {
                        e.setStatus(PurchaseConstant.purchaseDetailStatus.PURCHASE_BUYING_STATUS.getCode());
                        return e;
                    }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(newPurchaseDetailEntitie);
        }
    }

    @Transactional
    @Override
    public void done(DoneVO doneVO) {
        List<PurchaseEntity> updatePs = new ArrayList<>();
        List<PurchaseDetailEntity> updatePDs = new ArrayList<>();
        //先修改采购项状态为已完成
        AtomicInteger purchaseStatus = new AtomicInteger(3);
        List<DonePurchaseDetailVO> purchaseDetail = doneVO.getItems();
        List<PurchaseDetailEntity> collect = purchaseDetail.stream().map(e -> {
            //如果采购项异常 将purchaseStatus 修改为4；
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            if (e.getStatus() == PurchaseConstant.purchaseDetailStatus.PURCHASE_FAIL_STATUS.getCode())
                purchaseStatus.set(4);
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(e.getItemId());
            purchaseDetailEntity.setStatus(e.getStatus());
            log.debug("purchaseStatus: " + purchaseStatus);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        //将采购项加入list中
        updatePDs.addAll(collect);

        //修改 采购单信息
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(doneVO.getId());
        purchaseEntity.setStatus(purchaseStatus.get());
        updatePs.add(purchaseEntity);

        purchaseDetailService.updateBatchById(updatePDs);
        purchaseService.updateBatchById(updatePs);

        //更新库存信息
        List<Long> purchaseDetailIds = updatePDs.stream()
                .filter(e -> e.getStatus() == PurchaseConstant.purchaseDetailStatus.PURCHASE_COMPLETE_STATUS.getCode())
                .map(e -> e.getId()).collect(Collectors.toList());

        wareSkuService.updateInfo(purchaseDetailIds);
    }

}