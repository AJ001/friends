package my.friends.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import my.friends.validator.NetworkValidator;
import my.friends.validator.UserValidator;

/**
 * Holds the state of the network and offers methods to manage it
 */
public class FriendsNetwork {

    private final Map<User, Set<User>> friendships;

    public FriendsNetwork() {

        friendships = new HashMap<>();
    }

    public FriendsNetwork(Map<User, Set<User>> friendships) {

        this.friendships = new HashMap<>(friendships);
    }

    /**
     * Returns a snapshot of the current friendships of the network
     *
     * @return an immutable map containing the users and the friendships
     */
    public Map<User, Set<User>> getFriendships() {

        return Collections.unmodifiableMap(friendships);
    }

    /**
     * Adds a bidirectional connection between the given users; if any of the users are not part of the network,
     * they are implicitly added, including the associated direct connection
     *
     * @param user   an user
     * @param friend the new friend
     */
    public void addFriendshipFor(User user, User friend) {

        new UserValidator().validateUsers(user, friend);
        addBiDirectionalFriendship(user, friend);
    }

    /**
     * Adds bidirectional connections between the given user and its friends; if any of the users are not part of the network,
     * they are implicitly added, including the associated direct connections with the user
     *
     * @param user    an user
     * @param friends the new friends
     */
    public void addFriendshipsFor(User user, User... friends) {

        new UserValidator().validateUsers(user, friends);
        Arrays.stream(friends)
                .forEach(friend -> addBiDirectionalFriendship(user, friend));
    }

    /**
     * Removes the bidirectional direct connection between the given users; in case any user remains without friends, it will be
     * implicitly removed from the network as well
     *
     * @param user   an user
     * @param friend the friend to be removed
     */
    public void removeFriendship(User user, User friend) {

        new NetworkValidator().validateFriendshipExists(this.friendships, user, friend);
        removeBiDirectionalFriendship(user, friend);

        removeUsersWithoutFriends();
    }

    /**
     * Removes the given user from the network together with its friendships; in case any user remains without friends, it will be
     * implicitly removed from the network as well
     *
     * @param user the user to be removed
     */
    public void removeUser(User user) {

        new UserValidator().validateUser(user);
        new NetworkValidator().validateUserBelongsToNetwork(this.friendships, user);

        friendships.values().forEach(friends -> friends.remove(user));
        friendships.remove(user);

        removeUsersWithoutFriends();
    }

    /**
     * Displays the current state of the network
     */
    public void displayNetwork() {

        Map<User, Set<User>> sortedFriendships = new TreeMap<>(friendships);

        for (User currentUser : sortedFriendships.keySet()) {

            System.out.print(currentUser.getUsername() + ":");

            for (User friend : sortedFriendships.get(currentUser)) {

                System.out.print(" " + friend.getUsername());
            }

            System.out.println();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendsNetwork that = (FriendsNetwork) o;

        return friendships.equals(that.friendships);
    }

    @Override
    public int hashCode() {
        return friendships.hashCode();
    }

    private void addBiDirectionalFriendship(User user, User friend) {

        addOneDirectionFriends(user, friend);
        addOneDirectionFriends(friend, user);
    }

    private void addOneDirectionFriends(User user, User friend) {

        friendships.putIfAbsent(user, new HashSet<>());
        friendships.get(user).add(friend);
    }

    private void removeBiDirectionalFriendship(User user, User friend) {

        removeOneDirectionFriendship(user, friend);
        removeOneDirectionFriendship(friend, user);
    }

    private void removeOneDirectionFriendship(User user, User friend) {

        friendships.get(user).remove(friend);
    }

    private void removeUsersWithoutFriends() {

        Set<User> usersWithoutFriends = new HashSet<>();

        friendships.keySet().stream()
                .filter(key -> friendships.get(key).isEmpty())
                .forEach(usersWithoutFriends::add);

        usersWithoutFriends.forEach(friendships::remove);;
    }
}
