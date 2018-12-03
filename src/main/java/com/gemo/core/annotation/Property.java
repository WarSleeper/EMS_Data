package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性
 * @author  
 * @date 2015年7月8日
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Property {

	//属性名称字段
	String propertyNameField() default "propertyName";
	//属性值字段
	String propertyValueField() default "propertyValue";
	
}
