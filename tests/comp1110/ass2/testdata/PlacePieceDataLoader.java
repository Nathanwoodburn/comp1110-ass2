package comp1110.ass2.testdata;


public class PlacePieceDataLoader extends DataLoader<String>{
    public PlacePieceDataLoader(){
        super();
    }

    @Override
    protected String getFileName() {
        return "placePiece";
    }

    @Override
    protected String processLine(String line) {
        return line;
    }
}
