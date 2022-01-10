package my.friends.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import my.friends.exception.ApplicationException;

class FriendsNetworkShould {

    private FriendsNetwork friendsNetwork;

    @BeforeEach
    void initialize() {
        friendsNetwork = new FriendsNetwork();
    }

    @Test
    void addTheFriendshipBetweenTwoUsers() {

        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Bob"));

        friendsNetwork.addFriendshipFor(new User("Alice"), new User("Bob"));

        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void failAddingFriendshipBetweenTwoUsersWhenInvalidInput() {

        FriendsNetwork expectedNetwork = new FriendsNetwork();

        assertThrows(ApplicationException.class, () -> friendsNetwork.addFriendshipFor(null, new User("Alice")));
        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void addTheFriendshipBetweenMultipleUsers() {

        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Bob"), new User("Mark"));

        friendsNetwork.addFriendshipsFor(new User("Alice"), new User("Bob"), new User("Mark"));

        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void failAddingFriendshipBetweenMultipleUsersWhenInvalidInput() {

        FriendsNetwork expectedNetwork = new FriendsNetwork();

        assertThrows(ApplicationException.class, () -> friendsNetwork.addFriendshipsFor(null, new User("Alice"),
                new User("Bob")));
        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void removeFriendshipBetweenTwoUsersInTheNetwork() {

        friendsNetwork = createNetwork(new User("Alice"), new User("Bob"), new User("Mark"));
        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Bob"));

        friendsNetwork.removeFriendship(new User("Alice"), new User("Mark"));

        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void failRemovingNonExistentFriendship() {

        friendsNetwork = createNetwork(new User("Alice"), new User("Bob"));
        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Bob"));

        assertThrows(ApplicationException.class, () -> friendsNetwork.removeFriendship(new User("Mark"),
                new User("Alice")));

        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void removeUser() {

        friendsNetwork = createNetwork(new User("Alice"), new User("Bob"), new User("Mark"));
        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Mark"));

        friendsNetwork.removeUser(new User("Bob"));

        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    @Test
    void removeAllUsersWithoutFriendsAfterUserRemoval() {

        friendsNetwork = createNetwork(new User("Alice"), new User("Bob"), new User("Mark"), new User("Ed"));

        friendsNetwork.removeUser(new User("Alice"));

        assertThat(friendsNetwork.getFriendships()).isEmpty();
    }

    @Test
    void failRemovingNonExistentUser() {

        friendsNetwork = createNetwork(new User("Alice"), new User("Bob"));
        FriendsNetwork expectedNetwork = createNetwork(new User("Alice"), new User("Bob"));

        assertThrows(ApplicationException.class, () -> friendsNetwork.removeUser(new User("Rob")));
        assertThat(friendsNetwork).isEqualTo(expectedNetwork);
    }

    private FriendsNetwork createNetwork(User firstUser, User secondUser) {

        Map<User, Set<User>> friendships = new HashMap<>();

        addFriendship(friendships, firstUser, secondUser);
        addFriendship(friendships, secondUser, firstUser);

        return new FriendsNetwork(friendships);
    }

    private FriendsNetwork createNetwork(User user, User firstFriend, User secondFriend) {

        Map<User, Set<User>> friendships = new HashMap<>();

        addFriendship(friendships, user, firstFriend, secondFriend);

        return new FriendsNetwork(friendships);
    }

    private FriendsNetwork createNetwork(User user, User... friends) {

        Map<User, Set<User>> friendships = new HashMap<>();

        addFriendship(friendships, user, friends);

        return new FriendsNetwork(friendships);
    }

    private void addFriendship(Map<User, Set<User>> friendships, User firstUser, User... friends) {


        Set<User> firstUsersFriends = new HashSet<>();
        Arrays.stream(friends).forEach(firstUsersFriends::add);

        friendships.put(firstUser, firstUsersFriends);

        firstUsersFriends.forEach(friend -> {
            Set<User> currentFriends = new HashSet<>();
            currentFriends.add(firstUser);
            friendships.put(friend, currentFriends);
        });
    }
}