package com.qk.util;

import android.app.Activity;
import android.view.View;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/10
 * 视图工具类
 */
public class ViewUtils {

    /**
     * 通用头部布局包括返回按钮和标题
     * @param activity 当前Activity
     * @param viewTitle 当前标题
     */
    public static void topLayout(final Activity activity, String viewTitle){
        TopMenuHeader topMenu = new TopMenuHeader(activity.getWindow().getDecorView());
        topMenu.topMenuTitle.setText(viewTitle);
        topMenu.topMenuLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
