package com.qk.util;

import android.widget.EditText;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * EditText工具类
 */
public class EditTextUtils {

    /**
     * 获取EditText内容,防止空指针异常
     * @param editText editText
     * @return EditText内容
     */
    public static String getString(EditText editText){
        return editText.getText() == null?"":editText.getText().toString();
    }

    /**
     * 获取EditText内容,防止空指针异常
     * @param  editText editText
     * @return EditText内容
     */
    public static boolean isEmpty(EditText editText){
        String edtString = editText.getText() == null?"":editText.getText().toString();
        return "".equals(edtString);
    }
}
