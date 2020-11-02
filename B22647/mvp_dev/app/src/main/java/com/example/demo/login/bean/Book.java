package com.example.demo.login.bean;



import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Book extends BmobObject implements Serializable {
    private int id;
    private String userid;
    private String author;
   private String desc;
    private String mealname;
    private String type;
    private BmobFile image;
    private String admin_id;
    private String from;

    public Book() {
        super();
    }
    public Book(String userid, String mealname, String type, String author, String desc, BmobFile image) {
        super();
        this.userid = userid;
        this.author = author;
        this.desc = desc;
        this.mealname = mealname;
        this.type=type;
        this.image = image;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}

    public String getUserid(){return userid;}
    public void setUserid(String userid){this.userid=userid;}

    public String getAuthor(){return author;}
    public void setAuthor(String author){this.author = author;}

    public String getDesc(){return desc;}
    public void setDesc(String desc){this.desc = desc;}

    public String getMealname(){return mealname;}
    public void setMealname(String mealname){this.mealname = mealname;}

    public String getType(){return type;}
    public void setType(String type){this.type = type;}

    public BmobFile getImage(){return image;}
    public void setImage(BmobFile image){this.image = image;}

    public String getAdmin_id(){return admin_id;}
    public void setAdmin_id(String admin_id){this.admin_id = admin_id;}

    public String getFrom(){return from;}
    public void setFrom(String from){this.from = from;}
}
