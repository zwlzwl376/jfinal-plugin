package com.wl.jfinal.plugin.tablebind;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
/**
 * Auto scanner tools  for Jfinal2.2
 * @author Along(ZengWeiLong)
 * @ClassName: AutoTableBindPlugin 
 * @date 2016年3月20日 上午10:42:52 
 *
 */
public class AutoTableBindPlugin {

    private static Logger logger = Logger.getLogger(AutoTableBindPlugin.class);
    /**
     * 实体所在包名
     */
    private String packageName = "";

    /**
     * 
     * @Author Along(ZengWeiLong)
     * @param packageName 所在包名 com.web.entity
     */
    public AutoTableBindPlugin(String packageName) {
        this.packageName = packageName;
    }

    public ActiveRecordPlugin start(ActiveRecordPlugin arp) {
        String path = packageName.replace(".", File.separator);
        String root = this.getClass().getResource("/").toString().substring(6);
        File filePath = new File(root + path);
        return loop(filePath, packageName, arp);
    }

    private ActiveRecordPlugin loop(File folder, String packageName, ActiveRecordPlugin arp) {
        if (folder != null) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
                    File file = files[fileIndex];
                    if (file.isDirectory()) {
                        arp = loop(file, packageName + file.getName() + ".", arp);
                    } else {
                        arp = listMethodNames(file.getName(), packageName, arp);
                    }
                }
            } else {
                logger.info(folder.getPath() + " == > No have file ");
            }
        }
        return arp;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ActiveRecordPlugin listMethodNames(String filename, String packageName, ActiveRecordPlugin arp) {
        try {
            String name = filename.substring(0, filename.length() - 6);
            Class classes = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + name);
            Table table = (Table) classes.getAnnotation(Table.class);
            if(table != null){
                String tableName = table.value(); 
                if(StringUtils.isNotEmpty(tableName)){
                    arp.addMapping(tableName, classes);
                }
            }else{
                arp.addMapping(name.toLowerCase(), classes);
            }
            logger.info(name.toLowerCase() + " == > " + classes.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("exception:" + e.getLocalizedMessage(), e);
        }
        return arp;
    }
}
