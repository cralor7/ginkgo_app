package com.qk.module;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/23
 */
public class GetCode {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public GetCode(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "GetCode{" +
                "data='" + data + '\'' +
                '}';
    }
}
