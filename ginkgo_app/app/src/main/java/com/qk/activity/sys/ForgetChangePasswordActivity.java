package com.qk.activity.sys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.R;
import com.qk.util.DataUtils;
import com.qk.util.EditTextUtils;
import com.qk.util.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 忘记密码后重新修改密码Activity
 */
public class ForgetChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 存放本地存储的token
     */
    private String token;
    /**
     * 线程
     */
    private Handler handler;
    Context ctx;
    private EditText newPwd;
    private EditText confirmPwd;
    private Button submit;
    private Button cancel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_forget_change_password;
    }

    @Override
    public void initView() {
        //修改引入的通用头部的标题
        ViewUtils.topLayout(this, "修改密码");
        newPwd = findViewById(R.id.edit_new_password);
        confirmPwd = findViewById(R.id.edit_confirm_password);
        submit = findViewById(R.id.btn_submit);
        cancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.progress_edit);
        submit.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取本地存储的token和username
        token = DataUtils.getLocalData(this, "token", "token");
        ctx = this;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                String newPwdStr = EditTextUtils.getString(newPwd);
                String confirmPwdStr = EditTextUtils.getString(confirmPwd);
                submit(newPwdStr, confirmPwdStr);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 修改密码
     */
    private void submit(final String newPwd, final String confirmPwd) {

        if(TextUtils.isEmpty(newPwd)){
            Toast.makeText(this, Constant.EMPTY_NEW_PASSWORD,Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(confirmPwd)){
            Toast.makeText(this, Constant.EMPTY_CONFIRM_PASSWORD,Toast.LENGTH_SHORT).show();
            return;
        }
        if(!newPwd.equals(confirmPwd)){
            Toast.makeText(this, Constant.DIFFER_CONFIRM_PASSWORD,Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.CHANGE_PASSWORD)
                        .tag(1)
                        .headers("Authorization", "Bearer "+token)
                        .params("newPwd", newPwd)
                        .params("confirmPwd", confirmPwd)
                        .execute(new StringCallback() {
                            @Override
                            public void onStart(Request<String, ? extends Request> request) {
                                super.onStart(request);
                                //显示进度条
                                //progressBar.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                //隐藏进度条
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onSuccess(Response<String> response) {
                                String data = response.body();
                                String code = "";
                                String message = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    code = jsonObject.get("code").toString();
                                    message = jsonObject.get("message").toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //判断是否修改成功 如果code是success code则成功，否则失败
                                if(Constant.SUCCESS_CODE.equals(code)){
                                    //保存新的密码到本地
                                    DataUtils.updateLocalData(ctx,"password", newPwd);
                                    Toast.makeText(ctx, "修改成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //重新登录
                                    Intent intent2 = new Intent(ctx, LoginActivity.class);
                                    startActivity(intent2);
                                }else{
                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(Response<String> response) {
                                Toast.makeText(ctx, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }, 1000);

    }
}
