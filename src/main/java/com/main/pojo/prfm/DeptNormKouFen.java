package com.main.pojo.prfm;

import java.io.Serializable;

import com.common.auto.annotation.AutoCode;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

@AutoCode(isOverride = true, showWay = "list")
@Table(name = "PD_DNKF")
public class DeptNormKouFen implements Serializable{
	private static final long serialVersionUID = -9011777967101276026L;
	@Column(flag="primary",treeId=false,autoGenneral=true,treecolumn=true,jsname="编号",jswidth=150,jsAllowBlank=false,jsValidator=true,keyWordFilte=true)
	private String kid;
	@Column(jsname="单位编号",jswidth=150,jsAllowBlank=false)
	private String companyid;
	@Column(jsname="部门或者个人id",jswidth=150,jsAllowBlank=false)
	private String deptOrPersonId;
	@Column(jsname="指标id",jswidth=150,jsAllowBlank=false)
	private String zhiBiaoId;
	@Column(type="varchar(10)",jsname="扣分(单击可编辑)",jsAllowBlank=false,jswidth=150)
	private String kouFen;
	@Column(type="varchar(10)",jsname="实际扣分",jsAllowBlank=false,jswidth=150)
	private String realKouFen;
	@Column(type="varchar(500)",jsname="扣分说明",jsAllowBlank=true,jswidth=150)
	private String kouFenMark;
	@Column(type="varchar(500)",jsname="质控科扣分")
	private String zkkKouFen;
	@Column(type="varchar(500)",jsname="质控科实际扣分")
	private String zkkRealKouFen;
	@Column(type="varchar(500)",jsname="质控科扣分说明")
	private String zkkKouFenMark;
	@Column(type="varchar(500)",jsname="其他扣分")
	private String qiTaKouFen;
	@Column()
	private String kouFenRenId;//扣分人
	@Column(type="int")
	private int bigtype;
	
	public String getZkkRealKouFen() {
		return zkkRealKouFen;
	}
	public void setZkkRealKouFen(String zkkRealKouFen) {
		this.zkkRealKouFen = zkkRealKouFen;
	}
	public String getZkkKouFenMark() {
		return zkkKouFenMark;
	}
	public void setZkkKouFenMark(String zkkKouFenMark) {
		this.zkkKouFenMark = zkkKouFenMark;
	}
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}
	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	public String getDeptOrPersonId() {
		return deptOrPersonId;
	}
	public void setDeptOrPersonId(String deptOrPersonId) {
		this.deptOrPersonId = deptOrPersonId;
	}
	public String getZhiBiaoId() {
		return zhiBiaoId;
	}
	public void setZhiBiaoId(String zhiBiaoId) {
		this.zhiBiaoId = zhiBiaoId;
	}
	public String getKouFen() {
		return kouFen;
	}
	public void setKouFen(String kouFen) {
		this.kouFen = kouFen;
	}
	public String getRealKouFen() {
		return realKouFen;
	}
	public void setRealKouFen(String realKouFen) {
		this.realKouFen = realKouFen;
	}
	public String getKouFenMark() {
		return kouFenMark;
	}
	public void setKouFenMark(String kouFenMark) {
		this.kouFenMark = kouFenMark;
	}
	public String getZkkKouFen() {
		return zkkKouFen;
	}
	public void setZkkKouFen(String zkkKouFen) {
		this.zkkKouFen = zkkKouFen;
	}
	public String getQiTaKouFen() {
		return qiTaKouFen;
	}
	public void setQiTaKouFen(String qiTaKouFen) {
		this.qiTaKouFen = qiTaKouFen;
	}
	
	public String getKouFenRenId() {
		return kouFenRenId;
	}
	public void setKouFenRenId(String kouFenRenId) {
		this.kouFenRenId = kouFenRenId;
	}
	public int getBigtype() {
		return bigtype;
	}
	public void setBigtype(int bigtype) {
		this.bigtype = bigtype;
	}

}
