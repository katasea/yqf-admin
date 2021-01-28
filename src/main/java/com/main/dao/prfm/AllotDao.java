package com.main.dao.prfm;

import com.main.pojo.platform.Bridge;
import com.main.pojo.prfm.RbrvsCoefficient;

import java.util.List;
import java.util.Map;

/**
 * 分配计算工分
 * */
public interface AllotDao {

    /**
     *   删除当月公分结果数据
     * */
    boolean deleteAllotResult(Map<String,Object> paramMap);

    /**
     *   获取门诊与住院的原始收入数据
     *
     * */
    List<Map<String,Object>> getOriMzAndZyData(Map<String,Object> paramMap);

    /**
     *   根据原始收入数据，插入申请医生，执行医生，执行护士(如果有对照的话) 的工分
     *
     * */
    boolean insertIntoAllotResult(Map<String,Object> paramMap);


    /**
     *  获取rbrvs系数
     *
     * */
    List<RbrvsCoefficient> getRbrvsXsMap(Bridge bridge);

}
