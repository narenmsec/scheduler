package com.scheduler.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import com.scheduler.JobExecution.JobExecution;
import com.scheduler.pojo.ScheduleJobPojo;
import com.scheduler.pojo.TaskDefinitionRestApiCallPojo;

@Service
public class TaskSchedulingService {

	public void scheduleTask(TaskDefinitionRestApiCallPojo taskDefinitionRestApiCallPojo) {
		try {
			StdSchedulerFactory factory = new StdSchedulerFactory();
			Scheduler scheduler = factory.getScheduler();

			JobDataMap headerJobDataMap = new JobDataMap();
			headerJobDataMap.putAll(taskDefinitionRestApiCallPojo.getHeaders());

			JobDataMap bodyParamJobDataMap = new JobDataMap();
			bodyParamJobDataMap.putAll(taskDefinitionRestApiCallPojo.getBodyParams());

			JobDetail jobDetail = JobBuilder.newJob().ofType(JobExecution.class)
					.withIdentity("myJob", taskDefinitionRestApiCallPojo.getJobName())
					.usingJobData("url", taskDefinitionRestApiCallPojo.getUrl())
					.usingJobData("jobName", taskDefinitionRestApiCallPojo.getJobName())
					.usingJobData("cronExpression", taskDefinitionRestApiCallPojo.getCronExpression()).build();
			jobDetail.getJobDataMap().put("bodyParam", taskDefinitionRestApiCallPojo.getBodyParams());
			jobDetail.getJobDataMap().put("headerParam", taskDefinitionRestApiCallPojo.getHeaders());

//			SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity("myJob", "group1")
//					.forJob(jobDetail)
//					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(5))
//					.build();
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()
					.withIdentity("myTrigger", taskDefinitionRestApiCallPojo.getJobName())
					.withSchedule(CronScheduleBuilder.cronSchedule(taskDefinitionRestApiCallPojo.getCronExpression()))
					.forJob("myJob", taskDefinitionRestApiCallPojo.getJobName()).build();

			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();

		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public List<ScheduleJobPojo> getScheduledJobDetails() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		List<ScheduleJobPojo> jobList = new ArrayList<ScheduleJobPojo>();
		for (String groupName : scheduler.getJobGroupNames()) {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				ScheduleJobPojo job = new ScheduleJobPojo();
				String jobName = jobKey.getName();
				String jobGroup = jobKey.getGroup();
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				Date nextFireTime = triggers.get(0).getNextFireTime();
				System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);
				job.setJobId(jobGroup + "_" + jobName);
				job.setJobName(jobName);
				job.setJobGroup(jobGroup);
//				List<? extends Trigger> trigger = scheduler.getTriggersOfJob(jobKey);
//				job.setJobDescription(trigger.toString());
//				
//				scheduler.getTriggerGroupNames();
				
				jobList.add(job);
			}

		}
		return jobList;

	}

//	public List<ScheduleJob> getRunningJobs() throws SchedulerException {
//		  Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		  List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
//		  List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
//		  for (JobExecutionContext executingJob : executingJobs) {
//		    ScheduleJob job = new ScheduleJob();
//		    JobDetail jobDetail = executingJob.getJobDetail();
//		    JobKey jobKey = jobDetail.getKey();
//		    Trigger trigger = executingJob.getTrigger();
//		    job.setJobId(jobKey.getGroup() + "_" + jobKey.getName());
//		    job.setJobName(jobKey.getName());
//		    job.setJobGroup(jobKey.getGroup());
//		    job.setDesc(trigger.getDescription());
//		    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//		    job.setJobStatus(triggerState.name());
//		    if (trigger instanceof CronTrigger) {
//		      CronTrigger cronTrigger = (CronTrigger) trigger;
//		      String cronExpression = cronTrigger.getCronExpression();
//		      job.setCronExpression(cronExpression);
//		    }
//		    jobList.add(job);
//		  }
//		  return jobList;
//		}

}
