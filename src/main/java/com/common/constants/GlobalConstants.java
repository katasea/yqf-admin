package com.common.constants;

/**
 * 全局静态业务常量
 */
public class GlobalConstants {
	//部门还是个人
	public static final Integer DEPT_OR_PERSON_D = 0;
	public static final Integer DEPT_OR_PERSON_P = 1;

	//指标类型
	public static final Integer ZHIBIAOTYPE_YUE = 1;//月
	public static final Integer ZHIBIAOTYPE_JI = 2;//季度
	public static final Integer ZHIBIAOTYPE_NIAN = 3;//年

	public static final String ALLOWADDOPT = "1";//允许新增操作;

	//中间辅助数据
	public static final int MIDDIC_FORMULA_TYPE = 2;//公式类型
	public static final int MIDDIC_INPUT_TYPE = 1;//输入导入类型

	//权限中心
	public static final String AUTHCODE_MIDDIC_INPUT = "1";//辅助数据录入权限
	public static final String AUTHCODE_MIDDIC_INPUT_BM = "2";//辅助数据录入关联部门
	public static final String AUTHCODE_PORFORMANCE_DEPT_GROUP = "3";//绩效部门捆绑关联
	public static final String AUTHCODE_KH_DEPT_GROUP = "4";//考核部门捆绑关联
	public static final String AUTHCODE_ZB_DEPT_RELA = "5";//质量指标关联部门权限
}
