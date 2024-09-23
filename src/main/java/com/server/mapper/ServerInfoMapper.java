package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.vps.ServerInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ServerInfoMapper extends BaseMapper<ServerInfo> {
}