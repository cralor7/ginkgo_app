package com.qk.util;

import android.view.View;
/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * RecycleViewLisitenter
 */
public class RecycleViewLisitenter {

    /**
     * RecycleView的条目点击监听
     */
    public interface onItemClickLisitenter{
        /**
         *  单击
         * @param v View
         * @param position position
         */
        void onItemClick(View v, int position);
    }
    /**
     * RecycleView的条目长按点击监听
     */
    public interface onItemLongClickLisitenter{
        /**
         * 长按
         * @param v View
         * @param  position position
         */
        void onItemLongClick(View v, int position);
    }
}
