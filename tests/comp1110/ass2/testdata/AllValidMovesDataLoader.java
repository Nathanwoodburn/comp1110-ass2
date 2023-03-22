package comp1110.ass2.testdata;

import java.util.*;

public class AllValidMovesDataLoader extends DataLoader<Set<String>>{
    public AllValidMovesDataLoader(){
        super();
    }

    @Override
    protected String getFileName() {
        return "generateAllValidMoves";
    }

    @Override
    protected Set<String> processLine(String line) {
        line = line.substring(1,line.length()-1);
        if(line.length() > 0){
            return new HashSet<>(List.of(line.split(", ")));
        } else{
            return new HashSet<>();
        }
    }
}
