/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter
 */
public class Post {
    private String id;
    private TypeName typename;
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
    private Location location;
    private String videoUrl;
    private Post[] sidecars;
    private List<Comment> comments = new ArrayList<>();

    public enum TypeName{
        Sidecar("GraphSidecar"),
        Video("GraphVideo"),
        Image("GraphImage");
        TypeName(String name){
            this.name=name;
        }
        private String name;

        public String toString(){
            return this.name;
        }
    }


    /**
     * get String id of post on instagram to use in other method
     * @return String id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * get type name of post like "GraphVideo" and "GraphImage"
     * @return String type
     */
    public TypeName getTypename() {
        return typename;
    }

    public void setTypename(TypeName typename) {
        this.typename = typename;
    }

    public void setTypename(String typename){
        switch (typename){
            case "GraphImage":
                setTypename(Post.TypeName.Image);
                break;
            case "GraphVideo":
                setTypename(Post.TypeName.Video);
                break;
            case "GraphSidecar":
                setTypename(Post.TypeName.Sidecar);
                break;

        }
    }

    /**
     * get caption of post
     * @return String caption
     */
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * get short code of post that see in https://www.instagram.com/p/{ShortCode}/ and use in other method
     * @return String type
     */
    public String getShortcode() {
        return shortcode;
    }

    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }

    /**
     * get comment count of post
     * @return int comment count
     */
    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    /**
     * is disable comments in this post
     * @return boolean
     */
    public boolean isCommentsDisabled() {
        return commentsDisabled;
    }

    public void setCommentsDisabled(boolean commentsDisabled) {
        this.commentsDisabled = commentsDisabled;
    }

    /**
     * get time of post in timestamp format
     * @return int timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * get dimensions of media
     * @return Dimension
     */
    public Dimension getDimensions() {
        return dimensions;
    }

    public void setDimensions(int width, int height) {
        dimensions.setSize(width, height);
    }

    /**
     * get display image link of post that shown when you in list of posts and don't select the post
     * @return String link of display image
     */
    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    /**
     * get like count of post
     * @return int like count
     */
    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    /**
     * get owner id of post
     * @return String ownerId id
     */
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * get type of post that is video or not
     * @return boolean
     */
    public boolean isVideo() {
        return isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

    /**
     * get view count of video. return 0 for images
     * @return Dimension
     */
    public int getVideoViewCount() {
        return videoViewCount;
    }

    public void setVideoViewCount(int videoViewCount) {
        this.videoViewCount = videoViewCount;
    }

    /**
     * get location id of post
     * if location id is 0 then means not set and if location id is -1 then there is no location id founded for his post at all.
     * @return String
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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

    public List<Comment> getComments() {
        return comments;
    }

    public void addComments(List<Comment> comments) {
        this.comments.addAll(comments);
    }

    public void addComments(Comment comment) {
        this.comments.add(comment);
    }
}
