package com.main.pojo.prfm;

import com.common.auto.annotation.AutoCode;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

@AutoCode(isOverride = true, showWay = "list")
@Table(name = "PD_KHBL")
public class KaoHeBiLi {
	/**
	 *
	 */
	private static final long serialVersionUID = -9011777967101276026L;
	@Column(flag="primary",treeId=false,autoGenneral=true,treecolumn=true,jsname="编号",jswidth=150,jsAllowBlank=false,jsValidator=true,keyWordFilte=true)
	private String kid;
	@Column(jsname="单位编号",jswidth=150,jsAllowBlank=false)
	private String companyid;
	@Column(jsname="部门Id",jswidth=150,jsAllowBlank=false)
	private String deptId;
    /**
     * 指标id
     */
	@Column(jsname="指标编号",jswidth=150,jsAllowBlank=false)
	private String zbId;
	/**
	 * 自评比例
	 */
	@Column(type="varchar(10)",jsname="自评比例(%)",jsAllowBlank=false,jswidth=150)
	private String zpbl;
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
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getZbId() {
		return zbId;
	}
	public void setZbId(String zbId) {
		this.zbId = zbId;
	}
	public String getZpbl() {
		return zpbl;
	}
	public void setZpbl(String zpbl) {
		this.zpbl = zpbl;
	}
	public String getZkkbl() {
		return zkkbl;
	}
	public void setZkkbl(String zkkbl) {
		this.zkkbl = zkkbl;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 质控科比例
	 */
	@Column(type="varchar(10)",jsname="质控科比例(%)",jsAllowBlank=false,jswidth=150)
	private String zkkbl;
}
