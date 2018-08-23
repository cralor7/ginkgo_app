package com.qk.activity.xsgl;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.R;
import com.qk.fragment.xsgl.SearchOrderRetrievalDialogFragment;
import com.qk.module.Order;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.util.DataUtils;
import com.qk.util.ViewUtils;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 订单详情 Activity
 */
public class OrderInfoActivity extends BaseActivity  implements View.OnClickListener{

    private TextView ddh;
    private TextView jxscode;
    private TextView ywms;
    private TextView ddlb;
    private TextView cccxh;
    private TextView ys;
    private TextView sl;
    private TextView dpprice;
    private TextView djzt;
    private Button btnOrderAgree;
    private Button btnOrderReject;
    private Bundle bundle;
    private Handler handler;
    private String token;

    private String searchDdh;
    private String searchYwms;
    private String searchJxscode;

    private String position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(intiLayout());
        initView();
        initData();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_order_info;
    }

    @Override
    public void initView() {
        //修改引入的通用头部的标题
        ViewUtils.topLayout(this, "订单详情");

        bundle = getIntent().getExtras();
        searchDdh = bundle.getString(SearchOrderRetrievalDialogFragment.DDH);
        searchYwms = bundle.getString(SearchOrderRetrievalDialogFragment.YWMS);
        searchJxscode = bundle.getString(SearchOrderRetrievalDialogFragment.JXSCODE);
        position =bundle.getString("position");
        ddh = findViewById(R.id.order_ddh);
        jxscode = findViewById(R.id.order_jxscode);
        ywms = findViewById(R.id.order_ywms);
        ddlb = findViewById(R.id.order_ddlb);
        cccxh = findViewById(R.id.order_cccxh);
        ys = findViewById(R.id.order_ys);
        sl = findViewById(R.id.order_sl);
        dpprice = findViewById(R.id.order_dpprice);
        djzt = findViewById(R.id.order_djzt);
        btnOrderAgree = findViewById(R.id.order_agree);
        btnOrderReject = findViewById(R.id.order_reject);
        btnOrderAgree.setOnClickListener(this);
        btnOrderReject.setOnClickListener(this);
        handler = new Handler(Looper.getMainLooper());

        token = DataUtils.getLocalData(this, "token", "token");
    }

    @Override
    public void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.ORDER_SEARCH)
                        .tag(1)
                        .headers("Authorization", "Bearer " + token)
                        .params("ddh", searchDdh)
                        //             .params("ywms", "")
                        //             .params("ywms", searchYwms)
                        //              .params("jxs.jxscode", searchJxscode)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                /*
                                 * 这个就是返回来的结果
                                 */
                                try {
                                    String data = response.body();
                                    Log.v("das", data);
                                    Gson gson = new Gson();
                                    JsonParser parser = new JsonParser();
                                    JsonArray jsonArray = parser.parse(data).getAsJsonArray();
                                    if(jsonArray.size() < 1){
                                        return;
                                    }
                                    Order order = gson.fromJson( jsonArray.get(0), Order.class);
                                    ddh.setText(order.getDdh());
                                    jxscode.setText(order.getJxscode());
                                    ywms.setText(order.getYwms());
                                    ddlb.setText(order.getDdlb());
                                    cccxh.setText(order.getCccxh());
                                    ys.setText(order.getYs());
                                    sl.setText(order.getSl());
                                    dpprice.setText(order.getDpprice());
                                    djzt.setText(order.getDjzt());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(OrderInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                //     Intent intent = new Intent(OrderInfoActivity.this, LoginActivity.class);
                                //      startActivity(intent);
                                //     overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });
            }
        }, 3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_agree:
                Toast.makeText(OrderInfoActivity.this, "同意", Toast.LENGTH_SHORT).show();
                Intent data2 = new Intent();
                data2.putExtra("msgB", "哇咔咔");
                data2.putExtra("position", position);
                setResult(10, data2);
                finish();
                break;
            case R.id.order_reject:
                Toast.makeText(OrderInfoActivity.this, "驳回", Toast.LENGTH_SHORT).show();
                Intent data = new Intent();
                data.putExtra("msgB", "哇咔咔");
                data.putExtra("position", position);
                setResult(RESULT_OK, data);
                finish();
                break;
            default:break;
        }
    }
}
