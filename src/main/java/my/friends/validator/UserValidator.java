package my.friends.validator;

import java.util.Arrays;

import my.friends.exception.UsersCannotBeNullException;
import my.friends.model.User;

/**
 * Holds the validation rules for {@link User}
 */
public class UserValidator {

    /**
     * Checks if the received users are valid
     *
     * @param firstUser  user to be validated
     * @param secondUser user to be validated
     * @throws UsersCannotBeNullException in case any user is null
     */
    public void validateUsers(User firstUser, User secondUser) {

        validateUser(firstUser);
        validateUser(secondUser);
    }

    /**
     * Checks if the received users are valid
     *
     * @param firstUser user to be validated
     * @param users     an array of users to be validated
     * @throws UsersCannotBeNullException in case any user is null
     */
    public void validateUsers(User firstUser, User... users) {

        validateUser(firstUser);
        validateUsers(users);
    }

    /**
     * Checks if the received user is valid
     *
     * @param user user to be validated
     * @throws UsersCannotBeNullException in case the user is null
     */
    public void validateUser(User user) {

        if (user == null) {
            throw new UsersCannotBeNullException();
        }
    }

    private void validateUsers(User[] users) {

        if (users == null) {
            throw new UsersCannotBeNullException();
        }
        UserValidator userValidator = new UserValidator();
        Arrays.stream(users).forEach(userValidator::validateUser);
    }
}
