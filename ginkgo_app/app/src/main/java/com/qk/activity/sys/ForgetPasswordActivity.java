package com.qk.activity.sys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.R;
import com.qk.util.DataUtils;
import com.qk.util.ViewUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 忘记密码Activity
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private String CODE = "";

    private String token;
    private String username;
    private Handler handler;
    private Context ctx;
    private CountDownTimer timer;
    private EditText phoneNumber;
    private EditText checkCode;
    private Button btnSubmit;
    private Button btnCancel;
    private Button btnCheckCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    public void initView() {
        //修改引入的通用头部的标题
        ViewUtils.topLayout(this, "忘记密码");
        btnSubmit = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.progress_edit);
        phoneNumber = findViewById(R.id.get_phone);
        checkCode = findViewById(R.id.checkcode_text);
        btnCheckCode = findViewById(R.id.checkcode_btn);
        btnCheckCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取本地存储的token和username
        token = DataUtils.getLocalData(this, "token", "token");
        username = DataUtils.getLocalData(this, "username");
        ctx = this;
        handler = new Handler(Looper.getMainLooper());
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnCheckCode.setText(millisUntilFinished/1000 + "");
            }
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFinish() {
                btnCheckCode.setText("发送验证码");
                btnCheckCode.setEnabled(true);
            }
        };
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(final View v) {
        final QMUITipDialog tipDialog;
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.checkcode_btn:
                final String phone =phoneNumber.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(this, "请先输入注册手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(phone.length() != 11){
                    Toast.makeText(this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                v.setEnabled(false);
                ((TextView) v).setTextColor(R.color.withe);
                tipDialog = new QMUITipDialog.Builder(ctx)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("正在发送")
                        .create();
                tipDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OkGo.<String>post(Constant.GET_CODE)
                                //强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                                .tag(1)
                                .params("loginName",phone)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        try {
                                            String data = response.body();
                                            JSONObject jsonObject=new JSONObject(data);
                                            CODE = jsonObject.get("code").toString();
                                            Toast.makeText(ctx, "CODE---"+CODE, Toast.LENGTH_SHORT).show();
                                            tipDialog.dismiss();
                                            final QMUITipDialog tipDialog2;
                                            tipDialog2 = new QMUITipDialog.Builder(ctx)
                                                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                                                        .setTipWord("发送成功")
                                                        .create();
                                            tipDialog2.show();
                                            btnCheckCode.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    tipDialog2.dismiss();
                                                }
                                            }, 1000);
                                            start(v);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onError(Response<String> response) {
                                        btnCheckCode.setEnabled(true);
                                        tipDialog.dismiss();
                                        final QMUITipDialog tipDialog2;
                                        tipDialog2 = new QMUITipDialog.Builder(ctx)
                                                .setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                                                .setTipWord("获取失败")
                                                .create();
                                        tipDialog2.show();
                                        btnCheckCode.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                tipDialog2.dismiss();
                                            }
                                        }, 1000);
                                    }
                                });
                    }
                }, 1000);
                break;

            default:break;
        }
    }

    /**
     * 修改密码
     */
    private void submit() {
        Toast.makeText(ctx, "Code---"+CODE, Toast.LENGTH_SHORT).show();
        if(TextUtils.isEmpty(CODE)){
            Toast.makeText(this, "请先输入注册手机号获取验证码！", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(checkCode.getText().toString())){
            Toast.makeText(this, "请输入获取的验证码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if(checkCode.getText().toString().length() == 0){
            Toast.makeText(ctx, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return ;
        }
        if(CODE.equals(checkCode.getText().toString())){
            finish();
            Intent intent2 = new Intent(ctx, ForgetChangePasswordActivity.class);
            startActivity(intent2);
        }else{
            Toast.makeText(ctx, "请输入正确的验证码！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击开始按钮
     * @param v view
     */
    @SuppressLint("ResourceAsColor")
    public void start(View v){
        timer.start();

    }

    /**
     * 点击结束按钮
     * @param v view
     */
    @SuppressLint("ResourceAsColor")
    public void cancel(View v){
        timer.cancel();

    }



}
