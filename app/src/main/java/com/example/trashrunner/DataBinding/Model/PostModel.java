package com.example.trashrunner.DataBinding.Model;

import java.util.List;

public class PostModel {

    private String name;
    private String email;
    private String content;
    private int commentCount;
    private int likeCount;
    private String timestamp;
    private String profilePic;
    private List<String> images;

    public PostModel() {
    }

    public PostModel(
            String name,
            String email,
            String content,
            int commentCount,
            int likeCount,
            String timestamp,
            String profilePic,
            List<String> images) {

        this.name = name;
        this.email = email;
        this.content = content;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.timestamp = timestamp;
        this.profilePic = profilePic;
        this.images = images;

    }

    // Getters and Setters for each field
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
