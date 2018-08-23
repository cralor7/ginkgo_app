package com.qk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qk.R;
import com.qk.module.InForm;
import com.qk.module.Order;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * ListView Adapter
 */
public class ListViewAdapter extends BaseAdapter {
   private Context ctx;
    private List<Object> list;
    private static final int ITEM_VIEW_TYPE_DATA = 0;
    private static final int ITEM_VIEW_TYPE_ORDER = 1;
    private static final int ITEM_VIEW_TYPE_SPACE = 2;
    private static final int ITEM_VIEW_TYPE_COUNT = 3;
    public ListViewAdapter(Context ctx, List<Object> list) {
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
     *  类型
     */
    @Override
    public int getItemViewType(int position) {
      //  return list.get(position) instanceof InForm ? ITEM_VIEW_TYPE_DATA : ITEM_VIEW_TYPE_SPACE;
        if( list.get(position) instanceof InForm){
            return ITEM_VIEW_TYPE_DATA;
        }else if( list.get(position) instanceof Order){
            return ITEM_VIEW_TYPE_ORDER;
        }else{
            return ITEM_VIEW_TYPE_SPACE;
        }
    }
    /**
     * 获取类型数
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
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.informlistview, parent, false);
                    holder = new ViewHolder();
                    holder.img1 = convertView.findViewById(R.id.img);
                    holder.text1 = convertView.findViewById(R.id.title);
                    holder.text2 = convertView.findViewById(R.id.message);
                    holder.text3 = convertView.findViewById(R.id.inform);
                    holder.text4 = convertView.findViewById(R.id.time);
                    convertView.setTag(holder);
                }
                else{
                    holder = (ViewHolder)convertView.getTag();
                }
                InForm content = (InForm) list.get(position);
                holder.img1.setImageResource(content.getImg());
                holder.text1.setText(content.getTitle());
                holder.text2.setText(content.getMessage());
                holder.text3.setText(content.getInform());
                holder.text4.setText(content.getTime());
                break;
            case ITEM_VIEW_TYPE_ORDER:
                ViewHolder2 holder2 = null;

                if(convertView == null){
//            convertView为空的时候实例化view
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.item_listview_order, parent, false);
                    holder2 = new ViewHolder2();
                    holder2.num = convertView.findViewById(R.id.num);
                    holder2.text1 = convertView.findViewById(R.id.list_ddh);
                    holder2.text2 = convertView.findViewById(R.id.list_ywms);
                    holder2.text3 = convertView.findViewById(R.id.list_jxscode);
                    convertView.setTag(holder2);
                }
                else{
                    holder2 = (ViewHolder2)convertView.getTag();
                }
                Order order = (Order) list.get(position);
                holder2.num.setText(order.getNumber() + "");
                holder2.text1.setText(order.getDdh());
                holder2.text2.setText(order.getYwms());
                holder2.text3.setText(order.getJxscode());
                break;
            case ITEM_VIEW_TYPE_SPACE:
                if (convertView == null) {
                    convertView = LayoutInflater.from(ctx).inflate(R.layout.item_listview_space, parent, false);
                }else{
                }
                break;
//           不能加default加了报错还不知道原因
        }
        return convertView;
    }

     class ViewHolder{
        ImageView img1;
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
    }

    class ViewHolder2{
        TextView num;
        TextView text1;
        TextView text2;
        TextView text3;
    }

    /**
     * 更新listview指定item
     */
    @SuppressLint("ResourceAsColor")
    public void updataView(int posi, ListView listView, int type) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();

        int visibleLastPosi = listView.getLastVisiblePosition();
        String text = "1";
        if(type == 1){
            text = "-----驳回";
        }else{
            text = "-----通过";
        }
        if (posi >= visibleFirstPosi && posi <= visibleLastPosi) {
            View view = listView.getChildAt(posi - visibleFirstPosi);
            ViewHolder2 holder = (ViewHolder2) view.getTag();
            String num = holder.num.getText().toString();
            String txt1 = holder.text1.getText().toString();
            String txt2 = holder.text2.getText().toString();
            String txt3 = holder.text3.getText().toString();
            Order order = new Order();
            order.setNumber(Integer.parseInt(num));
            order.setDdh(txt1);
            order.setYwms(txt2);
            order.setJxscode(txt3);
            txt2 = txt2 + text;
            holder.text2.setText(txt2);
            holder.text2.setTextColor(R.color.wyRed);
            list.set(posi, order);
        } else {
            Order order = (Order) list.get(posi);
            order.setYwms(order.getYwms() + "1");
            list.set(posi, order);
        }
    }
}


