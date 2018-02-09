/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

/**
 *
 * @author peter
 */
public class Comment {
    private String id;
    private String text;
    private int timestamp;
    private String ownerId;
    private String ownerProfilePicUrl;
    private String ownerUsername;

    /**
     * get id of comment on instagram
     * @return String id
     */
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    /**
     * get text of comment
     * @return String text
     */
    public String getText() {
        return text;
    }

    protected void setText(String text) {
        this.text = text;
    }

    /**
     * get time of comment in timestamp format
     * @return int timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    protected void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * get owner id of comment
     * @return String owner id
     */
    public String getOwnerId() {
        return ownerId;
    }

    protected void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * get owner profile picture of comment
     * @return String profile picture url
     */
    public String getOwnerProfilePicUrl() {
        return ownerProfilePicUrl;
    }

    protected void setOwnerProfilePicUrl(String ownerProfilePicUrl) {
        this.ownerProfilePicUrl = ownerProfilePicUrl;
    }

    /**
     * get owner username of comment
     * @return String username
     */
    public String getOwnerUsername() {
        return ownerUsername;
    }

    protected void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
    
    
}
