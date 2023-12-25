package com.mmry.glshoop.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mmry.common.utils.PageUtils;
import com.mmry.glshoop.member.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 13:56:23
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

