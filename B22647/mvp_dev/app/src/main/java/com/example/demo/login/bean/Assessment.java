package com.example.demo.login.bean;

import cn.bmob.v3.BmobObject;

public class Assessment extends BmobObject {
    public  String userid;
    private String userName;
    public String assessmentType;
    private String topic;
    public String kg;
    public String newKG;
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
    public String getUserid() {                //获取用户密码
        return userid;
    }
    //设置用户类型
    public void setUserid(String userid) {     //输入用户密码
        this.userid = userid;
    }
    //获取用户密码
    public String getKg() {                //获取用户密码
        return kg;
    }
    //设置用户密码
    public void setKg(String kg) {     //输入用户密码
        this.kg = kg;
    }
    //获取用户邮箱
    public String getNewKG() {                //获取用户密码
        return newKG;
    }
    //设置用户邮箱
    public void setNewKG(String newKG) {     //输入用户密码
        this.newKG = newKG;
    }

    public String getTopic() {                   //获取用户ID号
        return topic;
    }

    public void setTopic(String topic) {       //设置用户ID号
        this.topic = topic;
    }

    public Assessment() {  //这里只采用用户名和密码
        super();
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {     //输入用户密码
        this.assessmentType = assessmentType;
    }
}