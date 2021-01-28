package com.main.service.prfm;

import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.ParamBean;

import java.util.List;
import java.util.Map;

public interface ParamService {
	StateInfo insert(ParamBean paramBean);

	StateInfo update(ParamBean paramBean);

	StateInfo delecte(String paramsno);

	ParamBean get(String paramsno);

	int getCount(String keyWord, String paramstype);

	List<ParamBean> getPage(String keyWord, int start, int limit, String paramstype);

	StateInfo initData();

	/**
	 * 获取参数定义的SQL或JSON的具体值
	 *
	 * @return list有序集合
	 */
	List<Map<String,Object>> getParamsValueData(ParamBean bean);

	/**
	 * paramskey - beaninfo
	 * @return
	 */
	Map<String,ParamBean> getMap();

	/**
	 * 获取不显示的动态条件名称-随意值
	 *
	 * @param repid 报表ID
	 * @return {应用：1，渠道：1，*:1}
	 */
	Map<String,String> getRelaNoShowMap(String repid);
}
