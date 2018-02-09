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
public class PostResponse {
    private transient Ason json;
    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Post> posts = new ArrayList<>();

    /**
     * get full json object of get posts response
     * @return JSONObject
     */
    public Ason getJson() {
        return json;
    }

    public void setJson(Ason json) {
        this.json = json;
    }

    /**
     * is exist any other post
     * @return boolean
     */
    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    /**
     * string code for getting next posts with getPost method
     * @return String
     */
    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    /**
     * an array of post object
     * @return ArrayList
     */
    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void addPost(Post post) {
        posts.add(post);
    }
    
    
}
