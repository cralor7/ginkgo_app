package com.qk.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qk.R;
import com.qk.module.SetListViewItem;
import com.qk.util.RecycleViewLisitenter;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * RecyclerView Adapter
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    Context context;
    private List<SetListViewItem> list;
    private RecycleViewLisitenter.onItemClickLisitenter onItem;
    private RecycleViewLisitenter.onItemLongClickLisitenter onLongItem;

    public MyRecyclerViewAdapter(Context context, List<SetListViewItem> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnItemLongClickLisitenter(RecycleViewLisitenter.onItemLongClickLisitenter onLongItem){
        this.onLongItem = onLongItem;
    }
    public void setOnItemClickLisitenter(RecycleViewLisitenter.onItemClickLisitenter onItem){
        this.onItem = onItem;
    }

    /**
     * 获取对象
     * @param parent parent
     * @param viewType viewType
     * @return viewHolder
     */
    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nomal, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        MyRecyclerViewAdapter.ViewHolder viewHolder = new MyRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    /**
     * 绑定对象
      * @param holder holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        /** 三目运算如果没有照片就用默认的，如果有照片则用获取的*/
        holder.myImage.setImageResource(list.get(position).getImg() == 0 ? R.mipmap.ddcx : list.get(position).getImg());
        holder.mText.setText(list.get(position).getText());
    }
    /**
     * 获取总数量
     * @return 数量
     */
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public void onClick(View v) {
        if(onItem != null){
            onItem.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(onLongItem != null){
            onLongItem.onItemLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myImage;
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.MyImageView);
            mText = itemView.findViewById(R.id.MyText);
        }
    }
}
