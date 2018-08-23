package com.qk.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.qk.GApp;
import com.qk.R;
import com.qk.module.PhotoItem;
import com.qk.util.BitmapUtil;
import com.qk.util.RecycleViewLisitenter;

import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 显示图片 dapter
 */
public class PhotoShowAdapter extends RecyclerView.Adapter<PhotoShowAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    Context context;
    private List<PhotoItem> list;
    private Boolean showorhide;
    private RecycleViewLisitenter.onItemClickLisitenter onItem;
    private RecycleViewLisitenter.onItemLongClickLisitenter onLongItem;

    public PhotoShowAdapter(Context context, List<PhotoItem> list) {
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
    public PhotoShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        PhotoShowAdapter.ViewHolder viewHolder = new PhotoShowAdapter.ViewHolder(view);
        return viewHolder;
    }
    /**
     * 绑定对象
     * @param holder holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        Bitmap bitmap = BitmapUtil.getSmallBitmap(list.get(position).getImg());
        holder.myImage.setImageBitmap(bitmap);
        holder.mText.setText("");
        if(GApp.SHOWORHIDE){
            holder.checkBox.setVisibility(View.VISIBLE);
        }else{
            holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setChecked(false);
        }
    }
    /**
     * 获取总数量
     * @return 总数量
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
        if(onLongItem!=null){
            onLongItem.onItemLongClick(v, (Integer) v.getTag());
        }
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myImage;
        TextView mText;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            myImage = itemView.findViewById(R.id.MyImageView);
            mText = itemView.findViewById(R.id.MyText);
            checkBox = itemView.findViewById(R.id.check_photo);
        }
    }
}
