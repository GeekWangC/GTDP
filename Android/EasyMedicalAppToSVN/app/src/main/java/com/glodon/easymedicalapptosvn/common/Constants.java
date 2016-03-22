package com.glodon.easymedicalapptosvn.common;

/**
 * Created by shirr on 2015/12/4.
 */
public class Constants {

    /**
     * 数据格式格式 *
     */
    public static String CodeType = "utf-8";

    /**
     * 网络请求 *
     */
    public static final String NetError = "网络请求出错,请重试";

    /**
     * bug路径 *
     */
    public static String bugPath = "";

    /**
     * 根目录 *
     */
    public static String rootPath = "";

    /**
     * 缓存路径 *
     */
    public static String imgLoaderPath = "imageloader/Cache";
    public static String imgPath = "";
    public static String apkPath = "";

    //服务器的URL
    public static String ServerURL = "http://115.29.97.231:3000/";
    //public static String ServerURL = "http://172.16.11.14:3000/";
    //Banner图片的链接
    //public static String BannerPicURL = "http://115.29.97.231/AutoDoc";
    public static String BannerPicURL = "http://115.29.97.231:3000/";
    //public static String BannerPicURL = "http://172.16.11.14:3000/";
    //推荐课程和历史记录列表的图片路径
    public static String PicURL = "http://115.29.97.231:3000/";
    //public static String PicURL = "http://172.16.11.14:3000/";


    /**
     * @description 接口定义
     */
    public static class ServiceInterFace {
        //获取首页的图片的接口
        public static final String GetBannerPcUrl = ServerURL + "api/Mobile/getBanners";
        //获取历史记录的接口
        public static final String GetHistoryURL = ServerURL + "api/Mobile/GetHistoryRecord";
        //获取推荐课程的接口
        public static final String GetRecommendURL = ServerURL + "api/Mobile/GetRecommendCourse";
        //改变头像的接口
        public static final String ChangePhotoURL = ServerURL + "api/Mobile/ChangeFace";
        //加入收藏的接口
        public static final String AddCollectionURL = ServerURL + "api/Mobile/AddCollection/";
        //取消收藏的接口
        public static final String DeleteCollectionURL = ServerURL + "api/Mobile/DeleteCollection/";
    }

    /**
     * @description Request的Tag管理
     */
    public static class RequestTag {
        //banner的tag
        public static final int BannerPcTag = 100;
        //历史记录的tag
        public static final int HistoryTag = 101;
        //推荐课程的tag
        public static final int RecommendTag = 102;
    }

}
