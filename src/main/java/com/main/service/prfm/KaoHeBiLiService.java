package com.main.service.prfm;
import java.util.List;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.KaoHeBiLi;
import com.main.pojo.result.KaoHeBiLiResult;
/**
 * 对象服务层接口
 * @author Tim[ATC.Pro Generate]
 */
public interface KaoHeBiLiService {
    /**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
  	KaoHeBiLi selectBykid(String id);
    /**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
  	List<KaoHeBiLiResult> get(String keyword, String node, Bridge bridge, String start, String limit,String deptId);
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
  	StateInfo delete(String code,String parentId, Bridge bridge);
    /**
	 * 修改对象
	 *
	 * @param code     主键信息
	 * @param kaoHeBiLi 修改对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, KaoHeBiLi kaoHeBiLi, Bridge bridge);
    /**
	 * 新增对象
	 *
	 * @param parentId 父节点信息
	 * @param kaoHeBiLi 新增对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId,KaoHeBiLi kaoHeBiLi, Bridge bridge);
    /**
	 * 验证当前key是否存在重复记录value
	 *
	 * @param key    数据库字段
	 * @param value  数据库值
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo validator(String key,String value, Bridge bridge);
}