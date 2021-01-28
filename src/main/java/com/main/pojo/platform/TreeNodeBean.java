package com.main.pojo.platform;

import java.io.Serializable;

import com.common.auto.annotation.Column;

/**
 * @author CFQ
 * 子类若要继承此类，可以指定注解  includeSupperClass 若true 则包含父类的字段。默认false
 */
public class TreeNodeBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1641729730524337568L;
    @Column
    private String id;
    @Column
    private String text;
    @Column(type = "int", jstype = "boolean")
    private boolean leaf;
    @Column(type = "int", jstype = "boolean")
    private boolean expanded;
    @Column
    private String iconCls;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

}
