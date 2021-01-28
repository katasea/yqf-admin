package com.main.service.platform;
import com.main.pojo.platform.ResourcesInfo;
import com.main.pojo.platform.StateInfo;
import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;

/**
 * @author ATC[auto-code project create]
 *
 */
public interface ResourcesInfoService {
  	ResourcesInfo selectBypkid(int id);
  	List<ResourcesInfo> get(String keyword, String node, Bridge bridge, String start, String limit);
	int getCount(String keyword, String node, Bridge bridge);
	/**
	 * 查询所有资源菜单
	 * @return
	 */
	List<ResourcesInfo> queryAll();

	StateInfo add(String parentId,ResourcesInfo resourcesInfo, Bridge bridge);
	StateInfo validator(String key,String value, Bridge bridge);
	StateInfo autoInitFromAccess(Bridge bridge);
	StateInfo edit(String code, ResourcesInfo resourcesInfo, Bridge bridge);
	StateInfo delete(String code,String parentId, Bridge bridge);

	/**
	 * 递归获取所有资源节点信息，并且根据页面传递的参数判断是否已经勾选
	 * 角色界面有勾选资源的功能  即 查询该角色对应的菜单资源并返回对应格式的对象集合
	 * 用户界面有勾选资源的功能  即 查询该用户对应的菜单资源并返回对应格式的对象集合
	 *
	 * @param bridge   系统信息
	 * @param rolepkid 角色id
	 * @param userpkid 用户id
	 * @return 资源节点集合 e.g. [{ id:1, pId:0, name:"菜单 1", open:true, checked:true},...]
	 */
	List<Map<String, Object>> getResZTree(Bridge bridge, String rolepkid, String userpkid);

	/**
	 * shiro 获取用户所有的菜单
	 *
	 * @param userpkid 用户id
	 * @return 资源菜单对象集
	 */
	List<ResourcesInfo> loadUserResources(String userpkid);
}