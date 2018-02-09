/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import java.awt.Dimension;

/**
 *
 * @author peter
 */
public class Post {
    private String id;
    private String typename;
    private String caption;
    private String shortcode;
    private int comment;
    private boolean commentsDisabled;
    private int timestamp;
    private Dimension dimensions = new Dimension();
    private String displayUrl;
    private int like;
    private String ownerId;
    private boolean isVideo;
    private int videoViewCount = 0;
    private String locationId = "0";
    private String videoUrl;
    private Post[] sidecars;

    
    /**
     * get String id of post on instagram to use in other method
     * @return String id
     */
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    /**
     * get type name of post like "GraphVideo" and "GraphImage"
     * @return String type
     */
    public String getTypename() {
        return typename;
    }

    protected void setTypename(String typename) {
        this.typename = typename;
    }

    /**
     * get caption of post
     * @return String caption
     */
    public String getCaption() {
        return caption;
    }

    protected void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * get short code of post that see in https://www.instagram.com/p/{ShortCode}/ and use in other method
     * @return String type
     */
    public String getShortcode() {
        return shortcode;
    }

    protected void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    /**
     * get comment count of post
     * @return int comment count
     */
    public int getComment() {
        return comment;
    }

    protected void setComment(int comment) {
        this.comment = comment;
    }

    /**
     * is disable comments in this post
     * @return boolean
     */
    public boolean isCommentsDisabled() {
        return commentsDisabled;
    }

    protected void setCommentsDisabled(boolean commentsDisabled) {
        this.commentsDisabled = commentsDisabled;
    }

    /**
     * get time of post in timestamp format
     * @return int timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    protected void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * get dimensions of media
     * @return Dimension
     */
    public Dimension getDimensions() {
        return dimensions;
    }

    protected void setDimensions(int width, int height) {
        dimensions.setSize(width, height);
    }

    /**
     * get display image link of post that shown when you in list of posts and don't select the post
     * @return String link of display image
     */
    public String getDisplayUrl() {
        return displayUrl;
    }

    protected void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    /**
     * get like count of post
     * @return int like count
     */
    public int getLike() {
        return like;
    }

    protected void setLike(int like) {
        this.like = like;
    }

    /**
     * get owner id of post
     * @return String ownerId id
     */
    public String getOwnerId() {
        return ownerId;
    }

    protected void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * get type of post that is video or not
     * @return boolean
     */
    public boolean isVideo() {
        return isVideo;
    }

    protected void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    /**
     * get view count of video. return 0 for images
     * @return Dimension
     */
    public int getVideoViewCount() {
        return videoViewCount;
    }

    protected void setVideoViewCount(int videoViewCount) {
        this.videoViewCount = videoViewCount;
    }

    /**
     * get location id of post
     * if location id is 0 then means not set and if location id is -1 then there is no location id founded for his post at all.
     * @return String
     */
    public String getLocationId() {
        return locationId;
    }

    protected void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    /**
     * get video url with mp4 extention
     * @return String
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * get sidecars for post with multi video or image
     * @return String
     */
    public Post[] getSidecars() {
        return sidecars;
    }

    public void setSidecars(Post[] sidecars) {
        this.sidecars = sidecars;
    }
   
}
