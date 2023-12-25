package com.mmry.glshoop.member.dao;

import com.mmry.glshoop.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author mmry
 * @email 473747139@qq.com
 * @date 2023-11-22 13:56:23
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
