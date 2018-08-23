package com.qk.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.qk.R;
import com.qk.adapter.ListViewAdapter;
import com.qk.module.InForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * TwoFragment
 */
public class TwoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refresh;
    private Context context;
    private ListView listView;
    private BaseAdapter adapter;
    private List<Object> list=new ArrayList<>();
    private View  view;
    private int img[] = {R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_two, null,false);
        listView = view.findViewById(R.id.listview);
        refresh = view.findViewById(R.id.refresh);
        //设置下拉刷新的箭头颜色
        refresh.setColorSchemeResources(android.R.color.holo_red_light);
        //设置下拉刷新的背景颜色为白色
        refresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refresh.setOnRefreshListener(this);
        initListView();
        return view;
    }

    /**
     * 添加内容
     * @param
     */
    public void initListView(){

        for(int i = 0; i < 15; i++) {
            InForm gridviewentity = new InForm();
            gridviewentity.setImg(img[i]);
            gridviewentity.setTitle("1.暂无数据");
            gridviewentity.setMessage("1.暂无数据");
            gridviewentity.setInform("1.暂无数据");
            gridviewentity.setTime("6月22日");
            list.add(gridviewentity);
        }
        adapter = new ListViewAdapter(this.context, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "点击了列表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        Toast.makeText(context, "下拉刷新成功", Toast.LENGTH_SHORT).show();
        //如果正在刷新
        if (refresh.isRefreshing()) {
            //取消刷新
            refresh.setRefreshing(false);
        }
    }
}
