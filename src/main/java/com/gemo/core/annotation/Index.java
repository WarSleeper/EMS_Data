package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引注解
 * @author  
 * @date 2015年7月8日
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Index {

	//索引字段
	String[] columns();
	//是否唯一索引
	boolean unique() default false;
}
