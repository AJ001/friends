package my.friends.finder;

import static my.friends.model.factory.UsersToVisitFactory.instantiateUsersToVisit;
import static my.friends.model.factory.VisitedUsersFactory.instantiateVisitedUsers;

import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import my.friends.exception.SameSourceAndTargetUserException;
import my.friends.model.PathFinderInput;
import my.friends.model.User;
import my.friends.model.UserDistance;
import my.friends.model.UsersToVisit;
import my.friends.model.VisitedUsers;
import my.friends.validator.PathFinderInputValidator;

/**
 * Responsible of searching the shortest path length
 */
public class ShortestPathLengthFinder {

    /**
     * Searches the shortest path length for the given input
     *
     * @param pathFinderInput the required details to perform the search, like network, source user and target user
     * @return an {@link Optional} containing an integer that represents the value of the shortest path length if
     * any path exists between source user and target user, or empty otherwise
     */
    public Optional<Integer> find(PathFinderInput pathFinderInput) {

        new PathFinderInputValidator().validate(pathFinderInput);

        return computeShortestPathLength(pathFinderInput);
    }

    private Optional<Integer> computeShortestPathLength(PathFinderInput pathFinderInput) {

        User sourceUser = pathFinderInput.getSourceUser();
        User targetUser = pathFinderInput.getTargetUser();

        if (sourceUser.equals(targetUser)) {
            throw new SameSourceAndTargetUserException();
        }

        VisitedUsers visitedUsers = instantiateVisitedUsers(sourceUser, targetUser);
        UsersToVisit usersToVisit = instantiateUsersToVisit(sourceUser, targetUser);

        return visitUsers(pathFinderInput.getFriendships(), usersToVisit, visitedUsers);
    }

    private Optional<Integer> visitUsers(Map<User, Set<User>> friendships, UsersToVisit usersToVisit,
                                         VisitedUsers visitedUsers) {

        while (usersToVisit.hasElements()) {

            Optional<Integer> shortestPathLength = visitUsersBidirectional(friendships, usersToVisit, visitedUsers);

            if (shortestPathLength.isPresent()) {
                return shortestPathLength;
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> visitUsersBidirectional(Map<User, Set<User>> friendships, UsersToVisit usersToVisit,
                                                      VisitedUsers visitedUsers) {

        Optional<Integer> pathLength = visitUser(friendships, usersToVisit.getUsersForSource(),
                visitedUsers.getUsersFromSource(), visitedUsers.getUsersFromTarget());

        if (pathLength.isPresent()) {

            return pathLength;
        }

        return visitUser(friendships, usersToVisit.getUsersForTarget(), visitedUsers.getUsersFromTarget(),
                visitedUsers.getUsersFromSource());

    }

    private Optional<Integer> visitUser(Map<User, Set<User>> friendships, Queue<UserDistance> usersToVisit,
                                        Map<User, Integer> visitedOwn, Map<User, Integer> visitedOther) {

        if (usersToVisit.size() == 0) {
            return Optional.empty();
        }

        UserDistance currentUserToVisit = usersToVisit.poll();
        User currentUser = currentUserToVisit.getUser();
        Set<User> friendsOfCurrentUser = friendships.get(currentUser);
        Integer distanceUntilDirectFriendsOfCurrentUser = currentUserToVisit.getDistance() + 1;

        return checkFriendsForVisit(friendsOfCurrentUser, usersToVisit, visitedOwn, visitedOther, distanceUntilDirectFriendsOfCurrentUser);
    }

    private Optional<Integer> checkFriendsForVisit(Set<User> friends, Queue<UserDistance> usersToVisit,
                                                   Map<User, Integer> visitedOwn, Map<User, Integer> visitedOther,
                                                   Integer distanceUntilDirectFriendsOfCurrentUser) {

        return friends.stream()
                .map(friend -> checkFriendForVisit(friend, usersToVisit, visitedOwn, visitedOther,
                        distanceUntilDirectFriendsOfCurrentUser))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Integer> checkFriendForVisit(User user, Queue<UserDistance> usersToVisit,
                                                  Map<User, Integer> visitedOwn, Map<User, Integer> visitedOther,
                                                  Integer distanceUntilDirectFriendsOfCurrentUser) {

        if (visitedOther.containsKey(user)) {

            Integer shortestPathLength = distanceUntilDirectFriendsOfCurrentUser + visitedOther.get(user);
            return Optional.of(shortestPathLength);
        }

        markForVisit(user, usersToVisit, visitedOwn, distanceUntilDirectFriendsOfCurrentUser);

        return Optional.empty();
    }

    private void markForVisit(User user, Queue<UserDistance> usersToVisit, Map<User, Integer> visitedOwn, Integer distanceUntilDirectFriendsOfCurrentUser) {

        if (visitedOwn.containsKey(user)) {
            return;
        }

        usersToVisit.add(new UserDistance(user, distanceUntilDirectFriendsOfCurrentUser));
        visitedOwn.put(user, distanceUntilDirectFriendsOfCurrentUser);
    }
}
