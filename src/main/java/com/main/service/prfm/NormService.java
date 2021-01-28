package com.main.service.prfm;
import com.main.pojo.prfm.Norm;
import com.main.pojo.platform.StateInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.main.pojo.platform.Bridge;
/**
 * 对象服务层接口
 * @author Tim[ATC.Pro Generate]
 */
public interface NormService {
    /**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
  	Norm selectByuid(String id);
    /**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
  	List<Norm> get(String keyword, String node, Bridge bridge, String start, String limit,String deptOrPerson);
  	
    /**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
  	List<Norm> getByCondition(String keyword, String node, Bridge bridge, String start, String limit);
  	
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
	 * @param norm 修改对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, Norm norm, Bridge bridge);
    /**
	 * 新增对象
	 *
	 * @param parentId 父节点信息
	 * @param norm 新增对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId,Norm norm, Bridge bridge);
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
	
	/**
	 * 导入上传的EXCEL数据
	 *
	 * @param targetFile 上传的临时EXCEL文件
	 * @param bridge     桥梁对象
	 * @return 状态信息
	 */
	StateInfo importData(File targetFile, Bridge bridge) throws FileNotFoundException;

	/**
	 * 生成上传EXCEL模板
	 *
	 * @param os     输出源
	 * @param bridge 桥梁信息
	 * @return 状态信息
	 */
	StateInfo exportExcelTemplate(OutputStream os, Bridge bridge);

	/**
	 * 导出数据
	 *
	 * @param os     输出源
	 * @param bridge 桥梁信息
	 * @return 状态信息
	 */
	StateInfo exportData(OutputStream os, Bridge bridge,String keyword);
	/**
	 * 获取指标树
	 * @param bridge
	 * @return
	 */
	public List<Map<String,Object>> getDeptTree(Bridge bridge,String deptOrPerson,String zhiBiaoType);
}