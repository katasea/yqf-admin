package com.main.service.platform;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
public interface QuartzServer extends Job{
	void execute(JobExecutionContext context) throws JobExecutionException;
}
