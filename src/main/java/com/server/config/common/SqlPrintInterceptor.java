package com.server.config.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

/**
 * @author lklbjn
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class SqlPrintInterceptor implements Interceptor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
        MetaObject metaObject = mappedStatement.getConfiguration().newMetaObject(parameter);

        Object[] params = boundSql.getParameterMappings().stream()
                .map(mapping -> {
                    String propertyName = mapping.getProperty();
                    return metaObject.hasGetter(propertyName) ? metaObject.getValue(propertyName)
                            : boundSql.hasAdditionalParameter(propertyName) ? boundSql.getAdditionalParameter(propertyName)
                            : "缺失";
                }).toArray();

        String sql = boundSql.getSql();
        for (Object param : params) {
            sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(param)));
        }
        //sql语句中多个空格都用一个空格代替
        sql = sql.replaceAll("[\\s]+", " ");
        log.info("执行的 SQL 语句：" + sql);
        return invocation.proceed();
    }

    /**
     * 如果参数是String，则添加单引号，
     * 如果是日期，则转换为时间格式器并加单引号；
     * 对参数是null和不是null的情况作了处理
     */
    private String getParameterValue(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return "'" + ((String) obj).replace("'", "") + "'";
        } else if (obj instanceof Date) {
            LocalDateTime dateTime = ((Date) obj).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return "'" + dateTime.format(FORMATTER) + "'";
        } else {
            return obj.toString();
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // do nothing
    }
}
