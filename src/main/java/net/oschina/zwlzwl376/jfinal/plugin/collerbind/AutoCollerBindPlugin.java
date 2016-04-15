package net.oschina.zwlzwl376.jfinal.plugin.collerbind;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.config.Routes;

/**
 * Auto scanner tools  for Jfinal2.2
 * Along(ZengWeiLong)
 * AutoCollerBindPlugin 
 * 2016-3-20 10:42:52 
 *
 */
public class AutoCollerBindPlugin {

    private static Logger log = Logger.getLogger(AutoCollerBindPlugin.class);
    
    /**
     * packageName
     */
    private String packageName = "";

    /**
     * 
     * Along(ZengWeiLong)
     * packageName path com.web.entity
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
                log.info(folder.getPath() + " == > No have file ");
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
               String[] mapings = coller.value();
               String[] paths = coller.path();
               if(mapings != null){
                   for(int i = 0 ; i < mapings.length; i++){
                       String maping = mapings[i];
                       String path = null;
                       if(paths != null && paths.length > i){
                           path = paths[i];
                       }
                       if(StringUtils.isNotEmpty(maping)){
                           if(StringUtils.isNotEmpty(path)){
                               routes.add(maping, classes, path);
                               log.info(maping + " == > " + classes.getSimpleName() + " path = "+ path);
                           }else{
                               routes.add(maping, classes);
                               log.info(maping + " == > " + classes.getSimpleName());
                           }
                       }
                   }
               }
            }else{
                String defaultMapping = "/" + name.toLowerCase().replace("controller", "");
                routes.add(defaultMapping, classes);
                log.info(defaultMapping + " == > " + classes.getSimpleName());
            }            
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exception:" + e.getLocalizedMessage(), e);
        }
        return routes;
    }
}
