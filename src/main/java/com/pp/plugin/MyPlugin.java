package com.pp.plugin;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

/**
 * 自定义MyBatis插件
 * 开发插件的时候要特别小心，特别容易破坏MyBatis的底层
 */
@Intercepts({
        // type:拦截的四大对象之一
        // method：拦截的四大对象的其中方法之一
        // args：方法参数
        @Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)
})
public class MyPlugin implements Interceptor {
    /**
     * 拦截目标对象的目标方法的执行
     * <p>
     * 目标方法真正执行操作是通过invocation.proceed()方法来执行的
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 执行目标方法，必须要执行这个方法，否则目标方法不会被调用
        Object result = invocation.proceed();
        System.out.println("被拦截的方法：" + invocation.getMethod());
        // 返回执行后的结果
        return result;
    }

    /**
     * 包装目标对象，为目标对象创建一个代理对象
     * 【根据签名，返回Signature中type类型对象的代理对象】
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        // 我们可以借助wrap方法，使用当前拦截器包装我们的目标对象
        System.out.println("MyFirstPlugin包装的对象：" + target.getClass());
        Object wrap = Plugin.wrap(target, this);
        System.out.println("MyFirstPlugin包装后的对象：" + wrap.getClass());
        // 返回为当前对象创建的代理对象
        return wrap;
    }

    /**
     * 将插件注册时的property设置进来
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的信息：" + properties);
    }
}