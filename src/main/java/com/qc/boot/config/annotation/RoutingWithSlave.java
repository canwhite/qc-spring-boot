package com.qc.boot.config.annotation;

/**
 * 功能描述: ano
 *
 * @author lijinhua
 * @date 2022/8/24 15:31
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 如果参数名称是value，且只有一个参数，那么可以省略参数名称
 * @Check(99) // @Check(value=99)
 * //给x赋值为99，
 * public int x;
*/
//这里都不需要值，只要挂载这个ano 名字，我们就知道要干啥了
@Retention(RUNTIME)
@Target(METHOD)
public @interface RoutingWithSlave {
    //String value() default "";
}
