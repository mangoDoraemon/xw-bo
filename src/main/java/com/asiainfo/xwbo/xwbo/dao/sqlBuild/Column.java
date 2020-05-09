package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

import java.lang.annotation.*;

/** 
 * 字段注解
 *
 */
@Documented 
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 
public @interface Column {
	
	String value();
}
