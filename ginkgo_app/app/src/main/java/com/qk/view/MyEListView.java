package com.qk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 自定义ExpandableList
 */
public class MyEListView extends ExpandableListView {
    public MyEListView(Context context) {
        super(context);
    }

    public MyEListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyEListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }




}
