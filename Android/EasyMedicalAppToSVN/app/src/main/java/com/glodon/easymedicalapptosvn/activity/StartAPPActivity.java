package com.glodon.easymedicalapptosvn.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;

import java.util.Map;

/**
 * Created by shirr on 2015/12/4.
 */
public class StartAPPActivity extends Activity{

    private ImageView mAnimaImg;
    private SharePreferenceUtils mSP;
    private String isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = View.inflate(this, R.layout.start, null);
        setContentView(view);

        mAnimaImg = (ImageView) view.findViewById(R.id.anima_imageview);
        mSP = new SharePreferenceUtils(StartAPPActivity.this);
        isLogin = mSP.getString("islogin",null);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1800);
        view.startAnimation(aa);

        /*final Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.tip);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        *//*operatingAnim.setDuration(800);
        operatingAnim.setRepeatCount(3);*//*
        view.startAnimation(operatingAnim);*/

        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
               // mAnimaImg.clearAnimation();
                if(!Utils.isEmpty(isLogin) && isLogin.equals("yes")){
                    Intent intent = new Intent(StartAPPActivity.this,MainActivity.class);
                    startActivity(intent);
                    StartAPPActivity.this.finish();
                }else{
                    Intent intent = new Intent(StartAPPActivity.this,GuideActivity.class);
                    startActivity(intent);
                    StartAPPActivity.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
               /* if (operatingAnim != null) {
                    mAnimaImg.startAnimation(operatingAnim);
                }*/
            }
        });



    }



}
