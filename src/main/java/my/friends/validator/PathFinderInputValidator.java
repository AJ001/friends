package my.friends.validator;

import java.util.Map;
import java.util.Set;

import my.friends.exception.ApplicationException;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

/**
 * Holds validation rules for {@link PathFinderInput}
 */
public class PathFinderInputValidator {

    /**
     * Checks if the provided input is valid
     *
     * @param pathFinderInput the input to be validated
     * @throws ApplicationException in case the input is not valid
     */
    public void validate(PathFinderInput pathFinderInput) {

        if (pathFinderInput == null) {
            throw new ApplicationException("The search input cannot be null");
        }

        Map<User, Set<User>> friendships = pathFinderInput.getFriendships();

        if (friendships == null) {
            throw new ApplicationException("Friendships from the search input cannot be null");
        }

        User sourceUser = pathFinderInput.getSourceUser();
        User targetUser = pathFinderInput.getTargetUser();
        new UserValidator().validateUsers(sourceUser, targetUser);
        new NetworkValidator().validateUsersBelongToNetwork(friendships, sourceUser, targetUser);
    }
}
