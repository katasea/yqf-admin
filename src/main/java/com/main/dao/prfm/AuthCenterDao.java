package com.main.dao.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.AuthCenter;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuthCenterDao {

	List<Map<String,Object>> getPageResult(@Param("sqlstr") String sqlstr, @Param("start") int start, @Param("end") int end);

	int getCountResult(@Param("sqlstr") String sqlstr);

	List<Map<String,Object>> getAuthPage(@Param("keyword") String keyword, @Param("funcode") String funcode, @Param("bridge") Bridge bridge,@Param("morig") String morig,@Param("start") int start, @Param("end") int end);

	int getAuthCount(@Param("keyword") String keyword, @Param("funcode") String funcode, @Param("bridge") Bridge bridge, @Param("morig") String morig);

	void addAuth(@Param("bridge") Bridge bridge, @Param("authCenter") AuthCenter authCenter);

	void delAuth(@Param("bridge") Bridge bridge, @Param("pkid") String pkid);

	void delAuthByMorig(@Param("bridge") Bridge bridge, @Param("morig") String morig);
}
