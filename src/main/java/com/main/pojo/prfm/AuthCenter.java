package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

@Table(name = "PD_AUTHCENTER")
public class AuthCenter implements Serializable {
	private static final long serialVersionUID = 2599889539602514721L;

	@Column(flag = "primary")
	private String pkid;
	@Column
	private String companyid;
	@Column
	private String funcode; //1 辅助数据录入权限 2 辅助数据录入管理部门 3 绩效部门捆绑关联 4 考核部门捆绑关联 5 质量指标关联部门权限
	@Column
	private String morig;
	@Column
	private String morigname;
	@Column
	private String mtarg;
	@Column
	private String mtargname;

	//=======================GETTER AND SETTER ==============================//
	public String getMorigname() {
		return morigname;
	}

	public void setMorigname(String morigname) {
		this.morigname = morigname;
	}

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getFuncode() {
		return funcode;
	}

	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}

	public String getMorig() {
		return morig;
	}

	public void setMorig(String morig) {
		this.morig = morig;
	}

	public String getMtarg() {
		return mtarg;
	}

	public void setMtarg(String mtarg) {
		this.mtarg = mtarg;
	}

	public String getMtargname() {
		return mtargname;
	}

	public void setMtargname(String mtargname) {
		this.mtargname = mtargname;
	}
}
