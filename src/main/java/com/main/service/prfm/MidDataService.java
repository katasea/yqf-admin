package com.main.service.prfm;

import com.main.pojo.prfm.MidData;
import com.main.pojo.platform.StateInfo;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface MidDataService {
	/**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
	MidData selectByid(String id);

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param midBh   对应的辅助数据字典编号
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
	List<MidData> get(String keyword, String midBh, Bridge bridge, String start, String limit, String deptorper);

	/**
	 * 获取当前条件下的对象数量
	 *
	 * @param keyword 关键检索词
	 * @param midBh   对应的辅助数据字典编号
	 * @param bridge  桥梁对象
	 * @return 数量
	 */
	int getCount(String keyword, String midBh, Bridge bridge, String deptorper);

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
	 * @param code    主键信息
	 * @param midData 修改对象
	 * @param bridge  桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, MidData midData, Bridge bridge);

	/**
	 * 新增对象
	 *
	 * @param parentId 父节点信息
	 * @param midData  新增对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId, MidData midData, Bridge bridge);

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
	 * 批量插入结果
	 *
	 * @param year 年份
	 * @param list 列表
	 * @return 状态对象
	 */
	StateInfo insertBatch(String year, List<MidData> list);

	/**
	 * 获取动态列
	 *
	 * @param bridge         桥梁对象
	 * @param chooseParentId 辅助数据父类信息
	 * @param deptOrper      0 部门 1 个人
	 * @return 状态对象
	 */
	StateInfo getDynamicColumn(Bridge bridge, String chooseParentId, String deptOrper);

	/**
	 * 获取动态列对应的JSON数据
	 *
	 * @param bridge         桥梁对象
	 * @param deptOrper      0部门 1个人
	 * @param chooseParentId 辅助字典父类
	 * @param start          开始
	 * @param limit          限制
	 * @param keyword        关键词
	 * @return 分页数据信息。
	 */
	Map<String, Object> getDynamicJson(Bridge bridge, String deptOrper, String chooseParentId, String start, String limit, String keyword);
}