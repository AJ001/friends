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

public class FriendsNetwork {

    private final Map<User, Set<User>> friendships;

    public FriendsNetwork() {

        friendships = new HashMap<>();
    }

    public FriendsNetwork(Map<User, Set<User>> friendships) {

        this.friendships = new HashMap<>(friendships);
    }


    public Map<User, Set<User>> getFriendships() {

        return Collections.unmodifiableMap(friendships);
    }

    public void addFriendshipFor(User user, User friend) {

        new UserValidator().validateUsers(user, friend);
        addBiDirectionalFriendship(user, friend);
    }

    public void addFriendshipsFor(User user, User... friends) {

        new UserValidator().validateUsers(user, friends);
        Arrays.stream(friends)
                .forEach(friend -> addBiDirectionalFriendship(user, friend));
    }

    public void removeFriendship(User user, User friend) {

        new NetworkValidator().validateFriendshipExists(this.friendships, user, friend);
        removeBiDirectionalFriendship(user, friend);

        removeUsersWithoutFriends();
    }

    public void removeUser(User user) {

        new UserValidator().validateUser(user);
        new NetworkValidator().validateUserBelongsToNetwork(this.friendships, user);

        friendships.values().forEach(friends -> friends.remove(user));
        friendships.remove(user);

        removeUsersWithoutFriends();
    }


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
