package ir.sadeghpro.insta.client;

import com.afollestad.ason.Ason;

import java.util.ArrayList;

public class HashTagResponse {

    private transient Ason json;
    private boolean topMediaOnly;
    private String profilePicUrl;
    private int count;
    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Post> topPosts = new ArrayList<>();
    private ArrayList<Post> recentPosts = new ArrayList<>();

    public Ason getJson() {
        return json;
    }

    public void setJson(Ason json) {
        this.json = json;
    }

    public boolean isTopMediaOnly() {
        return topMediaOnly;
    }

    public void setTopMediaOnly(boolean topMediaOnly) {
        this.topMediaOnly = topMediaOnly;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getEndCursor() {
        return endCursor;
    }

    public void setEndCursor(String endCursor) {
        this.endCursor = endCursor;
    }

    public ArrayList<Post> getTopPosts() {
        return topPosts;
    }

    public void addTopPost(Post topPost) {
        topPosts.add(topPost);
    }

    public ArrayList<Post> getRecentPosts() {
        return recentPosts;
    }

    public void addRecentPost(Post recentPost) {
        recentPosts.add(recentPost);
    }
}
