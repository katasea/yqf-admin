package com.common.auto.annotation;

import com.common.Global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义列注解
 * @author chenfuqiang
 *
 */
//该注解用于方法声明
@Target(ElementType.FIELD)
//VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
//将此注解包含在javadoc中
@Documented
//允许子类继承父类中的注解
@Inherited
public @interface Column {
	//=====================数据库定义方面=====================//
	/**
	 * e.g.  1  101  10101  102 10201
	 * 自动根据数据库记录按上方规则生成主键代码。
	 * 只能放置于 treeId 是true 的列
	 * @return
	 */
	public abstract boolean autoGenneral() default false;

	/**
	 * 默认当前字段名。
	 * @return
	 */
	public abstract String name() default Global.NULLSTRING;
	/**
	 * 默认值是normal
	 * 仅可以填写【primary,normal】其他无效。
	 * 可以多个字段都是primary做联合主键。
	 * @return
	 */
	public abstract String flag() default "normal";
	/**
	 * 默认值是varchar(50)
	 * 【varchar(50),decimal(18,2),int,smallint,...】
	 * 字段类型，支持SQLSERVER数据库类型
	 * @return
	 */
	public abstract String type() default "varchar(50)";

	/**
	 * 字段默认值 禁止使用，可能导致产生约束从而导致无法后续修复表结构。
	 * 支持 newid()方法
	 * 支持变量如下【@hbdwbh,@dwbh,@year,@month】
	 * @return
	 */
//	@Deprecated
//	public abstract String defaultValue() default "";
	/**
	 * 【identity(1,1),NOT NULL,NULL】
	 * 自增，非空，空，默认空值
	 * 默认不写表示primary则是NOT NULL normal是NULL 如果是normal 想要NOT NULL 可以加OTH="NOT NULL"
	 * @return
	 */
	public abstract String oth() default Global.NULLSTRING;

	//=====================================JS 设定部分===========================================//
	/**
	 * 生成js model时候的数据类型，如果不指定，他会根据type来判断类型
	 * 例如boolean 数据库没有这个类型，而js需要，所以单独出来。但是如果跟数据库一致，则不需要配置
	 * 默认取type()值。
	 * @return
	 */
	public abstract String jstype() default Global.NULLSTRING;

	/**
	 * 生成grid 或者tree 的列名称 如果为空则不显示在页面上
	 * @return
	 */
	public abstract String jsname() default Global.NULLSTRING;

	/**
	 * 设置列在 js grid 里面的宽度
	 * 默认所有列1:1平均分。
	 * @return
	 */
	public abstract int jswidth() default 0;


	/**
	 * 是否允许为空
	 * 默认允许为空
	 * @return
	 */
	public abstract boolean jsAllowBlank() default true;

	/**
	 * 主页面隐藏该列，但增加修改页面会显示出来。如果都不要这个列显示，只需要把jsname=''即可。
	 * 默认显示(false)
	 * @return
	 */
	public abstract boolean jshidden() default false;

	/**
	 * 当其为空则会自动获取jstype来判断，当jstype为空时候会自动获取type进行判断
	 * 这里针对增加修改的展示的 xtype进行定义特殊的组件例如 radioGroup,checkBox等不能用
	 * 数据库字段类型或者jstype类型判断的类型进行定义。
	 * e.g.  radiogroup [部分配置项 例如"items:[{
	 inputValue: 'visa',
	 name : 'xxx',
	 boxLabel: 'VISA',
	 checked: true
	 }, {
	 inputValue: 'mastercard',
	 name : 'xxx',
	 boxLabel: 'MasterCard'
	 }]]
	 * @return
	 */
	public abstract String jsxtype() default Global.NULLSTRING;

	/**
	 * e.g.  function(value,cellmeta,record,row,col,store){if(value=='1'){return '支出类型'}else {return '收入类型'}}
	 * 与extjs renderer 编写的方法内容一致。采取直接替换。
	 * @return
	 */
	public abstract String render() default Global.NULLSTRING;
	/**
	 * e.g.  where hbdwbh = ? and dwbh = ? and keyid = value
	 * 只过滤 hbdwbh,dwbh,以及设定的当前列。
	 * 对于不分年的表，若要对里面的年份也进行过滤，需要自己修改代码。
	 * @return
	 */
	public abstract boolean jsValidator() default false;

	//=================================tree 树设定部分 ============================//
	/**
	 * 如果是树的话需要指定主列
	 * @return
	 */
	public abstract boolean treecolumn() default false;
	/**
	 * 标识该属性为tree id
	 * 与treeparentid对应
	 * @return
	 */
	public abstract boolean treeId() default false;
	/**
	 * 标识树父节点是哪个字段
	 * @return
	 */
	public abstract boolean treeparentId() default false;
	/**
	 * 要求数据库类型和属性类型必须为int。
	 * @return
	 */
	public abstract boolean treeleaf() default false;

	/**
	 * 对树起作用，会自动汇总子节点的数值在增删改子节点的时候。
	 * @return
	 */
	public abstract boolean treeSum() default false;

	//=======================================其他功能部分=========================//

	/**
	 * 是否需要查询这个字段的值
	 * keyWord作用域
	 * @return
	 */
	public abstract boolean keyWordFilte() default  false;
}
