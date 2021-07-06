package net.theEvilReaper.bot.service;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class FollowService {

    private String following;

    public void setFollowing(String following) {
        this.following = following;
    }

    public boolean isFollowing() {
        return following != null;
    }

    public String getFollowing() {
        return following;
    }

    public void reset() {
        this.following = null;
    }
}
