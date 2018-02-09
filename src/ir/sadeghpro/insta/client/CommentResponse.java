/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author peter
 */
public class CommentResponse {
    private JSONObject json;
    private int count;
    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Comment> comments = new ArrayList<>();

    
    /**
     * get full json object of get comments response
     * @return JSONObject
     */
    public JSONObject getJson() {
        return json;
    }

    protected void setJson(JSONObject json) {
        this.json = json;
    }

    /**
     * get count of comment on a post
     * @return int
     */
    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    /**
     * is exist any other comment
     * @return boolean
     */
    public boolean hasNextPage() {
        return hasNextPage;
    }

    protected void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * string code for getting next comments with getComment method
     * @return String
     */
    public String getEndCursor() {
        return endCursor;
    }

    protected void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    /**
     * an array of comment object
     * @return ArrayList
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    protected void addComments(Comment comment) {
        comments.add(comment);
    }
    
    
}
