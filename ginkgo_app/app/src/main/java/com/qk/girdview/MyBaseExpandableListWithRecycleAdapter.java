package com.qk.girdview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;
import com.qk.activity.CameraActivity;
import com.qk.activity.IndexActivity;
import com.qk.activity.PhotoActivity;
import com.qk.activity.QmuiActivity;
import com.qk.adapter.MyRecyclerViewAdapter;
import com.qk.module.SetListViewItem;
import com.qk.util.RecycleViewLisitenter;

import java.util.List;
import java.util.Map;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * ExpandableListWithRecycleView Adapter
 */
public class MyBaseExpandableListWithRecycleAdapter extends BaseExpandableListAdapter {

    List<Map<String, Object>> parentMapList;
    List<List<SetListViewItem>> childMapList_list;
    Context context;
    List<Map<String,Object>> nowChildMapList;
    public MyBaseExpandableListWithRecycleAdapter(Context context, List<Map<String, Object>> parentMapList, List<List<SetListViewItem>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context=context;
    }

    /**
     * 获取当前父item的数据数量
     * @return 数量
     */
    @Override
    public int getGroupCount() {
        return parentMapList.size();
    }
    /**
     * 获取当前父item下的子item的个数
     * @param groupPosition 父item
     * @return 数量
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        //子列表项的数量
        // return childMapList_list.get(groupPosition).size();
        //子列表项的数量本来是list  多个  现在同样的数据以girdview形式展示  就只有个girdview项 所以返回1
        return 1;
    }
    /**
     * 获取当前父item的数据
     * @param groupPosition 父item
     * @return 数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return parentMapList.get(groupPosition);
    }
    /**
     * 得到子item需要关联的数据
     * @param groupPosition 父item
     * @param childPosition 子item
     * @return 数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMapList_list.get(groupPosition).get(childPosition);
    }
    /**
     * 得到父item的ID
     * @param groupPosition 父item
     * @return ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    /**
     * 得到子item的ID
     * @param groupPosition 父item
     * @param childPosition 子item
     * @return ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        //return false;
        return true;
    }
    /**
     * 设置父item组件
     * @param groupPosition 父item
     * @param isExpanded 是否展开
     * @param convertView convertView
     * @param parent parent
     * @return convertView
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.parent_layout, null);
        }
        TextView tevTitleParent = convertView
                .findViewById(R.id.tv_title_parent);
        String parentName =parentMapList.get(groupPosition).get("parentName").toString();
        tevTitleParent.setText(parentName);

        ImageView imvParent = convertView.findViewById(R.id.iv_img_parent);
        int parentIcon = Integer.parseInt(parentMapList.get(groupPosition).get("parentIcon").toString());
        imvParent.setImageResource(parentIcon);

        ImageView imvParentRight = convertView.findViewById(R.id.iv_img_parent_right);
        //判断isExpanded就可以控制是按下还是关闭，同时更换图片
        if(isExpanded){
            imvParentRight.setImageResource(R.mipmap.channel_expandablelistview_top_icon);
        }else{
            imvParentRight.setImageResource(R.mipmap.channel_expandablelistview_bottom_icon);
        }
        return convertView;
    }
    /**
     * 设置子item的组件
     * @param groupPosition 父item
     * @param childPosition 子item
     * @param isLastChild 是否是最后一个
     * @param convertView convertView
     * @param parent parent
     * @return convertView
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recycle, null);
        }
        RecyclerView mRecyclerView = convertView.findViewById(R.id.recyclerView);
//      设置RecyclerView管理器 设置垂直显示还是横向显示
//      mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
//      设置网格布局
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(0));
//      初始化适配器
        final List<SetListViewItem> childMapList = childMapList_list.get(groupPosition);
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(context, childMapList);
//        设置recycleview表格间距
//        mRecyclerView.addItemDecoration(new MyItemDecoration(context));
//        mRecyclerView.addItemDecoration(new SpaceItemDecoration(1));

//      设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        /*
         * 设置自定义的条目点击事件
         */
        mAdapter.setOnItemClickLisitenter(new RecycleViewLisitenter.onItemClickLisitenter() {
            @Override
            public void onItemClick(View v, int position) {
                TextView text = v.findViewById(R.id.MyText) ;
                goToActivity(childMapList, position);
                Toast.makeText(context, childMapList.get(position).getText() + "", Toast.LENGTH_SHORT).show();
            }
        });
        /*
         * 设置自定义的条目长按监听
         */
        mAdapter.setOnItemLongClickLisitenter(new RecycleViewLisitenter.onItemLongClickLisitenter() {
            @Override
            public void onItemLongClick(View v, int position) {
//                goToActivity(position);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void goToActivity(List<SetListViewItem> childMapList,int position){
        Intent intent = new Intent();
        switch (childMapList.get(position).getText()){
            case "NFC":
                intent.setClass(context,IndexActivity.class);
                intent.putExtra("fragmentNumber",1);
                context.startActivity(intent);
                break;
            case "订单查询":
                intent.setClass(context,IndexActivity.class);
                intent.putExtra("fragmentNumber",1);
                context.startActivity(intent);
                break;
            case "订单审核":
                intent.setClass(context,QmuiActivity.class);
                intent.putExtra("fragmentNumber",1);
                context.startActivity(intent);
                break;
            case "订单修改":
                intent.setClass(context,IndexActivity.class);
                intent.putExtra("fragmentNumber",2);
                context.startActivity(intent);
                break;
            case "订单加急":
                intent.setClass(context, CameraActivity.class);
                intent.putExtra("fragmentNumber",2);
                context.startActivity(intent);
                break;
            case "订单转让":
                intent.setClass(context, PhotoActivity.class);
                intent.putExtra("fragmentNumber",2);
                context.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
