/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import com.afollestad.ason.Ason;
import java.util.ArrayList;

/**
 *
 * @author peter
 */
public class CommentResponse {
    private transient Ason json;
    private int count;
    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Comment> comments = new ArrayList<>();

    
    /**
     * get full json object of get comments response
     * @return JSONObject
     */
    public Ason getJson() {
        return json;
    }

    public void setJson(Ason json) {
        this.json = json;
    }

    /**
     * get count of comment on a post
     * @return int
     */
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * is exist any other comment
     * @return boolean
     */
    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * string code for getting next comments with getComment method
     * @return String
     */
    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    /**
     * an array of comment object
     * @return ArrayList
     */
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void addComments(Comment comment) {
        comments.add(comment);
    }
    
    
}
