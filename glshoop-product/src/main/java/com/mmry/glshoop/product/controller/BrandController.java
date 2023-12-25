package com.mmry.glshoop.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mmry.glshoop.product.exception.GlshoopExceptionControllerAdvice;
import com.mmry.glshoop.valid.AddGroup;
import com.mmry.glshoop.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mmry.glshoop.product.entity.BrandEntity;
import com.mmry.glshoop.product.service.BrandService;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    GlshoopExceptionControllerAdvice advice;
    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("generator:brand:list")
    public R list(@RequestParam Map<String, Object> params) {
//        PageUtils page = brandService.queryPage(params);
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    // @RequiresPermissions("generator:brand:info")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("generator:brand:save")
    public R save(@Validated(AddGroup.class) @RequestBody BrandEntity brand )  {
        //hasError 判断校验是否通过
//        if (result.hasErrors()) {
//            HashMap<String, String> map = new HashMap<>();
//            //FieldErrors 放着每个校验错误字段的信息
//            result.getFieldErrors().forEach(feild -> {
//                map.put(feild.getField(), feild.getDefaultMessage());
//            });
//            //将校验结果返回出去
//            return R.error(400, "提交的数据不合法").put("data", map);
//        }
        brandService.save(brand);
        System.out.println(advice);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("generator:brand:update")
    public R update(@RequestBody @Validated(UpdateGroup.class) BrandEntity brand) {
        //brandService.updateById(brand);
        brandService.updateDetail(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("generator:brand:delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
