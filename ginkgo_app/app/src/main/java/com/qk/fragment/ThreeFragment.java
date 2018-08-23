package com.qk.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.qk.Constant;
import com.qk.R;
import com.qk.adapter.ListViewAdapter;
import com.qk.module.InForm;
import com.qk.module.Stock;
import com.qk.util.JsonCallback;
import com.qk.util.RefreshLayout;
import com.qk.activity.StockInfoActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * ThreeFragment
 */
public class ThreeFragment extends Fragment implements View.OnClickListener,SearchView.OnQueryTextListener {
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    private String token;
    private Handler handler;
    private RefreshLayout  refresh;
    private Context ctx;
    private ListView listView;
    private BaseAdapter adapter;
    private List<Object> list = new ArrayList<>();
    private List<InForm> informlist = new ArrayList<>();
    private View  view;
    private SearchView searchview;
    private Boolean scrollFlag = false;
    private AVLoadingIndicatorView avi;
    private int img[] = {R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx=context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.refresh, null, false);
        initview();
        return view;
    }
//      初始化控件
        public void initview(){
            avi = view.findViewById(R.id.avi);
            //控件隐藏
            avi.setVisibility(View.GONE);
            handler = new Handler(Looper.getMainLooper());
            listView = view.findViewById(R.id.listview);
            searchview = view.findViewById(R.id.searchview);
            SetSeachView(searchview);
            refresh = view.findViewById(R.id.swipe_layout);
            sharedPreferences=ctx.getSharedPreferences("token",MODE_PRIVATE);
//          获取token
            token=sharedPreferences.getString("token","token");
            sharedPreferences2 = ctx.getSharedPreferences("username",MODE_PRIVATE);
            Log.e("token",token);
            //设置下拉刷新的箭头颜色
            refresh.setColorSchemeResources(android.R.color.holo_red_light);
            //设置下拉刷新的背景颜色为白色
            refresh.setProgressBackgroundColorSchemeResource(android.R.color.white);
            //设置下拉不刷新
            refresh.setEnabled(false);
            searchview.setOnSearchClickListener(this);
            searchview.setOnQueryTextListener(this);
        }

        public void initListView(final String searchtext){
            startAnim();
            listView.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    list.clear();

                    OkGo.<Stock>post(Constant.STOCK)
                            .headers("Authorization", "Bearer " + token)
                            .tag(1)
                            .params("stockcode", searchtext)
                            .execute(new JsonCallback<Stock>(Stock.class) {
                                @Override
                                public void onStart(Request<Stock, ? extends Request> request) {
                                    super.onStart(request);
                                    //显示进度条
                                    //    progressBar.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onSuccess(Response<Stock> response) {
                                    listView.setVisibility(View.VISIBLE);
                                    Stock   l = response.body();
                                    List<Stock.Data> data =l.getData();
                                    for ( Stock.Data getdata: data) {
                                        InForm gridviewentity = new InForm();
                                        gridviewentity.setImg(R.mipmap.gengduo_huise);
                                        gridviewentity.setTitle(getdata.getStockname());
                                        gridviewentity.setMessage(getdata.getStockcode());
                                        gridviewentity.setInform(getdata.getOffice().getName());
                                        gridviewentity.setTime("");
                                        list.add(gridviewentity);
                                        informlist.add(gridviewentity);
                                    }
                                    adapter = new ListViewAdapter(ctx, list);
                                    listView.setAdapter(adapter);
                                    Toast.makeText(ctx, "总共"+list.size()+"条", Toast.LENGTH_SHORT).show();
                                    stopAnim();
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
                                            bundle.putString("stock_name",informlist.get(position).getMessage().toString());
                                            Intent intent = new Intent(getActivity(),StockInfoActivity.class);
                                            intent.putExtras(bundle);
                                            searchview.clearFocus();
                                            startActivity(intent);
                                        }
                                    });
                                }
                                @Override
                                public void onError(Response<Stock> response) {
                                    Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }, 1000);

            // 加载监听器
            refresh.setOnLoadListener(new RefreshLayout.OnLoadListener() {
                @Override
                public void onLoad() {
                    Toast.makeText(ctx, "load", Toast.LENGTH_SHORT).show();
                    refresh.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InForm gridviewentity = new InForm();
                            gridviewentity.setImg(img[1]);
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
        }

        public void SetSeachView(SearchView mSearchView){
//            mSearchView.clearFocus();// sv_wddb是SearchView控件
            mSearchView.setIconifiedByDefault(true);
            /*
             * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
             * true)来添加一个提交按钮（"submit" button)
             * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
             */
            mSearchView.setSubmitButtonEnabled(true);
            /*
             * 初始是否已经是展开的状态
             * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
             */
          //  mSearchView.onActionViewExpanded();
            // 设置search view的背景色
            /*
             * 默认情况下, search widget是"iconified“的，只是用一个图标 来表示它(一个放大镜),
             * 当用户按下它的时候才显示search box . 你可以调用setIconifiedByDefault(false)让search
             * box默认都被显示。 你也可以调用setIconified()让它以iconified“的形式显示。
             */
            mSearchView.setIconifiedByDefault(false);
            /*
             * 设置searchview提示字
             * */
            mSearchView.setQueryHint("请输入仓库编码");
        }

    @Override
    public void onClick(View v) {

    }


    /**
     * 监听searchview 点击搜索的方法
     * */
    @Override
    public boolean onQueryTextSubmit(String query) {
       Log.e("999999","dianjile");
        initListView(query);
        return false;
    }

    /**
     * 监听searchview 字体改变的方法
     * */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }
}
