package my.friends.finder;

import static my.friends.util.FriendsNetworkCreator.createMultipleUsersNetwork;
import static my.friends.util.FriendsNetworkCreator.createTwoUsersNetwork;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import my.friends.exception.ApplicationException;
import my.friends.exception.SameSourceAndTargetUserException;
import my.friends.model.FriendsNetwork;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

class ShortestPathLengthFinderShould {

    private ShortestPathLengthFinder shortestPathLengthFinder;

    @BeforeEach
    void setup() {

        shortestPathLengthFinder = new ShortestPathLengthFinder();
    }

    @Test
    void findOnePathLengthBetweenTwoFriends() {

        FriendsNetwork friendsNetwork = createTwoUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("Bob"));

        Optional<Integer> shortestPathLength = shortestPathLengthFinder.find(pathFinderInput);

        assertThat(shortestPathLength).isPresent();
        assertThat(shortestPathLength.get()).isEqualTo(1);
    }

    @Test
    void findNoPathLengthBetweenTwoUnrelatedUsers() {

        FriendsNetwork friendsNetwork = createMultipleUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("John"));

        Optional<Integer> shortestPathLength = shortestPathLengthFinder.find(pathFinderInput);

        assertThat(shortestPathLength).isEmpty();
    }

    @Test
    void findTheShortestPathLengthBetweenTwoUsersWhenMultiplePathsExist() {

        FriendsNetwork friendsNetwork = createMultipleUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Ema"),
                new User("Rob"));

        Optional<Integer> shortestPathLength = shortestPathLengthFinder.find(pathFinderInput);

        assertThat(shortestPathLength).isPresent();
        assertThat(shortestPathLength.get()).isEqualTo(3);
    }

    @Test
    void failForSourceUserTheSameAsTargetUser() {

        FriendsNetwork friendsNetwork = createTwoUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("Alice"));

        Exception exception = assertThrows(SameSourceAndTargetUserException.class,
                () -> shortestPathLengthFinder.find(pathFinderInput));

        assertThat(exception.getMessage()).isEqualTo("Source user cannot be the same as target user.");
    }

    @Test
    void failForInvalidInput(){
        
        PathFinderInput invalidPathFinderInput = new PathFinderInput(null, new User("Ema"),
                new User("Rob"));

        assertThrows(ApplicationException.class, () -> shortestPathLengthFinder.find(invalidPathFinderInput));
    }
}