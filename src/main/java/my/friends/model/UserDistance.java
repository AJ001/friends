package my.friends.model;

/**
 * Holds the distance from source node for the current user
 */
public final class UserDistance {

    private final User user;
    private final int distance;

    public UserDistance(final User user, final int distance) {

        this.user = user;
        this.distance = distance;
    }

    public User getUser() {
        return user;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDistance that = (UserDistance) o;

        return distance == that.distance && user.equals(that.user);
    }

    @Override
    public int hashCode() {

        int result = user.hashCode();
        result = 31 * result + distance;
        return result;
    }
}
