package com.gitee.cs_liwei.annocache.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface AnnoCache {

	public String prefix() default ""; // key前缀

	public String key() default ""; // 指定缓存key

	public long seconds() default 600; // 缓存多少秒,默认10分钟
}
