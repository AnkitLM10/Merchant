package com.example.merchant;

public class RegisterUser {
    String name,gender,email,password,phone,dateOfBirth;

    public RegisterUser(String name,String gender, String email, String password ,String phone , String dateOfBirth){
        this.name = name;
        this.gender=gender;
        this.email=email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.phone = phone;

    }
}
