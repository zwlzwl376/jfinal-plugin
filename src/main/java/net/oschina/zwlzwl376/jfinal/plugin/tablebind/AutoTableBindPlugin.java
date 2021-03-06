package net.oschina.zwlzwl376.jfinal.plugin.tablebind;

import java.io.File;

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
public class AutoTableBindPlugin {

    private static Logger log = Logger.getLogger(AutoTableBindPlugin.class);

    private String packageName = "";

    /**
     * Along(ZengWeiLong)
     * packageName com.web.entity
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
                log.info(folder.getPath() + " == > No have file ");
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
            String tableName = BindUtils.underscoreName(name);
            if (table != null) {
                tableName = table.value();
                if (StringUtils.isNotBlank(tableName)) {
                    arp.addMapping(tableName.trim(), classes);
                }
            } else {
                arp.addMapping(tableName, classes);
            }
            log.info(name + " == > " + classes.getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("exception:" + e.getLocalizedMessage(), e);
        }
        return arp;
    }
}
