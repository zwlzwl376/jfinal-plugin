package net.oschina.zwlzwl376.jfinal.plugin.ioc.interceptor;

import java.lang.reflect.Field;

import net.oschina.zwlzwl376.jfinal.plugin.ioc.annotation.Autowired;
import net.oschina.zwlzwl376.jfinal.plugin.ioc.annotation.Resource;
import net.oschina.zwlzwl376.jfinal.plugin.ioc.plugin.IocPlugin;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * Ioc injection Interceptor
 */
public class DependencyInjectionInterceptor implements Interceptor{
	@Override
	public void intercept(Invocation inv) {
		try{
			Field[] fields = inv.getController().getClass().getDeclaredFields(); 
			for(Field f : fields){
				if(f!=null && f.isAnnotationPresent(Resource.class)){
					Resource resource = f.getAnnotation(Resource.class);
					Object value = null;
					if(resource.value()!=null&&!"".equals(resource.value())){
						value = IocPlugin.getInstance(resource.value());
					}else{
						value = IocPlugin.getInstance(f.getName());
					}
					f.setAccessible(true);
					f.set(inv.getController(), value);
				}
	
				if(f!=null && f.isAnnotationPresent(Autowired.class)){
					Object value = null;
					value = IocPlugin.getInstance(f.getType());
					f.setAccessible(true);
					f.set(inv.getController(), value);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		inv.invoke();
	}
}
