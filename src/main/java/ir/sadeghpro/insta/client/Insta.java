/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.sadeghpro.insta.client;

import com.afollestad.ason.Ason;
import com.afollestad.ason.AsonArray;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * @author peter
 */
public class Insta {

    private Map<String, String> cookie = new HashMap<>();
    private boolean login;
    private String username;
    private String password;
    private String csrf;
    private String rhx_gis;
    private String userAgent;
    private String ip = "";
    private int port = 0;
    private User user;

    public Insta() throws IOException {
        userAgent = S.userAgents[new Random().nextInt(S.userAgents.length)];
        Response r = Jsoup.connect(S.URL).userAgent(userAgent).execute();
        Pattern p = Pattern.compile("_sharedData[\\s\\S]*?;</script>");
        Matcher m = p.matcher(r.body());
        if (m.find()) {
            Ason object = new Ason(m.group(0).substring(14, m.group(0).lastIndexOf(";")));
            rhx_gis = object.getString("rhx_gis");
            csrf = object.get("config.csrf_token");
        } else {
            System.out.println("NO MATCH");
        }
    }

    public Insta(String ip, int port) throws IOException {
        this();
        this.ip = ip;
        this.port = port;
    }

    public Insta(String username, String password) throws IOException {
        userAgent = S.userAgents[new Random().nextInt(S.userAgents.length)];
        login(username, password);
    }

    public Insta(String username, String password, String ip, int port) throws IOException {
        userAgent = S.userAgents[new Random().nextInt(S.userAgents.length)];
        this.ip = ip;
        this.port = port;
        login(username, password);
    }

    public User getUser(String username) throws IOException {
        String link = S.URL + username + "/";
        User user = new User();
        Connection c = Jsoup.connect(link).ignoreHttpErrors(true).userAgent(userAgent).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
        }
        Response r = c.execute();
        if(r.statusCode()==200){
            if (!cookie.isEmpty()) {
                for (Map.Entry<String, String> entry : r.cookies().entrySet()) {
                    if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                        cookie.remove(entry.getKey());
                    } else {
                        cookie.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            Pattern p = Pattern.compile("_sharedData[\\s\\S]*?;</script>");
            Matcher m = p.matcher(r.body());
            if (m.find()) {
                try{
                    Ason object = new Ason(m.group(0).substring(14, m.group(0).lastIndexOf(";")));
                    Ason userObj = object.getJsonObject("entry_data.ProfilePage.$0.graphql.user");
                    user.setJson(object);
                    user.setImage(userObj.getString("profile_pic_url_hd"));
                    user.setBio(userObj.getString("biography", ""));
                    user.setFollower(userObj.getInt("edge_followed_by.count"));
                    user.setFollowing(userObj.getInt("edge_follow.count"));
                    user.setInstaId(userObj.getString("id"));
                    user.setPosts(userObj.getInt("edge_owner_to_timeline_media.count"));
                    user.setUsername(username);
                    user.setFullname(userObj.getString("full_name"));
                    user.setExternalUrl(userObj.getString("external_url", ""));
                    user.setFollowedByViewer(userObj.getBool("followed_by_viewer"));
                    user.setIsPrivate(userObj.getBool("is_private"));
                    user.setIsVerified(userObj.getBool("is_verified"));
                    user.setRequestedByViewer(userObj.getBool("requested_by_viewer"));
                    user.setFollowsViewer(userObj.getBool("follows_viewer"));
                    user.setRequestedViewer(userObj.getBool("has_requested_viewer"));
                    return user;
                } catch (NullPointerException ex){
                    return null;
                }
            } else {
                System.out.println("NO MATCH");
                return null;
            }
        } else{
            return null;
        }

    }

    public String getUsernameById(String instaId) throws IOException {
        String link = S.USER_BY_ID_ADDRESS + instaId + "/info/";
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason json = new Ason(r.body());
        return json.getString("user.username", "");
    }

    public Post getPost(String shortcode) throws IOException {
        String link = S.URL + "p/" + shortcode + "/?__a=1";
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
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
        if(postObj.has("location")){
            Location location = new Location();
            location.setId(postObj.getString("location.id"));
            location.setHasPublicPage(postObj.getBool("location.has_public_page"));
            location.setName(postObj.getString("location.name"));
            location.setSlug(postObj.getString("location.slug"));
            post.setLocation(location);
        }
        post.setId(postObj.getString("id"));
        if (post.isVideo()) {
            post.setVideoViewCount(postObj.getInt("video_view_count"));
            post.setVideoUrl(postObj.getString("video_url"));
        }
        if (post.getTypename() == Post.TypeName.Sidecar) {
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
        String variable = "{\"id\":\"" + instaId + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        String link = S.POST_ADDRESS + variable;
        PostResponse postRespons = new PostResponse();
        String gis = md5(rhx_gis + ":" + variable);
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true).header("X-Instagram-GIS", gis).header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
        }
        c.cookie("csrftoken", csrf);
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
            postRespons.addPost(returnTimeLinePost(postObj));
        }
        postRespons.setJson(data);
        return postRespons;
    }

    public CommentResponse getComment(String shortcode) throws IOException {
        return getComment(shortcode, 28, "");
    }

    public CommentResponse getComment(String shortcode, String after) throws IOException {
        return getComment(shortcode, 28, after);
    }

    public CommentResponse getComment(String shortcode, int first) throws IOException {
        return getComment(shortcode, first, "");
    }

    public CommentResponse getComment(String shortcode, int first, String after) throws IOException {
        String variable = "{\"shortcode\":\"" + shortcode + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"}" : "}");
        String link = S.COMMENT_ADDRESS + variable;
        CommentResponse commentResponse = new CommentResponse();
        String gis = md5(rhx_gis + ":" + variable);
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true).header("X-Instagram-GIS", gis).header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
        }
        c.cookie("csrftoken", csrf);
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

    public Ason login(String username, String password) throws IOException {
        this.username = username;
        this.password = password;
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Connection", "keep-alive");
        headers.put("Upgrade-Insecure-Requests", "1");
        headers.put("Host", "www.instagram.com");
        Connection c = Jsoup.connect(S.URL).userAgent(userAgent).headers(headers).ignoreContentType(true);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response loginPage = c.execute();
        for (Map.Entry<String, String> entry : loginPage.cookies().entrySet()) {
            if (entry.getValue().isEmpty() || entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        Pattern p = Pattern.compile("_sharedData[\\s\\S]*?;</script>");
        Matcher m = p.matcher(loginPage.body());
        if (m.find()) {
            Ason object = new Ason(m.group(0).substring(14, m.group(0).lastIndexOf(";")));
            csrf = object.get("config.csrf_token");
        } else {
            System.out.println("NO MATCH");
        }
        Map<String, String> post = new HashMap<>();
        post.put("username", username);
        post.put("password", password);
        post.put("queryParams", "{}");
        headers.put("origin", "https://www.instagram.com");
        headers.put("Host", "www.instagram.com");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("X-CSRFToken", csrf);
        headers.put("X-Instagram-AJAX", "1");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("Accept", "*/*");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "en-US,en;q=0.9");
//        headers.put("Referrer", S.URL);
//        headers.put("User-Agent", userAgent);
        c = Jsoup.connect(S.LOGIN_ADDRESS).userAgent(userAgent).cookies(cookie).ignoreContentType(true).data(post)
                .referrer(S.URL).headers(headers).method(Connection.Method.POST);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response login = c.ignoreHttpErrors(true).execute();
        System.out.println(login.statusCode());
        for (Map.Entry<String, String> entry : login.cookies().entrySet()) {
            if (entry.getValue().isEmpty() || entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        Ason json = new Ason(login.body());
        if (json.getBool("authenticated", false)) {
            Response redirect = Jsoup.connect(S.URL).userAgent(userAgent).cookies(cookie).referrer(S.URL + username + "/").execute();
            for (Map.Entry<String, String> entry : redirect.cookies().entrySet()) {
                if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                    cookie.remove(entry.getKey());
                } else {
                    cookie.put(entry.getKey(), entry.getValue());
                }
            }
            m = p.matcher(redirect.body());
            if (m.find()) {
                Ason object = new Ason(m.group(0).substring(14, m.group(0).lastIndexOf(";")));
                csrf = object.get("config.csrf_token");
                rhx_gis = object.getString("rhx_gis");
                this.login = true;
            } else {
                System.out.println("NO MATCH");
            }
        }
        return json;
//        return null;
    }

    public boolean isLogin() {
        if (login) {
            try {
                user = getUser(username);
                if (user.getJson().has("config.viewer")) {
                    return user.getJson().get("config.viewer") != null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public FollowResponse getFollowers(String instaId) throws Exception {
        return getFollowers(instaId, 12, "");
    }

    public FollowResponse getFollowers(String instaId, String after) throws Exception {
        return getFollowers(instaId, 12, after);
    }

    public FollowResponse getFollowers(String instaId, int first) throws Exception {
        return getFollowers(instaId, first, "");
    }

    public FollowResponse getFollowers(String instaId, int first, String after) throws Exception {
        return getFollow(instaId, first, after, 1);
    }

    public FollowResponse getFollowing(String instaId) throws Exception {
        return getFollowing(instaId, 12, "");
    }

    public FollowResponse getFollowing(String instaId, String after) throws Exception {
        return getFollowing(instaId, 12, after);
    }

    public FollowResponse getFollowing(String instaId, int first) throws Exception {
        return getFollowing(instaId, first, "");
    }

    public FollowResponse getFollowing(String instaId, int first, String after) throws Exception {
        return getFollow(instaId, first, after, 2);
    }

    private FollowResponse getFollow(String instaId, int first, String after, int type) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String variable = "{\"id\":\"" + instaId + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        String jsonAddress = type == 1 ? "edge_followed_by" : "edge_follow";
        String link = (type == 1 ? S.FOLLOWER_ADDRESS : S.FOLLOWING_ADDRESS) + variable;
        String gis = md5(rhx_gis + ":" + variable);
        Connection c = Jsoup.connect(link).userAgent(userAgent).cookies(cookie).ignoreContentType(true).header("X-Instagram-GIS", gis)
                .header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        c.cookie("csrftoken", csrf);
        Response r = c.execute();
        Ason object = new Ason(r.body());
        FollowResponse fr = new FollowResponse();
        fr.setJson(object);
        fr.setCount(object.getInt("data.user." + jsonAddress + ".count"));
        fr.setHasNextPage(object.getBool("data.user." + jsonAddress + ".page_info.has_next_page"));
        fr.setEndCursor(object.getString("data.user." + jsonAddress + ".page_info.end_cursor"));
        AsonArray<Ason> jsonArrayUsers = object.getJsonArray("data.user." + jsonAddress + ".edges");
        for (Ason jsonUser : jsonArrayUsers) {
            jsonUser = jsonUser.getJsonObject("node");
            User user = new User();
            user.setInstaId(jsonUser.getString("id"));
            user.setUsername(jsonUser.getString("username"));
            user.setFullname(jsonUser.getString("full_name"));
            user.setImage(jsonUser.getString("profile_pic_url"));
            user.setIsVerified(jsonUser.getBool("is_verified"));
            user.setFollowedByViewer(jsonUser.getBool("followed_by_viewer"));
            user.setRequestedByViewer(jsonUser.getBool("requested_by_viewer"));
            fr.addUsers(user);
        }
        return fr;
    }

    public boolean follow(String username) throws Exception {
        return friendship(getUser(username), "follow");
    }

    public boolean follow(User user) throws Exception {
        return friendship(user, "follow");
    }

    public boolean unfollow(String username) throws Exception {
        return friendship(getUser(username), "unfollow");
    }

    public boolean unfollow(User user) throws Exception {
        return friendship(user, "unfollow");
    }

    private boolean friendship(User user, String type) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String link = S.FRIENDSHIP_ADDRESS + user.getInstaId() + "/" + type + "/";
//        csrf = cookie.get("csrftoken");
        Map<String, String> headrs = new HashMap<>();
        headrs.put("X-CSRFToken", csrf);
        headrs.put("X-Instagram-AJAX", "1");
        headrs.put("X-Requested-With", "XMLHttpRequest");
        headrs.put("Host", "www.instagram.com");
        headrs.put("content-type", "application/x-www-form-urlencoded");
        Connection c = Jsoup.connect(link).cookies(cookie).referrer("https://www.instagram.com/" + user.getUsername() + "/").ignoreContentType(true).method(Connection.Method.POST)
                .ignoreHttpErrors(true).userAgent(userAgent).headers(headrs);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        for (Map.Entry<String, String> entry : r.cookies().entrySet()) {
            if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.println("statusCode is " + r.statusCode());
        System.out.println(r.body());
        Ason object = new Ason(r.body());
        return object.getString("status").equals("ok");
    }

    public boolean like(String shortcode) throws Exception {
        return likes(getPost(shortcode), "like");
    }

    public boolean like(Post post) throws Exception {
        return likes(post, "like");
    }

    public boolean unlike(String shortcode) throws Exception {
        return likes(getPost(shortcode), "unlike");
    }

    public boolean unlike(Post post) throws Exception {
        return likes(post, "unlike");
    }

    private boolean likes(Post post, String type) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String link = S.LIKES_ADDRESS + post.getId() + "/" + type + "/";
//        csrf = cookie.get("csrftoken");
        Map<String, String> headrs = new HashMap<>();
        headrs.put("X-CSRFToken", csrf);
        headrs.put("X-Instagram-AJAX", "1");
        headrs.put("X-Requested-With", "XMLHttpRequest");
        headrs.put("Host", "www.instagram.com");
        headrs.put("content-type", "application/x-www-form-urlencoded");
        Connection c = Jsoup.connect(link).cookies(cookie).referrer("https://www.instagram.com/p/" + post.getShortcode() + "/").ignoreContentType(true).method(Connection.Method.POST)
                .ignoreHttpErrors(true).userAgent(userAgent).headers(headrs);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        for (Map.Entry<String, String> entry : r.cookies().entrySet()) {
            if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        Ason object = new Ason(r.body());
        return object.getString("status").equals("ok");
    }

    public Comment addComment(String shortcode, String text) throws Exception {
        return addComment(getPost(shortcode), text);
    }

    public Comment addComment(Post post, String text) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String link = S.ADD_COMMENT_ADDRESS + post.getId() + "/add/";
//        csrf = cookie.get("csrftoken");
        Map<String, String> headrs = new HashMap<>();
        headrs.put("X-CSRFToken", csrf);
        headrs.put("X-Instagram-AJAX", "1");
        headrs.put("X-Requested-With", "XMLHttpRequest");
        headrs.put("Host", "www.instagram.com");
        headrs.put("content-type", "application/x-www-form-urlencoded");
        Connection c = Jsoup.connect(link).cookies(cookie).referrer("https://www.instagram.com/p/" + post.getShortcode() + "/").ignoreContentType(true)
                .ignoreHttpErrors(true).data("comment_text", text).method(Connection.Method.POST)
                .userAgent(userAgent).headers(headrs);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        for (Map.Entry<String, String> entry : r.cookies().entrySet()) {
            if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        Ason json = new Ason(r.body());
        Comment comment = new Comment();
        comment.setId(json.getString("id"));
        comment.setOwnerId(json.getString("from.id"));
        comment.setOwnerProfilePicUrl(json.getString("from.profile_picture"));
        comment.setOwnerUsername(json.getString("from.username"));
        comment.setTimestamp(json.getInt("created_time"));
        comment.setText(json.getString("text"));
        comment.setPostShortCode(post.getShortcode());
        return comment;
    }

    public boolean deleteComment(Comment comment) throws Exception {
        return deleteComment(comment, getPost(comment.getPostShortCode()));
    }

    public boolean deleteComment(Comment comment, Post post) throws Exception {
        if (cookie == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String link = S.ADD_COMMENT_ADDRESS + post.getId() + "/delete/" + comment.getId() + "/";
//        csrf = cookie.get("csrftoken");
        Map<String, String> headrs = new HashMap<>();
        headrs.put("X-CSRFToken", csrf);
        headrs.put("X-Instagram-AJAX", "1");
        headrs.put("X-Requested-With", "XMLHttpRequest");
        headrs.put("Host", "www.instagram.com");
        headrs.put("content-type", "application/x-www-form-urlencoded");
        Connection c = Jsoup.connect(link).cookies(cookie).referrer("https://www.instagram.com/p/" + post.getShortcode() + "/").ignoreContentType(true).method(Connection.Method.POST)
                .ignoreHttpErrors(true).userAgent(userAgent).headers(headrs);
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        for (Map.Entry<String, String> entry : r.cookies().entrySet()) {
            if (entry.getValue().isEmpty() ||  entry.getValue().equals("\"\"")) {
                cookie.remove(entry.getKey());
            } else {
                cookie.put(entry.getKey(), entry.getValue());
            }
        }
        Ason object = new Ason(r.body());
        return object.getString("status").equals("ok");
    }

    public Location getLocation(String locationId) throws IOException {
        String link = S.LOCATION_ADDRESS + locationId + "/?__a=1";
        Location location = new Location();
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true);
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

    public SuggestedResponse getSuggestedUser() throws IOException {
        return getSuggestedUser(new ArrayList<>());
    }

    public SuggestedResponse getSuggestedUser(ArrayList<String> seenIds) throws IOException {
        String variable = "{\"fetch_media_count\":0,\"fetch_suggested_count\":20,\"ignore_cache\":false,\"filter_followed_friends\":true,\"seen_ids\":" + Ason.serializeList(seenIds) + "}";
        String link = S.SUGGESTED_ADDRESS + variable;
        String gis = md5(rhx_gis + ":" + variable);
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true).cookies(cookie).header("X-Instagram-GIS", gis).header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        Response r = c.execute();
        Ason json = new Ason(r.body()).getJsonObject("data.user.edge_suggested_users");
        SuggestedResponse sr = new SuggestedResponse();
        sr.setHasNextPage(json.getBool("page_info.has_next_page"));
        AsonArray<Ason> usersArray = json.getJsonArray("edges");
        for (Ason userJson : usersArray) {
            SuggestedUser suggestedUser = new SuggestedUser();
            suggestedUser.setDescription(userJson.getString("node.description"));
            userJson = userJson.getJsonObject("node.user");
            suggestedUser.setFollowersCount(userJson.getInt("edge_followed_by.count"));
            suggestedUser.setFollow(userJson.getBool("followed_by_viewer"));
            suggestedUser.setFullname(userJson.getString("full_name"));
            suggestedUser.setInstaId(userJson.getString("id"));
            suggestedUser.setPrivate(userJson.getBool("is_private"));
            suggestedUser.setVerified(userJson.getBool("is_verified"));
            suggestedUser.setViewer(userJson.getBool("is_viewer"));
            suggestedUser.setImage(userJson.getString("profile_pic_url"));
            suggestedUser.setRequested(userJson.getBool("requested_by_viewer"));
            suggestedUser.setUsername(userJson.getString("username"));
            sr.addSuggestedUser(suggestedUser);
        }
        return sr;
    }

    public ExploreResponse getExplore() throws Exception {
        return getExplore(24, "");
    }

    public ExploreResponse getExplore(int first) throws Exception {
        return getExplore(first, "");
    }

    public ExploreResponse getExplore(String after) throws Exception {
        return getExplore(24, after);
    }

    public ExploreResponse getExplore(int first, String after) throws Exception {
        if (username == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String variable = "{\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        String link = S.EXPLORE_ADDRESS + variable;
        String gis = md5(rhx_gis + ":" + variable);
        ExploreResponse er = new ExploreResponse();
        Connection c = Jsoup.connect(link).userAgent(userAgent).ignoreContentType(true).header("X-Instagram-GIS", gis).header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
        }
        c.cookie("csrftoken", csrf);
        Response r = c.execute();
        Ason data = new Ason(r.body()).getJsonObject("data.user.edge_web_discover_media");
        boolean isNextPage = data.getBool("page_info.has_next_page");
        er.setHasNextPage(isNextPage);
        if (isNextPage) {
            er.setEndCursor(data.getString("page_info.end_cursor"));
        }
        AsonArray<Ason> posts = data.getJsonArray("edges");
        for (int i = 0; i < posts.size(); i++) {
            Ason postObj = posts.get(i).getJsonObject("node");
            er.addPost(returnTimeLinePost(postObj));
        }


        return er;
    }

    public LikerResponse getLiker(String shortcode, int first, String after) throws Exception {
        if (username == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        String variable = "{\"shortcode\":\"" + shortcode + "\",\"first\":" + first + (after.length() > 0 ? ",\"after\":\"" + after + "\"" : "") + "}";
        String link = S.POST_LIKERS_ADDRESS + variable;
        String gis = md5(rhx_gis + ":" + variable);
        LikerResponse lr = new LikerResponse();
        Connection c = Jsoup.connect(link).userAgent(userAgent).referrer(S.URL + "p/" + shortcode).ignoreContentType(true)
                .header("X-Instagram-GIS", gis).header("X-Requested-With", "XMLHttpRequest");
        if (ip.length() > 0 && port != 0) {
            c.proxy(ip, port);
        }
        if (!cookie.isEmpty()) {
            c.cookies(cookie);
        }
        c.cookie("csrftoken", csrf);
        Response r = c.execute();
        Ason data = new Ason(r.body()).getJsonObject("data.shortcode_media.edge_liked_by");
        boolean isNextPage = data.getBool("page_info.has_next_page");
        lr.setHasNextPage(isNextPage);
        if (isNextPage) {
            lr.setEndCursor(data.getString("page_info.end_cursor"));
        }
        AsonArray<Ason> posts = data.getJsonArray("edges");
        for (int i = 0; i < posts.size(); i++) {
            Ason likerObj = posts.get(i).getJsonObject("node");
            Liker liker = new Liker();
            liker.setFullname(likerObj.getString("full_name"));
            liker.setId(likerObj.getString("id"));
            liker.setUsername(likerObj.getString("username"));
            liker.setImage(likerObj.getString("profile_pic_url"));
            liker.setFollowed(likerObj.getBool("followed_by_viewer"));
            liker.setRequested(likerObj.getBool("requested_by_viewer"));
            liker.setVerified(likerObj.getBool("is_verified"));
            lr.addLiker(liker);
        }


        return lr;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public User getYourSelf()throws Exception{
        return getYourSelf(false);
    }

    public User getYourSelf(boolean refresh) throws Exception {
        if (username == null) {
            throw new Exception("you don't login yet. for this method you need to login first");
        }
        if(refresh || user == null){
            return user = getUser(username);
        } else {
            return user;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCsrf() {
        return csrf;
    }

    public String getRhx_gis() {
        return rhx_gis;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public User getUser() {
        return user;
    }

    private Post returnTimeLinePost(Ason postObj) {
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
        return post;
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

    private String md5(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {

        }
        return null;
    }
}
