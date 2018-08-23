package com.qk.activity.sys;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.R;
import com.qk.module.UserInfo;
import com.qk.util.DataUtils;
import com.qk.util.EditTextUtils;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 用户详情 Activity
 */
public class UserActivity extends BaseActivity  implements View.OnClickListener{
    /**
     * 线程
     **/
    private Handler handler;
    private Context ctx;
    private QMUIEmptyView mEmptyView;
    private QMUIGroupListView mGroupListView;
    private String token;
    private String username;
    private TextView company;
    private TextView office;
    private TextView name;
    private TextView no;
    private EditText email;
    private EditText phone;
    private EditText mobile;
    private TextView tevMenuRight;
    private ImageView imvLeftBack;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    private ScrollView scrollView;
    private String oldEmail,oldPhone,oldMobile;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        ctx = this;
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_user;
    }

    @Override
    public void initView() {
        mEmptyView = findViewById(R.id.emptyView);
        mEmptyView.hide();
        handler = new Handler(Looper.getMainLooper());
        mGroupListView = findViewById(R.id.groupListView);
        token = DataUtils.getLocalData(this, "token", "token");
        username = DataUtils.getLocalData(this, "username", "username");
        company = findViewById(R.id.text2);
        office = findViewById(R.id.text22);
        name = findViewById(R.id.text23);
        no = findViewById(R.id.text24);
        email = findViewById(R.id.text25);
        phone = findViewById(R.id.text26);
//        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        mobile = findViewById(R.id.text27);
        tevMenuRight = findViewById(R.id.menu_text_right);
        tevMenuRight.setVisibility(View.GONE);
        scrollView = findViewById(R.id.scrollview);
        scrollView.setVisibility(View.GONE);
        tevMenuRight.setOnClickListener(this);
        imvLeftBack = findViewById(R.id.left_back_menu);
        imvLeftBack.setOnClickListener(this);
        editTextable(email,false);
        editTextable(phone,false);
        editTextable(mobile,false);
    }

    @Override
    public void initData() {
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(ctx)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        tipDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OkGo.<String>post(Constant.USER_MESSAGE)
                        .tag(1)
                        .headers("Authorization","Bearer " + token)
                        .params("username", username)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                scrollView.setVisibility(View.VISIBLE);
                                tevMenuRight.setVisibility(View.VISIBLE);
                                mEmptyView.hide();
                                String data = response.body();
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    Gson gson = new Gson();
                                    String  userInfoData = jsonObject.get("data").toString();
                                    UserInfo userinfo = gson.fromJson(userInfoData, UserInfo.class);
                                    String name1 = userinfo.getName();
                                    String no1 = userinfo.getNo();
                                    String company1 = userinfo.getCompany();
                                    String office1 = userinfo.getOffice();
                                    String email1 = userinfo.getEmail();
                                    String phone1 = userinfo.getPhone();
                                    String mobile1 = userinfo.getMobile();
                                    company.setText(company1);
                                    office.setText(office1);
                                    name.setText(name1);
                                    no.setText(no1);
                                    email.setText(email1);
                                    phone.setText(phone1);
                                    mobile.setText(mobile1);
                                    oldEmail = email1;
                                    oldPhone = phone1;
                                    oldMobile = mobile1;
                                    tipDialog.dismiss();
                                } catch (JSONException e) {
                                    scrollView.setVisibility(View.GONE);
                                    tevMenuRight.setVisibility(View.GONE);
                                    e.printStackTrace();
                                    tipDialog.dismiss();
                                    mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            initData();
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onFinish() {
                                super.onFinish();
                            }
                            @Override
                            public void onError(Response<String> response) {
                                scrollView.setVisibility(View.GONE);
                                tevMenuRight.setVisibility(View.GONE);
                                tipDialog.dismiss();
                                mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        initData();
                                    }
                                });
                            }

                        });
            }
        }, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_text_right:
                if("修改".equals(tevMenuRight.getText().toString())){
                    tevMenuRight.setText("提交");
                    editTextable(email, true);
                    editTextable(phone, true);
                    editTextable(mobile, true);
                    email.requestFocus();
                    email.setBackgroundResource(R.drawable.user_edittext_bg);
                    phone.setBackgroundResource(R.drawable.user_edittext_bg);
                    mobile.setBackgroundResource(R.drawable.user_edittext_bg);
                }else{
                    //手机号格式不正确
                    if(EditTextUtils.getString(mobile).length()<11){
                        Toast.makeText(ctx, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                        mobile.setFocusable(true);
                        mobile.setFocusableInTouchMode(true);
                        mobile.requestFocus();
                        return;
                    }
                    email.setFocusable(false);
                    tevMenuRight.setText("修改");
                    editTextable(email, false);
                    editTextable(phone, false);
                    editTextable(mobile, false);
                    if(oldEmail.equals(EditTextUtils.getString(email)) && oldPhone.equals(EditTextUtils.getString(phone)) && oldMobile.equals(EditTextUtils.getString(mobile))){
                        Toast.makeText(ctx, "您并没有做出修改", Toast.LENGTH_SHORT).show();
                        email.setBackgroundResource(R.drawable.user_edittext_nobg);
                        phone.setBackgroundResource(R.drawable.user_edittext_nobg);
                        mobile.setBackgroundResource(R.drawable.user_edittext_nobg);
                        return;
                    }

                    updateUser();
                    oldEmail =  EditTextUtils.getString(email);
                    oldPhone =  EditTextUtils.getString(phone);
                    oldMobile =  EditTextUtils.getString(mobile);
                }
                break;
            case R.id.left_back_menu:
                finish();
                break;
            default:break;
        }
    }
    /**设置EditText可输入和不可输入状态*/
    private void editTextable(EditText editText, boolean editable) {
        if (!editable) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setClickable(false);
        } else {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.setClickable(true);
        }
    }

    /**
     * 修改用户个人信息
     */
    public void updateUser(){
       final QMUITipDialog tipDialog;
       tipDialog = new QMUITipDialog.Builder(ctx)
               .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
               .setTipWord("正在提交")
               .create();
       tipDialog.show();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               OkGo.<String>post(Constant.USER_MESSAGE_UPDATE)
                       .tag(1)
                       .headers("Authorization","Bearer " + token)
                       .params("email", EditTextUtils.getString(email))
                       .params("mobile", EditTextUtils.getString(mobile))
                       .params("phone", EditTextUtils.getString(phone))
                       .execute(new StringCallback() {
                           @Override
                           public void onSuccess(Response<String> response) {
                               String data = response.body();
                               String code = "";
                               try {
                                   JSONObject jsonObject = new JSONObject(data);
                                   code = jsonObject.get("code").toString();

                                   //判断是否修改成功 如果code是success code 则成功，否则失败
                                   if(Constant.SUCCESS_CODE.equals(code)){
                                       tipDialog.dismiss();
                                       email.setBackgroundResource(R.drawable.user_edittext_nobg);
                                       phone.setBackgroundResource(R.drawable.user_edittext_nobg);
                                       mobile.setBackgroundResource(R.drawable.user_edittext_nobg);
                                       Toast.makeText(ctx, "修改成功", Toast.LENGTH_SHORT).show();

                                   }else{
                                   }

                               } catch (Exception e) {
                                   scrollView.setVisibility(View.GONE);
                                   tevMenuRight.setVisibility(View.GONE);
                                   e.printStackTrace();
                                   tipDialog.dismiss();
                                   mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           initData();
                                       }
                                   });
                               }
                           }
                           @Override
                           public void onFinish() {
                               super.onFinish();
                           }
                           @Override
                           public void onError(Response<String> response) {
                               scrollView.setVisibility(View.GONE);
                               tevMenuRight.setVisibility(View.GONE);
                               tipDialog.dismiss();
                               mEmptyView.show(false, getResources().getString(R.string.emptyView_mode_desc_fail_title), getResources().getString(R.string.emptyView_mode_desc_fail_desc), getResources().getString(R.string.emptyView_mode_desc_retry), new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       initData();
                                   }
                               });
                           }

                       });
           }
       }, 1);
   }

}
