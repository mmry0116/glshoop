package com.mmry.glshoop.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mmry.glshoop.product.entity.ProductAttrValueEntity;
import com.mmry.glshoop.product.vo.AttrRespVO;
import com.mmry.glshoop.product.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmry.glshoop.product.entity.AttrEntity;
import com.mmry.glshoop.product.service.AttrService;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;


/**
 * 商品属性
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:23
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }
    //获取spu规格 product/attr/base/listforspu/{spuId}
    @RequestMapping("/base/listforspu/{spuId}")
    public R listforspu(@PathVariable("spuId")Long spuId) {
       List<ProductAttrValueEntity> data = attrService.listforspu(spuId);

        return R.ok().put("data", data);
    }

    //修改商品规格 /product/attr/update/{spuId}
    @RequestMapping("/update/{spuId}")
    public R updateBySpuId(@PathVariable("spuId")Long spuId,
                           @RequestBody List<ProductAttrValueEntity> productAttrValues) {
        attrService.updateBySpuId(spuId,productAttrValues);

        return R.ok();
    }

    /**
     * 分页查出所有属性列表  根据catelogId key
     *
     */
    //product/attr/sale/list/0
    @RequestMapping("/{attrType}/list/{catelogId}")
    // @RequiresPermissions("generator:attr:list")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId") Long catelogId,
                      @PathVariable("attrType") String attrType) {
        PageUtils page = attrService.queryBaseListPage(params, catelogId, attrType);

        return R.ok().put("page", page);
    }


    /**
     * 查出一个属性的详细 包括分组id 分类地址
     */
    @RequestMapping("/info/{attrId}")
    // @RequiresPermissions("generator:attr:info")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrEntity attr = attrService.getDetail(attrId);
        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("generator:attr:save")
    public R save(@RequestBody AttrVO attrVO) {
        attrService.saveDetail(attrVO);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("generator:attr:update")
    public R update(@RequestBody AttrVO attrVO) {
        attrService.updateDetail(attrVO);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("generator:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
