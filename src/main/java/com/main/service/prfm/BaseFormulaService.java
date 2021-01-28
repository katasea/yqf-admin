package com.main.service.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.platform.StateInfo;
import com.main.pojo.prfm.BaseFormula;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对象服务层接口
 *
 * @author Tim[ATC.Pro Generate]
 */
public interface BaseFormulaService {
	/**
	 * 通过主键获取部门信息
	 *
	 * @param id pkid主键
	 * @return 部门对象
	 */
	BaseFormula selectByname(String id);

	/**
	 * 获取树
	 *
	 * @param keyword 关键检索词
	 * @param node    展开节点id
	 * @param bridge  桥梁对象
	 * @return 对象集
	 */
	List<BaseFormula> get(String keyword, String node, Bridge bridge, String start, String limit);

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
	 * 获取 中文名称 对应的 公式信息
	 *
	 * @param keyword 关键词
	 * @param bridge  桥梁
	 * @return 结果
	 */
	Map<String, BaseFormula> getFormulaInfoByName(String keyword, Bridge bridge);

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
	 * @param code        主键信息
	 * @param baseFormula 修改对象
	 * @param bridge      桥梁对象
	 * @return 状态对象
	 */
	StateInfo edit(String code, BaseFormula baseFormula, Bridge bridge);

	/**
	 * 新增对象
	 *
	 * @param parentId    父节点信息
	 * @param baseFormula 新增对象
	 * @param bridge      桥梁对象
	 * @return 状态对象
	 */
	StateInfo add(String parentId, BaseFormula baseFormula, Bridge bridge);

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
	 * 解析公式获取结果
	 *
	 * @param formula  公式 ：[公式测试1()]+[公式测试2()]/[公式测试3()]
	 * @param paramMap 传递所需的参数，后续补充
	 *                 必传      formulaInfoMap : Map<String,BaseFormula>  //可以调用本服务 getFormulaInfoByName 获取数据
	 *                 必传      dec : 2  //小数位
	 *                 必传      date_begin 2018-09-09
	 *                 必传      date_end  2018-09-09
	 *                 必传      month_begin 09
	 *                 必传      month_end   09
	 *                 必传      companyid   001
	 *                 必传      year        2018
	 * @return key:{data:value,desc:'xxxx+xxxx=xxxx'}
	 */
	StateInfo getData(String formula, Map<String, Object> paramMap) throws Exception;

	/**
	 * 获取单体公式集合
	 *
	 * @param formula 公式中文名称集合 [公式1()]+ [公式2()] / [公式3()]
	 * @return 单个公式中文名称集合 [[公式1()],[公式2()],[公式3()]]
	 */
	Set<String> getSingleFormulas(String formula) throws Exception;

	/**
	 * 获取原始的参数集合
	 *
	 * @param bridge      桥梁信息
	 * @param month_begin 开始月份
	 * @param month_end   结束月份
	 * @return 参数集合
	 */
	Map<String, Object> getParamMap(Bridge bridge, String month_begin, String month_end);

	/**
	 * 处理sql里 @的参数
	 *
	 * @param sqlstr   原始sql
	 * @param paramMap 条件map
	 * @return sqlstr
	 */
	String dealBridgeParam(String sqlstr, Map<String, Object> paramMap);
}