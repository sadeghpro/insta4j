package ir.sadeghpro.insta.client;

import java.util.ArrayList;

public class LikerResponse {

    private boolean hasNextPage;
    private String endCursor;
    private ArrayList<Liker> likers = new ArrayList<>();


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
     * string code for getting next likers with getPost method
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
    public ArrayList<Liker> getLikers() {
        return likers;
    }

    public void addLiker(Liker liker) {
        likers.add(liker);
    }
}
