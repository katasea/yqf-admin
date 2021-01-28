package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 基础数据列表字典
 *
 * 用于存储列表类的字典信息
 */
@Table(name = "PD_DICTIONARY_LIST")
public class BaseDic4List implements Serializable {


	private static final long serialVersionUID = 2660260394562323238L;

	@Column(flag = "primary")
	private String companyid;//医院编号
	@Column(flag = "primary")
	private String mkey;//编号
	@Column(flag = "primary",type="int")
	private int type;//类型 1 考核部门组  2 绩效部门组
	@Column(oth = "NOT NULL")
	private String mvalue;//名称
	@Column
	private String reverse;//备用字段

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getMkey() {
		return mkey;
	}

	public void setMkey(String mkey) {
		this.mkey = mkey;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMvalue() {
		return mvalue;
	}

	public void setMvalue(String mvalue) {
		this.mvalue = mvalue;
	}

	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}
}
