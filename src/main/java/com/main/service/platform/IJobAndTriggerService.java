package com.main.service.platform;


import com.github.pagehelper.PageInfo;
import com.main.pojo.platform.JobAndTrigger;

public interface IJobAndTriggerService {
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize);
}
