package com.gustavo.gimnasio.models;

public class UserModel {

    private String email;
    private String password;
    private String photoURL;

    public UserModel() {
    }

    public UserModel(String email, String password, String photoURL) {
        this.email = email;
        this.password = password;
        this.photoURL = photoURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "UserModel [email=" + email + ", password=" + password + ", photoURL=" + photoURL + "]";
    }

}
