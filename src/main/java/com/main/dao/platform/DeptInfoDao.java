package com.main.dao.platform;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.DeptInfo;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;

/**
 * 对象持久层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface DeptInfoDao {

	/**
	 * 获取树形对应节点数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return 对象集
	 */
	List<DeptInfo> get(Map<String, Object> map);

	/**
	 * 获取对应数量
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return map {count:11}
	 */
	Map<String, Object> getCount(Map<String, Object> map);

	/**
	 * 获取业务数据备份表的记录。
	 * @param map
	 * @return
	 */
	Map<String,Object> getMBCount(Map<String, Object> map);

	/**
	 * 获取列表数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:'',start:1,end:10}
	 * @return 对象集
	 */
	List<DeptInfo> getPage(Map<String, Object> map);

	/**
	 * 获取备份部门表分页数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:'',start:1,end:10}
	 * @return 对象集
	 */
	List<DeptInfo> getMBPage(Map<String, Object> map);

	/**
	 * 获取所有数据包括停用
	 *
	 * @param companyid 单位id
	 * @return 对象集
	 */
	List<DeptInfo> getAll(@Param("companyid") String companyid);
	/**
	 * 获取所有子节点数据不包括停用
	 * @param companyid 单位id
	 * @return 对象集
	 */
	List<DeptInfo> getLeafs(@Param("companyid") String companyid);

	/**
	 * 插入对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int insert(Map<String, Object> map);

	/**
	 * 修改对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
	int update(Map<String, Object> map);

	/**
	 * 通过主键删除对象
	 *
	 * @param map {companyid:'',id:''}
	 * @return 无
	 */
	int deleteByPrimaryKey(Map<String, Object> map);

	/**
	 * 改变子节点信息
	 *
	 * @param map {year:2018,companyid:'',id:'10101',value:0 or 1}
	 */
	void changeLeaf(Map<String, Object> map);

	/**
	 * 通过主键获取对象
	 *
	 * @param map {id:''}
	 * @return 部门
	 */
	DeptInfo selectBypid(Map<String, Object> map);

	/**
	 * 验证数据是否存在，返回具体数据
	 *
	 * @param key    字段
	 * @param value  值
	 * @param bridge 桥梁对象
	 * @return 查询结果
	 */
	List<Map<String, Object>> validator(@Param("key") String key, @Param("value") String value, @Param("bridge") Bridge bridge);

	/**
	 * 从数据看获取最大id用于自动计算生成id
	 *
	 * @param parentId 父节点
	 * @param bridge 桥梁对象
	 * @return map {id:''}
	 */
	Map<String, Object> getAutoGeneralID(@Param("parentId") String parentId, @Param("bridge") Bridge bridge);

	/**
	 * 删除所有记录
	 *
	 * @param year 年
	 * @return 无
	 */
	int deleteAll(@Param("year") String year,@Param("yearmonth") String yearmonth);

	/**
	 * 批量新增
	 *
	 * @param deptInfos 部门集合
	 */
	void insertDeptInfos(@Param("depts") List<DeptInfo> deptInfos);

	/**
	 * 先删除后插入
	 * 备份当前数据到 dept_month
	 * @param year_month eg: 1990-09
	 */
	void backupMonthData(@Param("year_month") String year_month);



}
