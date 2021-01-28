package com.main.service.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.Transform;

import java.util.List;
import java.util.Map;

public interface TransformService {

    /**
     * @Author: sjl
     * @Date: 2018/5/4 14:23
     * @param typeid 转换类型ID
     * @Description:
     *
     */
    List<Transform> getDataByType(String typeid, Bridge bridge);

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
     * @Author: sjl
     * @Date: 2018/5/6 16:57
     * @param transform 转换定义bean
     * @Description:
     *  删除转换定义
     */
    boolean del(Transform transform);

    /**
     *  执行转换
     *
     * */
    Map<String,Object> transform(Bridge bridge,String month1,String month2);

}
