package com.main.service.platform;

import com.main.pojo.platform.BasDicInfo;
import com.main.pojo.platform.StateInfo;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface BaseDicInfoService {
	/**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
	BasDicInfo selectBydicid(String id);

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
	List<BasDicInfo> get(String keyword, String node, Bridge bridge, String start, String limit);

	/**
	 * 获取当前条件下的对象数量
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 数量
	 */
	int getCount(String keyword, String node, Bridge bridge);

	/**
	 * 删除对象
	 *
	 * @param code     主键信息
	 * @param parentId 父节点信息
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo delete(String code, String parentId, Bridge bridge);

	/**
	 * 修改对象
	 *
	 * @param code       主键信息
	 * @param basDicInfo 修改对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, BasDicInfo basDicInfo, Bridge bridge);

	/**
	 * 新增对象
	 *
	 * @param parentId   父节点信息
	 * @param basDicInfo 新增对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId, BasDicInfo basDicInfo, Bridge bridge);

	/**
	 * 验证当前key是否存在重复记录value
	 *
	 * @param key    数据库字段
	 * @param value  数据库值
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo validator(String key, String value, Bridge bridge);

	/**
	 * 从access数据库初始化数据
	 *
	 * @return 状态对象
	 */
	StateInfo autoInitFromAccess();

	/**
	 * 获取明细数据
	 *
	 * @param type 要获取的字典的父节点编号
	 * @return 结果集合[{name:'',value:''}...]
	 */
	List<Map<String, Object>> getComboJson(String type);

	/**
	 * 获取用户关联的字典的ztree返回数据
	 *
	 * @param bridge 桥梁对象
	 * @param userpkid 用户id
	 * @param type 字典父节点
	 * @return ztree 要求的对象集
	 */
	List<Map<String,Object>> getUserDicZTree(Bridge bridge, String userpkid, String type);

	/**
	 * 获取字典的业务键值对
	 *
	 * @param bridge 桥梁对象
	 * @param type 字典父节点
	 * @return Map 键值对
	 */
	Map<String,String> getDicKeyValue(Bridge bridge, String type);
}