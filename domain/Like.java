package com.moocall.moocall.domain;

import java.io.Serializable;

public class Like implements Serializable {
    private Integer userId;

    public Like(Integer userId) {
        setUserId(userId);
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
