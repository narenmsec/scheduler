package com.scheduler.pojo;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TaskDefinitionRestApiCallPojo {
	private String cronExpression;
	private String jobName;
	private Map<String, String> bodyParams = new HashMap<>();
	private Map<String, String> headers = new HashMap<>();
	private String url;
}
