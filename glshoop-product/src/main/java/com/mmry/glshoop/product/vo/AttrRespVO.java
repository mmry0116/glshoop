package com.mmry.glshoop.product.vo;

import com.mmry.glshoop.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author : mmry
 * @date : 2023/12/12 15:21
 */
@Data
public class AttrRespVO extends AttrEntity {
    private String catelogName; //所属分类名字
    private String groupName;  //所属分组名字
    private Long attrGroupId; //分组id
    private List<Long> catelogPath; //[2, 34, 225] 分类完整路径
}
