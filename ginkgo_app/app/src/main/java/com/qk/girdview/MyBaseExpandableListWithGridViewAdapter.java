package com.qk.girdview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;
import com.qk.activity.IndexActivity;

import java.util.List;
import java.util.Map;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * ExpandableListWithGridView Adapter
 */
public class MyBaseExpandableListWithGridViewAdapter extends BaseExpandableListAdapter {

    List<Map<String, Object>> parentMapList;
    List<List<Map<String, Object>>> childMapList_list;
    Context context;
    List<Map<String,Object>> nowChildMapList;
    public MyBaseExpandableListWithGridViewAdapter(Context context, List<Map<String, Object>> parentMapList, List<List<Map<String, Object>>> childMapList_list) {
        this.parentMapList = parentMapList;
        this.childMapList_list = childMapList_list;
        this.context = context;
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
        String parentName = parentMapList.get(groupPosition).get("parentName").toString();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.child_layout_girdview, null);

        }
        CanAddInListViewGridView canAddInListViewGridView = convertView.findViewById(R.id.channel_item_child_gridView);
        canAddInListViewGridView.setNumColumns(4);
      List<Map<String,Object>> childMapList= childMapList_list.get(groupPosition);
        nowChildMapList = childMapList_list.get(groupPosition);
        SimpleAdapter simpleAdapter = new SimpleAdapter(context,childMapList, R.layout.gridview_item, new String[]{"childName"}, new int[]{R.id.id_tv_gridview_item});
        canAddInListViewGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Toast.makeText(context, "点击了" + nowChildMapList.get(position).get("childName"), Toast.LENGTH_SHORT).show();
                switch (nowChildMapList.get(position).get("childName")+""){
                    case "订单查询":
                        intent.setClass(context,IndexActivity.class);
                        intent.putExtra("order",1);
                        context.startActivity(intent);
                        break;
                    default:break;
                }
            }
        });
        canAddInListViewGridView.setAdapter(simpleAdapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
