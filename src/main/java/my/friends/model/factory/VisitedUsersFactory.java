package my.friends.model.factory;

import java.util.HashMap;
import java.util.Map;

import my.friends.model.User;
import my.friends.model.VisitedUsers;

/**
 * Provides methods to instantiate {@link VisitedUsers}
 */
public final class VisitedUsersFactory {

    /**
     * Hides the default constructor to avoid instance creation
     */
    private VisitedUsersFactory() {

    }

    /**
     * Creates a {@link VisitedUsers} for the received users
     *
     * @param sourceUser the source user
     * @param targetUser the target user
     * @return the newly created object
     */
    public static VisitedUsers instantiateVisitedUsers(User sourceUser, User targetUser) {

        Map<User, Integer> usersVisitedFromSource = instantiateVisitedUsersFor(sourceUser);
        Map<User, Integer> usersVisitedFromTarget = instantiateVisitedUsersFor(targetUser);

        return new VisitedUsers(usersVisitedFromSource, usersVisitedFromTarget);
    }

    private static Map<User, Integer> instantiateVisitedUsersFor(User user) {

        Map<User, Integer> usersVisited = new HashMap<>();
        usersVisited.put(user, 0);

        return usersVisited;
    }
}
