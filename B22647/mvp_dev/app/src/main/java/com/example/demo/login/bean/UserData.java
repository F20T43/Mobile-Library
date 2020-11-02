package com.example.demo.login.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class UserData  extends BmobUser {
    private String userName;
    private String userPwd;
    private String userPhone;
    private String userType;
    private String topic;
    public String grade;
    public String userEmail;
    public  int userId;
    private String cohort;
    public String assessmentType;
    public String reason;
    public  String knowledgeGap;
    public String  strategy;
    public String  learnLevel;

    public String desc;
    //获取用户名
    public String getUserName() {             //获取用户名
        return userName;
    }
    //设置用户名
    public void setUserName(String userName) {  //输入用户名
        this.userName = userName;
    }
    //获取用户类型
    public String getUserPhone() {                //获取用户密码
        return userPhone;
    }
    //设置用户类型
    public void setUserPhone(String userPhone) {     //输入用户密码
        this.userPhone = userPhone;
    }
    //获取用户类型
    public String getUserType() {                //获取用户密码
        return userType;
    }
    //设置用户类型
    public void setUserType(String userType) {     //输入用户密码
        this.userType = userType;
    }
    //获取用户密码
    public String getUserPwd() {                //获取用户密码
        return userPwd;
    }
    //设置用户密码
    public void setUserPwd(String userPwd) {     //输入用户密码
        this.userPwd = userPwd;
    }
    //获取用户邮箱
    public String getUserEmail() {                //获取用户密码
        return userEmail;
    }
    //设置用户邮箱
    public void setUserEmail(String userEmail) {     //输入用户密码
        this.userEmail = userEmail;
    }

    public String getTopic() {                   //获取用户ID号
        return topic;
    }

    public void setTopic(String topic) {       //设置用户ID号
        this.topic = topic;
    }

    public String getGrade() {                   //获取用户ID号
        return grade;
    }

    public void setGrade(String grade) {       //设置用户ID号
        this.grade = grade;
    }
    public UserData() {  //这里只采用用户名和密码
        super();
    }
    public UserData(String grade) {  //这里只采用用户名和密码
        super();
        this.grade = grade;
    }
    public UserData(String userName, String userPwd) {  //这里只采用用户名和密码
        super();
        this.userName = userName;
        this.userPwd = userPwd;
    }
    public UserData(String userName, String userPwd, String userPhone) {  //这里只采用用户名和密码
        super();
        this.userName = userName;
        this.userPwd = userPwd;
        this.userPhone=userPhone;
    }

    public UserData(String assessmentType, String reason, String knowledgeGap,String strategy,String desc) {  //这里只采用用户名和密码
        super();

        this.assessmentType=assessmentType;
        this.reason=reason;
        this.knowledgeGap=knowledgeGap;
        this.strategy=strategy;
        this.desc=desc;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {     //输入用户密码
        this.assessmentType = assessmentType;
    }

    public String getReason() {
        return reason;
    }
    public void setCohort(String cohort) {
        this.cohort = cohort;
    }
    public String getCohort() {
        return cohort;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getKnowledgeGap() {
        return knowledgeGap;
    }
    public void setKnowledgeGap(String knowledgeGap) {
        this.knowledgeGap = knowledgeGap;
    }
    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
    public String getLearnLevel() {
        return learnLevel;
    }
    public void setLearnLevel(String learnLevel) {
        this.learnLevel = learnLevel;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
}