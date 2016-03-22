package com.glodon.easymedicalapptosvn.service;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.glodon.easymedicalapptosvn.utils.ToastUtils;

import static android.net.NetworkInfo.State;

/**
 * Created by Administrator on 2016/3/18.
 */
public class MyService extends Service {
    BroadcastReceiver networkBroadcast=null;
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkBroadcast = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent) {
                State wifiState = null;
                State mobileState = null;
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                if (wifiState != null && mobileState != null
                        && State.CONNECTED != wifiState
                        && State.CONNECTED == mobileState) {
                    ToastUtils.showShort(context,"当前是手机网络连接！");
                    // 手机网络连接成功
                } else if (wifiState != null && mobileState != null
                        && State.CONNECTED != wifiState
                        && State.CONNECTED != mobileState) {
                    ToastUtils.showShort(context,"无网络连接！");
                    // 手机没有任何的网络
                } else if (wifiState != null && State.CONNECTED == wifiState) {
                    // 无线网络连接成功

                }

            }

            /**
             * 网络是否可用
             *
             * @param context
             * @return
             */
            public boolean isNetworkAvailable(Context context) {
                ConnectivityManager mgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo[] info = mgr.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == State.CONNECTED) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };

        registerReceiver(networkBroadcast, filter);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        //动态注册广播接收者



    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        unregisterReceiver(networkBroadcast);
    }

}
