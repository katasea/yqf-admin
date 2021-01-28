package com.main.pojo.prfm;

import java.io.Serializable;
import java.math.BigDecimal;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

/**
 * @ClassName: RbrvsCoefficient
 * @description: 医院RBRVS系数
 * @author: LPZ
 * @Date: 2018年12月4日 下午9:17:59
 */
@Table(name = "PD_RBRVS_XS")
public class RbrvsCoefficient implements Serializable {

	private static final long serialVersionUID = -8457755205788031127L;

	/**
	 * 主键
	 */
	@Column(flag = "primary")
	private String uid;

	/**
	 * 公司ID
	 */
	@Column
	private String companyid;
	/**
	 * 项目编号
	 */
	@Column
	private String xmbh;
	/**
	 * 项目名称
	 */
	@Column(type = "varchar(200)")
	private String xmtext;
	/**
	 * 单价
	 */
	@Column(type = "decimal(18,2)")
	private BigDecimal price;
	/**
	 * 项目类别
	 */
	@Column
	private String itemtype;
	/**
	 * 医生原始系数
	 */
	@Column(type = "decimal(18,6)")
	private String oridoc;
	/**
	 * 护士原始系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal orinurse;
	/**
	 * 医技原始系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal orioperator;
	/**
	 * 麻醉原始系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal oripoison;
	/**
	 * 诊断原始系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal oridiagnosis;
	/**
	 * 医生调整系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal adjdoc;
	/**
	 * 护士调整系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal adjnurse;
	/**
	 * 医技调整系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal adjoperator;
	/**
	 * 麻醉调整系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal adjpoison;
	/**
	 * 诊断调整系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal adjdiagnosis;
	/**
	 * 医生最终系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal doc;
	/**
	 * 护士最终系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal nurse;
	/**
	 * 医技最终系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal operator;
	/**
	 * 麻醉最终系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal poision;
	/**
	 * 诊断最终系数
	 */
	@Column(type = "decimal(18,6)")
	private BigDecimal diagnosis;
	/**
	 * 是否停用
	 */
	@Column(type = "int")
	private int isstop;
	/**
	 * 备注信息
	 */
	@Column(type = "varchar(500)")
	private String memo;
	/**
	 * 类型
	 */
	@Column(type = "int")
	private int style;
	/**
	 * 修改时间
	 */
	@Column
	private String updateTime;
	/**
	 * 备用字段
	 */
	@Column
	private String reverse;
	/**
	 * 备用字段2
	 */
	@Column
	private String reverse2;
	/**
	 * 备用字段3
	 */
	@Column
	private String reverse3;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getXmbh() {
		return xmbh;
	}

	public void setXmbh(String xmbh) {
		this.xmbh = xmbh;
	}

	public String getXmtext() {
		return xmtext;
	}

	public void setXmtext(String xmtext) {
		this.xmtext = xmtext;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getOridoc() {
		return oridoc;
	}

	public void setOridoc(String oridoc) {
		this.oridoc = oridoc;
	}

	public BigDecimal getOrinurse() {
		return orinurse;
	}

	public void setOrinurse(BigDecimal orinurse) {
		this.orinurse = orinurse;
	}

	public BigDecimal getOrioperator() {
		return orioperator;
	}

	public void setOrioperator(BigDecimal orioperator) {
		this.orioperator = orioperator;
	}

	public BigDecimal getOripoison() {
		return oripoison;
	}

	public void setOripoison(BigDecimal oripoison) {
		this.oripoison = oripoison;
	}

	public BigDecimal getOridiagnosis() {
		return oridiagnosis;
	}

	public void setOridiagnosis(BigDecimal oridiagnosis) {
		this.oridiagnosis = oridiagnosis;
	}

	public BigDecimal getAdjdoc() {
		return adjdoc;
	}

	public void setAdjdoc(BigDecimal adjdoc) {
		this.adjdoc = adjdoc;
	}

	public BigDecimal getAdjnurse() {
		return adjnurse;
	}

	public void setAdjnurse(BigDecimal adjnurse) {
		this.adjnurse = adjnurse;
	}

	public BigDecimal getAdjoperator() {
		return adjoperator;
	}

	public void setAdjoperator(BigDecimal adjoperator) {
		this.adjoperator = adjoperator;
	}

	public BigDecimal getAdjpoison() {
		return adjpoison;
	}

	public void setAdjpoison(BigDecimal adjpoison) {
		this.adjpoison = adjpoison;
	}

	public BigDecimal getAdjdiagnosis() {
		return adjdiagnosis;
	}

	public void setAdjdiagnosis(BigDecimal adjdiagnosis) {
		this.adjdiagnosis = adjdiagnosis;
	}

	public BigDecimal getDoc() {
		return doc;
	}

	public void setDoc(BigDecimal doc) {
		this.doc = doc;
	}

	public BigDecimal getNurse() {
		return nurse;
	}

	public void setNurse(BigDecimal nurse) {
		this.nurse = nurse;
	}

	public BigDecimal getOperator() {
		return operator;
	}

	public void setOperator(BigDecimal operator) {
		this.operator = operator;
	}

	public BigDecimal getPoision() {
		return poision;
	}

	public void setPoision(BigDecimal poision) {
		this.poision = poision;
	}

	public BigDecimal getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(BigDecimal diagnosis) {
		this.diagnosis = diagnosis;
	}

	public int getIsstop() {
		return isstop;
	}

	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}

	public String getReverse2() {
		return reverse2;
	}

	public void setReverse2(String reverse2) {
		this.reverse2 = reverse2;
	}

	public String getReverse3() {
		return reverse3;
	}

	public void setReverse3(String reverse3) {
		this.reverse3 = reverse3;
	}

}
