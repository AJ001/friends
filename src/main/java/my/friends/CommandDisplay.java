package my.friends;

import static my.friends.Command.ADD_FRIENDSHIP;
import static my.friends.Command.ADD_FRIENDSHIPS;
import static my.friends.Command.CREATE_NETWORK;
import static my.friends.Command.DISPLAY_NETWORK;
import static my.friends.Command.EXIT;
import static my.friends.Command.GET_ALL_SHORTEST_PATHS;
import static my.friends.Command.GET_SHORTEST_PATH_LENGTH;
import static my.friends.Command.HELP;
import static my.friends.Command.REMOVE_FRIENDSHIP;
import static my.friends.Command.REMOVE_USER;

/**
 * Responsible of displaying the available commands
 */
public final class CommandDisplay {

    /**
     * Hides the default constructor to avoid instance creation
     */
    private CommandDisplay() {
    }

    /**
     * Display the available commands and their description
     */
    public static void displayCommands() {

        System.out.println("============================");
        displayCommand(CREATE_NETWORK, makeBold(" <file_path>") + " - creates a network of friends from the file path specified as argument");
        displayCommand(DISPLAY_NETWORK, " - displays the current network");
        displayCommand(GET_SHORTEST_PATH_LENGTH, makeBold(" <source_user> <target_user>") + " - displays the shortest path length between <source_user> and <target_user> if any path exists");
        displayCommand(GET_ALL_SHORTEST_PATHS, makeBold(" <source_user> <target_user>") + " - displays all the paths with the shortest length between <source_user> and <target_user> if any path exists");
        displayCommand(ADD_FRIENDSHIP, makeBold(" <user> <friend>") + " - adds friendship in the current network between <user> and <friend>");
        displayCommand(ADD_FRIENDSHIPS, makeBold(" <user>: <friends>") + " - adds friendships in the current network between <user> and all <friends>");
        displayCommand(REMOVE_FRIENDSHIP, makeBold(" <user> <friend>") + " - removes friendship between <user> and <friend>");
        displayCommand(REMOVE_USER, makeBold(" <user>") + " - removes <user> from the network");
        displayCommand(HELP, " - displays the options menu");
        displayCommand(EXIT, " - exits the application");
        System.out.println("============================");
    }

    private static void displayCommand(Command createNetwork, String string) {

        System.out.println(makeBold(createNetwork.getValue()) + string);
        System.out.println();
    }

    private static String makeBold(String string) {

        return "\033[1m" + string + "\033[0m";
    }
}
