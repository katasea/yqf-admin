package com.main.service.prfm.impl;

import com.main.dao.platform.CommonDao;
import com.main.pojo.platform.StateInfo;
import com.main.service.prfm.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class CommonServiceImpl implements CommonService {
	@Resource
	private CommonDao commonDao;

	@Override
	public List<Map<String, Object>> getListInfos(String sqlstr) {
		if(this.valiadSelectSQL(sqlstr).getFlag()) {
			return commonDao.getListInfos(sqlstr);
		}else {
			return null;
		}
	}

	@Override
	public List<String> getList(String s) {

		if(this.valiadSelectSQL(s).getFlag()) {
			return commonDao.getList(s);
		}else {
			return null;
		}
	}

	@Override
	public Map<String, Object> getMapInfo(String sqlstr) {
		if(this.valiadSelectSQL(sqlstr).getFlag()) {
			return commonDao.getMapInfo(sqlstr);
		}else {
			return null;
		}
	}

	/**
	 * 过滤风险SQL
	 *
	 * @param sqlstr SQL字符串
	 */
	@Override
	public StateInfo valiadSelectSQL(String sqlstr) {
		StateInfo stateInfo = new StateInfo();
		sqlstr = sqlstr.toUpperCase();
		String beforeFrom = sqlstr.indexOf("FROM")!=-1?sqlstr.substring(0,sqlstr.lastIndexOf("FROM")):"";
		String setFrom = sqlstr.indexOf("SET")!=-1?sqlstr.substring(0,sqlstr.lastIndexOf("SET")):"";
		if(beforeFrom.contains("DELETE")) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"检测到非法语句，应该为查询结果为删除",null);
		}
		if(setFrom.contains("UPDATE")) {
			stateInfo.setFlag(false);
			stateInfo.setMsg(this.getClass(),"检测到非法语句，应该为查询结果为修改",null);
		}
		return stateInfo;
	}
}
