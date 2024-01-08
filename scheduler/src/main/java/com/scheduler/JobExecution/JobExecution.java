package com.scheduler.JobExecution;

import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class JobExecution implements Job {
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String url = dataMap.getString("url");
		String jobName = dataMap.getString("jobName");
		Map<String, String> bodyParam = (Map<String, String>) context.getMergedJobDataMap().get("bodyParam");
		Map<String, String> headers = (Map<String, String>) context.getMergedJobDataMap().get("headerParam");

		System.out.println("Running action: " + jobName);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		headers.forEach((headKey, headValue) -> {
			httpHeaders.set(headKey, headValue);
		});

		HttpEntity<Object> entity = new HttpEntity<>(bodyParam, httpHeaders);
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		System.out.println("Output+++++++++++" + response);
	}
}
