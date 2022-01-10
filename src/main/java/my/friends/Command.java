package my.friends;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds the available commands
 */
public enum Command {

    CREATE_NETWORK("create_network"),
    DISPLAY_NETWORK("display_network"),
    GET_SHORTEST_PATH_LENGTH("get_shortest_path_length"),
    GET_ALL_SHORTEST_PATHS("get_all_shortest_paths"),
    ADD_FRIENDSHIP("add_friendship"),
    ADD_FRIENDSHIPS("add_friendships"),
    REMOVE_FRIENDSHIP("remove_friendship"),
    REMOVE_USER("remove_user"),
    HELP("help"),
    EXIT("exit");

    private static final Map<String, Command> ENUM_VALUES = Arrays.stream(Command.values())
            .collect(Collectors.toUnmodifiableMap(Command::getValue, Function.identity()));

    private final String value;

    Command(final String value) {
        this.value = value;
    }

    public String getValue() {

        return this.value;
    }

    /**
     * Returns the {@link Command} corresponding to the received value
     *
     * @param value the value for which the corresponding {@link Command} is wanted
     * @return the corresponding {@link Command}
     * @throws IllegalArgumentException in case no {@link Command} is found
     */
    public static Command fromString(String value) {

        if (ENUM_VALUES.containsKey(value)) {
            return ENUM_VALUES.get(value);
        }

        throw new IllegalArgumentException("Unknown command: " + value);
    }
}
