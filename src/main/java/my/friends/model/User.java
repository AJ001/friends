package my.friends.model;

import java.util.Comparator;

/**
 * Represents a user in the network
 */
public class User implements Comparable<User> {

    private static final Comparator<String> NULL_SAFE_COMPARATOR = Comparator.nullsFirst(String::compareToIgnoreCase);

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getUsername, NULL_SAFE_COMPARATOR)
            .thenComparing(User::getUsername, NULL_SAFE_COMPARATOR);

    private final String username;

    public User(String username) {

        this.username = username;
    }

    public String getUsername() {

        return username;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);
    }

    @Override
    public int hashCode() {

        return username.hashCode();
    }

    @Override
    public int compareTo(User user) {

        return USER_COMPARATOR.compare(this, user);
    }
}
