package com.example.demo.login.bean;


import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {

    private String image;
    private String name;
    private String gender;
    private String age;
    private String classfy;
    private String hosptial;
    private String number;


    private  String address;
    private  String  carclass;
    private String carpai;
    private  String cartime;
    private  String carnum;
    private String VIN;
        public MyUser() {
        super();
    }
    public MyUser(String number) {
            super();
            this.number=number;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getVIN() {
        return VIN;
    }

    public void setVIN(String VIN) {
        this.VIN = VIN;
    }

    public String getClassfy() {
        return classfy;
    }

    public void setClassfy(String classfy) {
        this.classfy = classfy;
    }

    public String getHosptial() {
        return hosptial;
    }

    public void setHosptial(String hosptial) {
        this.hosptial = hosptial;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarclass() {
        return carclass;
    }

    public void setCarclass(String carclass) {
        this.carclass = carclass;
    }

    public String getCarpai() {
        return carpai;
    }

    public void setCarpai(String carpai) {
        this.carpai = carpai;
    }

    public String getCartime() {
        return cartime;
    }

    public void setCartime(String cartime) {
        this.cartime = cartime;
    }

    public String getCarnum() {
        return carnum;
    }

    public void setCarnum(String carnum) {
        this.carnum = carnum;
    }
}
