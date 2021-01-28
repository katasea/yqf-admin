package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.main.pojo.platform.TreeNodeBean;

import java.io.Serializable;

/**
 * EG 预算项目字典
 *
 * @author
 */
@Table(name = "PD_ITEMTYPE")
public class ItemType extends TreeNodeBean implements Serializable {
	private static final long serialVersionUID = -922622834092981916L;
	/**
	 * id
	 */
	@Column(flag = "primary")
	private String pid;
	/**
	 * 公司ID
	 */
	@Column
	private String companyid;
	/**
	 * 分类编码
	 */
	@Column
	private String code;
	/**
	 * 分类名称
	 */
	@Column
	private String text;
	/**
	 * 分类
	 */
	@Column(type = "int")
	private int itemtype;
	/**
	 * 父节点
	 */
	@Column
	private String parentid;
	/**
	 * 是否叶子节
	 */
	@Column(type = "int")
	private int isleaf;
	/**
	 * 是否停用
	 */
	@Column(type = "int")
	private int isstop;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getItemtype() {
		return itemtype;
	}

	public void setItemtype(int itemtype) {
		this.itemtype = itemtype;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public int getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
