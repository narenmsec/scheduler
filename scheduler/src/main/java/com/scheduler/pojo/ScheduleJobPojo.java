package com.scheduler.pojo;

import lombok.Data;

@Data
public class ScheduleJobPojo {

	String jobId;

	String jobName;

	String jobGroup;

	String jobDescription;

	String jobStatus;

	String cronExpression;
}
