package com.main.pojo.prfm;

import java.io.Serializable;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;
import com.main.pojo.platform.TreeNodeBean;
@Table(name = "PM_Norm",jsname = "质量综合指标",includeSupperClass=false)
public class Norm extends TreeNodeBean implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = -9011777967101276026L;
	/**
	 * id
	 */
	@Column(flag="primary",treeId=true,autoGenneral=true,treecolumn = true,jsValidator = true,jsname="id")
	private String uid;
	
	/**
	 * 字典名称
	 */
	@Column()
	private String companyid;
	/**
	 * 助记码
	 */
	@Column(oth = "NOT NULL",jsname="指标名称",jswidth=100,keyWordFilte=true)
	private String text;
	/**
	 * 考核标准
	 */
	@Column(jsname="考核标准",jswidth=100)
	private String checknorm;
	/**
	 * 评分标准
	 */
	@Column(jsname="评分标准",jswidth=100)
	private String recordnorm;
	/**
	 * 扣分类型
	 */
	@Column(type="int",jsname = "扣分类型", render = "function(v){if(v=='1'){return '按次扣分';}else if(v=='2'){return '加分项';}else{return '标准比例计分';}}",
	jsxtype = "radiogroup [items:[{"
			+ "inputValue: '1',"
			+ "boxLabel: '按次扣分',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "checked: true"
			+ "}, {"
			+ "inputValue: '2',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '加分项'"
			+ "}, {"
			+ "inputValue: '3',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '标准比例计分'"
			+ "}]]")
	private int kouFenType;
	/**
	 * 扣分类型
	 */
	@Column(type="int",jsname = "指标类型", render = "function(v){if(v=='1'){return '月';}else if(v=='2'){return '季度';}else{return '年度';}}",
	jsxtype = "radiogroup [items:[{"
			+ "inputValue: '1',"
			+ "boxLabel: '月',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "checked: true"
			+ "}, {"
			+ "inputValue: '2',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '季度'"
			+ "}, {"
			+ "inputValue: '3',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '年度'"
			+ "}]]")
	private int zhiBiaoType;
	/**
	 * 绩效占比
	 */
	@Column(type="decimal(18,6)",jsname = "绩效占比",jswidth=150)
	private double jiXiaoZhanBi;
	/**
	 * 公式
	 */
	@Column(type="varchar(500)",jsname="公式",jswidth=150)
	private String formula;
	/**
	 * 部门还是个人
	 */
	@Column(type="int")
	private int bigtype;
	/**
	 * 排序号
	 */
	@Column(type="int")
	private int orderid;
	/**
	 * 是否计分
	 */
	@Column(type="int",jsname = "是否计分", render = "function(v){if(v=='1'){return '是';}else{return '否';}}",
	jsxtype = "radiogroup [items:[{"
			+ "inputValue: '1',"
			+ "boxLabel: '是',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "checked: true"
			+ "}, {"
			+ "inputValue: '0',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '否'"
			+ "}]]")
	private int iscomp;
	/**
	 * 是否停用
	 */
	@Column(type="int",jsname = "是否停用", render = "function(v){if(v=='1'){return '是';}else{return '否';}}",
	jsxtype = "radiogroup [items:[{"
			+ "inputValue: '1',"
			+ "boxLabel: '是',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "checked: true"
			+ "}, {"
			+ "inputValue: '0',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '否'"
			+ "}]]")
	private int isstop;
	/**
	 * 标准分数
	 */
	@Column(type="decimal(18,6)",jswidth=100,jsname="标准分数")
	private double score;
	/**
	 * 单次扣分
	 */
	@Column(type="decimal(18,6)",jswidth=100,jsname="单词扣分")
	private double rec1;
	/**
	 * 最多扣分
	 */
	@Column(type="decimal(18,6)",jswidth=100,jsname="最多扣分")
	private double recsum;
	/**
	 * 扣分是否可改
	 */
	@Column(type="int",jsname = "扣分是否可改", render = "function(v){if(v=='1'){return '是';}else{return '否';}}",
	jsxtype = "radiogroup [items:[{"
			+ "inputValue: '1',"
			+ "boxLabel: '是',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "checked: true"
			+ "}, {"
			+ "inputValue: '0',"
			+ "anchor  : '10%',"
			+ "name  : 'protype',"
			+ "boxLabel: '否'"
			+ "}]]")
	private int recforce;
    /**
     * 父节点
     */
    @Column(oth = "NOT NULL",treeparentId=true)
	private String parentid;
    /**
     * 是否叶子节点
     * 1 是叶子节点 0 是父节点
     */
    @Column(treeleaf=true)
	private int isleaf;
	/**
	 * 年份
	 */
	@Column(type="int")
	private int year;
	
	
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getChecknorm() {
		return checknorm;
	}
	public void setChecknorm(String checknorm) {
		this.checknorm = checknorm;
	}
	public String getRecordnorm() {
		return recordnorm;
	}
	public void setRecordnorm(String recordnorm) {
		this.recordnorm = recordnorm;
	}
	public int getKouFenType() {
		return kouFenType;
	}
	public void setKouFenType(int kouFenType) {
		this.kouFenType = kouFenType;
	}
	public int getZhiBiaoType() {
		return zhiBiaoType;
	}
	public void setZhiBiaoType(int zhiBiaoType) {
		this.zhiBiaoType = zhiBiaoType;
	}
	public double getJiXiaoZhanBi() {
		return jiXiaoZhanBi;
	}
	public void setJiXiaoZhanBi(double jiXiaoZhanBi) {
		this.jiXiaoZhanBi = jiXiaoZhanBi;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public int getBigtype() {
		return bigtype;
	}
	public void setBigtype(int bigtype) {
		this.bigtype = bigtype;
	}
	public int getOrderid() {
		return orderid;
	}
	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}
	public int getIscomp() {
		return iscomp;
	}
	public void setIscomp(int iscomp) {
		this.iscomp = iscomp;
	}
	public int getIsstop() {
		return isstop;
	}
	public void setIsstop(int isstop) {
		this.isstop = isstop;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getRec1() {
		return rec1;
	}
	public void setRec1(double rec1) {
		this.rec1 = rec1;
	}
	public double getRecsum() {
		return recsum;
	}
	public void setRecsum(double recsum) {
		this.recsum = recsum;
	}
	public int getRecforce() {
		return recforce;
	}
	public void setRecforce(int recforce) {
		this.recforce = recforce;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public int getIsleaf() {
		return isleaf;
	}
	public void setIsleaf(int isleaf) {
		this.isleaf = isleaf;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
