package com.mmry.glshoop.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.mmry.glshoop.product.entity.AttrEntity;
import com.mmry.glshoop.product.vo.AttrAttrGroupRelationVO;
import com.mmry.glshoop.product.vo.AttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mmry.glshoop.product.entity.AttrGroupEntity;
import com.mmry.glshoop.product.service.AttrGroupService;
import com.mmry.common.utils.PageUtils;
import com.mmry.common.utils.R;


/**
 * 属性分组
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@RestController
@RequestMapping("/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    /**
     * @author : mmry
     * @date : 2023/12/12 14:17
     * 获取某个分类下所有属性分组
     */
    @RequestMapping("/list/{categoryId}")
    // @RequiresPermissions("generator:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") String categoryId) {
        PageUtils page = attrGroupService.queryPage(params, categoryId);
        System.out.println(page);
        return R.ok().put("page", page);
    }

    //获取属性分组没有关联的其他属性
    ///product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R NoBindAttr(@RequestParam() Map<String, Object> params,
                        @PathVariable("attrGroupId") Long attrGroupId) {
        PageUtils page = attrGroupService.queryNoBindAttr(attrGroupId, params);
        return R.ok().put("page", page);
    }

    @GetMapping("/{attrGroupId}/attr/relation")
    // @RequiresPermissions("generator:attrgroup:list")
    public R listAttr(@PathVariable("attrGroupId") Long attrGroupId) {
        //    PageUtils page = attrGroupService.queryPage(params);
        List<AttrEntity> page = attrGroupService.ListAttr(attrGroupId);
        return R.ok().put("data", page);
    }

    //删除属性与分组的关联关系
    ///product/attrgroup/attr/relation/delete
    @PostMapping("/attr/relation/delete")
    // @RequiresPermissions("generator:attrgroup:list")
    public R deleteBatch(@RequestBody List<AttrAttrGroupRelationVO> idss) {
        //    PageUtils page = attrGroupService.queryPage(params);
        List<AttrEntity> page = attrGroupService.deleteBatch(idss);
        return R.ok();
    }


    // 添加属性与分组关联关系
    ///product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R attrsRelation(@RequestBody List<AttrAttrGroupRelationVO> relations) {
        attrGroupService.saveAttrsRelation(relations);
        return R.ok();
    }

    //获取分类下所有分组&关联属性
    // product/attrgroup/{catelogId}/withattr
    @GetMapping("/{catelogId}/withattr")
    public R attrsRelation(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupVo> data = attrGroupService.ListAttrGroupWithAttr(catelogId);
        return R.ok().put("data", data);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    // @RequiresPermissions("generator:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getByIdContaincategoryPath(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //   @RequiresPermissions("generator:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //   @RequiresPermissions("generator:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("generator:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
