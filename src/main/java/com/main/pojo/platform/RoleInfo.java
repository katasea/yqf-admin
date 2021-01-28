package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 角色信息
 */
@Table(name = "PM_ROLE")
public class RoleInfo implements Serializable {

    private static final long serialVersionUID = -2970562131519770361L;
    /**
     * 主键
     */
    @Column(flag = "primary")
    private String pkid;
    /**
     * 角色编号【仅显示】
     */
    @Column(oth = "NOT NULL")
    private String roleid;
    /**
     * 角色描述
     */
    @Column(oth = "NOT NULL")
    private String roledesc;

    //======================GETTERS && SETTERS=================//

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getRoledesc() {
        return roledesc;
    }

    public void setRoledesc(String roledesc) {
        this.roledesc = roledesc;
    }
}
