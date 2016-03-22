package com.glodon.easymedicalapptosvn.utils;

import android.graphics.Bitmap;
import android.widget.AbsListView;

import com.glodon.easymedicalapptosvn.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * Created by shirr on 2015/12/4.
 */
public class ImageManager {

    /**
     * 加载器 *
     */
    public static ImageLoader imageLoader;

    /**
     * 选项卡 *
     */
    public static DisplayImageOptions options;

    /**
     * 滑动监听器 *
     */
    public static AbsListView.OnScrollListener pauseScrollListener;

    public static void init() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher).cacheInMemory(true)
                .considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        pauseScrollListener = new PauseOnScrollListener(imageLoader, true, true);
    }

    /**
     * 初始化
     */
    public ImageManager() {
        if (imageLoader != null) {
            imageLoader = ImageLoader.getInstance();
        }
    }

    /**
     * 初始化暂停时不刷新
     */
    public ImageManager(boolean isPause) {
        if (imageLoader != null) {
            imageLoader = ImageLoader.getInstance();
        }
        pauseScrollListener = new PauseOnScrollListener(imageLoader, true, true);
    }

    /**
     * 设置加载的图片
     *
     * @param LoadResoucrId
     * @param EmptyUri
     * @return
     */
    public static DisplayImageOptions getOptions(int LoadResoucrId, int EmptyUri) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(LoadResoucrId)
                .showImageForEmptyUri(EmptyUri).showImageOnFail(EmptyUri)
                .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    /**
     * 设置本地默认图片
     *
     * @param LoadResoucrId
     * @param EmptyUri
     * @return
     */
    public static DisplayImageOptions getDefaultOptions(int LoadResoucrId,
                                                        int EmptyUri) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(LoadResoucrId)
                .showImageForEmptyUri(EmptyUri).showImageOnFail(EmptyUri)
                .cacheInMemory(false).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
                .build();
        return options;
    }



}
