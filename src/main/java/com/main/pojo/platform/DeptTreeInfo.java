package com.main.pojo.platform;

import java.util.List;
import java.util.Map;

public class DeptTreeInfo {
	private List<DeptInfo> sub;
	private DeptInfo deptInfo;
	List<Map<String, Object>> childrenList;
public List<Map<String, Object>> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<Map<String, Object>> childrenList) {
		this.childrenList = childrenList;
	}

public DeptInfo getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(DeptInfo deptInfo) {
		this.deptInfo = deptInfo;
	}

public List<DeptInfo> getSub() {
		return sub;
	}

	public void setSub(List<DeptInfo> sub) {
		this.sub = sub;
	}

}
