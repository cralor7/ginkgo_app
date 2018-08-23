package com.qk.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * IDockingAdapterDataSource
 */
public interface IDockingAdapterDataSource {
    int getGroupCount();
    int getChildCount(int groupPosition);
    Object getGroup(int groupPosition);
    Object getChild(int groupPosition, int childPosition);
    View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);
    View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);
}
