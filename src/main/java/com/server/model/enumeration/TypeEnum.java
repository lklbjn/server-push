package com.server.model.enumeration;

import lombok.Getter;

/**
 * 状态枚举
 *
 * @author lklbjn
*/
@Getter
public enum TypeEnum {
    // 1服务器 2邮件服务器 3域名 4GoogleVoice
    /** 服务器*/
    HOT_WORK(1,"服务器","Server"),
    /** 邮件服务器*/
    CONFINED_SPACE_WORK(2,"邮件服务器","EmailServer"),
    /** 域名*/
    HIGH_ALTITUDE_WORK(3,"域名","Domain"),
    /** GoogleVoice*/
    TEMPORARY_ELECTRICITY_USAGE_WORK(4,"谷歌语音","GoogleVoice"),
    /** 未知*/
    UNKNOWN(-1,"未知","UNKNOWN"),
    ;
   private final Integer number;
   private final String cnName;
   private final String enName;
    TypeEnum(Integer number, String cnName, String enName) {
        this.number = number;
        this.cnName = cnName;
        this.enName = enName;
    }
    public static TypeEnum getEnumByCode(Integer number) {
        for (TypeEnum type : TypeEnum.values()) {
            if (type.getNumber().equals(number)) {
                return type;
            }
        }
        return UNKNOWN;
    }

}