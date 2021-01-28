package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * PARAM BEAN
 *
 * @author fuqiang chen
 */
@Table(name = "PD_PARAMS")
public class ParamBean implements Serializable {

	private static final long serialVersionUID = 5132311058497963363L;

	@Column
	private String paramsno;

	@Column(flag = "primary")
	private String paramskey;

	@Column(type = "VARCHAR(4000)")
	private String paramsvalue;

	@Column
	private int paramstype;

	@Column
	private String reverse1;

	@Column
	private String reverse2;

	public String getParamsno() {
		return paramsno;
	}

	public void setParamsno(String paramsno) {
		this.paramsno = paramsno;
	}

	public String getParamskey() {
		return paramskey;
	}

	public void setParamskey(String paramskey) {
		this.paramskey = paramskey;
	}

	public String getParamsvalue() {
		return paramsvalue;
	}

	public void setParamsvalue(String paramsvalue) {
		this.paramsvalue = paramsvalue;
	}

	public int getParamstype() {
		return paramstype;
	}

	public void setParamstype(int paramstype) {
		this.paramstype = paramstype;
	}

	public String getReverse1() {
		return reverse1;
	}

	public void setReverse1(String reverse1) {
		this.reverse1 = reverse1;
	}

	public String getReverse2() {
		return reverse2;
	}

	public void setReverse2(String reverse2) {
		this.reverse2 = reverse2;
	}
}
