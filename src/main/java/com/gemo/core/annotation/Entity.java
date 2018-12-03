package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体
 * 
 * @author  
 * @date 2015年7月8日
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Entity {
	//实体名
	String name();
	//是否已作废
	boolean deprecated() default false;
}