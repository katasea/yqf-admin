package com.main.pojo.result;

import java.util.List;
import java.util.Map;

import com.main.pojo.platform.DeptInfo;
import com.main.pojo.prfm.Norm;

public class NormTree{
	private List<Norm> sub;
	private Norm norm;
	List<Map<String,Object>> childrenList;
public List<Map<String, Object>> getChildrenList() {
		return childrenList;
	}

	public void setChildrenList(List<Map<String, Object>> childrenList) {
		this.childrenList = childrenList;
	}

	public List<Norm> getSub() {
		return sub;
	}

	public void setSub(List<Norm> sub) {
		this.sub = sub;
	}

	public Norm getNorm() {
		return norm;
	}

	public void setNorm(Norm norm) {
		this.norm = norm;
	}


}
