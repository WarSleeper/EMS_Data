package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段
 * @author  
 * @date 2015年7月8日
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Column {

	//字段顺序
	int order();
	//字段名
	String name();
	//字段长度
	int length();
	//小数位数
	int scale() default 0;
	//允许空
	boolean nullable() default true;
	//说明
	String comment();
	
	
}
