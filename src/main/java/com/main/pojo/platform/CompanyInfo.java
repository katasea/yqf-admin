package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 单位信息
 */
@Table(name = "PM_COMPANY")
public class CompanyInfo implements Serializable {

    private static final long serialVersionUID = -2775034183934537298L;
    /**
     * 单位编号【仅显示】
     */
    @Column(flag = "primary")
    private String companyid;
    /**
     * 单位描述
     */
    @Column(oth = "NOT NULL")
    private String companyname;
	/**
	 * 是否停用
	 *
	 */
    @Column(type="int")
    private int isstop;

    //======================GETTERS && SETTERS=================//

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}
}
