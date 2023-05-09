package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BlueLagoon {
    // The Game Strings for five maps have been created for you.
    // They have only been encoded for two players. However, they are
    // easily extendable to more by adding additional player statements.

    // region Checks on strings
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
        matchArray[4] = "r C (\\d{1,2},\\d{1,2} )*B (\\d{1,2},\\d{1,2} )*W (\\d{1,2},\\d{1,2} )*P (\\d{1,2},\\d{1,2} " +
                ")*S( \\d{1,2},\\d{1,2})*;";
        // For the playersStatement use the following regex string
        matchArray[5] = "( p \\d \\d{1,3} \\d{1,2} \\d{1,2} \\d{1,2} \\d{1,2} \\d{1,2} S (\\d{1,2},\\d{1,2} )*T( " +
                "(\\d{1,2},\\d{1,2} ?)*)?;)*";

        // Combine the regex strings into one string to match the state string
        StringBuilder matchString = new StringBuilder();
        for (String match:matchArray) {
            matchString.append(match);
        }

        // Check if the state string matches the regex string
        if (!stateString.matches(matchString.toString())) return false;

        // Check that there is one and only one of each player id
        // This fixed test 2-3 of D2DTests.testIsStateStringWellFormed
        int numPlayers = Character.getNumericValue(stateString.charAt(stateString.indexOf(";")-1));
        for (int i = 0; i < numPlayers; i++) {
            if (stateString.length() - stateString.replaceAll("p "+i,"").length() != 3) return false;
        }
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
        return moveString.matches("[ST] \\d{1,2},\\d{1,2}");
        // If the 1st element of moveString is neither a "S" nor a "T" return false
        // if the 2nd element is not a whitespace return false
        // if the 3rd and/or 4th element (as long as it is before ",") are not
        // digits, return false
        // if the 6th and/or 7th element (as long as it is after ",") are not digits,
        // return false
    }

    // endregion

    // region Distribute resources
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

    public static String distributeResources(String stateString) {
        State state = new State(stateString);
        state.distributeResources();
        return state.toString();
    }

    // endregion

    // region Check and generate moves
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

        int numberOfPlayer; // Number of player
        String playerId = ""; // Player ID
        String pStatePlayerId; // the current Player's move ID
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
                case "a" -> {
                    boardHeight = Integer.parseInt(parseSplit[1]);
                    String playerAmount = parseSplit[2];
                    numberOfPlayer = Integer.parseInt(playerAmount);
                    switch (numberOfPlayer) {
                        case 4 -> numberOfSettlersPerPlayer = 20;
                        case 3 -> numberOfSettlersPerPlayer = 25;
                        case 2 -> numberOfSettlersPerPlayer = 30;
                    }
                }

                // Get the player ID and Current Phase from here
                // Phase Exploration or Settlement
                case "c" -> {
                    playerId = parseSplit[1];
                    currentPhase = parseSplit[2];
                }

                // Get the Land coords (Island Coords)
                case "i" -> coordsContainer.addAll(Arrays.asList(parseSplit).subList(2, parseSplit.length));
                case "p" -> {
                    // Check if there's enough pieces left for that player that is moving
                    pStatePlayerId = parseSplit[1];

                    // Collecting the settler Coords that has been placed
                    for (int i = 9; i < parseSplit.length; i++) {
                        while (!parseSplit[i].equals("T")) {
                            settlerCoords.add(parseSplit[i]); // Store all the settler coords

                            // If the current player ID is the same as the placed settler's player ID
                            // Store it into array
                            if (pStatePlayerId.equals(playerId)) playerSettlerCoords.add(parseSplit[i]);
                            i++;
                        }

                        // If the current player ID is the same as the placed settler's player ID
                        // iterate the settlerCounter
                        if (pStatePlayerId.equals(playerId)) settlerCounter = playerSettlerCoords.size();
                        i++;

                        // Collecting the village coords that has been placed
                        while (i < parseSplit.length) {
                            if (pStatePlayerId.equals(playerId)) villageCounter = i - 9 - settlerCounter;
                            villageCoords.add(parseSplit[i]); // Store all the village Coords

                            // If the current player ID is the same as the placed Village's player ID
                            // Store it into array
                            if (pStatePlayerId.equals(playerId)) playerVillageCoords.add(parseSplit[i]);
                            i++;
                        }
                        if (pieceType.equals("S") && settlerCounter + 1 > numberOfSettlersPerPlayer) return false;
                        else if (pieceType.equals("T") && villageCounter + 1 > numberOfVillagesPerPlayer) return false;
                    }
                }
            }
        }

        // out of bound for height
        if(yMoveCoords > boardHeight - 1) return false;

        // if it's even rows, check the number of cols for out of bound (i.e. the width)
        if(yMoveCoords % 2 == 0 && xMoveCoords > boardHeight - 2) return false;
        else if(xMoveCoords > boardHeight - 1) return false;

                            // For Exploration Phase and or Settlement Phase
        switch (currentPhase) {
            // Exploration Phase
            case "E" -> {
                // If the move Coords is an occupied space, return false;
                if (settlerCoords.contains(moveCoords) || villageCoords.contains(moveCoords)) return false;

                // If the Village is being placed on the sea return false
                if (pieceType.equals("T") && !coordsContainer.contains(moveCoords)) return false;


                // if the village is placed on Land and it's not adjacent to any
                // of the pieces return false
                if (pieceType.equals("T") && (!isAdjacent(moveCoords, playerVillageCoords) &&
                        !isAdjacent(moveCoords, playerSettlerCoords))) return false;


                // If settler is on land and it's not adjacent to any of the pieces
                // return false
                if (pieceType.equals("S") && coordsContainer.contains(moveCoords)) {
                    if (!isAdjacent(moveCoords, playerSettlerCoords) &&
                            !isAdjacent(moveCoords, playerVillageCoords)) return false;
                }
            }

            // Settlement Phase
            case "S" -> {
                // If the move coord is an occupied space, return false;
                if (settlerCoords.contains(moveCoords)) return false;
                if (villageCoords.contains(moveCoords)) return false;

                // As the only move is for the settler, the village is false
                if (pieceType.equals("T")) return false;

                // if the settler is not adjacent with any of the pieces return false
                if (!isAdjacent(moveCoords, playerSettlerCoords) &&
                        !isAdjacent(moveCoords, playerVillageCoords)) return false;
            }
        }
        return true;
    }

    public static boolean isAdjacent(String centerCoords, ArrayList<String> coordsContainer) {
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
     * Given a state string, generate a set containing all move strings playable
     * by the current player.
     * <p>
     * A move is playable if it is valid.
     *
     * @param stateString a string representing a game state
     * @return a set of strings representing all moves the current player can play
     */
    public static Set<String> generateAllValidMoves(String stateString) {

        // Create a state object
        State state = new State(stateString);

        // Get information from the state string
        int numPlayers = state.getNumPlayers();
        char gamePhase = state.getCurrentPhase();

        // Get the board size
        int boardHeight = state.boardHeight;

        // Create a set to store all possible moves
        Set<String> allMoves = new HashSet<>();

        // Calculate number of pieces each player starts with
        int startNumSettlers = switch (numPlayers) {
            case 2 -> 30;
            case 3 -> 25;
            case 4 -> 20;
            default -> 0;
        };


        // Check if the player has placed all their settlers or villages
        boolean hasSettler = (state.getCurrentPlayer().getSettlers().length < startNumSettlers);
        boolean hasVillage = (state.getCurrentPlayer().getVillages().length < 5);

        if (!hasSettler && !(hasVillage && gamePhase == 'E')) return allMoves;

        // Add used coords
        ArrayList<String> settlerCoords = new ArrayList<>(); // Placed Settler Coordinates
        ArrayList<String> villageCoords = new ArrayList<>(); // Placed villages coordinates
        ArrayList<String> playerSettlerCoords = new ArrayList<>(); // The current Player's settler coords
        ArrayList<String> playerVillageCoords = new ArrayList<>(); // The current Player's Village coords

        for (int i = 0; i < numPlayers; i++){
           for (Coord c: state.getPlayer(i).getSettlers()){
                settlerCoords.add(c.toString());
           }
           for (Coord c: state.getPlayer(i).getVillages()){
                villageCoords.add(c.toString());
           }
        }

        for (Coord c: state.getCurrentPlayer().getSettlers()){
            playerSettlerCoords.add(c.toString());
        }
        for (Coord c: state.getCurrentPlayer().getVillages()){
            playerVillageCoords.add(c.toString());
        }

        // Get the coordinates of the islands
        ArrayList<String> coordsContainer = new ArrayList<>();

        for (Island island : state.getIslands()) {
            for (Coord c:island.getCoords()){
                coordsContainer.add(c.toString());
            }
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
            // Make sure the coordinate is not already used
            if(settlerCoords.contains(cord)) continue;
            if(villageCoords.contains(cord)) continue;
            // Make sure the coordinate is in bounds
            int y = Integer.parseInt(cord.split(",")[1]);
            if(Integer.parseInt(cord.substring(0,cord.indexOf(','))) % 2 == 0) {
                if(y > boardHeight - 2) continue;
            }
            else if(y > boardHeight - 1) continue;
            switch (gamePhase) {
                case 'E' -> {
                    if (!coordsContainer.contains(cord)) {
                        if (hasSettler) allMoves.add("S " + cord);
                        break;
                    }
                    // If the Village is being placed on the sea return false
                    if ((isAdjacent(cord, playerVillageCoords) || isAdjacent(cord, playerSettlerCoords))) {
                        // Add the move to the set
                        if (hasVillage) allMoves.add("T " + cord);
                        if (hasSettler) allMoves.add("S " + cord);
                    }
                }
                // Settlement Phase
                case 'S' -> {
                    // if the settler is not adjacent with any of the pieces return false
                    if ((isAdjacent(cord, playerVillageCoords) || isAdjacent(cord, playerSettlerCoords))) {
                        // Add the move to the set
                        allMoves.add("S " + cord);
                    }
                }
            }
        }
        return allMoves;
    }

    // endregion

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
         State state = new State(stateString);
         return state.isPhaseOver();
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
        State state = new State(stateString);
        char pieceType = moveString.charAt(0);
        String coordStr = moveString.substring(2);
        int y = Integer.parseInt(coordStr.split(",")[0]);
        int x = Integer.parseInt(coordStr.split(",")[1]);
        Coord coord = new Coord(y, x);
        state.placePiece(coord, pieceType);
        return state.toString();
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

    public static int[] calculateTotalIslandsScore(String stateString) {
        State state = new State(stateString);
        int[] scores = new int[state.getNumPlayers()];
        for (int i = 0; i < state.getNumPlayers(); i++) {
            scores[i] = state.scoreTotalIslands(i);
        }
        return scores;
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
        State state = new State(stateString);
        int[] scores = new int[state.getNumPlayers()];

        for (int i = 0; i < state.getNumPlayers(); i++) {
            scores[i] = state.scoreLinks(i);
        }

        return scores;
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
        State state = new State(stateString);
        int[] scores = new int[state.getNumPlayers()];
        for (int i = 0; i < state.getNumPlayers(); i++) {
            scores[i] = state.scoreMajorities(i);
        }
        return scores;
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
        State state = new State(stateString);
        int[] scores = new int[state.getNumPlayers()];
        for (int i = 0; i < state.getNumPlayers(); i++) {
            scores[i] = state.scoreResources(i)+state.scoreStatuettes(i);
        }
        return scores;
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
        State state = new State(stateString);
        int[] scores = new int[state.getNumPlayers()];
        for (int i = 0; i < state.getNumPlayers(); i++) {
            scores[i] = state.createScore(i);
        }
        return scores;
    }
    // endregion

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
         State state = new State(stateString);
         state.scorePhase();
         if (state.getCurrentPhase() == 'E') {
             state.cleanBoard();
             state.distributeResources();
             state.nextPhase();
         }
         return state.toString();
    }

    // 2 phases, exploration and settlement
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
        State state = new State(stateString);
        char pieceType = moveString.charAt(0);
        String coordStr = moveString.substring(2);
        int y = Integer.parseInt(coordStr.split(",")[0]);
        int x = Integer.parseInt(coordStr.split(",")[1]);
        Coord coord = new Coord(y, x);

        // if the move is valid, place it
        if ( isMoveValid(stateString, moveString)) state.placePiece(coord, pieceType);

        // if the move ends the phase
        if (state.isPhaseOver()){

            // Applying end of Phase rules
            // For Exploration Phase
            // Tally up the score, clean the board, distribute resources, change to next Phase
            if (state.getCurrentPhase() == 'E') {
                state.scorePhase();
                state.cleanBoard();
                state.distributeResources();
                state.nextPhase();
            }

            // For Settlement Phase
            // Tally up the score
            else if (state.getCurrentPhase() == 'S') {
                state.scorePhase();
            }
        }

        // After the endPhase is over, move to the next player
        state.nextPlayer();

        // if the current player cannot play, go to the next player
        int players = state.getNumPlayers();
        while (!state.getCurrentPlayer().canPlay(state)) {
            if (players == 1) break;
            state.nextPlayer();
            players--;
        }

        return state.toString();
    }

    // // Tally up the score
    //            else if (state.getCurrentPhase() == 'S') {
    //                state.scorePhase();
    //            }
    //        }
    //
    //        // After the endPhase is over, move to the next player
    //         state.nextPlayer();
    //
    //        // if the current player cannot play the move, move to the next player
    //        if (!state.getCurrentPlayer().canPlay(state)) state.nextPlayer();
    //
    //        return state.toString();
    //    }
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
        State state = new State(stateString);
        return state.getCurrentPlayer().createAIMove(state);
    }
}
