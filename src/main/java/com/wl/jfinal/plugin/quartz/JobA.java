package com.wl.jfinal.plugin.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobA implements Job{
    
    private static Logger logger = Logger.getLogger(JobA.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // TODO Auto-generated method stub
        logger.info("------execute JobA---------");
    }

}
