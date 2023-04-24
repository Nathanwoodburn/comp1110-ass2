package comp1110.ass2;

import comp1110.ass2.testdata.DataLoader;
import comp1110.ass2.testdata.GameDataLoader;
import comp1110.ass2.testdata.TotalIslandsScoreDataLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Timeout(value = 2000, unit = TimeUnit.MILLISECONDS)
public class CalculateTotalIslandsScoreTest implements TestMapNamePlayerCount {
    private final GameDataLoader gameDataLoader = new GameDataLoader();
    private final TotalIslandsScoreDataLoader totalIslandsScoreDataLoader = new TotalIslandsScoreDataLoader();

    private static void test(int[] expected, String input){
        int[] solution = BlueLagoon.calculateTotalIslandsScore(input);
        Assertions.assertArrayEquals(expected, solution, "failed on input: " + input + "\nexpected: "+ Arrays.toString(expected) + "\nactual: " + Arrays.toString(solution));
    }
    private static void testGame(List<String> game, List<List<Integer>> solutions){
        for (int i = 0; i < solutions.size(); i++) {
            List<Integer> solution = solutions.get(i);

            int[] expected = new int[solution.size()];
            for (int j = 0; j < solution.size(); j++) {
                expected[j] = solution.get(j);
            }

            test(expected, game.get(2*i));
        }
    }

    public void testMapNamePlayerCount(String mapName, int playerCount){
        List<List<String>> games = gameDataLoader.fetchGames(mapName, playerCount);
        List<List<List<Integer>>> solutions = totalIslandsScoreDataLoader.fetchGames(mapName, playerCount);
        for(int game = 0; game < DataLoader.GAME_COUNT; game++){
            testGame(games.get(game), solutions.get(game));
        }
    }

    @Test
    public void testDefaultTwoPlayerGames(){
        testMapNamePlayerCount("default", 2);
    }

    @Test
    public void testWheelsTwoPlayerGames(){
        testMapNamePlayerCount("wheels", 2);
    }

    @Test
    public void testFaceTwoPlayerGames(){
        testMapNamePlayerCount("face", 2);
    }

    @Test
    public void testEdgeCaseGames(){
        List<List<String>> games = gameDataLoader.fetchAllEdgeCaseGames();
        List<List<List<Integer>>> solutions = totalIslandsScoreDataLoader.fetchAllEdgeCaseGames();
        for(int game = 0; game < games.size(); game++){
            testGame(games.get(game), solutions.get(game));
        }
        test(new int[]{20, 20}, "a 13 2; c 1 E; i 6 0,0 0,2 1,1 1,2; i 6 0,4 0,6 1,5 1,6; i 6 0,8 0,10 1,9 1,10; i 6 1,12 2,11 3,12 4,11; i 6 3,0 3,2 4,0 4,1; i 6 3,4 3,6 4,4 4,5; i 6 3,8 3,10 4,8 4,9; i 6 8,0 8,1 9,0 9,2; i 6 8,4 8,5 9,4 9,6; i 6 8,8 8,9 9,8 9,10; i 6 8,11 9,12 10,11 11,12; i 6 11,0 11,2 12,0 12,1; i 6 11,4 11,6 12,4 12,5; i 6 11,8 11,10 12,8 12,9; i 10 6,0 6,1 6,2; i 10 6,4 6,5 6,6; i 10 6,8 6,9 6,10 6,11; s 0,0 0,2 0,4 0,6 0,8 0,10 1,12 3,0 3,4 3,10 4,1 4,5 4,8 4,11 6,1 6,5 6,8 6,11 8,1 8,5 8,8 8,11 9,0 9,4 9,10 11,0 11,2 11,4 11,6 11,8 11,10 11,12; r C 9,4 B 0,0 1,12 11,4 W 8,8 P 3,0 9,10 11,12 S 0,4 9,0 11,6 11,8; p 0 0 2 2 4 2 1 S 0,2 0,3 0,5 0,6 0,7 0,8 0,9 0,10 2,7 2,9 4,1 4,8 4,10 5,1 5,5 5,9 5,10 7,4 7,6 7,12 8,1 8,2 8,11 9,3 10,0 10,9 11,0 11,9 11,10 12,8 T 1,10 3,10 6,0 6,4 9,2; p 1 0 3 1 1 1 3 S 1,7 3,2 3,4 3,5 4,2 4,3 4,5 4,11 5,2 5,4 5,6 5,11 6,1 6,2 6,5 6,8 6,10 6,11 7,0 7,1 7,5 7,10 8,4 8,5 8,6 8,9 10,1 10,6 10,8 11,2 T 6,6 6,9 8,0 12,1;");
    }
}
