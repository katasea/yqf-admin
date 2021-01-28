package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 角色和资源关联
 */
@Table(name = "PM_USER_RES")
public class UserResourceRela implements Serializable {
    private static final long serialVersionUID = -5518672990323227359L;
    /**
     * 用户主键
     */
    @Column(oth = "NOT NULL")
    private String userpkid;
    /**
     * 资源主键
     */
    @Column(oth = "NOT NULL")
    private String respkid;

    //======================GETTERS && SETTERS=================//

    public String getUserpkid() {
        return userpkid;
    }

    public void setUserpkid(String userpkid) {
        this.userpkid = userpkid;
    }

    public String getRespkid() {
        return respkid;
    }

    public void setRespkid(String respkid) {
        this.respkid = respkid;
    }
}
