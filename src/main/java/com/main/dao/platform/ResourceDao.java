package com.main.dao.platform;

import com.main.pojo.platform.ResourcesInfo;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 菜单资源
 */
public interface ResourceDao {
    /**
     * 加载指定用户的资源信息
     * @param userpkid 用户pkid
     * @param type 1 菜单 2 按钮
     * @return 资源信息列表
     */
    List<ResourcesInfo> loadUserRes(@Param("userpkid") String userpkid, @Param("type") String type);

    /**
     * 加载所有资源信息
     * @return 资源信息列表
     */
    List<ResourcesInfo> queryAll();
}
