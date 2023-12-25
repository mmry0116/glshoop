package com.mmry.glshoop.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.service.CategoryBrandRelationService;
import com.mmry.glshoop.product.vo.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmry.glshoop.product.entity.CategoryBrandRelationEntity;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@RestController
@RequestMapping("/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService relationService;

    //获取分类关联的品牌
    ///product/categorybrandrelation/brands/list
    @RequestMapping("/brands/list")
    public R listBrands(@RequestParam("catId") Long catId) {
        List<BrandEntity> brandEntities = relationService.listBrandsByCatId(catId);
        if (brandEntities == null)
            return R.ok().put("data", null);
        List<BrandVO> data = brandEntities.stream().map(entity -> {
            BrandVO brandVO = new BrandVO();
            brandVO.setBrandId(entity.getBrandId());
            brandVO.setBrandName(entity.getName());
            return brandVO;
        }).collect(Collectors.toList());
        return R.ok().put("data", data);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("generator:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = relationService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/catelog/list")
    // @RequiresPermissions("generator:categorybrandrelation:list")
    public R catelogList(@RequestParam Long brandId) {
//        PageUtils page = relationService.queryPage(params);
        List<CategoryBrandRelationEntity> list = relationService.catelogList(brandId);

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("generator:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = relationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("generator:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        //relationService.save(categoryBrandRelation);
        relationService.saveDetail(categoryBrandRelation);


        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("generator:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        relationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("generator:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        relationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
