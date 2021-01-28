package com.main.pojo.platform;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

/**
 * 字典信息
 * @author CFQ
 *
 */
@Table(name="PM_DICTIONARY")
public class BasDicInfo extends TreeNodeBean{
	/**
	 *
	 */
	private static final long serialVersionUID = -9011777967101276026L;
	/**
	 * 字典编号
	 */
	@Column(flag="primary")
	private String dicid;
	/**
	 * 字典名称
	 */
	@Column(oth = "NOT NULL")
	private String dicname;
	/**
	 * 助记码
	 */
	@Column(oth = "NOT NULL")
	private String zjm;
	/**
	 * 字典 键  用于业务系统当做key 即数据库保存值
	 */
	@Column(oth = "NOT NULL")
	private String dickey;
	/**
	 * 字典 值  用于业务系统当做value 即显示名称
	 */
	@Column(oth = "NOT NULL")
	private String dicval;
	/**
	 * 父节点
	 */
	@Column(oth = "NOT NULL")
	private String parentid;
	/**
	 * 是否叶子节点
	 */
	@Column(type="int")
	private int isleaf;
	/**
	 * 科室状态
	 * 1停用还是0启用
	 */
	@Column(type="int")
	private int isstop;// 0启用,1停用

	//===========================GETTERS && SETTERS=============================//

	public String getDicid() {
		return dicid;
	}

	public void setDicid(String dicid) {
		this.dicid = dicid;
	}

	public String getDicname() {
		return dicname;
	}

	public void setDicname(String dicname) {
		this.dicname = dicname;
	}

	public String getZjm() {
		return zjm;
	}

	public void setZjm(String zjm) {
		this.zjm = zjm;
	}

	public String getDickey() {
		return dickey;
	}

	public void setDickey(String dickey) {
		this.dickey = dickey;
	}

	public String getDicval() {
		return dicval;
	}

	public void setDicval(String dicval) {
		this.dicval = dicval;
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
}
