package com.server.domain.vps;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 服务器信息
 * @author wangbinzhe
 */
@ApiModel(description="服务器信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "vps_info")
public class ServerInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * aff地址
     */
    @TableField(value = "url")
    @ApiModelProperty(value="aff地址")
    private String url;

    /**
     * 价格
     */
    @TableField(value = "price")
    @ApiModelProperty(value="价格")
    private String price;

    /**
     * 开始时间
     */
    @TableField(value = "expire_start")
    @ApiModelProperty(value="开始时间")
    private LocalDate expireStart;

    /**
     * 到期时间
     */
    @TableField(value = "expire_end")
    @ApiModelProperty(value="到期时间")
    private LocalDate expireEnd;

    /**
     * 是否删除
     */
    @TableField(value = "deleted")
    @ApiModelProperty(value="是否删除")
    private Integer deleted;
    private static final long serialVersionUID = 1L;
}