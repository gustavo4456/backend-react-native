package com.gustavo.gimnasio.models;

public class PostUserModel {
    private Long id;
    private String title;
    private String content;
    private String image;
    private Long idUser;

    private String email;
    private String photoURL;
    
    public PostUserModel() {
    }

    public PostUserModel(Long id, String title, String content, String image, Long idUser, String email,
            String photoURL) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.idUser = idUser;
        this.email = email;
        this.photoURL = photoURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public String toString() {
        return "PostUserModel [content=" + content + ", email=" + email + ", id=" + id + ", idUser=" + idUser
                + ", image=" + image + ", photoURL=" + photoURL + ", title=" + title + "]";
    }

    
}
