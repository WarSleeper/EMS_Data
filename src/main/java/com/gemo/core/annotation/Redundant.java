package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EMS兼容字段配置
 * @author  
 * @date 2014-5-16
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Redundant {
	
	//主表对象
	Class<?> cls();
	//对应主表值字段 
	String valueField();
	//外键
	String field();
}
