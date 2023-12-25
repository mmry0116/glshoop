package com.mmry.glshoop.product.controller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmry.glshoop.product.entity.CategoryEntity;
import com.mmry.glshoop.product.service.CategoryService;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;


/**
 * 商品三级分类
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //后端vue获取树形菜单
    @RequestMapping("/list/tree")
    // @RequiresPermissions("generator:category:list")
    public R listWithTree() {
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        return R.ok().put("data", categoryEntities);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("generator:category:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    // @RequiresPermissions("generator:category:info")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("generator:category:save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("generator:category:update")
    public R update(@RequestBody CategoryEntity category) {
//        categoryService.updateById(category);
        categoryService.updateDetail(category);

        return R.ok();
    }
    //批量更新
    @RequestMapping("/updateBatch")
    public R updateBatch(@RequestBody CategoryEntity[] category) {
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("generator:category:delete")
    public R delete(@RequestBody Long[] catIds) {
       // categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
