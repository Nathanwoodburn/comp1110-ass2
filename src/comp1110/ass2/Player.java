package comp1110.ass2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import static comp1110.ass2.BlueLagoon.isAdjacent;

/**
 * Player class
 * This class is used to store the information of a player
 * This includes the player's ID, score, resources, settlers and villages
 */
public class Player {

    // region Setup
    final int playerID;
    private int score;
    private int numCoconuts;
    private int numBamboo;
    private int numWater;
    private int numPreciousStones;
    private int numStatuette;
    private Coord[] settlers;
    private Coord[] villages;
    private Coord lastMove;
    private Boolean isAI;

    /**
     * Constructor for Player class
     * This creates a player with a player ID and initialises the player's score, resources, settlers and villages
     * @param playerID int player ID
     */
    public Player(int playerID) {
        this.playerID = playerID;
        this.score = 0;
        this.numCoconuts = 0;
        this.numBamboo = 0;
        this.numWater = 0;
        this.numPreciousStones = 0;
        this.numStatuette = 0;
        this.settlers = new Coord[0];
        this.villages = new Coord[0];
        this.isAI = false;
        lastMove = new Coord(-1, -1);
    }
    // endregion
    // region Getters and Setters

    /**
     * Get the player's ID
     * @return int player ID
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Get the player's score
     * @return int player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add a score to the player's score
     * @param score int score to be added
     */
    public void addScore(int score) {
        this.score += score;
    }

    /**
     * Get the number of a resource the player has of a type
     * @param resourceType char resource type
     * @return int number of the resource the player has
     */
    public int getNumResource(char resourceType) {
        return switch (resourceType) {
            case 'C' -> numCoconuts;
            case 'B' -> numBamboo;
            case 'W' -> numWater;
            case 'P' -> numPreciousStones;
            case 'S' -> numStatuette;
            default -> 0;
        };
    }

    /**
     * Add a resource to the player's resources
     * @param numResource int number of the resource to be added
     * @param resourceType char resource type
     */
    public void addResource(int numResource, char resourceType) {
        switch (resourceType) {
            case 'C' -> numCoconuts += numResource;
            case 'B' -> numBamboo += numResource;
            case 'W' -> numWater += numResource;
            case 'P' -> numPreciousStones += numResource;
            case 'S' -> numStatuette += numResource;
        }
    }

    /**
     * Remove all resources from the player's resources
     */
    public void removeResources() {
        numCoconuts = 0;
        numBamboo = 0;
        numWater = 0;
        numPreciousStones = 0;
        numStatuette = 0;


    }

    /**
     * Get the player's settlers
     * @return Coord[] list of the player's settlers coords
     */
    public Coord[] getSettlers() {
        return settlers;
    }

    /**
     * Get the player's villages
     * @return Coord[] list of the player's villages coords
     */
    public Coord[] getVillages() {
        return villages;
    }

    /**
     * Add settlers to the player's settlers
     * @param coord Coord coord of the settler to be added
     */
    public void addSettler(Coord coord) {
        Coord[] newSettlers = new Coord[settlers.length + 1];
        System.arraycopy(settlers, 0, newSettlers, 0, settlers.length);
        newSettlers[settlers.length] = coord;
        settlers = newSettlers;
        lastMove = coord;
    }

    /**
     * Add a village to the player's villages
     * @param coord Coord coord of the village to be added
     */
    public void addVillage(Coord coord) {
        // Check if the player already has the maximum number of villages
        if (villages.length >= 5) {
            return;
        }

        Coord[] newVillages = new Coord[villages.length + 1];
        System.arraycopy(villages, 0, newVillages, 0, villages.length);
        newVillages[villages.length] = coord;
        villages = newVillages;
        lastMove = coord;
    }

    /**
     * Delete all settlers
     */
    public void clearSettlers() {
        settlers = new Coord[0];
    }

    /**
     * Delete village
     */
    public void removeVillage(Coord coord) {
        Coord[] newVillages = new Coord[villages.length - 1];
        int j = 0;
        for (Coord village : villages) {
            if (village != coord) {
                newVillages[j] = village;
                j++;
            }
        }
        villages = newVillages;
    }


    /**
     * Get all the player's piece's coords
     * @return coord[] list of all the player's piece's coords
     */
    public Coord[] getPieces() {
        Coord[] pieces = new Coord[settlers.length + villages.length];
        System.arraycopy(settlers, 0, pieces, 0, settlers.length);
        System.arraycopy(villages, 0, pieces, settlers.length, villages.length);
        return pieces;
    }

    /**
     * Get number of pieces on island
     * @param island Island to check
     * @return int number of pieces on island
     */
    public int getNumPiecesOnIsland(Island island) {
        int numPieces = 0;
        for (Coord piece : getPieces()) {
            if (island.containsCoord(piece)) {
                numPieces++;
            }
        }
        return numPieces;
    }

    /**
     * Get the player's last move coord
     * @return Coord last move coord
     */
    public Coord getLastMove() {
        return lastMove;
    }

    /**
     * Set if the player is an AI
     * @param ai boolean true if player is an AI, false otherwise
     */
    public void setAI(boolean ai) {
        this.isAI = ai;
    }

    /**
     * Check if the player is an AI
     * @return boolean true if player is an AI, false otherwise
     */
    public boolean isAI() {
        return isAI;
    }


    /**
     * Check if player is able to do any moves
     * @return true if player can do any moves, false otherwise
     */
    public boolean canPlay(State state) {

        // Check if the player has placed all their settlers or villages
        int numSettlers = 30 - ((state.getNumPlayers() - 2) * 5);
        boolean hasSettler = (settlers.length < numSettlers);
        boolean hasVillage = (villages.length < 5);
        if (!hasSettler && !hasVillage) return false;
//        if (state.getCurrentPhase() != 'E'){
//            if (!hasSettler && !hasVillage) return false;
//        }


        // Add used coords
        ArrayList<String> settlerCoords = new ArrayList<>(); // Placed Settler Coordinates
        ArrayList<String> villageCoords = new ArrayList<>(); // Placed villages coordinates
        ArrayList<String> playerSettlerCoords = new ArrayList<>(); // The current Player's settler coords
        ArrayList<String> playerVillageCoords = new ArrayList<>(); // The current Player's Village coords

        for (int i = 0; i < state.getNumPlayers(); i++){
            for (Coord c: state.getPlayer(i).getSettlers()){
                settlerCoords.add(c.toString());
            }
            for (Coord c: state.getPlayer(i).getVillages()){
                villageCoords.add(c.toString());
            }
        }
        for (Coord c: settlers){
            playerSettlerCoords.add(c.toString());
        }
        for (Coord c: villages){
            playerVillageCoords.add(c.toString());
        }

        // Get the coordinates of the islands
        ArrayList<String> islandCoords = new ArrayList<>();

        for (Island island : state.getIslands()) {
            for (Coord c:island.getCoords()){
                islandCoords.add(c.toString());
            }
        }

        // Generate all possible coordinates in an array
        String[] coordinates = new String[state.boardHeight * state.boardHeight];
        int index = 0;
        for (int i = 0; i < state.boardHeight; i++){
            for (int j = 0; j < state.boardHeight; j++){
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
                if(y > state.boardHeight - 2) continue;
            }
            else if(y > state.boardHeight - 1) continue;
            switch (state.getCurrentPhase()) {
                case 'E' -> {
                    if (!islandCoords.contains(cord) && hasSettler) return true;
                    if ((isAdjacent(cord, playerVillageCoords) || isAdjacent(cord, playerSettlerCoords))) return true;
                }
                // Settlement Phase
                case 'S' -> {
                    // if the settler is adjacent with any of the pieces return true
                    if ((isAdjacent(cord, playerVillageCoords) || isAdjacent(cord, playerSettlerCoords)) && hasSettler) return true;
                }
            }
        }
        return false;
    }

    // endregion

    // region Auto Player
    /**
     * Do a Random Move
     * @param state State to do the move on
     */
    public void doRandomMove(State state) {
        if (state.getCurrentPlayer() != this) {
            System.out.println("Not this player's turn");
            return;
        }
        Set<String> validMoves = BlueLagoon.generateAllValidMoves(state.toString());
        if (validMoves.size() == 0) {
            return;
        }
        Random rand = new Random();
        int randomMove = rand.nextInt(0, validMoves.size());
        int i = 0;
        for (String move : validMoves) {
            if (i == randomMove) {
                char pieceType = move.charAt(0);
                String coordStr = move.substring(2);
                int y = Integer.parseInt(coordStr.split(",")[0]);
                int x = Integer.parseInt(coordStr.split(",")[1]);
                Coord coord = new Coord(y, x);
                lastMove = coord;
                state.placePiece(coord, pieceType);
                state.nextPlayer();
                return;
            }
            i++;
        }
    }

    /**
     * Do a calculated move
     * @param state State to do the move on
     */
    public String createAIMove(State state){
        if (state.getCurrentPlayer() != this){
            System.out.println("Not this player's turn");
            return "";
        }
        Set<String> validMoves = BlueLagoon.generateAllValidMoves(state.toString());
        if (validMoves.size() == 0){
            System.out.println("No valid moves");
            return "";
        }
        int islandCount = 0;
        for (Island island : state.getIslands()) {
            if (this.getNumPiecesOnIsland(island)>0) islandCount++;
        }

        String bestMove = "";
        int bestScore = -1;
        for (String move : validMoves){
            int score = calculateMoveScore(state, move,islandCount);
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Do a calculated move
     */
    public Boolean doAIMove(State state){

        String bestMove = createAIMove(state);
        if (bestMove.equals("")){
            System.out.println("No AI moves");
            return false;
        }
        char pieceType = bestMove.charAt(0);
        String coordStr = bestMove.substring(2);
        int y = Integer.parseInt(coordStr.split(",")[0]);
        int x = Integer.parseInt(coordStr.split(",")[1]);
        Coord coord = new Coord(y, x);
        lastMove = coord;
        state.placePiece(coord, pieceType);
        state.nextPlayer();
        return true;
    }

    /**
     * Calculate the score of a move
     * The score is calculated only with public information
     * The score is calculated by:
     * 1. If the move gives the player some resources (10 points)
     * 2. If the move gives the player a new island (1 point) or if below rules are met don't give a point
     * 3. If this move makes the player have the pieces on 7-8 islands (10-20 points)
     * 4. If this move makes the player have the most pieces on an island (island bonus points)
     *
     * @param state State to do the move on
     * @param move String move to calculate the score of
     * @return int score of the move
     */
    public int calculateMoveScore(State state, String move, int currentIslandCount){
        int score = 0;
        String coordStr = move.substring(2);
        int y = Integer.parseInt(coordStr.split(",")[0]);
        int x = Integer.parseInt(coordStr.split(",")[1]);
        Coord coord = new Coord(y, x);

        // Check if this will give player some resources
        if (state.isStone(coord)){
            score += 10;
        }

        // Check if this is a new island or will make player have the most pieces on the island
        for (Island island:state.getIslands()) {
            if (!island.containsCoord(coord)) continue;
            if (this.getNumPiecesOnIsland(island) == 0) {
                if (currentIslandCount == 7) score += 20;
                else if (currentIslandCount == 6) score += 10;
                else score += 1;
            }
            // Check if adding this piece will make player have the most pieces on the island
            int ties = 0;
            int loses = 0;
            int myPieces = this.getNumPiecesOnIsland(island);
            for (int i = 0; i < state.getNumPlayers(); i++) {
                if (i == this.getPlayerID()) {
                    continue;
                }
                int otherPlayerPieces = state.getPlayer(i).getNumPiecesOnIsland(island);
                if (otherPlayerPieces > myPieces+1) {
                    loses++;
                } else if (otherPlayerPieces == myPieces + 1) {
                    ties++;
                }
            }

            if (loses == 0){
                if (ties > 0){
                    score += island.getBonus()/(ties+1);
                }
                else{
                    score += island.getBonus();
                }
            }
        }

        // Check if player needs more islands
        if (score == 0) {
            if (currentIslandCount < 8 ){
                // Check if there is an island adjacent to this move
                for (Island island : state.getIslands()) {
                    if (island.containsCoord(new Coord(coord.x() + 1, coord.y()))
                            || island.containsCoord(new Coord(coord.x() - 1, coord.y()))
                            || island.containsCoord(new Coord(coord.x(), coord.y() + 1))
                            || island.containsCoord(new Coord(coord.x(), coord.y() - 1))) {
                        score += 1;
                        if (state.getCurrentPhase() == 'E'){
                            score += 4;
                        }
                    }
                }

            }
        }
        return score;
    }

    // endregion

    /**
     * Get the largest column of the player's pieces
     * @param coords Coord[] list of the player's pieces
     * @return int largest column
     */
    private int maxCol(Coord[] coords){
        int maxCol = 0;
        for (Coord coord : coords) {
            if (coord.x() > maxCol) {
                maxCol = coord.x();
            }
        }
        return maxCol;
    }


    /**
     * Check if player is equal to another player
     * @param player Player player to be compared to
     * @return true if the players are equal, false otherwise
     */
    public boolean equals(Player player) {
        if (player.getPlayerID() != playerID) return false;
        if (player.getScore() != score) return false;
        if (player.getNumResource('C') != numCoconuts) return false;
        if (player.getNumResource('B') != numBamboo) return false;
        if (player.getNumResource('W') != numWater) return false;
        if (player.getNumResource('P') != numPreciousStones) return false;
        if (player.getNumResource('S') != numStatuette) return false;
        if (player.getSettlers().length != settlers.length) return false;
        if (player.getVillages().length != villages.length) return false;

        for (int i = 0; i < settlers.length; i++) {
            if (!player.getSettlers()[i].equals(settlers[i])) return false;
        }
        for (int i = 0; i < villages.length; i++) {
            if (!player.getVillages()[i].equals(villages[i])) return false;
        }
        return true;
    }

    /**
     * Get the string representation of the player
     * @return String representation of the player in playerString format
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("p " + playerID + " " + score + " " + numCoconuts + " " + numBamboo + " " + numWater + " " + numPreciousStones + " " + numStatuette + " S");


        // Get the coords of the player's pieces in row major order
        Coord[] settlersCoords = new Coord[settlers.length];
        if (settlers.length != 0) {
            int row = 0;
            int col = 0;
            int i = 0;
            int maxcol = maxCol(settlers);

            while (settlersCoords[settlers.length - 1] == null) {
                for (Coord coord : settlers) {
                    if (coord.x() == col && coord.y() == row) {
                        settlersCoords[i] = coord;
                        i++;
                    }
                }
                col++;
                if (col > maxcol) {
                    col = 0;
                    row++;
                }
            }
        }

        Coord[] villagesCoords = new Coord[villages.length];
        if (villages.length != 0) {
            int row = 0;
            int col = 0;
            int i = 0;
            int maxcol = maxCol(villages);

            while (villagesCoords[villages.length-1] == null){
                for (Coord coord : villages) {
                    if (coord.x() == col && coord.y() == row) {
                        villagesCoords[i] = coord;
                        i++;
                    }
                }
                col++;
                if (col > maxcol) {
                    col = 0;
                    row++;
                }
            }
        }




        for (Coord coord : settlersCoords) {
            str.append(" ").append(coord.toString());

        }
        str.append(" T");
        for (Coord coord : villagesCoords) {
            str.append(" ").append(coord.toString());
        }
        return str.toString();
    }
}
