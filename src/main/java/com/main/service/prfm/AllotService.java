package com.main.service.prfm;

import com.main.pojo.platform.Bridge;

import java.util.Map;

/**
 *  分配计算工分
 *
 * */
public interface AllotService {

    /**
     *   分配计算
     *
     * */
    Map<String,Object> calculateAllot(Bridge bridge);
}
