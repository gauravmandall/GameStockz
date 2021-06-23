package com.gamestockz.data.modals;

public class Users {
    String name,password, mobilenumber,referralcode,otp,wallet,email;

    public Users(String name, String password, String mobilenumber, String referralcode, String wallet, String email, String otp) {
        this.name = name;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.referralcode = referralcode;
        this.wallet =wallet;
        this.email=email;
    }
    public Users(){}
    //Signup constructor
    public Users(String name, String password, String mobilenumber, String referralcode, String wallet, String email) {
        this.name = name;
        this.password = password;
        this.mobilenumber = mobilenumber;
        this.referralcode = referralcode;
        this.wallet = wallet;
        this.email=email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getReferralcode() {
        return referralcode;
    }

    public void setReferralcode(String referralcode) {
        this.referralcode = referralcode;
    }
    public String getwallet(){
        return wallet;
    }
    public void setwallet(String wallet){
        this.wallet=wallet;
    }
    public String getemail(){return email;}
    public void setemail(){this.email=email;}




    public String getotp(){
        return otp;
    }
    public void setotp(String otp){
        this.otp=otp;
    }
}
