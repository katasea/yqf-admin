package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 中间数据公式字典
 */
@Table(name = "PD_MIDDATAFORMULADIC")
public class MidDataFormulaDic implements Serializable {

	private static final long serialVersionUID = -4586123660062447825L;
	@Column(flag="primary")
	private String pkid;
	@Column
	private String companyid;
	@Column
	private String id;//id
	@Column(type="int")
	private int orderid;//排序号，从小到大进行计算并覆盖之前的结果。
	@Column(type="varchar(200)")
	private String name;//名称
	@Column(type="varchar(2000)")
	private String formula;//公式
	@Column(type="int")
	private int isstop;//0否 1是
	@Column(type="int")
	private int dec;//小数位
	@Column(type="int")
	private int caclfalg;//计算标识 ： 0未计算 1计算成功 2计算失败  这里英文拼错了，代码写了好多怕麻烦懒得改了。
	@Column(type="varchar(500)")
	private String failreason;//失败原因
	@Column(type="int")
	private int deptorper;//1部门2个人

	//======================GETTER && SETTER===========================//

	public int getDeptorper() {
		return deptorper;
	}

	public void setDeptorper(int deptorper) {
		this.deptorper = deptorper;
	}

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getFailreason() {
		return failreason;
	}

	public void setFailreason(String failreason) {
		this.failreason = failreason;
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

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public int getDec() {
		return dec;
	}

	public void setDec(int dec) {
		this.dec = dec;
	}

	public int getCaclfalg() {
		return caclfalg;
	}

	public void setCaclfalg(int caclfalg) {
		this.caclfalg = caclfalg;
	}
}
