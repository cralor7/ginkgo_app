package com.qk.fragment.xsgl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.qk.R;
import com.qk.fragment.xsgl.OrderRetrievalFragment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/1
 * 订单检索输入界面
 */
public class SearchOrderRetrievalDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String DDH = "ddh";
    public static final String YWMS = "ywms";
    public static final String JXSCODE = "jxscode";
    private EditText searchDDH;
    private EditText searchYWMS;
    private EditText searchJXSCODE;
    private Button searchSubmit;
    private Button searchClean;
    private ImageView searchCancel;
    private Context ctx;
    private SearchOrderRetrievalDialogFragment fragment;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.ctx = context;
    }
    @Override
    public void onStart() {
        super.onStart();
        //设置宽高
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment=this;
        //设置背景透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //点击外部不消失
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setWindowAnimations(R.style.dialogAnim);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_view, null);
        searchCancel = view.findViewById(R.id.search_cancel);
        searchDDH = view.findViewById(R.id.search_ddh);
        searchYWMS = view.findViewById(R.id.search_ywms);
        searchJXSCODE = view.findViewById(R.id.search_jxscode);
        searchSubmit = view.findViewById(R.id.search_submit);
        searchClean = view.findViewById(R.id.search_clean);

        //设置EditText不可编辑但可点击,隐藏光标
        searchYWMS.setCursorVisible(false);
        searchYWMS.setFocusable(false);
        searchYWMS.setFocusableInTouchMode(false);
        searchYWMS.setOnClickListener(this);

        searchCancel.setOnClickListener(this);
        searchSubmit.setOnClickListener(this);
        searchClean.setOnClickListener(this);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_submit:
                if (getTargetFragment() == null){
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(DDH, searchDDH.getText().toString());
                intent.putExtra(YWMS, searchYWMS.getText().toString());
                intent.putExtra(JXSCODE, searchJXSCODE.getText().toString());
                getTargetFragment().onActivityResult(OrderRetrievalFragment.REQUEST_CODE, Activity.RESULT_OK, intent);
                break;
            case R.id.search_clean:
                searchDDH.setText("");
                searchYWMS.setText("");
                searchJXSCODE.setText("");
                break;
            case R.id.search_cancel:
                fragment.dismiss();
                break;
            case R.id.search_ywms:
                showSingleChoiceDialog();
                break;
            default:break;
        }
    }
    private void showSingleChoiceDialog() {
        final String[] items = new String[]{"全部", "贷款发车", "款到发车", "保证金周转车"};
        final int checkedIndex = 1;
        new QMUIDialog.CheckableDialogBuilder(ctx)
                // .setCheckedIndex(checkedIndex)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ctx, "你选择了 " + items[which], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        searchYWMS.setText(items[which]);
                    }
                })
                .create(mCurrentDialogStyle).show();
    }
}