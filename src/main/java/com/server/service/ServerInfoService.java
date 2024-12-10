package com.server.service;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.config.common.NotifyConfig;
import com.server.config.property.EmailProperty;
import com.server.mapper.ServerInfoMapper;
import com.server.model.enumeration.TypeEnum;
import com.server.model.vps.ServerInfo;
import com.server.model.vps.vo.ServerInfoVO;
import com.xxl.job.core.handler.annotation.XxlJob;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author lklbjn
 */
@Slf4j
@Service
public class ServerInfoService extends ServiceImpl<ServerInfoMapper, ServerInfo> {

    @Resource
    private NotifyConfig notifyConfig;

    @XxlJob("vpsCheck")
    public void pushExpiredMessage() {
        LocalDate now = LocalDate.now();
        log.info("{}--开始检查快过期服务器", LocalDateTime.now());
        // 获取所有快过期的服务器
        List<ServerInfo> infos = lambdaQuery().le(ServerInfo::getExpireStart, now)
                .ge(ServerInfo::getExpireEnd, now).list().stream().filter(info ->
                        info.getNotifyLimit() >= ChronoUnit.DAYS.between(now, info.getExpireEnd())).collect(Collectors.toList());
        log.info("快过期数量：{}。内容：{}", infos.size(), infos.stream().map(ServerInfo::getId).collect(Collectors.toSet()));
        infos.forEach(info -> {
            TypeEnum type = TypeEnum.getEnumByCode(info.getType());
            if (notifyConfig.getGotify().isEnabled()) {
                sendGotify(info, type, now);
            }
            if (notifyConfig.getEmail().isEnabled()) {
                sendEmail(info, type);
            }
        });
    }

    @XxlJob("alarmEmail")
    public void alarmEmail() {
        log.info("{}--进行报警邮件发送", LocalDateTime.now());
        int num = 1 / 0;
        if (num != 0) {
            log.info("{}--报警邮件发送成功", LocalDateTime.now());
        }
    }

    public List<ServerInfoVO> getVpsInfo() {
        return ServerInfoVO.from(list());
    }


    public void sendGotify(ServerInfo info, TypeEnum type, LocalDate now) {
        String title = info.getBrand() + "的" + (type.getNumber() == 1 ? info.getArea() + type.getCnName() : info.getArea()) +
                "即将过期[" + ChronoUnit.DAYS.between(now, info.getExpireEnd()) + "天]";
        String message = "如需继续使用，请即刻赶往续费：" + info.getUrl();
        HttpRequest.post(notifyConfig.getGotify().getUrl())
                .form("title", title)
                .form("message", message)
                .execute();
    }

    public void sendEmail(ServerInfo info, TypeEnum type) {
        log.info("准备发送邮件");
        EmailProperty email = notifyConfig.getEmail();
        // SMTP服务器信息
        final String username = email.getUsername();
        final String password = email.getPassword();
        // 发件人邮箱
        String from = email.getFrom();
        // 设置SMTP服务器属性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", email.getHost());
        properties.put("mail.smtp.auth", email.getSmtpAuth());
        properties.put("mail.smtp.port", email.getPort());
        properties.put("mail.smtp.socketFactory.class", email.getSocketFactoryClass());
        properties.put("mail.smtp.starttls.enable", email.getSmtpStarttlsEnable());
        // 设置连接和读取超时
        properties.put("mail.smtp.connectiontimeout", email.getConnectionTimeout());
        properties.put("mail.smtp.timeout", email.getTimeout());
        // 启用调试输出
        properties.put("mail.debug", email.getDebug());
        // 打印properties
        log.info("SMTP服务器属性：{}", properties);
        // 获取默认的Session对象
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            // 创建一个默认的MimeMessage对象
            Message message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(from));
            // 拆分收件人字符串并设置多个收件人
            String[] toAddresses = info.getEmailNotifyAddress() == null ? email.getDefaultTo().split(";")
                    : info.getEmailNotifyAddress().split(";");
            InternetAddress[] recipientAddresses = new InternetAddress[toAddresses.length];
            for (int i = 0; i < toAddresses.length; i++) {
                recipientAddresses[i] = new InternetAddress(toAddresses[i].trim());
            }
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            // 设置邮件主题
            message.setSubject("Test Email");
            // 设置邮件内容（HTML格式）
            // 配置FreeMarker
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(ServerInfoService.class, "/template");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            // 加载模板
            Template template = cfg.getTemplate("EmailNotifyTemplate.html");
            // 创建数据模型
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("typeName", type.getEnName());
            templateData.put("recipient", capitalizeFirstLetter(info.getRecipient() == null ? email.getRecipient() : info.getRecipient()));
            templateData.put("typeNameUpper", convertToSnakeCase(type.getEnName()));
            templateData.put("url", info.getUrl());
            // 合并模板和数据模型
            StringWriter stringWriter = new StringWriter();
            template.process(templateData, stringWriter);
            String htmlContent = stringWriter.toString();
            message.setContent(htmlContent, "text/html; charset=utf-8");
            // 发送邮件
            Transport.send(message);
            log.info("Email sent successfully!");
        } catch (MessagingException e) {
            log.error("Failed to send email: " + e.getMessage());
        } catch (TemplateException | IOException e) {
            log.error("Failed parse template: " + e.getMessage());
        }
    }

    public static String convertToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2")
                .toUpperCase();
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
