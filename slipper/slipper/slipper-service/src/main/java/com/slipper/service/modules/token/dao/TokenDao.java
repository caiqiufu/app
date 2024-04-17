package com.slipper.service.modules.token.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slipper.service.modules.token.entity.TokenEntity;

/**
 * 用户凭证
 *
 * @author gumingchen
 * @email 1240235512@qq.com
 * @date 1995-08-30 00:00:00
 */
@Mapper
public interface TokenDao extends BaseMapper<TokenEntity> {

	TokenEntity queryByToken(String token);

	TokenEntity selectById(Long id);
}
