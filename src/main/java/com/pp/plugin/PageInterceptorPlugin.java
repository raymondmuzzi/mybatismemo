package com.pp.plugin;


import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import com.mysql.jdbc.PreparedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Properties;

//@Intercepts(
//        @Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)
//)
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        }
)
public class PageInterceptorPlugin implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
//        Object target = invocation.getTarget();
//        System.out.println("当前拦截的对象：" + target);
//        RoutingStatementHandler statementHandler = (RoutingStatementHandler) target;
//        MetaObject metaObject = SystemMetaObject.forObject(target);
//        Object value = metaObject.getValue("parameterHandler.parameterObject");
//        System.out.println("原来sql语句用的参数为：" + value);
//        metaObject.setValue("parameterHandler.parameterObject",11);
//        System.out.println("后来sql语句用的参数为：" + metaObject.getValue("parameterHandler.parameterObject"));

//        ParameterHandler parameterHandler = statementHandler.getParameterHandler();
//        Field field = parameterHandler.getClass().getField("parameterObject");
//        field.setAccessible(true);
//        field.set(parameterHandler,13);
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        System.out.println("ms:----->" + ms);
        Object result = invocation.proceed();

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println(properties);
    }
}
