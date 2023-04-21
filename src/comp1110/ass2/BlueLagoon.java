package comp1110.ass2;

import java.util.*;

public class BlueLagoon {
    // The Game Strings for five maps have been created for you.
    // They have only been encoded for two players. However, they are
    // easily extendable to more by adding additional player statements.
    public static final String DEFAULT_GAME = "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    public static final String WHEELS_GAME = "a 13 2; c 0 E; i 5 0,1 0,2 0,3 0,4 1,1 1,5 2,0 2,5 3,0 3,6 4,0 4,5 5,1 5,5 6,1 6,2 6,3 6,4; i 5 0,8 0,9 0,10 1,8 1,11 2,7 2,11 3,8 3,11 4,8 4,9 4,10; i 7 8,8 8,9 8,10 9,8 9,11 10,7 10,11 11,8 11,11 12,8 12,9 12,10; i 7 10,0 10,1 10,4 10,5 11,0 11,2 11,3 11,4 11,6 12,0 12,1 12,4 12,5; i 9 2,2 2,3 3,2 3,4 4,2 4,3; i 9 2,9; i 9 6,6 6,7 6,8 6,9 6,10 6,11 7,6 8,0 8,1 8,2 8,3 8,4 8,5; i 9 10,9; s 0,1 0,4 0,10 2,2 2,3 2,9 2,11 3,0 3,2 3,4 3,6 4,2 4,3 4,10 6,1 6,4 6,6 6,11 8,0 8,5 8,8 8,10 10,0 10,5 10,7 10,9 10,11 11,3 12,1 12,4 12,8 12,10; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    public static final String FACE_GAME = "a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 0,4 0,5 0,6 0,7 0,8 0,9 0,10 0,11 1,0 1,12 2,0 2,11 3,0 3,12 4,0 4,11 5,0 5,12 6,0 6,11 7,0 7,12 8,0 8,11 9,0 9,12 10,0 10,11 11,0 11,12 12,0 12,1 12,2 12,3 12,4 12,5 12,6 12,7 12,8 12,9 12,10 12,11; i 6 2,4 2,5 2,6 2,7; i 9 4,4 4,5 4,6 4,7; i 9 6,5 6,6 7,5 7,7 8,5 8,6; i 12 2,2 3,2 3,3 4,2 5,2 5,3 6,2 7,2 7,3; i 12 2,9 3,9 3,10 4,9 5,9 5,10 6,9 7,9 7,10; i 12 9,2 9,10 10,2 10,3 10,4 10,5 10,6 10,7 10,8 10,9; s 0,3 0,8 1,0 1,12 2,2 2,4 2,7 2,9 4,2 4,5 4,6 4,9 5,0 5,12 6,2 6,5 6,6 6,9 8,0 8,5 8,6 8,11 9,2 9,10 10,3 10,5 10,6 10,8 11,0 11,12 12,4 12,7; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    public static final String SIDES_GAME =  "a 7 2; c 0 E; i 4 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 2,0 2,1 2,2 2,3 3,0 3,1 3,2 3,3 4,0 4,1 4,2 4,3 5,0 5,1 5,2 5,3 6,0 6,1 6,2 6,3; i 20 0,5 1,5 1,6 2,5 3,5 3,6 4,5 5,5 5,6 6,5; s 0,0 0,1 0,2 0,3 1,1 1,2 1,3 1,5 1,6 2,0 2,1 2,2 2,3 3,0 3,1 3,2 3,3 3,5 3,6 4,0 4,1 4,2 4,3 5,1 5,2 5,3 5,5 5,6 6,0 6,1 6,2 6,3; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";
    public static final String SPACE_INVADERS_GAME = "a 23 2; c 0 E; i 6 0,2 0,7 1,3 1,7 2,2 2,3 2,4 2,5 2,6 2,7 3,2 3,4 3,5 3,6 3,8 4,0 4,1 4,2 4,3 4,4 4,5 4,6 4,7 4,8 4,9 5,0 5,1 5,3 5,4 5,5 5,6 5,7 5,9 5,10 6,0 6,2 6,7 6,9 7,3 7,4 7,6 7,7; i 6 0,14 0,19 1,15 1,19 2,14 2,15 2,16 2,17 2,18 2,19 3,14 3,16 3,17 3,18 3,20 4,12 4,13 4,14 4,15 4,16 4,17 4,18 4,19 4,20 4,21 5,12 5,13 5,15 5,16 5,17 5,18 5,19 5,21 5,22 6,12 6,14 6,19 6,21 7,15 7,16 7,18 7,19; i 6 17,9 18,8 18,9 19,6 19,7 19,8 19,9 19,10 19,11 19,12 20,5 20,6 20,7 20,8 20,9 20,10 20,11 20,12 21,5 21,6 21,7 21,8 21,9 21,10 21,11 21,12 21,13 22,5 22,6 22,7 22,8 22,9 22,10 22,11 22,12; i 8 12,3 12,5 13,3 13,4 13,5 13,6 14,1 14,2 14,3 14,4 14,5 15,1 15,2 15,3 16,1 16,2; i 8 12,17 12,18 12,19 13,17 13,18 13,19 13,20 14,17 14,18 14,19 14,20 15,19 15,20 15,21 16,19 16,20; i 8 13,14 14,13 14,14 15,13 15,14 15,15 16,13 16,14; i 8 14,7 15,7 15,8 16,7; i 10 8,9 9,9 10,9 11,9; i 10 8,12 9,13 10,12 11,13; i 10 9,1 10,1 11,1 12,1; i 10 9,22 10,21 11,22 12,21; i 10 13,10 14,10 15,10; i 10 17,0 18,0 19,0 20,0; i 10 17,16 18,16 19,16 20,16; s 0,2 0,7 0,14 0,19 3,5 3,17 6,0 6,9 6,12 6,21 7,4 7,6 7,16 7,18 11,9 11,13 12,1 12,19 12,21 13,10 15,2 15,8 15,14 15,20 17,9 18,8 18,9 20,0 20,16 21,6 21,9 21,12; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;";

    /**
     * Check if the string encoding of the game state is well-formed.
     * Note that this does not mean checking that the state is valid
     * (represents a state that players could reach in game play),
     * only that the string representation is syntactically well-formed.
     * <p>
     * A description of the state string will be included in README.md
     * in an update of the project after D2B is complete.
     *
     * @param stateString a string representing a game state
     * @return true if stateString is well-formed and false otherwise
     */
    public static boolean isStateStringWellFormed(String stateString){

        // Create an array of regex strings to match the state string
        // The state string contains 5 parts, each of which is matched by a regex string
        String[] matchArray = new String[6];

        // For the gameArrangementStatement use the following regex string
        matchArray[0] = "a \\d{1,2} \\d{1,2}; ";
        // For the currentStateStatement use the following regex string
        matchArray[1] = "c \\d{1,2} [E|S]; ";
        // For the islandStatement use the following regex string
        matchArray[2] = "(i \\d{1,2} (\\d{1,2},\\d{1,2} )*\\d{1,2},\\d{1,2}; )*";
        // For the stonesStatement use the following regex string
        matchArray[3] = "(s (\\d{1,2},\\d{1,2} )+\\d{1,2},\\d{1,2}; )";
        // For the resources and statuettes use the following regex string
        matchArray[4] = "r C (\\d{1,2},\\d{1,2} )*B (\\d{1,2},\\d{1,2} )*W (\\d{1,2},\\d{1,2} )*P (\\d{1,2},\\d{1,2} )*S( \\d{1,2},\\d{1,2})*;";
        // For the playersStatement use the following regex string
        matchArray[5] = "( p \\d \\d{1,3} \\d{1,2} \\d{1,2} \\d{1,2} \\d{1,2} \\d{1,2} S (\\d{1,2},\\d{1,2} )*T( (\\d{1,2},\\d{1,2} ?)*)?;)*";

        // Combine the regex strings into one string to match the state string
        String matchString = "";
        for (String match:matchArray) {
            matchString += match;
        }

        // Check if the state string matches the regex string
        return stateString.matches(matchString);
    }

    /**
     * Check if the string encoding of the move is syntactically well-formed.
     * <p>
     * A description of the move string will be included in README.md
     * in an update of the project after D2B is complete.
     *
     * @param moveString a string representing a player's move
     * @return true if moveString is well-formed and false otherwise
     *
     * coordinate = row , col (i.e. "0,1" means row 0 col 1)
     */
    public static boolean isMoveStringWellFormed(String moveString){
        return moveString.matches("[ST] \\d{1,2},\\d{1,2}");
        // If the 1st element of moveString is neither a "S" nor a "T" return false
        // if the 2nd element is not a whitespace return false
        // if the 3rd and/or 4th element (as long as it is before ",") are not
        // digits, return false
        // if the 6th and/or 7th element (as long as it is after ",") are not digits,
        // return false
    }

    /**
     * Given a state string which is yet to have resources distributed amongst the stone circles,
     * randomly distribute the resources and statuettes between all the stone circles.
     * <p>
     * There will always be exactly 32 stone circles.
     * <p>
     * The resources and statuettes to be distributed are:
     * - 6 coconuts
     * - 6 bamboo
     * - 6 water
     * - 6 precious stones
     * - 8 statuettes
     * <p>
     * The distribution must be random.
     *
     * @param stateString a string representing a game state without resources distributed
     * @return a string of the game state with resources randomly distributed
     */
    public static String distributeResources(String stateString){
        // Check if the stateString is well-formed
        if (!isStateStringWellFormed(stateString)) return stateString;

        // Grab the stone circles from the stateString
        String stoneCircles = stateString.substring(stateString.indexOf("s") + 2, stateString.indexOf("r") - 2);

        // Split the stone circles into an array of cords
        String[] stoneCircleCords = stoneCircles.split(" ");

        // Check if there are 32 stone circles
        if (stoneCircleCords.length != 32) return stateString;

        // Create a random object and an arrays and list to shuffle the stone circles
        Random rand = new Random();

        // Number of times to shuffle the stone circles (can be changed)
        int shuffle_number = 3;

        // Create a copy of the stone circle array to shuffle
        String[] stoneCircleRandom = stoneCircleCords;

        // Shuffle the stone circles the specified number of times
        for (int i = 0; i < shuffle_number; i++) {
            // Create a temporary array to store the shuffled stone circles
            String[] tempStoneCircleRandom = new String[32];
            // Create a list to store the used cords (to avoid duplicates)
            List<String> usedCords = new ArrayList<String>();

            // Shuffle the array
            for (int j = 0; j < 32; j++) {
                // For 0-31 generate a random cord from the stone circle array and check if it has been used
                int randomIndex = rand.nextInt(31);
                while (usedCords.contains(stoneCircleCords[randomIndex])) {
                    // If it has been used, try the next in line
                    if (randomIndex == 31) {
                        randomIndex = 0;
                    }
                    else randomIndex++;
                }
                // If it hasn't been used, add it to the new array
                tempStoneCircleRandom[j] = stoneCircleRandom[randomIndex];
                usedCords.add(stoneCircleCords[randomIndex]);
            }
            // Replace the old array with the new one
            stoneCircleRandom = tempStoneCircleRandom;
        }


        // Create a string to store the new resources state
        String newResourcesState = "r";

        // Create an array for each resource type
        char[] resources = {'C', 'B', 'W', 'P'};

        // Create a variable to keep track of how many resources have been sorted
        int numSorted = 0;

        // For each resource type
        for (char r:resources){
            newResourcesState += " " + r;
            // Assign 6 to a stone circle
            for (int i = 0; i < 6; i++){
                newResourcesState += " " + stoneCircleRandom[numSorted];
                numSorted++;
            }
        }

        // Assign 8 statuettes to a stone circle
        newResourcesState += " S";
        for (int i = 0; i < 8; i++){
            newResourcesState += " " + stoneCircleRandom[numSorted];
            numSorted++;
        }

        // Replace the old resources state with the new one
        stateString = stateString.replace("r C B W P S", newResourcesState);

        return stateString;
    }

    /**
     * Given a state string and a move string, determine if the move is
     * valid for the current player.
     * <p>
     * For a move to be valid, the player must have enough pieces left to
     * play the move. The following conditions for each phase must also
     * be held.
     * <p>
     * In the Exploration Phase, the move must either be:
     * - A settler placed on any unoccupied sea space
     * - A settler or a village placed on any unoccupied land space
     *   adjacent to one of the player's pieces.
     * <p>
     * In the Settlement Phase, the move must be:
     * - Only a settler placed on an unoccupied space adjacent to
     *   one of the player's pieces.
     * Importantly, players can now only play on the sea if it is
     * adjacent to a piece they already own.
     *
     * @param stateString a string representing a game state
     * @param moveString a string representing the current player's move
     * @return true if the current player can make the move and false otherwise
     */
    public static boolean isMoveValid(String stateString, String moveString) {
        // Check if the inputs are wellFormed or not
        if (!isStateStringWellFormed(stateString)) return false;
        if (!isMoveStringWellFormed(moveString)) return false;

        String[] parts = stateString.split("; ?");

        // List of initializations used
        String currentPhase = "";

        // Coords of the island tiles
        ArrayList<String> coordsContainer = new ArrayList<>();

        int numberOfPlayer = 0; // Number of player
        String playerId = ""; // Player ID
        String pStatePlayerId = ""; // the current Player's move ID
        ArrayList<String> settlerCoords = new ArrayList<>(); // Placed Settler Coordinates
        ArrayList<String> villageCoords = new ArrayList<>(); // Placed villages coordinates
        ArrayList<String> playerSettlerCoords = new ArrayList<>(); // The current Player's settler coords
        ArrayList<String> playerVillageCoords = new ArrayList<>(); // The current Player's Village coords

        String[] split = moveString.split(" ");
        String pieceType = split[0]; // Move coord piece type S or T
        String moveCoords = split[1]; // The actual coords from the move String
        String[] splitCoords = moveCoords.split(",");
        int xMoveCoords = Integer.parseInt(splitCoords[1]);
        int yMoveCoords = Integer.parseInt(splitCoords[0]);
        int boardHeight = 0;
        int numberOfSettlersPerPlayer = 30;
        int numberOfVillagesPerPlayer = 5;
        int settlerCounter = 0;
        int villageCounter = 0;

        for (String part : parts) {
            String[] parseSplit = part.split(" ");
            String stateCases = parseSplit[0];

            switch (stateCases) {

                // Get the number of player from here
                case "a":
                    boardHeight = Integer.parseInt(parseSplit[1]);
                    String playerAmount = parseSplit[2];
                    numberOfPlayer = Integer.parseInt(playerAmount);
                    break;

                    // Get the player ID and Current Phase from here
                // Phase Exploration or Settlement
                case "c":
                    playerId = parseSplit[1];
                    currentPhase = parseSplit[2];
                    break;

                    // Get the Land coords (Island Coords)
                case "i":
                    coordsContainer.addAll(Arrays.asList(parseSplit).subList(2, parseSplit.length));
                    break;

                case "p":
                    // Check if there's enough pieces left for that player that is moving
                    pStatePlayerId = parseSplit[1];

                            // Collecting the settler Coords that has been placed
                            for (int i = 9; i < parseSplit.length; i++) {
                                while (!parseSplit[i].equals("T")) {
                                    settlerCoords.add(parseSplit[i]); // Store all the settler coords

                                    // If the current player ID is the same as the placed settler's player ID
                                    // Store it into array
                                    if(pStatePlayerId.equals(playerId)) playerSettlerCoords.add(parseSplit[i]);
                                    i++;
                                }

                                // If the current player ID is the same as the placed settler's player ID
                                // iterate the settlerCounter
                                if(pStatePlayerId.equals(playerId)) settlerCounter = playerSettlerCoords.size();
                                i++;

                                // Collecting the village coords that has been placed
                                while (i < parseSplit.length) {
                                    if(pStatePlayerId.equals(playerId)) villageCounter = i - 9 - settlerCounter;
                                    villageCoords.add(parseSplit[i]); // Store all the village Coords

                                    // If the current player ID is the same as the placed Village's player ID
                                    // Store it into array
                                    if(pStatePlayerId.equals(playerId)) playerVillageCoords.add(parseSplit[i]);
                                    i++;
                                }

                                // Checking the requirement of how many pieces are left //
                                switch (numberOfPlayer) {
                                    case 4:
                                        numberOfSettlersPerPlayer -= 10;
                                        if (pieceType.equals("S")) {
                                            if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                        } else if (pieceType.equals("T")) {
                                            if (villageCounter + 1 > numberOfVillagesPerPlayer) return false;
                                        }
                                        break;
                                    case 3:
                                        numberOfSettlersPerPlayer -= 5;
                                        if (pieceType.equals("S")) {
                                            if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                        } else if (pieceType.equals("T")) {
                                            if (villageCounter + 1 > numberOfVillagesPerPlayer) return false;
                                        }
                                        break;
                                    case 2:
                                        if (pieceType.equals("S")) {
                                            if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                        } else if (pieceType.equals("T")) {
                                            if (villageCounter + 1 > numberOfVillagesPerPlayer) return false;
                                        }
                                }
                            }
                    break;
            }
        }

        // out of bound for height
        if(yMoveCoords > boardHeight - 1) return false;

        // if it's even rows, check the number of cols for out of bound (i.e. the width)
        if(yMoveCoords % 2 == 0) {
            if(xMoveCoords > boardHeight - 2) return false;
        }
        else if(xMoveCoords > boardHeight - 1) return false;

                            // For Exploration Phase and or Settlement Phase
                            switch(currentPhase){
                                // Exploration Phase
                                case "E":
                                    // If the move Coords is an occupied space, return false;
                                    if(settlerCoords.contains(moveCoords) || villageCoords.contains(moveCoords)) return false;

                                    // If the Village is being placed on the sea return false
                                    if(pieceType.equals("T") && !coordsContainer.contains(moveCoords)) return false;


                                    // if the village is placed on Land and it's not adjacent to any
                                    // of the pieces return false
                                    if(pieceType.equals("T") && (!isAdjacent(moveCoords, playerVillageCoords) &&
                                            !isAdjacent(moveCoords, playerSettlerCoords))) return false;


                                    // If settler is on land and it's not adjacent to any of the pieces
                                    // return false
                                    if(pieceType.equals("S") && coordsContainer.contains(moveCoords)){
                                        if(!isAdjacent(moveCoords, playerSettlerCoords) &&
                                                !isAdjacent(moveCoords, playerVillageCoords)) return false;
                                    }
                                    break;

                                    // Settlement Phase
                                case "S":
                                    // If the move coord is an occupied space, return false;
                                    if(settlerCoords.contains(moveCoords)) return false;
                                    if(villageCoords.contains(moveCoords)) return false;

                                    // As the only move is for the settler, the village is false
                                    if(pieceType.equals("T")) return false;

                                    // if the settler is not adjacent with any of the pieces return false
                                    if(!isAdjacent(moveCoords, playerSettlerCoords) &&
                                    !isAdjacent(moveCoords, playerVillageCoords)) return false;
                            }
                            return true;
    }

    private static boolean isAdjacent(String centerCoords, ArrayList<String> coordsContainer) {
        String[] coordsSplit = centerCoords.split(",");
        int mainX = Integer.parseInt(coordsSplit[1]); // xCoord for center Coords
        int mainY = Integer.parseInt(coordsSplit[0]); // yCoord for center Coords

        // To check for the 6 adjacencies surrounding the center coords
        int[][] adjacentModifiers = {
                {-mainY % 2, -1},
                {1 - mainY % 2, -1},
                {-1, 0}, {1, 0},
                {-mainY % 2, 1},
                {1 - mainY % 2, 1},
        };

        for (int[] mod : adjacentModifiers) {
            if (coordsContainer.contains(String.format("%s,%s", mainY + mod[1], mainX + mod[0])))
                return true;
        }
        return false;
    }

    /**
     * This method is to check if the move is valid for the current player
     * This is a very trimmed down version of the isMoveValid method
     *
     * @param pieceType Type of piece being placed (S = Settler, T = Village)
     * @param moveCoords The coords of the piece being placed (i.e. 1,2)
     * @param currentPhase The current phase of the game (E = Exploration, S = Settlement)
     * @param coordsContainer The coords of the land in a ArrayList of Strings
     * @param settlerCoords The coords of the Settlers in a ArrayList of Strings
     * @param villageCoords The coords of the Villages in a ArrayList of Strings
     * @param playerSettlerCoords The coords of the Settlers of the current player in a ArrayList of Strings
     * @param playerVillageCoords The coords of the Villages of the current player in a ArrayList of Strings
     * @return boolean True if the move is valid, false if the move is invalid
     */

    public static boolean isMoveValidTrim(String pieceType, String moveCoords,
                                          String currentPhase, ArrayList<String> coordsContainer,
                                          ArrayList<String> settlerCoords,
                                          ArrayList<String> villageCoords, ArrayList<String> playerSettlerCoords,
                                          ArrayList<String> playerVillageCoords) {
        // For Exploration Phase and or Settlement Phase
        switch(currentPhase){
            // Exploration Phase
            case "E":
                // If the move Coords is an occupied space, return false;
                if(settlerCoords.contains(moveCoords) || villageCoords.contains(moveCoords)) return false;
                // If the Village is being placed on the sea return false
                if(pieceType.equals("T") && !coordsContainer.contains(moveCoords)) return false;
                // if the village is placed on Land and it's not adjacent to any
                // of the pieces return false
                if(pieceType.equals("T") && (!isAdjacent(moveCoords, playerVillageCoords) &&
                        !isAdjacent(moveCoords, playerSettlerCoords))) return false;
                // If settler is on land and it's not adjacent to any of the pieces
                // return false
                if(pieceType.equals("S") && coordsContainer.contains(moveCoords)){
                    if(!isAdjacent(moveCoords, playerSettlerCoords) &&
                            !isAdjacent(moveCoords, playerVillageCoords)) return false;
                }
                break;
            // Settlement Phase
            case "S":
                // If the move coord is an occupied space, return false;
                if(settlerCoords.contains(moveCoords)) return false;
                if(villageCoords.contains(moveCoords)) return false;

                // if the settler is not adjacent with any of the pieces return false
                if(!isAdjacent(moveCoords, playerSettlerCoords) &&
                        !isAdjacent(moveCoords, playerVillageCoords)) return false;
        }
        return true;
    }

    /**
     * Given a state string, generate a set containing all move strings playable
     * by the current player.
     * <p>
     * A move is playable if it is valid.
     *
     * @param stateString a string representing a game state
     * @return a set of strings representing all moves the current player can play
     */
    public static Set<String> generateAllValidMoves(String stateString) {

        // Get number of players
        int numPlayers = Character.getNumericValue(stateString.charAt(stateString.indexOf(";") - 1));
        // Store the current game phase
        String gamePhase = "E";
        // If the game is not in the exploration phase use state 1
        if (!stateString.contains("E")) gamePhase = "S";
        // Get the current player
        String currentPlayer = stateString.substring(stateString.indexOf("c ") + 2,stateString.indexOf("c ") + 3);
        // Get the board size
        int boardHeight = Integer.parseInt(stateString.substring(stateString.indexOf("a ") + 2, stateString.indexOf(";") - 2));
        // Get player data
        String allPlayerData = stateString.substring(stateString.indexOf("p " + currentPlayer));
        String playerData = allPlayerData.substring(0, allPlayerData.indexOf(";"));
        String[] pStates = stateString.substring(stateString.indexOf("p ")).split("; ?");

        ArrayList<String> settlerCoords = new ArrayList<>(); // Placed Settler Coordinates
        ArrayList<String> villageCoords = new ArrayList<>(); // Placed villages coordinates
        ArrayList<String> playerSettlerCoords = new ArrayList<>(); // The current Player's settler coords
        ArrayList<String> playerVillageCoords = new ArrayList<>(); // The current Player's Village coords

        for (String pState:pStates) {

            String[] parseSplit = pState.split(" ");
            // Check if there's enough pieces left for that player that is moving
            String pStatePlayerId = parseSplit[1];

            // Collecting the settler Coords that has been placed
            for (int i = 9; i < parseSplit.length; i++) {
                while (!parseSplit[i].equals("T")) {
                    settlerCoords.add(parseSplit[i]); // Store all the settler coords

                    // If the current player ID is the same as the placed settler's player ID
                    // Store it into array
                    if (pStatePlayerId.equals(currentPlayer)) playerSettlerCoords.add(parseSplit[i]);
                    i++;
                }
                i++;

                // Collecting the village coords that has been placed
                while (i < parseSplit.length) {
                    villageCoords.add(parseSplit[i]); // Store all the village Coords

                    // If the current player ID is the same as the placed Village's player ID
                    // Store it into array
                    if (pStatePlayerId.equals(currentPlayer)) playerVillageCoords.add(parseSplit[i]);
                    i++;
                }
            }
        }

        // Get placed pieces
        String settlersPlaced = playerData.substring(playerData.indexOf("S") + 2, playerData.indexOf("T"));
        int numSettlersPlaced = settlersPlaced.split(" ").length;
        if (!settlersPlaced.contains(" ")){
            numSettlersPlaced = 0;
        }
        String villagesPlaced = playerData.substring(playerData.indexOf("T")+1);
        int numVillagesPlaced = villagesPlaced.split(" ").length;
        if (!villagesPlaced.contains(" ")){
            numVillagesPlaced = 0;
        }

        // Get island data
        String[] islands = stateString.substring(stateString.indexOf("i ")).split("; ");
        // Get the coordinates of the islands
        ArrayList<String> coordsContainer = new ArrayList<>();

        for (String island : islands) {
            if (!island.startsWith("i ")) continue;
            coordsContainer.addAll(Arrays.asList(island.substring(4).split(" ")));
        }

        // Calculate number of pieces each player starts with
        int startNumSettlers = 0;
        switch (numPlayers) {
            case 2:
                startNumSettlers = 30;
                break;
            case 3:
                startNumSettlers = 25;
                break;
            case 4:
                startNumSettlers = 20;
                break;
        }

        // Check if the player has placed all their settlers or villages
        boolean hasSettler = (numSettlersPlaced < startNumSettlers);
        boolean hasVillage = (numVillagesPlaced <= 5);

        // Create a set to store all possible moves
        Set<String> allMoves = new HashSet<>();

        // Generate all possible coordinates in an array
        String[] coordinates = new String[boardHeight * boardHeight];
        int index = 0;
        for (int i = 0; i < boardHeight; i++){
            for (int j = 0; j < boardHeight; j++){
                coordinates[index] = j + "," + i;
                index++;
            }
        }
        // For each coordinate
        for (String cord:coordinates) {

            int y = Integer.parseInt(cord.split(",")[1]);
            if(Integer.parseInt(cord.substring(0,cord.indexOf(','))) % 2 == 0) {
                if(y > boardHeight - 2) continue;
            }
            else if(y > boardHeight - 1) continue;

            // If the player has not placed all their settlers
            if (hasSettler){
                if (isMoveValidTrim("S", cord,gamePhase,
                        coordsContainer,settlerCoords,villageCoords,playerSettlerCoords,playerVillageCoords)) {
                    allMoves.add("S " + cord);
                }
            }
            if (hasVillage && gamePhase == "E") {
                if (isMoveValidTrim("T", cord,gamePhase,
                        coordsContainer,settlerCoords,villageCoords,playerSettlerCoords,playerVillageCoords)) {
                    allMoves.add("T " + cord);
                }
            }

        }
        return allMoves;
    }

    /**
     * Given a state string, determine whether it represents an end of phase state.
     * <p>
     * A phase is over when either of the following conditions hold:
     * - All resources (not including statuettes) have been collected.
     * - No player has any remaining valid moves.
     *
     * @param stateString a string representing a game state
     * @return true if the state is at the end of either phase and false otherwise
     */
    public static boolean isPhaseOver(String stateString){
         return false; // FIXME Task 9
    }

    /**
     * Given a state string and a move string, place the piece associated with the
     * move on the board. Ensure the player collects any corresponding resource or
     * statuettes.
     * <p>
     * Do not handle switching to the next player here.
     *
     * @param stateString a string representing a game state
     * @param moveString a string representing the current player's move
     * @return a new state string achieved by placing the move on the board
     */
    public static String placePiece(String stateString, String moveString){
         return ""; // FIXME Task 10
    }

    /**
     * Given a state string, calculate the "Islands" portion of the score for
     * each player as if it were the end of a phase. The return value is an
     * integer array sorted by player number containing the calculated score
     * for the respective player.
     * <p>
     * The "Islands" portion is calculated for each player as follows:
     * - If the player has pieces on 8 or more islands, they score 20 points.
     * - If the player has pieces on 7 islands, they score 10 points.
     * - No points are scored otherwise.
     *
     * @param stateString a string representing a game state
     * @return an integer array containing the calculated "Islands" portion of
     * the score for each player
     */
    public static int[] calculateTotalIslandsScore(String stateString){
         return new int[]{0, 0}; // FIXME Task 11
    }

    /**
     * Given a state string, calculate the "Links" portion of the score for
     * each player as if it were the end of a phase. The return value is an
     * integer array sorted by player number containing the calculated score
     * for the respective player.
     * <p>
     * Players earn points for their chain of pieces that links the most
     * islands. For each island linked by this chain, they score 5 points.
     * <p>
     * Note the chain needn't be a single path. For instance, if the chain
     * splits into three or more sections, all of those sections are counted
     * towards the total.
     *
     * @param stateString a string representing a game state
     * @return an integer array containing the calculated "Links" portion of
     * the score for each player
     */
    public static int[] calculateIslandLinksScore(String stateString){
         return new int[]{0, 0}; // FIXME Task 11
    }

    /**
     * Given a state string, calculate the "Majorities" portion of the score for
     * each player as if it were the end of a phase. The return value is an
     * integer array sorted by player number containing the calculated score
     * for the respective player.
     * <p>
     * The "Majorities" portion is calculated for each island as follows:
     * - The player with the most pieces on the island scores the number
     *   of points that island is worth.
     * - In the event of a tie for pieces on an island, those points are
     *   divided evenly between those players rounding down. For example,
     *   if two players tied for an island worth 7 points, they would
     *   receive 3 points each.
     * - No points are awarded for islands without any pieces.
     *
     * @param stateString a string representing a game state
     * @return an integer array containing the calculated "Majorities" portion
     * of the score for each player
     */
    public static int[] calculateIslandMajoritiesScore(String stateString){
         return new int[]{0, 0}; // FIXME Task 11
    }

    /**
     * Given a state string, calculate the "Resources" and "Statuettes" portions
     * of the score for each player as if it were the end of a phase. The return
     * value is an integer array sorted by player number containing the calculated
     * score for the respective player.
     * <p>
     * Note that statuettes are not resources.
     * <p>
     * In the below "matching" means a set of the same resources.
     * <p>
     * The "Resources" portion is calculated for each player as follows:
     * - For each set of 4+ matching resources, 20 points are scored.
     * - For each set of exactly 3 matching resources, 10 points are scored.
     * - For each set of exactly 2 matching resources, 5 points are scored.
     * - If they have all four resource types, 10 points are scored.
     * <p>
     * The "Statuettes" portion is calculated for each player as follows:
     * - A player is awarded 4 points per statuette in their possession.
     *
     * @param stateString a string representing a game state
     * @return an integer array containing the calculated "Resources" and "Statuettes"
     * portions of the score for each player
     */
    public static int[] calculateResourcesAndStatuettesScore(String stateString){
         return new int[]{0, 0}; // FIXME Task 11
    }

    /**
     * Given a state string, calculate the scores for each player as if it were
     * the end of a phase. The return value is an integer array sorted by player
     * number containing the calculated score for the respective player.
     * <p>
     * It is recommended to use the other scoring functions to assist with this
     * task.
     *
     * @param stateString a string representing a game state
     * @return an integer array containing the calculated scores for each player
     */
    public static int[] calculateScores(String stateString){
         return new int[]{0, 0}; // FIXME Task 11
    }

    /**
     * Given a state string representing an end of phase state, return a new state
     * achieved by following the end of phase rules. Do not move to the next player
     * here.
     * <p>
     * In the Exploration Phase, this means:
     * - The score is tallied for each player.
     * - All pieces are removed from the board excluding villages not on stone circles.
     * - All resources and statuettes remaining on the board are removed. All resources are then
     *   randomly redistributed between the stone circles.
     * <p>
     * In the Settlement Phase, this means:
     * - Only the score is tallied and added on for each player.
     *
     * @param stateString a string representing a game state at the end of a phase
     * @return a string representing the new state achieved by following the end of phase rules
     */
    public static String endPhase(String stateString){
         return ""; // FIXME Task 12
    }

    /**
     * Given a state string and a move string, apply the move to the board.
     * <p>
     * If the move ends the phase, apply the end of phase rules.
     * <p>
     * Advance current player to the next player in turn order that has a valid
     * move they can make.
     *
     * @param stateString a string representing a game state
     * @param moveString a string representing the current player's move
     * @return a string representing the new state after the move is applied to the board
     */
    public static String applyMove(String stateString, String moveString){
         return ""; // FIXME Task 13
    }

    /**
     * Given a state string, returns a valid move generated by your AI.
     * <p>
     * As a hint, generateAllValidMoves() may prove a useful starting point,
     * maybe if you could use some form of heuristic to see which of these
     * moves is best?
     * <p>
     * Your AI should perform better than randomly generating moves,
     * see how good you can make it!
     *
     * @param stateString a string representing a game state
     * @return a move string generated by an AI
     */
    public static String generateAIMove(String stateString){
         return ""; // FIXME Task 16
    }
}
