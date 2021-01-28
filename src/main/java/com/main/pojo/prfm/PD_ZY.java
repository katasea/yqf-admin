package com.main.pojo.prfm;

import com.common.auto.annotation.Column;
import com.common.auto.annotation.Table;

import java.io.Serializable;

/**
 * 住院原始数据
 * */
@Table(name="PD_ZY@year")
public class PD_ZY implements Serializable {


    private static final long serialVersionUID = -4927055683233793776L;
    @Column(flag = "primary")
    private String pkid;//主键

    @Column(type = "varchar(50)")
    private String companyid;//单位编号

    @Column(type = "int")
    private int month;//月

    @Column(type = "int")
    private int day;//天

    @Column(type = "varchar(100)")
    private String itembh;//收费编号

    @Column(type = "varchar(50)")
    private String itemtypebh;//收费项目类别

    @Column(type = "varchar(50)")
    private String ticketbh;//发票项目

    @Column(type = "varchar(50)")
    private String sbmbh;//申请部门

    @Column(type = "varchar(50)")
    private String sgrbh;//申请人

    @Column(type = "varchar(50)")
    private String zbmbh;//执行部门

    @Column(type = "varchar(50)")
    private String zgrbh;//执行人

    @Column(type = "decimal(18,2)")
    private String amount;//金额

    @Column(type = "int")
    private int count;//数量

    @Column(type = "varchar(300)")
    private String ICD;//ICD

    @Column(type = "varchar(500)")
    private String reverse;//备注

    @Column(type = "varchar(50)")
    private String brbh;//病人编号

    @Column(type = "varchar(300)")
    private String reverse1;//备用字段1

    @Column(type = "varchar(300)")
    private String reverse2;//备用字段2

    @Column(type = "varchar(300)")
    private String reverse3;//备用字段3

    @Column(type = "varchar(300)")
    private String reverse4;//备用字段4

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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getItembh() {
        return itembh;
    }

    public void setItembh(String itembh) {
        this.itembh = itembh;
    }

    public String getItemtypebh() {
        return itemtypebh;
    }

    public void setItemtypebh(String itemtypebh) {
        this.itemtypebh = itemtypebh;
    }

    public String getTicketbh() {
        return ticketbh;
    }

    public void setTicketbh(String ticketbh) {
        this.ticketbh = ticketbh;
    }

    public String getSbmbh() {
        return sbmbh;
    }

    public void setSbmbh(String sbmbh) {
        this.sbmbh = sbmbh;
    }

    public String getSgrbh() {
        return sgrbh;
    }

    public void setSgrbh(String sgrbh) {
        this.sgrbh = sgrbh;
    }

    public String getZbmbh() {
        return zbmbh;
    }

    public void setZbmbh(String zbmbh) {
        this.zbmbh = zbmbh;
    }

    public String getZgrbh() {
        return zgrbh;
    }

    public void setZgrbh(String zgrbh) {
        this.zgrbh = zgrbh;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getICD() {
        return ICD;
    }

    public void setICD(String ICD) {
        this.ICD = ICD;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getBrbh() {
        return brbh;
    }

    public void setBrbh(String brbh) {
        this.brbh = brbh;
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

    public String getReverse4() {
        return reverse4;
    }

    public void setReverse4(String reverse4) {
        this.reverse4 = reverse4;
    }
}
