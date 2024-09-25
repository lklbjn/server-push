package com.server.domain.vps.vo;

import com.server.domain.vps.ServerInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务器信息
 *
 * @author lklbjn
 */
@Data
@ApiModel(description = "服务器信息")
public class ServerInfoVO implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * aff地址
     */
    @ApiModelProperty(value = "aff地址")
    private String url;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private String price;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDate expireStart;

    /**
     * 到期时间
     */
    @ApiModelProperty(value = "到期时间")
    private LocalDate expireEnd;

    private static final long serialVersionUID = 1L;


    public static ServerInfoVO from(ServerInfo serverInfo) {
        if (serverInfo == null) {
            return null;
        }
        ServerInfoVO serverInfoVO = new ServerInfoVO();
        serverInfoVO.setId(serverInfo.getId());
        serverInfoVO.setUrl(serverInfo.getUrl());
        serverInfoVO.setPrice(serverInfo.getPrice());
        serverInfoVO.setExpireStart(serverInfo.getExpireStart());
        serverInfoVO.setExpireEnd(serverInfo.getExpireEnd());

        // Not mapped ServerInfo fields:
        // brand
        // area
        // deleted
        return serverInfoVO;
    }

    public static List<ServerInfoVO> from(List<ServerInfo> serverInfos) {
        return serverInfos.stream().map(ServerInfoVO::from).collect(Collectors.toList());
    }

}