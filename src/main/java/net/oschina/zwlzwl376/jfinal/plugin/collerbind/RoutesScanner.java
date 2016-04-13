package net.oschina.zwlzwl376.jfinal.plugin.collerbind;

import java.io.File;
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
    
    /**
     * packageName
     */
    private String packageName = "";

    /**
     * 
     * Along(ZengWeiLong)
     * packageName path com.web.entity
     */
    public RoutesScanner(String packageName) {
        this.packageName = packageName;
    }

    public Routes start(Routes routes) {
        File pagePath = new File(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).getFile());
        if (!pagePath.exists() || !pagePath.isDirectory()) {
            return null;
        }
        List<File> fileList = FileScanner.scannPage(pagePath.getAbsolutePath(), "*.class");
        for (int i = 0; i < fileList.size(); i++) {
            routes = this.listMethodNames(fileList.get(i).getName(), packageName, routes);
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
               String[] views = coller.views();
               if(strArray != null){
                   for(int i = 0 ; i < strArray.length; i++){
                       String maping = strArray[i];
                       String view = null;
                       if(views != null && views.length > i){
                           view = views[i];
                       }
                       if(StringUtils.isNotEmpty(maping)){
                           if(StringUtils.isNotEmpty(view)){
                               routes.add(maping, classes,view);
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
