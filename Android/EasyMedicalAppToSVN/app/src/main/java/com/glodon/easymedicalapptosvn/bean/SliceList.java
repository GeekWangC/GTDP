package com.glodon.easymedicalapptosvn.bean;

/**
 * Created by shirr on 2016/1/11.
 */
public class SliceList {

    /*
     *切片ID
     */
    private int SliceID;
    /*
     *切片名称
     */
    private String Name;
    /*
     *缩略图地址
     */
    private String ThumbnailUrl;
    /*
     *是否已经收藏
     */
    private Boolean IsCollect;
    /*
    *当前目录对应的产品图像数目
    */
    private int PicsNum;

    public int getSliceID() {
        return SliceID;
    }

    public void setSliceID(int sliceID) {
        SliceID = sliceID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getThumbnailUrl() {
        return ThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        ThumbnailUrl = thumbnailUrl;
    }

    public Boolean getIsCollect() {
        return IsCollect;
    }

    public void setIsCollect(Boolean isCollect) {
        IsCollect = isCollect;
    }

    public int getPicsNum() {
        return PicsNum;
    }

    public void setPicsNum(int picsNum) {
        PicsNum = picsNum;
    }
}
