package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
	GenerationType type() default GenerationType.UUID;

	String sequence() default "";
	
	/**
	 * 时间戳类型，默认为yyyyMMdd
	 * @return
	 */
	DateFormatType dateFormat() default DateFormatType.yyyyMMdd;
	
	/**
	 * 数据库类型，默认为ORACLE
	 * @return
	 */
	DbType dbType() default DbType.ORACLE;

}
