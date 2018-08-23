package com.qk.module;

import java.io.Serializable;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 *
 */
public class InForm  implements Serializable {
    private int img;
    private String title;
    private String message;
    private String inform;
    private String time;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInform() {
        return inform;
    }

    public void setInform(String inform) {
        this.inform = inform;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "InForm{" +
                "img=" + img +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", inform='" + inform + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public InForm(int img, String title, String message, String inform, String time) {
        this.img = img;
        this.title = title;
        this.message = message;
        this.inform = inform;
        this.time = time;
    }

    public InForm() {

    }
}
