package comp1110.ass2;

/**
 * Player class
 * This class is used to store the information of a player
 * This includes the player's ID, score, resources, settlers and villages
 */
public class Player {
    final int playerID;
    private int score;
    private int numCoconuts;
    private int numBamboo;
    private int numWater;
    private int numPreciousStones;
    private int numStatuette;
    private Coord[] settlers;
    private Coord[] villages;

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
    }

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
     * Remove a resource from the player's resources
     * @param numResource int number of the resource to be removed
     * @param resourceType char resource type
     */
    public void removeResource(int numResource, char resourceType) {
        switch (resourceType) {
            case 'C':
                numCoconuts -= numResource;
                break;
            case 'B':
                numBamboo -= numResource;
                break;
            case 'W':
                numWater -= numResource;
                break;
            case 'P':
                numPreciousStones -= numResource;
                break;
            case 'S':
                numStatuette -= numResource;
                break;
        }
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
}
