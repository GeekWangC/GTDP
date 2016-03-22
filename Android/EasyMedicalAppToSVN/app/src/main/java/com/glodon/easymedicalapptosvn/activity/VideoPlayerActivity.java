package com.glodon.easymedicalapptosvn.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glodon.easymedicalapptosvn.R;
import com.glodon.easymedicalapptosvn.downloader.DownloadProgressListener;
import com.glodon.easymedicalapptosvn.downloader.FileDownloader;
import com.glodon.easymedicalapptosvn.service.MyService;
import com.glodon.easymedicalapptosvn.utils.SharePreferenceUtils;
import com.glodon.easymedicalapptosvn.utils.ToastUtils;
import com.glodon.easymedicalapptosvn.utils.Utils;
import com.glodon.easymedicalapptosvn.widget.Player;

import java.io.File;
import java.util.HashMap;


public class VideoPlayerActivity extends Activity {
    private SurfaceView surfaceView;
    private Button btnPause, btnPlayUrl, btnGoon;
    private SeekBar skbProgress;
    private Player player;
    private int screenWidth;
    private int screenHeight;
    private boolean sensor_flag = true;
    private boolean stretch_flag = true;
    private OrientationSensorListener listener;
    private Sensor sensor;
    private SensorManager sm;
    private int stopProgress = 0;
    private int getFileProgress = 0;
    //private HashMap<String, FileEntity> getVideoStopList = new HashMap<String, FileEntity>();
    private SharePreferenceUtils mSharePreferenceUtils;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 888:
                    int orientation = msg.arg1;
                    if (orientation > 45 && orientation < 135) {

                    } else if (orientation > 135 && orientation < 225) {

                    } else if (orientation > 225 && orientation < 315) {
                        VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        sensor_flag = false;
                        stretch_flag = false;
                    } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {
                        VideoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        sensor_flag = true;
                        stretch_flag = true;
                    }
                    break;
                default:
                    break;
            }
        };
    };


    private SensorManager sm1;
    private Sensor sensor1;
    private OrientationSensorListener2 listener1;
    private String getUrl;
    private String getId;


    private static final int PROCESSING = 1;// 正在下载实时数据传输Message标志
    private static final int FAILURE = -1;// 下载失败时的Message标志
    private TextView resultView;// 实现进度显示百分比文本框
    private Button downloadButton;// 下载按钮，可以触发下载事件
    private Button stopbutton;// 停止按钮，可以停止下载
    private TextView mIsVisible;
    private boolean isVisible = true;
    private ProgressBar progressBar;// 下载进度条，实时图形化的显示进度信息
    // handler对象的作用是用于往创建Handler对象所在的线程所绑定的消息队列发送信息并处理信息
    private Handler handlerDownLoad = new UIHandler();

    private final class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case PROCESSING:// 下载时
                    int size = msg.getData().getInt("size");// 从消息中获取已经下载的数据长度
                    progressBar.setProgress(size);// 设置进度条的进度
                    float num = (float) progressBar.getProgress()
                            / (float) progressBar.getMax();// 计算已经下载的百分比，此处需要转换为浮点数计算
                    int result = (int) (num * 100);// 把获取的浮点数计算结构专访为整数
                    resultView.setText(result + "%");// 把下载的百分比显示在界面显示控件上
                    if (progressBar.getProgress() == progressBar.getMax()) {
                        Toast.makeText(getApplicationContext(), R.string.success,
                                Toast.LENGTH_LONG).show();
                        // 使用Toast技术，提示用户下载完成
                    }
                    break;
                case FAILURE:// 下载失败
                    Toast.makeText(getApplicationContext(), R.string.error,
                            Toast.LENGTH_LONG).show();// 提示用户下载失败

            }
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getUrl = bundle.getString("url");
            getId = bundle.getString("id");
        }

        //启动service来处理广播任务
        Intent intent=new Intent(VideoPlayerActivity.this,MyService.class);
        VideoPlayerActivity.this.startService(intent);

        mSharePreferenceUtils = new SharePreferenceUtils(VideoPlayerActivity.this);
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);

        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);
        btnPlayUrl.setOnClickListener(new ClickEvent());

        btnPause = (Button) this.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new ClickEvent());

        btnGoon = (Button) this.findViewById(R.id.btnGoon);
        btnGoon.setOnClickListener(new ClickEvent());

        mIsVisible = (TextView) this.findViewById(R.id.visible_textview);
        mIsVisible.setBackgroundColor(Color.argb(255, 0, 255, 0));
        mIsVisible.setOnClickListener(new ClickEvent());

        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(surfaceView, skbProgress);

        //注册重力感应器  屏幕旋转
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new OrientationSensorListener(handler);
        sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);


        //根据  旋转之后 点击 符合之后 激活sm
        sm1 = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sm1.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener1 = new OrientationSensorListener2();
        sm1.registerListener(listener1, sensor1, SensorManager.SENSOR_DELAY_UI);

        resultView = (TextView) this.findViewById(R.id.resultView);// 获取显示下载百分比文本控制对象
        downloadButton = (Button) this.findViewById(R.id.btnDownLoad);// 获取下载按钮对象
        stopbutton = (Button) this.findViewById(R.id.btnPauseDown);// 获取停止下载按钮对象
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);// 获取进度条对象
        ButtonClickListener listener = new ButtonClickListener();// 声明并定义按钮监听对象
        downloadButton.setOnClickListener(listener);
        stopbutton.setOnClickListener(listener);

    }

    class ClickEvent implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (arg0 == btnPause) {
                player.pause();
            } else if (arg0 == btnPlayUrl) {
                if(mSharePreferenceUtils.getString(getId,null) != null && !Utils.isEmpty(mSharePreferenceUtils.getString(getId,null))){

                    getFileProgress = Integer.valueOf(mSharePreferenceUtils.getString(getId,null).toString());
                }
                player.playUrl(getUrl,getFileProgress);
            } else if (arg0 == btnGoon) {
                //player.stop();  
                player.play();
            }else if(arg0 == mIsVisible){
                if(isVisible){
                    btnPause.setVisibility(View.GONE);
                    btnPlayUrl.setVisibility(View.GONE);
                    btnGoon.setVisibility(View.GONE);
                    skbProgress.setVisibility(View.GONE);
                    isVisible = false;
                }else{
                    btnPause.setVisibility(View.VISIBLE);
                    btnPlayUrl.setVisibility(View.VISIBLE);
                    btnGoon.setVisibility(View.VISIBLE);
                    skbProgress.setVisibility(View.VISIBLE);
                    isVisible = true;
                }
            }
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()  
            this.progress = progress * player.mediaPlayer.getDuration() / seekBar.getMax();
            stopProgress = this.progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            if(getFileProgress > 0){
                player.mediaPlayer.seekTo(getFileProgress);
            }else{
                player.mediaPlayer.seekTo(progress);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (stretch_flag) {
            //切换成竖屏
            //FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(screenHeight, DensityUtil.dip2px(this, 160));
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            surfaceView.setLayoutParams(params1);
        } else {
            //切换成横屏
            FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(screenHeight, screenWidth);
            surfaceView.setLayoutParams(params1);
        }
    }

    /**
     * 重力感应监听者
     */
    public class OrientationSensorListener implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        private Handler rotateHandler;

        public OrientationSensorListener(Handler handler) {
            rotateHandler = handler;
        }

        public void onAccuracyChanged(Sensor arg0, int arg1) {

        }

        public void onSensorChanged(SensorEvent event) {

            if (sensor_flag != stretch_flag)  //只有两个不相同才开始监听行为
            {
                float[] values = event.values;
                int orientation = ORIENTATION_UNKNOWN;
                float X = -values[_DATA_X];
                float Y = -values[_DATA_Y];
                float Z = -values[_DATA_Z];
                float magnitude = X * X + Y * Y;
                // Don't trust the angle if the magnitude is small compared to the y value
                if (magnitude * 4 >= Z * Z) {
                    //屏幕旋转时
                    float OneEightyOverPi = 57.29577957855f;
                    float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                    orientation = 90 - (int) Math.round(angle);
                    // normalize to 0 - 359 range
                    while (orientation >= 360) {
                        orientation -= 360;
                    }
                    while (orientation < 0) {
                        orientation += 360;
                    }
                }
                if (rotateHandler != null) {
                    rotateHandler.obtainMessage(888, orientation, 0).sendToTarget();
                }

            }
        }
    }


    public class OrientationSensorListener2 implements SensorEventListener {
        private static final int _DATA_X = 0;
        private static final int _DATA_Y = 1;
        private static final int _DATA_Z = 2;

        public static final int ORIENTATION_UNKNOWN = -1;

        public void onAccuracyChanged(Sensor arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        public void onSensorChanged(SensorEvent event) {

            float[] values = event.values;

            int orientation = ORIENTATION_UNKNOWN;
            float X = -values[_DATA_X];
            float Y = -values[_DATA_Y];
            float Z = -values[_DATA_Z];

            /**
             * 这一段据说是 android源码里面拿出来的计算 屏幕旋转的 不懂 先留着 万一以后懂了呢
             */
            float magnitude = X * X + Y * Y;
            // Don't trust the angle if the magnitude is small compared to the y value
            if (magnitude * 4 >= Z * Z) {
                //屏幕旋转时
                float OneEightyOverPi = 57.29577957855f;
                float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
                orientation = 90 - (int) Math.round(angle);
                // normalize to 0 - 359 range
                while (orientation >= 360) {
                    orientation -= 360;
                }
                while (orientation < 0) {
                    orientation += 360;
                }
            }

            if (orientation > 225 && orientation < 315) {  //横屏
                sensor_flag = false;
            } else if ((orientation > 315 && orientation < 360) || (orientation > 0 && orientation < 45)) {  //竖屏
                sensor_flag = true;
            }

            if (stretch_flag == sensor_flag) {  //点击变成横屏  屏幕 也转横屏 激活
                System.out.println("激活");
                sm.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }


    /**
     * 按钮监听器实现类
     *
     * @zhangxiaobo
     */
    private final class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            // 该方法在注册了该按钮监听器的对象被单击时会自动调用，用力响应单击事件
            switch (v.getId()) {
                case R.id.btnDownLoad:// 获取点击对象的id
                    if (Environment.getExternalStorageState().endsWith(
                            Environment.MEDIA_MOUNTED)) {
                        // 获取SDCard是否存在，当SDCard存在时
                        Environment.getExternalStorageDirectory();// 获取SDCard根目录文件、
                        File saveDir = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                        getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                        download(getUrl, saveDir);
                        Toast.makeText(getApplicationContext(), saveDir.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        // 当SDCard不存在时
                        Toast.makeText(getApplicationContext(),
                                R.string.sdcarderror, Toast.LENGTH_LONG).show();// 提示用户SDCard不存在
                    }
                    downloadButton.setEnabled(false);
                    stopbutton.setEnabled(true);
                    break;
                case R.id.btnPauseDown:
                    exit();// 停止下载
                    downloadButton.setEnabled(true);
                    stopbutton.setEnabled(false);
                    break;
            }
        }

    }

    // 由于用户的输入事件（点击button，触摸屏幕...）是由主线程负责处理的
    // 如果主线程处于工作状态
    // 此时用户产生的输入时间如果没能在5秒内得到处理，系统就会报应用无响应的错误
    // 所以在主线程里不能执行一件比较耗时的工作，否则会因主线程阻塞而无法处理用户的输入事件
    // 导致“应用无响应”错误的出现，耗时的工作应该在子线程里执行
    private DownloadTask task;// 声明下载执行者

    /**
     * 退出下载
     */
    public void exit() {
        if (task != null)
            task.exit();
    }

    /**
     * 下载资源，声明下载执行者并开辟线程开始下载
     *
     * @param path
     *            下载的路径
     * @param saveDir
     *            保存文件
     */
    private void download(String path, File saveDir) {
        task = new DownloadTask(path, saveDir);// 实例化下载业务
        new Thread(task).start();
    }

    /**
     * UI控制画面的重绘（更新）是由主线程负责处理的，如果在子线程中更新UI控件值，更新后值不会重绘到屏幕上
     * 一定要在主线程里更新UI控件的值，这样才能在屏幕上显示出来，不能在子线程中更新UI控件的值
     */
    private final class DownloadTask implements Runnable {
        private String path;// 下载路径
        private File saveDir;// 下载到保存到的文件
        private FileDownloader loader;// 文件下载器（下载线程的容器）

        /**
         * 构造方法，实现变量的初始化
         *
         * @param path
         *            下载路径
         * @param saveDir
         *            下载要保存到的文件
         */
        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        /**
         * 退出下载
         */
        public void exit() {
            if (loader != null)
                loader.exit();// 如果下载器存在的话则退出下载
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            /**
             * 下载的文件长度会不断地被传入该回调方法
             */
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;
                msg.getData().putInt("size", size);
                handlerDownLoad.sendMessage(msg);
            }
        };

        public void run() {
            // TODO Auto-generated method stub
            try {
                loader = new FileDownloader(getApplicationContext(), path,
                        saveDir, 3);
                progressBar.setMax(loader.getFileSize());// 设置进度条的最大刻度
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                handlerDownLoad.sendMessage(handlerDownLoad.obtainMessage(FAILURE));

            }
        }

    }

    class FileEntity {
        public int saveProgress;

        public int getSaveProgress() {
            return saveProgress;
        }

        public void setSaveProgress(int saveProgress) {
            this.saveProgress = saveProgress;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) { //按下的如果是BACK，同时没有重复
            player.stop();
            mSharePreferenceUtils.put(getId, String.valueOf(stopProgress));
            //ToastUtils.showShort(VideoPlayerActivity.this, getId + "," +  mSharePreferenceUtils.getString(getId,null));
            VideoPlayerActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}