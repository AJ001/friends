package my.friends.model;

import java.util.Map;

/**
 * Holds the visited users from both directions
 */
public class VisitedUsers {

    private final Map<User, Integer> usersFromSource;
    private final Map<User, Integer> usersFromTarget;

    public VisitedUsers(Map<User, Integer> usersFromSource, Map<User, Integer> usersFromTarget) {

        this.usersFromSource = usersFromSource;
        this.usersFromTarget = usersFromTarget;
    }

    public Map<User, Integer> getUsersFromSource() {

        return usersFromSource;
    }

    public Map<User, Integer> getUsersFromTarget() {

        return usersFromTarget;
    }
}
