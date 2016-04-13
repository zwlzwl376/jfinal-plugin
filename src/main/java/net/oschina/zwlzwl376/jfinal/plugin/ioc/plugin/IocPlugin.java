package net.oschina.zwlzwl376.jfinal.plugin.ioc.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.oschina.zwlzwl376.jfinal.plugin.ioc.annotation.Ioc;
import net.oschina.zwlzwl376.jfinal.plugin.utils.BindUtils;
import net.oschina.zwlzwl376.jfinal.plugin.utils.FileScanner;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.jfinal.plugin.IPlugin;

public class IocPlugin implements IPlugin {

    private static boolean isSingleton;

    private static final Map<Object, Class<?>> interClazzMap = new ConcurrentHashMap<Object, Class<?>>();

    private static final Map<String, Class<?>> nameClazzMap = new ConcurrentHashMap<String, Class<?>>();

    private static final Map<Object, Object> instanceMap = new ConcurrentHashMap<Object, Object>();

    /**
     * Along(ZengWeiLong)
     * packageName com.web.entity
     */
    public IocPlugin addPackage(String packageName, boolean isSingleton) {
        IocPlugin.isSingleton = isSingleton;
        File dir = new File(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).getFile());
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        List<File> fileList = FileScanner.scannPage(dir.getAbsolutePath(), "*.class");
        for (File file : fileList) {
            ClassReader reader = null;
            ClassNode cn = null;
            try {
                reader = new ClassReader(new FileInputStream(file.getAbsolutePath())); // 解析class文件
                cn = new ClassNode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            reader.accept(cn, 0);
            String className = cn.name.replaceAll("/", ".");
            try {
                Class<?> clazz = Class.forName(className);
                Class<?>[] interfaces = clazz.getInterfaces();
                Ioc ioc = clazz.getAnnotation(Ioc.class);
                if (ioc != null) {
                    if (isSingleton) {
                        if (ioc.value().equals("")) {
                            instanceMap.put(BindUtils.headLower(clazz.getSimpleName()), clazz.newInstance()); 
                        } else {
                            instanceMap.put(ioc.value(), clazz.newInstance());
                        }
                        instanceMap.put(clazz, clazz.newInstance());
                        for (Class<?> inter : interfaces) {
                            instanceMap.put(inter, clazz.newInstance());
                        }
                    } else {
                        if (ioc.value().equals("")) {
                            addNameClass(BindUtils.headLower(clazz.getSimpleName()), clazz);
                        } else {
                            addNameClass(ioc.value(), clazz);
                        }
                        for (Class<?> inter : interfaces) {
                            addInterClass(inter, clazz);
                        }
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public void addNameClass(String name, Class<?> clazz) {
        nameClazzMap.put(name, clazz);
    }

    public void addInterClass(Object inter, Class<?> clazz) {
        interClazzMap.put(inter, clazz);
    }

    public static Object getInstance(String name) {
        if (isSingleton) {
            return instanceMap.get(name);
        } else {
            try {
                return nameClazzMap.get(name).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static Object getInstance(Class<?> clazz) {
        try {
            if (isSingleton) {
                return instanceMap.get(clazz);
            } else {
                return clazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean start() {
        for (Entry<Object, Object> entry : instanceMap.entrySet()) {
            Logger.getLogger(this.getClass()).info(entry.getKey() + " - " + entry.getValue() + " has been created");
        }
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }

    public static boolean isSingleton() {
        return isSingleton;
    }

    public static void setSingleton(boolean isSingleton) {
        IocPlugin.isSingleton = isSingleton;
    }

}
