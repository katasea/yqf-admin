package com.main.service.platform.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.main.dao.platform.JobAndTriggerMapper;
import com.main.pojo.platform.JobAndTrigger;
import com.main.service.platform.IJobAndTriggerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * quartz任务管理服务实现类
 */
@Service
public class JobAndTriggerImpl implements IJobAndTriggerService {

	@Resource
	private JobAndTriggerMapper jobAndTriggerMapper;
	
	public PageInfo<JobAndTrigger> getJobAndTriggerDetails(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails();
		PageInfo<JobAndTrigger> page = new PageInfo<JobAndTrigger>(list);
		return page;
	}

}