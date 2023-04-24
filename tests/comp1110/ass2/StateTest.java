package comp1110.ass2;

import comp1110.ass2.testdata.GameDataLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * This class is used to test the state Class.
 *
 */
public class StateTest {
    @Test
    public void testStateDEFAULTGAME() {
        State state = new State(GameDataLoader.DEFAULT_GAME);
        Assertions.assertEquals(13, state.getBoardHeight(), "The test failed because the board length is not 13");
        Assertions.assertEquals(2, state.getNumPlayers(), "The test failed because the number of players is not 2");
        Assertions.assertEquals(0, state.getCurrentPlayerID(), "The test failed because the current player is not 0");
        Assertions.assertEquals('E', state.getCurrentPhase(), "The test failed because the current phase is not E");

        Assertions.assertEquals(GameDataLoader.DEFAULT_GAME, state.toString(), "The test failed because the state created from the string is not the same as the state created from the string");

        // Creating test island for the string
        // i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1;
        Island island = new Island(6);
        island.addCoord(new Coord(0, 0));
        island.addCoord(new Coord(0, 1));
        island.addCoord(new Coord(0, 2));
        island.addCoord(new Coord(0, 3));
        island.addCoord(new Coord(1, 0));
        island.addCoord(new Coord(1, 1));
        island.addCoord(new Coord(1, 2));
        island.addCoord(new Coord(1, 3));
        island.addCoord(new Coord(1, 4));
        island.addCoord(new Coord(2, 0));
        island.addCoord(new Coord(2, 1));
        Assertions.assertTrue(island.equals(state.getIsland(0)), "The test failed because the island at index 0 is not the same as the island created above");
        island.addCoord(new Coord(2, 2));
        Assertions.assertFalse(island.equals(state.getIsland(0)), "The test failed because the island at index 0 is the same as the island created above even though it should not be because the island created above has 11 coords while the island at index 0 has 12 coords");
        Assertions.assertTrue(state.isStone(new Coord(0, 0)), "The test failed because the stone at (0,0) is not a stone");
        Assertions.assertFalse(state.isStone(new Coord(0, 1)), "The test failed because the stone at (0,5) is a stone even though it should not be because there is no stone at that location");

        // Creating a test player for the string
        // p 0 0 0 0 0 0 0 S T;
        Player player = new Player(0);
        Assertions.assertTrue(player.equals(state.getPlayer(0)), "The test failed because the player at index 0 is not the same as the player created above");
        player.addScore(1);
        Assertions.assertFalse(player.equals(state.getPlayer(0)), "The test failed because the player at index 0 is the same as the player created above even though it should not be because the player created above has a score of 1 while the player at index 0 has a score of 0");
        player = new Player(0);
        player.addVillage(new Coord(0, 0));
        Assertions.assertFalse(player.equals(state.getPlayer(0)), "The test failed because the player at index 0 is the same as the player created above even though it should not be because the player created above has a village at (0,0) while the player at index 0 has no villages");
        player = new Player(0);
        player.addResource(1, 'C');
        Assertions.assertFalse(player.equals(state.getPlayer(0)), "The test failed because the player at index 0 is the same as the player created above even though it should not be because the player created above has a resource of 1 for C while the player at index 0 has a resource of 0 for C");

        // Distribute resources
        state.distributeResources();
        Resource toClaim = state.getUnclaimedResource(new Coord(0, 0));

        // Place a settler at (0,0)
        state.placePiece(new Coord(0, 0), 'S');
        // Get the island score
        Assertions.assertEquals(6, state.scoreMajorities(0), "The test failed because the score of the islands for player 0 is not 34");
        Assertions.assertEquals(0, state.scoreMajorities(1), "The test failed because the score of the islands for player 1 is not 28");
        Assertions.assertEquals(0, state.scoreTotalIslands(0), "The test failed because the score of the islands for player 0 is not 0");
        Assertions.assertEquals(1, state.getPlayer(0).getNumResource(toClaim.getType()), "The test failed because the number of resources for player 0 is not 1");
    }

    /**
     * Run Sim on AI vs Random
     *
     * @return true if AI wins, false if Random wins
     */
    public boolean simulateGame(State state) {
        while (!state.isPhaseOver()) {
            if (!state.getCurrentPlayer().canPlay(state)) {
                System.out.println("Player " + state.getCurrentPlayerID() + " can't play");
                state.nextPlayer();
            }
            if (state.getCurrentPlayerID() == 0) {
                state.getCurrentPlayer().doAIMove(state);
            } else {
                state.getCurrentPlayer().doRandomMove(state);
            }
        }
        state.scorePhase();
        int P0 = state.getPlayer(0).getScore();
        int P1 = state.getPlayer(1).getScore();
        if (P0 > P1) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void testAIDominance() {


        State Startstate;

        for (String map : GameDataLoader.MAP_NAMES) {
            Startstate = new State(GameDataLoader.readMap(map));
            Startstate.distributeResources();

            int numGames = 5;
            int numWins = 0;
            for (int i = 0; i < numGames; i++) {
                State state = new State(Startstate.toString());
                if (simulateGame(state)) {
                    numWins++;
                }
            }
            System.out.println("AI won " + numWins + " out of " + numGames + " games on the " + map + " map");
        }
    }

    @Test
    public void testMorePlayers() {
        State state = new State(GameDataLoader.DEFAULT_GAME);
        state.addPlayer();

        Assertions.assertEquals("a 13 3; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T; p 2 0 0 0 0 0 0 S T;", state.toString(), "The test failed because the state is not the same as the state created above");

        state.addPlayer();
        Assertions.assertEquals("a 13 4; c 0 E; i 6 0,0 0,1 0,2 0,3 1,0 1,1 1,2 1,3 1,4 2,0 2,1; i 6 0,5 0,6 0,7 1,6 1,7 1,8 2,6 2,7 2,8 3,7 3,8; i 6 7,12 8,11 9,11 9,12 10,10 10,11 11,10 11,11 11,12 12,10 12,11; i 8 0,9 0,10 0,11 1,10 1,11 1,12 2,10 2,11 3,10 3,11 3,12 4,10 4,11 5,11 5,12; i 8 4,0 5,0 5,1 6,0 6,1 7,0 7,1 7,2 8,0 8,1 8,2 9,0 9,1 9,2; i 8 10,3 10,4 11,0 11,1 11,2 11,3 11,4 11,5 12,0 12,1 12,2 12,3 12,4 12,5; i 10 3,3 3,4 3,5 4,2 4,3 4,4 4,5 5,3 5,4 5,5 5,6 6,3 6,4 6,5 6,6 7,4 7,5 7,6 8,4 8,5; i 10 5,8 5,9 6,8 6,9 7,8 7,9 7,10 8,7 8,8 8,9 9,7 9,8 9,9 10,6 10,7 10,8 11,7 11,8 12,7 12,8; s 0,0 0,5 0,9 1,4 1,8 1,12 2,1 3,5 3,7 3,10 3,12 4,0 4,2 5,9 5,11 6,3 6,6 7,0 7,8 7,12 8,2 8,5 9,0 9,9 10,3 10,6 10,10 11,0 11,5 12,2 12,8 12,11; r C B W P S; p 0 0 0 0 0 0 0 S T; p 1 0 0 0 0 0 0 S T; p 2 0 0 0 0 0 0 S T; p 3 0 0 0 0 0 0 S T;", state.toString(), "The test failed because the state is not the same as the state created above");
    }
}
