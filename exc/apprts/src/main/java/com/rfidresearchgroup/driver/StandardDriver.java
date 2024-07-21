package com.rfidresearchgroup.driver;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;

import com.rfidresearchgroup.common.util.AppUtil;
import com.rfidresearchgroup.mifare.StdMifareIntent;
import com.proxgrind.com.DevCallback;
import com.proxgrind.com.DriverInterface;

public class StandardDriver implements DriverInterface<String, NfcAdapter> {

    private Application context = AppUtil.getInstance().getApp();
    private static final String LOG_TAG = StandardDriver.class.getSimpleName();
    private static final int UNIQUE_ID = 0x05;
    private DevCallback<String> callback = null;
    private StdMifareIntent mMftools = null;
    private volatile static boolean isRegister = false;
    private static StandardDriver mThiz;

    static {
        mThiz = new StandardDriver();
    }

    /*
     * 请求发现设备的广播!
     * */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "收到了设备寻找广播!");
            if (getAdapter() != null) {
                if (getAdapter().isEnabled()) {
                    callback.onAttach(getDevice());
                } else {
                    callback.onDetach(getDevice());
                }
            } else {
                Log.d(LOG_TAG, "当前设备不支持标准NFC设备!");
            }
        }
    };

    private StandardDriver() {
        //单例实现
    }

    public static StandardDriver get() {
        return mThiz;
    }

    @Override
    public void register(DevCallback<String> callback) {
        if (!isRegister) {
            //实例化工具持有
            mMftools = new StdMifareIntent(context);
            //缓存回调
            this.callback = callback;
            //标志位更改
            isRegister = true;
            //注册设备发现广播!
            context.registerReceiver(mReceiver, new IntentFilter("cn.rrg.devices.std_discovery"));
        }
    }

    @Override
    public boolean connect(String t) {
        //对于设备的连接直接返回设备当前的开关状态即可!
        if (getAdapter() != null)
            return getAdapter().isEnabled();
        return false;
    }

    @Override
    public NfcAdapter getAdapter() {
        return mMftools.getAdapter();
    }

    @Override
    public String getDevice() {
        return "标准NFC设备";
    }

    @Override
    public void disconect() {
        //TODO 待实现!
        if (callback != null)
            callback.onDetach(getDevice());
    }

    @Override
    public void unregister() {
        //解注册广播监听事件!
        context.unregisterReceiver(mReceiver);
        isRegister = false;
    }

    @Override
    public OutputStream getOutput() {
        return null;
    }

    @Override
    public InputStream getInput() {
        return null;
    }
}
