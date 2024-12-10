package com.server.controller;

import com.server.config.common.SchedulerConfig;
import com.server.model.vps.vo.ServerInfoVO;
import com.server.exception.Result;
import com.server.service.ServerInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * VPS服务器info
 *
 * @author lklbjn
 * @DATE 2023/9/26 16:51
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/vps")
@Api(value = "VPS信息请求类", tags = {"VPS信息请求类"})
public class ServerController {

    @Resource
    private SchedulerConfig schedulerConfig;
    @Resource
    private ServerInfoService serverInfoService;

    @ApiOperation(value = "获取服务器信息", notes = "获取服务器信息")
    @GetMapping("info")
    public Result<List<ServerInfoVO>> getVpsInfo() {
        return Result.ok(serverInfoService.getVpsInfo());
    }

    @ApiOperation(value = "检查快过期服务器", notes = "检查快过期服务器")
    @GetMapping("check")
    public Result<String> pushExpiredMessage() {
        if (Boolean.FALSE.equals(schedulerConfig.getEnableCheck())) {
            return Result.error("Unable");
        }
        serverInfoService.pushExpiredMessage();
        return Result.ok();
    }

}
