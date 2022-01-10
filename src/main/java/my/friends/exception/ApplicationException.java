package my.friends.exception;

/**
 * Base class for all business exceptions
 */
public class ApplicationException extends RuntimeException{

    public ApplicationException(String message){

        super(message);
    }
}
