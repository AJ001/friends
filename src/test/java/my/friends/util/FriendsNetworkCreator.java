package my.friends.util;

import my.friends.model.FriendsNetwork;
import my.friends.model.User;

/**
 * Provides methods for creating {@link FriendsNetwork} to be used in tests
 */
public class FriendsNetworkCreator {

    /**
     * Hides the default constructor to avoid instance creation
     */
    private FriendsNetworkCreator() {

    }

    /**
     * Creates a friends network containing just two users
     *
     * @return the newly created network
     */
    public static FriendsNetwork createTwoUsersNetwork() {

        FriendsNetwork network = new FriendsNetwork();
        network.addFriendshipFor(new User("Alice"), new User("Bob"));

        return network;
    }

    /**
     * Creates a friends network containing more than two users
     *
     * @return the newly created network
     */
    public static FriendsNetwork createMultipleUsersNetwork() {

        FriendsNetwork network = new FriendsNetwork();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User mark = new User("Mark");
        User katy = new User("Katy");
        User ema = new User("Ema");
        User rob = new User("Rob");

        network.addFriendshipsFor(alice, bob, mark, katy, ema);
        network.addFriendshipsFor(bob, mark, katy, rob, alice);
        network.addFriendshipsFor(rob, bob, katy);
        network.addFriendshipsFor(katy, bob, alice, rob);
        network.addFriendshipsFor(ema, alice, mark);
        network.addFriendshipsFor(mark, alice, bob, ema, katy);
        network.addFriendshipFor(new User("John"), new User("Eddie"));

        return network;
    }
}
