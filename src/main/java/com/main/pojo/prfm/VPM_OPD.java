package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 门诊收费明细
 */
@Table(name = "VPM_OPD@YEAR")
public class VPM_OPD implements Serializable {

	private static final long serialVersionUID = 3895867522971364236L;
	@Column(oth = "NOT NULL")
	private String hospital;//医院编号
	@Column(oth = "NOT NULL")
	private String pkid;//费用明细
	@Column(oth = "NOT NULL")
	private String flowid;//门诊挂号流水号
	@Column(oth = "NOT NULL")
	private String item;//收费项目编码
	@Column(oth = "NOT NULL")
	private String item_name;//收费项目名称
	@Column(oth = "NOT NULL", type = "decimal(18,2)")
	private String count;//数量
	@Column(oth = "NOT NULL", type = "decimal(18,2)")
	private String price;//单价
	@Column(oth = "NOT NULL", type = "decimal(18,2)")
	private String money;//金额
	@Column(oth = "NOT NULL", type = "char(10)")
	private String charge_time;//收费时间 yyyy-MM-dd
	@Column(oth = "NOT NULL")
	private String order_dept;//下嘱医生科室编码
	@Column(oth = "NOT NULL")
	private String order_staff;//下嘱医生工号
	@Column(oth = "NOT NULL")
	private String order_staff_name;//下嘱医生名称
	@Column(oth = "NOT NULL")
	private String do_dept;//执行科室编码
	@Column(oth = "NOT NULL")
	private String do_staff;//执行医生工号
	@Column(oth = "NOT NULL")
	private String do_staff_name;//执行医生名称
	@Column(oth = "NOT NULL")
	private String remark;//备注
	@Column(oth = "NOT NULL")
	private String finc_item;//财务项目编号
	@Column(oth = "NOT NULL")
	private String finc_item_name;//财务项目名称
	@Column
	private int consultation;//是否会诊 0否1是
	@Column(oth = "NOT NULL", type = "char(10)")
	private String finc_time;//财务核算时间  yyyy-MM-dd
	@Column
	private String patient_dept;//病人所在科室
	@Column
	private String nurse;//责任护士
	@Column
	private String doctor;//责任医生
	@Column
	private String doctor_dept;//医生所在科室
	@Column
	private String sectionno;//医生归属医疗组号
	@Column
	private String sectionname;//医生归属医疗组名称
	@Column
	private String order_sectionno;//申请医生归属医疗组号
	@Column
	private String order_sectionname;//申请医生归属医疗组名称
	@Column
	private String do_sectionno;//执行医生归属医疗组号
	@Column
	private String do_sectionname;//执行医生归属医疗组名称
	@Column
	private int patient_age;//病人年龄

	public String getHospital() {
		return hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public String getPkid() {
		return pkid;
	}

	public void setPkid(String pkid) {
		this.pkid = pkid;
	}

	public String getFlowid() {
		return flowid;
	}

	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCharge_time() {
		return charge_time;
	}

	public void setCharge_time(String charge_time) {
		this.charge_time = charge_time;
	}

	public String getOrder_dept() {
		return order_dept;
	}

	public void setOrder_dept(String order_dept) {
		this.order_dept = order_dept;
	}

	public String getOrder_staff() {
		return order_staff;
	}

	public void setOrder_staff(String order_staff) {
		this.order_staff = order_staff;
	}

	public String getOrder_staff_name() {
		return order_staff_name;
	}

	public void setOrder_staff_name(String order_staff_name) {
		this.order_staff_name = order_staff_name;
	}

	public String getDo_dept() {
		return do_dept;
	}

	public void setDo_dept(String do_dept) {
		this.do_dept = do_dept;
	}

	public String getDo_staff() {
		return do_staff;
	}

	public void setDo_staff(String do_staff) {
		this.do_staff = do_staff;
	}

	public String getDo_staff_name() {
		return do_staff_name;
	}

	public void setDo_staff_name(String do_staff_name) {
		this.do_staff_name = do_staff_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFinc_item() {
		return finc_item;
	}

	public void setFinc_item(String finc_item) {
		this.finc_item = finc_item;
	}

	public String getFinc_item_name() {
		return finc_item_name;
	}

	public void setFinc_item_name(String finc_item_name) {
		this.finc_item_name = finc_item_name;
	}

	public int getConsultation() {
		return consultation;
	}

	public void setConsultation(int consultation) {
		this.consultation = consultation;
	}

	public String getFinc_time() {
		return finc_time;
	}

	public void setFinc_time(String finc_time) {
		this.finc_time = finc_time;
	}

	public String getPatient_dept() {
		return patient_dept;
	}

	public void setPatient_dept(String patient_dept) {
		this.patient_dept = patient_dept;
	}

	public String getNurse() {
		return nurse;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getDoctor_dept() {
		return doctor_dept;
	}

	public void setDoctor_dept(String doctor_dept) {
		this.doctor_dept = doctor_dept;
	}

	public String getSectionno() {
		return sectionno;
	}

	public void setSectionno(String sectionno) {
		this.sectionno = sectionno;
	}

	public String getSectionname() {
		return sectionname;
	}

	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}

	public String getOrder_sectionno() {
		return order_sectionno;
	}

	public void setOrder_sectionno(String order_sectionno) {
		this.order_sectionno = order_sectionno;
	}

	public String getOrder_sectionname() {
		return order_sectionname;
	}

	public void setOrder_sectionname(String order_sectionname) {
		this.order_sectionname = order_sectionname;
	}

	public String getDo_sectionno() {
		return do_sectionno;
	}

	public void setDo_sectionno(String do_sectionno) {
		this.do_sectionno = do_sectionno;
	}

	public String getDo_sectionname() {
		return do_sectionname;
	}

	public void setDo_sectionname(String do_sectionname) {
		this.do_sectionname = do_sectionname;
	}

	public int getPatient_age() {
		return patient_age;
	}

	public void setPatient_age(int patient_age) {
		this.patient_age = patient_age;
	}
}
