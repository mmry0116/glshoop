package com.mmry.glshoop.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.mmry.glshoop.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author : mmry
 * @date : 2023/12/15 12:47
 */
@Data
public class AttrGroupVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
