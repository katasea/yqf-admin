package com.main.service.prfm;

import com.main.pojo.platform.StateInfo;

import java.util.List;
import java.util.Map;

public interface CommonService {
	List<Map<String,Object>> getListInfos(String sqlstr);

	List<String> getList(String s);

	Map<String,Object> getMapInfo(String sqlstr);

	StateInfo valiadSelectSQL(String sqlstr);

}
