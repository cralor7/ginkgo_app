package com.qk.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qk.R;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 头部菜单
 */
public class TopMenuHeader {

    /**
     * 顶部菜单左边按钮
     */
    public ImageView topMenuLeft;

    /**
     * 顶部菜单文字
     */
    public TextView topMenuTitle;

    public TopMenuHeader(View v) {
        // 右边按钮
        topMenuLeft = v.findViewById(R.id.left_back_menu);

        // 顶部中间文字
        topMenuTitle = v.findViewById(R.id.menu_text);

    }
}