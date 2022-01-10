package my.friends.finder;


import static my.friends.util.FriendsNetworkCreator.createMultipleUsersNetwork;
import static my.friends.util.FriendsNetworkCreator.createTwoUsersNetwork;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import my.friends.exception.ApplicationException;
import my.friends.model.FriendsNetwork;
import my.friends.model.Path;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

class AllShortestPathsFinderShould {

    private AllShortestPathsFinder allShortestPathsFinder;

    @BeforeEach
    void setup() {

        allShortestPathsFinder = new AllShortestPathsFinder();
    }

    @Test
    void shouldReturnASinglePathForTwoFriends() {

        FriendsNetwork friendsNetwork = createTwoUsersNetwork();

        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("Bob"));

        List<Path> paths = allShortestPathsFinder.find(pathFinderInput);

        assertThat(paths).isNotNull();
        assertThat(paths.size()).isEqualTo(1);
        assertThat(paths.get(0)).isEqualTo(createExpectedPathForFriends());
    }

    @Test
    void findNoPathBetweenTwoUnrelatedUsers(){

        FriendsNetwork friendsNetwork = createMultipleUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("John"));

        List<Path> paths = allShortestPathsFinder.find(pathFinderInput);

        assertThat(paths).isNotNull();
        assertThat(paths).isEmpty();
    }

    @Test
    void shouldFindALlPathsWithShortestLength(){
        
        FriendsNetwork friendsNetwork = createMultipleUsersNetwork();
        PathFinderInput pathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Ema"),
                new User("Rob"));

        List<Path> paths = allShortestPathsFinder.find(pathFinderInput);

        verifyPaths(paths, createExpectedPaths());
    }

    @Test
    void failForInvalidInput(){

        FriendsNetwork friendsNetwork = createTwoUsersNetwork();
        PathFinderInput invalidPathFinderInput = new PathFinderInput(friendsNetwork.getFriendships(), new User("Alice"),
                new User("Alice"));

        assertThrows(ApplicationException.class, () -> allShortestPathsFinder.find(invalidPathFinderInput));
    }

    private Path createExpectedPathForFriends(){

        User user = new User("Alice");
        User friend = new User("Bob");

        return createPath(user, friend);
    }

    private List<Path> createExpectedPaths(){

        User alice = new User("Alice");
        User bob = new User("Bob");
        User mark = new User("Mark");
        User katy = new User("Katy");
        User ema = new User("Ema");
        User rob = new User("Rob");

        List<Path> expectedPaths = new ArrayList<>();

        expectedPaths.add(createPath(ema, mark, katy, rob));
        expectedPaths.add(createPath(ema, mark, bob, rob));
        expectedPaths.add(createPath(ema, alice, bob, rob));
        expectedPaths.add(createPath(ema, alice, katy, rob));

        return expectedPaths;
    }

    private Path createPath(User... users){

        Path path = new Path();
        Arrays.stream(users).forEach(path::addUser);

        return path;
    }

    private void verifyPaths(List<Path> paths, List<Path> expectedPaths) {

        assertThat(paths).isNotNull();
        assertThat(paths.size()).isEqualTo(expectedPaths.size());

        expectedPaths.removeAll(paths);
        assertThat(expectedPaths).isEmpty();
    }
}