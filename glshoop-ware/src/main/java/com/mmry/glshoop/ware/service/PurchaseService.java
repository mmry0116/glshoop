package com.mmry.glshoop.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.ware.entity.PurchaseEntity;
import com.mmry.glshoop.ware.vo.DoneVO;
import com.mmry.glshoop.ware.vo.MergeVO;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:10:09
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryunReceivePage(Map<String, Object> params);

    void merge(MergeVO mergeVO);

    void receive(List<Long> purchaseIds);

    void done(DoneVO doneVO);
}

