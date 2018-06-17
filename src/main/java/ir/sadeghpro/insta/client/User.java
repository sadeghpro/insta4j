/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import com.afollestad.ason.Ason;

/**
 *
 * @author peter
 */
public class User {

    private transient Ason json;
    private String image;
    private String instaId;
    private int posts;
    private int follower;
    private int following;
    private String bio;
    private String username;
    private String fullname;
    private String externalUrl;
    private boolean isPrivate;
    private boolean isVerified;
    private boolean followedByViewer;
    private boolean requestedByViewer;
    private boolean followsViewer;
    private boolean requestedViewer;

    /**
     * get full json object of user
     *
     * @return JSONObject
     */
    public Ason getJson() {
        return json;
    }

    public void setJson(Ason json) {
        this.json = json;
    }

    /**
     * get hd image link of user profile
     *
     * @return String Link of instagram
     */
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * get id of user in instagram
     *
     * @return String user id
     */
    public String getInstaId() {
        return instaId;
    }

    public void setInstaId(String instaId) {
        this.instaId = instaId;
    }

    /**
     * get post count of user
     *
     * @return int post count
     */
    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    /**
     * get follower count of user
     *
     * @return int follower count
     */
    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    /**
     * get following count of user
     *
     * @return int following count
     */
    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    /**
     * get user biography
     *
     * @return String bio
     */
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * get username of account
     *
     * @return String username
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get user full name
     *
     * @return String name
     */
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * get user external url
     *
     * @return String url
     */
    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    /**
     * is user private
     *
     * @return boolean is private
     */
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    /**
     * is user followed by this account if you login
     *
     * @return boolean is followed
     */
    public boolean isFollowedByViewer() {
        return followedByViewer;
    }

    public void setFollowedByViewer(boolean followedByViewer) {
        this.followedByViewer = followedByViewer;
    }

    /**
     * is you request for following or not if you login
     *
     * @return boolean is followed
     */
    public boolean isRequestedByViewer() {
        return requestedByViewer;
    }

    public void setRequestedByViewer(boolean requestedByViewer) {
        this.requestedByViewer = requestedByViewer;
    }

    public boolean isFollowsViewer() {
        return followsViewer;
    }

    public void setFollowsViewer(boolean followsViewer) {
        this.followsViewer = followsViewer;
    }

    public boolean isRequestedViewer() {
        return requestedViewer;
    }

    public void setRequestedViewer(boolean requestedViewer) {
        this.requestedViewer = requestedViewer;
    }
}
