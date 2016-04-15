package net.oschina.zwlzwl376.jfinal.plugin.collerbind;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.oschina.zwlzwl376.jfinal.plugin.utils.FileScanner;

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
public class RoutesScanner {

    private static Logger log = Logger.getLogger(RoutesScanner.class);
    
    private List<String> packages = new ArrayList<String>();

    public RoutesScanner(){
        
    }
    
    /**
     * 
     * Along(ZengWeiLong)
     * packageName path com.web.entity
     */
    public RoutesScanner(String packageName) {
        if(StringUtils.isNotBlank(packageName)){
            this.packages.add(packageName);
        }
    }
    
    public void addScanner(String packageName) {
        if(StringUtils.isNotBlank(packageName)){
            this.packages.add(packageName);
        }
    }

    public Routes start(Routes routes) {
        for(String packageName:packages){
            File pagePath = new File(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).getFile());
            if (!pagePath.exists() || !pagePath.isDirectory()) {
                return null;
            }
            List<File> fileList = FileScanner.scannPage(pagePath.getAbsolutePath(), "*.class");
            for (int i = 0; i < fileList.size(); i++) {
                routes = this.listMethodNames(fileList.get(i).getName(), packageName, routes);
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
                       if(StringUtils.isNotBlank(maping)){
                           if(StringUtils.isNotBlank(path)){
                               routes.add(maping, classes, path);
                           }else{
                               routes.add(maping, classes);
                           }
                           log.info(maping + " == > " + classes.getSimpleName());
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
