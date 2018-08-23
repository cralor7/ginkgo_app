package com.qk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;
import com.qk.util.ACache;

/**
 * @author fengyezong&cuiweilong
 * @date 2018/8/20
 * 缓存 Activity
 */
public class CashActivity extends AppCompatActivity {
    private EditText edit;
    private TextView text;
    private Button save;
    private Button show;
    private ACache aCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        initView();
    }

    public void initView(){
        edit = findViewById(R.id.edit);
        text = findViewById(R.id.text);
        save = findViewById(R.id.save);
        show = findViewById(R.id.show);
        aCache = ACache.get(this);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edit.getText().toString();
                //间数据放到缓存中，保存时间是2秒
                aCache.put("text", text, 10);
                Toast.makeText(CashActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从缓存中取数据
                String cacheData = aCache.getAsString("text");
                Toast.makeText(CashActivity.this, "获取成功", Toast.LENGTH_SHORT).show();
                text.setText(cacheData);
            }
        });
    }
}
