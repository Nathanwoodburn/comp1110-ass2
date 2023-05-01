package comp1110.ass2;

import java.util.Random;
import java.util.Set;

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
        switch (resourceType) {
            case 'C':
                return numCoconuts;
            case 'B':
                return numBamboo;
            case 'W':
                return numWater;
            case 'P':
                return numPreciousStones;
            case 'S':
                return numStatuette;
            default:
                return 0;
        }
    }

    /**
     * Add a resource to the player's resources
     * @param numResource int number of the resource to be added
     * @param resourceType char resource type
     */
    public void addResource(int numResource, char resourceType) {
        switch (resourceType) {
            case 'C':
                numCoconuts += numResource;
                break;
            case 'B':
                numBamboo += numResource;
                break;
            case 'W':
                numWater += numResource;
                break;
            case 'P':
                numPreciousStones += numResource;
                break;
            case 'S':
                numStatuette += numResource;
                break;
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
        for (int i = 0; i < settlers.length; i++) {
            newSettlers[i] = settlers[i];
        }
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
        for (int i = 0; i < villages.length; i++) {
            newVillages[i] = villages[i];
        }
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
        for (int i = 0; i < villages.length; i++) {
            if (villages[i] != coord) {
                newVillages[j] = villages[i];
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
        for (int i = 0; i < settlers.length; i++) {
            pieces[i] = settlers[i];
        }
        for (int i = 0; i < villages.length; i++) {
            pieces[settlers.length + i] = villages[i];
        }
        return pieces;
    }

    /**
     * Get number of pieces on island
     * @param island Island island to check
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

    public Coord getLastMove() {
        return lastMove;
    }

    // endregion

    /**
     * Check if player is able to do any moves
     * @return true if player can do any moves, false otherwise
     */
    public boolean canPlay(State state) {
        Set<String> validMoves = BlueLagoon.generateAllValidMoves(state.toString());
        return validMoves.size() > 0;
    }

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
                int x = Integer.parseInt(coordStr.split(",")[0]);
                int y = Integer.parseInt(coordStr.split(",")[1]);
                Coord coord = new Coord(x, y);
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
    public void doAIMove(State state){

        String bestMove = createAIMove(state);
        char pieceType = bestMove.charAt(0);
        String coordStr = bestMove.substring(2);
        int x = Integer.parseInt(coordStr.split(",")[0]);
        int y = Integer.parseInt(coordStr.split(",")[1]);
        Coord coord = new Coord(x, y);
        lastMove = coord;
        state.placePiece(coord, pieceType);
        state.nextPlayer();
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
            int wins = 0;
            int loses = 0;
            int myPieces = this.getNumPiecesOnIsland(island);
            for (int i = 0; i < state.getNumPlayers(); i++) {
                if (i == this.getPlayerID()) {
                    continue;
                }
                int otherPlayerPieces = state.getPlayer(i).getNumPiecesOnIsland(island);
                if (otherPlayerPieces > myPieces+1) {
                    loses++;
                } else
                if (otherPlayerPieces == myPieces) {
                    wins++;
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
                    if (island.containsCoord(new Coord(coord.getX() + 1, coord.getY()))
                            || island.containsCoord(new Coord(coord.getX() - 1, coord.getY()))
                            || island.containsCoord(new Coord(coord.getX(), coord.getY() + 1))
                            || island.containsCoord(new Coord(coord.getX(), coord.getY() - 1))) {
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
            if (coord.getX() > maxCol) {
                maxCol = coord.getX();
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

    @Override
    public String toString() {
        String str = "p " + playerID + " " + score + " " + numCoconuts + " " + numBamboo + " " + numWater + " " + numPreciousStones + " " + numStatuette + " S";


        // Get the coords of the player's pieces in row major order
        Coord[] settlersCoords = new Coord[settlers.length];
        if (settlers.length != 0) {
            int row = 0;
            int col = 0;
            int i = 0;
            int maxcol = maxCol(settlers);

            while (settlersCoords[settlers.length - 1] == null) {
                for (Coord coord : settlers) {
                    if (coord.getX() == col && coord.getY() == row) {
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
                    if (coord.getX() == col && coord.getY() == row) {
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
            str += " " + coord.toString();

        }
        str += " T";
        for (Coord coord : villagesCoords) {
            str += " " + coord.toString();
        }
        return str;
    }
}
