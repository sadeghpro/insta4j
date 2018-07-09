package ir.sadeghpro.insta.client;

import java.util.ArrayList;

public class SuggestedResponse {
    private boolean hasNextPage;
    private ArrayList<SuggestedUser> suggestedUsers = new ArrayList<>();
    private ArrayList<String> userIds = new ArrayList<>();

    public boolean hasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public ArrayList<SuggestedUser> getSuggestedUsers() {
        return suggestedUsers;
    }

    public void addSuggestedUser(SuggestedUser suggestedUser) {
        suggestedUsers.add(suggestedUser);
    }

    public void addAllSuggestedUsers(ArrayList<SuggestedUser> suggestedUsers){
        this.suggestedUsers.addAll(suggestedUsers);
    }

    public ArrayList<String> getUserIds() {
        return userIds;
    }

    public void addUserId(String userId) {
        userIds.add(userId);
    }

    public void addAllUserIds(ArrayList<String> userIds){
        this.userIds.addAll(userIds);
    }
}
