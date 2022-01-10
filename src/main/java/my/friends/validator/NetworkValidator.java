package my.friends.validator;

import java.util.Map;
import java.util.Set;

import my.friends.exception.FriendshipDoesNotExistException;
import my.friends.exception.UserNotInTheNetworkException;
import my.friends.model.User;

/**
 * Holds validation rules for the network
 */
public class NetworkValidator {

    /**
     * Checks if the provided users are friends
     *
     * @param friendships the friendships inside the network
     * @param sourceUser  the source user
     * @param targetUser  the target user
     * @throws FriendshipDoesNotExistException in case the users are not friends
     */
    public void validateFriendshipExists(Map<User, Set<User>> friendships, User sourceUser, User targetUser) {

        new UserValidator().validateUsers(sourceUser, targetUser);
        validateUsersBelongToNetwork(friendships, sourceUser, targetUser);

        if (!friendships.get(sourceUser).contains(targetUser)) {

            throw new FriendshipDoesNotExistException(sourceUser, targetUser);
        }
    }

    /**
     * Checks if the provided users are part of the network
     *
     * @param friendships the friendships inside the network
     * @param sourceUser  the source user
     * @param targetUser  the target user
     * @throws UserNotInTheNetworkException in case at least one user is not part of the network
     */
    public void validateUsersBelongToNetwork(Map<User, Set<User>> friendships, User sourceUser, User targetUser) {

        validateUserBelongsToNetwork(friendships, sourceUser);
        validateUserBelongsToNetwork(friendships, targetUser);
    }

    /**
     * Checks if the provided user is part of the network
     *
     * @param friendships the friendships inside the network
     * @param user        the user to be checked
     * @throws UserNotInTheNetworkException in case the user is not part of the network
     */
    public void validateUserBelongsToNetwork(Map<User, Set<User>> friendships, User user) {

        if (!friendships.containsKey(user)) {

            throw new UserNotInTheNetworkException(user);
        }
    }
}
