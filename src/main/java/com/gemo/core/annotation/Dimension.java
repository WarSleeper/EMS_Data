package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gemo.core.enumeration.EMSDimension;

/**
 * 数据维度
 * @author  
 * @date 2014-5-26
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Dimension {

	//数据维度枚举
	EMSDimension dimension() default EMSDimension.No;
}
