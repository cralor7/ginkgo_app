package com.qk.girdview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 这里写GridView的onMeasure(int widthMeasureSpec, int heightMeasureSpec)的方法，将GridView重新测量，并且指定GridView的新的高度
 * 解决android:layout_height="wrap_content"  嵌套到listview里只显示一行
 */
public class CanAddInListViewGridView extends GridView {

    public CanAddInListViewGridView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CanAddInListViewGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public CanAddInListViewGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    /**
     * 不滚动
     * @param widthMeasureSpec widthMeasureSpec
     * @param heightMeasureSpec heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
