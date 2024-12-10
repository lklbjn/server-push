package com.server.config.property;


import lombok.Data;

/**
 * @author lklbjn
 */
@Data
public class EmailProperty {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * 收信人
     */
    private String recipient = "User";

    /**
     * 邮件服务器
     */
    private String host;

    /**
     * 端口
     */
    private Integer port = 25;

    /**
     * 账号
     */
    private String username;

    /**
     * 发件人
     */
    private String from;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否认证
     */
    private Boolean smtpAuth = true;

    /**
     * SSL
     */
    private String socketFactoryClass;

    /**
     * 是否开启startTLS
     */
    private Boolean smtpStarttlsEnable = false;

    /**
     * 默认收件人
     */
    private String defaultTo;

    /**
     * 连接超时(ms)
     */
    private Long connectionTimeout/* = 10000L*/;

    /**
     * 读取超时(ms)
     */
    private Long timeout/* = 10000L*/;

    /**
     * 是否开启debug
     */
    private Boolean debug = false;

}
