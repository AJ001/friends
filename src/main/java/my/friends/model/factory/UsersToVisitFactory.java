package my.friends.model.factory;

import java.util.LinkedList;
import java.util.Queue;

import my.friends.model.User;
import my.friends.model.UserDistance;
import my.friends.model.UsersToVisit;

/**
 * Provides methods to instantiate {@link UsersToVisit}
 */
public final class UsersToVisitFactory {

    /**
     * Hides the default constructor to avoid instance creation
     */
    private UsersToVisitFactory() {

    }

    /**
     * Creates a {@link UsersToVisit} for the received users having the distance equal to 0
     * @param sourceUser the source user
     * @param targetUser the target user
     * @return the newly created object
     */
    public static UsersToVisit instantiateUsersToVisit(User sourceUser, User targetUser) {

        Queue<UserDistance> usersToVisitForSource = instantiateUsersToVisitFor(sourceUser);
        Queue<UserDistance> usersToVisitForTarget = instantiateUsersToVisitFor(targetUser);

        return new UsersToVisit(usersToVisitForSource, usersToVisitForTarget);
    }

    private static Queue<UserDistance> instantiateUsersToVisitFor(User user) {

        UserDistance userDistance = new UserDistance(user, 0);

        Queue<UserDistance> usersToVisit = new LinkedList<>();
        usersToVisit.add(userDistance);

        return usersToVisit;
    }

}
