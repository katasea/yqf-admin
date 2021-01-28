package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 用户所在部门关系月备份表
 */
@Table(name = "PM_USER_DEPT_MB@YEAR")
public class UserDeptRelaMonthBackup implements Serializable {

    private static final long serialVersionUID = -1407626225396573317L;
    /**
     * 用户主键
     */
    @Column(oth = "NOT NULL")
    private String userpkid;
    /**
     * 科室主键
     */
    @Column(oth = "NOT NULL")
    private String deptpkid;

	/**
	 * 年月标识 存入格式 2018-09
	 */
	@Column(oth = "NOT NULL")
	private String yearmonth;

    //======================GETTERS && SETTERS=================//


	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getUserpkid() {
        return userpkid;
    }

    public void setUserpkid(String userpkid) {
        this.userpkid = userpkid;
    }

    public String getDeptpkid() {
        return deptpkid;
    }

    public void setDeptpkid(String deptpkid) {
        this.deptpkid = deptpkid;
    }
}
