package com.server.service;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.config.common.SchedulerConfig;
import com.server.domain.vps.ServerInfo;
import com.server.domain.vps.vo.ServerInfoVO;
import com.server.mapper.ServerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lklbjn
 */
@Slf4j
@Service
public class ServerInfoService extends ServiceImpl<ServerInfoMapper, ServerInfo> {

    @Resource
    private  SchedulerConfig schedulerConfig;

    @Scheduled(cron = "#{@schedulerConfig.cron}")
    public void pushExpiredMessage() {
        LocalDate now = LocalDate.now();
        log.info("{}--开始检查快过期服务器", LocalDateTime.now());
        // 获取所有快过期的服务器
        List<ServerInfo> infos = lambdaQuery().lt(ServerInfo::getExpireEnd, now.plusDays(schedulerConfig.getExpiredDay()))
                .list();
        infos.forEach(info -> {
            String title = info.getBrand() + "的" + info.getArea() + "区域服务器即将过期";
            String message = "如需继续使用，请即刻赶往续费：" + info.getUrl();
            HttpRequest.post(schedulerConfig.getGotifyUrl())
                    .form("title", title)
                    .form("message", message)
                    .execute();
        });
    }

    public List<ServerInfoVO> getVpsInfo() {
        return ServerInfoVO.from(list());
    }
}
