package my.friends.exception;

/**
 * Exception thrown when the user is null
 */
public class UsersCannotBeNullException extends ApplicationException {

    public UsersCannotBeNullException() {

        super("Users cannot be null");
    }
}
