package net.oschina.zwlzwl376.jfinal.plugin.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.Enumeration;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * config job.properties run
 * Along(ZengWeiLong)
 * QuartzPlugin 
 * 2016-4-6 19:20:43 
 *
 */
public class QuartzPlugin{

    private static Logger log = LoggerFactory.getLogger(QuartzPlugin.class);
    
    private SchedulerFactory sf = null;
    
    private Scheduler sched = null;
    
    private String config = "job.properties";
    
    private Prop prop = null;

    public QuartzPlugin() {
        prop = PropKit.use(config);
    }
    
    public QuartzPlugin(String config) {
        this.config = config;
        prop = PropKit.use(this.config);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean start() {
        sf = new StdSchedulerFactory();
        try {
            sched = sf.getScheduler();
        } catch (SchedulerException e) {
            log.error(e.getMessage(),e);
            new RuntimeException(e);
        }
        Enumeration enums = prop.getProperties().keys();
        while (enums.hasMoreElements()) {
            String jobkey = enums.nextElement()+"";
            if (!jobkey.endsWith("job")) {
                continue;
            }
            String headkey = jobkey.substring(0, jobkey.indexOf("job"));
            String cronKey = headkey + "cron";
            String enableKey = headkey + "enable";
            if (isDisableJob(enableKey)) {
                continue;
            }
            String jobClassName = prop.get(jobkey) + "";
            String jobCronExp = prop.get(cronKey) + "";
            Class clazz;
            try {
                clazz = Class.forName(jobClassName);
            } catch (ClassNotFoundException e) {
                log.error(e.getMessage(),e);
                throw new RuntimeException(e);
            }
            JobDetail job = newJob(clazz).withIdentity(jobClassName, jobClassName).build();
            CronTrigger trigger = newTrigger().withIdentity(jobClassName, jobClassName).withSchedule(cronSchedule(jobCronExp)).build();
            Date ft = null;
            try {
                ft = sched.scheduleJob(job, trigger);
                sched.start();
            } catch (SchedulerException e) {
                log.error(e.getMessage(),e);
                new RuntimeException(e);
            }
            log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "+ trigger.getCronExpression());
        }
        return true;
    }

    /*close open*/
    private boolean isDisableJob(String enableKey) {
        return prop.getBoolean(enableKey) == false;
    }

    public boolean stop() {
        try {
            sched.shutdown();
        } catch (SchedulerException e) {
            log.error("shutdown error", e);
            return false;
        }
        return true;
    }

}
