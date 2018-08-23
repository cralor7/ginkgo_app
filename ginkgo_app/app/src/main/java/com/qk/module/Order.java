package com.qk.module;

import java.io.Serializable;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * 订单实体类
 */
public class Order implements Serializable{
    private String xpcj;
    private String gzcj;
    private String category;
    /**
     * 订单号
     */
    private String ddh;
    /**
     * 经销商代码
     */
    private String jxscode;
    /**
     * 业务模式
     */
    private String ywms;
    /**
     * 订单类别
     */
    private String ddlb;
    /**
     * 成车车型号
     */
    private String cccxh;
    /**
     * 颜色
     */
    private String ys;
    /**
     * 数量
     */
    private String sl;
    /**
     * 定单价
     */
    private String dpprice;
    /**
     * 单据状态
     */
    private String djzt;

    private int number;

    public String getXpcj() {
        return xpcj;
    }

    public void setXpcj(String xpcj) {
        this.xpcj = xpcj;
    }

    public String getGzcj() {
        return gzcj;
    }

    public void setGzcj(String gzcj) {
        this.gzcj = gzcj;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Order{" +
                "xpcj='" + xpcj + '\'' +
                ", gzcj='" + gzcj + '\'' +
                ", category='" + category + '\'' +
                ", ddh='" + ddh + '\'' +
                ", jxscode='" + jxscode + '\'' +
                ", ywms='" + ywms + '\'' +
                ", ddlb='" + ddlb + '\'' +
                ", cccxh='" + cccxh + '\'' +
                ", ys='" + ys + '\'' +
                ", sl='" + sl + '\'' +
                ", dpprice='" + dpprice + '\'' +
                ", djzt='" + djzt + '\'' +
                ", number=" + number +
                '}';
    }
    public Order(){

    }

    public Order(String xpcj, String gzcj, String category, String ddh, String jxscode, String ywms, String ddlb, String cccxh, String ys, String sl, String dpprice, String djzt, int number) {
        this.xpcj = xpcj;
        this.gzcj = gzcj;
        this.category = category;
        this.ddh = ddh;
        this.jxscode = jxscode;
        this.ywms = ywms;
        this.ddlb = ddlb;
        this.cccxh = cccxh;
        this.ys = ys;
        this.sl = sl;
        this.dpprice = dpprice;
        this.djzt = djzt;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getJxscode() {
        return jxscode;
    }

    public void setJxscode(String jxscode) {
        this.jxscode = jxscode;
    }

    public String getYwms() {
        return ywms;
    }

    public void setYwms(String ywms) {
        this.ywms = ywms;
    }

    public String getDdlb() {
        return ddlb;
    }

    public void setDdlb(String ddlb) {
        this.ddlb = ddlb;
    }

    public String getCccxh() {
        return cccxh;
    }

    public void setCccxh(String cccxh) {
        this.cccxh = cccxh;
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getDpprice() {
        return dpprice;
    }

    public void setDpprice(String dpprice) {
        this.dpprice = dpprice;
    }

    public String getDjzt() {
        return djzt;
    }

    public void setDjzt(String djzt) {
        this.djzt = djzt;
    }
}
