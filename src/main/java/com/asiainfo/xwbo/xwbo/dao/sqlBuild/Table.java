package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

import java.lang.annotation.*;

/**
 * 表名注解类
 * 
 * dynamic为true时，表名可以支持时间动态<br/>
 * 表名格式如下：TABLENAME_${dateFormat}，默认以系统当前时间替换${dateFormat}<br/>
 * dateFormat格式为：yyyymmddhhmiss，不区分大小写<br/>
 * 如：test_${yyyymmdd}
 * 
 * @author t-xiabin
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
	/**
	 * 表名
	 * @return
	 */
	String value() default "";
	/**
	 * 主键
	 * @return
	 */
	String pks() default "";
	/**
	 * 是否为动态表名
	 * @return
	 */
	boolean dynamic() default false;
	
	/**
	 * 动态表名规则
	 * @return
	 */
	DynamicRule dynamicRule() default DynamicRule.date;
}
