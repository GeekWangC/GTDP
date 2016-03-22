package com.glodon.easymedicalapptosvn.bean;

/**
 * Created by shirr on 2016/1/11.
 */
public class CourseWareList {
    /*
     *课程ID
     */
    private int Id;
    /*
     *课程名称
     */
    private String Name;
    /*
    *课程缩略图地址
    */
    private String ThumbnailUrl;
    /*
    *讲师名称
    */
    private String Teacher;
    /*
    *包含课件数量
    */
    private int totalPage;
    /*
     *总积分
      */
    private int TotalScore;
    /*
    *是否已经收藏
    */
    private Boolean IsCollect;
    /*
   *课程所属科目
   */
    private String CourseName;

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

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        Teacher = teacher;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        TotalScore = totalScore;
    }

    public Boolean getIsCollect() {
        return IsCollect;
    }

    public void setIsCollect(Boolean isCollect) {
        IsCollect = isCollect;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }
}
