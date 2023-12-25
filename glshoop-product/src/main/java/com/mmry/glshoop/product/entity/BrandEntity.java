package com.mmry.glshoop.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.mmry.glshoop.valid.AddGroup;
import com.mmry.glshoop.valid.ListValue;
import com.mmry.glshoop.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-21 18:45:24
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@NotNull(message = "品牌Id不能为空" ,groups = {UpdateGroup.class})
	@Null(message = "新增不能指定Id" ,groups = {AddGroup.class})
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class}) //值不可以为null 空格
	//@NotEmpty(message = "品牌名不能为空")//该注解和notblank相似 但值可以为空格
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "logo不能为空",groups = {AddGroup.class})
	@URL(message = "logo必须是个url地址",groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "显示状态不能为空",groups = {AddGroup.class})
	@ListValue(vals = {0,1},groups = {AddGroup.class,UpdateGroup.class}/*,message = "显示状态必须为1或0"*/)
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "检索首字母不能为空",groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "必须是一个字母" ,groups = {AddGroup.class, UpdateGroup.class})//自定义校验规则
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序不嫩为空",groups = {AddGroup.class})
	@Min(value = 0 ,message = "值必须大于零",groups = {AddGroup.class,UpdateGroup.class})
	private Integer sort;

}
