package my.friends.validator;

import static my.friends.util.FriendsNetworkCreator.createTwoUsersNetwork;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import my.friends.exception.ApplicationException;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

class PathFinderInputValidatorShould {

    private PathFinderInputValidator pathFinderInputValidator;

    @BeforeEach
    void initialize() {

        pathFinderInputValidator = new PathFinderInputValidator();
    }

    @Test
    void doNothingWhenTheInputIsValid() {

        PathFinderInput pathFinderInput = createPathFinderInput();

        pathFinderInputValidator.validate(pathFinderInput);
    }

    @Test
    void failWhenTheInputIsNull() {

        Exception exception = assertThrows(ApplicationException.class,
                () -> pathFinderInputValidator.validate(null));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("The search input cannot be null");
    }

    @Test
    void failWhenTheFriendshipsFromTheInputIsNull(){

        PathFinderInput pathFinderInput = createPathFinderInputWithoutFriendships();

        Exception exception = assertThrows(ApplicationException.class,
                () -> pathFinderInputValidator.validate(pathFinderInput));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Friendships from the search input cannot be null");
    }

    @Test
    void failWhenInputUsersAreInvalid(){

        PathFinderInput pathFinderInput = createPathFinderInputWithNullUser();

        assertThrows(ApplicationException.class,  () -> pathFinderInputValidator.validate(pathFinderInput));
    }

    private PathFinderInput createPathFinderInput() {

        return new PathFinderInput(createTwoUsersNetwork().getFriendships(),
                new User("Alice"), new User("Bob"));
    }

    private PathFinderInput createPathFinderInputWithoutFriendships() {

        return new PathFinderInput(null,
                new User("Alice"), new User("Bob"));
    }

    private PathFinderInput createPathFinderInputWithNullUser() {

        return new PathFinderInput(null,
                new User("Alice"), null);
    }
}