package com.main.service.platform;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.DeptInfo;
import com.main.pojo.platform.StateInfo;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface DeptInfoService {
	/**
	 * 通过pkid获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
	DeptInfo selectByPkid(String id);

	/**
	 * 获取部门树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 部门对象集
	 */
	List<DeptInfo> get(String keyword, String node, Bridge bridge);

	/**
	 * 获取当前条件下的部门数量
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 部门数量
	 */
	int getCount(String keyword, String node, Bridge bridge);

	/**
	 * 删除部门对象
	 *
	 * @param pkid     主键信息
	 * @param parentId 父节点信息
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo delete(String pkid, String parentId, Bridge bridge);

	/**
	 * 修改部门对象
	 *
	 * @param pkid     主键信息
	 * @param deptInfo 部门对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String pkid, DeptInfo deptInfo, Bridge bridge);

	/**
	 * 新增部门对象
	 *
	 * @param parentId 父节点信息
	 * @param deptInfo 部门对象
	 * @param bridge   桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId, DeptInfo deptInfo, Bridge bridge);

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
	 * 获取明细部门的数据
	 *
	 * @param keyword 关键字
	 * @param node    节点
	 * @param bridge  桥梁对象
	 * @param start   开始
	 * @param limit   结束
	 * @return 对象集
	 */
	List<DeptInfo> getListJson(String keyword, String node, Bridge bridge, String start, String limit);

	/**
	 * 导入上传的EXCEL数据
	 *
	 * @param targetFile 上传的临时EXCEL文件
	 * @param bridge     桥梁对象
	 * @return 状态信息
	 */
	StateInfo importData(File targetFile, Bridge bridge);

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
	StateInfo exportData(OutputStream os, Bridge bridge);

	/**
	 * 自动备份当月数据，先删除后插入
	 *
	 * @param year_month eg: 1990-09
	 */
	void backupMonthData(String year_month);

	/**
	 * 获取部门树
	 *
	 * @param bridge
	 * @return
	 */
	public List<Map<String, Object>> getDeptTree(Bridge bridge);

	/**
	 * 获取备份的表记录【业务主要使用这个】
	 *
	 * @param keyword  关键字
	 * @param bridge   桥梁
	 * @param start    起始
	 * @param limit    限制
	 * @param authcode 空不过滤。权限编码【权限中心的编码参考 GlobalConstant.AUTHCODE....】
	 * @return
	 */
	List<DeptInfo> getMB(String keyword, Bridge bridge, String start, String limit, String authcode);

	/**
	 * 获取备份的表的总数【业务主要使用这个】
	 *
	 * @param keyword  关键字
	 * @param bridge   桥梁对象
	 * @param authcode 空不过滤。权限编码【权限中心的编码参考 GlobalConstant.AUTHCODE....】
	 * @return
	 */
	int getMBCount(String keyword, Bridge bridge, String authcode);
}