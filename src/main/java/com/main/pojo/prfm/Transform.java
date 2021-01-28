package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 *  转换定义类
 */

@Table(name = "PM_Transform")
public class Transform implements Serializable {

	private static final long serialVersionUID = -3916103980318562011L;

	@Column(flag = "primary")
	private String id;//id

	@Column(type = "varchar(50)")
	private String companyid;//单位编号

	@Column(type = "int")
	private int theyear;//年份

	@Column(type = "int")
	private int themonth;//月份

	@Column(type = "int")
	private String typeID;//转换类型

	@Column(type = "varchar(50)")
	private String type_name;//转换类型名称

	@Column(type = "varchar(300)")
	private String condition_id;//条件字段ID

	@Column(type = "varchar(300)")
	private String condition_name;//条件字段名称

	@Column(type = "varchar(300)")
	private String condition_value;//条件字段值 类型9 转换后调整使用

	@Column(type = "varchar(300)")
	private String adjustment_id;//调整字段ID

	@Column(type = "varchar(300)")
	private String adjustment_name;//调整字段名称

	@Column(type = "varchar(300)")
	private String adjustment_value;//调整字段的值 类型9 转换后调整使用

	@Column(type = "int")
	private int order_num;//转换类型顺序

	@Column(type = "varchar(20)")
	private String transform_percent;//转换比例

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public int getTheyear() {
		return theyear;
	}

	public void setTheyear(int theyear) {
		this.theyear = theyear;
	}

	public int getThemonth() {
		return themonth;
	}

	public void setThemonth(int themonth) {
		this.themonth = themonth;
	}

	public String getTypeID() { return typeID; }

	public void setTypeID(String typeID) { this.typeID = typeID; }

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getCondition_id() {
		return condition_id;
	}

	public void setCondition_id(String condition_id) {
		this.condition_id = condition_id;
	}

	public String getAdjustment_id() {
		return adjustment_id;
	}

	public void setAdjustment_id(String adjustment_id) {
		this.adjustment_id = adjustment_id;
	}

	public String getCondition_name() {
		return condition_name;
	}

	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}

	public String getAdjustment_name() {
		return adjustment_name;
	}

	public void setAdjustment_name(String adjustment_name) {
		this.adjustment_name = adjustment_name;
	}

	public int getOrder_num() {
		return order_num;
	}

	public void setOrder_num(int order_num) {
		this.order_num = order_num;
	}

	public String getTransform_percent() {
		return transform_percent;
	}

	public void setCondition_value(String condition_value) {
		this.condition_value = condition_value;
	}

	public void setAdjustment_value(String adjustment_value) {
		this.adjustment_value = adjustment_value;
	}

	public String getCondition_value() {
		return condition_value;
	}

	public String getAdjustment_value() {
		return adjustment_value;
	}

	public void setTransform_percent(String transform_percent) {
		this.transform_percent = transform_percent;
	}
}
