package my.friends.validator;

import static my.friends.util.FriendsNetworkCreator.createTwoUsersNetwork;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import my.friends.exception.FriendshipDoesNotExistException;
import my.friends.exception.UserNotInTheNetworkException;
import my.friends.model.FriendsNetwork;
import my.friends.model.User;

class NetworkValidatorShould {

    private NetworkValidator networkValidator;
    private FriendsNetwork network;
    private Map<User, Set<User>> friendships;

    @BeforeEach
    void initialize(){

        networkValidator = new NetworkValidator();
        network = createTwoUsersNetwork();
        friendships = network.getFriendships();
    }

    @Test
    void doNothingWhenFriendshipExists(){

        networkValidator.validateFriendshipExists(friendships, new User("Alice"), new User("Bob"));
    }

    @Test
    void doNothingWhenBothUsersBelongToTheNetwork(){

        networkValidator.validateUsersBelongToNetwork(friendships, new User("Alice"), new User("Bob"));
    }

    @Test
    void dDoNothingWhenTheUsersBelongsToTheNetwork(){

        networkValidator.validateUserBelongsToNetwork(friendships, new User("Alice"));
    }

    @Test
    void failWhenTheFriendshipDoesNotExist(){

        network.addFriendshipFor(new User("Mark"), new User("Ema"));

        Exception exception = assertThrows(FriendshipDoesNotExistException.class,
                () -> networkValidator.validateFriendshipExists(friendships, new User("Alice"), new User("Ema")));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Friendship between user Alice and user Ema does not exist");
    }

    @Test
    void failWhenAtLeastOneUserDoesNotBelongToTheNetwork(){

        Exception exception = assertThrows(UserNotInTheNetworkException.class,
                () -> networkValidator.validateUsersBelongToNetwork(friendships, new User("Alice"), new User("Mark")));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("The user Mark is not part of the network.");
    }

    @Test
    void failWhenTheUserDoesNotBelongToTheNetwork(){

        Exception exception = assertThrows(UserNotInTheNetworkException.class,
                () -> networkValidator.validateUserBelongsToNetwork(friendships, new User("Mark")));

        assertThat(exception.getMessage()).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("The user Mark is not part of the network.");
    }
}