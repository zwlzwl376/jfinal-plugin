package net.oschina.zwlzwl376.jfinal.plugin.tablebind;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.oschina.zwlzwl376.jfinal.plugin.utils.BindUtils;
import net.oschina.zwlzwl376.jfinal.plugin.utils.FileScanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Auto scanner tools for Jfinal2.2
 * 
 * Along(ZengWeiLong)
 * AutoTableBindPlugin
 * 2016-3-20 10:42:52
 *
 */
public class TablesScanner {

    private static Logger log = Logger.getLogger(TablesScanner.class);

    private List<String> packages = new ArrayList<String>();

    
    public TablesScanner(){
        
    }

    /**
     * Along(ZengWeiLong)
     * package com.web.entity
     */
    public TablesScanner(String packageName) {
        if(StringUtils.isNotBlank(packageName)){
            this.packages.add(packageName);
        }
    }
    
    public void addScanner(String packageName) {
        if(StringUtils.isNotBlank(packageName)){
            this.packages.add(packageName);
        }
    }

    public ActiveRecordPlugin start(ActiveRecordPlugin arp) {
        for(String packageName:packages){
            File pagePath = new File(this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).getFile());
            if (!pagePath.exists() || !pagePath.isDirectory()) {
                return null;
            }
            List<File> fileList = FileScanner.scannPage(pagePath.getAbsolutePath(), "*.class");
            for (int i = 0; i < fileList.size(); i++) {
                arp = this.listMethodNames(fileList.get(i).getName(), packageName, arp);
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
            name = BindUtils.underscoreName(name);
            if (table != null) {
                String tableName = table.value();
                if (StringUtils.isNotBlank(tableName)) {
                    String pkName = table.pkName();
                    if(StringUtils.isNotBlank(pkName)){
                        arp.addMapping(tableName.trim(),pkName.trim(),classes);
                    }else{
                        arp.addMapping(tableName.trim(),classes);
                    }
                }
            } else {
                arp.addMapping(name.trim(), classes);
            }
            log.info(name + " == > " + classes.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exception:" + e.getLocalizedMessage(), e);
        }
        return arp;
    }
}
