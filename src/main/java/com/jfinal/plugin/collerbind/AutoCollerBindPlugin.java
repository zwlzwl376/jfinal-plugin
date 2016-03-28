package com.jfinal.plugin.collerbind;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.config.Routes;

/**
 * Auto scanner tools  for Jfinal2.2
 * @author Along(ZengWeiLong)
 * @ClassName: AutoCollerBindPlugin 
 * @date 2016年3月20日 上午10:42:52 
 *
 */
public class AutoCollerBindPlugin {

    private static Logger logger = Logger.getLogger(AutoCollerBindPlugin.class);
    /**
     * 包名
     */
    private String packageName = "";

    /**
     * 
     * @Author Along(ZengWeiLong)
     * @param packageName 所在包名 com.web.entity
     */
    public AutoCollerBindPlugin(String packageName) {
        this.packageName = packageName;
    }

    public Routes start(Routes routes) {
        String path = packageName.replace(".", File.separator);
        String root = this.getClass().getResource("/").toString().substring(6);
        File filePath = new File(root + path);
        return loop(filePath, packageName, routes);
    }

    private Routes loop(File folder, String packageName, Routes routes) {
        if (folder != null) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
                    File file = files[fileIndex];
                    if (file.isDirectory()) {
                        routes = loop(file, packageName + file.getName() + ".", routes);
                    } else {
                        routes = listMethodNames(file.getName(), packageName, routes);
                    }
                }
            } else {
                logger.info(folder.getPath() + " == > No have file ");
            }
        }
        return routes;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Routes listMethodNames(String filename, String packageName, Routes routes) {
        try {
            String name = filename.substring(0, filename.length() - 6);
            Class classes = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + name);
            Coller coller = (Coller) classes.getAnnotation(Coller.class);
            if(coller != null){
               String[] strArray = coller.value();
               if(strArray != null){
                   for(String maping:strArray){
                       if(StringUtils.isNotEmpty(maping)){
                           routes.add(maping, classes);
                           logger.info(maping + " == > " + classes.getSimpleName());
                       }
                   }
               }
            }else{
                String defaultMapping = "/" + name.toLowerCase().replace("controller", "");
                routes.add(defaultMapping, classes);
                logger.info(defaultMapping + " == > " + classes.getSimpleName());
            }            
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception:" + e.getLocalizedMessage(), e);
        }
        return routes;
    }
}
