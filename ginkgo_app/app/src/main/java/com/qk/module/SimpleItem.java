package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * SimpleItem
 */
public class SimpleItem {
    private String text;

    public SimpleItem() {
    }

    public SimpleItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SimpleItem{" +
                "text='" + text + '\'' +
                '}';
    }
}
