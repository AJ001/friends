package my.friends.exception;

import static java.lang.String.format;

import my.friends.model.User;

/**
 * Exception thrown when a friendship between to users does not exist
 */
public class FriendshipDoesNotExistException extends ApplicationException {

    public FriendshipDoesNotExistException(User firstUser, User secondUser) {

        super(format("Friendship between user %s and user %s does not exist", firstUser.getUsername(),
                secondUser.getUsername()));
    }
}
