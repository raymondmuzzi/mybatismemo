package com.pp.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 多个插件就会产生多层代理
 *
 * 创建代理对象的时候，是按照插件配置顺序，创建层层代理对象
 * 执行目标方法的时候，按照逆向顺序执行
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)
)
public class MySecondPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MySecondPlugin...intercept:" + invocation.getMethod());
        Object result = invocation.proceed();
        return result;
    }

    @Override
    public Object plugin(Object target) {
        System.out.println("MySecondPlugin...plugin:" + target);
        Object wrap = Plugin.wrap(target, this);
        return wrap;
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println("MySecondPlugin...properties:" + properties);
    }
}