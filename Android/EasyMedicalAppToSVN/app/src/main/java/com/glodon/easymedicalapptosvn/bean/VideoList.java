package com.glodon.easymedicalapptosvn.bean;

/**
 * Created by Administrator on 2016/1/12.
 */
public class VideoList {
    /*
    *ID
    */
    private int Id;
    /*
     *名称
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
    *上次播放位置
    */
    private String LastPlayAt;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    public String getLastPlayAt() {
        return LastPlayAt;
    }

    public void setLastPlayAt(String lastPlayAt) {
        LastPlayAt = lastPlayAt;
    }
}
