package comp1110.ass2.testdata;

public class EndPhaseDataLoader extends DataLoader<String>{
    public EndPhaseDataLoader(){
        super();
    }

    @Override
    protected String getFileName() {
        return "endPhase";
    }

    @Override
    protected String processLine(String line) {
        return line;
    }
}
