/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

//import crawel.db_connect;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 *
 * @author peter
 */
public class jInsta {

    private final static String URL = "https://www.instagram.com/";
    private final static String POST_ADDRESS = URL + "graphql/query/?query_id=17888483320059182&variables=";
    private final static String COMMENT_ADDRESS = URL + "graphql/query/?query_id=17852405266163336";

    public User getUser(String username) {
        String link = URL + username + "/?__a=1";
        User user = new User();
        try {
            Response r = Jsoup.connect(link).ignoreContentType(true).execute();
            JSONObject object = new JSONObject(r.body());
            JSONObject userObj = object.getJSONObject("user");
            String image = userObj.getString("profile_pic_url_hd");
            String instaId = userObj.getString("id");
            int posts = userObj.getJSONObject("media").getInt("count");
            int follower = userObj.getJSONObject("followed_by").getInt("count");
            int following = userObj.getJSONObject("follows").getInt("count");
            String bio = userObj.getString("biography");
            user.setJson(object);
            user.setImage(image);
            user.setBio(bio);
            user.setFollower(follower);
            user.setFollowing(following);
            user.setInstaId(instaId);
            user.setPosts(posts);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(jInsta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    public PostResponse getPost(String instaId) {
        return getPost(instaId, 12, "");
    }

    public PostResponse getPost(String instaId, String after) {
        return getPost(instaId, 12, after);
    }

    public PostResponse getPost(String instaId, int first) {
        return getPost(instaId, first, "");
    }

    public PostResponse getPost(String instaId, int first, String after) {
        String link = POST_ADDRESS + "{\"id\":\"" + instaId + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        PostResponse postRespons = new PostResponse();
        try {
            Response r = Jsoup.connect(link).ignoreContentType(true).execute();
            JSONObject object = new JSONObject(r.body());
            JSONObject data = object.getJSONObject("data").getJSONObject("user").getJSONObject("edge_owner_to_timeline_media");
            boolean isNextPage = data.getJSONObject("page_info").getBoolean("has_next_page");
            postRespons.setHasNextPage(isNextPage);
            if(isNextPage){
                postRespons.setEndCursor(data.getJSONObject("page_info").getString("end_cursor"));
            }
            JSONArray posts = data.getJSONArray("edges");
            for (int i = 0; i < posts.length(); i++) {
                JSONObject postObj = posts.getJSONObject(i).getJSONObject("node");
                Post post = new Post();
                post.setCaption(postObj.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text"));
                post.setComment(postObj.getJSONObject("edge_media_to_comment").getInt("count"));
                post.setCommentsDisabled(postObj.getBoolean("comments_disabled"));
                JSONObject dimensions = postObj.getJSONObject("dimensions");
                post.setDimensions(dimensions.getInt("width"), dimensions.getInt("height"));
                post.setDisplayUrl(postObj.getString("display_url"));
                post.setId(postObj.getString("id"));
                post.setIsVideo(postObj.getBoolean("is_video"));
                post.setLike(postObj.getJSONObject("edge_media_preview_like").getInt("count"));
                post.setOwnerId(postObj.getJSONObject("owner").getString("id"));
                post.setShortcode(postObj.getString("shortcode"));
                post.setTimestamp(postObj.getInt("taken_at_timestamp"));
                post.setTypename(postObj.getString("__typename"));
                if(post.isVideo){
                    post.setVideoViewCount(postObj.getInt("video_view_count"));
                }
                postRespons.addPost(post);
            }
            postRespons.setJson(data);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(jInsta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return postRespons;
    }

    public CommentResponse getComment(String shortcode) {
        return getComment(shortcode, 12, "");
    }

    public CommentResponse getComment(String shortcode, String after) {
        return getComment(shortcode, 12, after);
    }

    public CommentResponse getComment(String shortcode, int first) {
        return getComment(shortcode, first, "");
    }
    
    public CommentResponse getComment(String shortcode, int first, String after) {
        String link = COMMENT_ADDRESS + "&shortcode=" + shortcode + "&first=" + first + (after.length() > 0 ? "&after=" + after : "");
        System.out.println(link);
        CommentResponse commentResponse = new CommentResponse();
        try {
            Response r = Jsoup.connect(link).ignoreContentType(true).execute();
            JSONObject object = new JSONObject(r.body());
            JSONObject data = object.getJSONObject("data").getJSONObject("shortcode_media").getJSONObject("edge_media_to_comment");
            commentResponse.setCount(data.getInt("count"));
            commentResponse.setHasNextPage(data.getJSONObject("page_info").getBoolean("has_next_page"));
            if(commentResponse.hasNextPage()){
                commentResponse.setEndCursor(data.getJSONObject("page_info").getString("end_cursor"));
            }
            JSONArray comments = data.getJSONArray("edges");
            for (int i = 0; i < comments.length(); i++) {
                JSONObject commentObj = comments.getJSONObject(i).getJSONObject("node");
                Comment comment = new Comment();
                comment.setId(commentObj.getString("id"));
                comment.setText(commentObj.getString("text"));
                comment.setTimestamp(commentObj.getInt("created_at"));
                comment.setOwnerId(commentObj.getJSONObject("owner").getString("id"));
                comment.setOwnerProfilePicUrl(commentObj.getJSONObject("owner").getString("profile_pic_url"));
                comment.setOwnerUsername(commentObj.getJSONObject("owner").getString("username"));
                commentResponse.addComments(comment);
            }
            commentResponse.setJson(data);
        } catch (JSONException | IOException ex) {
            Logger.getLogger(jInsta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return commentResponse;
    }

}
