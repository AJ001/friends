package my.friends.exception;

import static java.lang.String.format;

import my.friends.model.User;

/**
 * Exception thrown when the user is not part of the network
 */
public class UserNotInTheNetworkException extends ApplicationException {

    public UserNotInTheNetworkException(User user) {

        super(format("The user %s is not part of the network.", user.getUsername()));
    }
}
