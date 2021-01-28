package com.main.dao.prfm;

import com.main.pojo.prfm.ParamBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ParamDao {
	int deleteByPrimaryKey(String paramsno);

	int insert(ParamBean record);

	int insertSelective(ParamBean record);

	ParamBean selectByPrimaryKey(String paramsno);

	ParamBean selectByKey(@Param("paramskey") String paramskey);

	List<ParamBean> selectByPage(@Param("keyWord") String keyWord, @Param("start") int start, @Param("end") int end, @Param("paramstype") String paramstype);

	int getCount(@Param("keyWord") String keyWord, @Param("paramstype") String paramstype);


	int updateByPrimaryKey(@Param("record") ParamBean record);

	List<Map<String,Object>> getRela(@Param("repid") String repid);
}
