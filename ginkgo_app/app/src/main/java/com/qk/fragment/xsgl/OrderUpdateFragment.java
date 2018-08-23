package com.qk.fragment.xsgl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.Constant;
import com.qk.R;
import com.qk.activity.IndexActivity;
import com.qk.activity.xsgl.OrderInfoActivity;
import com.qk.adapter.ListViewAdapter;
import com.qk.module.Order;
import com.qk.util.DataUtils;
import com.qk.util.RefreshLayout;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 订单修改
 */
public class OrderUpdateFragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_CODE = 1;
    private ImageView left_back;
    private String token;
    private Handler handler;
    Context ctx;

    private BaseAdapter adapter;
    private List<Object> list=new ArrayList<>();

    private RefreshLayout  refresh;
    private Button search;
    private ListView listView;
    private AVLoadingIndicatorView avi;

    private SearchOrderUpdateDialogFragment fragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_update, container, false);

        avi = view.findViewById(R.id.avi);
        left_back = view.findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        //控件隐藏
        avi.setVisibility(View.GONE);
        handler = new Handler(Looper.getMainLooper());
        listView = view.findViewById(R.id.listview);
        refresh = view.findViewById(R.id.swipe_layout);
        //设置下拉不刷新
        refresh.setEnabled(false);

        token = DataUtils.getLocalData(ctx, "token", "");
        Log.e("token", token);

        search = view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment= new SearchOrderUpdateDialogFragment();
                fragment.setTargetFragment(OrderUpdateFragment.this, REQUEST_CODE);
                fragment.show(getFragmentManager(), "search");
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            final String ddh = data.getStringExtra(SearchOrderRetrievalDialogFragment.DDH );
            final String ywms = data.getStringExtra(SearchOrderRetrievalDialogFragment.YWMS );
            final String jxscode = data.getStringExtra(SearchOrderRetrievalDialogFragment.JXSCODE );
            fragment.dismiss();

            Log.v("search", "开始调接口");
            avi.show();
            listView.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    list.clear();
                    OkGo.<String>post(Constant.ORDER_APPORVAL)
                            .headers("Authorization", "Bearer " + token)
                            .tag(1)
                            .params("dd.ddh", "")
                            .params("jxs.jxscode", "")
/*                            .params("ywms", ywms)
                            .params(" jxs.jxscode", jxscode)*/
                            .execute(new StringCallback() {

                                @Override
                                public void onSuccess(Response<String> response)  {
                                    listView.setVisibility(View.VISIBLE);
                                    try {
                                        //这个就是返回来的结果
                                        String data = response.body();
                                        Gson gson=new Gson();
                                        JsonParser parser = new JsonParser();
                                        JsonArray jsonArray = parser.parse(data).getAsJsonArray();

                                        // JsonSrray转成对象
                                     //   ArrayList<Order> orders = new ArrayList<>();
                                        int i=0;
                                        for(JsonElement obj : jsonArray ){
                                            Order order = gson.fromJson( obj , Order.class);
                                      //      orders.add(order);
                                            i++;
                                            order.setNumber(i);
                                            list.add(order);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    adapter = new ListViewAdapter(ctx, list);
                                    listView.setAdapter(adapter);
                                    Toast.makeText(ctx, "总共"+list.size()+"条", Toast.LENGTH_SHORT).show();
                                    avi.hide();
                                    // 设置下拉刷新监听器
                                    refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                        }
                                    });
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString(SearchOrderRetrievalDialogFragment.DDH,((Order)list.get(position)).getDdh());
                                            bundle.putString(SearchOrderRetrievalDialogFragment.YWMS,((Order)list.get(position)).getYwms());
                                            bundle.putString(SearchOrderRetrievalDialogFragment.JXSCODE,((Order)list.get(position)).getJxscode());
                                            Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                            // .params("ddh",ddh)
                                            //                            .params("ywms",ywms)
                                            //                            .params(" jxs.jxscode",jxdm)
                                        }
                                    });
                                }
                                @Override
                                public void onError(Response<String> response) {
                                    Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }, 100);

            Log.v("search","结束");

 /*           // 加载监听器
            refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
                @Override
                public void onLoad() {
                    Toast.makeText(ctx, "load", Toast.LENGTH_SHORT).show();
                    refresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InForm gridviewentity = new InForm();
                            gridviewentity.setImg(R.mipmap.gengduo_huise);
                            gridviewentity.setTitle("1.暂无数据");
                            gridviewentity.setMessage("1.暂无数据");
                            gridviewentity.setInform("1.暂无数据");
                            gridviewentity.setTime("6月22日");
                            list.add(gridviewentity);
                            adapter.notifyDataSetChanged();
                            // 加载完后调用该方法
                            refresh.setLoading(false);
                        }
                    }, 1500);

                }
            });
*/


        }
    }
    @Override
    public void onDestroy() {
        Log.v("lifec","111onStop");
        IndexActivity parentActivity = (IndexActivity ) getActivity();
        parentActivity.finish();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_back:
                IndexActivity parentActivity = (IndexActivity ) getActivity();
                parentActivity.finish();
                break;
            default:break;
        }
    }
}

