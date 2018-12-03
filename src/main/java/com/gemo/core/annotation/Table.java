package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gemo.core.constant.SchemaConstant.Schema;

/**
 * 表注解
 * @author  
 * @date 2015年7月8日
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {
	
	//模式
	Schema schema();
	//表名称
	String name();
	//说明
	String comment();
	//是否事务
	boolean transaction() default true;
	//索引
	Index[] indexes() default {};
	
}
