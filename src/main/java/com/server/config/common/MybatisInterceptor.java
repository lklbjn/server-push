package com.server.config.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author lklbjn
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        String name = invocation.getMethod().getName();
        log.debug("Exec MethodName is {}", name);

        SqlCommandType sqlCommandType = null;

        //遍历处理所有参数，update方法有两个参数，参见Executor类中的update()方法。
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            // -- 第一个参数处理。根据它判断是否给“操作属性”赋值。
            if (arg instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();

                // 如果是“增加”或“更新”操作，则继续进行默认操作信息赋值。否则，则退出
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
                    continue;
                } else {
                    break;
                }
            }

            // -- 第二个参数处理。实际的请求参数

            // 如果是map，有两种情况：（1）使用@Param多参数传入，由Mybatis包装成map。（2）原始传入Map
            if (arg instanceof Map) {
                Map map = (Map) arg;
                putProperty(map, sqlCommandType);

                for (Object value : map.values()) {
                    if (value instanceof Collection) {
                        for (Object e : ((Collection) value)) {
                            setProperty(e, sqlCommandType);
                        }
                    } else {
                        setProperty(value, sqlCommandType);
                    }
                }

                //原始参数传入
            } else {
                setProperty(arg, sqlCommandType);
            }
        }
        return invocation.proceed();
    }

    private void putProperty(Map<String, Object> map, SqlCommandType sqlCommandType) {
        try {
            if (SqlCommandType.INSERT == sqlCommandType) {
                map.putIfAbsent("createTime", new Date());
                map.putIfAbsent("createUserId", -1);
                map.putIfAbsent("createUserName", "root");
            } else if (SqlCommandType.UPDATE == sqlCommandType) {
                map.putIfAbsent("updateTime", new Date());
                map.putIfAbsent("updateUserId", -1);
                map.putIfAbsent("updateUserName", "root");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void setProperty(Object obj, SqlCommandType sqlCommandType) {
        //空对象返回
        if (ObjectUtils.isEmpty(obj)) {
            return;
        }
        try {
            if (SqlCommandType.INSERT == sqlCommandType) {
                BeanUtils.setProperty(obj, "createTime", new Date());
                BeanUtils.setProperty(obj, "createUserId", -1);
                BeanUtils.setProperty(obj, "createUserName", "root");
            } else if (SqlCommandType.UPDATE == sqlCommandType) {
                BeanUtils.setProperty(obj, "updateTime", new Date());
                BeanUtils.setProperty(obj, "updateUserId", -1);
                BeanUtils.setProperty(obj, "updateUserName", "root");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);

    }

    @Override
    public void setProperties(Properties properties) {
    }

}