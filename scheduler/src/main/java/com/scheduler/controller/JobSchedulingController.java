package com.scheduler.controller;

import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.pojo.ScheduleJobPojo;
import com.scheduler.pojo.TaskDefinitionRestApiCallPojo;
import com.scheduler.service.TaskSchedulingService;

@EnableScheduling
@RestController
@RequestMapping("schedule/")
public class JobSchedulingController {

	@Autowired
	private TaskSchedulingService taskSchedulingService;

	@PostMapping(path = "/scheduleTask", consumes = "application/json", produces = "application/json")
	public void scheduleTask(@RequestBody TaskDefinitionRestApiCallPojo taskDefinitionRestApiCallPojo) {
		taskSchedulingService.scheduleTask(taskDefinitionRestApiCallPojo);
	}

	@PostMapping(path = "/getScheduledJobDetails", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ScheduleJobPojo> getScheduledJobDetails() throws SchedulerException {
		return taskSchedulingService.getScheduledJobDetails();
	}

}
