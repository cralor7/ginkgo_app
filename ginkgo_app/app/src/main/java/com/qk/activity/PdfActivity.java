package com.qk.activity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.qk.R;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class PdfActivity extends AppCompatActivity implements View.OnClickListener,DownloadFile.Listener{
    private RelativeLayout pdf_root;
    private ProgressBar pb_bar;
    private RemotePDFViewPager remotePDFViewPager;
    private String mUrl = "http://10.2.72.105:8080/ceshi.pdf";
    private PDFPagerAdapter adapter;
    private ImageView iv_back;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        initView();
        setDownloadListener();
    }

    protected void initView() {
        pdf_root = (RelativeLayout) findViewById(R.id.remote_pdf_root);
        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDownloadListener();
            }
        });
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remotePDFViewPager.removeAllViews();
                setDownloadListener();
            }
        });
    }


    /*设置监听*/
    protected void setDownloadListener() {
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(this, mUrl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    /*加载成功调用*/
    @Override
    public void onSuccess(String url, String destinationPath) {
        pb_bar.setVisibility(View.GONE);
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    /*更新视图*/
    private void updateLayout() {
        pdf_root.removeAllViewsInLayout();
        pdf_root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /*加载失败调用*/
    @Override
    public void onFailure(Exception e) {
        pb_bar.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                break;
                default:
                    break;
        }
    }
}
