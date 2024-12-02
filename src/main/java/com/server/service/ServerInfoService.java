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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lklbjn
 */
@Slf4j
@Service
public class ServerInfoService extends ServiceImpl<ServerInfoMapper, ServerInfo> {

    @Resource
    private SchedulerConfig schedulerConfig;

    @Scheduled(cron = "#{@schedulerConfig.cron}")
    public void pushExpiredMessage() {
        LocalDate now = LocalDate.now();
        log.info("{}--开始检查快过期服务器", LocalDateTime.now());
        // 获取所有快过期的服务器
        List<ServerInfo> infos = lambdaQuery().le(ServerInfo::getExpireStart, now)
                .ge(ServerInfo::getExpireEnd, now).list().stream().filter(info ->
                        info.getNotifyLimit() >= ChronoUnit.DAYS.between(now, info.getExpireEnd())).collect(Collectors.toList());
        log.info("快过期数量：{}。内容：{}", infos.size(), infos.stream().map(ServerInfo::getId).collect(Collectors.toSet()));
        infos.forEach(info -> {
            String title = info.getBrand() + "的" + info.getArea() + (info.getType() == 1 ? "区域服务器" : "") +
                    "即将过期[" + ChronoUnit.DAYS.between(now, info.getExpireEnd()) + "天]";
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
