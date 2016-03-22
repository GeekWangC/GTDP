package com.glodon.easymedicalapptosvn.bean;

import java.util.List;

/**
 * Created by shirr on 2015/12/7.
 */
public class HistoryListBean{

    /*
     *课程列表
     */
    private List<CurriculumList> CurriculumList;
    /*
     *课程列表
     */
    private List<CourseWareList> CourseWareList;
    /*
     *题库当前位置试题列表
     */
    private List<QuestionList> QuestionList;
    /*
    *模拟考试当前位置试题
    */
    private List<ExamQuestionList> ExamQuestionList;
    /*
     *切片列表
     */
    private List<SliceList> SliceList;
    /*
     *视频列表
     */
    private List<VideoList> VideoList;

    public List<CurriculumList> getCurriculumList() {
        return CurriculumList;
    }

    public void setCurriculumList(List<CurriculumList> curriculumList) {
        CurriculumList = curriculumList;
    }

    public List<CourseWareList> getCourseWareList() {
        return CourseWareList;
    }

    public void setCourseWareList(List<CourseWareList> courseWareList) {
        CourseWareList = courseWareList;
    }

    public List<QuestionList> getQuestionList() {
        return QuestionList;
    }

    public void setQuestionList(List<QuestionList> questionList) {
        QuestionList = questionList;
    }

    public List<ExamQuestionList> getExamQuestionList() {
        return ExamQuestionList;
    }

    public void setExamQuestionList(List<ExamQuestionList> examQuestionList) {
        ExamQuestionList = examQuestionList;
    }

    public List<SliceList> getSliceList() {
        return SliceList;
    }

    public void setSliceList(List<SliceList> sliceList) {
        SliceList = sliceList;
    }

    public List<VideoList> getVideoList() {
        return VideoList;
    }

    public void setVideoList(List<VideoList> videoList) {
        VideoList = videoList;
    }
}
