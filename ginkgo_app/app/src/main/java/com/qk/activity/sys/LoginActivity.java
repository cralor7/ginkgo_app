package com.qk.activity.sys;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.GApp;
import com.qk.R;
import com.qk.activity.MainActivity;
import com.qk.activity.sys.ForgetPasswordActivity;
import com.qk.module.Data;
import com.qk.module.Menu;
import com.qk.util.DataUtils;
import com.qk.util.EditTextUtils;
import com.qk.util.JsonCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.qk.view.ClearEditText;

import java.util.ArrayList;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 登录Activity
 */
public class LoginActivity extends BaseActivity implements  View.OnClickListener{

    /** 线程 **/
    private Handler handler;
    /** 用户名 */
    private EditText edtUsername;
    /** 密码 */
    private EditText edtPassword;
    /** 登陆按钮 */
    private Button btnLogin;
    /** 记住密码 */
    private CheckBox ckbRemember;
    /** 注册 */
    private TextView btnRegister;
    /** 忘记密码 */
    private TextView tevForgetPassword;
    /** 进度条 */
    private ProgressBar progressBar;
    /** 显示密码按钮*/
    private CheckBox mCbDisplayPassword;
    protected Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(intiLayout());
        ctx = this;
        initView();
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }
    @Override
    public void initView() {

        //获取本地存储的token和username
        String username = DataUtils.getLocalData(this, "username", "");
        String password = DataUtils.getLocalData(this, "password", "");

        handler = new Handler(Looper.getMainLooper());
        mCbDisplayPassword = findViewById(R.id.cbDisplayPassword);
        edtUsername = findViewById(R.id.edt_username_login);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        ckbRemember = findViewById(R.id.ckb_remember);
        tevForgetPassword = findViewById(R.id.tev_forget_login);
        progressBar = this.findViewById(R.id.progress_login);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        ckbRemember.setOnClickListener(this);
        tevForgetPassword.setOnClickListener(this);
        remember(username, password);
        lookPwd();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String username = EditTextUtils.getString(edtUsername);
                String password = EditTextUtils.getString(edtPassword);
                submit(username, password);
                break;
            case R.id.btn_register:
                //     startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.tev_forget_login:

 /*               Toast.makeText(ctx, "忘记密码", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ForgetPasswordActivity.class));*/

                break;
            case R.id.lel_remember_login:
                ckbRemember.setChecked(!ckbRemember.isChecked());
                break;
            default:break;
        }
    }

    /**
     * 登录
     */
    private void submit(final String username, final String password) {
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, Constant.EMPTY_USERNAME, Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, Constant.EMPTY_PASSWORD, Toast.LENGTH_SHORT).show();
            return;
        }
        //显示进度条
        progressBar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<Data>post(Constant.LOGIN)
                        .tag(1)
                        .params("username", username)
                        .params("password", password)
                        .execute(new JsonCallback<Data>(Data.class) {
                            @Override
                            public void onStart(Request<Data, ? extends Request> request) {
                                super.onStart(request);
                                //显示进度条 progressBar.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                //隐藏进度条
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            @Override
                            public void onSuccess(Response<Data> response) {
//                                判断是否登陆成功 如果code是SUCCESS_CODE成功 否则失败
                                if(Constant.SUCCESS_CODE.equals(response.body().getCode())){
                                    String name = edtUsername.getText().toString();
                                    String pwd = edtPassword.getText().toString();
                                    String token = response.body().getToken();
                                    ArrayList<ArrayList<Menu>> menuList = response.body().getMenuList();

                                    String company = response.body().getUserIfo().getCompany();
                                    String office = response.body().getUserIfo().getOffice();
                                    //保存用户权限列表
                                    DataUtils.saveMenu(menuList,ctx);
                                    //保存用户的登录信息
                                    saveUserInfo(ctx, name, pwd, token, ckbRemember, company, office);
                                    Toast.makeText(ctx, "登陆成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent2 = new Intent(ctx, MainActivity.class);
                                    startActivity(intent2);
                                }else{
                                    Toast.makeText(ctx, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(Response<Data> response) {
                                Toast.makeText(ctx, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }, 1000);
    }

    /**
     * 保存用户的登录信息
     * 如果点击了记住密码 还需要保存密码
     * @param context context
     * @param username 用户名
     * @param password 密码
     * @param token token
     * @param ckbRemember 记住密码CheckBox
     * @param company 公司
     * @param office 部门
     */
    public static void saveUserInfo(Context context, String username, String password, String token, CheckBox ckbRemember, String company, String office){
        //记住密码
        if(ckbRemember.isChecked()) {
            DataUtils.saveLocalData(context, "password", password);
        }
        DataUtils.saveLocalData(context, "username", username);
        DataUtils.saveLocalData(context, "token", token);
        DataUtils.saveLocalData(context, "company", company);
        DataUtils.saveLocalData(context, "office", office);
        GApp.TOKEN=token;
    }

    /**
     * 默认记住用户名和密码
     * @param username 用户名
     * @param password 密码
     */
    public void remember(String username, String password){
        ckbRemember.setChecked(true);
        edtUsername.setText(username);
        edtPassword.setText(password);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void lookPwd(){
        mCbDisplayPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //按下操作
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                //抬起操作
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                //移动操作
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                  //  Toast.makeText(ctx, "移动", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

    }
/*    public void LookPwd111(){
        mCbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: "+isChecked);
                if(isChecked){
                    //选择状态 显示明文--设置为可见的密码
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }*/
}
