package com.main.pojo.prfm;

import com.common.CommonUtil;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 中间数据
 */
@Table(name = "PD_MIDDATA@YEAR")
public class MidData implements Serializable{


	private static final long serialVersionUID = -2652728170379550658L;

	@Column(flag="primary")
	private String pkid;
	@Column
	private String companyid;
	@Column
	private String id;
	@Column
	private String bh;
	private String name;
	@Column(type="int")
	private int deptorper;
	@Column(type="int")
	private int month;
	@Column(type="decimal(18,6)")
	private String data;
	@Column(type="varchar(2000)")
	private String process;


	//===========================GETTER && SETTER====================//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getBh() {
		return bh;
	}

	public void setBh(String bh) {
		this.bh = bh;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDeptorper() {
		return deptorper;
	}

	public void setDeptorper(int deptorper) {
		this.deptorper = deptorper;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getData() {
		return CommonUtil.nullToZero(data);
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
}
