package my.friends.model;

import java.util.Map;
import java.util.Set;

/**
 * Contains the details to search for paths between two users in the network
 */
public class PathFinderInput {

    private final Map<User, Set<User>> friendships;
    private final User sourceUser;
    private final User targetUser;

    public PathFinderInput(Map<User, Set<User>> friendships, User sourceUser, User targetUser) {

        this.friendships = friendships;
        this.sourceUser = sourceUser;
        this.targetUser = targetUser;
    }

    public Map<User, Set<User>> getFriendships() {

        return friendships;
    }

    public User getSourceUser() {

        return sourceUser;
    }

    public User getTargetUser() {

        return targetUser;
    }
}
