package com.main.pojo.prfm;


import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * ReportParamRela BEAN
 *
 * @author fuqiang chen
 */
@Table(name = "PD_REPORTPARAMRELA")
public class ReportParamRela implements Serializable {

	private static final long serialVersionUID = 3266508308812607708L;
	@Column(flag = "primary")
	private String uuid;
	@Column
	private int repid;
	@Column(oth = "NOT NULL", type = "varchar(200)")
	private String paramskey;


	//=================================GETTER && SETTER=================================//
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getRepid() {
		return repid;
	}

	public void setRepid(int repid) {
		this.repid = repid;
	}

	public String getParamskey() {
		return paramskey;
	}

	public void setParamskey(String paramskey) {
		this.paramskey = paramskey;
	}
}
