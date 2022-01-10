package my.friends.exception;

/**
 * Exception thrown when the source user is equal to the target user
 */
public class SameSourceAndTargetUserException extends ApplicationException{

    public SameSourceAndTargetUserException() {

        super("Source user cannot be the same as target user.");
    }
}
