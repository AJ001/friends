# Friends Network

The **Friends Network** application allows defining a network of users and the friendships between them, modifying the
created network and search for paths between users.

## Concepts

A **user** represents the base entity of the network and any user has at least one friend.

A **friendship** represents the direct connection between two users from the network.

A **path** represents any sequence of adjacent users that link a source user to a target user.

## Supported Operations

Operations can be executed using commands.

**Create the network**

The network structure can be provided:

- at once via a file in which every line represents a user and his friends in the following format:
  `user: friend1, friend2`; every user needs to have at least one friend specified.
    - `create_network <absolute_path_to_file>`
- by adding friendships incrementally
    - `add_friendship <user> <friend>` or
    - `add_friendships <user>: <friend1>, <friend2>`

**Display the network**

Prints all users from the network in alphabetical order with their friendships in the format `user: friend1 friend2`.

`display_network`

**Add Friendship**

Adds a new bidirectional connection between two users; if any user is not yet part of the network, it is added to the
network.

`add_friendship <user> <friend>`

**Add Friendships**

Adds a new bidirectional connection between a user and each of its friends; if any user is not yet part of the network,
it is added to the network.

`add_friendships <user>: <friend1>, <friend2>`

**Remove Friendship**

Removes a bidirectional connection between two users; if any user has no more friends after this operation, it will be
removed from the network.

`remove_friendship <user> <friend>`

**Remove User**

Removes a users and all its friendships from the network;

`remove_user <user>`

**Find the length of the shortest path between two users**

Finds the shortest path length between two users from the network if any path exists.

`get_shortest_path_length <source_user> <target_user>`

**Find all the paths with the shortest length between two users**

Finds all the paths with the shortest length between two users.

`get_all_shortest_paths <source_user> <target_user>`

**Help**

Displays all supported commands.

`help`

**Exit**

Stops the application.

`exit`

## Running the application

### Prerequisites

- JDK 13 and Gradle 6.7 or higher compatible versions installed
- Check java and gradle versions with
```
java -version 
gradle -version
```

### How to run

```
./gradlew --console plain run
```

## Examples of Operations

**Create the network via an input file**

`create_network /Users/aj/Documents/friends/friends_network_example.txt`

If the network was successfully created, `Network successfully created` message will be displayed.

**Display the network**

`display_network`

For the previously created network, it will display:

```
Alice: Bob Katy Ema Mark
Bob: Rob Alice Katy Mark
Eddie: John
Ema: Alice Mark
John: Eddie
Katy: Bob Rob Alice Mark
Mark: Bob Alice Ema Katy
Rob: Bob Katy
```

**Find the length of the shortest path between two users**

`get_shortest_path_length Ema Rob`

For the previously created network, it will return `3`

**Find all the paths with the shortest length between two users**

`get_all_shortest_paths Ema Rob`

For the previously created network, it will return

```
Ema Alice Bob Rob 
Ema Alice Katy Rob 
Ema Mark Bob Rob 
Ema Mark Katy Rob 
```

**Add Friendship**

`add_friendship Eddie Katy`

If the friendship was successfully added, `Friendship successfully added` message will be displayed. The network will
become:

```
Alice: Bob Katy Ema Mark
Bob: Rob Alice Katy Mark
Eddie: John Katy
Ema: Alice Mark
John: Eddie
Katy: Bob Rob Alice Mark Eddie
Mark: Bob Alice Ema Katy
Rob: Bob Katy
```

**Add Friendships**

`add_friendships Ema: Eddie, Jim, Chris`

If the friendships were successfully added, `Friendships successfully added` message will be displayed. The network will
become:

```
Alice: Bob Katy Ema Mark
Bob: Rob Alice Katy Mark
Chris: Ema
Eddie: John Katy Ema
Ema: Alice Chris Mark Eddie Jim
Jim: Ema
John: Eddie
Katy: Bob Rob Alice Mark Eddie
Mark: Bob Alice Ema Katy
Rob: Bob Katy
```

_Note: Jim and Chris were not part of the network, so they have been added automatically._

**Remove Friendship**

`remove_friendship Jim Ema`

If the friendship was successfully removed, `Friendship successfully removed` message will be displayed. The network
will become

```
Alice: Bob Katy Ema Mark
Bob: Rob Alice Katy Mark
Chris: Ema
Eddie: John Katy Ema
Ema: Alice Chris Mark Eddie
John: Eddie
Katy: Bob Rob Alice Mark Eddie
Mark: Bob Alice Ema Katy
Rob: Bob Katy
```

_Note: Because Ema was Jim's only friend, after the friendship between them was removed, Jim no longer met the required
condition to be part of the network (to have at least one friend) and he was removed as well._

**Remove User**

`remove_user Bob`

If the user was successfully removed, `User successfully removed` message will be displayed. The network will become:

```
Alice: Katy Ema Mark
Chris: Ema
Eddie: John Katy Ema
Ema: Alice Chris Mark Eddie
John: Eddie
Katy: Rob Alice Mark Eddie
Mark: Alice Ema Katy
Rob: Katy
```

## Implementation details

**Network representation**

The Friendship Network is represented as a graph using adjacency lists. The implementation is a HashMap having the user
as key, and a HashSet of users as the friends. This representation was chosen instead of a graph using an adjacency
matrix to facilitate the operations which alter the network.

E.g. When a user remains without friends, and it needs to be removed from the network, the only operation required is to
remove the corresponding entry from the map (operation performed in O(1) because the map implementation is HashMap and
the User class used as key implements correctly hashCode(), equals() and compareTo() methods). For an adjacency matrix,
rows (to the left) and columns (upwards) shifting would be needed to remove a user from the network.

Another reason is that adjacency lists allow holding only exiting friendships, while an adjacency matrix needs to hold
information about both existing and non-existing friendships (extra space is used);

**Algorithm used for finding the shortest path length**

Bidirectional Breadth First Search (BFS) is used for finding the shortest path length. This algorithm was chosen instead
of other alternatives like Depth First Search (DFS), unidirectional BFS or Dijkstra.

Dijkstra was not chosen because for an undirected and unweighted graph, its complexity is similar with BFS.
DFS was not chosen because the shortest path length can be found only after all paths are traversed
(at any point, a shortest path which was not yet traversed can exist).

Bidirectional BFS was chosen over unidirectional BFS because by searching from both ends (source and target), reduces
the number of visited nodes given the average branching factor of the tree created while visiting the nodes. Identifying
a common node in the middle is often more efficient than browsing through the entire distance between the source and
target nodes.

**Algorithm used for finding all the paths with the shortest length**

After the shortest path length is determined, DFS algorithm is used for searching all the paths with that exact length
between the source and target users. During the search, if a path reaches the shortest length without reaching the
target node, the in-depth search is abandoned on that path.

**Test cases**

- Let Alice be the source user and Bob the target user;

    - Alice and Bob are friends
        - the shortest path length should be `1`
        - a single path with the shortest length should be found `Alice Bob`
        - tests direct connection (friendship)
    - There is no path in the network between Alice and Bob (the network is represented as two separate graphs)
        - no shortest path length should be found
        - no path with the shortest length should be found
        - tests that no path is found when there is none, and the algorithm completes without failing
    - Multiple paths (including multiple paths with the shortest length and longer paths) exist between Alice and Bob
        - the shortest path length is correctly identified
        - all the paths with the shortest length are identified even when there is an intermediary user which is accessible
        from multiple paths; E.g:
         ```
        Alice Ema John Bob 
        Alice Mark John Bob
        ```
    - Alice or/and Bob are not part of the network
        - the search will not be performed (there is a validation step which makes sure that both users are part of the
        network before starting the search algorithm)
        - tests that there is a difference between the cases when no path exists between two users from the network, and the
        cases when no path exists because at least one user is not part of the network
-  The source user is the same as the target user
    - the search will not be performed because a user is not a friend of itself

