package com.mmry.glshoop.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mmry.glshoop.ware.vo.DoneVO;
import com.mmry.glshoop.ware.vo.MergeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mmry.glshoop.ware.entity.PurchaseEntity;
import com.mmry.glshoop.ware.service.PurchaseService;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;


/**
 * 采购信息
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 19:10:09
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/received")
    public R receive(@RequestBody List<Long> purchaseIds) {
        purchaseService.receive(purchaseIds);

        return R.ok();
    }

    @PostMapping("/done")
    public R done(@RequestBody DoneVO doneVO ) {
        purchaseService.done(doneVO);

        return R.ok();
    }

    /**
     * 合并采购单详情
     *
     * @param mergeVO
     * @return
     */
    @RequestMapping("/merge")
    public R merge(@RequestBody MergeVO mergeVO) {
        purchaseService.merge(mergeVO);

        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    public R unReceive(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryunReceivePage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
