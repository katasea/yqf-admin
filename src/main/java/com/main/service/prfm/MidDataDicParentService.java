package com.main.service.prfm;
import com.main.pojo.prfm.MidDataDicParent;
import com.main.pojo.platform.StateInfo;
import java.util.List;
import com.main.pojo.platform.Bridge;
/**
 * 对象服务层接口
 * @author Tim[ATC.Pro Generate]
 */
public interface MidDataDicParentService {
    /**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
  	MidDataDicParent selectByid(String id);
    /**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param deptOrper    0 部门 1 个人
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
  	List<MidDataDicParent> get(String keyword, String deptOrper, Bridge bridge, String start, String limit);
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
	 * @param midDataDicParent 修改对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, MidDataDicParent midDataDicParent, Bridge bridge);
    /**
	 * 新增对象
	 *
	 * @param parentId 父节点信息
	 * @param midDataDicParent 新增对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId,MidDataDicParent midDataDicParent, Bridge bridge);
    /**
	 * 验证当前key是否存在重复记录value
	 *
	 * @param key    数据库字段
	 * @param value  数据库值
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo validator(String key,String value, Bridge bridge);
	/**
	 * 从access数据库初始化数据
	 *
	 * @param bridge 桥梁对象
	 * @return 状态对象
	 */
	StateInfo autoInitFromAccess(Bridge bridge);
}