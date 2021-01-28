package com.main.dao.prfm;

import java.util.List;
import java.util.Map;
import com.main.pojo.prfm.MidDataFormulaDic;
import org.apache.ibatis.annotations.Param;
import com.main.pojo.platform.Bridge;
/**
 * 对象持久层接口
 * @author Tim[ATC.Pro Generate]
 */
public interface MidDataFormulaDicDao {

    /**
	 * 获取树形对应节点数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return 对象集
	 */
	List<MidDataFormulaDic> get(Map<String, Object> map);

    /**
	 * 获取对应数量
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:''}
	 * @return map {count:11}
	 */
	Map<String,Object> getCount(Map<String, Object> map);

    /**
	 * 获取列表数据
	 *
	 * @param map {keyword:'',node:'',year:'',companyid:'',start:1,end:10}
	 * @return 对象集
	 */
	List<MidDataFormulaDic> getPage(Map<String, Object> map);

    /**
	 * 获取所有数据包括停用
	 *
	 * @param companyid 单位id
	 * @return 对象集
	 */
	List<MidDataFormulaDic> getAll(@Param("companyid")String companyid);

    /**
	 * 插入对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
    int insert(Map<String,Object> map);

    /**
	 * 修改对象
	 *
	 * @param map 对象转map
	 * @return 无
	 */
    int update(Map<String,Object> map);

    /**
	 * 通过主键删除对象
	 *
	 * @param map {year:2018,id:''}
	 * @return 无
	 */
    int deleteByPrimaryKey(Map<String,Object> map);


    /**
	 * 通过主键获取对象
	 *
	 * @param map {id:''}
	 * @return 对象
	 */
    MidDataFormulaDic selectByid(Map<String,Object> map);

    /**
	 * 验证数据是否存在，返回具体数据
     *
	 * @param key 字段
	 * @param value 值
	 * @param bridge 桥梁对象
	 * @return 查询结果
	 */
    List<Map<String,Object>> validator(@Param("key")String key,@Param("value")String value,@Param("bridge")Bridge bridge);

    /**
	 * 从数据看获取最大id用于自动计算生成id
	 *
	 * @param parentId 父节点
	 * @param bridge 桥梁对象
	 * @return map {id:''}
	 */
    Map<String,Object> getAutoGeneralID(@Param("parentId")String parentId,@Param("bridge")Bridge bridge);

    /**
	 * 删除所有记录
	 *
	 * @param year 年
	 */
    void deleteAll(@Param("year")String year);


	void updateCalcSucc(@Param("calcMidFormulaInfo")List<String> calcMidFormulaInfo);

	void updateCalcError(@Param("errorcode") String errorcode,@Param("errormsg") String errormsg);

}
