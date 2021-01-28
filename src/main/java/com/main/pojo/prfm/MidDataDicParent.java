package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 中间数据字典【分类父类】
 */
@Table(name = "PD_MIDDATADICPARENT")
public class MidDataDicParent implements Serializable{

	private static final long serialVersionUID = 5117876501990181944L;

	@Column(flag = "primary")
	private String companyid;
	@Column(flag = "primary")
	private String id;
	@Column(type="varchar(200)")
	private String name;
	@Column(type="int")
	private int orderid;


	//=======================GETTER && SETTER======================//


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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
}
