package com.qk.nfc;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qk.R;

/**
 * @author fengyezong & cuiweilong
 * @date 2018/8/13
 * NFC
 */
public class NFC extends Activity {
    TextView tv1,tv2,tv3;
    EditText etSector,etBlock,etData;
    private NfcAdapter nfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;
    private int mCount = 0;
    String info="";
    Button btn1;
    String shebeihao = "";
    private int bIndex;
    private int bCount;

    private int BlockData;
    private String BlockInfo;
    private RadioButton mRead, mWriteData,mChange;
    private byte[] b3;

    boolean hasData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test1);
        init();//刷新控件
        //获取默认的NFC适配器

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            tv1.setText("设备不支持NFC！");
            //  finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            tv1.setText("请在系统设置中先启用NFC功能！");
            // finish();
            return;
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,  getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // Setup an intent filter for all MIME based dispatches
        IntentFilter ndef  = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        mFilters = new IntentFilter[] {ndef,    };

        // 读标签之前先确定标签类型。这里以大多数的NfcA为例
        mTechLists  = new String[][] { new String[] {NfcA.class.getName() } };
    }

    private void init() {
        // TODO 自动生成的方法存根
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
    }

    /**
     * 指定一个用于处理NFC标签的窗口
     */
    @Override
    protected void onResume() {
        // TODO 自动生成的方法存根
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this,  mPendingIntent, mFilters, mTechLists);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO 自动生成的方法存根
        super.onNewIntent(intent);
//    	  tv1.setText("发现新的 Tag:  "+ ++mCount);//mCount 计数
        String intentActionStr = intent.getAction();// 获取到本次启动的action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intentActionStr)// NDEF类型
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentActionStr)// 其他类型
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentActionStr)) {// 未知类型

            //在intent中读取Tag id
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] bytesId = tag.getId();// 获取id数组

            shebeihao = ByteArrayChange.ByteArrayToHexString(bytesId);
            info = ByteArrayChange.ByteArrayToHexString(bytesId) + "\n";
            //展示芯片ID
            tv2.setText("标签UID:  " + info);
            tv3.setText("车架号: " + readTag(tag));
        }
    }

    public String readTag(Tag tag) {
        MifareClassic mfc = MifareClassic.get(tag);
        for (String tech : tag.getTechList()) {
            //显示设备支持技术
            System.out.println(tech);
        }
        boolean auth = false;
        //读取TAG

        try {
            StringBuffer metaInfo = new StringBuffer();

            StringBuffer chejiahao = new StringBuffer();
            String chejia = new String();
            String newStr = new String();

            // String metaInfo = "";
            //Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            //获取TAG的类型
            int type = mfc.getType();
            //获取TAG中包含的扇区数
            int sectorCount = mfc.getSectorCount();
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
                default:break;
            }
            metaInfo.append("  卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
                    + "B\n");
//        metaInfo += "  卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
//        + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
//        + "B\n";
            System.out.println("车架号222222222222222" + "=====");
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                //逐个获取密码
                auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                if (auth) {
                    metaInfo.append("Sector " + j + ":验证成功\n");
                    //   metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo.append("Block " + bIndex + " : "
                                + ByteArrayChange.ByteArrayToHexString(data) + "\n");
//                      metaInfo += "Block " + bIndex + " : "
//                              + ByteArrayChange.ByteArrayToHexString(data) + "\n";
                        System.out.println("十六进制车架号" + chejiahao + "----");

                        if (j == 1 && i == 2) {
                            chejiahao.append(ByteArrayChange.ByteArrayToHexString(data));
//  						byte[] bt = new byte[1024];
//  						newStr = chejiahao.toString();
                            newStr = ToStringHex.toStringHex(chejiahao.toString().substring(0, 16));
                            System.out.println("车架号" + chejia + "=====");
                            System.out.println("十六进制车架号" + chejiahao + "----");
                            //取到车架号信息，将车架号信息传到后台，获取后台结果，展示
                        }

                        bIndex++;
                    }
                } else {
                    metaInfo.append("Sector " + j + ":验证失败\n");
                }
            }

            SharedPreferences sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE);
            String name = sharedPreferences.getString("name", "");
            String jd = sharedPreferences.getString("jd", "");
            String wd = sharedPreferences.getString("wd", "");
            String city = sharedPreferences.getString("city", "");
            String address = sharedPreferences.getString("address", "");
            String sessionid = sharedPreferences.getString("sessionid", "");
            String stockcede = sharedPreferences.getString("stockcode", "");
            String pkcode = sharedPreferences.getString("pkcode", "");
            String sbh = sharedPreferences.getString("sbh", "");
//          T.showLong(this, pkcode);
            //扫描成功 传地址、标签ID、车架号、登录名到后台
            return newStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //转换Hex为字符串
    public String change(Tag tag) {
        MifareClassic mfc = MifareClassic.get(tag);
        Log.d("----------","change----------");
        boolean auth = false;
        //读取TAG

        String ChangeInfo = "";
        String  Ascll = "";
        //Enable I/O operations to the tag from this TagTechnology object.
        try {
            mfc.connect();
            //获取扇区数
            String etSectorInfo = etSector.getText().toString();
            //非常重要---------------------------
            auth = mfc.authenticateSectorWithKeyA(StringToInt(etSectorInfo),
                    MifareClassic.KEY_DEFAULT);
            //获取块数
            String etBlockInfo = etBlock.getText().toString();
            if (auth) {
                Log.i("change 的auth验证成功", "开始读取模块信息");
                byte[] data = mfc.readBlock(4*StringToInt(etSectorInfo)+StringToInt(etBlockInfo));
                //--------------
                ChangeInfo =  ByteArrayChange.ByteArrayToHexString(data) ;
                //String temp=ToStringHex.decode(ChangeInfo);
                Ascll = ToStringHex.decode(ChangeInfo);
                return Ascll;
            }
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    public int StringToInt(String s) {
        if (!(TextUtils.isEmpty(s))||s.length()>0) {
            BlockData = Integer.parseInt(s);
        }
        else {
            Toast.makeText(NFC.this, "Block输入有误", Toast.LENGTH_LONG).show();
        }
        System.out.println(BlockData);
        return BlockData;
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }



    public static String toStringHex(String s)
    {
        if("0x".equals(s.substring(0, 2)))
        {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try{
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2), 16));
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        try
        {
            //UTF-16le:Not
            s = new String(baKeyword, "utf-8");
        }
        catch (Exception e1){
            e1.printStackTrace();
        }
        return s;
    }
}

