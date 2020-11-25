package com.ttsea.library;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ttsea.commonlibrary.debug.JLog;
import com.ttsea.commonlibrary.utils.AppInformationUtils;
import com.ttsea.commonlibrary.utils.DateTimeFormat;
import com.ttsea.commonlibrary.utils.DateUtils;
import com.ttsea.commonlibrary.utils.NetWorkUtils;
import com.ttsea.commonlibrary.utils.SdStatusUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();
    }

    private void test() {

        try {
            //AdapterSettingUtils.gotoPSP(this, BuildConfig.APPLICATION_ID);
        } catch (Exception e) {
        }

        JLog.d("packageName:" + AppInformationUtils.getPackageName(this));
        JLog.d("versionCode:" + AppInformationUtils.getVersionCode(this));
        JLog.d("versionName" + AppInformationUtils.getVersionName(this));

        JLog.d("currentTime:" + DateUtils.getCurrentTime(DateTimeFormat.DATE_FORMAT_YYYY_MM_DD_HH_MM));

        JLog.d(NetWorkUtils.getNetWorkStatusStr(NetWorkUtils.getNetWorkStatus(this)));

        JLog.d("isSDAvailable:" + SdStatusUtils.isSDAvailable());
        JLog.d(SdStatusUtils.getAvailableBlockMB() + " MB");
    }
}