package com.qk.activity.sys;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.qk.BaseActivity;
import com.qk.Constant;
import com.qk.GApp;
import com.qk.R;
import com.qk.activity.MainActivity;
import com.qk.adapter.SetListViewAdapter;
import com.qk.module.SetListViewItem;
import com.qk.module.SimpleItem;
import com.qk.util.DataUtils;
import com.qk.util.ViewUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import cn.jpush.android.api.JPushInterface;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 设置 Activity
 */
public class SetActivity extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    /**
     * 滑动按钮
     */
    private Switch aSwitch;
    private SwipeRefreshLayout refresh;
    /**
     * 判断是否接收推送
     */
    private Boolean jpush;
    private String token;
    private TextView tevName;
    private Context context;
    private ListView listView;
    private BaseAdapter adapter;
    private List<Object> list;
    private View  view;
    private Boolean scrollFlag=false;
    private ImageView imgvUserPhoto;
    Handler handler;
    private String image;
    private Bitmap bitmap;
    private String[] strings1={"账号","修改密码","个人信息","其他","每页显示数据条数","检查更新","关于我们","退出"};
    private String[] stringsNum={"","","","","10","","","","","","","","",""};
    private int img[]={R.mipmap.tongzhi_huise,R.mipmap.wo_huise,R.mipmap.xiangmu_huise,R.mipmap.liaotian_huise,R.mipmap.gengduo_huise,R.mipmap.gengduo_huise,R.mipmap.xiangmu_huise,R.mipmap.liaotian_huise,R.mipmap.xiangmu_huise,R.mipmap.xiangmu_huise,R.mipmap.tongzhi_huise,R.mipmap.wo_huise,R.mipmap.xiangmu_huise,R.mipmap.liaotian_huise,R.mipmap.tongzhi_huise,R.mipmap.xiangmu_huise,R.mipmap.wo_huise ,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige,R.mipmap.xinge,R.mipmap.rege,R.mipmap.yaogunge,R.mipmap.oumeige};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
//        setContentView(intiLayout());
     /*   initView();*/

    }
    @Override
    public int intiLayout() {
        return R.layout.activity_set;
    }
    @Override
    public void initView(){
        //修改引入的通用头部的标题
        ViewUtils.topLayout(this, "设置");
        aSwitch = findViewById(R.id.set_switch);
        listView = findViewById(R.id.listview);
        jpush = DataUtils.getLocalDataBoolean(this, "jpush", false);
        token = DataUtils.getLocalData(this, "token", "");
        aSwitch.setOnCheckedChangeListener(this);
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void initData() {
        list = new ArrayList<>();
        initListView();
        //根据存储的是否接受信息展示switch
        if(jpush){
            aSwitch.setChecked(true);
        }else{
            aSwitch.setChecked(false);
        }
    }

    /**
     * 添加内容
     */
    public void initListView(){
        stringsNum[4] = DataUtils.getLocalData(this, "pageNumber", "10");
        for(int i = 0; i < strings1.length; i++) {
            if(i == 0){
                SimpleItem simpleItem = new SimpleItem();
                simpleItem.setText(strings1[0]);
                list.add(simpleItem);
            }
            else if(i == 3){
                SimpleItem simpleItem = new SimpleItem();
                simpleItem.setText(strings1[3]);
                list.add(simpleItem);
            }
            else{
                SetListViewItem gridviewentity = new SetListViewItem();
                gridviewentity.setImg(img[i]);
                gridviewentity.setText(strings1[i]);
                gridviewentity.setText2(stringsNum[i]);
                list.add(gridviewentity);
            }
        }
        adapter = new SetListViewAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    Intent intent3 = new Intent(context, ChangePasswordActivity.class);
                    startActivity(intent3);
                }
                if(position == 2){
                    Intent intent2 = new Intent(context, UserActivity.class);
                    startActivity(intent2);
                }
                if(position == 4){
                    new QMUIBottomSheet.BottomListSheetBuilder(context)
                            .addItem(getResources().getString(R.string.set_listview_num10))
                            .addItem(getResources().getString(R.string.set_listview_num20))
                            .addItem(getResources().getString(R.string.set_listview_num30))
                            .addItem(getResources().getString(R.string.set_listview_num50))
                            .addItem(getResources().getString(R.string.set_listview_num100))
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
                                    switch (position) {
                                        case 0:
                                            setPageNum("10");
                                            break;
                                        case 1:
                                            setPageNum("20");
                                            break;
                                        case 2:
                                            setPageNum("30");
                                            break;
                                        case 3:
                                            setPageNum("50");
                                            break;
                                        case 4:
                                            setPageNum("100");
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            })
                            .build()
                            .show();
                }
                if(position == 5){
                    checkVersion();
                }
                if(position == 6){
                    Intent intent2 = new Intent(context, AboutUsActivity.class);
                    startActivity(intent2);
                }
                if(position == 7){
                    //退出
                    logout();
                }
                Toast.makeText(context, position + "！！！", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_back:
                finish();
                break;
            default:break;
        }
    }

    /**
     * 监听switch
     * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if (isChecked) {
            DataUtils.updateLocalData(this, "jpush", true);
        } else {
            DataUtils.updateLocalData(this, "jpush", false);
        }
        JPushInterface.stopPush(GApp.context);
    }

    /**
     * 退出登录，清除token
     */
    public void logout(){
        //退出的确认弹出框
        new AlertDialog.Builder(context)
                .setTitle("退出")
                .setMessage("退出当前账号？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //清除本地token
                        DataUtils.removeLocalData(context,"token");
                        finish();
                        MainActivity.instance.finish();
                        Intent intent3 = new Intent(context, LoginActivity.class);
                        startActivity(intent3);
                        //清除服务器token
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                OkGo.<String>post(Constant.LOGOUT)
                                        .tag(1)
                                        .headers("Authorization", "Bearer " + token)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                //这个就是返回来的结果
                                                try {
                                                    String data = response.body();
                                                    JSONObject jsonObject = new JSONObject(data);
                                                    String code = "" + jsonObject.get("code").toString();
                                                    if(Constant.SUCCESS_CODE.equals(code)){
                                                    }else{
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onError(Response<String> response) {
                                            }
                                        });
                            }
                        }, 3);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
    /**
     * 列表页每次加载数据条数
     * @param num 加载条数
     */
    public void setPageNum(String num){
        ((SetListViewItem)list.get(4)).setText2(num);
        DataUtils.updateLocalData(context, "pageNumber", num);
        GApp.pageNumber = num;
        adapter.notifyDataSetChanged();
    }


    public void checkVersion() {
        final QMUITipDialog tipDialog;
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在检查新版本")
                .create();
        tipDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              /*  OkGo.<String>post(Constant.USER_MESSAGE)
                        .tag(1)
                        .headers("Authorization","Bearer " + token)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                tipDialog.dismiss();
                            }
                            @Override
                            public void onFinish() {
                                super.onFinish();
                            }
                            @Override
                            public void onError(Response<String> response) {
                                tipDialog.dismiss();

                            }

                        });*/
                tipDialog.dismiss();
            }
        }, 3000);
    }

}

