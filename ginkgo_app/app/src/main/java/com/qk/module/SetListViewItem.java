package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * SetListViewItem
 */
public class SetListViewItem {
    private int img;
    private String text;
    private String text2;



    public SetListViewItem() {
    }

    public SetListViewItem(int img, String text, String text2) {
        this.img = img;
        this.text = text;
        this.text2 = text2;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    @Override
    public String toString() {
        return "SetListViewItem{" +
                "img=" + img +
                ", text='" + text + '\'' +
                ", text2='" + text2 + '\'' +
                '}';
    }
}
