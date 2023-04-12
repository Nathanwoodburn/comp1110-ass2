package comp1110.ass2;

import gittest.A;
import javafx.scene.paint.Color;

import java.sql.Time;
import java.text.NumberFormat;
import java.util.*;
import java.lang.*;
import java.util.stream.Stream;

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
        if (!stateString.matches(matchString)) return false;

        return true;
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
        if(!moveString.matches("[ST] \\d{1,2},\\d{1,2}"))return false;
        return true;
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
//     * @param stateString a string representing a game state
//     * @param moveString a string representing the current player's move
     * @return true if the current player can make the move and false otherwise
     *
     *  * move = pieceType, " ", coordinate
     *      *
     *      * pieceType = "S" | "T"
     *      *
     *      * currentStateStatement = "c ", playerId, " ", phase, ";"
     *      *
     *      * phase = "E" | "S"
     *      *
     *      *
     *      * c 0 E; S 2,3
     *
     *      p 1 42 1 2 3 4 5 S 5,6 8,7 T 1,2
     */
//    public static void main(String[] args) {
//        isMoveValid("a 13 2; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T;", "S 2,3" );
//    }
    public static boolean isMoveValid(String stateString, String moveString) {
        if (!isStateStringWellFormed(stateString)) return false;
        if (!isMoveStringWellFormed(moveString)) return false;
        if (moveString == "") return false; // checking if the players have any pieces left to move

        String[] parts = stateString.split("; ?");
        String currentPhase = "";
        ArrayList<String> coordsContainer = new ArrayList<>();
        int numberOfPlayer = 0;
        String playerId = "";
        ArrayList<String> settlerCoords = new ArrayList<>();
        ArrayList<String> villageCoords = new ArrayList<>();

        for (String part : parts) {
            String[] parseSplit = part.split(" ");
            String stateCases = parseSplit[0];

//            int numberOfPlayer = 0;
            int numberOfSettlersPerPlayer = 30;
            int numberOfVillagesPerPlayer = 5;
            int settlerCounter;
            int villageCounter = 0;
//            String currentPhase = "";

            // Collect the island coordinates into 1 single array first
            // Then use .contains for each resource pos and island pos
            // to check whether that pos is unoccupied or not

            // ArrayList<String> coordsContainer = new ArrayList<>();
            switch (stateCases) {
                case "a":
                    String playerAmount = parseSplit[2];
                    numberOfPlayer = Integer.parseInt(playerAmount);
                    break;
                case "c":
                    playerId = parseSplit[1];
                    // * c 0 E; S 2,3
                    currentPhase = parseSplit[2];
                    break;
                case "i":
                    for (int i = 2; i < parseSplit.length; i++) {
                        String coords = parseSplit[i];
                        coordsContainer.add(coords);
                    }
                    break;
                case "p":
                    // Check if there's enough pieces left
                    if(playerId.equals(parseSplit[1])) {
                        for (int i = 9; i < parseSplit.length; i++) {
                            while (!parseSplit[i].equals("T")) {
                                // settlerCounter = i - 8;
                                settlerCoords.add(parseSplit[i]);
                                i++;
                            }
                            settlerCounter = settlerCoords.size();

                            i++;
                            while(i < parseSplit.length) {
                                villageCounter = i - 9 - settlerCounter;
                                villageCoords.add(parseSplit[i]);
                                i++;
                            }

                            switch (numberOfPlayer) {
                                case 4:
                                    numberOfSettlersPerPlayer -= 10;
                                    if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                    if (villageCounter > numberOfVillagesPerPlayer) return false;
                                    break;
                                case 3:
                                    numberOfSettlersPerPlayer -= 5;
                                    if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                    if(villageCounter > numberOfVillagesPerPlayer) return false;
                                    break;
                                case 2:
                                    if (settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                                    if (villageCounter > numberOfVillagesPerPlayer) return false;
                            }
                        }
                    }
                    break;
            }
        }
//        * In the Exploration Phase, the move must either be:
//           * - A settler placed on any unoccupied sea space
//        * - A settler or a village placed on any unoccupied land space
//     *   adjacent to one of the player's pieces.
                            switch(currentPhase){
                                case "E":
                                    String[] split = moveString.split(" ");
                                    String moveCoords = split[1];
//                                    System.out.println(moveCoords);

                                    // Water Pos ( if there's a settler on move Coords, return false )
                                    if(settlerCoords.contains(moveCoords)) return false;
                                    if(villageCoords.contains(moveCoords)) return false;

                                    // If the moveCoords is on island pos, return false
                                    // if(coordsContainer.contains(moveCoords)) return false;
//                                    if(coordsContainer.contains(moveCoords)) {
//
//                                    }
                                    if(!isAdjacent(moveCoords, settlerCoords) ||
                                    !isAdjacent(moveCoords, villageCoords)) return false;
                                    break;
                                case "S":

                            }
                            return true;
    }

    private static boolean isAdjacent(String centerCoords, ArrayList<String> coordsContainer) {
        String[] coordsSplit = centerCoords.split(",");
        int mainX = Integer.parseInt(coordsSplit[0]);
        int mainY = Integer.parseInt(coordsSplit[1]);

        int[][] adjacentModifiers = {
                {0 + mainX % 2 * -1, -1},
                {1 + mainX % 2 * -1, -1},
                {-1, 0}, {1, 0},
                {0 + mainX % 2 * -1, 1},
                {1 + mainX % 2 * -1, 1},
        };

        for (int[] mod : adjacentModifiers)
            if (coordsContainer.contains(String.format("%s,%s", mainX + mod[0], mainY + mod[1])))
                return true;

        return false;
    }
//                        if(parseSplit[i].equals("T")){
//                            villageCounter++;
//                            if(villageCounter > 5) return false;
//                        }
//
//                        if (numberOfPlayer == 4) {
//                            numberOfSettlersPerPlayer -= 10;
//                            if(parseSplit[i].equals("S")) {
//                                settlerCounter++;
//                                if(settlerCounter > 30) return false;
//                            }
//                        } else if (numberOfPlayer == 3) {
//                            numberOfSettlersPerPlayer -= 5;
//                            if(parseSplit[i].equals("S")) {
//                                settlerCounter++;
//                                if(settlerCounter > 35) return false;
//                            }
//                        } else if (numberOfPlayer == 2) {
//                            if(parseSplit[i].equals("S")){
//                                settlerCounter++;
//                                if(settlerCounter > 40) return false;
//                            }
//                        }
                    /*
                case "r":
                    for (int i = 1; i < parseSplit.length; i++) {
                        switch (parseSplit[i]) {
                            case "C":
                                i++; // To Skip the "C" itself and go to the numbers in the string
                                while (!parseSplit[i].equals("B")) {
                                    String coords = parseSplit[i];
                                    coordsContainer.add(coords);
                                    System.out.println(coords);
                                    System.out.println(coordsContainer);
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "B"
                                break;

                            // Generating Resource: Bamboo Tiles
                            case "B":
                                i++; // To Skip the "B" itself and go to the numbers in the string
                                while (!parseSplit[i].equals("W")) {

                                    String coords = parseSplit[i];
                                    coordsContainer.add(coords);
                                    System.out.println(coords);
                                    System.out.println(coordsContainer);
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "W"
                                break;

                            // Generating Resource: Water tiles
                            case "W":
                                i++; // To Skip the "W" itself and go to the numbers in the string
                                while (!parseSplit[i].equals("P")) {
                                    String coords = parseSplit[i];
                                    coordsContainer.add(coords);
                                    System.out.println(coords);
                                    System.out.println(coordsContainer);
                                    i++; // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "P"
                                break;

                            // Generating Resource: Precious Stone tiles
                            case "P":
                                i++; // To Skip the "P" itself and go to the numbers in the string
                                while (!parseSplit[i].equals("S")) {

                                    String coords = parseSplit[i];
                                    coordsContainer.add(coords);
                                    System.out.println(coords);
                                    System.out.println(coordsContainer);
                                    i++;  // To continue the iteration for the while loops
                                }
                                i--; // So that i does not go straight to the coords after the letters instead
                                // i stop at the letter "S"
                                break;

                            // Generating Resource: Statuettes tiles
                            case "S":
                                i++; // To Skip the "P" itself and go to the numbers in the string
                                while (i < parseSplit.length) {

                                    String coords = parseSplit[i];
                                    coordsContainer.add(coords);
                                    System.out.println(coords);
                                    System.out.println(coordsContainer);
                                    i++; // To continue the iteration for the while loops
                                }
                                break;
                        }
                    }
                    break;

                     */

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
        int gamePhase = 0;

        // If the game is not in the exploration phase use state 1
        if (!stateString.contains("E")) gamePhase = 1;

        // Get the current player
        int currentPlayer = Character.getNumericValue(stateString.charAt(stateString.indexOf("c ") + 2));

        // Get the board size
        int boardHeight = Integer.parseInt(stateString.substring(stateString.indexOf("a ") + 2, stateString.indexOf(";") - 2));

        // Get player data
        String allPlayerData = stateString.substring(stateString.indexOf("p " + currentPlayer));
        String playerData = allPlayerData.substring(0, allPlayerData.indexOf(";"));

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
        boolean hasVillage = (numVillagesPlaced < 5);


        // Create a set to store all possible moves
        Set<String> allMoves = new HashSet<>();

        // If the player has placed all their settlers and villages
        if (!hasSettler && !hasVillage){
            return allMoves;
        }

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
            // If the player has not placed all their settlers
            if (hasSettler && gamePhase == 0) {
                // Generate all possible settler moves

                // If the coordinate is not occupied and is water
                if (!stateString.contains(" " + cord + " ")) {
                    if (cord.equals("0,12")){
                        System.out.println("1");
                    }
                    allMoves.add("S " + cord);
                }
            }

            // If the coordinate is not occupied
            if (!allPlayerData.contains(" " + cord + " ")) {

                // if the coordinate is adjacent to one of the player's pieces

                int firstCord = Integer.parseInt(cord.substring(0, cord.indexOf(",")));
                int secondCord = Integer.parseInt(cord.substring(cord.indexOf(",") + 1));

                if (playerData.contains(" " + (firstCord - 1) + "," + (secondCord + 1) + " ") ||
                        playerData.contains(" " + (firstCord + 1) + "," + (secondCord + 1) + " ") ||
                        playerData.contains(" " + (firstCord - 1) + "," + (secondCord - 1) + " ") ||
                        playerData.contains(" " + (firstCord + 1) + "," + (secondCord - 1) + " ")) {

                    if (cord.equals("0,12")){
                        System.out.println("2");
                    }
                    if (hasSettler){
                        allMoves.add("S " + cord);
                    }
                    if (hasVillage) {
                        allMoves.add("T " + cord);
                    }

                }
            }
        }



        return allMoves; //! Test when Task 7 is done
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
