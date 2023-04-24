package comp1110.ass2;

/**
 * Object to store the game state
 * This stores the state of the game in a way that is easy to access and modify
 * It stores the board height, number of players, current player, current phase, islands, stones,
 * unclaimed resources and players (with their data)
 *
 */
public class State {
    final int boardHeight;
    private int numPlayers;
    private int currentPlayer;
    private int currentPhase; // 0 for exploration, 1 for settlement

    private Island[] islands;
    private Coord[] stonesCoords;
    private Resource[] unclaimedResources;
    private Player[] players;

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
            for (int j=0; j<tmpislands.length-1; j++)
            {
                tmpislands[j] = islands[j];
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
        String[] unclaimedResourcesComponents = components[3+islandcount].trim().split(" ");
        unclaimedResources = new Resource[unclaimedResourcesComponents.length-1];
        int currentResource = 0;
        char type = 'C';
        for (int i=1; i<unclaimedResourcesComponents.length; i++)
        {
            if (unclaimedResourcesComponents[i].matches("[CBWPS]")){
                currentResource++;
                type = unclaimedResourcesComponents[i].charAt(0);
                continue;
            }

            String[] coordComponents = unclaimedResourcesComponents[i].split(",");
            Coord tmpCoord = new Coord(Integer.parseInt(coordComponents[0]), Integer.parseInt(coordComponents[1]));
            unclaimedResources[i-1-currentResource] = new Resource(type, tmpCoord);
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
     * Get the current player
     * @return int current player
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Get the current phase
     * @return char current phase
     */
    public char getCurrentPhase() {
        if (currentPhase == 0) return 'E';
        else return 'S';
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

    public Player getPlayer(int i) {
        return players[i];
    }

    /**
     * Get the unclaimed resources
     * @return Resource[] unclaimed resources
     */
    public Resource[] getUnclaimedResources() {
        return unclaimedResources;
    }

    /**
     * Get the unclaimed resources of a given type
     * @param type char type of resource
     * @return Resource[] unclaimed resources of type
     */
    public Resource[] getUnclaimedResources(char type) {
        Resource[] tmpResources = new Resource[unclaimedResources.length];
        int i = 0;
        for (Resource resource : unclaimedResources) {
            if (resource.getType() == type){
                tmpResources[i] = resource;
                i++;
            }
        }
        Resource[] resources = new Resource[i];
        for (int j=0; j<i; j++) {
            resources[j] = tmpResources[j];
        }
        return resources;
    }
}
