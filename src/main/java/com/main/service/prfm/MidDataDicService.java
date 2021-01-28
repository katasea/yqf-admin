package com.main.service.prfm;

import com.main.pojo.prfm.MidDataDic;
import com.main.pojo.platform.StateInfo;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface MidDataDicService {
	/**
	 * 通过主键获取部门信息
	 *
	 * @param id        辅助数据编号
	 * @param companyid 单位编号
	 * @return 辅助数据字典
	 */
	MidDataDic selectByid(String id, String companyid);

	/**
	 * 获取小数位信息
	 *
	 * @param id        辅助数据编号
	 * @param companyid 单位编号
	 * @return 辅助数据字典
	 */
	Map<String,String> getDecInfo(String id,String companyid);

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
	List<MidDataDic> get(String keyword, String node, Bridge bridge, String start, String limit);

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
	 * @param midDataDic 修改对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, MidDataDic midDataDic, Bridge bridge);

	/**
	 * 新增对象
	 *
	 * @param parentId   父节点信息
	 * @param midDataDic 新增对象
	 * @param bridge     桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId, MidDataDic midDataDic, Bridge bridge);

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
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo autoInitFromAccess(Bridge bridge);

	/**
	 * 获取公式类型的辅助数据树
	 *
	 * @return
	 */
	List<Map<String, Object>> getTreeJson(Bridge bridge, int type);

	/**
	 * 通过类型获取辅助数据
	 *
	 * @param companyid 单位编号
	 * @param type 类型
	 * @return 字典列表
	 */
	List<MidDataDic> getByType(String companyid, int type);

	/**
	 * 通过父节点获取启用的辅助数据字典
	 *
	 * @param bridge 桥梁
	 * @param chooseParentId 父节点
	 * @return 字典列表
	 */
	List<MidDataDic> getByParentid(Bridge bridge, String chooseParentId);
}