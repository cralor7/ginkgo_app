package com.qk.fragment.xsgl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qk.Constant;
import com.qk.GApp;
import com.qk.R;
import com.qk.activity.IndexActivity;
import com.qk.activity.xsgl.OrderInfoActivity;
import com.qk.adapter.ListViewAdapter;
import com.qk.module.Order;
import com.qk.util.DataUtils;
import com.qk.util.RefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 订单检索
 */
public class OrderRetrievalFragment extends Fragment implements View.OnClickListener {
    public static final int REQUEST_CODE = 1;
    private ImageView imvLeftBack;
    private String token;
    private Handler handler;
    Context ctx;
    private ListViewAdapter adapter;
    private List<Object> list = new ArrayList<>();
    private RefreshLayout refresh;
    private Button search;
    private PullToRefreshListView listView;
//    private AVLoadingIndicatorView avi;
    private int pageNo = 1;
    private int pageSize = 10;
    private String listddh,listywms,listjxscode;
    private SearchOrderRetrievalDialogFragment fragment;
    private  int listNum = 0;

    private QMUITipDialog tipDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_order_retrieval, container, false);
//        avi = view.findViewById(R.id.avi);

        imvLeftBack = view.findViewById(R.id.left_back);
        imvLeftBack.setOnClickListener(this);
        //控件隐藏
//        avi.setVisibility(View.GONE);
        handler = new Handler(Looper.getMainLooper());
        listView = view.findViewById(R.id.listview);
        refresh = view.findViewById(R.id.swipe_layout);
        //设置下拉不刷新
        refresh.setEnabled(false);
        token = DataUtils.getLocalData(ctx, "token", "");
        pageSize = Integer.parseInt(GApp.pageNumber);
        Log.e("token", token);
        //设置当前上拉加载不可用
        //只允许上拉
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        //只允许下拉
//      listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //允许下拉上拉
//      listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(false, true).setPullLabel("正在加载...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel("正在加载...");

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(pageNo, pageSize, listddh, listywms, listjxscode);
            }
        });
        search= view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SearchOrderRetrievalDialogFragment();
                fragment.setTargetFragment(OrderRetrievalFragment.this, REQUEST_CODE);
                assert getFragmentManager() != null;
                fragment.show(getFragmentManager(), "search");
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10086 && resultCode == RESULT_OK){
            String msg = data.getStringExtra("msgB");
            int positionResult = Integer.parseInt(data.getStringExtra("position"));
    /*        Order order = (Order)list.get(position_result);*/
            list.remove(positionResult);
            adapter.notifyDataSetChanged();
            //  adapter.updataView(position_result, listView,1);//动态修改
            return ;
        }
        if (requestCode == 10086 && resultCode == 10){
            String msg = data.getStringExtra("msgB");
            int positionResult = Integer.parseInt(data.getStringExtra("position"));
            //动态修改
//            adapter.updataView(positionResult, listView, 2);
            return ;
        }
        if (requestCode == REQUEST_CODE) {
            final String ddh = data.getStringExtra(SearchOrderRetrievalDialogFragment.DDH );
            final String ywms = data.getStringExtra(SearchOrderRetrievalDialogFragment.YWMS );
            final String jxscode = data.getStringExtra(SearchOrderRetrievalDialogFragment.JXSCODE );
//          search.setHint(ddh+"/"+ywms+"/"+jxscode);
            listddh = ddh;
            listywms = ywms;
            listjxscode = jxscode;
            fragment.dismiss();
            Log.v("search", "开始调接口");
//            avi.show();
            listView.setVisibility(View.GONE);
            list.clear();
            pageNo = 1;
            listNum = 0;
            getData(pageNo, pageSize, ddh, ywms, jxscode);
            Log.v("search", "结束");
        }
    }
    @Override
    public void onDestroy() {
        IndexActivity parentActivity = (IndexActivity ) getActivity();
        assert parentActivity != null;
        parentActivity.finish();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_back:
                IndexActivity parentActivity = (IndexActivity ) getActivity();
                assert parentActivity != null;
                parentActivity.finish();
                break;
            default:break;
        }
    }

    /**
     *  pageNo == 1 , 首次加载数据，需要初始化listView  adapter
     *  之后下拉加载，刷新adapter listView
     *
     * @param no 当前页数
     * @param size 每页数据条数
     * @param ddh 订单号
     * @param ywms 业务模式
     * @param jxscode 经销代码
     */
    public void getData(final int no, final int size, final String ddh, final String ywms, final String jxscode){
        if (pageNo == 1) {
            getDataInit();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.ORDER_SEARCH)
                        .headers("Authorization", "Bearer " + token)
                        .tag(1)
                        .params("page.pageNo", no)
                        .params("page.pageSize", size)
                     /*   .params("ddh", ddh)
                        .params("ywms", ywms)
                        .params(" jxs.jxscode", jxscode)*/
                        .execute(new StringCallback() {
                            @Override
                            public void onStart(Request<String, ? extends Request> request) {
                                super.onStart(request);
                            }
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                if (tipDialog.isShowing()) {
                                    tipDialog.dismiss();
//                                avi.hide();
                                }
                            }
                            @Override
                            public void onSuccess(Response<String> response)  {
                                listView.setVisibility(View.VISIBLE);
                                try {
                                    //这个就是返回来的结果
                                    String data = response.body();
                                    Gson gson = new Gson();
                                    JsonParser parser = new JsonParser();
                                    JsonArray jsonArray = parser.parse(data).getAsJsonArray();
                                    for(JsonElement obj : jsonArray ){
                                        Order order = gson.fromJson( obj , Order.class);
                                        listNum++;
                                        order.setNumber(listNum);
                                        list.add(order);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                adapter.notifyDataSetChanged();
                                listView.onRefreshComplete();
                                pageNo++;
                                Toast.makeText(ctx, "总共"+list.size()+"条", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onError(Response<String> response) {
                                    Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }, 100);
    }

    /**
     * 初始化listView  adapter
     */
    public void getDataInit(){
        tipDialog = new QMUITipDialog.Builder(ctx)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        adapter = new ListViewAdapter(ctx, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position下标从1开始
                position--;
                Bundle bundle = new Bundle();
                bundle.putString(SearchOrderRetrievalDialogFragment.DDH, ((Order) list.get(position)).getDdh());
                bundle.putString(SearchOrderRetrievalDialogFragment.YWMS, ((Order) list.get(position)).getYwms());
                bundle.putString(SearchOrderRetrievalDialogFragment.JXSCODE, ((Order) list.get(position)).getJxscode());
                bundle.putString("position", position + "");
                Intent intent = new Intent(getActivity(), OrderInfoActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10086);
            }
        });
    }
}

