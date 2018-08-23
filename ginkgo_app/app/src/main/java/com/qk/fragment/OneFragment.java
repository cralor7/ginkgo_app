package com.qk.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;
import com.qk.activity.QmuiActivity;
import com.qk.adapter.MyRecyclerViewAdapter;
import com.qk.module.SetListViewItem;
import com.qk.nfc.NFC;
import com.qk.util.RecycleViewLisitenter;
import com.qk.activity.CameraActivity;
import com.qk.activity.PhotoAlbumActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * OneFragment
 */
public class OneFragment extends Fragment  {

    private SwipeRefreshLayout refresh;
    private View view;
    private int i = 0;
    private Context ctx;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;
    private List<SetListViewItem> list = new ArrayList<SetListViewItem>();
    private int img[]={R.mipmap.xiangmu_lanse,R.mipmap.gengduo_lanse,R.mipmap.wo_lanse,R.mipmap.tongzhi_lanse,R.mipmap.xiangmu_huise,R.mipmap.gengduo_lanse
            ,R.mipmap.wo_huise,R.mipmap.tongzhi_huise,R.mipmap.photoalbum,R.mipmap.camera,
            R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,
            R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,
            R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,
            R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,
            R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,
            R.mipmap.yaogunge,R.mipmap.oumeige};



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_five, null);

        for (  ;i < 10; i++) {
            if(i<7){
                SetListViewItem item = new SetListViewItem();
                item.setImg(img[i]);
                item.setText("功能"+i);
                list.add(item);
            }else{
                if(i==7){
                    SetListViewItem item = new SetListViewItem();
                    item.setImg(img[i]);
                    item.setText("NFC");
                    list.add(item);
                }
                if(i==8){
                    SetListViewItem item = new SetListViewItem();
                    item.setImg(img[i]);
                    item.setText("相册");
                    list.add(item);
                }
                if(i==9){
                    SetListViewItem item = new SetListViewItem();
                    item.setImg(img[i]);
                    item.setText("照相机");
                    list.add(item);
                }

            }


        }


        mRecyclerView =   view.findViewById(R.id.recyclerView);
//      设置RecyclerView管理器 设置垂直显示还是横向显示
//      mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));
//      设置网格布局
        mRecyclerView.setLayoutManager(new GridLayoutManager(ctx, 4));
//      初始化适配器
        mAdapter = new MyRecyclerViewAdapter(ctx,list);
//      设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        /**
         * 瀑布流
         */
       /* StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(manager);
        HomeAdapter adapter = new HomeAdapter(this,mList);*/

        /**
         * 设置自定义的条目点击事件
         */
        mAdapter.setOnItemClickLisitenter(new RecycleViewLisitenter.onItemClickLisitenter() {
            @Override
            public void onItemClick(View v, int position) {

                TextView text = (TextView)v.findViewById(R.id.MyText) ;

                Toast.makeText(ctx,list.get(position).getText()+"",Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 设置自定义的条目长按监听
         */
        mAdapter.setOnItemLongClickLisitenter(new RecycleViewLisitenter.onItemLongClickLisitenter() {
            @Override
            public void onItemLongClick(View v, int position) {
                goToActivity(position);

            }
        });

//      设置适配器
        mRecyclerView.setAdapter(mAdapter);
        initView();
        return view;


    }

    private void initView() {


    }

    private void goToActivity(int position){
        switch (list.get(position).getText()){
            case "NFC":
                Intent intent3 = new Intent(getActivity(),NFC.class);
                startActivity(intent3);
                break;
            case "相册":
                Intent intent = new Intent(getActivity(),PhotoAlbumActivity.class);
                startActivity(intent);
            break;
            case "照相机":
                Intent intent2 = new Intent(getActivity(),CameraActivity.class);
                startActivity(intent2);
                break;
            case "功能1":
                Intent intent4 = new Intent(getActivity(),QmuiActivity.class);
                startActivity(intent4);
                break;
            default:
                Toast.makeText(ctx,position+"long",Toast.LENGTH_SHORT).show();
                break;

        }

    }

}