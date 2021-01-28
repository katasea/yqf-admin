package com.main.pojo.platform;

import com.common.auto.annotation.AutoCode;
import com.common.auto.annotation.Column;
import com.common.auto.annotation.MyFunction;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 资源信息
 */
@AutoCode(isOverride=true,includeSupperClass=false,showWay = "tree")
@MyFunction(autoInitFromAccess=true,copyLastYearData=false,treeSort=false)
@Table(name = "PM_RESOURCE")
public class ResourcesInfo extends TreeNodeBean implements Serializable {
    private static final long serialVersionUID = 8328484452535384954L;
    /**
     * 主键
     */
    @Column(flag = "primary",treeId=true,autoGenneral=true)
    private String pkid;
    /**
     * 资源显示编码【仅显示作用，数据关联使用主键】
     */
    @Column(oth = "NOT NULL",treecolumn=true,jsname="资源编号",jswidth=150,jsAllowBlank=false,jsValidator=true,keyWordFilte=true)
    private String resid;
    /**
     * 资源名称
     */
    @Column(oth = "NOT NULL",jsname="资源名称",jswidth=150,jsAllowBlank=false,keyWordFilte=true)
    private String name;
    /**
     * 资源URL地址
     */
    @Column(oth = "NOT NULL",jsname="资源地址",jswidth=250,jsAllowBlank=false,keyWordFilte=true)
    private String resurl;
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
     * 资源类型
     * 1 菜单 2 按钮[先不考虑]
     */
    @Column(oth = "NOT NULL")
    private int type;
    /**
     * 排序编号【默认等于resid】
     */
    @Column(type = "int")
    private String sort;

	/**
	 * font - awesome 后面关键图标信息
	 */
	@Column(jsname="图标信息",jswidth=100,jsAllowBlank=false)
    private String fa;
    //======================GETTERS && SETTERS=================//


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFa() {
		return fa;
	}

	public void setFa(String fa) {
		this.fa = fa;
	}

	public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getResid() {
        return resid;
    }

    public void setResid(String resid) {
        this.resid = resid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResurl() {
        return resurl;
    }

    public void setResurl(String resurl) {
        this.resurl = resurl;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
