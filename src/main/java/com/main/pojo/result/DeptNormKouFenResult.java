package com.main.pojo.result;

import com.main.pojo.prfm.DeptNormKouFen;

public class DeptNormKouFenResult extends DeptNormKouFen{
	private String zhiBiaoName;//指标名称
	private String zuiGaoKouFen;//最高扣分
	private String deptOrPersonNo;//部门或者个人编号
	private String deptOrPersonName;//部门或者个人名称
	private String kaoHeBiaoZhun;//考核标准
	private String pingFenBiaoZhun;//评分标准
	public String getKaoHeBiaoZhun() {
		return kaoHeBiaoZhun;
	}
	public void setKaoHeBiaoZhun(String kaoHeBiaoZhun) {
		this.kaoHeBiaoZhun = kaoHeBiaoZhun;
	}
	public String getPingFenBiaoZhun() {
		return pingFenBiaoZhun;
	}
	public void setPingFenBiaoZhun(String pingFenBiaoZhun) {
		this.pingFenBiaoZhun = pingFenBiaoZhun;
	}
	public String getZuiGaoKouFen() {
		return zuiGaoKouFen;
	}
	public void setZuiGaoKouFen(String zuiGaoKouFen) {
		this.zuiGaoKouFen = zuiGaoKouFen;
	}
	public String getZhiBiaoName() {
		return zhiBiaoName;
	}
	public void setZhiBiaoName(String zhiBiaoName) {
		this.zhiBiaoName = zhiBiaoName;
	}
	public String getDeptOrPersonNo() {
		return deptOrPersonNo;
	}
	public void setDeptOrPersonNo(String deptOrPersonNo) {
		this.deptOrPersonNo = deptOrPersonNo;
	}
	public String getDeptOrPersonName() {
		return deptOrPersonName;
	}
	public void setDeptOrPersonName(String deptOrPersonName) {
		this.deptOrPersonName = deptOrPersonName;
	}
}
