package com.main.dao.prfm;

import com.main.pojo.prfm.Transform;

import java.util.List;
import java.util.Map;

/**
 * @Author: sjl
 * @Date: 2018/4/30 11:29
 * @param
 * @Description:
 *  转换定义持久层接口
 */
public interface TransformDao {

    /**
     * @Author: sjl
     * @Date: 2018/5/4 14:23
     * @param map
     * @Description:
     *
     */
    List<Transform> getDataByType(Map<String,Object> map);

    /**
     * @Author: sjl
     * @Date: 2018/5/6 11:50
     * @param transform 转换定义bean
     * @Description:
     *
     *  保存转换定义
     *
     */
    boolean save(Transform transform);

    /**
     *  更新
     *
     * */
    boolean update(Transform transform);

    /**
     * @Author: sjl
     * @Date: 2018/5/6 16:59
     * @param transform 转换定义bean
     * @Description:
     *
     *  删除转换定义
     *
     */
    boolean del(Transform transform);

    /**
     * 执行转换
     * */
    boolean transformItem(Map<String,Object> paramMap);

    /**
     * 转换后调整
     * */
    boolean transformAfter(Map<String,Object> paramMap);
    /**
     * 将尚未转换的原始数据导入
     * */
    boolean insertOriginalData(Map<String,Object> paramMap);

    /**
     *  删除当月原始数据
     * */
    boolean deleteOriginalData(Map<String,Object> paramMap);

}
