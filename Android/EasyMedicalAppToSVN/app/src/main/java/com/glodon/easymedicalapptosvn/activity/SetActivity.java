package com.glodon.easymedicalapptosvn.activity;

import android.os.Bundle;

import org.apache.cordova.*;

/**
 * Created by Administrator on 2016/1/7.
 */
public class SetActivity extends DroidGap {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/setphoto.html");
    }
}
