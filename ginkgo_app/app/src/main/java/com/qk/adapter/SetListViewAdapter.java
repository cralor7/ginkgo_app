package com.qk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qk.R;
import com.qk.module.SetListViewItem;
import com.qk.module.SimpleItem;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * SetListView Adapter。
 */
public class SetListViewAdapter extends BaseAdapter {
   private Context ctx;
    private List<Object> list;
    public static final int ITEM_VIEW_TYPE_DATA = 0;
    public static final int ITEM_VIEW_TYPE_SPACE = 1;
    public static final int ITEM_VIEW_TYPE_COUNT = 2;
    public SetListViewAdapter(Context ctx, List<Object> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 类型
     * @param position position
     * @return 类型NO
     */
    @Override
    public int getItemViewType(int position) {
        return list.get(position) instanceof SetListViewItem ? ITEM_VIEW_TYPE_DATA : ITEM_VIEW_TYPE_SPACE;
    }

    /**
     * 获取类型数量
     * @return  类型数量
     */
    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_DATA:
                ViewHolder holder = null;
                if(convertView == null){
//            convertView为空的时候实例化view
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.set_item, parent, false);
                    holder = new ViewHolder();

                    holder.img1 = convertView.findViewById(R.id.img1);
                    holder.text1 = convertView.findViewById(R.id.text1);
                    holder.text2 = convertView.findViewById(R.id.text2);

                    convertView.setTag(holder);
                }
                else{
                    holder = (ViewHolder)convertView.getTag();
                }
                Log.e("999","99-------"+position);
                Log.e("999","99-------"+list.get(position));
                SetListViewItem content = (SetListViewItem) list.get(position);
                holder.img1.setImageResource(content.getImg());
                holder.text1.setText(content.getText());
                holder.text2.setText(content.getText2());
                break;
            case ITEM_VIEW_TYPE_SPACE:
                ViewHolder2 holder2 = null;
                if(convertView == null){
//            convertView为空的时候实例化view
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.item_listview_space, parent, false);
                    holder2 = new ViewHolder2();
                    holder2.text1 = convertView.findViewById(R.id.text1);
                    convertView.setTag(holder2);
                }
                else{
                    holder2 = (ViewHolder2)convertView.getTag();
                }
                SimpleItem content2 = (SimpleItem) list.get(position);
                holder2.text1.setText(content2.getText());
                break;
//                不能加default加了报错还不知道原因
        }
        return convertView;
    }

     class ViewHolder{
        ImageView img1;
        TextView text1;
        TextView text2;
    }

    class ViewHolder2{
        TextView text1;

    }
}


