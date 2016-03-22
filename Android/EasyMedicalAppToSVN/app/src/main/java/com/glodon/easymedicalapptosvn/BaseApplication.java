package com.glodon.easymedicalapptosvn;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.WindowManager;

import com.glodon.easymedicalapptosvn.common.Constants;
import com.glodon.easymedicalapptosvn.utils.ScreenManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by shirr on 2015/10/16.
 */
public class BaseApplication extends Application {

    /**
     * 单例 BaseApplication *
     */
    private static BaseApplication mBaseApplication;

    /**
     * 管理所有的activity *
     */
    public ScreenManager mScreenManager = null;

    /**
     * 单例 Context *
     */
    private Context context;

    /*
     *登录后的session；
     */
    private static String userSession;

    public static String getUserSession() {
        return userSession;
    }

    public static void setUserSession(String userSession) {
        BaseApplication.userSession = userSession;
    }

    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }

    /**
     * 单例的BaseApplication
     *
     * @return
     */
    public static BaseApplication getInstance() {
        if (mBaseApplication == null) {
            mBaseApplication = new BaseApplication();
        }
        return mBaseApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication = this;
        context = this;
        mScreenManager = ScreenManager.getScreenManager();

        start();
    }


    /**
     * 初始化信息
     */
    public void start() {

        // 文件路径设置
        String parentPath = null;

        // 存在SdCard的时候，路径设置到SdCard
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parentPath = Environment.getExternalStorageDirectory().getPath() + File.separatorChar + getPackageName();
        // 不存在SdCard的时候
        } else {
            parentPath = Environment.getDataDirectory().getPath()+ File.separatorChar + "data" + File.separatorChar + getPackageName();
        }

        // 路径设置
        Constants.rootPath = parentPath;
        Constants.imgPath = parentPath + "/imagecache/";
        Constants.apkPath = parentPath + "/download/";

        // 创建目录
        new File(Constants.imgPath).mkdirs();
        new File(Constants.apkPath).mkdirs();

        // 图片加载和缓存路径
        initImageLoader();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        // 清除缓存和內存
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.clearMemoryCache();
    }

    public void initImageLoader() {

        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;

        // 缓存路径,第二个参数
        File cacheDir =StorageUtils.getOwnCacheDirectory(mBaseApplication, Constants.imgLoaderPath);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisc(true)
                        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                        .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                        .build();

        ImageLoaderConfiguration config =new ImageLoaderConfiguration.Builder(context)
                        // 缓存在内存的图片的宽和高度
                        .memoryCacheExtraOptions(480, 800)
                        .threadPriority(Thread.NORM_PRIORITY - 3)
                        .threadPoolSize(3)
                        .memoryCache(new WeakMemoryCache())// 配置软引用
                        .memoryCacheSize(cacheSize)// 缓存到内存的最大数据BaseBean.java
                        .discCacheSize(50 * 1024 * 1024)// 缓存到文件的最大数据
                        .discCacheFileCount(500)// 文件数量
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())
                        .defaultDisplayImageOptions(options)// 上面的options对象，一些属性配置
                        .discCache(new UnlimitedDiscCache(cacheDir)).writeDebugLogs().build();

        ImageLoader.getInstance().init(config); // 全局初始化设置
    }

}
