package com.qk.util;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * BaseResult
 */
public class BaseResult<T> {

    private int code;
    private String message;
    private T results;
    private boolean error;

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}