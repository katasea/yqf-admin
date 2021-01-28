package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 收费项目代码
 */
@Table(name = "VPM_ITEM")
public class VPM_ITEM implements Serializable {
	private static final long serialVersionUID = 597080535529697317L;

	@Column(flag = "primary")
	private String hospital;//医院编号
	@Column(flag = "primary")
	private String item;//收费项目编号
	@Column(oth = "NOT NULL")
	private String acc_item;//收费项目编号大类
	@Column(oth = "NOT NULL")
	private String item_name;//收费项目名称
	@Column(type = "decimal(18,2)")
	private String charge;//收费金额
	@Column(oth = "NOT NULL")
	private String finc;//项目(财务)分类
	@Column(oth = "NOT NULL")
	private String finc_name;//项目(财务)分类名称
	@Column(oth = "NOT NULL")
	private String invoice;//发票类别代码
	@Column
	private String parent;//上级收费项目编码
	@Column
	private int level;//等级
	@Column
	private String standard;//标准编号  省统一医保编码


	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getAcc_item() {
		return acc_item;
	}

	public void setAcc_item(String acc_item) {
		this.acc_item = acc_item;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	public String getFinc() {
		return finc;
	}

	public void setFinc(String finc) {
		this.finc = finc;
	}

	public String getFinc_name() {
		return finc_name;
	}

	public void setFinc_name(String finc_name) {
		this.finc_name = finc_name;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
}
