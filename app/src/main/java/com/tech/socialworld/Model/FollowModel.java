package com.tech.socialworld.Model;

public class FollowModel {

    private String followedBy;
    private long followAt;


    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowAt() {
        return followAt;
    }

    public void setFollowAt(long followAt) {
        this.followAt = followAt;
    }
}
