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
public class PostResponse {
    private JSONObject json;
    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Post> posts = new ArrayList<>();

    /**
     * get full json object of get posts response
     * @return JSONObject
     */
    public JSONObject getJson() {
        return json;
    }

    protected void setJson(JSONObject json) {
        this.json = json;
    }

    /**
     * is exist any other post
     * @return boolean
     */
    public boolean hasNextPage() {
        return hasNextPage;
    }

    protected void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * string code for getting next posts with getPost method
     * @return String
     */
    public String getEndCursor() {
        return endCursor;
    }

    protected void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    /**
     * an array of post object
     * @return ArrayList
     */
    public ArrayList<Post> getPosts() {
        return posts;
    }

    protected void addPost(Post post) {
        posts.add(post);
    }
    
    
}
