/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import org.json.JSONObject;

/**
 *
 * @author peter
 */
public class User {

    private JSONObject json;
    private String image;
    private String instaId;
    private int posts;
    private int follower;
    private int following;
    private String bio;

    /**
     * get full json object of user
     * @return JSONObject
     */
    public JSONObject getJson() {
        return json;
    }

    protected void setJson(JSONObject json) {
        this.json = json;
    }

    /**
     * get hd image link of user profile
     * @return String Link of instagram
     */
    public String getImage() {
        return image;
    }

    protected void setImage(String image) {
        this.image = image;
    }

    /**
     * get id of user in instagram
     * @return String user id
     */
    public String getInstaId() {
        return instaId;
    }

    protected void setInstaId(String instaId) {
        this.instaId = instaId;
    }

    /**
     * get post count of user
     * @return int post count
     */
    public int getPosts() {
        return posts;
    }

    protected void setPosts(int posts) {
        this.posts = posts;
    }

    /**
     * get follower count of user
     * @return int follower count
     */
    public int getFollower() {
        return follower;
    }

    protected void setFollower(int follower) {
        this.follower = follower;
    }

    /**
     * get following count of user
     * @return int following count
     */
    public int getFollowing() {
        return following;
    }

    protected void setFollowing(int following) {
        this.following = following;
    }

    /**
     * get user biography
     * @return String bio
     */
    public String getBio() {
        return bio;
    }

    protected void setBio(String bio) {
        this.bio = bio;
    }

}
