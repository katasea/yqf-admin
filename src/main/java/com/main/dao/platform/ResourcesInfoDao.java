package com.main.dao.platform;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.ResourcesInfo;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;

/**
 * 资源菜单数据持久层
 *
 * @author ATC[auto-code project create]
 */
public interface ResourcesInfoDao {

	List<ResourcesInfo> get(Map<String, Object> map);

	Map<String, Object> getCount(Map<String, Object> map);

	List<ResourcesInfo> getPage(Map<String, Object> map);

	List<ResourcesInfo> getAll();

	int insert(Map<String, Object> map);

	int update(Map<String, Object> map);

	int deleteByPrimaryKey(Map<String, Object> map);

	void changeLeaf(Map<String, Object> map);

	int insertSelective(ResourcesInfo record);

	ResourcesInfo selectBypkid(Map<String, Object> map);

	List<ResourcesInfo> getChildrens(Map<String, Object> map);

	int updateByPrimaryKeySelective(ResourcesInfo record);

	int updateByPrimaryKey(ResourcesInfo record);

	List<Map<String, Object>> validator(@Param("key") String key, @Param("value") String value, @Param("bridge") Bridge bridge);

	Map<String, Object> getAutoGeneralID(@Param("parentId") String parentId, @Param("bridge") Bridge bridge);

	List<Map<String, Object>> getResourcesByRole(@Param("rolepkid") String rolepkid);

	List<Map<String,Object>> getResourcesByUser(@Param("userpkid") String userpkid);
}
