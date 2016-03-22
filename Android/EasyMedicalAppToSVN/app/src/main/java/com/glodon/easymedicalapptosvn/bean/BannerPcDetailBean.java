package com.glodon.easymedicalapptosvn.bean;

/**
 * Created by Administrator on 2015/12/10.
 */
public class BannerPcDetailBean {
    /*
    *获取导航图地址
    */
    private String ImageUrl;
    /*
   *导航图链接地址
   */
    private String LinkUrl;

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return LinkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        LinkUrl = linkUrl;
    }
}
