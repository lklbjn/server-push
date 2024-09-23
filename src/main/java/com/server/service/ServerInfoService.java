package com.server.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.vps.ServerInfo;
import com.server.mapper.ServerInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author wangbinzhe
 */
@Service
public class ServerInfoService extends ServiceImpl<ServerInfoMapper, ServerInfo> {

    public List<ServerInfo> getVpsInfo() {
        return list();
    }
}
