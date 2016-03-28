package com.jfinal.plugin.velocity;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.view.ViewToolContext;

import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

/**
 * Velocity 模板插件复写类
 * 
 * @author Along(ZengWeiLong)
 * @ClassName: VelocityLayoutRender
 * @date 2016年3月14日 下午4:14:20
 *
 */
public class VelocityLayoutRender extends Render {

    private static Logger logger = Logger.getLogger(VelocityLayoutRender.class);
    
    private transient static final Properties properties = new Properties();
    //配置文件
    private static String configfile = "velocity.properties";
    //默认宏路径
    protected static String libraryDir;
    //默认宏文件
    protected static String defaultLibrar;
    //错误模板文件
    protected static String errorTmple;
    //布局模板路径
    protected static String layoutDir;
    //布局默认模板
    protected static String defaultLayout;
    
    static{
        String webPath = JFinal.me().getServletContext().getRealPath("/");
        properties.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, webPath);
        properties.setProperty(Velocity.ENCODING_DEFAULT, getEncoding());
        properties.setProperty(Velocity.INPUT_ENCODING, getEncoding());
        properties.setProperty(Velocity.OUTPUT_ENCODING, getEncoding());
        properties.setProperty(Velocity.VM_LIBRARY_AUTORELOAD, PropKit.use(configfile).get("library.directory"));
        Velocity.init(properties);
        //优先获取Velocity配置参数，如果配置参数为空则获取后面默认会值
        errorTmple = VelocityLayoutRender.properties.getProperty("tools.view.servlet.error.template", PropKit.use(configfile).get("error.template"));
        layoutDir = VelocityLayoutRender.properties.getProperty("tools.view.servlet.layout.directory",PropKit.use(configfile).get("layout.directory"));
        defaultLayout = VelocityLayoutRender.properties.getProperty("tools.view.servlet.layout.default.template",PropKit.use(configfile).get("default.template"));
        libraryDir = VelocityLayoutRender.properties.getProperty("tools.view.servlet.library.directory", PropKit.use(configfile).get("library.directory"));
        defaultLibrar = VelocityLayoutRender.properties.getProperty("tools.view.servlet.default.library", PropKit.use(configfile).get("default.library"));
        // preventive error checking! directory must end in /
        logger.info("VelocityRender: Error screen is '" + errorTmple + "'");
        logger.info("VelocityRender: Layout directory is '" + layoutDir + "'");
        logger.info("VelocityRender: Default layout template is '" + layoutDir + defaultLayout + "'");
        logger.info("VelocityRender: Default library template is '" + libraryDir + defaultLibrar + "'");
    }

    
    
    public VelocityLayoutRender(String view) {
        this.view = view;
    }

    /**
     * default value set
     * 
     * @Author:ALong (ZengWeiLong)
     * @param properties void
     * @date 2016年3月14日
     */
    public static void setProperties(Properties properties) {
        Set<Entry<Object, Object>> set = properties.entrySet();
        for (Iterator<Entry<Object, Object>> it = set.iterator(); it.hasNext();) {
            Entry<Object, Object> e = it.next();
            VelocityLayoutRender.properties.put(e.getKey(), e.getValue());
        }
    }

    public void render() {     
        PrintWriter writer = null;
        VelocityEngine velocityEngine = new VelocityEngine();
        ViewToolContext context = new ViewToolContext(velocityEngine, request, response, JFinal.me().getServletContext());
        try {            
            ToolManager tm = new ToolManager();
            tm.setVelocityEngine(velocityEngine);
            tm.configure(JFinal.me().getServletContext().getRealPath(PropKit.use(configfile).get("tools.xmlpath")));

            if (tm.getToolboxFactory().hasTools(Scope.REQUEST)) {
                context.addToolbox(tm.getToolboxFactory().createToolbox(Scope.REQUEST));
            }
            if (tm.getToolboxFactory().hasTools(Scope.APPLICATION)) {
                context.addToolbox(tm.getToolboxFactory().createToolbox(Scope.APPLICATION));
            }
            if (tm.getToolboxFactory().hasTools(Scope.SESSION)) {
                context.addToolbox(tm.getToolboxFactory().createToolbox(Scope.SESSION));
            }
            for (Enumeration<String> attrs = request.getAttributeNames(); attrs.hasMoreElements();) {
                String attrName = attrs.nextElement();
                context.put(attrName, request.getAttribute(attrName));
            }

            Template template = null;
            //读取宏
            Object objlibrary = context.get("library");
            String library = (objlibrary == null) ? null : objlibrary.toString();
            library = library == null ? libraryDir+defaultLibrar : libraryDir+library;
            try {
                // load the layout template
                template = Velocity.getTemplate(library);
            } catch (ResourceNotFoundException e) {
                logger.error("Can't load layout " + library, e);
                if (!library.equals(defaultLibrar)) {
                    template = Velocity.getTemplate(defaultLibrar);
                }
            }
            StringWriter bodyContent = new StringWriter();
            //合并宏
            template.merge(context, bodyContent);
            
            //读取视图
            template = Velocity.getTemplate(view);            
            StringWriter viewcontent = new StringWriter();
            //将视图写到sw
            template.merge(context, viewcontent);
            //将sw放入 content Map
            context.put("screen_content", viewcontent.toString());
                        
            //读取模板 合并模板
            response.setContentType("text/html;charset=" + getEncoding());
            writer = response.getWriter(); 
            Object objlayout = context.get("layout");
            String layout = (objlayout == null) ? null : objlayout.toString();
            layout = layout == null ? layoutDir+defaultLayout : layoutDir+layout;
            try {
                // load the layout template
                template = Velocity.getTemplate(layout);
            } catch (ResourceNotFoundException e) {
                logger.error("Can't load layout " + layout, e);
                if (!layout.equals(defaultLayout)) {
                        template = Velocity.getTemplate(defaultLayout);
                }
            }
            //合并模板
            template.merge(context, writer);             
            // flush and cleanup
            writer.flush(); 
        } catch (Exception e) {
            Template template = Velocity.getTemplate(errorTmple);
            response.setContentType("text/html;charset=" + getEncoding());
            try {
                writer = response.getWriter();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                logger.error("Service Error message: ", e);
            }
            //合并模板
            context.put("error_cause", e.fillInStackTrace());
            template.merge(context, writer);             
            // flush and cleanup
            writer.flush();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public static final class VelocityLayoutRenderFactory implements IMainRenderFactory {

        public Render getRender(String view) { 
            return new VelocityLayoutRender(view);
        }

        public String getViewExtension() {
            return ".html";
        }
    }

}
