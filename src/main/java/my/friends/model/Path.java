package my.friends.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a sequence of adjacent users from the network
 */
public class Path {

    private final Set<User> usersInPath;

    public Path() {

        usersInPath = new LinkedHashSet<>();
    }

    public Path(Path path) {

        usersInPath = new LinkedHashSet<>(path.usersInPath);
    }

    public Path(Set<User> usersInPath) {

        this.usersInPath = new LinkedHashSet<>(usersInPath);
    }

    public Set<User> getUsersInPath() {

        return Collections.unmodifiableSet(this.usersInPath);
    }

    public void addUser(User user) {

        usersInPath.add(user);
    }

    public int getLength() {

        return (usersInPath.isEmpty()) ? 0 : usersInPath.size() - 1;
    }

    public void display() {

        usersInPath.forEach(user -> System.out.print(user.getUsername() + " "));
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        return Objects.equals(usersInPath, path.usersInPath);
    }

    @Override
    public int hashCode() {

        return usersInPath.hashCode();
    }
}
