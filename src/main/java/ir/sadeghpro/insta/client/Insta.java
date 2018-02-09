/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import com.afollestad.ason.Ason;
import com.afollestad.ason.AsonArray;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author peter
 */
public class Insta {

    private final static String URL = "https://www.instagram.com/";
    private final static String LOGIN_ADDRESS = URL + "accounts/login/?force_classic_login";
    private final static String POST_ADDRESS = URL + "graphql/query/?query_id=17888483320059182&variables=";
    private final static String COMMENT_ADDRESS = URL + "graphql/query/?query_id=17852405266163336";
    private final static String FOLLOWER_ADDRESS = URL + "graphql/query/?query_id=17851374694183129&variables=";
    private final static String LOCATION_ADDRESS = URL + "explore/locations/";
    private Map<String, String> cookie;
    private String ip = "";
    private int port = 0;

    public Insta() {

    }

    public Insta(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public User getUser(String username) throws IOException {
        String link = URL + username + "/?__a=1";
        User user = new User();
        Connection c = Jsoup.connect(link).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason object = new Ason(r.body());
        Ason userObj = object.getJsonObject("user");
        user.setJson(object);
        user.setImage(userObj.getString("profile_pic_url_hd"));
        user.setBio(userObj.getString("biography", ""));
        user.setFollower(userObj.getInt("followed_by.count"));
        user.setFollowing(userObj.getInt("follows.count"));
        user.setInstaId(userObj.getString("id"));
        user.setPosts(userObj.getInt("media.count"));
        user.setUsername(username);
        user.setFullname(userObj.getString("full_name"));
        user.setExternalUrl(userObj.getString("external_url", ""));
        return user;
    }

    public Post getPost(String shortcode) throws IOException {
        String link = URL + "p/" + shortcode + "/?__a=1";
        Connection c = Jsoup.connect(link).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason postObj = new Ason(r.body()).getJsonObject("graphql.shortcode_media");
        Post post = returnPost(postObj);
        post.setCaption(postObj.getString("edge_media_to_caption.edges.$0.node.text", ""));
        post.setComment(postObj.getInt("edge_media_to_comment.count"));
        post.setCommentsDisabled(postObj.getBool("comments_disabled"));
        post.setLike(postObj.getInt("edge_media_preview_like.count"));
        post.setOwnerId(postObj.getString("owner.id"));
        post.setTimestamp(postObj.getInt("taken_at_timestamp"));
        post.setLocationId(postObj.getString("location.id", "-1"));
        if (post.isVideo()) {
            post.setVideoViewCount(postObj.getInt("video_view_count"));
            post.setVideoUrl(postObj.getString("video_url"));
        }
        if (post.getTypename().equals("GraphSidecar")) {
            AsonArray<Ason> sidecarsObj = postObj.getJsonArray("edge_sidecar_to_children.edges");
            Post[] sidecars = new Post[sidecarsObj.size()];
            for (int i = 0; i < sidecarsObj.size(); i++) {
                Ason sidecar = sidecarsObj.get(i).getJsonObject("node");
                sidecars[i] = returnPost(sidecar);
            }
            post.setSidecars(sidecars);

        }
        return post;
    }

    public PostResponse getUserPosts(String instaId) throws IOException {
        return Insta.this.getUserPosts(instaId, 12, "");
    }

    public PostResponse getUserPosts(String instaId, String after) throws IOException {
        return Insta.this.getUserPosts(instaId, 12, after);
    }

    public PostResponse getUserPosts(String instaId, int first) throws IOException {
        return Insta.this.getUserPosts(instaId, first, "");
    }

    public PostResponse getUserPosts(String instaId, int first, String after) throws IOException {
        String link = POST_ADDRESS + "{\"id\":\"" + instaId + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        PostResponse postRespons = new PostResponse();
        Connection c = Jsoup.connect(link).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason data = new Ason(r.body()).getJsonObject("data.user.edge_owner_to_timeline_media");
        boolean isNextPage = data.getBool("page_info.has_next_page");
        postRespons.setHasNextPage(isNextPage);
        if (isNextPage) {
            postRespons.setEndCursor(data.getString("page_info.end_cursor"));
        }
        AsonArray<Ason> posts = data.getJsonArray("edges");
        for (int i = 0; i < posts.size(); i++) {
            Ason postObj = posts.get(i).getJsonObject("node");
            Post post = new Post();
            post.setCaption(postObj.getString("edge_media_to_caption.edges.$0.node.text", ""));
            post.setComment(postObj.getInt("edge_media_to_comment.count"));
            post.setCommentsDisabled(postObj.getBool("comments_disabled"));
            post.setDimensions(postObj.getInt("dimensions.width"), postObj.getInt("dimensions.height"));
            post.setDisplayUrl(postObj.getString("display_url"));
            post.setId(postObj.getString("id"));
            post.setIsVideo(postObj.getBool("is_video"));
            post.setLike(postObj.getInt("edge_media_preview_like.count"));
            post.setOwnerId(postObj.getString("owner.id"));
            post.setShortcode(postObj.getString("shortcode"));
            post.setTimestamp(postObj.getInt("taken_at_timestamp"));
            post.setTypename(postObj.getString("__typename"));
            if (post.isVideo()) {
                post.setVideoViewCount(postObj.getInt("video_view_count"));
            }
            postRespons.addPost(post);
        }
        postRespons.setJson(data);
        return postRespons;
    }

    public CommentResponse getComment(String shortcode) throws IOException {
        return getComment(shortcode, 12, "");
    }

    public CommentResponse getComment(String shortcode, String after) throws IOException {
        return getComment(shortcode, 12, after);
    }

    public CommentResponse getComment(String shortcode, int first) throws IOException {
        return getComment(shortcode, first, "");
    }

    public CommentResponse getComment(String shortcode, int first, String after) throws IOException {
        String link = COMMENT_ADDRESS + "&shortcode=" + shortcode + "&first=" + first + (after.length() > 0 ? "&after=" + after : "");
        CommentResponse commentResponse = new CommentResponse();
        Connection c = Jsoup.connect(link).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason data = new Ason(r.body()).getJsonObject("data.shortcode_media.edge_media_to_comment");
        commentResponse.setCount(data.getInt("count"));
        commentResponse.setHasNextPage(data.getBool("page_info.has_next_page"));
        if (commentResponse.hasNextPage()) {
            commentResponse.setEndCursor(data.getString("page_info.end_cursor"));
        }
        AsonArray<Ason> comments = data.getJsonArray("edges");
        for (int i = 0; i < comments.size(); i++) {
            Ason commentObj = comments.get(i).getJsonObject("node");
            Comment comment = new Comment();
            comment.setId(commentObj.getString("id"));
            comment.setText(commentObj.getString("text"));
            comment.setTimestamp(commentObj.getInt("created_at"));
            comment.setOwnerId(commentObj.getString("owner.id"));
            comment.setOwnerProfilePicUrl(commentObj.getString("owner.profile_pic_url"));
            comment.setOwnerUsername(commentObj.getString("owner.username"));
            comment.setPostShortCode(shortcode);
            commentResponse.addComments(comment);
        }
        commentResponse.setJson(data);
        return commentResponse;
    }

    public boolean login(String username, String password) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Accept-Encoding", "deflate, br");
        headers.put("Connection", "keep-alive");
        headers.put("cache-control", "max-age=0");
        headers.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/50.0.2661.102 Chrome/50.0.2661.102 Safari/537.36");
        Connection c = Jsoup.connect(LOGIN_ADDRESS).headers(headers).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response loginPage = c.execute();
        cookie = loginPage.cookies();
        Document d = loginPage.parse();
        Elements inputs = d.getElementsByAttributeValue("type", "hidden");
        Map<String, String> post = new HashMap<>();
        post.put("username", username);
        post.put("password", password);
        inputs.forEach((input) -> {
            post.put(input.attr("name"), input.attr("value"));
        });
        headers.put("origin", "https://www.instagram.com");
        headers.put("authority", "www.instagram.com");
        headers.put("upgrade-insecure-requests", "1");
        headers.put("Host", "www.instagram.com");
        headers.put("content-type", "application/x-www-form-urlencoded");
        headers.put("cache-control", "max-age=0");
        c = Jsoup.connect(LOGIN_ADDRESS).cookies(cookie).ignoreContentType(true).data(post)
                .referrer(LOGIN_ADDRESS).headers(headers).method(Connection.Method.POST);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response login = c.execute();
        cookie.putAll(login.cookies());
        return login.statusCode() == 200;
    }

    public void getFollowers(String instaId) throws Exception {
        getFollowers(instaId, 12, "");
    }

    public void getFollowers(String instaId, String after) throws Exception {
        getFollowers(instaId, 12, after);
    }

    public void getFollowers(String instaId, int first) throws Exception {
        getFollowers(instaId, first, "");
    }

    public void getFollowers(String instaId, int first, String after) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String link = FOLLOWER_ADDRESS + "{\"id\":\"" + instaId + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        Connection c = Jsoup.connect(link).cookies(cookie).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason object = new Ason(r.body());
        System.out.println(r.body());
    }

    public Location getLocation(String locationId) throws IOException {
        String link = LOCATION_ADDRESS + locationId + "/?__a=1";
        Location location = new Location();
        Connection c = Jsoup.connect(link).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason obj = new Ason(r.body()).getJsonObject("location");
        location.setHasPublicPage(obj.getBool("has_public_page"));
        location.setId(locationId);
        location.setName(obj.getString("name"));
        location.setSlug(obj.getString("slug"));
        location.setLat(obj.getDouble("lat"));
        location.setLng(obj.getDouble("lng"));
        return location;
    }

    private Post returnPost(Ason postObj) {
        Post post = new Post();
        post.setDimensions(postObj.getInt("dimensions.width"), postObj.getInt("dimensions.height"));
        post.setDisplayUrl(postObj.getString("display_url"));
        post.setId(postObj.getString("id"));
        post.setIsVideo(postObj.getBool("is_video"));
        post.setShortcode(postObj.getString("shortcode"));
        post.setTypename(postObj.getString("__typename"));
        if (post.isVideo()) {
            post.setVideoViewCount(postObj.getInt("video_view_count"));
            post.setVideoUrl(postObj.getString("video_url"));
        }
        return post;
    }
}
