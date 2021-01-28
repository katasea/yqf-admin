package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 麻醉分配结果表
 * */
@Table(name="PD_POISION@year")
public class PD_POISION implements Serializable {


    private static final long serialVersionUID = -7183609589681146460L;
    @Column(flag = "primary")
    private String pkid;//主键

    @Column(type = "varchar(50)")
    private String companyid;//单位编号

    @Column(type = "int")
    private int month;//月

    @Column(type = "decimal(18,2)")
    private String amount;//金额

    @Column(type = "varchar(50)")
    private String bmbh;//部门编号

    @Column(type = "decimal(18,6)")
    private String poison;//麻醉系数

    @Column(type = "decimal(18,6)")
    private String oripoison;//麻醉原始系数

    @Column(type = "decimal(18,6)")
    private String adjpoison;//麻醉调整系数

    @Column(type = "int")
    private int soz;//开单还是执行

    @Column(type = "int")
    private int moz;//门诊还是住院

    @Column(type = "decimal(18,6)")
    private String price;//单价

    @Column(type = "int")
    private int  count;//数量

    @Column(type = "varchar(50)")
    private String sbmbh;//申请部门编号

    @Column(type = "varchar(50)")
    private String zbmbh;//执行部门编号

    @Column(type = "varchar(50)")
    private String hbmbh;//护理部门编号

    @Column(type = "varchar(50)")
    private String sgrbh;//申请人

    @Column(type = "varchar(50)")
    private String zgrbh;//执行人

    @Column(type = "varchar(50)")
    private String itemtypebh;//核算项目

    @Column(type = "varchar(50)")
    private String itembh;//收费项目

    @Column(type = "varchar(50)")
    private String itemname;//收费项目名称

    @Column(type = "varchar(300)")
    private String note;//备注

    @Column(type = "varchar(300)")
    private String reverse1;//备用字段1

    @Column(type = "varchar(300)")
    private String reverse2;//备用字段2

    @Column(type = "varchar(300)")
    private String reverse3;//备用字段3

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBmbh() {
        return bmbh;
    }

    public void setBmbh(String bmbh) {
        this.bmbh = bmbh;
    }

    public String getPoison() {
        return poison;
    }

    public void setPoison(String poison) {
        this.poison = poison;
    }

    public String getOripoison() {
        return oripoison;
    }

    public void setOripoison(String oripoison) {
        this.oripoison = oripoison;
    }

    public String getAdjpoison() {
        return adjpoison;
    }

    public void setAdjpoison(String adjpoison) {
        this.adjpoison = adjpoison;
    }

    public int getSoz() {
        return soz;
    }

    public void setSoz(int soz) {
        this.soz = soz;
    }

    public int getMoz() {
        return moz;
    }

    public void setMoz(int moz) {
        this.moz = moz;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSbmbh() {
        return sbmbh;
    }

    public void setSbmbh(String sbmbh) {
        this.sbmbh = sbmbh;
    }

    public String getZbmbh() {
        return zbmbh;
    }

    public void setZbmbh(String zbmbh) {
        this.zbmbh = zbmbh;
    }

    public String getHbmbh() {
        return hbmbh;
    }

    public void setHbmbh(String hbmbh) {
        this.hbmbh = hbmbh;
    }

    public String getSgrbh() {
        return sgrbh;
    }

    public void setSgrbh(String sgrbh) {
        this.sgrbh = sgrbh;
    }

    public String getZgrbh() {
        return zgrbh;
    }

    public void setZgrbh(String zgrbh) {
        this.zgrbh = zgrbh;
    }

    public String getItemtypebh() {
        return itemtypebh;
    }

    public void setItemtypebh(String itemtypebh) {
        this.itemtypebh = itemtypebh;
    }

    public String getItembh() {
        return itembh;
    }

    public void setItembh(String itembh) {
        this.itembh = itembh;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getReverse3() {
        return reverse3;
    }

    public void setReverse3(String reverse3) {
        this.reverse3 = reverse3;
    }
}

