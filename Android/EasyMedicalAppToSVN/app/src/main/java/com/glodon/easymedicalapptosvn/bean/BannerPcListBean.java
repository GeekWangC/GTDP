package com.glodon.easymedicalapptosvn.bean;

import java.util.List;

/**
 * Created by shirr on 2015/12/9.
 */
public class BannerPcListBean {
    /*
    *获取导航图地址
    */
    private List<BannerPcDetailBean> bannerPcDetailBean;

    public List<BannerPcDetailBean> getBannerPcDetailBean() {
        return bannerPcDetailBean;
    }

    public void setBannerPcDetailBean(List<BannerPcDetailBean> bannerPcDetailBean) {
        this.bannerPcDetailBean = bannerPcDetailBean;
    }

}
