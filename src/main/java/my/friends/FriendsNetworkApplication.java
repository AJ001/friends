package my.friends;

import static my.friends.Command.ADD_FRIENDSHIPS;
import static my.friends.CommandDisplay.displayCommands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import my.friends.exception.ApplicationException;
import my.friends.finder.AllShortestPathsFinder;
import my.friends.finder.ShortestPathLengthFinder;
import my.friends.model.FriendsNetwork;
import my.friends.model.Path;
import my.friends.model.PathFinderInput;
import my.friends.model.User;

/**
 * Application entry point
 */
public class FriendsNetworkApplication {

    public static final int TWO_ARGUMENTS_COMMAND_LENGTH = 3;
    public static final int SINGLE_ARGUMENT_COMMAND_LENGTH = 2;
    private static final String EXIT_COMMAND = "exit";
    private static final String LINE_PATTERN = "([a-zA-Z0-9]+):([a-zA-Z0-9]+)(,([a-zA-Z0-9]+))*";
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    private static final String INPUT_LINE_PREFIX = "> ";
    private static final String COMMA = ",";
    private static final String COLON = ":";
    private FriendsNetwork CURRENT_NETWORK;

    public static void main(String... args) {

        new FriendsNetworkApplication().run();
    }

    private void run() {

        displayCommands();
        String command;

        do {
            System.out.printf("%n" + INPUT_LINE_PREFIX);
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextLine();

            try {
                execute(command);
            } catch (ApplicationException | IllegalArgumentException applicationException) {
                System.out.println(applicationException.getMessage());
            }
        } while (!EXIT_COMMAND.equals(command));
    }

    private void execute(String command) {

        String[] commandWithArguments = command.split(SPACE);
        Command option = Command.fromString(commandWithArguments[0]);

        switch (option) {
            case CREATE_NETWORK:
                createNetwork(commandWithArguments);
                break;
            case DISPLAY_NETWORK:
                displayNetwork();
                break;
            case GET_SHORTEST_PATH_LENGTH:
                getShortestPathLength(commandWithArguments);
                break;
            case GET_ALL_SHORTEST_PATHS:
                getAllShortestPaths(commandWithArguments);
                break;
            case ADD_FRIENDSHIP:
                addFriendship(commandWithArguments);
                break;
            case ADD_FRIENDSHIPS:
                addFriendships(command);
                break;
            case REMOVE_FRIENDSHIP:
                removeFriendship(commandWithArguments);
                break;
            case REMOVE_USER:
                removeUser(commandWithArguments);
                break;
            case HELP:
                displayCommands();
                break;
        }
    }

    private void createNetwork(String[] commandWithArguments) {

        CURRENT_NETWORK = new FriendsNetwork();

        try {
            List<String> lines = Files.readAllLines(Paths.get(commandWithArguments[1]));
            lines.forEach(this::addUserWithFriends);

            System.out.println("Network successfully created");
        } catch (IOException ioException) {
            System.out.println("Unable to read the file");
        }
    }

    private void addFriendships(String command) {

        String userWithFriends = command.substring(ADD_FRIENDSHIPS.getValue().length());
        createNetworkIfDoesNotExists();

        addUserWithFriends(userWithFriends);

        System.out.println("Friendships successfully added");
    }

    private void addFriendship(String[] commandWithArguments) {

        if (isCommandLengthInvalid(commandWithArguments, TWO_ARGUMENTS_COMMAND_LENGTH)) {
            return;
        }

        createNetworkIfDoesNotExists();

        CURRENT_NETWORK.addFriendshipFor(new User(commandWithArguments[1]), new User(commandWithArguments[2]));

        System.out.println("Friendship successfully added");
    }

    private void createNetworkIfDoesNotExists() {

        if (CURRENT_NETWORK == null) {
            CURRENT_NETWORK = new FriendsNetwork();
        }
    }

    private void addUserWithFriends(String line) {

        String lineWithoutSpaces = line.replaceAll(SPACE, EMPTY_STRING);

        if (EMPTY_STRING.equals(lineWithoutSpaces)) {
            return;
        }

        if (!lineWithoutSpaces.matches(LINE_PATTERN)) {
            throw new IllegalArgumentException("Invalid format for line: " + line);
        }

        String[] userWithFriends = lineWithoutSpaces.split(COLON);
        User user = new User(userWithFriends[0]);

        String[] userFriends = userWithFriends[1].split(COMMA);
        User[] friends = Arrays.stream(userFriends)
                .map(User::new)
                .collect(Collectors.toList())
                .toArray(new User[userFriends.length]);

        CURRENT_NETWORK.addFriendshipsFor(user, friends);
    }

    private void displayNetwork() {

        if (CURRENT_NETWORK == null) {
            System.out.println("The network has not been initialised");
            return;
        }

        CURRENT_NETWORK.displayNetwork();
    }

    private void getShortestPathLength(String[] commandWithArguments) {

        if (isCommandLengthInvalid(commandWithArguments, TWO_ARGUMENTS_COMMAND_LENGTH)) {
            return;
        }

        PathFinderInput pathFinderInput = new PathFinderInput(CURRENT_NETWORK.getFriendships(), new User(commandWithArguments[1]),
                new User(commandWithArguments[2]));

        Optional<Integer> shortestPathLength = new ShortestPathLengthFinder().find(pathFinderInput);

        if (shortestPathLength.isPresent()) {
            System.out.printf("Shortest path length: %d%n", shortestPathLength.get());
        } else {
            System.out.println("There is no path between users");
        }
    }

    private void getAllShortestPaths(String[] commandWithArguments) {

        if (isCommandLengthInvalid(commandWithArguments, TWO_ARGUMENTS_COMMAND_LENGTH)) {
            return;
        }

        PathFinderInput pathFinderInput = new PathFinderInput(CURRENT_NETWORK.getFriendships(), new User(commandWithArguments[1]),
                new User(commandWithArguments[2]));

        List<Path> paths = new AllShortestPathsFinder().find(pathFinderInput);
        if (paths.isEmpty()) {
            System.out.println("There is no path between users");
            return;
        }

        paths.forEach(Path::display);
    }

    private void removeFriendship(String[] commandWithArguments) {

        if (isCommandLengthInvalid(commandWithArguments, TWO_ARGUMENTS_COMMAND_LENGTH)) {
            return;
        }

        CURRENT_NETWORK.removeFriendship(new User(commandWithArguments[1]), new User(commandWithArguments[2]));

        System.out.println("Friendship successfully removed");
    }

    private void removeUser(String[] commandWithArguments) {

        if (isCommandLengthInvalid(commandWithArguments, SINGLE_ARGUMENT_COMMAND_LENGTH)) {
            return;
        }

        CURRENT_NETWORK.removeUser(new User(commandWithArguments[1]));

        System.out.println("User successfully removed");
    }

    private boolean isCommandLengthInvalid(String[] commandWithArguments, int expectedLength) {

        if (commandWithArguments.length != expectedLength) {
            System.out.println("Invalid input");
            return true;
        }

        return false;
    }
}
