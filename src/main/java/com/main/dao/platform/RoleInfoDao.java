package com.main.dao.platform;

import java.util.List;
import java.util.Map;
import com.main.pojo.platform.RoleInfo;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;
/**
 * @author ATC[auto-code project create]
 */
public interface RoleInfoDao {

	List<RoleInfo> get(Map<String, Object> map);

	Map<String,Object> getCount(Map<String, Object> map);

	List<RoleInfo> getPage(Map<String, Object> map);

	List<RoleInfo> getAll();
	
    int insert(Map<String,Object> map);
    
    int update(Map<String,Object> map);
    
    int deleteByPrimaryKey(Map<String,Object> map);

    int insertSelective(RoleInfo record);

    RoleInfo selectBypkid(Map<String,Object> map);
    
    List<RoleInfo> getChildrens(Map<String,Object> map);
    
    int updateByPrimaryKeySelective(RoleInfo record);

    int updateByPrimaryKey(RoleInfo record);
    
    List<Map<String,Object>> validator(@Param("key")String key,@Param("value")String value,@Param("bridge")Bridge bridge);
    
    Map<String,Object> getAutoGeneralID(@Param("parentId")String parentId,@Param("bridge")Bridge bridge);

	List<Map<String,Object>> getRoleByUserid(String userpkid);
}
