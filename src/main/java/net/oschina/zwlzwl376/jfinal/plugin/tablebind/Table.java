package net.oschina.zwlzwl376.jfinal.plugin.tablebind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * entity orm table Target
 * @author Along(ZengWeiLong)
 * @ClassName: Table 
 * @date 2016年4月6日 下午7:24:15 
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Table {

    String value() default "";
}
