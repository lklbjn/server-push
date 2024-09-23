package com.server.controller;

import com.server.domain.vps.ServerInfo;
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
    ServerInfoService serverInfoService;

    @ApiOperation(value = "获取服务器信息", notes = "获取服务器信息")
    @GetMapping("info")
    public Result<List<ServerInfo>> getVpsInfo() {
        return Result.ok(serverInfoService.getVpsInfo());
    }

}
