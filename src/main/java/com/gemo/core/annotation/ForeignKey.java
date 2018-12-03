package com.gemo.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联表字段与主表对应
 * @author  
 * @date 2014-5-14
 * @version v1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ForeignKey {

	//主表对象
	Class<?> mainClass();
}
