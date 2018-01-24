package com.moocall.moocall.domain;

import java.io.Serializable;

public class User implements Serializable {
    private String city;
    private String country;
    private Integer id;
    private Boolean imageLoading;
    private String location;
    private Boolean moderator;
    private String nickname;
    private String picture;
    private String pictureUrl;

    public User(Integer id, String nickname, String pictureUrl, String country, String city, Boolean moderator) {
        setId(id);
        setNickname(nickname);
        setPictureUrl(pictureUrl);
        setCountry(country);
        setCity(city);
        setModerator(moderator);
        setImageLoading(Boolean.valueOf(false));
        setLocation(getCity() + ", " + getCountry());
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        if (pictureUrl.length() > 0) {
            this.pictureUrl = pictureUrl;
        }
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getModerator() {
        return this.moderator;
    }

    public void setModerator(Boolean moderator) {
        this.moderator = moderator;
    }

    public Boolean getImageLoading() {
        return this.imageLoading;
    }

    public void setImageLoading(Boolean imageLoading) {
        this.imageLoading = imageLoading;
    }

    private void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public String toString() {
        return "User{id=" + this.id + ", nickname='" + this.nickname + '\'' + ", pictureUrl='" + this.pictureUrl + '\'' + '}';
    }
}
