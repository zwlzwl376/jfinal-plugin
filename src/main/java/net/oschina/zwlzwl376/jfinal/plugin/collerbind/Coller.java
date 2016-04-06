package net.oschina.zwlzwl376.jfinal.plugin.collerbind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * for collection name Target
 * @author Along(ZengWeiLong)
 * @ClassName: Coller 
 * @date 2016年4月6日 下午7:24:59 
 *
 */
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
     * @Author:ALong (ZengWeiLong)
     * @return    
     * String[]
     * @date 2016-3-28
     */
    String[] views() default "";
}
