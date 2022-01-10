package my.friends.finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import my.friends.model.Path;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

/**
 * Responsible of searching all the shortest paths
 */
public class AllShortestPathsFinder {

    /**
     * Searches all the paths with the shortest length for the given input
     *
     * @param pathFinderInput the required details to perform the search, like network, source user and target user
     * @return a list containing all the shortest paths from source user to target user if any path exists or empty list otherwise
     */
    public List<Path> find(PathFinderInput pathFinderInput) {

        return new ShortestPathLengthFinder().find(pathFinderInput)
                .map(shortestPathLength -> findAllPaths(pathFinderInput, shortestPathLength))
                .orElse(Collections.emptyList());
    }

    private List<Path> findAllPaths(PathFinderInput pathFinderInput, int pathLength) {

        Path currentPath = new Path();
        currentPath.addUser(pathFinderInput.getSourceUser());

        List<Path> allPaths = new ArrayList<>();

        findAllPaths(pathFinderInput.getFriendships(), pathFinderInput.getSourceUser(),
                pathFinderInput.getTargetUser(), new HashSet<>(), pathLength, allPaths, currentPath);

        return allPaths;
    }

    private void findAllPaths(Map<User, Set<User>> friendships, User currentUser, User targetUser, Set<User> visited,
                              int pathLength, List<Path> allPaths, Path currentPath) {

        if (currentUser.equals(targetUser)) {
            allPaths.add(currentPath);
            return;
        }

        if (currentPath.getLength() == pathLength) {
            return;
        }

        findPathsContainingCurrentUser(friendships, currentUser, targetUser, visited, pathLength, allPaths, currentPath);
    }

    private void findPathsContainingCurrentUser(Map<User, Set<User>> friendships, User currentUser, User targetUser, Set<User> visited,
                                                int pathLength, List<Path> allPaths, Path currentPath) {
        visited.add(currentUser);

        friendships.get(currentUser)
                .forEach(friend -> findAllPathsForFriend(friendships, friend, targetUser, visited, pathLength, allPaths,
                        currentPath));

        visited.remove(currentUser);
    }

    private void findAllPathsForFriend(Map<User, Set<User>> friendships, User friend, User targetUser, Set<User> visited,
                                       int pathLength, List<Path> allPaths, Path currentPath) {

        if (visited.contains(friend)) {
            return;
        }
        Path newPath = new Path(currentPath);
        newPath.addUser(friend);

        findAllPaths(friendships, friend, targetUser, visited, pathLength, allPaths, newPath);
    }
}
