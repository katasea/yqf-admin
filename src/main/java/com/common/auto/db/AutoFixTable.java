package com.common.auto.db;

import com.main.pojo.platform.StateInfo;

/**
 * 自动扫描pojo，进行注解解析生成表结构语句并维护表结构
 * @author chenfuqiang
 *
 */
public interface AutoFixTable {
	public abstract StateInfo run(int year);
}
