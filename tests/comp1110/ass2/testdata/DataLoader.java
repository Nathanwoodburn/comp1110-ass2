package comp1110.ass2.testdata;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DataLoader<T> {
    public static final int GAME_COUNT = 2;
    public static final String[] MAP_NAMES = {"default", "wheels", "face", "sides", "space_invaders"};
    public static final String[] NON_VARIABLE_SIZE = {"default", "wheels", "face"};
    public static final String[] VARIABLE_SIZE = {"sides", "space_invaders"};

    public final HashMap<MapNamePlayerCount, List<List<T>>> GAMES = new HashMap<>();
    public final HashMap<MapNamePlayerCount, List<List<T>>> EDGE_CASES = new HashMap<>();

    protected DataLoader(){
        for(String mapName : MAP_NAMES) {
            for(int playerCount = 2; playerCount <= 4; playerCount++) {
                MapNamePlayerCount key = new MapNamePlayerCount(mapName, playerCount);
                GAMES.put(key, new ArrayList<>(GAME_COUNT));
                for (int game = 0; game < GAME_COUNT; game++) {
                    GAMES.get(key).add(loadData(key, game, false));
                }
            }
        }

        for(String mapName : NON_VARIABLE_SIZE){
            MapNamePlayerCount key = new MapNamePlayerCount(mapName, 2);
            EDGE_CASES.put(key, new ArrayList<>(2));
            for (int game = 0; game < GAME_COUNT; game++) {
                EDGE_CASES.get(key).add(loadData(key, game, true));
            }
        }
    }

    public List<List<T>> fetchGames(String mapName, int playerCount){
        return GAMES.get(new MapNamePlayerCount(mapName, playerCount));
    }

    public List<List<T>> fetchGames(String mapName){
        List<List<T>> games = new ArrayList<>(GAME_COUNT * 3);
        for(int playerCount = 2; playerCount <= 4; playerCount++){
            games.addAll(GAMES.get(new MapNamePlayerCount(mapName, playerCount)));
        }
        return games;
    }

    public List<List<T>> fetchGames(int playerCount){
        List<List<T>> games = new ArrayList<>(GAME_COUNT * MAP_NAMES.length);
        for(String mapName : MAP_NAMES){
            games.addAll(GAMES.get(new MapNamePlayerCount(mapName, playerCount)));
        }
        return games;
    }

    public List<List<T>> fetchAllGames(){
        List<List<T>> games = new ArrayList<>(GAME_COUNT * MAP_NAMES.length * 3);
        for(String mapName : MAP_NAMES){
            for(int playerCount = 2; playerCount <= 4; playerCount++){
                games.addAll(GAMES.get(new MapNamePlayerCount(mapName, playerCount)));
            }
        }
        return games;
    }

    public List<List<T>> fetchAllEdgeCaseGames(){
        List<List<T>> games = new ArrayList<>(2 * NON_VARIABLE_SIZE.length);
        for(String mapName : NON_VARIABLE_SIZE){
            games.addAll(EDGE_CASES.get(new MapNamePlayerCount(mapName, 2)));
        }
        return games;
    }

    protected abstract String getFileName();
    protected abstract T processLine(String line);

    private List<T> loadData(MapNamePlayerCount mapNamePlayerCount, int gameNumber, boolean edgeCase){
        return loadResourceStream(findResource(mapNamePlayerCount, gameNumber, getFileName(), edgeCase ? "edge" : "game"));
    }

    private List<T> loadResourceStream(InputStream stream){
        try {
            BufferedReader reader;
            List<T> data = new ArrayList<>();

            reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = reader.readLine()) != null){
                data.add(processLine(line.strip()));
            }

            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream findResource(MapNamePlayerCount mapNamePlayerCount, int gameNumber, String filename, String prefix){
        return this.getClass().getResourceAsStream("data/" + gameName(mapNamePlayerCount, gameNumber, prefix) + "/" + filename + ".txt");
    }

    private static String gameName(MapNamePlayerCount mapNamePlayerCount, int gameNumber, String prefix){
        return prefix + "_" + mapNamePlayerCount.mapName + "_p" + mapNamePlayerCount.playerCount + "_" + gameNumber;
    }

    private record MapNamePlayerCount(String mapName, int playerCount){}
}
