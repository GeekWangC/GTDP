package com.glodon.easymedicalapptosvn.bean;

/**
 * Created by shirr on 2016/1/11.
 */
public class QuestionList {
    /*
     *课程ID
     */
    private int QuestionID;
    /*
     *课程名称
     */
    private String Content;
    /*
     *总积分
     */
    private int Score;
    /*
     *是否已经收藏
     */
    private Boolean IsCollect;
    /*
     *课程所属科目
     */
    private String CourseName;

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
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
