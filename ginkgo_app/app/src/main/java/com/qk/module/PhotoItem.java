package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * 照片数据类
 */
public class PhotoItem {
    private String img;
    private String text;
    /**
     * 判断是否显示弹出框
     */
    private Boolean check;
    /**
     * 判断checkbox状态
     */
    private Boolean getCheck;

    public PhotoItem(String img, String text, Boolean check, Boolean getCheck) {
        this.img = img;
        this.text = text;
        this.check = check;
        this.getCheck = getCheck;
    }

    public PhotoItem() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Boolean getGetCheck() {
        return getCheck;
    }

    public void setGetCheck(Boolean getCheck) {
        this.getCheck = getCheck;
    }

    @Override
    public String toString() {
        return "PhotoItem{" +
                "img='" + img + '\'' +
                ", text='" + text + '\'' +
                ", check=" + check +
                ", getCheck=" + getCheck +
                '}';
    }
}
