package my.friends.validator;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import my.friends.exception.UsersCannotBeNullException;
import my.friends.model.User;

class UserValidatorShould {

    private UserValidator userValidator;

    @BeforeEach
    void initialize() {
        userValidator  = new UserValidator();
    }

    @Test
    void doNothingWhenTheValidatedUserIsNotNull(){

        userValidator.validateUsers(new User("Alice"));
    }

    @Test
    void doNothingWhenBothValidatedUsersAreNotNull(){

        userValidator.validateUsers(new User("Alice"), new User("Bob"));
    }

    @Test
    void doNothingWhenAllValidatedUsersAreNotNull(){

        userValidator.validateUsers(new User("Alice"), new User("Bob"), new User("Mark"));
    }

    @ParameterizedTest
    @MethodSource("provideTwoUsers")
    void failWhenValidatingTwoUsersIfAnyOfThemIsNull(User firstUser, User secondUser) {

        Exception exception = assertThrows(UsersCannotBeNullException.class,
                () -> userValidator.validateUsers(firstUser, secondUser));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Users cannot be null");
    }

    @ParameterizedTest
    @MethodSource("provideMultipleUsers")
    void failWhenValidatingMoreThanTwoUsersIfAnyOfThemIsNull(User firstUser, User... users) {

        Exception exception = assertThrows(UsersCannotBeNullException.class,
                () -> userValidator.validateUsers(firstUser, users));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Users cannot be null");
    }

    private static Stream<Arguments> provideTwoUsers() {

        return Stream.of(
                Arguments.of(new User("Alice"), null),
                Arguments.of(null, new User("Alice")),
                Arguments.of(null, null));
    }

    private static Stream<Arguments> provideMultipleUsers() {

        User[] friends = new User[2];
        friends[0] = new User("Alice");
        friends[1] = null;

        return Stream.of(
                Arguments.of(new User("Alice"), null),
                Arguments.of(null, friends),
                Arguments.of(null, null));
    }
}