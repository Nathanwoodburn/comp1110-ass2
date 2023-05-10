package comp1110.ass2;

import java.util.*;

/**
 * Object to store the game state
 * This stores the state of the game in a way that is easy to access and modify
 * It stores the board height, number of players, current player, current phase, islands, stones,
 * unclaimed resources and players (with their data)
 *
 */
public class State {

    // region Constants
    // Constants for the game. Changing these could create errors
    final int maxPlayers = 4;

    // endregion

    // region Variables
    final int boardHeight;
    private int numPlayers;
    private int currentPlayer;
    private int currentPhase; // 0 for exploration, 1 for settlement

    private Island[] islands;
    final private Coord[] stonesCoords;
    private Resource[] resources;
    private Player[] players;
    // endregion

    // region Setup methods/constructors
    /**
     * Constructor for the state object
     * This takes a string containing the state of the game and initialises the object
     * @param stateString String containing the state of the game
     */
    public State(String stateString) {

        // Split the state string into its components
        String[] components = stateString.split(";");

        // For the game initialisation part
        String[] gameInitComponents = components[0].trim().split(" ");

        boardHeight = Integer.parseInt(gameInitComponents[1]);
        numPlayers = Integer.parseInt(gameInitComponents[2]);


        // Current state part
        String[] currentStateComponents = components[1].trim().split(" ");
        currentPlayer = Integer.parseInt(currentStateComponents[1]);

        if (currentStateComponents[2].equals("E")) currentPhase = 0;
        else currentPhase = 1;

        // Islands part
        int islandcount = 0;
        for (int i=2; i< components.length; i++)
        {
            // Split island part
            String[] islandComponents = components[i].trim().split(" ");
            // Check if the component is still an island
            if (!islandComponents[0].equals("i")) break;

            Island tmpIsland = new Island(Integer.parseInt(islandComponents[1]));

            // Add each coord
            for (int j=2; j<islandComponents.length; j++)
            {
                String[] coordComponents = islandComponents[j].split(",");
                tmpIsland.addCoord(new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1])));
            }

            islandcount++;
            // Add the island to the array
            Island[] tmpislands = new Island[islandcount];
            if (islands != null) {
                System.arraycopy(islands, 0, tmpislands, 0, tmpislands.length - 1);
            }
            tmpislands[islandcount-1] = tmpIsland;
            islands = tmpislands;
        }

        // Stones part
        String[] stonesComponents = components[2+islandcount].trim().split(" ");
        stonesCoords = new Coord[stonesComponents.length-1];
        for (int i=1; i<stonesComponents.length; i++)
        {
            String[] coordComponents = stonesComponents[i].split(",");
            stonesCoords[i-1] = new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1]));
        }

        // Unclaimed resources part
        String[] resourcesComponents = components[3+islandcount].trim().split(" ");
        resources = new Resource[resourcesComponents.length-6];
        int currentResource = 0;
        char type = 'C';
        for (int i=1; i<resourcesComponents.length; i++)
        {
            if (resourcesComponents[i].matches("[CBWPS]")){
                currentResource++;
                type = resourcesComponents[i].charAt(0);
                continue;
            }
            String[] coordComponents = resourcesComponents[i].split(",");
            Coord tmpCoord = new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1]));
            resources[i-1-currentResource] = new Resource(type, tmpCoord);
        }

        // Players part
        players = new Player[numPlayers];
        for (int i=0; i<numPlayers; i++)
        {
            String[] playerComponents = components[4+islandcount+i].trim().split(" ");
            players[i] = new Player(Integer.parseInt(playerComponents[1]));
            players[i].addScore(Integer.parseInt(playerComponents[2]));
            players[i].addResource(Integer.parseInt(playerComponents[3]),'C');
            players[i].addResource(Integer.parseInt(playerComponents[4]),'B');
            players[i].addResource(Integer.parseInt(playerComponents[5]),'W');
            players[i].addResource(Integer.parseInt(playerComponents[6]),'P');
            players[i].addResource(Integer.parseInt(playerComponents[7]),'S');

            int pieceType = 0;
            for (int j=8; j<playerComponents.length; j++)
            {
                if (playerComponents[j].matches("[ST]")){
                    pieceType++;
                    continue;
                }

                if (pieceType == 1) {
                    String[] coordComponents = playerComponents[j].split(",");
                    players[i].addSettler(new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1])));
                }
                else if (pieceType == 2) {
                    String[] coordComponents = playerComponents[j].split(",");
                    players[i].addVillage(new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1])));
                }
            }
        }
    }

    /**
     * Add a player to the game
     * This will add a player to the game if there is less than the maximum number of players
     */
    public void addPlayer(){
        if (numPlayers >= maxPlayers) return; // There are already the maximum number of players
        Player[] oldPlayers = players;
        players = new Player[numPlayers+1];
        if (numPlayers >= 0) System.arraycopy(oldPlayers, 0, players, 0, numPlayers);
        players[numPlayers] = new Player(numPlayers);
        numPlayers++;
    }

    /**
     * Distribute the resources to the stone circles randomly
     */
    public void distributeResources() {
        if (stonesCoords.length != 32) return; // There are not enough stones to distribute resources


        // Create a random object and an arrays and list to shuffle the stone circles
        Random rand = new Random();

        // Number of times to shuffle the stone circles (can be changed)
        int shuffle_number = 3;

        // Create a copy of the stone circle array to shuffle
        Coord[] stoneCircleRandom = stonesCoords;

        // Shuffle the stone circles the specified number of times
        for (int i = 0; i < shuffle_number; i++) {
            // Create a temporary array to store the shuffled stone circles
            Coord[] tempStoneCircleRandom = new Coord[32];
            // Create a list to store the used cords (to avoid duplicates)
            List<Coord> usedCords = new ArrayList<>();

            // Shuffle the array
            for (int j = 0; j < 32; j++) {
                // For 0-31 generate a random cord from the stone circle array and check if it has been used
                int randomIndex = rand.nextInt(31);
                while (usedCords.contains(stonesCoords[randomIndex])) {
                    // If it has been used, try the next in line
                    if (randomIndex == 31) {
                        randomIndex = 0;
                    }
                    else randomIndex++;
                }
                // If it hasn't been used, add it to the new array
                tempStoneCircleRandom[j] = stoneCircleRandom[randomIndex];
                usedCords.add(stonesCoords[randomIndex]);
            }
            // Replace the old array with the new one
            stoneCircleRandom = tempStoneCircleRandom;
        }
        // Initialise unclaimed resources
        resources = new Resource[32];

        // Create an array for each resource type
        char[] toDistribute = {'C', 'B', 'W', 'P'};

        // Create a variable to keep track of how many resources have been sorted
        int numSorted = 0;

        // For each resource type
        for (char r:toDistribute){
            // Assign 6 to a stone circle
            for (int i = 0; i < 6; i++){
                resources[numSorted] = new Resource(r, stoneCircleRandom[numSorted]);
                numSorted++;
            }
        }

        // Assign 8 statuettes to a stone circle
        for (int i = 0; i < 8; i++){
            resources[numSorted] = new Resource('S', stoneCircleRandom[numSorted]);
            numSorted++;
        }
    }

    // endregion

    // region Getters/Setters
    /**
     * Get the board height
     * @return int board height
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Get the number of players
     * @return int number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Get the current player ID
     * @return int current player
     */
    public int getCurrentPlayerID() {
        return currentPlayer;
    }
    /**
     * Get the current player
     * @return Player current player
     */
    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    /**
     * Get the current phase
     * Returns 'E' for exploration, 'S' for settlement, 'O' for game over
     * @return char current phase
     */
    public char getCurrentPhase() {
        if (currentPhase == 0) return 'E';
        else if (currentPhase == 1) return 'S';
        else return 'O'; // Return 'O' for game over
    }

    /**
     * Get the islands
     * @return Island[] islands
     */
    public Island[] getIslands() {
        return islands;
    }

    /**
     * Get one island (by 0 index)
     * @return Island island
     */
    public Island getIsland(int i) {
        return islands[i];
    }

    /**
     * Get the stones
     * @return Coord[] stones
     */
    public Coord[] getStones() {
        return stonesCoords;
    }

    /**
     * Check if a stone is at a given coordinate
     * @param coord Coord coordinate to check
     * @return boolean true if stone is at coordinate
     */
    public boolean isStone(Coord coord) {
        for (Coord stone : stonesCoords) {
            if (stone.equals(coord)) return true;
        }
        return false;
    }

    /**
     * Get the player with a given index
     * @param i int index of player
     * @return Player player
     */
    public Player getPlayer(int i) {
        return players[i];
    }

    /**
     * Get the unclaimed resources
     * @return Resource[] unclaimed resources
     */
    public Resource[] getResources() {
        return resources;
    }

    /**
     * Get the unclaimed resources of a given type
     * @param type char type of resource
     * @return Resource[] unclaimed resources of type
     */
    public Resource[] getResources(char type) {
        Resource[] tmpResources = new Resource[resources.length];
        int i = 0;
        for (Resource resource : resources) {
            if (resource.getType() == type){
                tmpResources[i] = resource;
                i++;
            }
        }
        Resource[] resources = new Resource[i];
        System.arraycopy(tmpResources, 0, resources, 0, i);
        return resources;
    }

    /**
     * Get the unclaimed resource at a given coordinate
     * @param coord Coord coordinate to check
     * @return Resource unclaimed resource at coordinate
     */
    public Resource getUnclaimedResource(Coord coord) {
        for (Resource resource : resources) {
            if (resource.getCoord().equals(coord)) return resource;
        }
        return null;
    }

    // endregion

    // region Game play functions
    /**
     * Start next player's turn
     */
    public void nextPlayer() {
        currentPlayer++;
        if (currentPlayer >= numPlayers) currentPlayer = 0;
    }

    /**
     * Start next phase
     */
    public void nextPhase() {
        currentPhase++;
    }

    /**
     * Place a piece on the board. Uses current turn's player
     * This does not check if the move is valid (Use isValidMove() first)
     * @param coord Coord coordinate to place piece
     * @param type char type of piece
     */
    public void placePiece(Coord coord, char type) {
        if (type == 'S') {
            players[currentPlayer].addSettler(coord);
        }
        else if (type == 'V' || type == 'T') {
            players[currentPlayer].addVillage(coord);
        }

        // Claim resource if it is a stone circle
        if (isStone(coord)) {
            for (Resource resource : resources) {
                if (resource.getCoord().equals(coord) && resource.isAvailable()) {
                    players[currentPlayer].addResource(1, resource.getType());
                    resource.setClaimed();
                }
            }
        }
    }

    /**
     * Is move valid?
     * @param coord Coord coordinate to place piece
     * @param type char type of piece
     * @return boolean true if move is valid
     */
    public boolean isMoveValid(Coord coord, char type){
        // Create moveString
        String moveString = type + " " + coord.toString();
        return BlueLagoon.isMoveValid(this.toString(), moveString);
    }

    /**
     * is the phase over?
     */
    public boolean isPhaseOver() {
        boolean resourcesLeft = false;
        for (Resource r : resources) {
            if (r.isAvailable() && r.getType() != 'S') resourcesLeft = true;
        }
        boolean moveLeft = false;
        int numSettlers = 30 - ((numPlayers - 2) * 5);
        for (Player p: players) {
            boolean canPlay = p.getVillages().length < 5;
            if (p.getSettlers().length < numSettlers) canPlay = true;
            if (canPlay) {
                if (p.canPlay(this)) moveLeft = true;
            }

        }
        return !resourcesLeft || !moveLeft;
    }

    /**
     * Clean board for next phase
     */
    public void cleanBoard(){
        for (Player player : players) {
            player.clearSettlers();
            player.removeResources();
            for (Coord coord : player.getVillages()) {
                if (isStone(coord)) {
                    player.removeVillage(coord);
                }
            }
        }
    }


    // endregion

    // region Scoring

    /**
     * Score for that phase
     * This does not automatically move to the next phase
     */
    public void scorePhase() {
        for (Player p: players) {
            p.addScore(createScore(p.getPlayerID()));
        }
    }

    /**
     * Get score of player ID based on current phase and game state
     * @param playerID int player ID base 0
     * @return int score
     */
    public int createScore(int playerID) {
        int score = 0;
        score += scoreTotalIslands(playerID);
        score += scoreLinks(playerID);
        score += scoreMajorities(playerID);
        score += scoreResources(playerID);
        score += scoreStatuettes(playerID);
        return score;
    }

    /**
     * Get score from total islands
     * @param playerID int player to score
     * @return int score
     */
    public int scoreTotalIslands(int playerID) {
        int score = 0;
        int islandCount = 0;
        for (Island island : islands) {
            // Get island coords
            Coord[] islandCoords = island.getCoords();
            // Get player's coords
            Coord[] playerCoords = players[playerID].getPieces();
            // Check if player has a piece on the island
            boolean hasPiece = false;
            for (Coord playerCoord : playerCoords) {
                for (Coord islandCoord : islandCoords) {
                    if (playerCoord.equals(islandCoord)) {
                        hasPiece = true;
                        break;
                    }
                }
                if (hasPiece) break;
            }
            if (hasPiece) islandCount++;
        }

        if (islandCount >= 8) score = 20;
        else if (islandCount == 7) score = 10;

        return score;
    }

    /**
     * Score majorities
     * @param playerID int player to score
     * @return int score
     */
    public int scoreMajorities(int playerID){
        int score = 0;

        for (Island island:islands){
            int[] playerPieces = new int[getNumPlayers()];

            for (int i = 0; i < getNumPlayers(); i++){
                playerPieces[i] = players[i].getNumPiecesOnIsland(island);
            }
            boolean ishighest = true;
            int ties = 0;
            for (int i = 0; i < getNumPlayers(); i++){
                if (i == playerID) continue;
                if (playerPieces[i] > playerPieces[playerID]) {
                    ishighest = false;
                    break;
                }
                if (playerPieces[i] == playerPieces[playerID]) ties++;
            }
            if (ishighest && playerPieces[playerID] > 0) {
                if (ties > 0){
                    score += island.getBonus()/(ties + 1);
                }
                else {
                    score += island.getBonus();
                }
            }
        }

        return score;
    }

    /**
     * Score Links
     * A (potentially) branching path of neighbouring settlers and villages
     * belonging to a player forms a chain. Players earn points from the chain
     * of their pieces which links the most islands. Players earn 5 points
     * per linked island in this chain.
     * @param playerID int player to score
     * @return int score
     */

    public int scoreLinks(int playerID) {
        Coord[] playerCoords = players[playerID].getPieces();
        Set<Coord> playerCoordsSet = new HashSet<>(Arrays.asList(playerCoords));
        return findLongestLinkScore(playerCoordsSet, islands);
    }

    /**
     * Combines both the helper methods `DFSRecurssionLink` and `findScoreForLink` to find the link set that has the
     * highest score from a set all Player's pieces coordinates
     * @param allCoords all coordinates of a Player's pieces
     * @param islands the set of islands from the generated world
     * @return the highest score from a player's linked pieces
     */
    public static int findLongestLinkScore ( Set<Coord> allCoords, Island[] islands) {
        Set<Coord> longestPath = new HashSet<>(); // Container for Longest Path
        Set<Coord> currentPath = new HashSet<>(); // Container for Current Path

        Coord startingPoint; // Starting point of the DFS Algo
        int maxScore = findScoreForLink(longestPath,islands);

        for(Island i : islands) {
            for ( Coord c : allCoords) {
                if(i.containsCoord(c)) {

                    startingPoint = c; // set starting Point with the current Coords in the Player's all pieces coords

                    // DFS Algo starts
                    currentPath.add(startingPoint);
                    DFSRecursionLink(allCoords, currentPath, longestPath, startingPoint);

                    // if the score is bigger, update the Set
                    if(findScoreForLink(currentPath, islands) > maxScore) {
                        longestPath.clear();
                        longestPath.addAll(currentPath);

                        maxScore = findScoreForLink(longestPath, islands);
                    }

                    // clear the currentPath as well
                    currentPath = new HashSet<>();
                }
            }
        }
        return maxScore;
    }

    /**
     * a DFS (Depth-First-Search) Recursion algorithm that traverses all the coordinates of a player's pieces, with
     * each step of the DFS algorithm determined if the current coordinate is adjacent with other coordinates
     * within the allCoords set and wether or not the currentPath is contained inside the allCoords.
     * @param allCoords all coordinates of a Player's pieces
     * @param currentPath a set to store the path that the algorithm has gone through
     * @param longestPath a set to store the longest path
     * @param startingPoint the starting point of each step of DFS
     */
    public static void DFSRecursionLink(Set<Coord> allCoords, Set<Coord> currentPath, Set<Coord> longestPath, Coord startingPoint) {

        // Add the staring point to the `history` of the path that has been taken by the algorithm
        currentPath.add(startingPoint);

        for(Coord c : allCoords) {

            // if the startingPoint of the step is adjacent with the coords from allCoords and currentPath
            // i.e. the `history tracker` of the path so far does not have `c` coords from allCoords
            if( startingPoint.isAdjacentDiagonal(c) && !currentPath.contains(c) ) {

                // if the currentPath is bigger than the longestPath, update the longest Path
                if (currentPath.size() > longestPath.size() ) longestPath = currentPath;

                DFSRecursionLink(allCoords, currentPath, longestPath, c); // Repeat the step for all coords
            }
        }
    }

    /**
     * Helper method for finding the scores for a link set. This helper method is purely to help the
     * `findLongestLink` method.
     * @param longestLink the link that wants to be searched the score for
     * @param islands the islands within the world
     * @return the score of the link set
     */
    public static int findScoreForLink(Set<Coord> longestLink, Island[] islands) {
        Set<Island> connectedIslands = new HashSet<>(); // Container for connected Islands within the longest Link set

        for (Coord c : longestLink) {
            for (Island i : islands) {
                if (i.containsCoord(c)) {
                    connectedIslands.add(i);
                    break;
                }
            }
        }
        return connectedIslands.size() * 5;
    }

    /**
     * Score resources
     * @param playerID int player to score
     * @return int score
     */
    public int scoreResources(int playerID) {
        int score = 0;
        char[] resourceTypes = {'C', 'B', 'W', 'P'};
        for (char type : resourceTypes) {
            int numResources = players[playerID].getNumResource(type);
            if (numResources >= 4) {
                score += 20;
            } else if (numResources == 3) {
                score += 10;
            } else if (numResources == 2) {
                score += 5;
            }
        }
        int numCoconuts = players[playerID].getNumResource('C');
        int numBananas = players[playerID].getNumResource('B');
        int numWater = players[playerID].getNumResource('W');
        int numPreciousStones = players[playerID].getNumResource('P');
        if (numCoconuts >= 1 && numBananas >= 1 && numWater >= 1 && numPreciousStones >= 1) {
            score += 10;
        }
        return score;
    }

    /**
     * Score statuettes
     * @param playerID int player to score
     * @return int score
     */
    public int scoreStatuettes(int playerID) {
        return players[playerID].getNumResource('S') * 4;
    }

    // endregion

    // region String conversion

    /**
     * Convert board to stateString format string
     * @return String stateString
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("a " + boardHeight + " " + getNumPlayers() + "; c " + getCurrentPlayerID() + " " + getCurrentPhase() + "; ");
        for (Island island : islands) {
            str.append(island.toString()).append(" ");
        }
        str.append("s");
        for (Coord s: stonesCoords) {
            str.append(" ").append(s.toString());
        }
        str.append("; r");

        char[] types = {'C', 'B', 'W', 'P', 'S'};
        for (char type : types) {
            str.append(" ").append(type);
            for (Resource resource : resources) {
                if (resource.getType() == type && resource.isAvailable()) str.append(" ").append(resource.getCoord().toString());
            }
        }
        str.append(";");
        for (Player player : players) {
            str.append(" ").append(player.toString()).append(";");
        }
        return str.toString();
    }
    // endregion

}
