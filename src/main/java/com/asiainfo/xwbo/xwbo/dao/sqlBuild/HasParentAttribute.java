package com.asiainfo.xwbo.xwbo.dao.sqlBuild;

import java.lang.annotation.*;

/**
 * 是否继承父类的属性
 * 默认不继承
 * @author t-xiabin
 * @date 2019年9月17日
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasParentAttribute {

	boolean value() default true;
}
