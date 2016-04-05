package com.jfinal.plugin.collerbind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Coller {
    /**
     * The value may indicate a suggestion for a logical component name,
     * to be turned into a Spring bean in case of an autodetected component.
     * @return the suggested component name, if any
     */
    String[] value() default {};
    
    /**
     * 
     * TODO(这里用一句话描述这个方法的作用) 
     * @Author:ALong (ZengWeiLong)
     * @return    
     * String[]
     * @date 2016年3月28日
     */
    String[] views() default "";
}
