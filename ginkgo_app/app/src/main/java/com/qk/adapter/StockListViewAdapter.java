package com.qk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qk.R;
import com.qk.module.InForm;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * StockListViewAdapter
 */
public class StockListViewAdapter extends BaseAdapter {
    private Context ctx;
    private List<Object> list;

    public StockListViewAdapter(Context ctx, List<Object> list) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if(convertView == null){
//                   convertView为空的时候实例化view
                    convertView= LayoutInflater.from(ctx).inflate(R.layout.stock_listview_item, parent, false);
                    holder=new ViewHolder();
                    holder.text1 = convertView.findViewById(R.id.text1);
                    holder.text2 = convertView.findViewById(R.id.text2);
                    convertView.setTag(holder);
                }
                else{
                    holder = (ViewHolder)convertView.getTag();
                }
                InForm content = (InForm) list.get(position);
                holder.text1.setText(content.getTitle());
                holder.text2.setText(content.getMessage());
        return convertView;
    }

     class ViewHolder{
        TextView text1;
        TextView text2;
    }

    class ViewHolder2{
        TextView text1;
    }
}


