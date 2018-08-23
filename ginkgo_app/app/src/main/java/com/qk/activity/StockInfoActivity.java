package com.qk.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.activity.sys.LoginActivity;
import com.qk.util.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 仓库详情 Activity
 */
public class StockInfoActivity extends BaseActivity  {


    private TextView tevStockName;
    private TextView tecStockLocation;
    private TextView tevStockOfficeName;
    private TextView tevStockArea;
    private Bundle bundle;
    private Handler handler;
    private String token;
    private String searchStockCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(intiLayout());
        initView();
        initData();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_stock_info;
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        searchStockCode = bundle.getString("stock_code");
        tevStockName = findViewById(R.id.stock_name);
        tecStockLocation = findViewById(R.id.stock_location);
        tevStockOfficeName = findViewById(R.id.stock_office_name);
        tevStockArea = findViewById(R.id.stock_area);
        handler = new Handler(Looper.getMainLooper());

        token = DataUtils.getLocalData(this, "token", "token");
    }

    @Override
    public void initData() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.STOCK)
                        .tag(1)
                        .headers("Authorization", "Bearer " + token)
                        .params("stockcode", searchStockCode)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                /*
                                 * 这个就是返回来的结果
                                 */
                                String data = response.body();
                                String code = "";
                                try {
                                    JSONObject jsonObject=new JSONObject(data);
                                    Log.e("99999999",""+jsonObject.getString("message"));
                                       tevStockName.setText(jsonObject.getString("message"));
                                        tevStockArea.setText(jsonObject.getString("message"));
                                        tecStockLocation.setText(jsonObject.getString("message"));
                                        tevStockOfficeName.setText(jsonObject.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(StockInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StockInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });
            }
        }, 3);
    }
}
