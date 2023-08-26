package com.example.studentaccommodation;

import com.google.firebase.Timestamp;
import java.util.Objects;

public class Post {
    private String title;
    private String description;
    private String image1;
    private String image2;
    private String fName;
    private String phone;
    private String ownerId;
    private Timestamp timestamp;
    private String price;
    private String university;
    private String location;
    public Post() {
    }

    public Post(String title, String description, String image1, String image2,String fName,String phone,String ownerId,Timestamp timestamp) {
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.fName = fName;
        this.phone = phone;
        this.ownerId = ownerId;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}


