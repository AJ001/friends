package my.friends.model;

import java.util.Queue;

/**
 * Holds the users which need to be visited from both directions
 */
public class UsersToVisit {

    private final Queue<UserDistance> usersForSource;
    private final Queue<UserDistance> usersForTarget;

    public UsersToVisit(Queue<UserDistance> usersForSource, Queue<UserDistance> usersForTarget) {

        this.usersForSource = usersForSource;
        this.usersForTarget = usersForTarget;
    }

    public Queue<UserDistance> getUsersForSource() {

        return usersForSource;
    }

    public Queue<UserDistance> getUsersForTarget() {

        return usersForTarget;
    }

    /**
     * Checks if any of the queues still has elements
     *
     * @return true if at least one queue is not empty, false otherwise
     */
    public boolean hasElements() {

        return usersForSource.size() > 0 || usersForTarget.size() > 0;
    }
}
