package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 用户资源关联
 */
@Table(name = "PM_USER_ROLE")
public class UserRoleRela implements Serializable {

    private static final long serialVersionUID = -1407626225396573317L;
    /**
     * 用户主键
     */
    @Column(oth = "NOT NULL")
    private String userpkid;
    /**
     * 角色主键
     */
    @Column(oth = "NOT NULL")
    private String rolepkid;

    //======================GETTERS && SETTERS=================//


    public String getUserpkid() {
        return userpkid;
    }

    public void setUserpkid(String userpkid) {
        this.userpkid = userpkid;
    }

    public String getRolepkid() {
        return rolepkid;
    }

    public void setRolepkid(String rolepkid) {
        this.rolepkid = rolepkid;
    }
}
